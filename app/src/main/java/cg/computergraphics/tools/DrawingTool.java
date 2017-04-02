package cg.computergraphics.tools;

import android.graphics.Bitmap;
import android.view.MotionEvent;

import cg.computergraphics.MainActivity;

/**
 * Created by MAX on 13.03.2017.
 */

public abstract class DrawingTool {
    private Bitmap mainBitmap;
    private Bitmap fakeBitmap;
    private int color;

    DrawingTool(Bitmap mainBitmap, Bitmap fakeBitmap) {
        this.mainBitmap = mainBitmap;
        this.fakeBitmap = fakeBitmap;
        color = MainActivity.appSettings.getDrawingColor();
    }

    Bitmap getMainBitmap() {
        return mainBitmap;
    }

    Bitmap getFakeBitmap() {
        return fakeBitmap;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public abstract void onTouch(MotionEvent motionEvent);
}
