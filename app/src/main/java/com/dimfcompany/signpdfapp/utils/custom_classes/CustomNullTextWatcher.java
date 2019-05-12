package com.dimfcompany.signpdfapp.utils.custom_classes;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CustomNullTextWatcher implements TextWatcher
{
    private final EditText editText;

    public CustomNullTextWatcher(EditText editText)
    {
        this.editText = editText;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
        if(s.equals("0"))
        {
            editText.setText("");
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {

    }

    @Override
    public void afterTextChanged(Editable s)
    {

    }
}
