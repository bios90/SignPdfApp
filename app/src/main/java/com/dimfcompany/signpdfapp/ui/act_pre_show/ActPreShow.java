package com.dimfcompany.signpdfapp.ui.act_pre_show;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.dimfcompany.signpdfapp.base.activity.BaseActivity;

public class ActPreShow extends BaseActivity implements ActPreShowMvp.ViewListener
{
    private static final String TAG = "ActPreShow";

    ActPreShowMvp.MvpView mvpView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPresenterComponent().inject(this);
        mvpView = viewMvcFactory.getActPreShowMvpView(null);
        mvpView.registerListener(this);
        setContentView(mvpView.getRootView());
    }

    @Override
    public void clickedPlus()
    {
        resizeTextViews(true);
    }

    @Override
    public void clickedMinus()
    {
        resizeTextViews(false);
    }

    @Override
    public void clickedBack()
    {
        onBackPressed();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mvpView.unregisterListener(this);
    }

    private void resizeTextViews(boolean plus)
    {
        LinearLayout la_for_items = mvpView.getLaForItems();

        for (int a = 0; a < la_for_items.getChildCount(); a++)
        {
            View view = la_for_items.getChildAt(a);
            if (view instanceof TextView)
            {
                TextView textView = (TextView) view;
                float textSize = textView.getTextSize();
                Log.e(TAG, "resizeTextViews: Getted text size is " + textSize);
                textSize = plus ? (textSize + 2) : (textSize - 2);
                Log.e(TAG, "resizeTextViews: Text size to set is " + textSize);
                if (textSize < 20 || textSize > 75)
                {
                    continue;
                }
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }
        }
    }
}
