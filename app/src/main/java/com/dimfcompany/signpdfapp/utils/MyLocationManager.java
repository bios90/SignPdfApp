package com.dimfcompany.signpdfapp.utils;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.dimfcompany.signpdfapp.ui.act_sign.ActSign;
import com.google.android.gms.maps.model.LatLng;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static android.content.Context.LOCATION_SERVICE;

public class MyLocationManager
{
    public interface CheckPermissionCallback
    {
        void onCheckPermissionOk();

        void onCheckPermissionDenied();
    }

    public interface GetLocationCallback
    {
        void onGetLocationSuccess(Location location);

        void onUnableGetLocation();
    }

    private final AppCompatActivity activity;

    public MyLocationManager(AppCompatActivity activity)
    {
        this.activity = activity;
    }

    public void checkPermissions(CheckPermissionCallback callback)
    {
        if (XXPermissions.isHasPermission(activity, Permission.Group.LOCATION))
        {
            callback.onCheckPermissionOk();
            return;
        }

        XXPermissions.with(activity)
                .constantRequest()
                .permission(Permission.Group.LOCATION)
                .request(new OnPermission()
                {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll)
                    {
                        callback.onCheckPermissionOk();
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick)
                    {
                        callback.onCheckPermissionDenied();
                    }
                });
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation(boolean single, GetLocationCallback callback)
    {
        LocationManager locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,
                5, new LocationListener()
                {
                    @Override
                    public void onLocationChanged(Location location)
                    {
                        if (single)
                        {
                            locationManager.removeUpdates(this);
                        }
                        callback.onGetLocationSuccess(location);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras)
                    {

                    }

                    @Override
                    public void onProviderEnabled(String provider)
                    {

                    }

                    @Override
                    public void onProviderDisabled(String provider)
                    {
                        if (single)
                        {
                            locationManager.removeUpdates(this);
                        }
                        callback.onUnableGetLocation();
                    }
                });
    }

    public static LatLng locationToLatLng(Location location)
    {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }
}
