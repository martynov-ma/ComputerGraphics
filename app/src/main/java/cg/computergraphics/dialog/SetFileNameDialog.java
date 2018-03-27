package cg.computergraphics.dialog;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cg.computergraphics.MainActivity;
import cg.computergraphics.R;
import cg.computergraphics.files.BmpFileWriter;

public class SetFileNameDialog extends Dialog {

    //set file name dialog content
    private TextView fileNameTextView;
    private BmpFileWriter bmpFileWriter;

    public SetFileNameDialog(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    void build() {
        View setFileNameView = mainActivity.getLayoutInflater().inflate(R.layout.ad_setfilename_content, null);
        prepareView(setFileNameView);

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

    private void initSetFileNameDialog(AlertDialog dialog) {
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("HH:mm:ss");
        String dateNowStr = formatForDateNow.format(dateNow) + ".bmp";

        fileNameTextView = (TextView) dialog.findViewById(R.id.fileNameEdit);
        fileNameTextView.setText(dateNowStr);
    }
}
