package com.dimfcompany.signpdfapp.ui.act_admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.adapters.AdapterAdmin;
import com.dimfcompany.signpdfapp.base.adapters.AdapterFinished;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableViewAbstr;
import com.dimfcompany.signpdfapp.models.Model_Document;

import java.util.List;

public class ActAdminMvpView extends BaseObservableViewAbstr<ActAdminMvp.ViewListener>
    implements ActAdminMvp.MvpView
{
    private static final String TAG = "ActAdminMenuMvpView";

    private final LayoutInflater layoutInflater;

    AdapterAdmin adapterAdmin;

    TextView tv_profile,tv_local;
    RecyclerView rec_all_documents;

    public ActAdminMvpView(LayoutInflater layoutInflater, ViewGroup parent)
    {
        this.layoutInflater = layoutInflater;
        setRootView(layoutInflater.inflate(R.layout.act_admin,parent,false));
        initViews();
        setListeners();
    }

    private void initViews()
    {
        tv_profile = findViewById(R.id.tv_profile);
        tv_local = findViewById(R.id.tv_local);
        rec_all_documents = findViewById(R.id.rec_all_documents);

        rec_all_documents.setLayoutManager(new LinearLayoutManager(layoutInflater.getContext()));
        adapterAdmin = new AdapterAdmin();
        rec_all_documents.setAdapter(adapterAdmin);
    }

    private void setListeners()
    {
        tv_local.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedLocal();
            }
        });
        tv_profile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedProfile();
            }
        });
    }

    @Override
    public void bindDocuments(List<Model_Document> documents, AdapterFinished.CardFinishedCallback callback)
    {
        adapterAdmin.setItems(documents);
        adapterAdmin.setCallback(callback);
    }
}
