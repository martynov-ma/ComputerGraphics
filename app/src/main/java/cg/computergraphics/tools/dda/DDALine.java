package cg.computergraphics.tools.dda;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;
import android.widget.Button;

import cg.computergraphics.MainActivity;
import cg.computergraphics.tools.DrawingTool;
import cg.computergraphics.tools.RenderingType;

/**
 * Created by MAX on 13.03.2017.
 */

public class DDALine extends DrawingTool {
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public DDALine(Bitmap mainBitmap, Bitmap fakeBitmap) {
        super(mainBitmap, fakeBitmap);
    }

    public void drawDDALine(float startX, float startY, float endX, float endY, Bitmap bitmap, RenderingType renderingType) {
        float x = startX;
        float y = startY;

        int N = Math.round(Math.max(Math.abs(endX - startX), Math.abs(endY - startY)));
        float delX = (endX - startX) / N;
        float delY = (endY - startY) / N;

        //color approximation
        int startColor = Color.rgb(255, 0, 0);
        int endColor = Color.rgb(0, 0, 255);
        float r1 = Color.red(startColor), g1 = Color.green(startColor), b1 = Color.blue(startColor);
        float r2 = Color.red(endColor), g2 = Color.green(endColor), b2 = Color.blue(endColor);

        float delR = (r2 - r1) / N;
        float delG = (g2 - g1) / N;
        float delB = (b2 - b1) / N;

        for (int i = 0; i < N; i++, x += delX, y += delY) {
            try {
                switch (renderingType) {
                    case SOLID:
                        bitmap.setPixel(Math.round(x), Math.round(y), super.getColor());
                        break;
                    case GRADIENT:
                        bitmap.setPixel(Math.round(x), Math.round(y), Color.rgb(Math.round(r1), Math.round(g1), Math.round(b1)));
                        r1 += delR;
                        g1 += delG;
                        b1 += delB;
                        break;
                    case ERASE:
                        bitmap.setPixel(Math.round(x), Math.round(y), 0);
                        break;
                }
            } catch (IllegalArgumentException ignored) {}
        }
    }

    public void drawDDALineWithAA(int startX, int startY, int endX, int endY, Bitmap bitmap, RenderingType renderingType) {
        if (renderingType == RenderingType.ERASE) {
            bitmap.eraseColor(0);
            return;
        }

        int dx = Math.abs(endX - startX);
        int dy = Math.abs(endY - startY);
        //Горизонтальные и вертикальные линии не нуждаются в сглаживании
        if (dx == 0 || dy == 0) {
            drawDDALine(startX, startY, endX, endY, bitmap, renderingType);
            return;
        }
        float gradient;
        if (dx > dy) {
            if (endX < startX) {
                startX += endX;
                endX = startX - endX;
                startX -= endX;
                startY += endY;
                endY = startY - endY;
                startY -= endY;
            }

            gradient = (float) dy / dx;
            if(endY < startY) gradient =- gradient;

            float interY = startY + gradient;
            bitmap.setPixel(startX, startY, super.getColor());
            for (int x = startX; x < endX; ++x) {
                bitmap.setPixel(x, (int) interY, Color.argb((int) (255 - fractionalPart(interY) * 255), Color.red(super.getColor()), Color.green(super.getColor()), Color.blue(super.getColor())));
                bitmap.setPixel(x, (int) interY + 1, Color.argb((int) (fractionalPart(interY) * 255), Color.red(super.getColor()), Color.green(super.getColor()), Color.blue(super.getColor())));
                interY += gradient;
            }
            bitmap.setPixel(endX, endY, super.getColor());
        } else {
            if (endY < startY) {
                startX += endX;
                endX = startX - endX;
                startX -= endX;
                startY += endY;
                endY = startY - endY;
                startY -= endY;
            }

            gradient = (float) dx / dy;
            if(endX < startX) gradient =- gradient;

            float interX = startX + gradient;
            bitmap.setPixel(startX, startY, super.getColor());
            for (int y = startY; y < endY; ++y) {
                bitmap.setPixel((int)interX, y, Color.argb((int) (255 - fractionalPart(interX) * 255), Color.red(super.getColor()), Color.green(super.getColor()), Color.blue(super.getColor())));
                bitmap.setPixel((int)interX + 1, y, Color.argb((int) (fractionalPart(interX) * 255), Color.red(super.getColor()), Color.green(super.getColor()), Color.blue(super.getColor())));
                interX += gradient;
            }
            bitmap.setPixel(endX, endY, super.getColor());
        }
    }

    private float fractionalPart(float number) {
        int tmp = (int) number;
        return number - tmp; //вернёт дробную часть числа
    }

    @Override
    public void onTouch(MotionEvent motionEvent){

        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = x;
                startY = y;
                endX = x;
                endY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                drawDDALineWithAA(startX, startY, endX, endY, super.getFakeBitmap(), RenderingType.ERASE);
                endX = x;
                endY = y;
                drawDDALineWithAA(startX, startY, x, y, super.getFakeBitmap(),
                        MainActivity.appSettings.isLineColorApprox() ? RenderingType.GRADIENT : RenderingType.SOLID);
                break;
            case MotionEvent.ACTION_UP:
                drawDDALineWithAA(startX, startY, x, y, super.getFakeBitmap(), RenderingType.ERASE);
                drawDDALineWithAA(startX, startY, x, y, super.getMainBitmap(),
                        MainActivity.appSettings.isLineColorApprox() ? RenderingType.GRADIENT : RenderingType.SOLID);
                break;
        }
    }
}
