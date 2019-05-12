package com.dimfcompany.signpdfapp.ui.act_products;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_Product;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.MessagesManager;

import javax.inject.Inject;

public class ActProducts extends BaseActivity implements ActProductsMvp.ViewListener
{
    private static final String TAG = "ActProducts";

    public static void startScreenOver(AppCompatActivity activity, @Nullable Integer request_code, Model_Document document)
    {
        Intent intent = new Intent(activity, ActProducts.class);
        intent.putExtra(Constants.EXTRA_MODEL_DOCUMENT, document);

        if (request_code == null)
        {
            activity.startActivity(intent);
        } else
        {
            activity.startActivityForResult(intent, request_code);
        }
    }

    @Inject
    MessagesManager messagesManager;

    ActProductsMvp.MvpView mvpView;

    Model_Document model_document;
    Model_Product lastCalledForEditProduct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPresenterComponent().inject(this);
        mvpView = viewMvcFactory.getActProductsMvpView(null);
        mvpView.registerListener(this);
        setContentView(mvpView.getRootView());

        model_document = (Model_Document) getIntent().getSerializableExtra(Constants.EXTRA_MODEL_DOCUMENT);


        mvpView.displayProducts();
        mvpView.updateBottomInfo();
    }

    @Override
    public void clickedAddProduct()
    {
        navigationManager.toActAddProductDialog(Constants.RQ_PRODUCTS_DIALOG, null);
    }

    @Override
    public void clickedLongProduct(Model_Product product)
    {
        messagesManager.vibrate(100);
        model_document.getListOfProducts().add(GlobalHelper.copyProduct(product));
        mvpView.displayProducts();
        mvpView.updateBottomInfo();
    }


    @Override
    public void clickedEditProduct(Model_Product product)
    {
        lastCalledForEditProduct = product;
        navigationManager.toActAddProductDialog(Constants.RQ_PRODUCTS_DIALOG_EDIT, product);
        for (Model_Product product1 : model_document.getListOfProducts())
        {
            Log.e(TAG, "Product material name is "+product1.getMaterial().getName()+" ****\n" );
        }
    }

    @Override
    public Model_Document getDocument()
    {
        return model_document;
    }

    @Override
    public void clickedDeleteProduct(final Model_Product product)
    {
        messagesManager.showSimpleDialog("Удалить", "Удалить товар " + product.getMaterial().getName()+"?", "Удалить", "Отмена", new MessagesManager.DialogButtonsListener()
        {
            @Override
            public void onOkClicked(DialogInterface dialog)
            {
                dialog.dismiss();
                model_document.getListOfProducts().remove(product);
                mvpView.displayProducts();
                mvpView.updateBottomInfo();
            }

            @Override
            public void onCancelClicked(DialogInterface dialog)
            {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void clickedBack()
    {
        onBackPressed();
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRA_MODEL_DOCUMENT,model_document);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            switch (requestCode)
            {
                case Constants.RQ_PRODUCTS_DIALOG:
                    Model_Product product = (Model_Product) data.getSerializableExtra(Constants.EXTRA_PRODUCT);
                    if (product != null)
                    {
                        model_document.getListOfProducts().add(product);
                        mvpView.displayProducts();
                    }
                    mvpView.updateBottomInfo();
                    break;

                case Constants.RQ_PRODUCTS_DIALOG_EDIT:
                    Model_Product productEdited = (Model_Product) data.getSerializableExtra(Constants.EXTRA_PRODUCT);
                    int index = model_document.getListOfProducts().indexOf(lastCalledForEditProduct);
                    model_document.getListOfProducts().set(index,productEdited);
                    Log.e(TAG, "Index of last edited in list is  "+model_document.getListOfProducts().indexOf(lastCalledForEditProduct) );
                    mvpView.displayProducts();
                    mvpView.updateBottomInfo();
                    break;
            }
        }
    }
}
