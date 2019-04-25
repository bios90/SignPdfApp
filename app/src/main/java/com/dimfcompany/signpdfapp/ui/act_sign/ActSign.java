package com.dimfcompany.signpdfapp.ui.act_sign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.utils.FileManager;
import com.dimfcompany.signpdfapp.utils.MessagesManager;
import com.dimfcompany.signpdfapp.utils.PdfCreator;

import javax.inject.Inject;

public class ActSign extends BaseActivity implements ActSignMvp.ViewListener, PdfCreator.PdfCreationCallback
{
    private static final String TAG = "ActSign";

    @Inject
    FileManager fileManager;
    @Inject
    PdfCreator pdfCreator;
    @Inject
    MessagesManager messagesManager;

    ActSignMvp.MvpView mvpView;

    Model_Document model_document;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPresenterComponent().inject(this);
        mvpView = viewMvcFactory.getActSignMvpView(null);
        mvpView.registerListener(this);
        setContentView(mvpView.getRootView());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if(model_document == null)
        {
            model_document = new Model_Document();
        }
    }

    @Override
    public void clickedSignaturePad()
    {
        navigationManager.toSignDialog(mvpView.getCurrentFileName());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        model_document = collectDoucmentData();
        outState.putSerializable(Constants.EXTRA_MODEL_DOCUMENT,model_document);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        model_document = (Model_Document) savedInstanceState.getSerializable(Constants.EXTRA_MODEL_DOCUMENT);
        if(model_document != null)
        {
            mvpView.bindModelDocument(model_document);
        }
    }

    @Override
    public void clickedCreatePDf()
    {
        model_document = collectDoucmentData();
        pdfCreator.createPdfAsync(model_document,this);
    }

    @Override
    public void clickedMaterials()
    {
        model_document = collectDoucmentData();
        navigationManager.toActProducts(Constants.RQ_PRODUCTS_SCREEN,model_document);
    }

    private Model_Document collectDoucmentData()
    {
        model_document.setCity(mvpView.getCity());
        model_document.setFio(mvpView.getFio());
        model_document.setAdress(mvpView.getAdress());
        model_document.setPhone(mvpView.getPhone());
        model_document.setSignature_file_name(mvpView.getCurrentFileName());
        model_document.setDate(System.currentTimeMillis());

        return model_document;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
        {
            switch (requestCode)
            {
                case Constants.RQ_TAKE_SIGNATURE:
                    String fileName = data.getStringExtra(Constants.EXTRA_SIGNATURE_FILE_NAME);
                    if(fileName != null)
                    {
                        mvpView.bindSignatureFile(fileName);
                    }
                    break;

                case Constants.RQ_PRODUCTS_SCREEN:
                    model_document = (Model_Document)data.getSerializableExtra(Constants.EXTRA_MODEL_DOCUMENT);
                    if(model_document != null)
                    {
                        mvpView.bindModelDocument(model_document);
                    }
                    break;
            }
        }

        mvpView.updateMaterialButton();
    }

    @Override
    public void onSuccessPdfCreation()
    {
        messagesManager.showGreenAlerter("Завершено","Новый договор успешно создан");
    }

    @Override
    public void onErrorPdfCreation()
    {
        messagesManager.showRedAlerter("Ошибка","Не удалось создать новый договор");
    }

    @Override
    public Model_Document getDocument()
    {
        return model_document;
    }
}
