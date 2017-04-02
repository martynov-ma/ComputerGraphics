package cg.computergraphics.tools;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;

import cg.computergraphics.tools.enums.RenderingType;

/**
 * Created by MAX on 14.03.2017.
 */

public class BrzCircle extends DrawingTool {
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    public BrzCircle(Bitmap mainBitmap, Bitmap fakeBitmap) {
        super(mainBitmap, fakeBitmap);
    }

    private void drawCircleBrz(int x1, int y1, int x2, int y2, Bitmap bitmap, RenderingType renderingType) {

        int a = Math.abs(x2 - x1);
        int b = Math.abs(y2 - y1);
        int R = a < b ? Math.round(a / 2) : Math.round(b / 2);

        int directionX = (int) Math.signum(x2 - x1);
        int directionY = (int) Math.signum(y2 - y1);

        int centerX = x1 + R * directionX;
        int centerY = y1 + R * directionY;

        int x;
        int y;
        int f = 1 - R;
        x = 0;
        y = R;

        int color = 0;
        switch (renderingType) {
            case SOLID:
                color = super.getColor();
                break;
            case ERASE:
                color = 0;
                break;
        }

        bitmap.setPixel(centerX, centerY + R, color);
        bitmap.setPixel(centerX, centerY - R, color);
        bitmap.setPixel(centerX + R, centerY, color);
        bitmap.setPixel(centerX - R, centerY, color);

        while(x <= y) {

            if(f > 0) {
                y--;
                f = f + 2 * (x - y) + 5;
            } else {
                f = f + 2 * x + 3;
            }
            x++;

            bitmap.setPixel(centerX + x, centerY + y, color);
            bitmap.setPixel(centerX - x, centerY + y, color);
            bitmap.setPixel(centerX + x, centerY - y, color);
            bitmap.setPixel(centerX - x, centerY - y, color);
            bitmap.setPixel(centerX + y, centerY + x, color);
            bitmap.setPixel(centerX - y, centerY + x, color);
            bitmap.setPixel(centerX + y, centerY - x, color);
            bitmap.setPixel(centerX - y, centerY - x, color);
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
                drawCircleBrz(x1, y1, x2, y2, super.getFakeBitmap(), RenderingType.ERASE);
                x2 = x;
                y2 = y;
                drawCircleBrz(x1, y1, x, y, super.getFakeBitmap(), RenderingType.SOLID);
                break;
            case MotionEvent.ACTION_UP:
                drawCircleBrz(x1, y1, x, y, super.getFakeBitmap(), RenderingType.ERASE);
                drawCircleBrz(x1, y1, x, y, super.getMainBitmap(), RenderingType.SOLID);
                break;
        }
    }
}
