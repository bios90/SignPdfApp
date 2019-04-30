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
        double getMontage();
        double getDelivery();
        double getSale();
        double getPrePay();
        String getOrderForm();
        String getDopInfo();

        void bindModelDocument(Model_Document document);
        void bindSignatureFile(String filename);
        void updateMaterialButton();
        void setSignatureSizes();
    }

    interface ViewListener
    {
        void clickedSignaturePad();
        void clickedCreatePDf();
        void clickedMaterials();
        void clickedPreShow();
        Model_Document getDocument();
    }
}
