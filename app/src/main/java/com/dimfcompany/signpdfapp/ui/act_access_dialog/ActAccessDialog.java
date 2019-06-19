package com.dimfcompany.signpdfapp.ui.act_access_dialog;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.models.Model_User;
import com.dimfcompany.signpdfapp.ui.act_search_dialog.ActSearchDialog;

public class ActAccessDialog extends BaseActivity implements ActAccessDialogMvp.ViewListener
{
    private static final String TAG = "ActAccessDialog";

    public static void startScreenOver(AppCompatActivity activity, @Nullable Integer request_code, Model_User user)
    {
        Intent intent = new Intent(activity, ActAccessDialog.class);
        intent.putExtra(Constants.EXTRA_USER, user);

        if (request_code == null)
        {
            activity.startActivity(intent);
        }
        else
        {
            activity.startActivityForResult(intent, request_code);
        }
    }

    ActAccessDialogMvp.MvpView mvpView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPresenterComponent().inject(this);
        mvpView = viewMvcFactory.getActAccessDialogMvpView(null);
        mvpView.registerListener(this);
        setContentView(mvpView.getRootView());

        mvpView.bindUser(getUser());
    }

    private Model_User getUser()
    {
        return (Model_User) getIntent().getSerializableExtra(Constants.EXTRA_USER);
    }

    @Override
    public void clickedOk()
    {
        int role_id = mvpView.getSelectedRoleId();
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.EXTRA_USER, getUser());
        returnIntent.putExtra(Constants.EXTRA_ROLE_ID, role_id);
        finishWithResultOk(returnIntent);
    }

    @Override
    public void clickedCancel()
    {
        finishWithResultCancel();
    }
}
