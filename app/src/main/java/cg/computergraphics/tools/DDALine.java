package cg.computergraphics.tools;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;

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
                drawDDALine(startX, startY, endX, endY, super.getFakeBitmap(), RenderingType.ERASE);
                endX = x;
                endY = y;
                drawDDALine(startX, startY, x, y, super.getFakeBitmap(), RenderingType.GRADIENT);
                break;
            case MotionEvent.ACTION_UP:
                drawDDALine(startX, startY, x, y, super.getFakeBitmap(), RenderingType.ERASE);
                drawDDALine(startX, startY, x, y, super.getMainBitmap(), RenderingType.GRADIENT);
                break;
        }
    }
}
