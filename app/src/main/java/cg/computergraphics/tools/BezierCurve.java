package cg.computergraphics.tools;

import android.graphics.Bitmap;
import android.view.MotionEvent;

import java.util.ArrayList;

import cg.computergraphics.Vertex;

/**
 * Created by MAX on 14.03.2017.
 */

public class BezierCurve extends DrawingTool {

    private ArrayList<Vertex> refPoints;
    private BrzLine painter;

    public BezierCurve(Bitmap mainBitmap, Bitmap fakeBitmap) {
        super(mainBitmap, fakeBitmap);
        refPoints = new ArrayList<>();
        painter = new BrzLine(mainBitmap, null);
    }

    @Override
    public int getColor() {
        return painter.getColor();
    }

    @Override
    public void setColor(int color) {
        painter.setColor(color);
    }

    public void cleanRefPoints() {
        refPoints.clear();
    }

    private void drawBezierCurve(ArrayList<Vertex> refPoints, Bitmap bitmap, RenderingType renderingType) {
        int m = refPoints.size();
        float x1, y1, x2, y2, newPointX, newPointY;
        float oldX = refPoints.get(0).getX();
        float oldY = refPoints.get(0).getY();
        ArrayList<Vertex> temp = (ArrayList<Vertex>) refPoints.clone();

        for (float t = 0; t <= 1; t += 0.01) {
            for (int i = m - 1; i > 0; i--) {
                for (int j = 0; j < i; j++) {
                    x1 = temp.get(j).getX();
                    y1 = temp.get(j).getY();
                    x2 = temp.get(j + 1).getX();
                    y2 = temp.get(j + 1).getY();
                    newPointX = x1 + t * (x2 - x1);
                    newPointY = y1 + t * (y2 - y1);
                    Vertex newPoint = new Vertex(newPointX, newPointY);
                    temp.set(j, newPoint);
                }
            }
            painter.drawBrzLine(oldX, oldY, temp.get(0).getX(), temp.get(0).getY(), bitmap, renderingType);
            oldX = temp.get(0).getX();
            oldY = temp.get(0).getY();
        }
    }

    @Override
    public void onTouch(MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (refPoints.size() == 0) {
                    refPoints.add(new Vertex(x, y));
                } else if (refPoints.size() >= 2) {
                    drawBezierCurve(refPoints, super.getFakeBitmap(), RenderingType.ERASE);
                    refPoints.add(refPoints.size() - 1, new Vertex(x, y));
                    drawBezierCurve(refPoints, super.getFakeBitmap(), RenderingType.SOLID);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (refPoints.size() < 2) {
                    refPoints.add(new Vertex(x, y));
                    drawBezierCurve(refPoints, super.getFakeBitmap(), RenderingType.SOLID);
                } else if (refPoints.size() == 2) {
                    drawBezierCurve(refPoints, super.getFakeBitmap(), RenderingType.ERASE);
                    refPoints.set(refPoints.size() - 1, new Vertex(x, y));
                    drawBezierCurve(refPoints, super.getFakeBitmap(), RenderingType.SOLID);
                } else if (refPoints.size() > 2) {
                    drawBezierCurve(refPoints, super.getFakeBitmap(), RenderingType.ERASE);
                    refPoints.set(refPoints.size() - 2, new Vertex(x, y));
                    drawBezierCurve(refPoints, super.getFakeBitmap(), RenderingType.SOLID);
                }
                break;
        }
    }

    @Override
    public void transferToMainBitmap() {
        drawBezierCurve(refPoints, super.getMainBitmap(), RenderingType.SOLID);
    }
}
