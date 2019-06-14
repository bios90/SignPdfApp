package com.dimfcompany.signpdfapp.ui.act_pre_show;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableViewAbstr;

public class ActPreShowMvpView extends BaseObservableViewAbstr<ActPreShowMvp.ViewListener>
        implements ActPreShowMvp.MvpView
{
    private static final String TAG = "ActPreShowMvpView";

    RelativeLayout la_plus;
    RelativeLayout la_minus;
    TextView tv_back_top;
    LinearLayout la_for_items;

    public ActPreShowMvpView(LayoutInflater layoutInflater, ViewGroup parent)
    {
        setRootView(layoutInflater.inflate(R.layout.act_pre_show, parent, false));
        initViews();
        setListeners();
    }

    private void initViews()
    {
        la_minus = findViewById(R.id.la_minus);
        la_plus = findViewById(R.id.la_plus);
        la_for_items = findViewById(R.id.la_for_items);
        tv_back_top = findViewById(R.id.tv_back_top);
    }

    private void setListeners()
    {
        la_minus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedMinus();
            }
        });

        la_plus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedPlus();
            }
        });

        tv_back_top.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedBack();
            }
        });
    }

    @Override
    public LinearLayout getLaForItems()
    {
        return la_for_items;
    }
}
