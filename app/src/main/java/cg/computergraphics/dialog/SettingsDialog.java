package cg.computergraphics.dialog;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import cg.computergraphics.AppSettings;
import cg.computergraphics.MainActivity;
import cg.computergraphics.R;

/**
 * Created by mmartynau on 3/23/2018.
 */

public class SettingsDialog extends Dialog {

    //appSettings dialog content
    private TextView bitmapWidthEdit;
    private TextView bitmapHeightEdit;
    private Spinner lineSpinner;
    private Spinner circleSpinner;
    private TextView mosaicSize;

    public SettingsDialog(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    void build() {
        View setSettingsView = mainActivity.getLayoutInflater().inflate(R.layout.ad_settings_content, null);
        prepareView(setSettingsView);

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

    private void initSettingsDialog(AlertDialog dialog) {
        bitmapWidthEdit = (TextView) dialog.findViewById(R.id.bitmapWidthEdit);
        bitmapWidthEdit.setText(String.valueOf(AppSettings.getInstance().getBitmapWidth()));

        bitmapHeightEdit = (TextView) dialog.findViewById(R.id.bitmapHeightEdit);
        bitmapHeightEdit.setText(String.valueOf(AppSettings.getInstance().getBitmapHeight()));

        lineSpinner = (Spinner) dialog.findViewById(R.id.lineSpinner);
        lineSpinner.setSelection(AppSettings.getInstance().getLineDrawingAlgorithm());
        circleSpinner = (Spinner) dialog.findViewById(R.id.circleSpinner);
        circleSpinner.setSelection(AppSettings.getInstance().getCircleDrawingAlgorithm());

        mosaicSize = (TextView) dialog.findViewById(R.id.mosaicSizeEdit);
        mosaicSize.setText(String.valueOf(AppSettings.getInstance().getMosaicSize()));
    }

    private void applySettings() {
        AppSettings appSettings = AppSettings.getInstance();
        int newBitmapWidth = Integer.parseInt(bitmapWidthEdit.getText().toString());
        int newBitmapHeight = Integer.parseInt(bitmapHeightEdit.getText().toString());

        if (appSettings.getBitmapWidth() != newBitmapWidth || AppSettings.getInstance().getBitmapHeight() != newBitmapHeight) {
            appSettings.setBitmapWidth(newBitmapWidth);
            appSettings.setBitmapHeight(newBitmapHeight);
            mainActivity.getMyView().updateBitmap();
        }

        appSettings.setLineDrawingAlgorithm(lineSpinner.getSelectedItemPosition());
        appSettings.setCircleDrawingAlgorithm(circleSpinner.getSelectedItemPosition());
        mainActivity.getMyView().setDrawingTool(mainActivity.getMyView().getSelectedTool());

        appSettings.setMosaicSize(Integer.parseInt(mosaicSize.getText().toString()));
    }
}
