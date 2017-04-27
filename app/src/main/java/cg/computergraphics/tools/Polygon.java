package cg.computergraphics.tools;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;

import cg.computergraphics.MainActivity;

/**
 * Created by MAX on 19.04.2017.
 */

public class Polygon extends DrawingTool {

    private ArrayList<Point> vertices;
    private ArrayList<Edge> edges;
    private BrzLine painter;
    private int fillColor;

    private boolean editingExistingVertex = false;
    private Point editVertex;

    public Polygon(Bitmap mainBitmap, Bitmap fakeBitmap) {
        super(mainBitmap, fakeBitmap);
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
        fillColor = MainActivity.appSettings.getDrawingColor();
        painter = new BrzLine(mainBitmap, fakeBitmap);
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
        edges.clear();
    }



    private void drawPolygon(ArrayList<Point> vertices, Bitmap bitmap, RenderingType renderingType) {
        for (int i = 0; i < vertices.size(); i++) {
            painter.drawBrzLine(vertices.get(i).x,
                                vertices.get(i).y,
                                vertices.get((i + 1) % vertices.size()).x,
                                vertices.get((i + 1) % vertices.size()).y,
                                bitmap, renderingType);
        }
        //fillPolygon(bitmap, renderingType);
    }

    private void fillPolygon(Bitmap bitmap, RenderingType renderingType) {
        edges.clear();
        for (int i = 0; i < vertices.size(); i++) {
            edges.add(new Edge(vertices.get(i), vertices.get((i + 1) % vertices.size())));
        }

        //сортировка
        quickSort(edges, 0, edges.size() - 1);

        //проверка
        /*String str = "";
        for (Edge edge : edges) {
            str += edge.getStartPoint().y + " ";
        }
        System.out.printf("after sort %s\n", str);*/

        int minY = edges.get(0).getStartPoint().y;
        int maxY = edges.get(edges.size() - 1).getEndPoint().y;
        //System.out.printf("%d - %d\n", minY, maxY);

        ArrayList<Edge> activeEdges = new ArrayList<>();
        for (int currentY = minY; currentY < maxY; currentY++) {

            //System.out.printf("before AET %d - %d\n", edges.size(), activeEdges.size());
            //формируем список активных рёбер
            Edge currentEdge;
            for (int i = 0; i < edges.size(); i++) {
                currentEdge = edges.get(i);
                //System.out.printf("%d - %d\n", currentEdge.getStartPoint().y, currentY);
                if (currentEdge.getStartPoint().y == currentY) {
                    //System.out.println("true");
                    activeEdges.add(currentEdge);
                    edges.remove(i);
                    i -= 1;
                }
            }
            //System.out.printf("after AET %d - %d\n", edges.size(), activeEdges.size());

            //если список активных ребер пуст, то заполнение закончено
            if (activeEdges.size() == 0) {
                System.out.println("spisok pust");
                return;
            }

            //определяем y–координату ближайшей вершины
            int nextY = edges.get(0).getStartPoint().y;
            //System.out.printf("currentY %d - nextY %d\n", currentY, nextY);

            ArrayList<Float> intersectionPoints = new ArrayList<>();
            for (int i = currentY; i < nextY; i++) {
                //System.out.printf("i %d - nextY %d\n", i, nextY);
                //выбор из списка активных ребер x–координаты пересечений активных ребер со строкой сканирования
                for (Edge edge : activeEdges) {
                    edge.recalcX();
                    intersectionPoints.add(edge.getCurrentX());
                }

                if (intersectionPoints.size() % 2 != 0) {
                    System.out.println("parallel");
                    return;
                }

                //сортировка
                quickSort(intersectionPoints, 0, intersectionPoints.size() - 1);
                for (Float intersectionPoint : intersectionPoints) {
                    System.out.println(intersectionPoint);
                }


                for (int j = 0; j < intersectionPoints.size(); j += 2) {
                    painter.drawBrzLine(intersectionPoints.get(j), i, intersectionPoints.get(j + 1), i, bitmap, renderingType);
                    /*for (float k = intersectionPoints.get(j); k < intersectionPoints.get(j + 1); i++) {
                        switch (renderingType) {
                            case SOLID:
                                bitmap.setPixel((int) k, i, super.getColor());
                                break;
                            case ERASE:
                                bitmap.setPixel((int) k, i, 0);
                                break;
                        }
                    }*/
                }
                intersectionPoints.clear();
            }
            break;
        }
    }

    private void quickSort(ArrayList arrayList, int left, int right) {
        int i = left, j = right;

        if (arrayList.get(0).getClass().getSimpleName().compareTo("Edge") == 0) {
            int pivot = ((Edge) arrayList.get((left + right) / 2)).getStartPoint().y;

            while (i <= j) {
                while (((Edge) arrayList.get(i)).getStartPoint().y < pivot)
                    i++;
                while (((Edge) arrayList.get(j)).getStartPoint().y > pivot)
                    j--;
                if (i <= j) {
                    Collections.swap(arrayList, i, j);
                    i++;
                    j--;
                }
            }
        } else if (arrayList.get(0).getClass().getSimpleName().compareTo("Float") == 0) {
            float pivot = ((Float) arrayList.get((left + right) / 2));

            while (i <= j) {
                while (((Float) arrayList.get(i)) < pivot)
                    i++;
                while (((Float) arrayList.get(j)) > pivot)
                    j--;
                if (i <= j) {
                    Collections.swap(arrayList, i, j);
                    i++;
                    j--;
                }
            }
        } else {
            System.out.println("ne to " + arrayList.get(0).getClass().getSimpleName());
            return;
        }

        if (left < j)
            quickSort(arrayList, left, j);
        if (i < right)
            quickSort(arrayList, i, right);
    }


    @Override
    public void onTouch(MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (vertices.size() < 2) {
                    vertices.add(new Point(x, y));
                } else {
                    check(x, y);
                    if (!editingExistingVertex) {
                        drawPolygon(vertices, super.getFakeBitmap(), RenderingType.ERASE);
                        vertices.add(new Point(x, y));
                        drawPolygon(vertices, super.getFakeBitmap(), RenderingType.SOLID);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (vertices.size() > 2){
                    if (editingExistingVertex) {
                        drawPolygon(vertices, super.getFakeBitmap(), RenderingType.ERASE);
                        //fillPolygon(super.getFakeBitmap(), RenderingType.ERASE);
                        editVertex.x = x;
                        editVertex.y = y;
                        drawPolygon(vertices, super.getFakeBitmap(), RenderingType.SOLID);
                        //fillPolygon(super.getFakeBitmap(), RenderingType.SOLID);
                    } else {
                        drawPolygon(vertices, super.getFakeBitmap(), RenderingType.ERASE);
                        //fillPolygon(super.getFakeBitmap(), RenderingType.ERASE);
                        vertices.set(vertices.size() - 1, new Point(x, y));
                        drawPolygon(vertices, super.getFakeBitmap(), RenderingType.SOLID);
                        //fillPolygon(super.getFakeBitmap(), RenderingType.SOLID);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (vertices.size() > 2) {
                    if (editingExistingVertex) {
                        editingExistingVertex = false;
                        if (MainActivity.appSettings.isFillEnabled()) {
                            fillPolygon(super.getFakeBitmap(), RenderingType.SOLID);
                        }
                    } else {
                        drawPolygon(vertices, super.getFakeBitmap(), RenderingType.ERASE);
                        //fillPolygon(super.getFakeBitmap(), RenderingType.ERASE);
                        vertices.set(vertices.size() - 1, new Point(x, y));
                        drawPolygon(vertices, super.getFakeBitmap(), RenderingType.SOLID);
                        if (MainActivity.appSettings.isFillEnabled()) {
                            fillPolygon(super.getFakeBitmap(), RenderingType.SOLID);
                        }
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
        drawPolygon(vertices, super.getMainBitmap(), RenderingType.SOLID);
        if (MainActivity.appSettings.isFillEnabled()) {
            fillPolygon(super.getMainBitmap(), RenderingType.SOLID);
        }
    }

    private class Edge {
        private Point startPoint;
        private Point endPoint;
        private float currentX;
        private float delX;

        Edge(Point firstPoint, Point secondPoint) {
            if (firstPoint.y < secondPoint.y) {
                startPoint = firstPoint;
                endPoint = secondPoint;
            } else {
                startPoint = secondPoint;
                endPoint = firstPoint;
            }
            currentX = startPoint.x;
            delX = (float) (endPoint.x - startPoint.x) / (endPoint.y - startPoint.y);
        }

        public Point getStartPoint() {
            return startPoint;
        }

        public Point getEndPoint() {
            return endPoint;
        }

        public float getCurrentX() {
            return currentX;
        }

        public void recalcX() {
            currentX += delX;
        }

    }
}
