package cg.computergraphics;

import android.content.DialogInterface;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import cg.computergraphics.files.BmpFileReader;
import cg.computergraphics.files.BmpFileWriter;
import cg.computergraphics.files.ObjFileManager;
import me.priyesh.chroma.ChromaDialog;
import me.priyesh.chroma.ColorMode;
import me.priyesh.chroma.ColorSelectListener;

/**
 * Created by MAX on 19.03.2017.
 */

public class DialogWindowManager {

    static final int IDD_SET_SCALE = 1;
    static final int IDD_MOSAIC = 2;
    static final int IDD_SETTINGS = 3;
    static final int IDD_OPEN_FILE = 4;
    static final int IDD_SET_FILE_NAME = 5;
    static final int IDD_COLOR_PICKER = 6;

    private MainActivity mainActivity;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;



    //open file dialog content
    private String[] mFileList;
    private String mChosenFile;
    private ObjFileManager objFileManager;
    private BmpFileReader bmpFileReader;

    //set file name dialog content
    private TextView fileNameTextView;
    private BmpFileWriter bmpFileWriter;

    //obj settings content
    private CheckBox objIsFilling;
    private CheckBox objIsRandomColor;


    DialogWindowManager(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    void showDialog(int id) {
        switch (id) {
            case IDD_SET_SCALE:
                showSetScaleDialog();
                break;
            case IDD_MOSAIC:
                showMosaicDialog();
                break;
            case IDD_SETTINGS:
                showSettingsDialog();
                break;
            case IDD_OPEN_FILE:
                showOpenFileDialog();
                break;
            case IDD_SET_FILE_NAME:
                showSetFileNameDialog();
                break;
            case IDD_COLOR_PICKER:
                showColorPickerDialog();
                break;
        }
    }








    private void showOpenFileDialog() {
        File sdPath = new File(Environment.getExternalStorageDirectory() + "/" + "MyFiles");
        mFileList = sdPath.list();

        builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("Read file:")
                .setItems(mFileList, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mChosenFile = mFileList[which];
                        int dot = mChosenFile.lastIndexOf(".");
                        if (Objects.equals(mChosenFile.substring(dot + 1).toLowerCase(), "obj")) {
                            objFileManager = new ObjFileManager(mainActivity);
                            objFileManager.readFile(mChosenFile);
                            showObjSettingsDialog();
                        } else if (Objects.equals(mChosenFile.substring(dot + 1).toLowerCase(), "bmp")) {
                            bmpFileReader = new BmpFileReader(mainActivity);
                            bmpFileReader.readFile(mChosenFile);
                            bmpFileReader.drawBmp();
                        } else Toast.makeText(mainActivity, "Invalid file.\nSupported files: .obj .bmp", Toast.LENGTH_LONG).show();
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        dialog = builder.create();
        dialog.show();
    }

    private void showSetFileNameDialog() {
        View setFileNameView = mainActivity.getLayoutInflater().inflate(R.layout.ad_setfilename_content, null);
        prepareView(setFileNameView);

        builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("Set file name:")
                .setView(setFileNameView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            bmpFileWriter = new BmpFileWriter(mainActivity);
                            bmpFileWriter.writeFile(mainActivity.getMyView().getMainBitmap(), fileNameTextView.getText().toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        dialog = builder.create();
        dialog.show();
        initSetFileNameDialog(dialog);
    }

    private void showColorPickerDialog() {
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

    private void showObjSettingsDialog() {
        View setObjSettingsView = mainActivity.getLayoutInflater().inflate(R.layout.ad_obj_settings_content, null);
        prepareView(setObjSettingsView);

        builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("Obj render settings")
                .setView(setObjSettingsView)
                .setPositiveButton("DRAW", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        applyObjSettings();
                        dialog.cancel();
                        long startTime, timeSpent;
                        switch (AppSettings.getInstance().getLineDrawingAlgorithm()) {
                            case 0:
                                startTime = System.currentTimeMillis();
                                new Thread(new Runnable() {
                                    public void run() {
                                        objFileManager.drawObjDDA();
                                    }
                                }).start();
                                //objFileManager.drawObjDDA();
                                timeSpent = System.currentTimeMillis() - startTime;
                                Toast.makeText(mainActivity, "DDA\ntime: " + (double) timeSpent / 1000 + " sec.", Toast.LENGTH_LONG).show();
                                break;
                            case 1:
                                startTime = System.currentTimeMillis();
                                objFileManager.drawObjBrz();
                                timeSpent = System.currentTimeMillis() - startTime;
                                Toast.makeText(mainActivity, "BRZ\ntime: " + (double) timeSpent / 1000 + " sec.", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        dialog = builder.create();
        dialog.show();
        initObjSettingsDialog(dialog);
    }


    private void prepareView(View view) {
        if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);
    }






    private void initSetFileNameDialog(AlertDialog dialog) {
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("HH:mm:ss");
        String dateNowStr = formatForDateNow.format(dateNow) + ".bmp";

        fileNameTextView = (TextView) dialog.findViewById(R.id.fileNameEdit);
        fileNameTextView.setText(dateNowStr);
    }

    private void initObjSettingsDialog(AlertDialog dialog) {
        objIsFilling = (CheckBox) dialog.findViewById(R.id.objIsFilling);
        objIsFilling.setChecked(AppSettings.getInstance().isObjFilling());

        objIsRandomColor = (CheckBox) dialog.findViewById(R.id.objIsRandomFilling);
        objIsRandomColor.setChecked(AppSettings.getInstance().isObjRandomColor());
    }




    private void applyObjSettings() {
        AppSettings.getInstance().setObjFilling(objIsFilling.isChecked());
        AppSettings.getInstance().setObjRandomColor(objIsRandomColor.isChecked());
    }
}
