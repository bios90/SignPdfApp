package com.dimfcompany.signpdfapp.utils.custom_classes;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.dimfcompany.signpdfapp.utils.StringManager;

public class DecimalTextWatcher implements TextWatcher
{
    EditText editText;

    public DecimalTextWatcher(EditText editText)
    {
        this.editText = editText;
    }


    private static final String TAG = "DecimalTextWatcher";

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {

    }

    @Override
    public void afterTextChanged(Editable s)
    {
        editText.removeTextChangedListener(this);
        try
        {
            String str =  StringManager.formatNum(s.toString(),false);
            editText.setText(str);
        }
        catch (Exception e)
        {
            Log.e(TAG, "onTextChanged: Exception on formating "+e.getMessage() );
        }
        editText.addTextChangedListener(this);
    }
}
