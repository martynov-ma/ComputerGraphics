package cg.computergraphics.tools;

import android.graphics.Bitmap;
import android.view.MotionEvent;

import cg.computergraphics.AppSettings;
import cg.computergraphics.MainActivity;

/**
 * Created by MAX on 13.03.2017.
 */

public abstract class DrawingTool {
    private Bitmap mainBitmap;
    private Bitmap fakeBitmap;
    private int color;

    public DrawingTool(Bitmap mainBitmap, Bitmap fakeBitmap) {
        this.mainBitmap = mainBitmap;
        this.fakeBitmap = fakeBitmap;
        color = AppSettings.getInstance().getDrawingColor();
    }

    public Bitmap getMainBitmap() {
        return mainBitmap;
    }

    public Bitmap getFakeBitmap() {
        return fakeBitmap;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void transferToMainBitmap() {}


    public abstract void onTouch(MotionEvent motionEvent);
}
