package com.dimfcompany.signpdfapp.ui.act_finished;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.adapters.AdapterFinished;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableViewAbstr;
import com.dimfcompany.signpdfapp.models.Model_Document;

import java.util.List;

public class ActFinishedMvpView extends BaseObservableViewAbstr<ActFinishedMvp.ViewListener>
    implements ActFinishedMvp.MvpView
{
    private static final String TAG = "ActFinishedMvpView";

    private RecyclerView rec_finished;
    private AdapterFinished adapter_finished;

    private final LayoutInflater layoutInflater;

    public ActFinishedMvpView(LayoutInflater layoutInflater, ViewGroup parent)
    {
        this.layoutInflater = layoutInflater;
        setRootView(layoutInflater.inflate(R.layout.act_finished,parent,false));
        initViews();
    }

    private void initViews()
    {
        rec_finished = findViewById(R.id.rec_finished);
        rec_finished.setLayoutManager(new LinearLayoutManager(layoutInflater.getContext()));
        adapter_finished = new AdapterFinished();
        rec_finished.setAdapter(adapter_finished);
    }



    @Override
    public void bindDocuments(List<Model_Document> documents, AdapterFinished.CardFinishedCallback callback)
    {
        adapter_finished.setCallback(callback);
        adapter_finished.setItems(documents);
    }
}
