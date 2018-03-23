package cg.computergraphics.tools;

import android.graphics.Bitmap;
import android.view.MotionEvent;

import cg.computergraphics.AppSettings;
import cg.computergraphics.MainActivity;
import cg.computergraphics.tools.brz.BrzLine;

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

        int temp;
        if (y1 > y2) {
            temp = y1;
            y1 = y2;
            y2 = temp;
        }
        if (x1 > x2) {
            temp = x1;
            x1 = x2;
            x2 = temp;
        }

        int color = 0;
        switch (renderingType) {
            case SOLID:
                color = super.getColor();
                break;
            case ERASE:
                color = 0;
                break;
        }

        for (int x = x1; x <= x2; x++) {
            bitmap.setPixel(x, y1, color);
            bitmap.setPixel(x, y2, color);
        }
        for (int y = y1; y <= y2; y++) {
            bitmap.setPixel(x1, y, color);
            bitmap.setPixel(x2, y, color);
        }

        if (AppSettings.getInstance().isFillEnabled()) {
            for (int y = y1; y < y2; y++) {
                for (int x = x1; x < x2; x++) {
                    bitmap.setPixel(x, y, color);
                }
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

    @Override
    public int getColor() {
        return painter.getColor();
    }

    @Override
    public void setColor(int color) {
        painter.setColor(color);
    }
}
