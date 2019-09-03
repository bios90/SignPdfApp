package com.dimfcompany.signpdfapp.ui.act_geo;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.networking.WintecApi;
import com.dimfcompany.signpdfapp.networking.helpers.HelperDocuments;
import com.dimfcompany.signpdfapp.utils.DocumentManipulator;
import com.dimfcompany.signpdfapp.utils.DocumentManipulator.ActionType;
import com.dimfcompany.signpdfapp.utils.DocumentManipulator.DocumentFileType;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.MessagesManager;
import com.dimfcompany.signpdfapp.utils.MyLocationManager;
import com.dimfcompany.signpdfapp.utils.ValidationManager;
import com.dimfcompany.signpdfapp.utils.dates.MyDatePicker;
import com.google.android.gms.maps.model.LatLng;
import com.hjq.permissions.XXPermissions;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ActGeo extends BaseActivity implements ActGeoMvp.ViewListener, DocumentManipulator.ManipulateCallback
{
    private static final String TAG = "ActGeo";


    @Inject
    MyLocationManager myLocationManager;
    @Inject
    MessagesManager messagesManager;
    @Inject
    HelperDocuments helperDocuments;
    @Inject
    DocumentManipulator documentManipulator;
    @Inject
    MyDatePicker myDatePicker;
    @Inject
    WintecApi wintecApi;

    ActGeoMvp.MvpView mvpView;

    private Date date_min;
    private Date date_max;
    private Integer sum_min;
    private Integer sum_max;
    private String search;

    private Model_Document lastCalledForGeoChange = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPresenterComponent().inject(this);
        mvpView = viewMvcFactory.getActGeoMvpView(null);
        mvpView.registerListener(this);
        setContentView(mvpView.getRootView());

        checkGeoPermissions();
    }

    @Override
    public FragmentManager getFragManager()
    {
        return getSupportFragmentManager();
    }

    @Override
    public void clickedSearch()
    {
        sum_min = mvpView.getSumMin();
        sum_max = mvpView.getSumMax();
        search = mvpView.getSearchText();

        if (!ValidationManager.validateDocumentSearch(date_min, date_max, sum_min, sum_max))
        {
            String message = ValidationManager.getSearchErrors(date_min, date_max, sum_min, sum_max);
            messagesManager.showRedAlerter(message);
            return;
        }

        mvpView.toggleSearch(false);
        loadDocuments();
    }

    @Override
    public void clickedMyLocation()
    {
        Log.e(TAG, "clickedMyLocation: Will get Location");
        myLocationManager.getRxFusedLocation()
                .subscribe(location ->
                {
                    mvpView.moveCameraToLocation(MyLocationManager.locationToLatLng(location));
                }, throwable ->
                {
                    messagesManager.showRedAlerter("Не удалось найти местоположение пользователя");
                });
    }

    @Override
    public void loadDocuments()
    {
        if (!GlobalHelper.isNetworkAvailable())
        {
            messagesManager.showNoInternetAlerter();
            return;
        }

        compositeDisposable.add(wintecApi.getSearchDocuments(search, GlobalHelper.getDateString(date_min, GlobalHelper.FORMAT_LARAVEL), GlobalHelper.getDateString(date_max, GlobalHelper.FORMAT_LARAVEL), sum_min, sum_max)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable ->
                {
                    messagesManager.showProgressDialog();
                })
                .doOnTerminate(() ->
                {
                    messagesManager.dismissProgressDialog();
                })
                .subscribe(documents ->
                        {
                            if (documents.size() == 0)
                            {
                                messagesManager.showRedAlerter("Не найдено документов по данному запросу");
                            }
                            mvpView.bindDocuments(documents);
                        },
                        throwable ->
                        {
                            messagesManager.showRedAlerter("Ошибка при загрузке документов");
                        }));
    }

    @Override
    public void clickedDocument(Model_Document document)
    {
        messagesManager.showFullDocumentDialog(document, new MessagesManager.DialogFinishedListener()
        {
            @Override
            public void clickedOpenDogovor()
            {
                messagesManager.showProgressDialog();
                documentManipulator.manipulateFromServer(document, DocumentFileType.TYPE_DOCUMENT, ActionType.OPEN, ActGeo.this);
            }

            @Override
            public void clickedOpenCheck()
            {
                messagesManager.showProgressDialog();
                documentManipulator.manipulateFromServer(document, DocumentFileType.TYPE_CHECK, ActionType.OPEN, ActGeo.this);
            }

            @Override
            public void clickedOpenVaucher()
            {
                messagesManager.showProgressDialog();
                documentManipulator.manipulateFromServer(document, DocumentFileType.TYPE_VAUCHER, ActionType.OPEN, ActGeo.this);
            }

            @Override
            public void clickedSendDogovor()
            {
                messagesManager.showProgressDialog();
                documentManipulator.manipulateFromServer(document, DocumentFileType.TYPE_DOCUMENT, ActionType.SHARE, ActGeo.this);
            }

            @Override
            public void clickedSendCheck()
            {
                messagesManager.showProgressDialog();
                documentManipulator.manipulateFromServer(document, DocumentFileType.TYPE_CHECK, ActionType.SHARE, ActGeo.this);
            }

            @Override
            public void clickedSendVaucher()
            {
                messagesManager.showProgressDialog();
                documentManipulator.manipulateFromServer(document, DocumentFileType.TYPE_VAUCHER, ActionType.SHARE, ActGeo.this);
            }

            @Override
            public void clickedPrintCheck()
            {
                messagesManager.showProgressDialog();
                documentManipulator.manipulateFromServer(document, DocumentFileType.TYPE_CHECK, ActionType.PRINT, ActGeo.this);
            }

            @Override
            public void clickedPrintVaucher()
            {
                messagesManager.showProgressDialog();
                documentManipulator.manipulateFromServer(document, DocumentFileType.TYPE_VAUCHER, ActionType.PRINT, ActGeo.this);
            }

            @Override
            public void clickedEdit(Dialog dialog)
            {
                messagesManager.showProgressDialog();
                documentManipulator.manipulateFromServer(document, DocumentFileType.TYPE_DOCUMENT, ActionType.EDIT, ActGeo.this);
            }

            @Override
            public void clickedAddress()
            {
                LatLng latLng = null;

                if (document.getLat() != 0 && document.getLon() != 0)
                {
                    latLng = new LatLng(document.getLat(), document.getLon());
                }

                lastCalledForGeoChange = document;
                navigationManager.toActGeoChoosing(Constants.RQ_GEO_CHOOSING_DIALOG, latLng);
            }

            @Override
            public void clickedDelete()
            {
                messagesManager.showSimpleDialog("Удаление", "Удалить с сервера документ " + document.getCode() + "?", "Удалить", "Отмена", new MessagesManager.DialogButtonsListener()
                {
                    @Override
                    public void onOkClicked(DialogInterface dialog)
                    {
                        dialog.dismiss();
                        messagesManager.showProgressDialog();
                        documentManipulator.manipulateFromServer(document, DocumentFileType.TYPE_DOCUMENT, ActionType.DELETE, ActGeo.this);
                    }

                    @Override
                    public void onCancelClicked(DialogInterface dialog)
                    {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    void checkGeoPermissions()
    {
        myLocationManager.checkPermissions(new MyLocationManager.CheckPermissionCallback()
        {
            @Override
            public void onCheckPermissionOk()
            {
                mvpView.setMap();
            }

            @Override
            public void onCheckPermissionDenied()
            {
                messagesManager.showRedAlerter("Работа приложения невозможна без доступа к геолокации");
                compositeDisposable.add(Completable.timer(5, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                        .subscribe(() ->
                        {
                            messagesManager.showSimpleDialog("Настйроки", "Перейти в настрйоки доступа геолокации?", "Перейти", "Отмена", new MessagesManager.DialogButtonsListener()
                            {
                                @Override
                                public void onOkClicked(DialogInterface dialog)
                                {
                                    dialog.dismiss();
                                    XXPermissions.gotoPermissionSettings(ActGeo.this);
                                    finish();

                                }

                                @Override
                                public void onCancelClicked(DialogInterface dialog)
                                {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                        }));
            }
        });
    }


    @Override
    public void clickedDate(boolean max)
    {
        String title = "";
        if (max)
        {
            title = "Выберите макcиvальную дату";
        }
        else
        {
            title = "Выберите минимальную дату";
        }
        myDatePicker.selectYearMonthDay(title, false, 3, new MyDatePicker.CallbackDatePicker()
        {
            @Override
            public void onDateSelected(Date date)
            {
                if (max)
                {
                    date_max = date;
                }
                else
                {
                    date_min = date;
                }

                mvpView.bindDate(date, max);
            }

            @Override
            public void onCancelled()
            {

            }
        });
    }

    @Override
    public void onNoInternetError()
    {
        messagesManager.dismissProgressDialog();
        messagesManager.showNoInternetAlerter();
    }

    @Override
    public void onDownloadError()
    {
        messagesManager.dismissProgressDialog();
        messagesManager.showRedAlerter("Ошибка при загрузке файлов");
    }

    @Override
    public void onSuccessAction()
    {
        messagesManager.dismissProgressDialog();
    }

    @Override
    public void onSuccessDownloadForEdit(Model_Document document)
    {
        messagesManager.dismissProgressDialog();
        GlobalHelper.resetDocumentIds(document);
        navigationManager.toSignActivity(Constants.RQ_EDIT_TEMPLATE, document);
    }

    @Override
    public void onErrorDownloadForEdit()
    {
        messagesManager.dismissProgressDialog();
        messagesManager.showRedAlerter("Ошибка при загрузке файлов");
    }

    @Override
    public void onSuccessDeleteOnServer()
    {
        messagesManager.dismissProgressDialog();
        messagesManager.showGreenAlerter("Документ удален");
        loadDocuments();
    }

    @Override
    public void onErrorDeleteOnServer()
    {
        messagesManager.dismissProgressDialog();
        messagesManager.showRedAlerter("Ошибка при удалении докуметов");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RQ_EDIT_TEMPLATE)
        {
            loadDocuments();
        }

        if (resultCode == Activity.RESULT_OK)
        {
            switch (requestCode)
            {
                case Constants.RQ_GEO_CHOOSING_DIALOG:
                    LatLng latLng = data.getParcelableExtra(Constants.EXTRA_LAT_LNG);
                    updateDocumentLocation(latLng);
                    break;
            }
        }
    }

    private void updateDocumentLocation(LatLng latLng)
    {
        if (!GlobalHelper.isNetworkAvailable())
        {
            messagesManager.showNoInternetAlerter();
            return;
        }

        compositeDisposable.add(
                helperDocuments.updateDocumentLocation(lastCalledForGeoChange.getId(), latLng.latitude, latLng.longitude)
                        .doOnSubscribe(disposable -> messagesManager.showProgressDialog())
                        .doOnTerminate(() -> messagesManager.dismissProgressDialog())
                        .subscribe(() ->
                        {
                            messagesManager.showGreenAlerter("Геолокация документа изменена");
                            loadDocuments();
                        }, throwable -> messagesManager.showRedAlerter("Ошибка при изменении геолокации"))

        );
    }
}
