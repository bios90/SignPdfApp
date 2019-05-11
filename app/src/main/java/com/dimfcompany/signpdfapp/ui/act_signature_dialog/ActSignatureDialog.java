package com.dimfcompany.signpdfapp.ui.act_signature_dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.utils.FileManager;

import java.io.File;

import javax.inject.Inject;

public class ActSignatureDialog extends BaseActivity implements ActSignatureDialogMvp.ViewListener
{
    private static final String TAG = "ActSignatureDialog";


    public static void startScreenOver(AppCompatActivity activity, @Nullable Integer request_code, String fileName)
    {
        Intent intent = new Intent(activity, ActSignatureDialog.class);
        intent.putExtra(Constants.EXTRA_SIGNATURE_FILE_NAME,fileName);

        if (request_code == null)
        {
            activity.startActivity(intent);
        } else
        {
            activity.startActivityForResult(intent, request_code);
        }
    }

    @Inject
    FileManager fileManager;

    ActSignatureDialogMvp.MvpView mvpView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        makeLandscape();
        getPresenterComponent().inject(this);
        mvpView = viewMvcFactory.getActSignatureDialogMvpView(null);
        mvpView.registerListener(this);
        setContentView(mvpView.getRootView());

        checkForExtra();
    }

    @Override
    public void clickedOk()
    {
        String filename = null;

        Bitmap bitmap = mvpView.getSignatureBitmap();
        if(bitmap != null)
        {
            File file = fileManager.saveBitmapToFile(bitmap);
            filename = FileManager.getFileName(file);
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.EXTRA_SIGNATURE_FILE_NAME,filename);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public void clickedCancel()
    {
        onBackPressed();
    }


    @Override
    public void onBackPressed()
    {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED,returnIntent);
        finish();
    }

    private void checkForExtra()
    {
        String fileName = getIntent().getStringExtra(Constants.EXTRA_SIGNATURE_FILE_NAME);
        if(fileName != null)
        {
            mvpView.loadSignature(fileName);
        }
    }

}
