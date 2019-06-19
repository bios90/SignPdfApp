package com.dimfcompany.signpdfapp.ui.act_access_dialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.adapters.AdapterUserRoles;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableViewAbstr;
import com.dimfcompany.signpdfapp.models.Model_User;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;

public class ActAccessDialogMvpView extends BaseObservableViewAbstr<ActAccessDialogMvp.ViewListener>
        implements ActAccessDialogMvp.MvpView
{
    private static final String TAG = "ActAccessDialogMvpView";

    Button btn_ok, btn_cancel;
    TextView tv_text;
    Spinner spinner_access;
    AdapterUserRoles adapterRoles;

    public ActAccessDialogMvpView(LayoutInflater layoutInflater, ViewGroup parent)
    {
        setRootView(layoutInflater.inflate(R.layout.act_access_dialog, parent, false));
        initViews();
        setListeners();
    }

    private void initViews()
    {
        btn_ok = findViewById(R.id.btn_ok);
        btn_cancel = findViewById(R.id.btn_cancel);
        spinner_access = findViewById(R.id.spinner_access);
        tv_text = findViewById(R.id.tv_text);

        adapterRoles = new AdapterUserRoles(getRootView().getContext());
        spinner_access.setAdapter(adapterRoles);
    }

    private void setListeners()
    {
        btn_ok.setOnClickListener(view ->
        {
            getListener().clickedOk();
        });

        btn_cancel.setOnClickListener(v ->
        {
            getListener().clickedCancel();
        });
    }

    @Override
    public int getSelectedRoleId()
    {
        switch (spinner_access.getSelectedItemPosition())
        {
            case 0:
                return 1;
            case 1:
                return 7;
            case 2:
                return 999;

            default:
                throw new RuntimeException("Error invalid spinner role id");
        }
    }

    @Override
    public void bindUser(Model_User user)
    {
        tv_text.setText("Выберите уровень доступа для пользователя " + GlobalHelper.getFullName(user) + ".");

        switch (user.getRole_id())
        {
            case 1:
                spinner_access.setSelection(0);
                break;
            case 7:
                spinner_access.setSelection(1);
                break;
            case 999:
                spinner_access.setSelection(2);
                break;
        }
    }
}
