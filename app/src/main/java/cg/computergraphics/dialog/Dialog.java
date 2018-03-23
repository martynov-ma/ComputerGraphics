package cg.computergraphics.dialog;

import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;

import cg.computergraphics.MainActivity;

/**
 * Created by mmartynau on 3/23/2018.
 */

public abstract class Dialog {

    protected AlertDialog dialog;
    protected AlertDialog.Builder builder;

    protected MainActivity mainActivity;

    public Dialog(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        builder = new AlertDialog.Builder(mainActivity);
        build();
    }

    abstract void build();

    protected void prepareView(View view) {
        if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);
    }
}
