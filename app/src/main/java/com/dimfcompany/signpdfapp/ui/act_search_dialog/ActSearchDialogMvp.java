package com.dimfcompany.signpdfapp.ui.act_search_dialog;

import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;

public interface ActSearchDialogMvp
{
    interface MvpView extends BaseObservableView<ViewListener>
    {
        String getSearchText();
        Integer getSortValue();
        void bindSearch(String search);
        void bindSortValue(Integer sort);
    }

    interface ViewListener
    {
        void clickedOk();
        void clickedCancel();
    }
}
