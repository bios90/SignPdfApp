package com.dimfcompany.signpdfapp.ui.act_geo_choosing;

import android.location.Location;

import androidx.fragment.app.FragmentManager;

import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;
import com.google.android.gms.maps.model.LatLng;

public interface ActGeoChoosingMvp
{
    interface MvpView extends BaseObservableView<ViewListener>
    {
        void setMap();
        void bindLatLng(LatLng latLng);
        void moveCameraToLatLng(LatLng latLng);
        LatLng getCurrentLatLng();
    }

    interface ViewListener
    {
        FragmentManager getFragManager();

        LatLng getCurrentLatLng();
        void clickedMyLocation();
        void clickedOk();
        void clickedCancel();
    }
}
