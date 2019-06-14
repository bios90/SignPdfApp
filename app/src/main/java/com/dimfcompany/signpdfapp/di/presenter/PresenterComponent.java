package com.dimfcompany.signpdfapp.di.presenter;

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

import dagger.Subcomponent;

@Subcomponent(modules = {PresenterModule.class})
public interface PresenterComponent
{
    void inject(ActMain actMain);
    void inject(ActSign actSign);
    void inject(ActSignatureDialog actSignatureDialog);
    void inject(ActFinished actFinished);
    void inject(ActProducts actProducts);
    void inject(ActAddProductDialog actAddProductDialog);
    void inject(ActFirst actFirst);
    void inject(ActRegister actRegister);
    void inject(ActMainNew actMainNew);
    void inject(ActProfileDialog actProfileDialog);
    void inject(ActAdmin actAdmin);
    void inject(ActVaucher actVaucher);
    void inject(ActElementDialog actElementDialog);
    void inject(ActPreShow actPreShow);
}
