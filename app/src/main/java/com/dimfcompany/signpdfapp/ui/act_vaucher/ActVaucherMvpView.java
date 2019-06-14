package com.dimfcompany.signpdfapp.ui.act_vaucher;

import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.adapters.AdapterSpinnerUniversal;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableViewAbstr;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_Price_Element;
import com.dimfcompany.signpdfapp.models.Model_Product;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.StringManager;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

import java.util.Collections;
import java.util.List;

public class ActVaucherMvpView extends BaseObservableViewAbstr<ActVaucherMvp.ViewListener>
        implements ActVaucherMvp.MvpView
{
    private static final String TAG = "ActVaucherMvpView";

    LayoutInflater layoutInflater;
    AdapterSpinnerUniversal<String> adapterHeaders;

    boolean spinner_init = true;

    TextView tv_back_top;
    Spinner spinner_headers;
    EditText et_header;
    TextView tv_clear;
    DragLinearLayout drag_la;
    TextView tv_elements_count, tv_elements_sum;
    RelativeLayout la_back, la_add_element;

    public ActVaucherMvpView(LayoutInflater layoutInflater, ViewGroup parent)
    {
        this.layoutInflater = layoutInflater;
        setRootView(layoutInflater.inflate(R.layout.act_vaucher,parent,false));
        initViews();
        setListeners();
    }



    private void initViews()
    {
        tv_back_top = findViewById(R.id.tv_back_top);
        spinner_headers = findViewById(R.id.spinner_headers);
        et_header = findViewById(R.id.et_header);
        tv_clear = findViewById(R.id.tv_clear);
        drag_la = findViewById(R.id.drag_la);
        tv_elements_count = findViewById(R.id.tv_elements_count);
        tv_elements_sum = findViewById(R.id.tv_elements_sum);
        la_back = findViewById(R.id.la_back);
        la_add_element = findViewById(R.id.la_add_element);
    }

    private void setListeners()
    {
        la_add_element.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedAddElement();
            }
        });

        tv_back_top.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedBack();
            }
        });

        la_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedBack();
            }
        });

        spinner_headers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(spinner_init)
                {
                    spinner_init = false;
                    return;
                }

                String text = adapterHeaders.getItem(position);
                et_header.setText(text);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        tv_clear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                et_header.setText("");
            }
        });

        drag_la.setOnViewSwapListener(new DragLinearLayout.OnViewSwapListener()
        {
            @Override
            public void onSwap(View firstView, int firstPosition, View secondView, int secondPosition)
            {
                Collections.swap(getListener().getDocument().getVaucher().getPrice_elements(), firstPosition, secondPosition);
                GlobalHelper.updatePositionNums(firstPosition, secondPosition,drag_la);
            }
        });
    }

    @Override
    public void updateBottomInfo()
    {
        int count = getListener().getDocument().getVaucher().getPrice_elements().size();
        double sum = GlobalHelper.countVaucherElementsSum(getListener().getDocument());
        String sumStr = StringManager.formatNum(sum,false);

        tv_elements_count.setText(String.valueOf(count));
        tv_elements_sum.setText(sumStr);
    }

    @Override
    public void bindPriceElements()
    {
        drag_la.removeAllViews();

        Model_Document document = getListener().getDocument();

        for(final Model_Price_Element price_element : document.getVaucher().getPrice_elements())
        {
            View price_view = layoutInflater.inflate(R.layout.item_price_element,drag_la,false);

            TextView tv_position = price_view.findViewById(R.id.tv_position);
            TextView tv_delete = price_view.findViewById(R.id.tv_delete);
            TextView tv_text = price_view.findViewById(R.id.tv_text);
            TextView tv_price = price_view.findViewById(R.id.tv_price);
            RelativeLayout rootView = price_view.findViewById(R.id.root_view);

            tv_text.setText(price_element.getText());
            tv_price.setText(StringManager.formatNum(price_element.getPrice(),false));
            tv_position.setText(String.valueOf(document.getVaucher().getPrice_elements().indexOf(price_element) + 1));

            tv_delete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    getListener().clickedDeleteElement(price_element);
                }
            });

            price_view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    getListener().clickedCardElement(price_element);
                }
            });

            price_view.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    getListener().clickedLongElement(price_element);
                    return true;
                }
            });

            drag_la.addDragView(price_view,tv_position);
        }

        updateBottomInfo();
    }

    @Override
    public String getHeaderString()
    {
        return StringManager.castToNullIfEmpty(et_header.getText().toString().trim());
    }

    @Override
    public void bindHeaderSpinner(List<String> headers)
    {
        adapterHeaders = new AdapterSpinnerUniversal<String>(getRootView().getContext(), headers);
        spinner_headers.setAdapter(adapterHeaders);
    }

    @Override
    public void bindHeader(String header)
    {
        et_header.setText(header);
    }
}
