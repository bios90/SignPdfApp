package com.dimfcompany.signpdfapp.ui.act_access;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.adapters.AdapterRvUsers;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableViewAbstr;
import com.dimfcompany.signpdfapp.models.Model_User;
import com.dimfcompany.signpdfapp.ui.act_admin_menu.ActAdminMenuMvp;

import java.util.List;

public class ActAccessMvpView extends BaseObservableViewAbstr<ActAccessMvp.ViewListener>
        implements ActAccessMvp.MvpView
{
    private static final String TAG = "ActAccessMvpView";

    TextView tv_search;
    TextView tv_search_text, tv_sort_text;
    RecyclerView rec_all_users;
    RelativeLayout la_create_user;

    AdapterRvUsers adapter;

    public ActAccessMvpView(LayoutInflater layoutInflater, ViewGroup parent)
    {
        setRootView(layoutInflater.inflate(R.layout.act_access, parent, false));
        initViews();
        setListeners();
    }

    private void initViews()
    {
        tv_search = findViewById(R.id.tv_search);
        tv_search_text = findViewById(R.id.tv_search_text);
        tv_sort_text = findViewById(R.id.tv_sort_text);
        rec_all_users = findViewById(R.id.rec_all_users);
        la_create_user = findViewById(R.id.la_create_user);

        rec_all_users.setLayoutManager(new LinearLayoutManager(getRootView().getContext()));
        adapter = new AdapterRvUsers(getRootView().getContext());
        rec_all_users.setAdapter(adapter);
    }

    private void setListeners()
    {
        tv_search.setOnClickListener((view) ->
        {
            getListener().clickedSearch();
        });

        la_create_user.setOnClickListener(v -> getListener().clickedAddUser());
    }

    @Override
    public void setUsers(List<Model_User> users, String app_last_version, AdapterRvUsers.UsersListener listener)
    {
        adapter.setLast_app_version(app_last_version);
        adapter.setUsers(users);
        adapter.setListener(listener);
    }

    @Override
    public void bindSearchText(String search)
    {
        tv_search_text.setText(search);
    }

    @Override
    public void bindSortText(String sort)
    {
        tv_sort_text.setText(sort);
    }
}
