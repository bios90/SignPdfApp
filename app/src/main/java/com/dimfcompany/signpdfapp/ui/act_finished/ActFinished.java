package com.dimfcompany.signpdfapp.ui.act_finished;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.base.adapters.Adapter_Finished;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.sqlite.CrudHelper;
import com.dimfcompany.signpdfapp.ui.act_main.ActMainMvp;
import com.dimfcompany.signpdfapp.utils.FileManager;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.MessagesManager;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

public class ActFinished extends BaseActivity implements ActFinishedMvp.ViewListener, Adapter_Finished.CardFinishedCallback
{
    private static final String TAG = "ActFinished";

    @Inject
    CrudHelper crudHelper;
    @Inject
    FileManager fileManager;
    @Inject
    MessagesManager messagesManager;

    ActFinishedMvp.MvpView mvpView;

    List<Model_Document> documents;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPresenterComponent().inject(this);
        mvpView = viewMvcFactory.getActFinishedMvpView(null);
        mvpView.registerListener(this);
        setContentView(mvpView.getRootView());

        loadLocalData();
    }

    private void loadLocalData()
    {
        documents = crudHelper.getAllSavedDocuments();
        mvpView.bindDocuments(documents, this);
    }


    @Override
    public void clickedDelete(Model_Document document)
    {
        Log.e(TAG, "clickedDelete: Will delete");
    }

    @Override
    public void clickedCard(Model_Document document)
    {
        File file = fileManager.getFileFromTemp(document.getPdf_file_name(),Constants.FOLDER_CONTRACTS, null);
        if (!file.exists())
        {
            messagesManager.showRedAlerter("Ошибка", "Файл не найден");
            return;
        }

        try
        {
            GlobalHelper.openPdf(ActFinished.this, file);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error on pf intent " + e.getMessage());
            messagesManager.showRedAlerter("Ошибка", "На устройстве ну установлены приложения для просмотра pdf");
        }
    }


    @Override
    public void clickedSend(Model_Document document)
    {
        File file = fileManager.getFileFromTemp(document.getPdf_file_name(), Constants.FOLDER_CONTRACTS, null);
        if (!file.exists())
        {
            messagesManager.showRedAlerter("Ошибка", "Файл не найден");
            return;
        }

        try
        {
            GlobalHelper.shareFile(ActFinished.this, file);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error on pf intent " + e.getMessage());
            messagesManager.showRedAlerter("Ошибка", "На устройстве ну установлены приложения для отправки файлов");
        }
    }

    @Override
    public void clickedPhone(Model_Document document)
    {
        String phone = document.getPhone();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }
}
