package com.dimfcompany.signpdfapp.utils.custom_classes;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class CustomEditTextNullFocusedChanged implements View.OnFocusChangeListener
{
    private static final String TAG = "CustomEditTextNullFocus";

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        try
        {
            EditText editText = (EditText)v;
            if(editText.getText().toString().equals("0"))
            {
                editText.setText("");
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "onFocusChange: Exception on focused changed "+e.getMessage() );
        }
    }
}
