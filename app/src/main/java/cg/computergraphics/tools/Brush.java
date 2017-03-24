package cg.computergraphics.tools;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;

/**
 * Created by MAX on 14.03.2017.
 */

public class Brush extends DrawingTool {
    private float oldX;
    private float oldY;

    public Brush(Bitmap mainBitmap) {
        super(mainBitmap, null);
    }

    private void drawLine(float startX, float startY, float endX, float endY) {
        float x = startX;
        float y = startY;
        int N = Math.round(Math.max(Math.abs(endX - startX), Math.abs(endY - startY))) + 1;

        float delX = (endX - startX)/(N - 1);
        float delY = (endY - startY)/(N - 1);

        x -= delX;
        y -= delY;
        for(int i = 0; i < N; i++) {
            x += delX;
            y += delY;
            try {
                super.getMainBitmap().setPixel((int)x, (int)y, Color.BLACK);
            } catch (IllegalArgumentException ignored) {}

        }
    }

    @Override
    public void onTouch (MotionEvent motionEvent) {

        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();

        try {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    super.getMainBitmap().setPixel(x, y, Color.BLACK);
                    oldX = x;
                    oldY = y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    drawLine(oldX, oldY, x, y);
                    oldX = x;
                    oldY = y;
                    break;
                case MotionEvent.ACTION_UP:
                    drawLine(oldX, oldY, x, y);
                    break;
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }
}
