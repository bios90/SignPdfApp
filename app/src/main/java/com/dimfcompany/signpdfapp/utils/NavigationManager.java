package com.dimfcompany.signpdfapp.utils;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_Product;
import com.dimfcompany.signpdfapp.ui.act_add_product_dialog.ActAddProductDialog;
import com.dimfcompany.signpdfapp.ui.act_finished.ActFinished;
import com.dimfcompany.signpdfapp.ui.act_products.ActProducts;
import com.dimfcompany.signpdfapp.ui.act_sign.ActSign;
import com.dimfcompany.signpdfapp.ui.act_signature_dialog.ActSignatureDialog;

public class NavigationManager
{
    private final AppCompatActivity activity;

    public NavigationManager(AppCompatActivity activity)
    {
        this.activity = activity;
    }

    public void toSignActivity(@Nullable Integer requestCode, @Nullable Model_Document document)
    {
        ActSign.startScreenOver(activity, requestCode, document);
    }


    public void toSignDialog(@Nullable String fileName)
    {
        ActSignatureDialog.startScreenOver(activity, Constants.RQ_TAKE_SIGNATURE, fileName);
    }

    public void toActFinished(@Nullable Integer requestCode)
    {
        BaseActivity.startScreen(activity, ActFinished.class, requestCode);
    }


    public void toActProducts(@Nullable Integer requestCode, Model_Document document)
    {
        ActProducts.startScreenOver(activity, requestCode, document);
    }

    public void toActAddProductDialog(@Nullable Integer requestCode, @Nullable Model_Product product)
    {
        ActAddProductDialog.startScreenOver(activity, requestCode, product);
    }
}
