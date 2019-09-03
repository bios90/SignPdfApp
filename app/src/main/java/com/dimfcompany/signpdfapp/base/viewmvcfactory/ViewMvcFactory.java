package com.dimfcompany.signpdfapp.base.viewmvcfactory;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.dimfcompany.signpdfapp.local_db.sharedprefs.SharedPrefsHelper;
import com.dimfcompany.signpdfapp.ui.act_access.ActAccessMvp;
import com.dimfcompany.signpdfapp.ui.act_access.ActAccessMvpView;
import com.dimfcompany.signpdfapp.ui.act_access_dialog.ActAccessDialogMvp;
import com.dimfcompany.signpdfapp.ui.act_access_dialog.ActAccessDialogMvpView;
import com.dimfcompany.signpdfapp.ui.act_add_product_dialog.ActAddProductDialogMvp;
import com.dimfcompany.signpdfapp.ui.act_add_product_dialog.ActAddProductDialogMvpView;
import com.dimfcompany.signpdfapp.ui.act_admin.ActAdminMvp;
import com.dimfcompany.signpdfapp.ui.act_admin.ActAdminMvpView;
import com.dimfcompany.signpdfapp.ui.act_admin_menu.ActAdminMenuMvp;
import com.dimfcompany.signpdfapp.ui.act_admin_menu.ActAdminMenuMvpView;
import com.dimfcompany.signpdfapp.ui.act_element_dialog.ActElementDialogMvp;
import com.dimfcompany.signpdfapp.ui.act_element_dialog.ActElementDialogMvpView;
import com.dimfcompany.signpdfapp.ui.act_finished.ActFinishedMvp;
import com.dimfcompany.signpdfapp.ui.act_finished.ActFinishedMvpView;
import com.dimfcompany.signpdfapp.ui.act_first.ActFirstMvp;
import com.dimfcompany.signpdfapp.ui.act_first.ActFirstMvpView;
import com.dimfcompany.signpdfapp.ui.act_geo.ActGeoMvp;
import com.dimfcompany.signpdfapp.ui.act_geo.ActGeoMvpView;
import com.dimfcompany.signpdfapp.ui.act_geo_choosing.ActGeoChoosingMvp;
import com.dimfcompany.signpdfapp.ui.act_geo_choosing.ActGeoChoosingMvpView;
import com.dimfcompany.signpdfapp.ui.act_main.ActMainMvp;
import com.dimfcompany.signpdfapp.ui.act_main.ActMainMvpView;
import com.dimfcompany.signpdfapp.ui.act_main_new.ActMainNewMvp;
import com.dimfcompany.signpdfapp.ui.act_main_new.ActMainNewMvpView;
import com.dimfcompany.signpdfapp.ui.act_pre_show.ActPreShowMvp;
import com.dimfcompany.signpdfapp.ui.act_pre_show.ActPreShowMvpView;
import com.dimfcompany.signpdfapp.ui.act_products.ActProductsMvp;
import com.dimfcompany.signpdfapp.ui.act_products.ActProductsMvpView;
import com.dimfcompany.signpdfapp.ui.act_profile_dialog.ActProfileDialogMvp;
import com.dimfcompany.signpdfapp.ui.act_profile_dialog.ActProfileDialogMvpView;
import com.dimfcompany.signpdfapp.ui.act_register.ActRegisterMvp;
import com.dimfcompany.signpdfapp.ui.act_register.ActRegisterMvpView;
import com.dimfcompany.signpdfapp.ui.act_search_dialog.ActSearchDialogMvp;
import com.dimfcompany.signpdfapp.ui.act_search_dialog.ActSearchDialogMvpView;
import com.dimfcompany.signpdfapp.ui.act_sign.ActSignMvp;
import com.dimfcompany.signpdfapp.ui.act_sign.ActSignMvpView;
import com.dimfcompany.signpdfapp.ui.act_signature_dialog.ActSignatureDialogMvp;
import com.dimfcompany.signpdfapp.ui.act_signature_dialog.ActSignatureDialogMvpView;
import com.dimfcompany.signpdfapp.ui.act_user_auth_dialog.ActUserAuthDialogMvp;
import com.dimfcompany.signpdfapp.ui.act_user_auth_dialog.ActUserAuthMvpView;
import com.dimfcompany.signpdfapp.ui.act_user_docs_dialog.ActUserDocsDialogMvp;
import com.dimfcompany.signpdfapp.ui.act_user_docs_dialog.ActUserDocsDialogMvpView;
import com.dimfcompany.signpdfapp.ui.act_user_page.ActUserPageMvp;
import com.dimfcompany.signpdfapp.ui.act_user_page.ActUserPageMvpView;
import com.dimfcompany.signpdfapp.ui.act_vaucher.ActVaucherMvp;
import com.dimfcompany.signpdfapp.ui.act_vaucher.ActVaucherMvpView;
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
    private final SharedPrefsHelper sharedPrefsHelper;

    public ViewMvcFactory(LayoutInflater layoutInflater, FileManager fileManager, StringManager stringManager, GlobalHelper globalHelper, MessagesManager messagesManager, SharedPrefsHelper sharedPrefsHelper)
    {
        this.layoutInflater = layoutInflater;
        this.fileManager = fileManager;
        this.stringManager = stringManager;
        this.globalHelper = globalHelper;
        this.messagesManager = messagesManager;
        this.sharedPrefsHelper = sharedPrefsHelper;
    }


    public ActMainMvp.MvpView getActMainMvpView(@Nullable ViewGroup parent)
    {
        return new ActMainMvpView(layoutInflater, parent);
    }

    public ActSignMvp.MvpView getActSignMvpView(@Nullable ViewGroup parent)
    {
        return new ActSignMvpView(layoutInflater, parent, fileManager, globalHelper, messagesManager);
    }

    public ActSignatureDialogMvp.MvpView getActSignatureDialogMvpView(@Nullable ViewGroup parent)
    {
        return new ActSignatureDialogMvpView(layoutInflater, parent, fileManager);
    }

    public ActFinishedMvp.MvpView getActFinishedMvpView(@Nullable ViewGroup parent)
    {
        return new ActFinishedMvpView(layoutInflater, parent);
    }

    public ActProductsMvp.MvpView getActProductsMvpView(@Nullable ViewGroup parent)
    {
        return new ActProductsMvpView(layoutInflater, parent, stringManager);
    }

    public ActAddProductDialogMvp.MvpView getActAddProductDialogMvpView(@Nullable ViewGroup parent)
    {
        return new ActAddProductDialogMvpView(layoutInflater, parent);
    }

    public ActFirstMvp.MvpView getActFirstMvpView(@Nullable ViewGroup parent)
    {
        return new ActFirstMvpView(layoutInflater, parent);
    }

    public ActRegisterMvp.MvpView getActRegisterMvpView(@Nullable ViewGroup parent)
    {
        return new ActRegisterMvpView(layoutInflater, parent);
    }

    public ActMainNewMvp.MvpView getActMainNewMvpView(@Nullable ViewGroup parent)
    {
        return new ActMainNewMvpView(layoutInflater, parent);
    }

    public ActProfileDialogMvp.MvpView getActProfileDialogMvpView(@Nullable ViewGroup parent)
    {
        return new ActProfileDialogMvpView(layoutInflater, parent);
    }

    public ActAdminMvp.MvpView getActAdminMvpView(@Nullable ViewGroup parent)
    {
        return new ActAdminMvpView(layoutInflater, parent);
    }

    public ActVaucherMvp.MvpView getActVaucherMvpView(@Nullable ViewGroup parent)
    {
        return new ActVaucherMvpView(layoutInflater, parent);
    }

    public ActElementDialogMvp.MvpView getActElementDialogMvpView(@Nullable ViewGroup parent)
    {
        return new ActElementDialogMvpView(layoutInflater, parent);
    }

    public ActPreShowMvp.MvpView getActPreShowMvpView(@Nullable ViewGroup parent)
    {
        return new ActPreShowMvpView(layoutInflater, parent);
    }

    public ActAdminMenuMvp.MvpView getActAdminMenuMvpView(@Nullable ViewGroup parent)
    {
        return new ActAdminMenuMvpView(layoutInflater, parent);
    }

    public ActAccessMvp.MvpView getActAccessMvpView(@Nullable ViewGroup parent)
    {
        return new ActAccessMvpView(layoutInflater, parent);
    }

    public ActSearchDialogMvp.MvpView getActSearchMvpView(@Nullable ViewGroup parent)
    {
        return new ActSearchDialogMvpView(layoutInflater, parent);
    }

    public ActAccessDialogMvp.MvpView getActAccessDialogMvpView(@Nullable ViewGroup parent)
    {
        return new ActAccessDialogMvpView(layoutInflater, parent);
    }

    public ActUserDocsDialogMvp.MvpView getActUserDialogMvpView(@Nullable ViewGroup parent)
    {
        return new ActUserDocsDialogMvpView(layoutInflater, parent);
    }

    public ActUserPageMvp.MvpView getActUserPageMvpView(@Nullable ViewGroup parent)
    {
        return new ActUserPageMvpView(layoutInflater, parent);
    }

    public ActUserAuthDialogMvp.MvpView getActUserAuthMvpView(@Nullable ViewGroup parent)
    {
        return new ActUserAuthMvpView(layoutInflater, parent, sharedPrefsHelper);
    }

    public ActGeoMvp.MvpView getActGeoMvpView(@Nullable ViewGroup parent)
    {
        return new ActGeoMvpView(layoutInflater, parent);
    }

    public ActGeoChoosingMvp.MvpView getActGeoChoosingMvpView(@Nullable ViewGroup parent)
    {
        return new ActGeoChoosingMvpView(layoutInflater,parent);
    }
}
