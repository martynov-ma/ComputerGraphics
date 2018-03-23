package cg.computergraphics.dialog;

import android.content.DialogInterface;

import cg.computergraphics.MainActivity;

/**
 * Created by mmartynau on 3/23/2018.
 */

public class MosaicDialog extends Dialog {
    public MosaicDialog(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    void build() {
        builder.setTitle("Draw mosaic?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mainActivity.getMyView().drawMosaic();
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
}
