package cg.computergraphics.tools;

import android.graphics.Bitmap;
import android.view.MotionEvent;

import cg.computergraphics.tools.dda.DDALine;

/**
 * Created by MAX on 14.03.2017.
 */

public class Brush extends DrawingTool {
    private DDALine painter;
    private float oldX;
    private float oldY;

    public Brush(Bitmap mainBitmap) {
        super(mainBitmap, null);
        painter = new DDALine(mainBitmap, null);
    }

    @Override
    public int getColor() {
        return painter.getColor();
    }

    @Override
    public void setColor(int color) {
        painter.setColor(color);
    }

    @Override
    public void onTouch (MotionEvent motionEvent) {

        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldX = x;
                oldY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                painter.drawDDALine(oldX,oldY,x,y, super.getMainBitmap(), RenderingType.SOLID);
                oldX = x;
                oldY = y;
                break;
            }
    }
}
