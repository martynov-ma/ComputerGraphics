package cg.computergraphics.tools;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;

import java.util.ArrayList;

import cg.computergraphics.MyView;
import cg.computergraphics.Vertex;

/**
 * Created by MAX on 14.03.2017.
 */

public class BezierCurve extends DrawingTool {

    private ArrayList<Vertex> bezierCurveRefPoints;
    private ParamLine painter;

    public BezierCurve(Bitmap mainBitmap, Bitmap fakeBitmap) {
        super(mainBitmap, fakeBitmap);
        bezierCurveRefPoints = new ArrayList<>();

        painter = new ParamLine(mainBitmap, null);
        painter.setColor(0);
    }

    public void cleanRefPoints() {
        bezierCurveRefPoints.clear();
    }

    private void drawBezierCurve(ArrayList<Vertex> refPoints, Bitmap bitmap) {
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
            painter.drawParamLine(oldX, oldY, temp.get(0).getX(), temp.get(0).getY(), bitmap);
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
                if (bezierCurveRefPoints.size() == 0) {
                    bezierCurveRefPoints.add(new Vertex(x, y));
                } else if (bezierCurveRefPoints.size() >= 2) {
                    painter.setColor(0);
                    drawBezierCurve(bezierCurveRefPoints, super.getFakeBitmap());
                    bezierCurveRefPoints.add(bezierCurveRefPoints.size() - 1, new Vertex(x, y));
                    painter.setColor(Color.BLACK);
                    drawBezierCurve(bezierCurveRefPoints, super.getFakeBitmap());
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (bezierCurveRefPoints.size() < 2) {
                    bezierCurveRefPoints.add(new Vertex(x, y));
                    painter.setColor(Color.BLACK);
                    drawBezierCurve(bezierCurveRefPoints, super.getFakeBitmap());
                } else if (bezierCurveRefPoints.size() == 2) {
                    painter.setColor(0);
                    drawBezierCurve(bezierCurveRefPoints, super.getFakeBitmap());
                    bezierCurveRefPoints.set(bezierCurveRefPoints.size() - 1, new Vertex(x, y));
                    painter.setColor(Color.BLACK);
                    drawBezierCurve(bezierCurveRefPoints, super.getFakeBitmap());
                } else if (bezierCurveRefPoints.size() > 2){
                    painter.setColor(0);
                    drawBezierCurve(bezierCurveRefPoints, super.getFakeBitmap());
                    bezierCurveRefPoints.set(bezierCurveRefPoints.size() - 2, new Vertex(x, y));
                    painter.setColor(Color.BLACK);
                    drawBezierCurve(bezierCurveRefPoints, super.getFakeBitmap());
                }
                break;
        }
    }
}
