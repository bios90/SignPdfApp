package com.dimfcompany.signpdfapp.ui.act_products;


import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_Product;

public interface ActProductsMvp
{
    interface MvpView extends BaseObservableView<ViewListener>
    {
        void displayProducts();
        void updateBottomInfo();
    }


    interface ViewListener
    {
        void clickedAddProduct();
        void clickedDeleteProduct(Model_Product product);
        void clickedEditProduct(Model_Product product);
        void clickedBack();
        void clickedLongProduct(Model_Product product);
        Model_Document getDocument();
    }
}
