package cg.computergraphics.kr;

import android.graphics.Bitmap;
import android.view.MotionEvent;

import cg.computergraphics.MainActivity;
import cg.computergraphics.Vertex;
import cg.computergraphics.tools.DDALine;
import cg.computergraphics.tools.DrawingTool;
import cg.computergraphics.tools.fill.FloodFill;
import cg.computergraphics.tools.RenderingType;

/**
 * Created by MAX on 15.04.2017.
 */

public class FirstFigure extends DrawingTool {
    private DDALine painter;
    private FloodFill filler;
    private Vertex points[];
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    public FirstFigure(Bitmap mainBitmap, Bitmap fakeBitmap) {
        super(mainBitmap, fakeBitmap);
        painter = new DDALine(mainBitmap, null);
        filler = new FloodFill(mainBitmap);
        points = new Vertex[11];
    }

    private void drawFigure(int x1, int y1, int x2, int y2, Bitmap bitmap, RenderingType renderingType) {
        float a = Math.abs(x2 - x1);
        float b = Math.abs(y2 - y1);

        int directionX = (int) Math.signum(x2 - x1);
        int directionY = (int) Math.signum(y2 - y1);

        int th = Math.round(b / 5);
        int tw = Math.round(a / 5);

        points[0] = new Vertex(x1, y1 + 5 * th * directionY);
        points[1] = new Vertex(x1, y1 + 2 * th * directionY);
        points[2] = new Vertex(x1 + 2 * tw * directionX, y1 + 2 * th * directionY);
        points[3] = new Vertex(x1 + 2 * tw * directionX, y1 + th * directionY);
        points[4] = new Vertex(x1 + tw * directionX, y1 + th * directionY);
        points[5] = new Vertex(x1 + a / 2 * directionX, y1);
        points[6] = new Vertex(x1 + 4 * tw * directionX, y1 + th * directionY);
        points[7] = new Vertex(x1 + 3 * tw * directionX, y1 + th * directionY);
        points[8] = new Vertex(x1 + 3 * tw * directionX, y1 + 2 * th * directionY);
        points[9] = new Vertex(x1 + 5 * tw * directionX, y1 + 2 * th * directionY);
        points[10] = (new Vertex(x1 + 5 * tw * directionX, y1 + 5 * th * directionY));

        for (int i = 0; i < 11; i++) {
            painter.drawDDALine(points[i].getX(), points[i].getY(),
                                points[(i + 1) % 11].getX(), points[(i + 1) % 11].getY(),
                                bitmap, renderingType);
        }
    }

    private void fillFigure(int x1, int y1, int x2, int y2) {
        float a = Math.abs(x2 - x1);
        float b = Math.abs(y2 - y1);

        int directionX = (int) Math.signum(x2 - x1);
        int directionY = (int) Math.signum(y2 - y1);

        filler.fillBackground(x1 + (int) a / 2 * directionX, y1 + (int) b / 2 * directionY);
    }


        @Override
    public void onTouch(MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x1 = x;
                    y1 = y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    drawFigure(x1, y1, x2, y2, super.getFakeBitmap(), RenderingType.ERASE);
                    x2 = x;
                    y2 = y;
                    drawFigure(x1, y1, x, y, super.getFakeBitmap(), RenderingType.SOLID);
                    break;
                case MotionEvent.ACTION_UP:
                    drawFigure(x1, y1, x, y, super.getFakeBitmap(), RenderingType.ERASE);
                    drawFigure(x1, y1, x, y, super.getMainBitmap(), RenderingType.SOLID);
                    if (MainActivity.appSettings.isFillEnabled()) {
                        fillFigure(x1, y1, x, y);
                    }
                    break;
            }
    }
}
