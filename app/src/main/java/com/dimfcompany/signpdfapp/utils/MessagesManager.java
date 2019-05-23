package com.dimfcompany.signpdfapp.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.tapadoo.alerter.Alerter;

public class MessagesManager
{
    private static final String TAG = "MessagesManager";

    private final AppCompatActivity activity;
    private final LayoutInflater layoutInflater;

    private AlertDialog progressDialog;

    public interface DialogButtonsListener
    {
        void onOkClicked(DialogInterface dialog);
        void onCancelClicked(DialogInterface dialog);
    }

    public interface DialogWithEtListener
    {
        void onOkDialogWithEt(Dialog dialog, String text);
        void onCancelDialogWithEt(Dialog dialog);
    }

    public interface DialogFinishedListener
    {
        void clickedOpenDogovor();
        void clickedOpenCheck();
        void clickedSendDogovor();
        void clickedSendCheck();
        void clickedPrintCheck();
        void clickedEdit();
        void clickedDelete();
    }

    public MessagesManager(AppCompatActivity activity)
    {
        this.activity = activity;
        this.layoutInflater = LayoutInflater.from(activity);
    }

    public void showNoInternetAlerter()
    {
        showRedAlerter("Ошибка", "Нет соединения с сетью");
    }

    public void showRedAlerter( String text)
    {
        showAlerter("Ошибка", text, R.color.redBase);
    }

    public void showRedAlerter(String title, String text)
    {
        showAlerter(title, text, R.color.redBase);
    }

    public void showGreenAlerter( String text)
    {
        showAlerter("", text, R.color.green);
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



    public void showFinishedDocument(final DialogFinishedListener listener)
    {
        View dialogView = layoutInflater.inflate(R.layout.dialog_document_card, null);
        RelativeLayout la_open_dogovor,la_open_check,la_send_dogovor,la_send_check,la_print_check,la_edit,la_delete;

        la_open_dogovor = dialogView.findViewById(R.id.la_open_dogovor);
        la_open_check = dialogView.findViewById(R.id.la_open_check);
        la_send_dogovor = dialogView.findViewById(R.id.la_send_dogovor);
        la_send_check = dialogView.findViewById(R.id.la_send_check);
        la_print_check = dialogView.findViewById(R.id.la_print_check);
        la_edit = dialogView.findViewById(R.id.la_edit);
        la_delete = dialogView.findViewById(R.id.la_delete);

        final AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.setView(dialogView);


        la_open_dogovor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.clickedOpenDogovor();
            }
        });

        la_open_check.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.clickedOpenCheck();
            }
        });
        
        la_send_dogovor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.clickedSendDogovor();
            }
        });

        la_send_check.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.clickedSendCheck();
            }
        });

        la_edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.clickedEdit();
            }
        });

        la_delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                listener.clickedDelete();
            }
        });

        la_print_check.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.clickedPrintCheck();
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

    public void showProgressDialog()
    {
        showProgressDialog(null);
    }

    public void showProgressDialog(@Nullable String message)
    {
        if (progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
            progressDialog = null;
        }

        View dialogView = LayoutInflater.from(activity).inflate(R.layout.progress_spinner_dialog, null);
        TextView textView = dialogView.findViewById(R.id.tv_header_title);

        if (message != null)
        {
            textView.setText(message);
        }

        progressDialog = new AlertDialog.Builder(activity).create();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setView(dialogView);

        try
        {
            progressDialog.show();
        } catch (Exception e)
        {
            Log.e(TAG, "showSpotsDialog: Error on show Green Dialog");
        }
    }

    public void dismissProgressDialog()
    {
        if (progressDialog != null)
        {
            if (progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }

            progressDialog = null;
        }
    }

    public void showEtDialog(String title, String text, String btnOkText, String btnCancelText, final DialogWithEtListener listener)
    {
        showEtDialog(title, text, btnOkText, btnCancelText, "", listener);
    }

    public void showEtDialog(String title, String text, String btnOkText, String btnCancelText, String currentText, final DialogWithEtListener listener)
    {
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_with_et, null);

        TextView tv_title = dialogView.findViewById(R.id.tv_title);
        TextView tv_text = dialogView.findViewById(R.id.tv_text);
        final EditText editText = dialogView.findViewById(R.id.et_et);
        Button btnOk = dialogView.findViewById(R.id.btn_ok);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        editText.setText(currentText);

        tv_title.setText(title);
        tv_text.setText(text);


        btnOk.setText(btnOkText);
        btnCancel.setText(btnCancelText);

        final AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.setView(dialogView);

        btnOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String text = editText.getText().toString().trim();
                listener.onOkDialogWithEt(dialog, text);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onCancelDialogWithEt(dialog);
            }
        });

        dialog.show();
    }

}
