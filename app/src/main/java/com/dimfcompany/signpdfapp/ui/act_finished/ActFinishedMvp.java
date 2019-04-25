package com.dimfcompany.signpdfapp.ui.act_finished;

import com.dimfcompany.signpdfapp.base.adapters.Adapter_Finished;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;
import com.dimfcompany.signpdfapp.models.Model_Document;

import java.util.List;

public interface ActFinishedMvp
{
    interface MvpView extends BaseObservableView<ViewListener>
    {
        void bindDocuments(List<Model_Document> documents, Adapter_Finished.CardFinishedCallback callback);
        void clearRecycler();
    }

    interface ViewListener
    {

    }
}
