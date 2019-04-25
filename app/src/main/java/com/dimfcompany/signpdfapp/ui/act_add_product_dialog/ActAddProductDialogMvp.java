package com.dimfcompany.signpdfapp.ui.act_add_product_dialog;

import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;
import com.dimfcompany.signpdfapp.models.Model_Product;

public interface ActAddProductDialogMvp
{
    interface MvpView extends BaseObservableView<ViewListener>
    {
        Model_Product getProduct();
        void bindProduct(Model_Product product);
        void showProductErrors(Model_Product product);
    }

    interface ViewListener
    {
        void clicked_ok();
        void clicked_cancel();
    }
}
