package com.dimfcompany.signpdfapp.ui.act_search_dialog;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableViewAbstr;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.StringManager;

public class ActSearchDialogMvpView extends BaseObservableViewAbstr<ActSearchDialogMvp.ViewListener>
        implements ActSearchDialogMvp.MvpView
{
    private static final String TAG = "ActSearchDialogMvpView";

    EditText et_search;
    TextView tv_search_icon;
    RadioGroup rg_sort;
    Button btn_ok, btn_cancel;

    public ActSearchDialogMvpView(LayoutInflater layoutInflater, ViewGroup parent)
    {
        setRootView(layoutInflater.inflate(R.layout.act_search_dialog, parent, false));
        initViews();
        setListeners();
    }

    private void initViews()
    {
        et_search = findViewById(R.id.et_search);
        tv_search_icon = findViewById(R.id.tv_search_icon);
        rg_sort = findViewById(R.id.rg_sort);
        btn_ok = findViewById(R.id.btn_ok);
        btn_cancel = findViewById(R.id.btn_cancel);
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
    public void bindSearch(String search)
    {
        if (TextUtils.isEmpty(search))
        {
            return;
        }

        et_search.setText(search);
    }

    @Override
    public void bindSortValue(Integer sort)
    {
        if (sort == null || sort > rg_sort.getChildCount())
        {
            return;
        }

        ((RadioButton) rg_sort.getChildAt(sort)).setChecked(true);
    }

    @Override
    public String getSearchText()
    {
        return StringManager.getEtText(et_search);
    }

    @Override
    public Integer getSortValue()
    {
        try
        {
            return GlobalHelper.getRadioGroupValue(rg_sort);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
