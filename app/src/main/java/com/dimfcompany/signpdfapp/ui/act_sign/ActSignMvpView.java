package com.dimfcompany.signpdfapp.ui.act_sign;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableViewAbstr;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.utils.FileManager;
import com.dimfcompany.signpdfapp.utils.ImageManager;

import org.w3c.dom.Text;

import java.io.File;

public class ActSignMvpView extends BaseObservableViewAbstr<ActSignMvp.ViewListener>
    implements ActSignMvp.MvpView
{
    private static final String TAG = "ActSignMvpView";

    private final FileManager fileManager;

    ImageView img_for_sign;
    RadioGroup rg_city;
    EditText et_fio,et_adress,et_phone;
    String lastBitmapFileName;
    RelativeLayout la_create_pdf;
    RelativeLayout la_materials;

    public ActSignMvpView(LayoutInflater layoutInflater, ViewGroup parent, FileManager fileManager)
    {
        setRootView(layoutInflater.inflate(R.layout.act_sign,parent,false));
        this.fileManager = fileManager;
        initViews();
        setListeners();
    }

    private void initViews()
    {
        img_for_sign = findViewById(R.id.img_for_sign);
        rg_city = findViewById(R.id.rg_city);
        et_fio = findViewById(R.id.et_fio);
        et_adress = findViewById(R.id.et_adress);
        et_phone = findViewById(R.id.et_phone);
        la_create_pdf = findViewById(R.id.la_create_pdf);
        la_materials = findViewById(R.id.la_materials);
    }


    private void setListeners()
    {
        img_for_sign.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedSignaturePad();
            }
        });

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
    }


    @Override
    public void bindModelDocument(Model_Document document)
    {
        if(document.getCity() != null)
        {
            RadioButton rb = (RadioButton) rg_city.getChildAt(document.getCity());
            rb.setChecked(true);
        }

        if(document.getFio() != null)
        {
            et_fio.setText(document.getFio());
        }

        if(document.getAdress() != null)
        {
            et_adress.setText(document.getAdress());
        }

        if(document.getPhone() != null)
        {
            et_phone.setText(document.getPhone());
        }

        if(document.getSignature_file_name() != null)
        {
            bindSignatureFile(document.getSignature_file_name());
        }
    }

    @Override
    public void bindSignatureFile(String filename)
    {
        File file = fileManager.getFileFromTemp(filename,null);
        if(file != null)
        {
            ImageManager.setFileToImageView(file, img_for_sign);
            lastBitmapFileName = filename;
        }
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

        tv_materials_btn.setText("Материалы ("+count+")");
    }

    @Override
    public String getFio()
    {
        return et_fio.getText().toString().trim();
    }

    @Override
    public String getAdress()
    {
        return et_adress.getText().toString().trim();
    }

    @Override
    public String getPhone()
    {
        return et_phone.getText().toString().trim();
    }

    @Override
    public String getCurrentFileName()
    {
        return lastBitmapFileName;
    }
}
