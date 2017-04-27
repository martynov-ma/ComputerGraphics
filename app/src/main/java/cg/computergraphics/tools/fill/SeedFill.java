package cg.computergraphics.tools.fill;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.MotionEvent;

import java.util.Stack;

import cg.computergraphics.MainActivity;
import cg.computergraphics.tools.DrawingTool;

/**
 * Created by Alexander on 12.04.2017.
 */

public class SeedFill extends DrawingTool {

    private Stack<Point> stack;
    private int fillColor;
    private int oldColor;

    public SeedFill(Bitmap mainBitmap) {
        super(mainBitmap, null);
        stack = new Stack<>();
        fillColor = MainActivity.appSettings.getDrawingColor();
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
        oldColor = mainBitmap.getPixel(x, y);
        if(oldColor == fillColor) return;
        stack.push(new Point(x, y));
        recursiveFill();
        stack.clear();
    }

    private void recursiveFill()
    {
        if(stack.empty()) return;
        Bitmap mainBitmap = super.getMainBitmap();
        Point point = stack.pop();
        int bitmapWidth = mainBitmap.getWidth();
        int bitmapHeight = mainBitmap.getHeight();
        int x = point.x;
        int y = point.y;

        mainBitmap.setPixel(x,y,fillColor);
        //System.out.println("Красит " + x + " " + y);
        if(mainBitmap.getPixel(x+1,y) == oldColor && x+1 < bitmapWidth-1 && y < bitmapHeight-1 && x+1 >=1 && y>=1){
            stack.push(new Point(x+1,y));
            //System.out.println("Поместили " + (x+1) + " " + y);
        }
        if(mainBitmap.getPixel(x-1,y) == oldColor && x-1 < bitmapWidth-1 && y < bitmapHeight-1 && x-1 >=1 && y>=1){
            stack.push(new Point(x-1,y));
            //System.out.println("Красит " + (x-1) + " " + y);
        }
        if(mainBitmap.getPixel(x,y+1) == oldColor && x < bitmapWidth-1 && y+1 < bitmapHeight-1 && x >=1 && y+1 >=1){
            stack.push(new Point(x,y+1));
            //System.out.println("Красит " + x + " " + (y+1));
        }
        if(mainBitmap.getPixel(x,y-1) == oldColor && x < bitmapWidth-1 && y-1 < bitmapHeight-1 && x >=1 && y-1 >=1){
            stack.push(new Point(x,y-1));
            //System.out.println("Красит " + x + " " + (y-1));
        }
        //System.out.println("========================");
        recursiveFill();
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
