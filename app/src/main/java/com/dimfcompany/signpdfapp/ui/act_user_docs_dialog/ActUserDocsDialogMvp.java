package com.dimfcompany.signpdfapp.ui.act_user_docs_dialog;

import com.dimfcompany.signpdfapp.base.adapters.AdapterFinished;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_User;

import java.util.List;

public interface ActUserDocsDialogMvp
{
    interface MvpView extends BaseObservableView<ViewListener>
    {
        void bindUser(Model_User user, AdapterFinished.CardFinishedCallback callback);
    }

    interface ViewListener
    {

    }
}
