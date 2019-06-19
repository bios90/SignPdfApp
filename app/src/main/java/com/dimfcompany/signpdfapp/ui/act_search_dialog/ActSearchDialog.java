package com.dimfcompany.signpdfapp.ui.act_search_dialog;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.base.viewmvcfactory.ViewMvcFactory;

public class ActSearchDialog extends BaseActivity implements ActSearchDialogMvp.ViewListener
{
    private static final String TAG = "ActSearchDialog";

    public static void startScreenOver(AppCompatActivity activity, @Nullable Integer request_code, @Nullable String search, int sort)
    {
        Intent intent = new Intent(activity, ActSearchDialog.class);
        intent.putExtra(Constants.EXTRA_SEARCH, search);
        intent.putExtra(Constants.EXTRA_SORT, sort);

        if (request_code == null)
        {
            activity.startActivity(intent);
        } else
        {
            activity.startActivityForResult(intent, request_code);
        }
    }

    ActSearchDialogMvp.MvpView mvpView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPresenterComponent().inject(this);
        mvpView = viewMvcFactory.getActSearchMvpView(null);
        mvpView.registerListener(this);
        setContentView(mvpView.getRootView());

        checkForExtra();
    }

    @Override
    public void clickedOk()
    {
        String search = mvpView.getSearchText();
        Integer sort = mvpView.getSortValue();

        Intent return_intent = new Intent();
        return_intent.putExtra(Constants.EXTRA_SEARCH,search);
        return_intent.putExtra(Constants.EXTRA_SORT,sort);
        finishWithResultOk(return_intent);
    }

    @Override
    public void clickedCancel()
    {
        finishWithResultCancel();
    }

    private void checkForExtra()
    {
        String search = getIntent().getStringExtra(Constants.EXTRA_SEARCH);
        int sort = getIntent().getIntExtra(Constants.EXTRA_SORT,999999);

        if(!TextUtils.isEmpty(search))
        {
            mvpView.bindSearch(search);
        }

        if(sort != 999999)
        {
            mvpView.bindSortValue(sort);
        }
    }
}
