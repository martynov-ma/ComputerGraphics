package cg.computergraphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import java.util.Random;

import cg.computergraphics.tools.BezierCurve;
import cg.computergraphics.tools.Brush;
import cg.computergraphics.tools.BrzCircle;
import cg.computergraphics.tools.BrzLine;
import cg.computergraphics.tools.DrawingTool;
import cg.computergraphics.tools.DDACircle;
import cg.computergraphics.tools.DDALine;

/**
 * Created by MAX on 03.03.2017.
 */

public class MyView extends View {

    private Bitmap mainBitmap;
    private Bitmap fakeBitmap;
    private int bitmapScale;

    private Paint paint;
    private DrawingTool drawingTool;
    private int selectedTool;

    private GestureDetector gd;
    private Scroller scroller;  //считает скроллинг
    private boolean isScrollEnabled;
    private int scrollX, scrollY;   //координаты скроллинга

    private AppSettings appSettings;


    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);

        appSettings = MainActivity.appSettings;

        mainBitmap = Bitmap.createBitmap(appSettings.getBitmapWidth(), appSettings.getBitmapHeight(), Bitmap.Config.ARGB_8888);
        fakeBitmap = Bitmap.createBitmap(appSettings.getBitmapWidth(), appSettings.getBitmapHeight(), Bitmap.Config.ARGB_8888);
        bitmapScale = 1;

        paint = new Paint();
        paint.setColor(Color.BLACK);

        scroller = new Scroller(context);
        gd = new GestureDetector(context, new MyGestureListener());
        isScrollEnabled = false;
    }

    public void setBitmap(Bitmap bitmap) {
        mainBitmap = bitmap;
        fakeBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        appSettings.setBitmapWidth(bitmap.getWidth());
        appSettings.setBitmapHeight(bitmap.getHeight());
        setDrawingTool(selectedTool);
        invalidate();
    }

    public void updateBitmap() {
        mainBitmap = Bitmap.createBitmap(appSettings.getBitmapWidth(), appSettings.getBitmapHeight(), Bitmap.Config.ARGB_8888);
        fakeBitmap = Bitmap.createBitmap(appSettings.getBitmapWidth(), appSettings.getBitmapHeight(), Bitmap.Config.ARGB_8888);
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(mainBitmap, 0, 0, paint);
        canvas.drawBitmap(fakeBitmap, 0, 0, paint);
    }

    public void setDrawingTool(int selectedTool) {

        this.selectedTool = selectedTool;
        switch (this.selectedTool) {
            case 1:
                drawingTool = new Brush(mainBitmap);
                break;
            case 2:
                switch (appSettings.getLineDrawingAlgorithm()) {
                    case 0:
                        drawingTool = new DDALine(mainBitmap, fakeBitmap);
                        break;
                    case 1:
                        drawingTool = new BrzLine(mainBitmap, fakeBitmap);
                        break;

                    default:
                        drawingTool = new DDALine(mainBitmap, fakeBitmap);
                        break;
                }
                break;
            case 3:
                switch (appSettings.getCircleDrawingAlgorithm()) {
                    case 0:
                        drawingTool = new DDACircle(mainBitmap, fakeBitmap);
                        break;
                    case 1:
                        drawingTool = new BrzCircle(mainBitmap, fakeBitmap);
                        break;

                    default:
                        drawingTool = new DDACircle(mainBitmap, fakeBitmap);
                        break;
                }
                break;
            case 4:
                drawingTool = new BezierCurve(mainBitmap, fakeBitmap);
                break;
        }
    }

    public Bitmap getMainBitmap() {
        return mainBitmap;
    }

    public DrawingTool getDrawingTool() {
        return drawingTool;
    }

    public int getSelectedTool() {
        return selectedTool;
    }

    public void setScroll(boolean isEnabled) {
        isScrollEnabled = isEnabled;
    }

    public int getBitmapScale() {
        return bitmapScale;
    }

    public void setBitmapScale(int n) {
        bitmapScale = n;
        setScaleX(n);
        setScaleY(n);
        setPivotX(0);
        setPivotY(0);
    }

    public void cleanScreen() {
        mainBitmap.eraseColor(0);
        fakeBitmap.eraseColor(0);
        if (selectedTool == 4) ((BezierCurve)drawingTool).cleanRefPoints();
        invalidate();
    }

    public Bitmap getMosaic() {
        int mosaicSize = appSettings.getMosaicSize();
        int bitmapWidth = appSettings.getBitmapWidth();
        int bitmapHeight = appSettings.getBitmapHeight();

        Bitmap mosaicBmp = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mosaicBmp);;
        Random r = new Random();
        for (int i = 0; i < bitmapWidth; i += mosaicSize) {
            for (int j = 0; j < bitmapHeight; j += mosaicSize) {
                paint.setColor(r.nextInt()%Color.rgb(125, 125, 125) + Color.rgb(125, 125, 125));
                canvas.drawRect(i, j, i + mosaicSize, j + mosaicSize, paint);
            }
        }
        paint.setColor(Color.BLACK);
        return mosaicBmp;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isScrollEnabled) {
            gd.onTouchEvent(event);
        }
        else {
            try {
                drawingTool.onTouch(event);
            } catch (IllegalArgumentException ignored) {}
            invalidate();
        }
        return true;
    }

    // Вызывается системой для пересчета скроллинга.
    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            int x = scroller.getCurrX();
            int y = scroller.getCurrY();
            scrollTo(x, y);
            if (scrollX != getScrollX() || scrollY != getScrollY()) {
                // Если изменились координаты, то обновляем координаты
                onScrollChanged(getScrollX(), getScrollY(), scrollX, scrollY);
            }
            invalidate();
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        scrollX = l;
        scrollY = t;
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onScroll(MotionEvent me1, MotionEvent me2, float X, float Y){
            scroller.abortAnimation();
            if (scrollX + X > 0 && scrollX + X < appSettings.getBitmapWidth() - getWidth()) {   //plan.getWidth()
                scrollBy((int) X, 0);
            }
            if (scrollY + Y > 0 && scrollY + Y < appSettings.getBitmapWidth() - getHeight()) {  //plan.getHeight()
                scrollBy(0, (int) Y);
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
            scroller.fling(scrollX, scrollY, -(int)velocityX, -(int)velocityY, 0, appSettings.getBitmapWidth() - getWidth(), 0, appSettings.getBitmapWidth() - getHeight());
            invalidate();
            return true;
        }

    }
}
