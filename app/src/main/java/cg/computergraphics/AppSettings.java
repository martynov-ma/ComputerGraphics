package cg.computergraphics;

import android.graphics.Color;

/**
 * Created by MAX on 18.03.2017.
 */

public class AppSettings {

    private static AppSettings appSettings = null;

    public static AppSettings getInstance() {
        if (appSettings == null) {
            appSettings = new AppSettings();
        }
        return appSettings;
    }

    private int bitmapWidth, bitmapHeight, bitmapScale;
    private int lineDrawingAlgorithm, circleDrawingAlgorithm;

    //private boolean isObj
    private boolean isObjFilling, isObjRandomColor;
    private boolean isScrollEnabled, isFillEnabled;
    private boolean isLineColorApprox, isPolygonColorApprox;

    private int defaultTool, mosaicSize, drawingColor, splinesRank;

    private AppSettings() {
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

    public int getBitmapWidth() {
        return bitmapWidth;
    }

    public void setBitmapWidth(int bitmapWidth) {
        this.bitmapWidth = bitmapWidth;
    }

    public int getBitmapHeight() {
        return bitmapHeight;
    }

    public void setBitmapHeight(int bitmapHeight) {
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


    public int getLineDrawingAlgorithm() {
        return lineDrawingAlgorithm;
    }

    public void setLineDrawingAlgorithm(int algorithmId) {
        lineDrawingAlgorithm = algorithmId;
    }

    public int getCircleDrawingAlgorithm() {
        return circleDrawingAlgorithm;
    }

    public void setCircleDrawingAlgorithm(int algorithmId) {
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


    public int getMosaicSize() {
        return mosaicSize;
    }

    public void setMosaicSize(int mosaicSize) {
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
