package com.dimfcompany.signpdfapp.utils;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.dimfcompany.signpdfapp.base.AppClass;
import com.dimfcompany.signpdfapp.ui.act_sign.ActSign;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.patloew.rxlocation.RxLocation;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

import static android.content.Context.LOCATION_SERVICE;

public class MyLocationManager
{
    private static final String TAG = "MyLocationManager";

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

    public Observable<Location> getSmartLocation()
    {
        final int[] secondsGone = {0};
        return Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap((Function<Long, ObservableSource<Location>>) aLong ->
                {
                    secondsGone[0]++;
                    Log.e(TAG, "getSmartLocation: Inserted int loop " + secondsGone[0]);
                    if (secondsGone[0] == 1)
                    {
                        return getRxGpsLocation(true);
                    }
                    else if (secondsGone[0] == 15)
                    {
                        return getRxFusedLocation();
                    }

                    return null;
                });
    }

    @SuppressLint("MissingPermission")
    public Observable<Location> getRxFusedLocation()
    {
        RxLocation rxLocation = new RxLocation(AppClass.getApp());
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1)
                .setInterval(1000);

        return rxLocation.location().updates(locationRequest);
    }

    @SuppressLint("MissingPermission")
    public Observable<Location> getRxGpsLocation(boolean single)
    {
        PublishSubject<Location> subject = PublishSubject.create();

        LocationManager locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                5, new LocationListener()
                {
                    @Override
                    public void onLocationChanged(Location location)
                    {
                        subject.onNext(location);

                        if (single)
                        {
                            locationManager.removeUpdates(this);
                            subject.onComplete();
                        }
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
                        Log.e(TAG, "onProviderDisabled: Disableeedd");
                        if (single)
                        {
                            locationManager.removeUpdates(this);
                        }
//                        subject.onError(new Throwable("Error nit found Location"));
                    }
                });

        return subject;
    }

    @SuppressLint("MissingPermission")
    public Observable<Location> getLocation(boolean single)
    {
        Log.e(TAG, "getLocation: called hereee");
        return Observable.create(new ObservableOnSubscribe<Location>()
        {
            @Override
            public void subscribe(ObservableEmitter<Location> emitter) throws Exception
            {
                LocationManager locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                        5, new LocationListener()
                        {
                            @Override
                            public void onLocationChanged(Location location)
                            {
                                emitter.onNext(location);

                                if (single)
                                {
                                    locationManager.removeUpdates(this);
                                    emitter.onComplete();
                                }
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
                                Log.e(TAG, "onProviderDisabled: Disableeedd");
                                if (single)
                                {
                                    locationManager.removeUpdates(this);
                                }
                                emitter.onError(new Throwable("Error nit found Location"));
                            }
                        });
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation(boolean single, GetLocationCallback callback)
    {
        LocationManager locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
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

    public static void moveCameraToLocation(GoogleMap googleMap, LatLng latLng, float zoom)
    {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom), 300, null);
    }

    public static void zoomCamera(GoogleMap googleMap, boolean plus)
    {
        LatLng latLng = googleMap.getCameraPosition().target;
        float zoom = googleMap.getCameraPosition().zoom;
        if (plus)
        {
            zoom++;
        }
        else
        {
            zoom--;
        }
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom), 300, null);
    }

    public static void addDefaultMarker(GoogleMap googleMap, LatLng latLng, boolean single)
    {
        if (single)
        {
            googleMap.clear();
        }

        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Локация документа")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }
}
