package com.dimfcompany.signpdfapp.ui.act_sign;

import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;
import com.dimfcompany.signpdfapp.models.Model_Document;


public interface ActSignMvp
{
    interface MvpView extends BaseObservableView<ViewListener>
    {
        Integer getCity();
        String getFio();
        String getAdress();
        String getPhone();
        String getCurrentFileName();

        void bindModelDocument(Model_Document document);
        void bindSignatureFile(String filename);
        void updateMaterialButton();
    }

    interface ViewListener
    {
        void clickedSignaturePad();
        void clickedCreatePDf();
        void clickedMaterials();
        Model_Document getDocument();
    }
}
