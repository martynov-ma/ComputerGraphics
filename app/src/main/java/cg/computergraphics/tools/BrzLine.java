package cg.computergraphics.tools;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;

/**
 * Created by MAX on 13.03.2017.
 */

public class BrzLine extends DrawingTool {
    private boolean color = false;
    private int oldX;
    private int oldY;
    private int x2;
    private int y2;


    public BrzLine(Bitmap mainBitmap, Bitmap fakeBitmap) {
        super(mainBitmap, fakeBitmap);
    }

    private void drawBrzLine(float startX, float startY, float endX, float endY, Bitmap bmp){
        float x = startX;
        float y = startY;
        float delX = Math.abs(endX - startX);
        float delY = Math.abs(endY - startY);
        if(delX !=0 || delY != 0) {
            int s1 = 1;
            int s2 = 1;
            System.out.println("1) delX = " + delX);
            if (endX - startX < 0) {
                s1 = -1;
            } else {
                if (endX - startX > 0) {
                    s1 = 1;
                } else {
                    if (endX - startX == 0) {
                        s1 = 0;
                    }
                }
            }

            if (endY - startY < 0) {
                s2 = -1;
            } else {
                if (endY - startY > 0) {
                    s2 = 1;
                } else {
                    if (endY - startY == 0) {
                        s2 = 0;
                    }
                }
            }

            boolean swap = true;
            if (delY > delX) {
                float buf = delX;
                delX = delY;
                delY = buf;
                swap = true;
            } else {
                swap = false;
            }
            System.out.println("2) delX = " + delX);
            float f = 2 * delY - delX;
            float f1 = 2 * delY;
            float f2 = 2 * delX;
            System.out.println("f = " + f);

            for (int i = 0; i <= delX; i++) {

                if (color) {
                    System.out.println("чёрный");
                    bmp.setPixel((int) x, (int) y, Color.BLACK);
                } else {
                    System.out.println("белый");
                    bmp.setPixel((int) x, (int) y, 0);
                }
//drawPoint(x,y);
                while (f >= 0) {
                    if (swap) {
                        x += s1;
                    } else {
                        y += s2;
                    }
                    f -= f2;
//System.out.println("f = " + f);
                }
                if (swap) {
                    y += s2;
                } else {
                    x += s1;
                }
                f += f1;
            }
        }
    }

    @Override
    public void onTouch(MotionEvent motionEvent){

        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();

        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                color = true;
                //System.out.println(x + " " + y + " " + (int)motionEvent.getX() + " " + (int)motionEvent.getY());
                super.getFakeBitmap().setPixel(x, y, Color.BLACK);
                oldX = x;
                oldY = y;
                x2 = x;
                y2 = y;
                break;
            case MotionEvent.ACTION_MOVE:
                color = false;
                drawBrzLine(oldX, oldY, x2, y2, super.getFakeBitmap());
                x2 = x;
                y2 = y;
                color = true;
                drawBrzLine(oldX, oldY, x, y, super.getFakeBitmap());
                break;
            case MotionEvent.ACTION_UP:
                color = true;
                drawBrzLine(oldX, oldY, x, y, super.getMainBitmap());
                color = false;
                drawBrzLine(oldX, oldY, x, y, super.getFakeBitmap());
                break;
        }
    }
}
