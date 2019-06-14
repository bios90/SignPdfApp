package com.dimfcompany.signpdfapp.utils;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_Price_Element;
import com.dimfcompany.signpdfapp.models.Model_Product;
import com.dimfcompany.signpdfapp.ui.act_add_product_dialog.ActAddProductDialog;
import com.dimfcompany.signpdfapp.ui.act_admin.ActAdmin;
import com.dimfcompany.signpdfapp.ui.act_element_dialog.ActElementDialog;
import com.dimfcompany.signpdfapp.ui.act_finished.ActFinished;
import com.dimfcompany.signpdfapp.ui.act_first.ActFirst;
import com.dimfcompany.signpdfapp.ui.act_main.ActMain;
import com.dimfcompany.signpdfapp.ui.act_main_new.ActMainNew;
import com.dimfcompany.signpdfapp.ui.act_pre_show.ActPreShow;
import com.dimfcompany.signpdfapp.ui.act_products.ActProducts;
import com.dimfcompany.signpdfapp.ui.act_profile_dialog.ActProfileDialog;
import com.dimfcompany.signpdfapp.ui.act_register.ActRegister;
import com.dimfcompany.signpdfapp.ui.act_sign.ActSign;
import com.dimfcompany.signpdfapp.ui.act_signature_dialog.ActSignatureDialog;
import com.dimfcompany.signpdfapp.ui.act_vaucher.ActVaucher;

public class NavigationManager
{
    private final AppCompatActivity activity;

    public NavigationManager(AppCompatActivity activity)
    {
        this.activity = activity;
    }

    public void toRegisterActivity(@Nullable Integer requestCode)
    {
        ActRegister.startScreen(activity, ActRegister.class, requestCode);
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

    public void toActMainNew(@Nullable Integer requestCode)
    {
        BaseActivity.startScreen(activity, ActMainNew.class, requestCode);
    }

    public void toActProfileDialog(@Nullable Integer requestCode)
    {
        BaseActivity.startScreen(activity, ActProfileDialog.class, requestCode);
    }

    public void toActFirst(@Nullable Integer requestCode)
    {
        BaseActivity.startScreen(activity, ActFirst.class, requestCode);
    }

    public void toActAdmin(@Nullable Integer requestCode)
    {
        BaseActivity.startScreen(activity, ActAdmin.class, requestCode);
    }

    public void toActVaucher(@Nullable Integer requestCode, Model_Document document)
    {
        ActVaucher.startScreenOver(activity, requestCode, document);
    }

    public void toActElementDialog(@Nullable Integer requestCode, Model_Price_Element element)
    {
        ActElementDialog.startScreenOver(activity, requestCode, element);
    }

    public void toActPreShow(@Nullable Integer requestCode)
    {
        BaseActivity.startScreen(activity, ActPreShow.class,requestCode);
    }
}
