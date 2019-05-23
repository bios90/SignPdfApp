package com.dimfcompany.signpdfapp.base.viewmvcfactory;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.dimfcompany.signpdfapp.ui.act_add_product_dialog.ActAddProductDialogMvp;
import com.dimfcompany.signpdfapp.ui.act_add_product_dialog.ActAddProductDialogMvpView;
import com.dimfcompany.signpdfapp.ui.act_finished.ActFinishedMvp;
import com.dimfcompany.signpdfapp.ui.act_finished.ActFinishedMvpView;
import com.dimfcompany.signpdfapp.ui.act_first.ActFirstMvp;
import com.dimfcompany.signpdfapp.ui.act_first.ActFirstMvpView;
import com.dimfcompany.signpdfapp.ui.act_main.ActMainMvp;
import com.dimfcompany.signpdfapp.ui.act_main.ActMainMvpView;
import com.dimfcompany.signpdfapp.ui.act_main_new.ActMainNewMvp;
import com.dimfcompany.signpdfapp.ui.act_main_new.ActMainNewMvpView;
import com.dimfcompany.signpdfapp.ui.act_products.ActProductsMvp;
import com.dimfcompany.signpdfapp.ui.act_products.ActProductsMvpView;
import com.dimfcompany.signpdfapp.ui.act_profile_dialog.ActProfileDialogMvp;
import com.dimfcompany.signpdfapp.ui.act_profile_dialog.ActProfileDialogMvpView;
import com.dimfcompany.signpdfapp.ui.act_register.ActRegisterMvp;
import com.dimfcompany.signpdfapp.ui.act_register.ActRegisterMvpView;
import com.dimfcompany.signpdfapp.ui.act_sign.ActSignMvp;
import com.dimfcompany.signpdfapp.ui.act_sign.ActSignMvpView;
import com.dimfcompany.signpdfapp.ui.act_signature_dialog.ActSignatureDialogMvp;
import com.dimfcompany.signpdfapp.ui.act_signature_dialog.ActSignatureDialogMvpView;
import com.dimfcompany.signpdfapp.utils.FileManager;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.MessagesManager;
import com.dimfcompany.signpdfapp.utils.StringManager;

public class ViewMvcFactory
{
    private final LayoutInflater layoutInflater;
    private final FileManager fileManager;
    private final StringManager stringManager;
    private final GlobalHelper globalHelper;
    private final MessagesManager messagesManager;

    public ViewMvcFactory(LayoutInflater layoutInflater, FileManager fileManager, StringManager stringManager, GlobalHelper globalHelper, MessagesManager messagesManager)
    {
        this.layoutInflater = layoutInflater;
        this.fileManager = fileManager;
        this.stringManager = stringManager;
        this.globalHelper = globalHelper;
        this.messagesManager = messagesManager;
    }


    public ActMainMvp.MvpView getActMainMvpView(@Nullable ViewGroup parent)
    {
        return new ActMainMvpView(layoutInflater,parent);
    }

    public ActSignMvp.MvpView getActSignMvpView(@Nullable ViewGroup parent)
    {
        return new ActSignMvpView(layoutInflater,parent,fileManager,globalHelper, messagesManager);
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

    public ActFirstMvp.MvpView getActFirstMvpView(@Nullable ViewGroup parent)
    {
        return new ActFirstMvpView(layoutInflater,parent);
    }
    
    public ActRegisterMvp.MvpView getActRegisterMvpView(@Nullable ViewGroup parent)
    {
        return new ActRegisterMvpView(layoutInflater,parent);
    }    
    
    public ActMainNewMvp.MvpView getActMainNewMvpView(@Nullable ViewGroup parent)
    {
        return new ActMainNewMvpView(layoutInflater,parent);
    }
    
    public ActProfileDialogMvp.MvpView getActProfileDialogMvpView(@Nullable ViewGroup parent)
    {
        return new ActProfileDialogMvpView(layoutInflater,parent);
    }
}
