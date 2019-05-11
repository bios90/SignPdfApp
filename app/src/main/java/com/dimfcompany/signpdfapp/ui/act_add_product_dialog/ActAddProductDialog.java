package com.dimfcompany.signpdfapp.ui.act_add_product_dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.models.Model_Product;
import com.dimfcompany.signpdfapp.ui.act_products.ActProducts;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.MessagesManager;

import javax.inject.Inject;

public class ActAddProductDialog extends BaseActivity implements ActAddProductDialogMvp.ViewListener
{
    private static final String TAG = "ActAddProductDialog";
    public static void startScreenOver(AppCompatActivity activity, @Nullable Integer request_code, Model_Product product)
    {
        Intent intent = new Intent(activity, ActAddProductDialog.class);
        intent.putExtra(Constants.EXTRA_PRODUCT,product);

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

    ActAddProductDialogMvp.MvpView mvpView;

    Model_Product product;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPresenterComponent().inject(this);
        mvpView= viewMvcFactory.getActAddProductDialogMvpView(null);
        mvpView.registerListener(this);
        setContentView(mvpView.getRootView());

        product = (Model_Product) getIntent().getSerializableExtra(Constants.EXTRA_PRODUCT);

        if(product != null)
        {
            mvpView.bindProduct(product);
        }
    }

    @Override
    public void clicked_ok()
    {
        product = mvpView.getProduct();

        if(!GlobalHelper.validateProduct(product))
        {
            mvpView.showProductErrors(product);
            messagesManager.showRedAlerter("Ошибка","Заполните все обязательные поля");
            return;
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.EXTRA_PRODUCT,product);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public void clicked_cancel()
    {
        onBackPressed();
    }

    @Override
    public void onBackPressed()
    {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
