package cg.computergraphics.dialog;

import android.support.annotation.ColorInt;

import cg.computergraphics.AppSettings;
import cg.computergraphics.MainActivity;
import me.priyesh.chroma.ChromaDialog;
import me.priyesh.chroma.ColorMode;
import me.priyesh.chroma.ColorSelectListener;

public class ColorPickerDialog extends Dialog {
    public ColorPickerDialog(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    void build() {
        new ChromaDialog.Builder()
                .initialColor(mainActivity.getMyView().getDrawingTool().getColor())
                .colorMode(ColorMode.RGB)
                .onColorSelected(new ColorSelectListener() {
                    @Override
                    public void onColorSelected(@ColorInt int color) {
                        mainActivity.getMyView().getDrawingTool().setColor(color);
                        AppSettings.getInstance().setDrawingColor(color);
                    }
                })
                .create()
                .show(mainActivity.getSupportFragmentManager(), "ChromaDialog");
    }
}
