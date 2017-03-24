package cg.computergraphics.files;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import cg.computergraphics.MainActivity;

/**
 * Created by MAX on 17.03.2017.
 */

public class BmpFileReader {

    private MainActivity mainActivity;

    private FileInputStream fileInputStream;
    private Bitmap bitmap;

    public BmpFileReader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public boolean readFile(String fileName) {

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
        if (sdFile.exists()) System.out.println("файл получен");

        try {
            fileInputStream = new FileInputStream(sdFile);

            //Заголовок файла
            if (readBytes(2) != 0x4D42) {   //Код 4D42h - Буквы 'BM'
                Toast.makeText(mainActivity, "Неверный формат файла.", Toast.LENGTH_LONG).show();
                return false;
            }

            int size = readBytes(4);    //Размер файла в байтах
            if (size == -1) {
                Toast.makeText(mainActivity, "Не удалось получить размер файла.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (readBytes(2) == -1) return false;   //0 (Резервное поле)
            if (readBytes(2) == -1) return false;   //0 (Резервное поле)

            if (readBytes(4) == -1) return false;  //Смещение, с которого начинается само изображение (растр).

            //Заголовок BITMAP (Информация об изображении)
            if (readBytes(4) == -1) return false;   //Размер заголовка BITMAP (в байтах) равно 40

            int width = readBytes(4);   //Ширина изображения в пикселях
            if (width == -1) return false;

            int height = readBytes(4);  //Высота изображения в пикселях
            if (height == -1) return false;

            int alignment = width % 4;  //Смещение
            int length = width * height * 3 + height * alignment;

            if (size != length + 54) {
                Toast.makeText(mainActivity, "Ошибка в заголовке файла. Не совпадает размер.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (readBytes(2) != 1) return false;   //Число плоскостей, должно быть 1
            if (readBytes(2) != 24) {   //Бит на пиксел: 1, 4, 8 или 24
                Toast.makeText(mainActivity, "Поддерживается файл BMP только 24bit.", Toast.LENGTH_LONG).show();
                return false;
            }
            if (readBytes(4) != 0) {    //Тип сжатия
                Toast.makeText(mainActivity, "Поддерживается файл BMP только без сжатия.", Toast.LENGTH_LONG).show();
                return false;
            }
            System.out.println("растровый массив " + readBytes(4));
            /*if (readBytes(4) != 0) {    //0 или размер сжатого изображения в байтах.
                Toast.makeText(mainActivity, "Ошибка в заголовке файла.Размер Растрового массива.", Toast.LENGTH_LONG).show();
                return false;
            }*/
            if (readBytes(4) == -1) return false;   //Горизонтальное разрешение, пиксел/м
            if (readBytes(4) == -1) return false;   //Вертикальное разрешение, пиксел/м
            if (readBytes(4) == -1) return false;   //Количество используемых цветов
            if (readBytes(4) == -1) return false;   //Количество "важных" цветов.

            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int i = height - 1; i >= 0; i--) {
                for (int j = 0; j < width; j++) {
                    int blue = readBytes(1);
                    int green = readBytes(1);
                    int red = readBytes(1);
                    bitmap.setPixel(j, i, Color.rgb(red, green, blue));
                }
                readBytes(alignment);
            }

            fileInputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    private int readBytes(int n) throws IOException {
        int offset = 0;
        int value = 0;

        for (int i = 0; i < n; i++) {
            int temp = fileInputStream.read();
            if (temp == -1) {
                return -1;
            }
            temp <<= offset;
            value |= temp;
            offset += 8;
        }
        return value;
    }

    public boolean drawBmp() {
        if (bitmap != null) {
            mainActivity.getMyView().setBitmap(bitmap);
            return  true;
        } else return false;
    }
}
