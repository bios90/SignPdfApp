package com.dimfcompany.signpdfapp.ui.act_geo_choosing;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.utils.MessagesManager;
import com.dimfcompany.signpdfapp.utils.MyLocationManager;
import com.google.android.gms.maps.model.LatLng;
import com.hjq.permissions.XXPermissions;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ActGeoChoosing extends BaseActivity implements ActGeoChoosingMvp.ViewListener
{
    private static final String TAG = "ActGeoChoosing";

    public static void startScreenOver(AppCompatActivity activity, @Nullable Integer request_code, @Nullable LatLng latLng)
    {
        Intent intent = new Intent(activity, ActGeoChoosing.class);
        intent.putExtra(Constants.EXTRA_LAT_LNG, latLng);

        if (request_code == null)
        {
            activity.startActivity(intent);
        }
        else
        {
            activity.startActivityForResult(intent, request_code);
        }
    }

    @Inject
    MyLocationManager myLocationManager;
    @Inject
    MessagesManager messagesManager;

    ActGeoChoosingMvp.MvpView mvpView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPresenterComponent().inject(this);
        mvpView = viewMvcFactory.getActGeoChoosingMvpView(null);
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
    public void clickedOk()
    {
        if (mvpView.getCurrentLatLng() == null)
        {
            messagesManager.showRedAlerter("Выберите местоположение");
            return;
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.EXTRA_LAT_LNG, mvpView.getCurrentLatLng());
        finishWithResultOk(returnIntent);
    }

    @Override
    public void clickedCancel()
    {
        finishWithResultCancel();
    }

    @Override
    public void clickedMyLocation()
    {
        Log.e(TAG, "clickedMyLocation: Will get Location");
        myLocationManager.getRxFusedLocation()
                .subscribe(location ->
                {
                    mvpView.moveCameraToLatLng(MyLocationManager.locationToLatLng(location));
                }, throwable ->
                {
                    messagesManager.showRedAlerter("Не удалось найти местоположение пользователя");
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
                Completable.timer(5, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                        .subscribe(() ->
                        {
                            messagesManager.showSimpleDialog("Настйроки", "Перейти в настрйоки доступа геолокации?", "Перейти", "Отмена", new MessagesManager.DialogButtonsListener()
                            {
                                @Override
                                public void onOkClicked(DialogInterface dialog)
                                {
                                    dialog.dismiss();
                                    XXPermissions.gotoPermissionSettings(ActGeoChoosing.this);
                                    finish();

                                }

                                @Override
                                public void onCancelClicked(DialogInterface dialog)
                                {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                        });
            }
        });
    }

    @Override
    public LatLng getCurrentLatLng()
    {
        return getIntent().getParcelableExtra(Constants.EXTRA_LAT_LNG);
    }
}
