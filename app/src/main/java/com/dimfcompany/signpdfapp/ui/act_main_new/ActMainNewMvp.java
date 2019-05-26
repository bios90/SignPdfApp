package com.dimfcompany.signpdfapp.ui.act_main_new;

import com.dimfcompany.signpdfapp.base.adapters.AdapterFinished;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;
import com.dimfcompany.signpdfapp.models.Model_Document;

import java.util.List;

public interface ActMainNewMvp
{
    interface MvpView extends BaseObservableView<ViewListener>
    {
        void bindDocuments(List<Model_Document> documents, AdapterFinished.CardFinishedCallback callback);
        void clearRecycler();
        void toggleAdminBtn(boolean visibility);
    }

    interface ViewListener
    {
        void clickedNewDocument();
        void clickedProfile();
        void clickedAdmin();
    }
}
