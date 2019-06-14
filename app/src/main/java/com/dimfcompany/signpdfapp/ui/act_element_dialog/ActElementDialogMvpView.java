package com.dimfcompany.signpdfapp.ui.act_element_dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableViewAbstr;
import com.dimfcompany.signpdfapp.models.Model_Price_Element;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.StringManager;

public class ActElementDialogMvpView extends BaseObservableViewAbstr<ActElementDialogMvp.ViewListener>
    implements ActElementDialogMvp.MvpView
{
    private static final String TAG = "ActElementDialogMvpView";

    EditText et_element_text;
    EditText et_element_price;
    TextView tv_header;
    Button btn_cancel,btn_ok;

    public ActElementDialogMvpView(LayoutInflater layoutInflater, ViewGroup parent)
    {
        setRootView(layoutInflater.inflate(R.layout.act_element_dialog,parent,false));
        initViews();
        setListeners();
    }

    private void initViews()
    {
        et_element_text = findViewById(R.id.et_element_text);
        et_element_price = findViewById(R.id.et_element_price);
        tv_header = findViewById(R.id.tv_header);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_ok = findViewById(R.id.btn_ok);
    }

    private void setListeners()
    {
        btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedOk();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedCancel();
            }
        });
    }

    @Override
    public String getText()
    {
        return StringManager.castToNullIfEmpty(et_element_text.getText().toString().trim());
    }

    @Override
    public void bindPriceElement(Model_Price_Element price_element)
    {
        tv_header.setText("Редактирование Элемента");
        btn_ok.setText("Сохранить");

        et_element_text.setText(price_element.getText());
        et_element_price.setText(StringManager.formatNum(price_element.getPrice(),false));
    }

    @Override
    public Double getPrice()
    {
        return GlobalHelper.getEtDoubleValue(et_element_price);
    }
}
