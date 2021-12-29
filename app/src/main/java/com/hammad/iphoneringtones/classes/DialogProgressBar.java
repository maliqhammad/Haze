package com.hammad.iphoneringtones.classes;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ProgressBar;

import com.hammad.iphoneringtones.R;

import java.util.Objects;


public class DialogProgressBar {
    Context context;
    public static Dialog dialog = null;
    ProgressBar progressBar;

    public DialogProgressBar(Context context) {
        this.context = context;
    }

    public void showSpinnerDialog() {
        try {
            if (dialog == null) {
                dialog = new Dialog(context, R.style.NewDialog);
                try {
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                    Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                try {
                    dialog.setContentView(R.layout.progress_bar_layout);
                    progressBar = dialog.findViewById(R.id.progress_bar_generic_layout);
                    dialog.setCancelable(false);
                    dialog.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelSpinnerDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isShowing() {
        try {
            return dialog != null && dialog.isShowing();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
