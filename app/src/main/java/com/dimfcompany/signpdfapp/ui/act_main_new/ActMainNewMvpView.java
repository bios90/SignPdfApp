package com.dimfcompany.signpdfapp.ui.act_main_new;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.adapters.AdapterFinished;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableViewAbstr;
import com.dimfcompany.signpdfapp.models.Model_Document;

import java.util.List;

public class ActMainNewMvpView extends BaseObservableViewAbstr<ActMainNewMvp.ViewListener>
        implements ActMainNewMvp.MvpView
{
    private static final String TAG = "ActMainNewMvpView";

    private final LayoutInflater layoutInflater;

    private TextView tv_profile;
    private TextView tv_admin;
    private TextView tv_title;
    private RecyclerView rec_finished;
    private RelativeLayout la_create_new;

    AdapterFinished adapter_finished;

    public ActMainNewMvpView(LayoutInflater layoutInflater, ViewGroup parent)
    {
        this.layoutInflater = layoutInflater;
        setRootView(layoutInflater.inflate(R.layout.act_main_new,parent,false));
        initViews();
        setListeners();
    }

    private void initViews()
    {
        tv_profile = findViewById(R.id.tv_profile);
        tv_admin = findViewById(R.id.tv_admin);
        tv_title = findViewById(R.id.tv_title);
        rec_finished = findViewById(R.id.rec_finished);
        la_create_new = findViewById(R.id.la_create_new);

        rec_finished.setLayoutManager(new LinearLayoutManager(layoutInflater.getContext()));
        adapter_finished = new AdapterFinished();
        rec_finished.setAdapter(adapter_finished);
    }

    private void setListeners()
    {
        tv_profile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedProfile();
            }
        });

        la_create_new.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedNewDocument();
            }
        });

        tv_admin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedAdmin();
            }
        });
    }

    @Override
    public void toggleAdminBtn(boolean visibility)
    {
        if(visibility)
        {
            tv_admin.setVisibility(View.VISIBLE);
            tv_title.setText("Мои договора");
        }
        else
            {
                tv_admin.setVisibility(View.GONE);
                tv_title.setText("Договора");
            }
    }

    @Override
    public void bindDocuments(List<Model_Document> documents, AdapterFinished.CardFinishedCallback callback)
    {
        adapter_finished.setCallback(callback);
        adapter_finished.setItems(documents);
    }

    @Override
    public void clearRecycler()
    {
        adapter_finished.setItems(null);
    }
}
