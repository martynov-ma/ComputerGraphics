package cg.computergraphics.tools;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;

import cg.computergraphics.MainActivity;
import cg.computergraphics.tools.brz.BrzLine;

/**
 * Created by MAX on 19.04.2017.
 */

public class Polygon extends DrawingTool {

    private ArrayList<Point> vertices;
    private BrzLine painter;
    private int fillColor;

    private boolean editingExistingVertex = false;
    private Point editVertex;

    public Polygon(Bitmap mainBitmap, Bitmap fakeBitmap) {
        super(mainBitmap, fakeBitmap);
        vertices = new ArrayList<>();
        fillColor = MainActivity.appSettings.getDrawingColor();
        painter = new BrzLine(mainBitmap, fakeBitmap);
        painter.setColor(Color.BLACK);
    }

    @Override
    public int getColor() {
        return fillColor;
    }

    @Override
    public void setColor(int color) {
        fillColor = color;
    }

    public void cleanVertices() {
        vertices.clear();
    }

    private void drawPolygon(ArrayList<Point> vertices, Bitmap bitmap) {
        super.getFakeBitmap().eraseColor(0);
        if (MainActivity.appSettings.isFillEnabled()) {
            fillPolygon(vertices, bitmap);
        }
        for (int i = 0; i < vertices.size(); i++) {
            painter.drawBrzLine(vertices.get(i).x,
                                vertices.get(i).y,
                                vertices.get((i + 1) % vertices.size()).x,
                                vertices.get((i + 1) % vertices.size()).y,
                                bitmap, RenderingType.SOLID);
        }
    }

    public void fillPolygon(ArrayList<Point> vertices, Bitmap bitmap) {
        ArrayList<Edge> edges = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            edges.add(new Edge(vertices.get(i), vertices.get((i + 1) % vertices.size())));
        }
        sort(edges);

        int minY = edges.get(0).getStartPoint().y;
        int maxY = edges.get(edges.size() - 1).getEndPoint().y;
        for (Edge edge : edges) {
            if (edge.getEndPoint().y > maxY) maxY = edge.getEndPoint().y;
        }

        //color approximation
        int fillColor;
        int delY = maxY - minY;
        float r1 = 0, g1 = 0, b1 = 0, delR = 0, delG = 0, delB = 0;
        if (MainActivity.appSettings.isPolygonColorApprox()) {
            int startColor = Color.rgb(255, 0, 0);
            int endColor = Color.rgb(0, 0, 255);
            r1 = Color.red(startColor);
            g1 = Color.green(startColor);
            b1 = Color.blue(startColor);
            float r2 = Color.red(endColor), g2 = Color.green(endColor), b2 = Color.blue(endColor);

            delR = (r2 - r1) / delY;
            delG = (g2 - g1) / delY;
            delB = (b2 - b1) / delY;

            fillColor = Color.rgb(Math.round(r1), Math.round(g1), Math.round(b1));
        } else fillColor = this.fillColor;

        int activeEdgesCount = 0;
        ArrayList<Integer> intersectionPoints = new ArrayList<>();
        for (int currentY = minY; currentY <= maxY; currentY++) {
            while (activeEdgesCount < edges.size() && edges.get(activeEdgesCount).getStartPoint().y <= currentY) {
                activeEdgesCount++;
            }

            intersectionPoints.clear();
            for (int i = 0; i < activeEdgesCount; i++) {
                if (edges.get(i).isActive()) {
                    intersectionPoints.add(edges.get(i).getCurrentX());
                }
            }
            sort(intersectionPoints, intersectionPoints.size());

            for (int i = 0; i < intersectionPoints.size(); i += 2) {
                for (int x = intersectionPoints.get(i + 1); x > intersectionPoints.get(i); x--) {
                    bitmap.setPixel(x, currentY, fillColor);
                }
            }

            if (MainActivity.appSettings.isPolygonColorApprox()) {
                r1 += delR;
                g1 += delG;
                b1 += delB;
                fillColor = Color.rgb(Math.round(r1), Math.round(g1), Math.round(b1));
            }
        }
    }

//    public void fillPolygon(ArrayList<Point> vertices, Bitmap bitmap) {
//        edges.clear();
//        for (int i = 0; i < vertices.size(); i++) {
//            if ((vertices.get(i).y - vertices.get((i + 1) % vertices.size()).y) != 0) {
//                edges.add(new Edge(vertices.get(i), vertices.get((i + 1) % vertices.size())));
//            }
//        }
//        if (edges.size() == 0) return;
//        quickSort(edges, 0, edges.size() - 1);
//
//        int minY = edges.get(0).getStartPoint().y;
//        int maxY = edges.get(edges.size() - 1).getEndPoint().y;
//
//        ArrayList<Edge> activeEdges = new ArrayList<>();
//        for (int currentY = minY; currentY < maxY; currentY++) {
//
//            //System.out.printf("before AET %d - %d\n", edges.size(), activeEdges.size());
//            //формируем список активных рёбер
//            Edge currentEdge;
//            for (int i = 0; i < edges.size(); i++) {
//                currentEdge = edges.get(i);
//                if (currentEdge.getStartPoint().y == currentY) {
//                    activeEdges.add(currentEdge);
//                    edges.remove(i);
//                    i -= 1;
//                }
//            }
//            //System.out.printf("after AET %d - %d\n", edges.size(), activeEdges.size());
//
//            //если список активных ребер пуст, то заполнение закончено
//            /*if (activeEdges.size() == 0) {
//                System.out.println("spisok pust");
//                return;
//            }*/
//
//            //определяем y–координату ближайшей вершины
//            int nextY;
//            if (edges.size() != 0) nextY = edges.get(0).getStartPoint().y;
//            else nextY = maxY;
//
//            ArrayList<Integer> intersectionPoints = new ArrayList<>();
//            for (int i = currentY; i <= nextY; i++) {
//                //System.out.printf("i %d - nextY %d\n", i, nextY);
//                //выбор из списка активных ребер x–координаты пересечений активных ребер со строкой сканирования
//                intersectionPoints.clear();
//                for (Edge edge : activeEdges) {
//                    intersectionPoints.add(edge.getCurrentX());
//                }
//                if (intersectionPoints.size() % 2 != 0) return;
//
//                //сортировка
//                quickSort(intersectionPoints, 0, intersectionPoints.size() - 1);
//
//                for (int j = 0; j < intersectionPoints.size(); j += 2) {
//                    for (int k = intersectionPoints.get(j); k <= intersectionPoints.get(j + 1) + 1; k++) {
//                        bitmap.setPixel(k, i, fillColor);
//                    }
//                }
//
//            }
//            currentY = nextY;
//            if (currentY == maxY) return;
//
//            for (int i = 0; i < activeEdges.size(); i++) {
//                if (activeEdges.get(i).getEndPoint().y == currentY) {
//                    activeEdges.remove(i);
//                    i -= 1;
//                }
//            }
//
//            currentY -= 1;
//        }
//    }

    private void sort(ArrayList<Edge> edges) {
        for (int i = 0; i < edges.size() - 1; i++) {
            int min = i;
            for (int j = i + 1; j < edges.size(); j++) {
                if (edges.get(j).getStartPoint().y < edges.get(min).getStartPoint().y) {
                    min = j;
                }
            }
            Collections.swap(edges, i, min);
        }
    }

    private void sort(ArrayList<Integer> intersectionPoints, int size) {
        for (int i = 0; i < size - 1; i++) {
            int min = i;
            for (int j = i + 1; j < size; j++) {
                if (intersectionPoints.get(j) < intersectionPoints.get(min)) {
                    min = j;
                }
            }
            Collections.swap(intersectionPoints, i, min);
        }
    }

    @Override
    public void onTouch(MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (vertices.size() <= 2) {
                    vertices.add(new Point(x, y));
                } else {
                    check(x, y);
                    if (!editingExistingVertex) {
                        vertices.add(new Point(x, y));
                        drawPolygon(vertices, super.getFakeBitmap());
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (vertices.size() > 2){
                    if (editingExistingVertex) {
                        editVertex.x = x;
                        editVertex.y = y;
                        drawPolygon(vertices, super.getFakeBitmap());
                    } else {
                        vertices.set(vertices.size() - 1, new Point(x, y));
                        drawPolygon(vertices, super.getFakeBitmap());
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (vertices.size() > 2) {
                    if (editingExistingVertex) {
                        editingExistingVertex = false;
                    } else {
                        vertices.set(vertices.size() - 1, new Point(x, y));
                        drawPolygon(vertices, super.getFakeBitmap());
                    }
                }
                break;
        }
    }

    private void check(int x, int y) {
        int n = MainActivity.appSettings.getBitmapScale();
        for (Point vertex : vertices) {
            if (x < vertex.x + 60 / n && x > vertex.x - 60 / n &&
                y < vertex.y + 60 / n && y > vertex.y - 60 / n) {
                editVertex = vertex;
                editingExistingVertex = true;
                return;
            }
        }
    }

    @Override
    public void transferToMainBitmap() {
        if (vertices.size() != 0)
            drawPolygon(vertices, super.getMainBitmap());
    }

    private class Edge {
        private Point startPoint;
        private Point endPoint;
        private float currentX;
        private float delX;
        private int delY;

        Edge(Point firstPoint, Point secondPoint) {
            if (firstPoint.y < secondPoint.y) {
                startPoint = firstPoint;
                endPoint = secondPoint;
            } else {
                startPoint = secondPoint;
                endPoint = firstPoint;
            }
            currentX = startPoint.x;
            delY = endPoint.y - startPoint.y;
            delX = (endPoint.x - startPoint.x) / (float) delY;
        }

        Point getStartPoint() {
            return startPoint;
        }

        Point getEndPoint() {
            return endPoint;
        }

        int getCurrentX() {
            float temp = currentX;
            currentX += delX;
            delY--;
            return (int) temp;
        }

        boolean isActive() {
            return delY != 0;
        }
    }
}
