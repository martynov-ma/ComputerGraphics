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

import cg.computergraphics.tools.*;
import cg.computergraphics.tools.brz.BrzCircle;
import cg.computergraphics.tools.brz.BrzLine;
import cg.computergraphics.tools.curve.BSpline;
import cg.computergraphics.tools.curve.BezierCurve;
import cg.computergraphics.tools.curve.HermiteCurve;
import cg.computergraphics.tools.curve.NURBSpline;
import cg.computergraphics.tools.dda.DDACircle;
import cg.computergraphics.tools.dda.DDALine;
import cg.computergraphics.tools.fill.FloodFill;
import cg.computergraphics.kr.FirstFigure;
import cg.computergraphics.tools.Polygon;

/**
 * Created by MAX on 03.03.2017.
 */

public class MyView extends View {

    private Bitmap mainBitmap;
    private Bitmap fakeBitmap;

    private Paint paint;
    private DrawingTool drawingTool;
    private int selectedTool;

    private GestureDetector gestureDetector;
    private Scroller scroller;  //считает скроллинг
    private int scrollX, scrollY;   //координаты скроллинга

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mainBitmap = Bitmap.createBitmap(AppSettings.getInstance().getBitmapWidth(), AppSettings.getInstance().getBitmapHeight(), Bitmap.Config.ARGB_8888);
        fakeBitmap = Bitmap.createBitmap(AppSettings.getInstance().getBitmapWidth(), AppSettings.getInstance().getBitmapHeight(), Bitmap.Config.ARGB_8888);

        paint = new Paint();

        scroller = new Scroller(context);
        gestureDetector = new GestureDetector(context, new MyGestureListener());
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(mainBitmap, 0, 0, paint);
        canvas.drawBitmap(fakeBitmap, 0, 0, paint);
        invalidate();
    }

    public void setDrawingTool(int selectedTool) {

        this.selectedTool = selectedTool;
        switch (this.selectedTool) {
            case 1:
                drawingTool = new Brush(mainBitmap);
                break;
            case 2:
                switch (AppSettings.getInstance().getLineDrawingAlgorithm()) {
                    case 0:
                        drawingTool = new DDALine(mainBitmap, fakeBitmap);
                        break;
                    case 1:
                        drawingTool = new BrzLine(mainBitmap, fakeBitmap);
                        break;
                }
                break;
            case 3:
                drawingTool = new BezierCurve(mainBitmap, fakeBitmap);
                break;
            case 4:
                drawingTool = new FloodFill(mainBitmap);
                break;
            case 5:
                switch (AppSettings.getInstance().getCircleDrawingAlgorithm()) {
                    case 0:
                        drawingTool = new DDACircle(mainBitmap, fakeBitmap);
                        break;
                    case 1:
                        drawingTool = new BrzCircle(mainBitmap, fakeBitmap);
                        break;
                }
                break;
            case 6:
                drawingTool = new Rectangle(mainBitmap, fakeBitmap);
                break;
            case 7:
                drawingTool = new Polygon(mainBitmap, fakeBitmap);
                break;
            case 8:
                drawingTool = new FirstFigure(mainBitmap, fakeBitmap);
                break;
            case 9:
                drawingTool = new HermiteCurve(mainBitmap, fakeBitmap);
                break;
            case 10:
                drawingTool = new BSpline(mainBitmap, fakeBitmap);
                break;
            case 11:
                drawingTool = new NURBSpline(mainBitmap, fakeBitmap);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (AppSettings.getInstance().isScrollEnabled()) {
            gestureDetector.onTouchEvent(event);
        }
        else {
            try {
                drawingTool.onTouch(event);
            } catch (IllegalArgumentException ignored) {}
            invalidate();
        }
        return true;
    }

    public void cleanScreen() {
        mainBitmap.eraseColor(0);
        fakeBitmap.eraseColor(0);
        if (selectedTool == 3) ((BezierCurve) drawingTool).cleanRefPoints();
        if (selectedTool == 7) ((Polygon) drawingTool).cleanVertices();
        if (selectedTool == 9) ((HermiteCurve) drawingTool).cleanPoints();
        if (selectedTool == 10) ((BSpline) drawingTool).cleanPoints();
        if (selectedTool == 11) ((NURBSpline) drawingTool).cleanPoints();
        invalidate();
    }

    public void setBitmapScale(int n) {
        AppSettings.getInstance().setBitmapScale(n);
        setScaleX(n);
        setScaleY(n);
        setPivotX(0);
        setPivotY(0);
    }

    public void drawMosaic() {
        int mosaicSize = AppSettings.getInstance().getMosaicSize();
        int bitmapWidth = AppSettings.getInstance().getBitmapWidth();
        int bitmapHeight = AppSettings.getInstance().getBitmapHeight();

        Canvas canvas = new Canvas(mainBitmap);
        Random r = new Random();
        for (int i = 0; i < bitmapWidth; i += mosaicSize) {
            for (int j = 0; j < bitmapHeight; j += mosaicSize) {
                paint.setColor(r.nextInt()%Color.rgb(125, 125, 125) + Color.rgb(125, 125, 125));
                canvas.drawRect(i, j, i + mosaicSize, j + mosaicSize, paint);
            }
        }
    }


    public void setBitmap(Bitmap bitmap) {
        mainBitmap = bitmap;
        fakeBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        AppSettings.getInstance().setBitmapWidth(bitmap.getWidth());
        AppSettings.getInstance().setBitmapHeight(bitmap.getHeight());
        setDrawingTool(selectedTool);
        invalidate();
    }

    public void updateBitmap() {
        mainBitmap = Bitmap.createBitmap(AppSettings.getInstance().getBitmapWidth(), AppSettings.getInstance().getBitmapHeight(), Bitmap.Config.ARGB_8888);
        fakeBitmap = Bitmap.createBitmap(AppSettings.getInstance().getBitmapWidth(), AppSettings.getInstance().getBitmapHeight(), Bitmap.Config.ARGB_8888);
        invalidate();
    }

    public Bitmap getMainBitmap() {
        return mainBitmap;
    }

    public Bitmap getFakeBitmap() {
        return fakeBitmap;
    }

    public DrawingTool getDrawingTool() {
        return drawingTool;
    }

    public int getSelectedTool() {
        return selectedTool;
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
            if (scrollX + X > 0 && scrollX + X < AppSettings.getInstance().getBitmapWidth() - getWidth()) {   //plan.getWidth()
                scrollBy((int) X, 0);
            }
            if (scrollY + Y > 0 && scrollY + Y < AppSettings.getInstance().getBitmapWidth() - getHeight()) {  //plan.getHeight()
                scrollBy(0, (int) Y);
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
            scroller.fling(scrollX, scrollY, -(int)velocityX, -(int)velocityY, 0, AppSettings.getInstance().getBitmapWidth() - getWidth(), 0, AppSettings.getInstance().getBitmapWidth() - getHeight());
            invalidate();
            return true;
        }

    }
}
