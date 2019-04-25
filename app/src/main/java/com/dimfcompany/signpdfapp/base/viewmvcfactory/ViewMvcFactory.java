package com.dimfcompany.signpdfapp.base.viewmvcfactory;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dimfcompany.signpdfapp.ui.act_add_product_dialog.ActAddProductDialogMvp;
import com.dimfcompany.signpdfapp.ui.act_add_product_dialog.ActAddProductDialogMvpView;
import com.dimfcompany.signpdfapp.ui.act_finished.ActFinishedMvp;
import com.dimfcompany.signpdfapp.ui.act_finished.ActFinishedMvpView;
import com.dimfcompany.signpdfapp.ui.act_main.ActMainMvp;
import com.dimfcompany.signpdfapp.ui.act_main.ActMainMvpView;
import com.dimfcompany.signpdfapp.ui.act_products.ActProductsMvp;
import com.dimfcompany.signpdfapp.ui.act_products.ActProductsMvpView;
import com.dimfcompany.signpdfapp.ui.act_sign.ActSignMvp;
import com.dimfcompany.signpdfapp.ui.act_sign.ActSignMvpView;
import com.dimfcompany.signpdfapp.ui.act_signature_dialog.ActSignatureDialogMvp;
import com.dimfcompany.signpdfapp.ui.act_signature_dialog.ActSignatureDialogMvpView;
import com.dimfcompany.signpdfapp.utils.FileManager;
import com.dimfcompany.signpdfapp.utils.StringManager;

public class ViewMvcFactory
{
    private final LayoutInflater layoutInflater;
    private final FileManager fileManager;
    private final StringManager stringManager;

    public ViewMvcFactory(LayoutInflater layoutInflater, FileManager fileManager, StringManager stringManager)
    {
        this.layoutInflater = layoutInflater;
        this.fileManager = fileManager;
        this.stringManager = stringManager;
    }


    public ActMainMvp.MvpView getActMainMvpView(@Nullable ViewGroup parent)
    {
        return new ActMainMvpView(layoutInflater,parent);
    }

    public ActSignMvp.MvpView getActSignMvpView(@Nullable ViewGroup parent)
    {
        return new ActSignMvpView(layoutInflater,parent,fileManager);
    }

    public ActSignatureDialogMvp.MvpView getActSignatureDialogMvpView(@Nullable ViewGroup parent)
    {
        return new ActSignatureDialogMvpView(layoutInflater,parent, fileManager);
    }

    public ActFinishedMvp.MvpView getActFinishedMvpView(@Nullable ViewGroup parent)
    {
        return new ActFinishedMvpView(layoutInflater,parent);
    }

    public ActProductsMvp.MvpView getActProductsMvpView(@Nullable ViewGroup parent)
    {
        return new ActProductsMvpView(layoutInflater,parent, stringManager);
    }

    public ActAddProductDialogMvp.MvpView getActAddProductDialogMvpView(@Nullable ViewGroup parent)
    {
        return new ActAddProductDialogMvpView(layoutInflater,parent);
    }
}
