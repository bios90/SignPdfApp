package com.dimfcompany.signpdfapp.ui.act_element_dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.models.Model_Price_Element;
import com.dimfcompany.signpdfapp.utils.MessagesManager;

import javax.inject.Inject;

public class ActElementDialog extends BaseActivity implements ActElementDialogMvp.ViewListener
{
    private static final String TAG = "ActElementDialog";

    public static void startScreenOver(AppCompatActivity activity, @Nullable Integer request_code, Model_Price_Element price_element)
    {
        Intent intent = new Intent(activity, ActElementDialog.class);
        intent.putExtra(Constants.EXTRA_PRICE_ELEMENT, price_element);

        if (request_code == null)
        {
            activity.startActivity(intent);
        }
        else
        {
            activity.startActivityForResult(intent, request_code);
        }
    }

    ActElementDialogMvp.MvpView mvpView;
    @Inject
    MessagesManager messagesManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPresenterComponent().inject(this);
        mvpView = viewMvcFactory.getActElementDialogMvpView(null);
        mvpView.registerListener(this);
        setContentView(mvpView.getRootView());

        checkForEditMode();
    }

    @Override
    public void clickedOk()
    {
        String text = mvpView.getText();
        Double price = mvpView.getPrice();

        if(text == null)
        {
            messagesManager.showRedAlerter("Заполните поле заглавие");
            return;
        }

        Model_Price_Element price_element = new Model_Price_Element();
        price_element.setText(text);
        price_element.setPrice(price);

        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.EXTRA_PRICE_ELEMENT,price_element);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public void clickedCancel()
    {
        finish();
    }

    private Model_Price_Element getPrice_element()
    {
        return (Model_Price_Element) getIntent().getSerializableExtra(Constants.EXTRA_PRICE_ELEMENT);
    }

    private void checkForEditMode()
    {
        if(getPrice_element() != null)
        {
            mvpView.bindPriceElement(getPrice_element());
        }
    }
}














