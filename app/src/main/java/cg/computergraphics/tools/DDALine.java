package cg.computergraphics.tools;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;

import cg.computergraphics.tools.enums.DDARenderingType;

/**
 * Created by MAX on 13.03.2017.
 */

public class DDALine extends DrawingTool {
    private int color;
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public DDALine(Bitmap mainBitmap, Bitmap fakeBitmap) {
        super(mainBitmap, fakeBitmap);
        color = Color.BLACK;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void drawParamLine(float startX, float startY, float endX, float endY, Bitmap bitmap, DDARenderingType renderingType) {
        float x = startX;
        float y = startY;

        int N = Math.round(Math.max(Math.abs(endX - startX), Math.abs(endY - startY)));
        float delX = (endX - startX) / N;
        float delY = (endY - startY) / N;

        //color approximation
        int startColor = Color.rgb(250, 0, 0);
        int endColor = Color.rgb(0, 0, 250);
        int r1 = Color.red(startColor), g1 = Color.green(startColor), b1 = Color.blue(startColor);
        int r2 = Color.red(endColor), g2 = Color.green(endColor), b2 = Color.blue(endColor);

        float delR = (float) (r2 - r1) / N;
        float delG = (float) (g2 - g1) / N;
        float delB = (float) (b2 - b1) / N;

        for (int i = 0; i < N; i++, x += delX, y += delY) {
            try {
                switch (renderingType) {
                    case SOLID:
                        bitmap.setPixel((int) x, (int) y, color);
                        break;
                    case GRADIENT:
                        bitmap.setPixel((int) x, (int) y, Color.rgb(r1, g1, b1));
                        r1 += delR;
                        g1 += delG;
                        b1 += delB;
                        break;
                    case ERASE:
                        bitmap.setPixel((int) x, (int) y, 0);
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
                drawParamLine(startX, startY, endX, endY, super.getFakeBitmap(), DDARenderingType.ERASE);
                endX = x;
                endY = y;
                drawParamLine(startX, startY, x, y, super.getFakeBitmap(), DDARenderingType.GRADIENT);
                break;
            case MotionEvent.ACTION_UP:
                drawParamLine(startX, startY, x, y, super.getFakeBitmap(), DDARenderingType.ERASE);
                drawParamLine(startX, startY, x, y, super.getMainBitmap(), DDARenderingType.GRADIENT);
                break;
        }
    }
}
