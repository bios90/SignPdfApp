package com.dimfcompany.signpdfapp.ui.act_user_docs_dialog;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.adapters.AdapterFinished;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableViewAbstr;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_User;
import com.dimfcompany.signpdfapp.utils.StringManager;

import java.util.List;

public class ActUserDocsDialogMvpView extends BaseObservableViewAbstr<ActUserDocsDialogMvp.ViewListener>
        implements ActUserDocsDialogMvp.MvpView
{
    private static final String TAG = "ActUserDocsDialogMvpVie";

    TextView tv_user_name;
    RecyclerView rec_user_docs;
    AdapterFinished adapterFinished;

    public ActUserDocsDialogMvpView(LayoutInflater layoutInflater, ViewGroup parent)
    {
        setRootView(layoutInflater.inflate(R.layout.act_user_docs_dialog, parent, false));
        initViews();
    }

    private void initViews()
    {
        tv_user_name = findViewById(R.id.tv_user_name);
        rec_user_docs = findViewById(R.id.rec_user_docs);

        rec_user_docs.setLayoutManager(new LinearLayoutManager(getRootView().getContext()));
        adapterFinished = new AdapterFinished();
        adapterFinished.setShowSync(false);
        rec_user_docs.setAdapter(adapterFinished);
    }

    @Override
    public void bindUser(Model_User user, AdapterFinished.CardFinishedCallback callback)
    {
        tv_user_name.setText(StringManager.getFullName(user));
        adapterFinished.setItems(user.getDocuments());
        adapterFinished.setCallback(callback);
    }
}
