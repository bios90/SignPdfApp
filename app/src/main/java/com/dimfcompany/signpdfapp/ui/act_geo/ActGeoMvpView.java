package com.dimfcompany.signpdfapp.ui.act_geo;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableViewAbstr;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.MyLocationManager;
import com.dimfcompany.signpdfapp.utils.StringManager;
import com.dimfcompany.signpdfapp.utils.ViewBinder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.Date;
import java.util.List;

public class ActGeoMvpView extends BaseObservableViewAbstr<ActGeoMvp.ViewListener>
        implements ActGeoMvp.MvpView, OnMapReadyCallback
{
    private static final String TAG = "ActGeoMvpView";

    private final LayoutInflater layoutInflater;

    private SupportMapFragment fragMap;
    private TextView tv_search;
    private TextView tv_zoom_minus;
    private TextView tv_zoom_plus;
    private TextView tv_my_location;
    private GoogleMap googleMap;

    private ExpandableLayout ex_search;
    private EditText et_search;
    private TextView tv_date_min;
    private TextView tv_date_max;
    private EditText et_sum_min;
    private EditText et_sum_max;
    private RelativeLayout la_search;

    public ActGeoMvpView(LayoutInflater layoutInflater, @Nullable ViewGroup parent)
    {
        this.layoutInflater = layoutInflater;
        setRootView(layoutInflater.inflate(R.layout.act_geo, parent, false));

        initViews();
        setListeners();
    }

    private void initViews()
    {
        tv_search = findViewById(R.id.tv_search);
        tv_my_location = findViewById(R.id.tv_my_location);
        tv_zoom_minus = findViewById(R.id.tv_zoom_minus);
        tv_zoom_plus = findViewById(R.id.tv_zoom_plus);

        ex_search = findViewById(R.id.ex_search);
        et_search = findViewById(R.id.et_search);
        tv_date_min = findViewById(R.id.tv_date_min);
        tv_date_max = findViewById(R.id.tv_date_max);
        et_sum_min = findViewById(R.id.et_sum_min);
        et_sum_max = findViewById(R.id.et_sum_max);
        la_search = findViewById(R.id.la_search);
    }

    private void setListeners()
    {
        tv_my_location.setOnClickListener(v -> getListener().clickedMyLocation());

        tv_zoom_plus.setOnClickListener(v -> zoomCamera(true));

        tv_zoom_minus.setOnClickListener(v -> zoomCamera(false));

        tv_search.setOnClickListener(v -> ex_search.toggle());

        tv_date_min.setOnClickListener((v) ->
        {
            getListener().clickedDate(false);
        });

        tv_date_max.setOnClickListener((v) ->
        {
            getListener().clickedDate(true);
        });

        la_search.setOnClickListener((v) ->
        {
            getListener().clickedSearch();
        });

    }

    @Override
    public void setMap()
    {
        fragMap = (SupportMapFragment) getListener().getFragManager().findFragmentById(R.id.map);
        fragMap.getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
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
        getListener().clickedMyLocation();
        getListener().loadDocuments();

        moveCameraToLocation(new LatLng(55.7558d, 37.6173d));
    }

    @Override
    public Integer getSumMin()
    {
        return GlobalHelper.getEtIntegerValueOrNull(et_sum_min);
    }

    @Override
    public Integer getSumMax()
    {
        return GlobalHelper.getEtIntegerValueOrNull(et_sum_max);
    }

    @Override
    public String getSearchText()
    {
        return StringManager.getEtText(et_search);
    }

    @Override
    public void moveCameraToLocation(LatLng latLng)
    {
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10), 300, null);
    }

    @Override
    public void toggleSearch(boolean open)
    {
        if (open)
        {
            ex_search.expand(true);
        }
        else
        {
            ex_search.collapse(true);
        }
    }

    @Override
    public void zoomCamera(boolean plus)
    {
        LatLng latLng = googleMap.getCameraPosition().target;
        Float zoom = googleMap.getCameraPosition().zoom;
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

    @Override
    public void bindDate(Date date, boolean max)
    {
        String dateStr = GlobalHelper.getDateString(date);

        if (max)
        {
            tv_date_max.setText(dateStr);
        }
        else
        {
            tv_date_min.setText(dateStr);
        }
    }

    @Override
    public void bindDocuments(List<Model_Document> documents)
    {
        googleMap.clear();

        for (Model_Document document : documents)
        {
            View view = layoutInflater.inflate(R.layout.la_marker, null, false);
            ViewBinder.bindDocumentToMarkerView(document, view);
            Bitmap btm = GlobalHelper.getBitmapFromView(view);
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(document.getLat(), document.getLon()))
                    .icon(BitmapDescriptorFactory.fromBitmap(btm)));
            marker.setTag(document);
        }

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                Model_Document document = (Model_Document) marker.getTag();
                if (document != null)
                {
                    getListener().clickedDocument(document);
                }

                return false;
            }
        });
    }
}
