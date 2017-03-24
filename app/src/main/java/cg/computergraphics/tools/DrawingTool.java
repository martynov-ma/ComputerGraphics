package cg.computergraphics.tools;

import android.graphics.Bitmap;
import android.view.MotionEvent;

/**
 * Created by MAX on 13.03.2017.
 */

public abstract class DrawingTool {
    private Bitmap mainBitmap;
    private Bitmap fakeBitmap;

    DrawingTool(Bitmap mainBitmap, Bitmap fakeBitmap) {
        this.mainBitmap = mainBitmap;
        this.fakeBitmap = fakeBitmap;
    }

    Bitmap getMainBitmap() {
        return mainBitmap;
    }

    Bitmap getFakeBitmap() {
        return fakeBitmap;
    }

    public abstract void onTouch(MotionEvent motionEvent);
}
