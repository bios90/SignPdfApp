package com.dimfcompany.signpdfapp.ui.act_element_dialog;

import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;
import com.dimfcompany.signpdfapp.models.Model_Price_Element;

public interface ActElementDialogMvp
{
    interface MvpView extends BaseObservableView<ViewListener>
    {
        String getText();
        Double getPrice();
        void bindPriceElement(Model_Price_Element price_element);
    }

    interface ViewListener
    {
        void clickedOk();
        void clickedCancel();
    }
}
