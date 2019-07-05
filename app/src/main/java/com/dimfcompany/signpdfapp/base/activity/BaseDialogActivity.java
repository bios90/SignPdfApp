package com.dimfcompany.signpdfapp.base.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.dimfcompany.signpdfapp.R;

public abstract class BaseDialogActivity extends BaseActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        setTheme(R.style.Theme_Transparent);
        super.onCreate(savedInstanceState);
    }
}
