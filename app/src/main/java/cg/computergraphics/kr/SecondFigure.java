package cg.computergraphics.kr;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;

import cg.computergraphics.MainActivity;
import cg.computergraphics.tools.BrzCircle;
import cg.computergraphics.tools.BrzLine;
import cg.computergraphics.tools.DrawingTool;
import cg.computergraphics.tools.fill.FloodFill;
import cg.computergraphics.tools.RenderingType;

/**
 * Created by Alexander on 15.04.2017.
 */

public class SecondFigure extends DrawingTool {

    BrzCircle brz;
    BrzLine line;
    FloodFill ff;
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private int centerX;
    private int centerY;
    private int R;

    public SecondFigure(Bitmap mainBitmap, Bitmap fakeBitmap) {
        super(mainBitmap, fakeBitmap);
        brz = new BrzCircle(getMainBitmap(),getFakeBitmap());
        line = new BrzLine(getMainBitmap(),getFakeBitmap());
        ff = new FloodFill(mainBitmap);
    }

    public void drawLines(int x1, int y1, int x2, int y2,Bitmap bitmap, RenderingType renderingType){

        int a = Math.abs(x2 - x1);
        int b = Math.abs(y2 - y1);
        R = a < b ? Math.round(a / 2) : Math.round(b / 2);

        int directionX = (int) Math.signum(x2 - x1);
        int directionY = (int) Math.signum(y2 - y1);

        centerX = x1 + R * directionX;
        centerY = y1 + R * directionY;
        System.out.println("======================");
        /*for(int i = 1;i<8;i+=2) {*/
        int x = (int) (centerX + R * Math.cos(Math.PI / (4)));
        int y = (int) (centerY + R * Math.cos(Math.PI / (4)));
        int delX = x-centerX;
        int delY = y-centerY;
        int r = y - delY;
        int t = x - delX;
        //System.out.println(t + " " + r + " " + delX + " " + delY + " 66666666");
        line.drawBrzLine(centerX, centerY, x, y, bitmap, renderingType);
        //System.out.println(centerX + " " + centerY + " " + x + " " + y);
        line.drawBrzLine(centerX, centerY, x, centerY - delY, bitmap, renderingType);
        //System.out.println(centerX + " " + centerY + " " + x + " " + (y-delY));
        line.drawBrzLine(centerX, centerY, centerX-delX, y, bitmap, renderingType);
        //System.out.println(centerX + " " + centerY + " " + (x-delX) + " " + y);
        line.drawBrzLine(centerX, centerY, centerX-delX, centerY-delY, bitmap, renderingType);
        //System.out.println(centerX + " " + centerY + " " + (x-delX) + " " + (y-delY));

    }

    @Override
    public void setColor(int color) {
        brz.setColor(Color.BLACK);
        line.setColor(Color.BLACK);
        ff.setColor(color);
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
                brz.drawCircleBrz(x1, y1, x2, y2, super.getFakeBitmap(), RenderingType.ERASE);
                drawLines(x1, y1, x2, y2, super.getFakeBitmap(), RenderingType.ERASE);
                x2 = x;
                y2 = y;
                brz.drawCircleBrz(x1, y1, x, y, super.getFakeBitmap(), RenderingType.SOLID);
                drawLines(x1, y1, x2, y2, super.getFakeBitmap(), RenderingType.SOLID);
                break;
            case MotionEvent.ACTION_UP:
                brz.drawCircleBrz(x1, y1, x, y, super.getFakeBitmap(), RenderingType.ERASE);
                drawLines(x1, y1, x2, y2, super.getFakeBitmap(), RenderingType.ERASE);
                brz.drawCircleBrz(x1, y1, x, y, super.getMainBitmap(), RenderingType.SOLID);
                drawLines(x1, y1, x2, y2, super.getMainBitmap(), RenderingType.SOLID);
                if (MainActivity.appSettings.isFillEnabled()) {
                    if (R > 1) {
                        ff.fillBackground(centerX + 1, centerY);
                        ff.fillBackground(centerX, centerY + 1);
                        ff.fillBackground(centerX - 1, centerY);
                        ff.fillBackground(centerX, centerY - 1);
                    } else {
                        super.getMainBitmap().setPixel(centerX, centerY, ff.getColor());
                    }
                }
                break;
        }
    }
}
