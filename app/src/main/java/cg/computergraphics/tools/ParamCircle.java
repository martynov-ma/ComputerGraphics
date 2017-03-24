package cg.computergraphics.tools;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;

/**
 * Created by MAX on 14.03.2017.
 */

public class ParamCircle extends DrawingTool {
    private int color = 0;  //"white"
    private int oldX;
    private int oldY;
    private int x2;
    private int y2;

    public ParamCircle(Bitmap mainBitmap, Bitmap fakeBitmap) {
        super(mainBitmap, fakeBitmap);
    }

    private void drawParamCircle(int x1, int y1, int x2, int y2, Bitmap bitmap) {

        float a = Math.abs(x2 - x1);
        float b = Math.abs(y2 - y1);
        int R = a < b ? (int) a / 2 : (int) b / 2;

        int directionX = (int) Math.signum(x2 - x1);
        int directionY = (int) Math.signum(y2 - y1);

        int centerX = x1 + R * directionX;
        int centerY = y1 + R * directionY;

        int y;
        for(int x = 0; x < R / Math.sqrt(2); x++) {
            y = (int)(Math.abs(Math.sqrt(Math.pow(R, 2) - Math.pow(x, 2))));

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
                oldX = x;
                oldY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                color = 0;
                drawParamCircle(oldX, oldY, x2, y2, super.getFakeBitmap());
                x2 = x;
                y2 = y;
                color = Color.BLACK;
                drawParamCircle(oldX, oldY, x, y, super.getFakeBitmap());
                break;
            case MotionEvent.ACTION_UP:
                color = Color.BLACK;
                drawParamCircle(oldX, oldY, x, y, super.getMainBitmap());
                color = 0;
                drawParamCircle(oldX, oldY, x, y, super.getFakeBitmap());
                break;
        }
    }
}
