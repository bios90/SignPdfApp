package com.dimfcompany.signpdfapp.ui.act_geo_choosing;

import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableViewAbstr;
import com.dimfcompany.signpdfapp.utils.MyLocationManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class ActGeoChoosingMvpView extends BaseObservableViewAbstr<ActGeoChoosingMvp.ViewListener>
        implements ActGeoChoosingMvp.MvpView, OnMapReadyCallback
{
    private static final String TAG = "ActGeoChoosingMvpView";

    private SupportMapFragment fragMap;
    private TextView tv_zoom_minus;
    private TextView tv_zoom_plus;
    private TextView tv_my_location;
    private TextView tv_lat;
    private TextView tv_lon;
    private GoogleMap googleMap;
    private Button btn_ok;
    private Button btn_cancel;

    private final LayoutInflater layoutInflater;

    private LatLng latLng = null;

    public ActGeoChoosingMvpView(LayoutInflater layoutInflater, @Nullable ViewGroup parent)
    {
        this.layoutInflater = layoutInflater;
        setRootView(layoutInflater.inflate(R.layout.act_geo_choosing, parent, false));
        initViews();
        setListeners();
    }

    private void initViews()
    {
        tv_zoom_minus = findViewById(R.id.tv_zoom_minus);
        tv_zoom_plus = findViewById(R.id.tv_zoom_plus);
        tv_my_location = findViewById(R.id.tv_my_location);
        tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);
        btn_ok = findViewById(R.id.btn_ok);
        btn_cancel = findViewById(R.id.btn_cancel);
    }

    private void setListeners()
    {
        tv_zoom_plus.setOnClickListener(v -> MyLocationManager.zoomCamera(googleMap, true));
        tv_zoom_minus.setOnClickListener(v -> MyLocationManager.zoomCamera(googleMap, false));
        tv_my_location.setOnClickListener(v -> getListener().clickedMyLocation());

        btn_ok.setOnClickListener(v -> getListener().clickedOk());
        btn_cancel.setOnClickListener(v -> getListener().clickedCancel());
    }

    @Override
    public void setMap()
    {
        fragMap = (SupportMapFragment) getListener().getFragManager().findFragmentById(R.id.map);
        fragMap.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        Log.e(TAG, "onMapReady: !!!!");
        tv_my_location.setEnabled(true);
        tv_zoom_minus.setEnabled(true);
        tv_zoom_plus.setEnabled(true);
        this.googleMap = googleMap;
        this.googleMap.setMyLocationEnabled(true);
        this.googleMap.getUiSettings().setMapToolbarEnabled(false);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(false);

        this.googleMap.setOnMapLongClickListener(this::bindLatLng);
        this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                marker.remove();
                latLng = null;
                tv_lat.setText("-");
                tv_lon.setText("-");
                return false;
            }
        });

        MyLocationManager.moveCameraToLocation(googleMap, new LatLng(55.7558d, 37.6173d), 15f);

        if (getListener().getCurrentLatLng() != null)
        {
            bindLatLng(getListener().getCurrentLatLng());
            moveCameraToLatLng(getListener().getCurrentLatLng());
        }
        else
        {
            getListener().clickedMyLocation();
        }
    }

    @Override
    public void bindLatLng(LatLng latLng)
    {
        this.latLng = latLng;
        if (latLng != null)
        {
            MyLocationManager.addDefaultMarker(googleMap, latLng, true);
            tv_lat.setText(latLng.latitude + "");
            tv_lon.setText(latLng.longitude + "");
        }
    }

    @Override
    public void moveCameraToLatLng(LatLng latLng)
    {
        if (latLng != null)
        {
            MyLocationManager.moveCameraToLocation(googleMap, latLng, 15f);
        }
    }

    @Override
    public LatLng getCurrentLatLng()
    {
        return latLng;
    }
}
