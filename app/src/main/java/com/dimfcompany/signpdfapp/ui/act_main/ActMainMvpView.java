package com.dimfcompany.signpdfapp.ui.act_main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableViewAbstr;

public class ActMainMvpView extends BaseObservableViewAbstr<ActMainMvp.ViewListener>
    implements ActMainMvp.MvpView
{
    private static final String TAG = "ActMainMvpView";

    RelativeLayout la_begin;
    RelativeLayout la_finished;

    public ActMainMvpView(LayoutInflater layoutInflater, ViewGroup parent)
    {
        setRootView(layoutInflater.inflate(R.layout.act_main,parent,false));
        initViews();
        setListeners();
    }

    private void initViews()
    {
        la_begin = findViewById(R.id.la_begin);
        la_finished = findViewById(R.id.la_finished);
    }


    private void setListeners()
    {
        la_begin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedMakeNewSign();
            }
        });

        la_finished.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedFinishedSign();
            }
        });
    }

}
