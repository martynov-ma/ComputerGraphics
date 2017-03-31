package cg.computergraphics.tools;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;

/**
 * Created by MAX on 14.03.2017.
 */

public class BrzCircle extends DrawingTool {
    private int color;
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    public BrzCircle(Bitmap mainBitmap, Bitmap fakeBitmap) {
        super(mainBitmap, fakeBitmap);
    }

    private void drawCircleBrz(int x1, int y1, int x2, int y2, Bitmap bitmap) {

        float a = Math.abs(x2 - x1);
        float b = Math.abs(y2 - y1);
        int R = a < b ? (int) a / 2 : (int) b / 2;

        int directionX = (int) Math.signum(x2 - x1);
        int directionY = (int) Math.signum(y2 - y1);

        int centerX = x1 + R * directionX;
        int centerY = y1 + R * directionY;

        int x;
        int y;
        int f = 1 - R;
        x = 0;
        y = R;

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
                color = 0;
                drawCircleBrz(x1, y1, x2, y2, super.getFakeBitmap());
                x2 = x;
                y2 = y;
                color = Color.BLACK;
                drawCircleBrz(x1, y1, x, y, super.getFakeBitmap());
                break;
            case MotionEvent.ACTION_UP:
                color = 0;
                drawCircleBrz(x1, y1, x, y, super.getFakeBitmap());
                color = Color.BLACK;
                drawCircleBrz(x1, y1, x, y, super.getMainBitmap());
                break;
        }
    }
}
