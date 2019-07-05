package com.dimfcompany.signpdfapp.ui.act_user_page;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableViewAbstr;
import com.dimfcompany.signpdfapp.models.Model_User;
import com.dimfcompany.signpdfapp.utils.ViewBinder;

public class ActUserPageMvpView extends BaseObservableViewAbstr<ActUserPageMvp.ViewListener>
        implements ActUserPageMvp.MvpView
{
    private static final String TAG = "ActUserPageMvpView";

    TextView tv_user_name_header;
    TextView tv_first_name;
    TextView tv_last_name;
    TextView tv_email;
    TextView tv_email_verified;
    TextView tv_admin_approved;
    TextView tv_created_at;
    TextView tv_role;
    TextView tv_docs_count;
    Button btn_edit;
    Button btn_toggle_approved;
    LinearLayout la_docs;

    public ActUserPageMvpView(LayoutInflater layoutInflater, ViewGroup parent)
    {
        setRootView(layoutInflater.inflate(R.layout.act_user_page, parent, false));
        initViews();
        setListeners();
    }

    private void initViews()
    {
        tv_user_name_header = findViewById(R.id.tv_user_name_header);
        tv_first_name = findViewById(R.id.tv_first_name);
        tv_last_name = findViewById(R.id.tv_last_name);
        tv_email = findViewById(R.id.tv_email);
        tv_email_verified = findViewById(R.id.tv_email_verified);
        tv_admin_approved = findViewById(R.id.tv_admin_approved);
        tv_created_at = findViewById(R.id.tv_created_at);
        tv_role = findViewById(R.id.tv_role);
        tv_docs_count = findViewById(R.id.tv_docs_count);
        btn_edit = findViewById(R.id.btn_edit);
        btn_toggle_approved = findViewById(R.id.btn_toggle_approved);

        la_docs = findViewById(R.id.la_docs);
    }

    private void setListeners()
    {
        btn_edit.setOnClickListener(v -> getListener().clickedEdit());
        la_docs.setOnClickListener(v -> getListener().clickedDocuments());
        btn_toggle_approved.setOnClickListener(v -> getListener().clickedToggleApporoved());
    }

    @Override
    public void bindUser(Model_User user, String last_version)
    {
        ViewBinder.bindUserPageInfo(user, last_version, getRootView());
    }
}
