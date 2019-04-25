package com.dimfcompany.signpdfapp.ui.act_signature_dialog;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableViewAbstr;
import com.dimfcompany.signpdfapp.utils.FileManager;
import com.dimfcompany.signpdfapp.utils.ImageManager;
import com.kyanogen.signatureview.SignatureView;

import java.io.File;

public class ActSignatureDialogMvpView extends BaseObservableViewAbstr<ActSignatureDialogMvp.ViewListener>
        implements ActSignatureDialogMvp.MvpView
{
    private static final String TAG = "ActSignatureDialogMvpVi";

    private final FileManager fileManager;
    SignatureView signature_view;
    TextView tv_clear;
    Button btn_ok, btn_cancel;

    public ActSignatureDialogMvpView(LayoutInflater layoutInflater, ViewGroup parent, FileManager fileManager)
    {
        this.fileManager = fileManager;
        setRootView(layoutInflater.inflate(R.layout.act_signature_dialog, parent, false));
        initViews();
        setListeners();
    }

    private void initViews()
    {
        signature_view = findViewById(R.id.signature_view);
        tv_clear = findViewById(R.id.tv_clear);
        btn_ok = findViewById(R.id.btn_ok);
        btn_cancel = findViewById(R.id.btn_cancel);
    }

    private void setListeners()
    {
        btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedOk();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedCancel();
            }
        });

        tv_clear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                clearSign();
            }
        });
    }

    @Override
    public Bitmap getSignatureBitmap()
    {
        if (signature_view.isBitmapEmpty())
        {
            return null;
        }

        return signature_view.getSignatureBitmap();
    }

    @Override
    public void loadSignature(String filename)
    {
        File file = fileManager.getFileFromTemp(filename,null);
        Bitmap bitmap = ImageManager.getBitmapFromFile(file);
        Bitmap bitmapMutable = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        signature_view.setBitmap(bitmapMutable);
    }

    @Override
    public void clearSign()
    {
        signature_view.clearCanvas();
    }
}
