package cg.computergraphics;

import android.graphics.Color;

/**
 * Created by MAX on 18.03.2017.
 */

public class AppSettings {
    private int bitmapWidth;
    private int bitmapHeight;
    private int bitmapScale;

    private int lineDrawingAlgorithm;
    private int circleDrawingAlgorithm;

    private int mosaicSize;

    private int drawingColor;

    private boolean isScrollEnabled;
    private boolean isFillEnabled;

    AppSettings() {
        bitmapWidth = 2000;
        bitmapHeight = 2000;
        bitmapScale = 1;

        lineDrawingAlgorithm = 0;
        circleDrawingAlgorithm = 1;

        mosaicSize = 10;

        drawingColor = Color.BLACK;

        isScrollEnabled = false;
        isFillEnabled = false;
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

    public int getBitmapScale() {
        return bitmapScale;
    }

    public void setBitmapScale(int bitmapScale) {
        this.bitmapScale = bitmapScale;
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


    public boolean isScrollEnabled() {
        return isScrollEnabled;
    }

    public void setScroll(boolean isEnabled) {
        isScrollEnabled = isEnabled;
    }

    public boolean isFillEnabled() {
        return isFillEnabled;
    }

    public void setFill(boolean isEnabled) {
        isFillEnabled = isEnabled;
    }

}
