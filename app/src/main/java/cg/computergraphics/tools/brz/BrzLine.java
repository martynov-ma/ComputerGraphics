package cg.computergraphics.tools.brz;

import android.graphics.Bitmap;
import android.view.MotionEvent;

import cg.computergraphics.tools.DrawingTool;
import cg.computergraphics.tools.RenderingType;

/**
 * Created by MAX on 13.03.2017.
 */

public class BrzLine extends DrawingTool {
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public BrzLine(Bitmap mainBitmap, Bitmap fakeBitmap) {
        super(mainBitmap, fakeBitmap);
    }

    public void drawBrzLine(float startX, float startY, float endX, float endY, Bitmap bitmap, RenderingType renderingType){
        float x = startX;
        float y = startY;
        float delX = Math.abs(endX - startX);
        float delY = Math.abs(endY - startY);
        if(delX !=0 || delY != 0) {
            int s1 = (int) Math.signum(endX - startX);
            int s2 = (int) Math.signum(endY - startY);

            boolean swap;
            if (delY > delX) {
                float buf = delX;
                delX = delY;
                delY = buf;
                swap = true;
            } else {
                swap = false;
            }

            float f = 2 * delY - delX;
            float f1 = 2 * delY;
            float f2 = 2 * delX;

            for (int i = 0; i <= delX; i++) {
                switch (renderingType) {
                    case SOLID:
                        bitmap.setPixel((int) x, (int) y, super.getColor());
                        break;
                    case ERASE:
                        bitmap.setPixel((int) x, (int) y, 0);
                        break;
                }

                while (f >= 0) {
                    if (swap) x += s1;
                    else y += s2;
                    f -= f2;
                }

                if (swap) y += s2;
                else x += s1;
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
                startX = x;
                startY = y;
                endX = x;
                endY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                drawBrzLine(startX, startY, endX, endY, super.getFakeBitmap(), RenderingType.ERASE);
                endX = x;
                endY = y;
                drawBrzLine(startX, startY, x, y, super.getFakeBitmap(), RenderingType.SOLID);
                break;
            case MotionEvent.ACTION_UP:
                drawBrzLine(startX, startY, x, y, super.getFakeBitmap(), RenderingType.ERASE);
                drawBrzLine(startX, startY, x, y, super.getMainBitmap(), RenderingType.SOLID);
                break;
        }
    }
}
