package cg.computergraphics;

import android.graphics.Color;

/**
 * Created by MAX on 18.03.2017.
 */

public class AppSettings {
    private int bitmapWidth;
    private int bitmapHeight;
    private int bitmapScale;

    private int defaultTool;

    private int lineDrawingAlgorithm;
    private int circleDrawingAlgorithm;

    //private boolean isObj
    private boolean isObjFilling;
    private boolean isObjRandomColor;

    private int mosaicSize;

    private int drawingColor;

    private boolean isScrollEnabled;
    private boolean isFillEnabled;

    private boolean isLineColorApprox;
    private boolean isPolygonColorApprox;

    private int splinesRank;

    AppSettings() {
        bitmapWidth = 2000;
        bitmapHeight = 2000;
        bitmapScale = 1;

        defaultTool = 1;

        lineDrawingAlgorithm = 0;
        circleDrawingAlgorithm = 1;

        isObjFilling = true;
        isObjRandomColor = true;

        mosaicSize = 10;

        drawingColor = Color.BLACK;

        isScrollEnabled = false;
        isFillEnabled = false;

        isLineColorApprox = false;
        isPolygonColorApprox = false;

        splinesRank = 3;
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

    public int getDefaultTool() {
        return defaultTool;
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


    public boolean isObjFilling() {
        return isObjFilling;
    }

    public void setObjFilling(boolean objFilling) {
        this.isObjFilling = objFilling;
    }

    public boolean isObjRandomColor() {
        return isObjRandomColor;
    }

    public void setObjRandomColor(boolean objRandomColor) {
        isObjRandomColor = objRandomColor;
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


    public boolean isLineColorApprox() {
        return isLineColorApprox;
    }

    public void setLineColorApprox(boolean lineColorApprox) {
        isLineColorApprox = lineColorApprox;
    }

    public boolean isPolygonColorApprox() {
        return isPolygonColorApprox;
    }

    public void setPolygonColorApprox(boolean isEnabled) {
        isPolygonColorApprox = isEnabled;
    }


    public int getSplinesRank() {
        return splinesRank;
    }

    public void setSplinesRank(int splinesRank) {
        this.splinesRank = splinesRank;
    }
}
