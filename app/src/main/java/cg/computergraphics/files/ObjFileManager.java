package cg.computergraphics.files;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import cg.computergraphics.*;
import cg.computergraphics.tools.brz.BrzLine;
import cg.computergraphics.tools.Polygon;
import cg.computergraphics.tools.RenderingType;
import cg.computergraphics.tools.dda.DDALine;

/**
 * Created by MAX on 08.03.2017.
 */

public class ObjFileManager {

    private Bitmap mainBitmap;
    private Bitmap fakeBitmap;
    private ArrayList<Vertex> vertices;
    private ArrayList<String[]> faces;
    private ArrayList<Point> triangleVertices;

    public ObjFileManager(MainActivity mainActivity) {
        mainBitmap = mainActivity.getMyView().getMainBitmap();
        fakeBitmap = mainActivity.getMyView().getFakeBitmap();

        vertices = new ArrayList<>();
        faces = new ArrayList<>();
        triangleVertices = new ArrayList<>();
    }

    public boolean readFile(String fileName) {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            System.out.println("SD-карта не доступна: " + Environment.getExternalStorageState());
            return false;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + "MyFiles");
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, fileName);
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            String line;
            // читаем содержимое
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                if (line.length() != 0) {
                    if (line.charAt(0) == 'v' && line.charAt(1) == ' ') {
                        String[] coordinates = line.split(" ");
                        Float x = (Float.parseFloat(coordinates[1]) + 1) * 540;
                        Float y = (-Float.parseFloat(coordinates[2]) + 1) * 540;
                        vertices.add(new Vertex(x, y));
                    }
                    if (line.charAt(0) == 'f' && line.charAt(1) == ' ') {
                        String[] temp = line.split(" ");
                        String[] faces = new String[3];
                        for (int i = 0; i < 3; i++) {
                            faces[i] = temp[i + 1].split("/")[0];
                        }
                        this.faces.add(faces);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void drawObjDDA() {
        if (MainActivity.appSettings.isObjFilling()) {
            Polygon polygon = new Polygon(mainBitmap, fakeBitmap);
            polygon.setColor(MainActivity.appSettings.getDrawingColor());
            for (int i = 0; i < faces.size() - 1; i++) {
                for (int j = 0; j < 3; j++) {
                    triangleVertices.add(new Point((int) vertices.get(Integer.parseInt(faces.get(i)[j]) - 1).getX(),
                                                   (int) vertices.get(Integer.parseInt(faces.get(i)[j]) - 1).getY()));
                }
                if (MainActivity.appSettings.isObjRandomColor()) {
                    polygon.setColor(Color.rgb((int) (Math.random() * 1000) % 255, (int) (Math.random() * 1000) % 255, (int) (Math.random() * 1000) % 255));
                }
                polygon.fillPolygon(triangleVertices, mainBitmap);
                triangleVertices.clear();
            }
        }
        DDALine painter = new DDALine(mainBitmap, fakeBitmap);
        painter.setColor(Color.BLACK);
        for (int i = 0; i < faces.size() - 1; i++) {
            for (int j = 0; j < 3; j++) {
                painter.drawDDALine(vertices.get(Integer.parseInt(faces.get(i)[j]) - 1).getX(),
                                    vertices.get(Integer.parseInt(faces.get(i)[j]) - 1).getY(),
                                    vertices.get(Integer.parseInt(faces.get(i)[(j + 1) % 3]) - 1).getX(),
                                    vertices.get(Integer.parseInt(faces.get(i)[(j + 1) % 3]) - 1).getY(),
                                    mainBitmap, RenderingType.SOLID);
            }
        }
    }

    public void drawObjBrz() {
        if (MainActivity.appSettings.isObjFilling()) {
            Polygon polygon = new Polygon(mainBitmap, fakeBitmap);
            polygon.setColor(MainActivity.appSettings.getDrawingColor());
            for (int i = 0; i < faces.size() - 1; i++) {
                for (int j = 0; j < 3; j++) {
                    triangleVertices.add(new Point((int) vertices.get(Integer.parseInt(faces.get(i)[j]) - 1).getX(),
                            (int) vertices.get(Integer.parseInt(faces.get(i)[j]) - 1).getY()));
                }
                if (MainActivity.appSettings.isObjRandomColor()) {
                    polygon.setColor(Color.rgb((int) (Math.random() * 1000) % 255, (int) (Math.random() * 1000) % 255, (int) (Math.random() * 1000) % 255));
                }
                polygon.fillPolygon(triangleVertices, mainBitmap);
                triangleVertices.clear();
            }
        }
        BrzLine painter = new BrzLine(mainBitmap, fakeBitmap);
        painter.setColor(Color.BLACK);
        for (int i = 0; i < faces.size() - 1; i++) {
            for (int j = 0; j < 3; j++) {
                painter.drawBrzLine(vertices.get(Integer.parseInt(faces.get(i)[j]) - 1).getX(),
                                    vertices.get(Integer.parseInt(faces.get(i)[j]) - 1).getY(),
                                    vertices.get(Integer.parseInt(faces.get(i)[(j + 1) % 3]) - 1).getX(),
                                    vertices.get(Integer.parseInt(faces.get(i)[(j + 1) % 3]) - 1).getY(),
                                    mainBitmap, RenderingType.SOLID);
            }
        }
    }
}
