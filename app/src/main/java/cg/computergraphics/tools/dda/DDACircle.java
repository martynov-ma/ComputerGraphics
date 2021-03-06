package cg.computergraphics.tools.dda;

import android.graphics.Bitmap;
import android.view.MotionEvent;

import cg.computergraphics.tools.DrawingTool;
import cg.computergraphics.tools.RenderingType;

/**
 * Created by MAX on 14.03.2017.
 */

public class DDACircle extends DrawingTool {
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    public DDACircle(Bitmap mainBitmap, Bitmap fakeBitmap) {
        super(mainBitmap, fakeBitmap);
    }

    private void drawDDACircle(int x1, int y1, int x2, int y2, Bitmap bitmap, RenderingType renderingType) {

        float a = Math.abs(x2 - x1);
        float b = Math.abs(y2 - y1);
        int R = a < b ? (int) a / 2 : (int) b / 2;

        int directionX = (int) Math.signum(x2 - x1);
        int directionY = (int) Math.signum(y2 - y1);

        int centerX = x1 + R * directionX;
        int centerY = y1 + R * directionY;

        int y;
        int color = 0;
        switch (renderingType) {
            case SOLID:
                color = super.getColor();
                break;
            case ERASE:
                color = 0;
                break;
        }

        double upperBound = R / Math.sqrt(2);
        for(int x = 0; x < upperBound; x++) {
            y = (int) Math.sqrt(R * R - x * x);

            bitmap.setPixel(centerX + x, centerY + y, color);
            bitmap.setPixel(centerX + y, centerY + x, color);
            bitmap.setPixel(centerX + y, centerY - x, color);
            bitmap.setPixel(centerX + x, centerY - y, color);
            bitmap.setPixel(centerX - x, centerY - y, color);
            bitmap.setPixel(centerX - y, centerY - x, color);
            bitmap.setPixel(centerX - y, centerY + x, color);
            bitmap.setPixel(centerX - x, centerY + y, color);
        }
    }

    @Override
    public void onTouch(MotionEvent motionEvent) {

        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = x;
                y1 = y;
                break;
            case MotionEvent.ACTION_MOVE:
                drawDDACircle(x1, y1, x2, y2, super.getFakeBitmap(), RenderingType.ERASE);
                x2 = x;
                y2 = y;
                drawDDACircle(x1, y1, x, y, super.getFakeBitmap(), RenderingType.SOLID);
                break;
            case MotionEvent.ACTION_UP:
                drawDDACircle(x1, y1, x, y, super.getFakeBitmap(), RenderingType.ERASE);
                drawDDACircle(x1, y1, x, y, super.getMainBitmap(), RenderingType.SOLID);
                break;
        }
    }
}
