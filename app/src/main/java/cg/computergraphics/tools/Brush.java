package cg.computergraphics.tools;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;

import cg.computergraphics.tools.enums.DDARenderingType;

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
        painter.setColor(Color.BLACK);
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
                painter.drawDDALine(oldX,oldY,x,y, super.getMainBitmap(), DDARenderingType.SOLID);
                oldX = x;
                oldY = y;
                break;
            }
    }
}
