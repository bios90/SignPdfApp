package com.dimfcompany.signpdfapp.ui.act_geo;

import android.location.Location;

import androidx.fragment.app.FragmentManager;

import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

public interface ActGeoMvp
{
    interface MvpView extends BaseObservableView<ViewListener>
    {
        void setMap();

        void toggleSearch(boolean open);

        void moveCameraToLocation(LatLng latLng);

        void zoomCamera(boolean plus);

        void bindDocuments(List<Model_Document> documents);

        void bindDate(Date date, boolean max);

        Integer getSumMin();
        Integer getSumMax();
        String getSearchText();
    }

    interface ViewListener
    {
        FragmentManager getFragManager();

        void clickedSearch();

        void clickedMyLocation();

        void loadDocuments();

        void clickedDocument(Model_Document document);

        void clickedDate(boolean max);

    }
}
