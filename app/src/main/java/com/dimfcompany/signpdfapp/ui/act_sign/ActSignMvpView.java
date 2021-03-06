package com.dimfcompany.signpdfapp.ui.act_sign;

import android.graphics.Bitmap;

import androidx.core.content.res.ResourcesCompat;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableViewAbstr;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.utils.FileManager;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.ImageManager;
import com.dimfcompany.signpdfapp.utils.MessagesManager;
import com.dimfcompany.signpdfapp.utils.StringManager;
import com.dimfcompany.signpdfapp.utils.custom_classes.CustomEditTextNullFocusedChanged;
import com.dimfcompany.signpdfapp.utils.custom_classes.CustomNullTextWatcher;
import com.github.gcacace.signaturepad.views.SignaturePad;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ActSignMvpView extends BaseObservableViewAbstr<ActSignMvp.ViewListener>
        implements ActSignMvp.MvpView
{
    private static final String TAG = "ActSignMvpView";

    private final FileManager fileManager;
    private final GlobalHelper globalHelper;
    private final LayoutInflater layoutInflater;
    private final MessagesManager messagesManager;

    RadioGroup rg_city;
    EditText et_fio, et_adress, et_phone;
    EditText et_sum, et_montage, et_delivery, et_sale_rub, et_sale_percent, et_prepay, et_prepay_percent, et_postpay, et_order_form, et_dop_info;
    TextView tv_itogo_sum;
    String lastBitmapFileName;
    RelativeLayout la_create_pdf;
    RelativeLayout la_materials;
    RelativeLayout la_vaucher;
    RelativeLayout la_pre_show;
    LinearLayout la_for_signature;

    TextView tv_city_arrow;
    ExpandableLayout exp_cities;

    TextView tv_clear;
    TextView tv_lock;

    boolean sign_locked = true;

    SignaturePad signature_pad;

    TextWatcher percentWatcher;
    TextWatcher priceWatcher;
    TextWatcher prepayPercentWatcher;

    boolean signaturePadSizeSetted = false;

    public ActSignMvpView(LayoutInflater layoutInflater, ViewGroup parent, FileManager fileManager, GlobalHelper globalHelper, MessagesManager messagesManager)
    {
        this.messagesManager = messagesManager;
        setRootView(layoutInflater.inflate(R.layout.act_sign, parent, false));
        this.fileManager = fileManager;
        this.globalHelper = globalHelper;
        this.layoutInflater = layoutInflater;
        initViews();
        setListeners();
        setPriceListeners();
    }


    private void initViews()
    {

        rg_city = findViewById(R.id.rg_city);
        et_fio = findViewById(R.id.et_fio);
        et_adress = findViewById(R.id.et_adress);
        et_phone = findViewById(R.id.et_phone);
        la_create_pdf = findViewById(R.id.la_create_pdf);
        la_materials = findViewById(R.id.la_materials);
        la_pre_show = findViewById(R.id.la_pre_show);
        la_for_signature = findViewById(R.id.la_for_signature);
        la_vaucher = findViewById(R.id.la_vaucher);

        et_sum = findViewById(R.id.et_sum);
        et_montage = findViewById(R.id.et_montage);
        et_delivery = findViewById(R.id.et_delivery);
        et_sale_rub = findViewById(R.id.et_sale_rub);
        et_sale_percent = findViewById(R.id.et_sale_percent);
        et_prepay = findViewById(R.id.et_prepay);
        et_postpay = findViewById(R.id.et_postpay);
        et_order_form = findViewById(R.id.et_order_form);
        et_dop_info = findViewById(R.id.et_dop_info);
        et_prepay_percent = findViewById(R.id.et_prepay_percent);

        tv_clear = findViewById(R.id.tv_clear);
        tv_lock = findViewById(R.id.tv_lock);
        tv_itogo_sum = findViewById(R.id.tv_itogo_sum);

        signature_pad = findViewById(R.id.signature_pad);
        signature_pad.setEnabled(false);

        tv_city_arrow = findViewById(R.id.tv_city_arrow);
        exp_cities = findViewById(R.id.exp_cities);
    }


    private void setListeners()
    {
        la_create_pdf.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedCreatePDf();
            }
        });

        la_materials.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedMaterials();
            }
        });

        tv_lock.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                makeLockToggle();
                return false;
            }
        });

        tv_clear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signature_pad.clear();
            }
        });

        tv_city_arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                exp_cities.toggle();
            }
        });

        la_pre_show.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedPreShow();
            }
        });

        la_vaucher.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedVaucher();
            }
        });
    }

    private void makeLockToggle()
    {
        String ic_lock_open = layoutInflater.getContext().getResources().getString(R.string.ic_lock_open);
        String ic_lock_close = layoutInflater.getContext().getResources().getString(R.string.ic_lock_close);

        int colorRed = ResourcesCompat.getColor(layoutInflater.getContext().getResources(), R.color.sel_red, null);
        int colorGreen = ResourcesCompat.getColor(layoutInflater.getContext().getResources(), R.color.sel_green, null);

        if (sign_locked)
        {
            tv_lock.setText(ic_lock_open);
            tv_lock.setTextColor(colorGreen);
            signature_pad.setEnabled(true);
            tv_clear.setEnabled(true);
        } else
        {
            tv_lock.setText(ic_lock_close);
            tv_lock.setTextColor(colorRed);
            signature_pad.setEnabled(false);
            tv_clear.setEnabled(false);
        }

        sign_locked = !sign_locked;
        messagesManager.vibrate();
    }


    @Override
    public void setSignatureSizes()
    {
        signature_pad.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener()
                {
                    @Override
                    public void onGlobalLayout()
                    {
                        if(!signaturePadSizeSetted)
                        {
                            int width = signature_pad.getWidth();
                            int height = (int) (width * 0.4);
                            int laHeight = (int) (height + globalHelper.pxFromDp(20f));

                            ViewGroup.LayoutParams layoutParams = signature_pad.getLayoutParams();
                            layoutParams.height = height;
                            signature_pad.setLayoutParams(layoutParams);

                            LinearLayout la_for_signature = getRootView().findViewById(R.id.la_for_signature);
                            RelativeLayout la_for_signature_rel = findViewById(R.id.la_for_signature_rel);

                            ViewGroup.LayoutParams linearParams = la_for_signature.getLayoutParams();
                            linearParams.height = laHeight;
                            la_for_signature.setLayoutParams(linearParams);

                            ViewGroup.LayoutParams relParams = la_for_signature_rel.getLayoutParams();
                            relParams.height = laHeight;
                            la_for_signature_rel.setLayoutParams(relParams);

                            GlobalHelper.invalidateRecursive(la_for_signature_rel);
                            signature_pad.invalidate();
                            if(signature_pad.getHeight() > 0)
                            {
                                signaturePadSizeSetted = true;
                                signature_pad.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                if(getListener().getSignatureFile() != null)
                                {
                                    signature_pad.setSignatureBitmap(ImageManager.getBitmapFromFile(getListener().getSignatureFile()));
                                }
                            }

                        }
                    }
                });
    }


    @Override
    public void bindModelDocument(final Model_Document document)
    {
        if (document.getCity() != null)
        {
            RadioButton rb = (RadioButton) rg_city.getChildAt(document.getCity());
            rb.setChecked(true);
        }

        if (document.getFio() != null)
        {
            et_fio.setText(document.getFio());
        }

        if (document.getAdress() != null)
        {
            et_adress.setText(document.getAdress());
        }

        if (document.getPhone() != null)
        {
            et_phone.setText(document.getPhone());
        }

        if (document.getSignature_file_name() != null)
        {
            bindSignatureFile(document.getSignature_file_name());
        }

        double sum = GlobalHelper.countSum(document);

        if (sum > 0)
        {
            et_sum.setText(StringManager.formatNum(sum, false));
        }

        if (document.getMontage() > 0)
        {
            et_montage.setText(StringManager.formatNum(document.getMontage(), false));
        }

        if (document.getDelivery() > 0)
        {
            et_delivery.setText(StringManager.formatNum(document.getDelivery(), false));
        }

        if (document.getSale() > 0)
        {
            et_sale_rub.setText(StringManager.formatNum(document.getSale(), false));
        }

        if (document.getPrepay() > 0)
        {
            et_prepay.setText(StringManager.formatNum(document.getPrepay(), false));
        }

        if (document.getOrder_form() != null)
        {
            et_order_form.setText(document.getOrder_form());
        }

        if (document.getDop_info() != null)
        {
            et_dop_info.setText(document.getDop_info());
        }

        removeTextWatchers();
        countNormal();
        addTextWatchers();

        updateMaterialButton();
        updateVaucherButton();
    }

    private void addTextWatchers()
    {
        et_montage.addTextChangedListener(priceWatcher);
        et_delivery.addTextChangedListener(priceWatcher);
        et_sale_rub.addTextChangedListener(priceWatcher);
        et_sale_percent.addTextChangedListener(percentWatcher);
        et_prepay.addTextChangedListener(priceWatcher);
        et_prepay_percent.addTextChangedListener(prepayPercentWatcher);
    }

    private void removeTextWatchers()
    {
        et_montage.removeTextChangedListener(priceWatcher);
        et_delivery.removeTextChangedListener(priceWatcher);
        et_sale_rub.removeTextChangedListener(priceWatcher);
        et_sale_percent.removeTextChangedListener(percentWatcher);
        et_prepay.removeTextChangedListener(priceWatcher);
        et_prepay_percent.removeTextChangedListener(prepayPercentWatcher);
    }


    private void setPriceListeners()
    {
        percentWatcher = new TextWatcher()
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
                removeTextWatchers();
                countPercent();
                addTextWatchers();
            }
        };

        priceWatcher = new TextWatcher()
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
                removeTextWatchers();
                countNormal();
                addTextWatchers();
            }
        };

        prepayPercentWatcher = new TextWatcher()
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
                removeTextWatchers();
                countPrepayPercent();
                addTextWatchers();
            }
        };

        et_montage.addTextChangedListener(priceWatcher);
        et_delivery.addTextChangedListener(priceWatcher);
        et_sale_rub.addTextChangedListener(priceWatcher);
        et_prepay.addTextChangedListener(priceWatcher);
        et_sale_percent.addTextChangedListener(percentWatcher);
        et_prepay_percent.addTextChangedListener(prepayPercentWatcher);
    }


    @Override
    public void bindSignatureFile(String filename)
    {
        setSignatureSizes();
    }

    @Override
    public Integer getCity()
    {
        Integer city = null;
        for (int a = 0; a < rg_city.getChildCount(); a++)
        {
            RadioButton radioButton = (RadioButton) rg_city.getChildAt(a);
            if (radioButton.isChecked())
            {
                city = a;
            }
        }

        return city;
    }

    @Override
    public void updateMaterialButton()
    {
        TextView tv_materials_btn = getRootView().findViewById(R.id.tv_materials_btn);
        int count = getListener().getDocument().getListOfProducts().size();

        tv_materials_btn.setText("Товары (" + count + ")");
    }

    @Override
    public void updateVaucherButton()
    {
        int count = 0;

        TextView tv_vaucher_btn = getRootView().findViewById(R.id.tv_vaucher_btn);
        if(getListener().getDocument().getVaucher() != null)
        {
            count = getListener().getDocument().getVaucher().getPrice_elements().size();
        }

        tv_vaucher_btn.setText("Ваучер (" + count + ")");
    }



    @Override
    public String getFio()
    {
        if(TextUtils.isEmpty(et_fio.getText().toString().trim()))
        {
            return null;
        }
        return et_fio.getText().toString().trim();
    }

    @Override
    public String getAdress()
    {
        if(TextUtils.isEmpty(et_adress.getText().toString().trim()))
        {
            return null;
        }

        return et_adress.getText().toString().trim();
    }

    @Override
    public String getPhone()
    {
        if(TextUtils.isEmpty(et_phone.getText().toString().trim()))
        {
            return null;
        }
        return et_phone.getText().toString().trim();
    }

    @Override
    public double getMontage()
    {
        return GlobalHelper.getEtDoubleValue(et_montage);
    }

    @Override
    public double getDelivery()
    {
        return GlobalHelper.getEtDoubleValue(et_delivery);
    }

    @Override
    public double getSale()
    {
        Log.e(TAG, "getSale: Sale is " + GlobalHelper.getEtDoubleValue(et_sale_rub));
        return GlobalHelper.getEtDoubleValue(et_sale_rub);
    }

    @Override
    public double getPrePay()
    {
        return GlobalHelper.getEtDoubleValue(et_prepay);
    }

    @Override
    public String getOrderForm()
    {
        if(TextUtils.isEmpty(et_order_form.getText().toString().trim()))
        {
            return null;
        }

        return et_order_form.getText().toString().trim();
    }

    @Override
    public String getDopInfo()
    {
        if(TextUtils.isEmpty(et_dop_info.getText().toString().trim()))
        {
            return null;
        }
        return et_dop_info.getText().toString().trim();
    }

    @Override
    public String getCurrentFileName()
    {
        if (signature_pad.isEmpty())
        {
            return null;
        }

        Bitmap bitmap = signature_pad.getSignatureBitmap();

        Log.e(TAG, "getCurrentFileName: Bitmap not Null!!");
        File file = fileManager.saveBitmapToFile(bitmap);
        return FileManager.getFileName(file);
    }

    private void countPercent()
    {
        double sum = GlobalHelper.countSum(getListener().getDocument());

        double montage = getMontage();
        double delivery = getDelivery();

        int percent = GlobalHelper.getEtIntegerPercent(et_sale_percent);

        double itogoSum = GlobalHelper.countItogoSumWithPercent(getListener().getDocument(), montage, delivery, percent);
        double sale_rub = sum + montage + delivery - itogoSum;

        if(sale_rub > 0)
        {
            et_sale_rub.setText(StringManager.formatNum(sale_rub, false));
        }
        else
            {
                et_sale_rub.setText("");
            }

        if(sum > 0)
        {
            et_sum.setText(StringManager.formatNum(sum, false));
        }
        else
            {
                et_sum.setText("");
            }

        tv_itogo_sum.setText(StringManager.formatNum(itogoSum, false) + " р");

        double prepay = getPrePay();
        double postPay = itogoSum - prepay;


        if(postPay > 0)
        {
            et_postpay.setText(StringManager.formatNum(postPay, false));
        }
        else
            {
                et_postpay.setText("");
            }
    }

    private void countNormal()
    {
        double sum = GlobalHelper.countSum(getListener().getDocument());

        double montage = getMontage();
        double delivery = getDelivery();
        double sale_rub = getSale();

        double itogoSum = GlobalHelper.countItogoSum(getListener().getDocument(), montage, delivery, sale_rub);

        double salePercent = GlobalHelper.getPercentFromTwoNums(sum, sale_rub);

        if(salePercent > 0)
        {
            et_sale_percent.setText(StringManager.formatNum(salePercent, false));
        }
        else
            {
                et_sale_percent.setText("");
            }

        if(sum > 0)
        {
            et_sum.setText(StringManager.formatNum(sum, false));
        }
        else
            {
                et_sum.setText("");
            }

        tv_itogo_sum.setText(StringManager.formatNum(itogoSum, false) + " р");

        double prepay = getPrePay();
        double postPay = itogoSum - prepay;
        double prepayPercent = GlobalHelper.getPercentFromTwoNums(itogoSum, prepay);

        if(prepayPercent > 0)
        {
            et_prepay_percent.setText(StringManager.formatNum(prepayPercent, false));
        }
        else
            {
                et_prepay_percent.setText("");
            }

        if(postPay > 0)
        {
            et_postpay.setText(StringManager.formatNum(postPay, false));
        }
        else
            {
                et_postpay.setText("");
            }
    }

    private void countPrepayPercent()
    {
        int percent = GlobalHelper.getEtIntegerPercent(et_prepay_percent);
        double montage = getMontage();
        double delivery = getDelivery();
        double sale_rub = getSale();

        double itogoSum = GlobalHelper.countItogoSum(getListener().getDocument(), montage, delivery, sale_rub);

        double postpay = GlobalHelper.countSumMinusPercent(itogoSum, percent);
        double prepay = itogoSum - postpay;

        if(postpay > 0)
        {
            et_postpay.setText(StringManager.formatNum(postpay, false));
        }
        else
            {
                et_postpay.setText("");
            }

        if(prepay > 0)
        {
            et_prepay.setText(StringManager.formatNum(prepay, false));
        }
        else
            {
                et_prepay.setText("");
            }
    }


}
