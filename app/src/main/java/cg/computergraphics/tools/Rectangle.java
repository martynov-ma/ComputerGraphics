package cg.computergraphics.tools;

import android.graphics.Bitmap;
import android.view.MotionEvent;

import cg.computergraphics.MainActivity;

/**
 * Created by MAX on 27.04.2017.
 */

public class Rectangle extends DrawingTool {
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private BrzLine painter;


    public Rectangle(Bitmap mainBitmap, Bitmap fakeBitmap) {
        super(mainBitmap, fakeBitmap);
        painter = new BrzLine(mainBitmap, fakeBitmap);
    }

    public void drawRectangle(int x1, int y1, int x2, int y2, Bitmap bitmap, RenderingType renderingType) {

        painter.drawBrzLine(x1, y1, x2, y1, bitmap, renderingType);
        painter.drawBrzLine(x2, y1, x2, y2, bitmap, renderingType);
        painter.drawBrzLine(x2, y2, x1, y2, bitmap, renderingType);
        painter.drawBrzLine(x1, y2, x1, y1, bitmap, renderingType);

        if (MainActivity.appSettings.isFillEnabled()) {
            fillRectangle(x1, y1, x2, y2, bitmap, renderingType);
        }
    }

    private void fillRectangle(int x1, int y1, int x2, int y2, Bitmap bitmap, RenderingType renderingType){

        if (y1 < y2) {
            for (int y = y1; y <= y2; y++) {
                painter.drawBrzLine(x1, y, x2, y, bitmap, renderingType);
            }
        } else {
            for (int y = y2; y <= y1; y++) {
                painter.drawBrzLine(x1, y, x2, y, bitmap, renderingType);
            }
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
                drawRectangle(x1, y1, x2, y2, super.getFakeBitmap(), RenderingType.ERASE);
                x2 = x;
                y2 = y;
                drawRectangle(x1, y1, x, y, super.getFakeBitmap(), RenderingType.SOLID);
                break;
            case MotionEvent.ACTION_UP:
                drawRectangle(x1, y1, x, y, super.getFakeBitmap(), RenderingType.ERASE);
                drawRectangle(x1, y1, x, y, super.getMainBitmap(), RenderingType.SOLID);
                break;
        }
    }
}
