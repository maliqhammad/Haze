package com.hammad.haze.classes;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.hammad.haze.R;

public class ShowSingletonAlertDialog {
    AlertDialog.Builder builder;
    Context context;

    private ShowSingletonAlertDialog(Context context) {
        this.context = context;
    }

    public static ShowSingletonAlertDialog newInstance(Context context) {
        return new ShowSingletonAlertDialog(context);
    }

    public void showDialog() {
        try {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                builder = new AlertDialog.Builder(context);
                builder.setIcon(R.mipmap.ic_app_launcher_square);
                builder.setTitle(context.getResources().getString(R.string.internet_connection));
                builder.setMessage(context.getResources().getString(R.string.please_check_your_network_connection_and_try_again));
                builder.setPositiveButton(context.getResources().getString(R.string.ok), null);
                try {
                    builder.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
