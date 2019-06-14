package com.dimfcompany.signpdfapp.ui.act_add_product_dialog;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableViewAbstr;
import com.dimfcompany.signpdfapp.models.Model_Color;
import com.dimfcompany.signpdfapp.models.Model_Control;
import com.dimfcompany.signpdfapp.models.Model_Krep;
import com.dimfcompany.signpdfapp.models.Model_Material;
import com.dimfcompany.signpdfapp.models.Model_Product;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.ImageManager;

public class ActAddProductDialogMvpView extends BaseObservableViewAbstr<ActAddProductDialogMvp.ViewListener>
        implements ActAddProductDialogMvp.MvpView
{

    private static final String TAG = "ActAddProductDialogMvp";
    TextView tv_header;
    EditText et_material_name;
    EditText et_width;
    EditText et_height;
    EditText et_color;
    EditText et_krep;
    EditText et_control;
    EditText et_count;
    EditText et_price;
    EditText et_sum;

    Button btn_ok;
    Button btn_cancel;

    TextWatcher textWatcher;

    public ActAddProductDialogMvpView(LayoutInflater layoutInflater, ViewGroup parent)
    {
        setRootView(layoutInflater.inflate(R.layout.act_add_product_dialog, parent, false));
        initViews();
        setListeners();
    }

    private void initViews()
    {
        tv_header = findViewById(R.id.tv_header);
        et_material_name = findViewById(R.id.et_material_name);
        et_width = findViewById(R.id.et_width);
        et_height = findViewById(R.id.et_height);
        et_color = findViewById(R.id.et_color);
        et_krep = findViewById(R.id.et_krep);
        et_control = findViewById(R.id.et_control);
        et_count = findViewById(R.id.et_count);
        et_price = findViewById(R.id.et_price);
        et_sum = findViewById(R.id.et_sum);

        btn_ok = findViewById(R.id.btn_ok);
        btn_cancel = findViewById(R.id.btn_cancel);
    }

    private void setListeners()
    {
        btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clicked_ok();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clicked_cancel();
            }
        });

        textWatcher = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                try
                {
                    int count = Integer.valueOf(et_count.getText().toString().trim());
                    double price = Double.valueOf(et_price.getText().toString().trim());

                    double sum = count * price;
                    et_sum.setText(String.valueOf(sum));
                } catch (Exception e)
                {
                    Log.e(TAG, "afterTextChanged: Exception in text wathc");
                }
            }
        };

        et_count.addTextChangedListener(textWatcher);
        et_price.addTextChangedListener(textWatcher);
    }


    @Override
    public Model_Product getProduct()
    {
        Model_Product product = new Model_Product();

        Model_Material material = new Model_Material();
        material.setName(et_material_name.getText().toString().trim());
        product.setMaterial(material);

        if(!TextUtils.isEmpty(et_width.getText().toString().trim()))
        {
            product.setWidth(et_width.getText().toString().trim());
        }

        if(!TextUtils.isEmpty(et_height.getText().toString().trim()))
        {
            product.setHeight(et_height.getText().toString().trim());
        }

        if(!TextUtils.isEmpty(et_color.getText().toString().trim()))
        {
            Model_Color color = new Model_Color();
            color.setName(et_color.getText().toString().trim());
            product.setColor(color);
        }


        if(!TextUtils.isEmpty(et_krep.getText().toString().trim()))
        {
            Model_Krep krep = new Model_Krep();
            krep.setName(et_krep.getText().toString().trim());
            product.setKrep(krep);
        }

        if(!TextUtils.isEmpty(et_control.getText().toString().trim()))
        {
            Model_Control control = new Model_Control();
            control.setName(et_control.getText().toString().trim());
            product.setControl(control);
        }


        product.setCount(GlobalHelper.getEtIntegerValue(et_count));
        product.setPrice(GlobalHelper.getEtDoubleValue(et_price));
        product.setSum(GlobalHelper.getEtDoubleValue(et_sum));

        return product;
    }

    @Override
    public void showProductErrors(Model_Product product)
    {
        @DrawableRes
        int greenBox = R.drawable.rounded_white_green_stroke;

        @DrawableRes
        int redBox = R.drawable.rounded_white_red_stroke;

        if (GlobalHelper.validateProductMaterial(product))
        {
            ImageManager.setBackgroundDrawable(et_material_name, greenBox);
        } else
        {
            ImageManager.setBackgroundDrawable(et_material_name, redBox);
        }

        if (GlobalHelper.validateProductColor(product))
        {
            ImageManager.setBackgroundDrawable(et_color, greenBox);
        } else
        {
            ImageManager.setBackgroundDrawable(et_color, redBox);
        }

        if (GlobalHelper.validateProductKrep(product))
        {
            ImageManager.setBackgroundDrawable(et_krep, greenBox);
        } else
        {
            ImageManager.setBackgroundDrawable(et_krep, redBox);
        }

        if (GlobalHelper.validateProductControl(product))
        {
            ImageManager.setBackgroundDrawable(et_control, greenBox);
        } else
        {
            ImageManager.setBackgroundDrawable(et_control, redBox);
        }

        if(product.getCount() < 1)
        {
            ImageManager.setBackgroundDrawable(et_count,redBox);
        }
        else
            {
                ImageManager.setBackgroundDrawable(et_count,greenBox);
            }


        if(product.getPrice() < 1)
        {
            ImageManager.setBackgroundDrawable(et_price,redBox);
        }
        else
            {
                ImageManager.setBackgroundDrawable(et_price,greenBox);
            }

        if(product.getSum() < 1)
        {
            ImageManager.setBackgroundDrawable(et_sum,redBox);
        }
        else
            {
                ImageManager.setBackgroundDrawable(et_sum,greenBox);
            }
    }

    @Override
    public void bindProduct(Model_Product product)
    {
        if (product == null)
        {
            Log.e(TAG, "bindProduct: error product is null");
            return;
        }

        if (product.getMaterial() != null && product.getMaterial().getName() != null)
        {
            et_material_name.setText(product.getMaterial().getName());
        }

        if (!TextUtils.isEmpty(product.getWidth()))
        {
            et_width.setText(product.getWidth());
        }

        if (!TextUtils.isEmpty(product.getHeight()))
        {
            et_height.setText(product.getHeight());
        }

        if (product.getColor() != null && product.getColor().getName() != null)
        {
            et_color.setText(product.getColor().getName());
        }

        if (product.getKrep() != null && product.getKrep().getName() != null)
        {
            et_krep.setText(product.getKrep().getName());
        }

        if (product.getControl() != null && product.getControl().getName() != null)
        {
            et_control.setText(product.getControl().getName());
        }

        if (product.getCount() > 0)
        {
            et_count.setText(String.valueOf(product.getCount()));
        }

        if (product.getPrice() > 0)
        {
            et_price.setText(String.valueOf(product.getPrice()));
        }

        if (product.getSum() > 0)
        {
            et_sum.setText(String.valueOf(product.getSum()));
        }

        tv_header.setText("Редактирование товара");
        btn_ok.setText("Сохранить");
    }
}
