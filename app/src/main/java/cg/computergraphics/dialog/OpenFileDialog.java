package cg.computergraphics.dialog;

import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.io.File;
import java.util.Objects;

import cg.computergraphics.MainActivity;
import cg.computergraphics.files.BmpFileReader;
import cg.computergraphics.files.ObjFileManager;

public class OpenFileDialog extends Dialog {

    //open file dialog content
    private String[] mFileList;
    private String mChosenFile;
    private ObjFileManager objFileManager;
    private BmpFileReader bmpFileReader;

    public OpenFileDialog(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    void build() {
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
                            new ObjSettingsDialog(mainActivity, objFileManager);
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
}
