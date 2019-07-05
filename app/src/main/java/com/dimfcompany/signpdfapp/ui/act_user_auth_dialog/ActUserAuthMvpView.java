package com.dimfcompany.signpdfapp.ui.act_user_auth_dialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.adapters.AdapterUserRoles;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableViewAbstr;
import com.dimfcompany.signpdfapp.local_db.sharedprefs.SharedPrefsHelper;
import com.dimfcompany.signpdfapp.models.Model_User;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.StringManager;
import com.dimfcompany.signpdfapp.utils.ViewBinder;

public class ActUserAuthMvpView extends BaseObservableViewAbstr<ActUserAuthDialogMvp.ViewListener>
        implements ActUserAuthDialogMvp.MvpView
{
    private static final String TAG = "ActUserAuthMvpView";

    TextView tv_user_name_header;
    EditText et_email;
    EditText et_first_name;
    EditText et_last_name;
    EditText et_password1;
    EditText et_password2;
    Spinner spinner_access;
    RadioGroup rg_approved;
    RadioGroup rg_email_verified;
    Button btn_ok;
    Button btn_cancel;

    AdapterUserRoles adapterRoles;

    private final SharedPrefsHelper sharedPrefsHelper;

    public ActUserAuthMvpView(LayoutInflater layoutInflater, ViewGroup parent, SharedPrefsHelper sharedPrefsHelper)
    {
        this.sharedPrefsHelper = sharedPrefsHelper;
        setRootView(layoutInflater.inflate(R.layout.act_user_auth_dialog, parent, false));
        initViews();
        setListeners();

        checkForNotAdmin();
    }

    private void initViews()
    {
        tv_user_name_header = findViewById(R.id.tv_user_name_header);
        et_email = findViewById(R.id.et_email);
        et_first_name = findViewById(R.id.et_first_name);
        et_last_name = findViewById(R.id.et_last_name);
        et_password1 = findViewById(R.id.et_password1);
        et_password2 = findViewById(R.id.et_password2);
        spinner_access = findViewById(R.id.spinner_access);
        rg_approved = findViewById(R.id.rg_approved);
        rg_email_verified = findViewById(R.id.rg_email_verified);
        btn_ok = findViewById(R.id.btn_ok);
        btn_cancel = findViewById(R.id.btn_cancel);

        adapterRoles = new AdapterUserRoles(getRootView().getContext());
        spinner_access.setAdapter(adapterRoles);
    }

    private void setListeners()
    {
        btn_ok.setOnClickListener(v -> getListener().clickedOk());
        btn_cancel.setOnClickListener(v -> getListener().clickedCancel());

        Log.e(TAG, "setListeners: Will set now listener");
    }

    @Override
    public String getEtEmailText()
    {
        return StringManager.getEtText(et_email);
    }

    @Override
    public String getEtEFirstNameText()
    {
        return StringManager.getEtText(et_first_name);
    }

    @Override
    public String getEtELastNameText()
    {
        return StringManager.getEtText(et_last_name);
    }

    @Override
    public String getEtPassword1()
    {
        return StringManager.getEtText(et_password1);
    }

    @Override
    public String getEtPassword2()
    {
        return StringManager.getEtText(et_password2);
    }

    @Override
    public int getRoleId()
    {
        return GlobalHelper.getRolesSpinnerId(spinner_access);
    }

    @Override
    public int getEmailVerified()
    {
        return GlobalHelper.getRadioGroupValue(rg_email_verified);
    }

    @Override
    public int getAdminApproved()
    {
        return GlobalHelper.getRadioGroupValue(rg_approved);
    }

    @Override
    public void bindUser(Model_User user)
    {
        ViewBinder.bindUserAuthPage(user, getRootView());
    }

    private void checkForNotAdmin()
    {
        if (sharedPrefsHelper.getUserFromSharedPrefs() == null || sharedPrefsHelper.getUserFromSharedPrefs().getRole_id() != 7)
        {
            RelativeLayout la_for_rg_verified = findViewById(R.id.la_for_rg_verified);
            RelativeLayout la_for_rg_approved = findViewById(R.id.la_for_rg_approved);
            RelativeLayout la_for_spinner = findViewById(R.id.la_for_spinner);
            la_for_rg_verified.setVisibility(View.GONE);
            la_for_rg_approved.setVisibility(View.GONE);
            la_for_spinner.setVisibility(View.GONE);
        }
    }

}
