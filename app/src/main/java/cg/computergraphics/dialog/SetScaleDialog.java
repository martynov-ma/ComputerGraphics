package cg.computergraphics.dialog;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import cg.computergraphics.AppSettings;
import cg.computergraphics.MainActivity;
import cg.computergraphics.R;

/**
 * Created by mmartynau on 3/23/2018.
 */

public class SetScaleDialog extends Dialog {

    //set scale dialog content
    private TextView scaleTextView;
    private SeekBar scaleSeekBar;

    public SetScaleDialog(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    void build() {
        View setScaleView = mainActivity.getLayoutInflater().inflate(R.layout.ad_setscale_content, null);
        prepareView(setScaleView);

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

    private void initSetScaleDialog(AlertDialog dialog) {
        scaleTextView = (TextView) dialog.findViewById(R.id.textScale);
        scaleTextView.setText(String.valueOf(AppSettings.getInstance().getBitmapScale()));

        scaleSeekBar = (SeekBar) dialog.findViewById(R.id.seekBar);
        scaleSeekBar.setProgress(AppSettings.getInstance().getBitmapScale() - 1);
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
}
