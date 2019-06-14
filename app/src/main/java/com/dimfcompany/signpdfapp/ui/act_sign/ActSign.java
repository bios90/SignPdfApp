package com.dimfcompany.signpdfapp.ui.act_sign;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.local_db.raw.LocalDatabase;
import com.dimfcompany.signpdfapp.local_db.room.RoomCrudHelper;
import com.dimfcompany.signpdfapp.local_db.sharedprefs.SharedPrefsHelper;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_User;
import com.dimfcompany.signpdfapp.models.Model_Vaucher;
import com.dimfcompany.signpdfapp.sync.SyncManager;
import com.dimfcompany.signpdfapp.sync.Synchronizer;
import com.dimfcompany.signpdfapp.ui.act_finished.ActFinished;
import com.dimfcompany.signpdfapp.utils.FileManager;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.MessagesManager;
import com.dimfcompany.signpdfapp.utils.PdfCreator;
import com.dimfcompany.signpdfapp.utils.StringManager;

import java.io.File;
import java.util.Date;

import javax.inject.Inject;

public class ActSign extends BaseActivity implements ActSignMvp.ViewListener, PdfCreator.PdfCreationCallback, SyncManager.CallbackInsertWithSync
{
    private static final String TAG = "ActSign";

    public static void startScreenOver(AppCompatActivity activity, @Nullable Integer request_code, @Nullable Model_Document document)
    {
        Intent intent = new Intent(activity, ActSign.class);
        intent.putExtra(Constants.EXTRA_MODEL_DOCUMENT, document);

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
    @Inject
    PdfCreator pdfCreator;
    @Inject
    MessagesManager messagesManager;
    @Inject
    LocalDatabase localDatabase;
    @Inject
    Synchronizer synchronizer;
    @Inject
    SharedPrefsHelper sharedPrefsHelper;

    ActSignMvp.MvpView mvpView;

    Model_Document model_document;
    Model_User user;

    boolean editMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPresenterComponent().inject(this);
        mvpView = viewMvcFactory.getActSignMvpView(null);
        mvpView.registerListener(this);
        setContentView(mvpView.getRootView());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        user = sharedPrefsHelper.getUserFromSharedPrefs();
        mvpView.setSignatureSizes();
        checkForEditMode();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        model_document = collectDocumentData();
        outState.putSerializable(Constants.EXTRA_MODEL_DOCUMENT, model_document);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        model_document = (Model_Document) savedInstanceState.getSerializable(Constants.EXTRA_MODEL_DOCUMENT);
        if (model_document != null)
        {
            mvpView.bindModelDocument(model_document);
        }
    }

    @Override
    public void clickedCreatePDf()
    {
        messagesManager.showProgressDialog();
        model_document = collectDocumentData();
        model_document.setUser_id(user.getId());
        pdfCreator.createPdfAsync(model_document, false, this);
    }

    @Override
    public void clickedPreShow()
    {
        navigationManager.toActPreShow(null);
//        model_document = collectDocumentData();
//        pdfCreator.createPdfAsync(model_document, true, this);
    }

    @Override
    public void clickedVaucher()
    {
        model_document = collectDocumentData();
        navigationManager.toActVaucher(Constants.RQ_VAUCHER_SCREEN, model_document);
    }

    @Override
    public void onShowCallSuccess(String fileName)
    {
        File file = fileManager.getFileFromTemp(fileName, Constants.FOLDER_CONTRACTS, null);
        try
        {
            GlobalHelper.openPdf(ActSign.this, file);
        } catch (Exception e)
        {
            Log.e(TAG, "Error on pf intent " + e.getMessage());
            messagesManager.showRedAlerter("Ошибка", "На устройстве ну установлены приложения для просмотра pdf");
        }
    }

    @Override
    public File getSignatureFile()
    {
        if (model_document == null || model_document.getSignature_file_name() == null)
        {
            return null;
        }

        File file = fileManager.getFileFromTemp(model_document.getSignature_file_name(), null);
        return file;
    }

    @Override
    public void clickedMaterials()
    {
        model_document = collectDocumentData();
        navigationManager.toActProducts(Constants.RQ_PRODUCTS_SCREEN, model_document);
    }

    private Model_Document collectDocumentData()
    {
        model_document.setCity(mvpView.getCity());
        model_document.setFio(mvpView.getFio());
        model_document.setAdress(mvpView.getAdress());
        model_document.setPhone(mvpView.getPhone());
        model_document.setSignature_file_name(mvpView.getCurrentFileName());
        model_document.setDate(new Date());
        model_document.setCode(StringManager.getCode(mvpView.getCity()));

        model_document.setSum(GlobalHelper.countSum(model_document));
        model_document.setMontage(mvpView.getMontage());
        model_document.setDelivery(mvpView.getDelivery());
        model_document.setSale(mvpView.getSale());
        model_document.setPrepay(mvpView.getPrePay());
        model_document.setItogo_sum(GlobalHelper.countItogoSum(model_document));

        model_document.setOrder_form(mvpView.getOrderForm());
        model_document.setDop_info(mvpView.getDopInfo());

        model_document = GlobalHelper.clearEmptyOrNullData(model_document);

        return model_document;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            switch (requestCode)
            {
                case Constants.RQ_TAKE_SIGNATURE:
                    String fileName = data.getStringExtra(Constants.EXTRA_SIGNATURE_FILE_NAME);
                    if (fileName != null)
                    {
                        mvpView.bindSignatureFile(fileName);
                    }
                    break;

                case Constants.RQ_PRODUCTS_SCREEN:
                case Constants.RQ_VAUCHER_SCREEN:
                    model_document = (Model_Document) data.getSerializableExtra(Constants.EXTRA_MODEL_DOCUMENT);
                    if (model_document != null)
                    {
                        mvpView.bindModelDocument(model_document);
                    }
                    break;
            }
        }
    }

    @Override
    public void onSuccessPdfCreation(Model_Document model_document)
    {
//        localDatabase.insertDocument(model_document);
        synchronizer.insertDocumentWithSync(model_document, this);
//        messagesManager.showGreenAlerter("Успешно", "Новый договор успешно создан");
    }

    @Override
    public void onErrorPdfCreation()
    {
        messagesManager.dismissProgressDialog();
        messagesManager.showRedAlerter("Ошибка", "Не удалось создать новый договор");
    }

    @Override
    public Model_Document getDocument()
    {
        return model_document;
    }

    @Override
    public void onBackPressed()
    {
        messagesManager.showSimpleDialog("Выйти", "Выйти из редактирования? Незавершенный прогресс будет потерян.", "Выйти", "Отмена", new MessagesManager.DialogButtonsListener()
        {
            @Override
            public void onOkClicked(DialogInterface dialog)
            {
                dialog.dismiss();
                finish();
            }

            @Override
            public void onCancelClicked(DialogInterface dialog)
            {
                dialog.dismiss();
            }
        });
    }

    private void checkForEditMode()
    {
        model_document = (Model_Document) getIntent().getSerializableExtra(Constants.EXTRA_MODEL_DOCUMENT);
        if (model_document == null)
        {
            Log.e(TAG, "checkForEditMode: Document is Null");
            model_document = new Model_Document();
            return;
        }

        editMode = true;
        mvpView.bindModelDocument(model_document);
        mvpView.updateMaterialButton();
    }

    @Override
    public void onSuccessInsert(boolean inserted_to_server)
    {
        messagesManager.dismissProgressDialog();
        messagesManager.showGreenAlerter("Новый договор успешно создан");
        if (!inserted_to_server)
        {
            synchronizer.putSynchronizeTask();
        }
    }

    @Override
    public void onErrorInsert()
    {
        messagesManager.dismissProgressDialog();
        messagesManager.showRedAlerter("Ошибка при создании договора");
    }
}
