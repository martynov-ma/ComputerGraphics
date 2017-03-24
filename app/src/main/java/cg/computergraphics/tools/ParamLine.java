package cg.computergraphics.tools;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;

/**
 * Created by MAX on 13.03.2017.
 */

public class ParamLine extends DrawingTool {
    private int color = 0;  //"white"
    private int oldX;
    private int oldY;
    private int x2;
    private int y2;

    public ParamLine(Bitmap mainBitmap, Bitmap fakeBitmap) {
        super(mainBitmap, fakeBitmap);
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void drawParamLine(float startX, float startY, float endX, float endY, Bitmap bitmap){
        float x = startX;
        float y = startY;
        int N = Math.round(Math.max(Math.abs(endX - startX), Math.abs(endY - startY))) + 1;

        float delX = (endX - startX) / (N - 1);
        float delY = (endY - startY) / (N - 1);

        x -= delX;
        y -= delY;
        for (int i = 0; i < N; i++) {
            x += delX;
            y += delY;
            try {
                bitmap.setPixel((int) x, (int) y, color);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    @Override
    public void onTouch(MotionEvent motionEvent){

        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                color = Color.BLACK;
                super.getFakeBitmap().setPixel(x, y, color);
                oldX = x;
                oldY = y;
                x2 = x;
                y2 = y;
                break;
            case MotionEvent.ACTION_MOVE:
                color = 0;
                drawParamLine(oldX, oldY, x2, y2, super.getFakeBitmap());
                x2 = x;
                y2 = y;
                color = Color.BLACK;
                drawParamLine(oldX, oldY, x, y, super.getFakeBitmap());
                break;
            case MotionEvent.ACTION_UP:
                color = Color.BLACK;
                drawParamLine(oldX, oldY, x, y, super.getMainBitmap());
                color = 0;
                drawParamLine(oldX, oldY, x, y, super.getFakeBitmap());
                break;
        }
    }
}
