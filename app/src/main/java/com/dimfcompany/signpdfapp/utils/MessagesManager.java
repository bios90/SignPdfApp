package com.dimfcompany.signpdfapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.ColorRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dimfcompany.signpdfapp.R;
import com.tapadoo.alerter.Alerter;

public class MessagesManager
{
    private final AppCompatActivity activity;
    private final LayoutInflater layoutInflater;

    public interface DialogButtonsListener
    {
        void onOkClicked(DialogInterface dialog);

        void onCancelClicked(DialogInterface dialog);
    }

    public MessagesManager(AppCompatActivity activity)
    {
        this.activity = activity;
        this.layoutInflater = LayoutInflater.from(activity);
    }


    public void showRedAlerter(String title, String text)
    {
        showAlerter(title, text, R.color.redBase);
    }

    public void showGreenAlerter(String title, String text)
    {
        showAlerter(title, text, R.color.green);
    }

    public void showAlerter(String title, String text, @ColorRes int color)
    {
        Alerter.create(activity)
                .setTitle(title)
                .setText(text)
                .setDuration(4000)
                .setBackgroundColorRes(color)
                .setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Alerter.hide();
                    }
                })
                .show();
    }

    public void showSimpleDialog(String title, String text, String btnOkText, String btnCancelText, final DialogButtonsListener listener)
    {
        View dialogView = layoutInflater.inflate(R.layout.dialog, null);

        TextView tvTitle = dialogView.findViewById(R.id.tv_title);
        TextView tvText = dialogView.findViewById(R.id.tv_text);
        Button btnOk = dialogView.findViewById(R.id.btn_ok);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);

        tvTitle.setText(title);
        tvText.setText(text);

        btnOk.setText(btnOkText);
        btnCancel.setText(btnCancelText);

        final AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.setView(dialogView);

        btnOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onOkClicked(dialog);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onCancelClicked(dialog);
            }
        });

        dialog.show();
    }

    public void vibrate()
    {
        vibrate(200);
    }

    public void vibrate(long miliseconds)
    {
        Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            v.vibrate(VibrationEffect.createOneShot(miliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        }
        else
        {

            v.vibrate(miliseconds);
        }
    }
}
