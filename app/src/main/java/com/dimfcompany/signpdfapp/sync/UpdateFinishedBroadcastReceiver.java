package com.dimfcompany.signpdfapp.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UpdateFinishedBroadcastReceiver extends BroadcastReceiver
{
    public interface CallbackUiUpdate
    {
        void updateFinishedCardUi();
    }

    private final CallbackUiUpdate callbackUiUpdate;

    public UpdateFinishedBroadcastReceiver(CallbackUiUpdate callbackUiUpdate)
    {
        this.callbackUiUpdate = callbackUiUpdate;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        callbackUiUpdate.updateFinishedCardUi();
    }
}
