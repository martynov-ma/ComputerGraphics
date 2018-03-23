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
 * Created by MAX on 31.05.2017.
 */

public class BSpline extends DrawingTool {

    private int n;
    private int rank;
    private double[] N;
    private ArrayList<Point> points;

    private float step;
    private int size;

    private boolean isEditingPoint = false;
    private Point editPoint;

    private BrzLine painter;

    public BSpline(Bitmap mainBitmap, Bitmap fakeBitmap) {
        super(mainBitmap, fakeBitmap);
        n = 0;
        rank = AppSettings.getInstance().getSplinesRank();
        points = new ArrayList<>();
        step = 0.002F;
        size = 0;

        painter = new BrzLine(mainBitmap, fakeBitmap);
        painter.setColor(Color.RED);
    }

    private void drawBSpline(ArrayList<Point> points, Bitmap bitmap) {
        super.getFakeBitmap().eraseColor(0);
        drawStroke(points, bitmap);
        int n = points.size();
        if (n != this.n) {
            System.out.println("!=");
            this.n = n;
            N = recalculateN();
        }

        float resX, resY;
        int k = 0;
        for (int i = 0; i < size; i++) {
            resX = 0;
            resY = 0;
            for (int j = 0; j < this.n; j++) {
                resX += points.get(j).x * N[k];
                resY += points.get(j).y * N[k];
                k++;
            }
            try {
                bitmap.setPixel((int) resX, (int) resY, super.getColor());
            } catch (Exception ignored) {}
        }
    }

    private double[] recalculateN() {
        int t[] = new int[n + rank + 1];

        int k = 0;
        while (k <= rank) {
            t[k++] = 0;
        }
        while (k < n) {
            t[k] = k - rank + 1;
            k++;
        }
        int temp = n - rank + 2;
        while (k <= n + rank) {
            t[k] = temp;
            k++;
        }

        float u = .0F;
        size = Math.round(temp / step) + 1;
        double N[] = new double[size * n];

        int i = 0;
        while (u <= temp) {
            for (int j = 0; j < n; j++) {
                N[i++] = N(j, rank, u, t);
            }
            u += step;
        }
        return N;
    }

    @Override
    public void onTouch(MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (points.size() < rank) {
                    addRefPoints(x, y);
                    points.add(new Point(x, y));
                    if (points.size() == rank) {
                        drawBSpline(points, super.getFakeBitmap());
                    }
                } else {
                    check(x, y);
                    if (!isEditingPoint) {
                        points.add(new Point(x, y));
                        drawBSpline(points, super.getFakeBitmap());
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isEditingPoint) {
                    editPoint.x = x;
                    editPoint.y = y;
                    drawBSpline(points, super.getFakeBitmap());
                } else {
                    points.set(points.size() - 1, new Point(x, y));
                    drawBSpline(points, super.getFakeBitmap());
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isEditingPoint) isEditingPoint = false;
                break;
        }
    }

    private float N(int i, int k, float u, int[] t) {
        if (k == 0) {
            if (u >= t[i] && u < t[i + 1]) {
                return 1;
            } else {
                return 0;
            }
        } else {
            float ch1 = (u - t[i]) * N(i, k - 1, u, t);
            int zn1 = (t[i + k] - t[i]);

            if (zn1 == 0) {
                ch1 = 0;
            } else {
                ch1 /= zn1;
            }

            float ch2 = (t[i + k + 1] - u) * N(i + 1, k - 1, u, t);
            int zn2 = (t[i + k + 1] - t[i + 1]);

            if (zn2 == 0) {
                ch2 = 0;
            } else {
                ch2 /= zn2;
            }

            return ch1 + ch2;
        }
    }

    private void addRefPoints(int x, int y) {
        for (int i = 0; i < rank; i++) {
            points.add(new Point(x, y));
        }
    }

    private void check(int x, int y) {
        int n = AppSettings.getInstance().getBitmapScale();
        for (Point point: points) {
            if (x < point.x + 60 / n && x > point.x - 60 / n &&
                    y < point.y + 60 / n && y > point.y - 60 / n) {
                editPoint = point;
                isEditingPoint = true;
                return;
            }
        }
    }

    private void drawStroke(ArrayList<Point> points, Bitmap bitmap) {
        for (int i = 0; i < points.size() - 1; i += 1) {
            painter.drawBrzLine(points.get(i).x, points.get(i).y,
                    points.get(i + 1).x, points.get(i + 1).y,
                    bitmap, RenderingType.SOLID);
        }
    }

    public void cleanPoints() {
        points.clear();
    }
}
