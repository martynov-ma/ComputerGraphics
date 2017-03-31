package cg.computergraphics;

import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by MAX on 19.03.2017.
 */

class DialogWindowManager {

    static final int IDD_SET_SCALE = 1;
    static final int IDD_MOSAIC = 2;
    static final int IDD_SETTINGS = 3;
    static final int IDD_OPEN_FILE = 4;
    static final int IDD_SET_FILE_NAME = 5;

    private MainActivity mainActivity;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    //set scale dialog content
    private TextView scaleTextView;
    private SeekBar scaleSeekBar;

    //settings dialog content
    private TextView bitmapWidthEdit;
    private TextView bitmapHeightEdit;
    private Spinner lineSpinner;
    private Spinner circleSpinner;
    private TextView mosaicSize;

    //open file dialog content
    private String[] mFileList;
    private String mChosenFile;
    private ObjFileManager objFileManager;
    private BmpFileReader bmpFileReader;

    //set file name dialog content
    private TextView fileNameTextView;
    private BmpFileWriter bmpFileWriter;


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
        }
    }


    private void showSetScaleDialog() {
        View setScaleView = mainActivity.getLayoutInflater().inflate(R.layout.ad_setscale_content, null);
        prepareView(setScaleView);

        builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("Set scale")
                .setView(setScaleView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mainActivity.getMyView().setBitmapScale(scaleSeekBar.getProgress() + 1);
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
        initSetScaleDialog(dialog);
    }

    private void showMosaicDialog() {
        builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("Draw mosaic?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mainActivity.getMyView().setBitmap(mainActivity.getMyView().getMosaic());
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        dialog = builder.create();
        dialog.show();
    }

    private void showSettingsDialog() {
        View setSettingsView = mainActivity.getLayoutInflater().inflate(R.layout.ad_settings_content, null);
        prepareView(setSettingsView);

        builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("Settings")
                .setView(setSettingsView)
                .setPositiveButton("APPLY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        applySettings();
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
        initSettingsDialog(dialog);
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
                            //long startTime = System.currentTimeMillis();
                            objFileManager.drawObj();
                            //long timeSpent = System.currentTimeMillis() - startTime;
                            //Toast.makeText(mainActivity, "Obj successfully drawn.\ntime: " + (double) timeSpent / 1000 + " sec.", Toast.LENGTH_LONG).show();
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


    private void prepareView(View view) {
        if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);
    }


    private void initSetScaleDialog(AlertDialog dialog) {
        scaleTextView = (TextView) dialog.findViewById(R.id.textScale);
        scaleTextView.setText(String.valueOf(mainActivity.getMyView().getBitmapScale()));

        scaleSeekBar = (SeekBar) dialog.findViewById(R.id.seekBar);
        scaleSeekBar.setProgress(mainActivity.getMyView().getBitmapScale() - 1);
        scaleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                scaleTextView.setText(String.valueOf(i + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initSettingsDialog(AlertDialog dialog) {
        bitmapWidthEdit = (TextView) dialog.findViewById(R.id.bitmapWidthEdit);
        bitmapWidthEdit.setText(String.valueOf(MainActivity.settings.getBitmapWidth()));

        bitmapHeightEdit = (TextView) dialog.findViewById(R.id.bitmapHeightEdit);
        bitmapHeightEdit.setText(String.valueOf(MainActivity.settings.getBitmapHeight()));

        lineSpinner = (Spinner) dialog.findViewById(R.id.lineSpinner);
        lineSpinner.setSelection(MainActivity.settings.getLineDrawingAlgorithm());
        circleSpinner = (Spinner) dialog.findViewById(R.id.circleSpinner);
        circleSpinner.setSelection(MainActivity.settings.getCircleDrawingAlgorithm());

        mosaicSize = (TextView) dialog.findViewById(R.id.mosaicSizeEdit);
        mosaicSize.setText(String.valueOf(MainActivity.settings.getMosaicSize()));
    }

    private void applySettings() {
        Settings settings = MainActivity.settings;
        int newBitmapWidth = Integer.parseInt(bitmapWidthEdit.getText().toString());
        int newBitmapHeight = Integer.parseInt(bitmapHeightEdit.getText().toString());

        if (settings.getBitmapWidth() != newBitmapWidth || MainActivity.settings.getBitmapHeight() != newBitmapHeight) {
            settings.setBitmapWidth(newBitmapWidth);
            settings.setBitmapHeight(newBitmapHeight);
            mainActivity.getMyView().updateBitmap();
        }

        settings.setLineDrawingAlgorithm(lineSpinner.getSelectedItemPosition());
        settings.setCircleDrawingAlgorithm(circleSpinner.getSelectedItemPosition());
        mainActivity.getMyView().setDrawingTool(mainActivity.getMyView().getSelectedTool());

        settings.setMosaicSize(Integer.parseInt(mosaicSize.getText().toString()));
    }

    private void initSetFileNameDialog(AlertDialog dialog) {
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("HH_mm_ss");
        String dateNowStr = formatForDateNow.format(dateNow) + ".bmp";

        fileNameTextView = (TextView) dialog.findViewById(R.id.fileNameEdit);
        fileNameTextView.setText(dateNowStr);
    }
}
