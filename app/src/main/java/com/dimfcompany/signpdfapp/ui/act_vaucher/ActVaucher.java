package com.dimfcompany.signpdfapp.ui.act_vaucher;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_Price_Element;
import com.dimfcompany.signpdfapp.models.Model_Vaucher;
import com.dimfcompany.signpdfapp.ui.act_products.ActProducts;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.MessagesManager;

import java.util.ArrayList;

import javax.inject.Inject;

public class ActVaucher extends BaseActivity implements ActVaucherMvp.ViewListener
{
    private static final String TAG = "ActVaucher";

    public static void startScreenOver(AppCompatActivity activity, @Nullable Integer request_code, Model_Document document)
    {
        Intent intent = new Intent(activity, ActVaucher.class);
        intent.putExtra(Constants.EXTRA_MODEL_DOCUMENT, document);

        if (request_code == null)
        {
            activity.startActivity(intent);
        } else
        {
            activity.startActivityForResult(intent, request_code);
        }
    }

    ActVaucherMvp.MvpView mvpView;
    @Inject
    MessagesManager messagesManager;

    private Model_Price_Element lastCalledElementForEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPresenterComponent().inject(this);
        mvpView = viewMvcFactory.getActVaucherMvpView(null);
        mvpView.registerListener(this);
        setContentView(mvpView.getRootView());

        mvpView.bindHeaderSpinner(Constants.headerStrings);
        checkForEdit();
    }



    @Override
    public void clickedBack()
    {
        onBackPressed();
    }

    @Override
    public void clickedCardElement(Model_Price_Element element)
    {
        lastCalledElementForEdit = element;
        navigationManager.toActElementDialog(Constants.RQ_EDIT_PRICE_ELEMENT, element);
    }

    @Override
    public void clickedLongElement(Model_Price_Element element)
    {
        messagesManager.vibrate(100);
        makeAdd(GlobalHelper.copyPriceElement(element));
        mvpView.bindPriceElements();
    }

    @Override
    public void clickedAddElement()
    {
        navigationManager.toActElementDialog(Constants.RQ_ADD_PRICE_ELEMENT, null);
    }

    @Override
    public void clickedDeleteElement(final Model_Price_Element element)
    {
        messagesManager.showSimpleDialog("Удаление", "Удалить элемент " + element.getText() + " ?", "Удалить", "Отмена", new MessagesManager.DialogButtonsListener()
        {
            @Override
            public void onOkClicked(DialogInterface dialog)
            {
                dialog.dismiss();
                getDocument().getVaucher().getPrice_elements().remove(element);
                mvpView.bindPriceElements();
            }

            @Override
            public void onCancelClicked(DialogInterface dialog)
            {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        mvpView.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public Model_Document getDocument()
    {
        return (Model_Document) getIntent().getSerializableExtra(Constants.EXTRA_MODEL_DOCUMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            switch (requestCode)
            {
                case Constants.RQ_ADD_PRICE_ELEMENT:
                    Model_Price_Element price_element = (Model_Price_Element) data.getSerializableExtra(Constants.EXTRA_PRICE_ELEMENT);
                    if (price_element == null)
                    {
                        Log.e(TAG, "onActivityResult: Error Product Element is null");
                        return;
                    }

                    makeAdd(price_element);

                    break;

                case Constants.RQ_EDIT_PRICE_ELEMENT:
                    Model_Price_Element price_element_edited = (Model_Price_Element) data.getSerializableExtra(Constants.EXTRA_PRICE_ELEMENT);
                    if (price_element_edited == null)
                    {
                        Log.e(TAG, "onActivityResult: Error Product Element is null");
                        return;
                    }

                    makeEdit(price_element_edited);

                    break;
            }
        }
    }

    private void makeEdit(Model_Price_Element price_element_edited)
    {
        checkForNullVaucher();

        int index = getDocument().getVaucher().getPrice_elements().indexOf(lastCalledElementForEdit);
        getDocument().getVaucher().getPrice_elements().set(index, price_element_edited);
        mvpView.bindPriceElements();
        mvpView.updateBottomInfo();
    }

    private void makeAdd(Model_Price_Element price_element)
    {
        checkForNullVaucher();

        getDocument().getVaucher().getPrice_elements().add(price_element);
        mvpView.bindPriceElements();
        mvpView.updateBottomInfo();
    }

    private void checkForNullVaucher()
    {
        if (getDocument().getVaucher() == null || getDocument().getVaucher().getPrice_elements() == null)
        {
            getDocument().setVaucher(new Model_Vaucher());
            getDocument().getVaucher().setPrice_elements(new ArrayList<Model_Price_Element>());
        }
    }

    private void checkForEdit()
    {
        if(getDocument().getVaucher() != null)
        {
            if(getDocument().getVaucher().getHeader() != null)
            {
                mvpView.bindHeader(getDocument().getVaucher().getHeader());
            }

            mvpView.bindPriceElements();
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRA_MODEL_DOCUMENT,getDocument());
        getDocument().getVaucher().setHeader(mvpView.getHeaderString());
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}
















