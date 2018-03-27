package cg.computergraphics.dialog;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import cg.computergraphics.AppSettings;
import cg.computergraphics.MainActivity;
import cg.computergraphics.R;
import cg.computergraphics.files.ObjFileManager;

public class ObjSettingsDialog extends Dialog {

    //obj settings content
    private CheckBox objIsFilling;
    private CheckBox objIsRandomColor;

    private ObjFileManager objFileManager;

    public ObjSettingsDialog(MainActivity mainActivity, ObjFileManager objFileManager) {
        super(mainActivity);
        this.objFileManager = objFileManager;
    }

    @Override
    void build() {
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
