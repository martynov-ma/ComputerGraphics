package cg.computergraphics;

import android.graphics.Color;

/**
 * Created by MAX on 18.03.2017.
 */

public class AppSettings {
    private int bitmapWidth;
    private int bitmapHeight;

    private int lineDrawingAlgorithm;
    private int circleDrawingAlgorithm;

    private int mosaicSize;

    private int drawingColor;

    AppSettings() {
        bitmapWidth = 2000;
        bitmapHeight = 2000;

        lineDrawingAlgorithm = 0;
        circleDrawingAlgorithm = 1;

        mosaicSize = 10;

        drawingColor = Color.BLACK;
    }

    int getBitmapWidth() {
        return bitmapWidth;
    }

    void setBitmapWidth(int bitmapWidth) {
        this.bitmapWidth = bitmapWidth;
    }

    int getBitmapHeight() {
        return bitmapHeight;
    }

    void setBitmapHeight(int bitmapHeight) {
        this.bitmapHeight = bitmapHeight;
    }


    int getLineDrawingAlgorithm() {
        return lineDrawingAlgorithm;
    }

    void setLineDrawingAlgorithm(int algorithmId) {
        lineDrawingAlgorithm = algorithmId;
    }

    int getCircleDrawingAlgorithm() {
        return circleDrawingAlgorithm;
    }

    void setCircleDrawingAlgorithm(int algorithmId) {
        circleDrawingAlgorithm = algorithmId;
    }


    int getMosaicSize() {
        return mosaicSize;
    }

    void setMosaicSize(int mosaicSize) {
        this.mosaicSize = mosaicSize;
    }


    public int getDrawingColor() {
        return drawingColor;
    }

    public void setDrawingColor(int drawingColor) {
        this.drawingColor = drawingColor;
    }
}
