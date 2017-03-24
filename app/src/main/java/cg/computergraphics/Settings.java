package cg.computergraphics;

/**
 * Created by MAX on 18.03.2017.
 */

class Settings {
    private int bitmapWidth;
    private int bitmapHeight;

    private int lineDrawingAlgorithm;
    private int circleDrawingAlgorithm;

    private int mosaicSize;

    Settings() {
        bitmapWidth = 2000;
        bitmapHeight = 2000;

        lineDrawingAlgorithm = 0;
        circleDrawingAlgorithm = 0;

        mosaicSize = 10;
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

}
