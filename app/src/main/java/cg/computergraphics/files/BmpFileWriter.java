package cg.computergraphics.files;

import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cg.computergraphics.MainActivity;

/**
 * Created by MAX on 21.03.2017.
 */

public class BmpFileWriter {

    private MainActivity mainActivity;

    private byte[] bytes;
    private int offset;

    public BmpFileWriter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public boolean writeFile(Bitmap bitmap, String fileName) throws IOException {
        File newFile = new File(Environment.getExternalStorageDirectory() + "/" + "MyFiles/" + fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(newFile);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int alignment = (width % 4);
        int length = width * height * 3 + height * alignment;
        int size = length + 54;

        bytes = new byte[size];
        offset = 0;

        //Заголовок файла
        writeBytes(0x4D42, 2);  //Код 4D42h - Буквы 'BM'
        writeBytes(size, 4);    //Размер файла в байтах
        writeBytes(0, 2);   //0 (Резервное поле)
        writeBytes(0, 2);   //0 (Резервное поле)
        writeBytes(54, 4);  //Смещение, с которого начинается само изображение (растр).

        //Заголовок BITMAP (Информация об изображении)
        writeBytes(40, 4);  //Размер заголовка BITMAP (в байтах) равно 40
        writeBytes(width, 4);   //Ширина изображения в пикселях
        writeBytes(height, 4);  //Высота изображения в пикселях
        writeBytes(1, 2);   //Число плоскостей, должно быть 1
        writeBytes(24, 2);  //Бит на пиксел: 1, 4, 8 или 24
        writeBytes(0, 4);   //Тип сжатия
        writeBytes(0, 4);   //0 или размер сжатого изображения в байтах.
        writeBytes(0, 4);   //Горизонтальное разрешение, пиксел/м
        writeBytes(0, 4);   //Вертикальное разрешение, пиксел/м
        writeBytes(0, 4);   //Количество используемых цветов
        writeBytes(0, 4);   //Количество "важных" цветов.

        for (int i = height - 1; i >= 0; i--) {
            for (int j = 0; j < width; j++) {
                int color = bitmap.getPixel(j, i);
                if (color == 0) {
                    writeBytes(0xFFFFFF, 3);
                } else {
                    writeBytes(color & 0xFF, 1);
                    writeBytes((color >> 8) & 0xFF, 1);
                    writeBytes((color >> 16) & 0xFF, 1);
                }
            }
            writeBytes(0, alignment);
        }
        fileOutputStream.write(bytes);
        fileOutputStream.close();
        Toast.makeText(mainActivity, "write success", Toast.LENGTH_LONG).show();
        return true;
    }

    private void writeBytes(int value, int n) {
        for (int i = 0; i < n; i++) {
            bytes[offset++] = (byte)(value & 0xFF);
            value >>= 8;
        }
    }
}