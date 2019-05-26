package com.dimfcompany.signpdfapp.ui.act_admin;

import com.dimfcompany.signpdfapp.base.adapters.AdapterFinished;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;
import com.dimfcompany.signpdfapp.models.Model_Document;

import java.util.List;

public interface ActAdminMvp
{
    interface MvpView extends BaseObservableView<ViewListener>
    {
        void bindDocuments(List<Model_Document> documents, AdapterFinished.CardFinishedCallback callback);

    }

    interface ViewListener
    {
        void clickedLocal();
        void clickedProfile();
    }
}
