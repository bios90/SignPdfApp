package com.dimfcompany.signpdfapp.ui.act_signature_dialog;

import android.graphics.Bitmap;

import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableViewAbstr;

public interface ActSignatureDialogMvp
{
    interface MvpView extends BaseObservableView<ViewListener>
    {
        void loadSignature(String filename);
        void clearSign();
        Bitmap getSignatureBitmap();
    }

    interface ViewListener
    {
        void clickedOk();
        void clickedCancel();
    }
}
