package cg.computergraphics.tools.curve;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.view.MotionEvent;

import java.util.ArrayList;

import cg.computergraphics.AppSettings;
import cg.computergraphics.MainActivity;
import cg.computergraphics.tools.DrawingTool;
import cg.computergraphics.tools.RenderingType;
import cg.computergraphics.tools.brz.BrzLine;

/**
 * Created by MAX on 26.05.2017.
 */

public class HermiteCurve extends DrawingTool {

    private ArrayList<Point> points;
    private boolean isEditingPoint = false;
    private Point editPoint;

    private BrzLine painter;

    public HermiteCurve(Bitmap mainBitmap, Bitmap fakeBitmap) {
        super(mainBitmap, fakeBitmap);
        points = new ArrayList<>();
        painter = new BrzLine(mainBitmap, fakeBitmap);
        painter.setColor(Color.RED);
    }

    public void drawHermiteCurve(ArrayList<Point> points, Bitmap bitmap) {
        super.getFakeBitmap().eraseColor(0);
        drawVectors(points, bitmap);

        float step = 0.0003F;
        float t;
        int x, y;
        // [ 01  23 ] [ 45  67 ]
        // [[xy][xy]] [[xy][xy]]
        for (int i = 0; i < points.size() - 2; i += 2) {
            int v1X, v1Y, v2X, v2Y;
            v1X = 3 * (points.get(i + 1).x - points.get(i).x);
            v2X = 3 * (points.get(i + 3).x - points.get(i + 2).x);
            v1Y = 3 * (points.get(i + 1).y - points.get(i).y);
            v2Y = 3 * (points.get(i + 3).y - points.get(i + 2).y);
            t = .0F;
            while (t <= 1) {
                x = (int) ((1 - 3 * t * t + 2 * t * t * t) * points.get(i).x
                        + t * t * (3 - 2 * t) * points.get(i + 2).x
                        + t * (1 - 2 * t + t * t) * v1X
                        - t * t * (1 - t) * v2X);

                y = (int) ((1 - 3 * t * t + 2 * t * t * t) * points.get(i).y
                        + t * t * (3 - 2 * t) * points.get(i + 2).y
                        + t * (1 - 2 * t + t * t) * v1Y
                        - t * t * (1 - t) * v2Y);
                try {
                    bitmap.setPixel(x, y, super.getColor());
                } catch (Exception ignored) {}

                t += step;
            }
        }
    }

    @Override
    public void onTouch(MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (points.size() < 4) {
                    addRefPoint(x, y);
                    if (points.size() == 4) {
                        drawHermiteCurve(points, super.getFakeBitmap());
                    }
                } else {
                    check(x, y);
                    if (!isEditingPoint) {
                        addRefPoint(x, y);
                        drawHermiteCurve(points, super.getFakeBitmap());
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (points.size() < 4) {
                    addRefPoint(x, y);
                } else {
                    if (isEditingPoint) {
                        editPoint.x = x;
                        editPoint.y = y;
                        drawHermiteCurve(points, super.getFakeBitmap());
                    } else {
                        points.set(points.size() - 1, new Point(x, y));
                        points.set(points.size() - 2, new Point(x, y));
                        drawHermiteCurve(points, super.getFakeBitmap());
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isEditingPoint) isEditingPoint = false;
                break;
        }
    }

    private void addRefPoint(int x, int y) {
        points.add(new Point(x, y));
        points.add(new Point(x, y));
    }

    private void drawVectors(ArrayList<Point> points, Bitmap bitmap) {
        for (int i = 0; i < points.size() - 1; i += 2) {
            painter.drawBrzLine(points.get(i).x, points.get(i).y,
                    points.get(i + 1).x, points.get(i + 1).y,
                    bitmap, RenderingType.SOLID);
        }
    }

    private void check(int x, int y) {
        int n = AppSettings.getInstance().getBitmapScale();
        for (int i = 0; i < points.size(); i++) {
            if (x < points.get(i).x + 60 / n && x > points.get(i).x - 60 / n &&
                    y < points.get(i).y + 60 / n && y > points.get(i).y - 60 / n) {
                if (i != points.size() - 1) {
                    if (points.get(i).x == points.get(i + 1).x && points.get(i).y == points.get(i + 1).y) {
                        editPoint = points.get(i + 1);
                    } else {
                        editPoint = points.get(i);
                    }
                } else editPoint = points.get(i);
                isEditingPoint = true;
                return;
            }
        }
    }

    public void cleanPoints() {
        points.clear();
    }
}
