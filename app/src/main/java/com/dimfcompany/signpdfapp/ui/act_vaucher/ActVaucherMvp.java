package com.dimfcompany.signpdfapp.ui.act_vaucher;

import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_Price_Element;

import java.util.List;

public interface ActVaucherMvp
{
    interface MvpView extends BaseObservableView<ViewListener>
    {
        void bindHeaderSpinner(List<String> headers);
        void bindPriceElements();
        void bindHeader(String header);
        void updateBottomInfo();
        String getHeaderString();
    }

    interface ViewListener
    {
        void clickedBack();
        void clickedAddElement();
        void clickedDeleteElement(Model_Price_Element element);
        void clickedCardElement(Model_Price_Element element);
        void clickedLongElement(Model_Price_Element element);
        Model_Document getDocument();
    }
}
