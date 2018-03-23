package cg.computergraphics.tools.fill;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.MotionEvent;

import java.util.Stack;

import cg.computergraphics.AppSettings;
import cg.computergraphics.MainActivity;
import cg.computergraphics.tools.DrawingTool;


/**
 * Created by MAX on 03.04.2017.
 */

public class FloodFill extends DrawingTool {

    private Stack<Point> stack;
    private int fillColor;

    public FloodFill(Bitmap mainBitmap) {
        super(mainBitmap, null);
        stack = new Stack<>();
        fillColor = AppSettings.getInstance().getDrawingColor();
    }

    @Override
    public int getColor() {
        return fillColor;
    }

    @Override
    public void setColor(int color) {
        fillColor = color;
    }

    public void fillBackground(int x, int y) {
        Bitmap mainBitmap = super.getMainBitmap();
        int bitmapWidth = mainBitmap.getWidth();
        int bitmapHeight = mainBitmap.getHeight();

        Point point;
        boolean spanAbove, spanBelow;
        int oldColor = mainBitmap.getPixel(x, y);

        if(oldColor == fillColor) return;
        stack.clear();
        stack.push(new Point(x, y));

        while(stack.size() > 0) {
            point = stack.pop();
            x = point.x;
            y = point.y;
            while(x >= 0 && mainBitmap.getPixel(x, y) == oldColor) x--;
            x++;

            spanAbove = spanBelow = false;
            while(x < bitmapWidth && mainBitmap.getPixel(x, y) == oldColor) {
                mainBitmap.setPixel(x, y, fillColor);

                if(!spanAbove && y > 0 && mainBitmap.getPixel(x, y - 1) == oldColor) {
                    stack.push(new Point(x, y - 1));
                    spanAbove = true;
                }
                else if(spanAbove && y > 0 && mainBitmap.getPixel(x, y - 1) != oldColor) spanAbove = false;

                if(!spanBelow && y < bitmapHeight - 1 && mainBitmap.getPixel(x, y + 1) == oldColor) {
                    stack.push(new Point(x, y + 1));
                    spanBelow = true;
                }
                else if(spanBelow && y < bitmapHeight - 1 && mainBitmap.getPixel(x, y + 1) != oldColor) spanBelow = false;

                x++;
            }
        }
    }

    @Override
    public void onTouch(MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                fillBackground(x, y);
                break;
        }
    }
}
