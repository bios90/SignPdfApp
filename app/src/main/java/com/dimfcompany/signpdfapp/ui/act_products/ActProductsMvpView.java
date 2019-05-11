package com.dimfcompany.signpdfapp.ui.act_products;

import androidx.core.content.res.ResourcesCompat;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableViewAbstr;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_Product;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.StringManager;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

import java.util.Collections;

public class ActProductsMvpView extends BaseObservableViewAbstr<ActProductsMvp.ViewListener>
        implements ActProductsMvp.MvpView, DragLinearLayout.OnViewSwapListener
{
    private static final String TAG = "ActProductsMvpView";

    private final LayoutInflater layoutInflater;

    RelativeLayout la_add_product;
    DragLinearLayout drag_la;

    TextView tv_all_product_count;
    TextView tv_all_product_sum;
    TextView tv_back_top;
    RelativeLayout la_back;

    final StringManager stringManager;


    public ActProductsMvpView(LayoutInflater layoutInflater, ViewGroup parent, StringManager stringManager)
    {
        this.layoutInflater = layoutInflater;
        this.stringManager = stringManager;
        setRootView(layoutInflater.inflate(R.layout.act_products, parent, false));
        initViews();
        setListeners();
    }

    private void initViews()
    {
        la_add_product = findViewById(R.id.la_add_product);
        drag_la = findViewById(R.id.drag_la);
        tv_all_product_count = findViewById(R.id.tv_all_product_count);
        tv_all_product_sum = findViewById(R.id.tv_all_product_sum);
        tv_back_top = findViewById(R.id.tv_back_top);
        la_back = findViewById(R.id.la_back);

        drag_la.setOnViewSwapListener(this);
    }

    private void setListeners()
    {
        la_add_product.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedAddProduct();
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

        tv_back_top.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedBack();
            }
        });
    }

    @Override
    public void displayProducts()
    {
        drag_la.removeAllViews();
        int colorBlue = ResourcesCompat.getColor(layoutInflater.getContext().getResources(), R.color.blue, null);
        int colorGreen = ResourcesCompat.getColor(layoutInflater.getContext().getResources(), R.color.green, null);
        int colorGray3 = ResourcesCompat.getColor(layoutInflater.getContext().getResources(), R.color.gray3, null);

        Model_Document document = getListener().getDocument();

        for (final Model_Product product : document.getListOfProducts())
        {
            View product_view = layoutInflater.inflate(R.layout.item_product, drag_la, false);

            TextView tv_material = product_view.findViewById(R.id.tv_material);
            TextView tv_position = product_view.findViewById(R.id.tv_position);
            TextView tv_info = product_view.findViewById(R.id.tv_info);
            TextView tv_delete = product_view.findViewById(R.id.tv_delete);
            RelativeLayout rootView = product_view.findViewById(R.id.root_view);

            tv_material.setText(product.getMaterial().getName());
            tv_position.setText(String.valueOf(document.getListOfProducts().indexOf(product) + 1));

            String width = product.getWidth();
            String height = product.getHeight();
            String count = StringManager.formatNum(product.getCount(),false);
            String sum = StringManager.formatNum(product.getSum(),false);

            SpannableString info = new SpannableString("Ш - " + width + " | В - " + height + " | Кол-во - ");
            SpannableString spanCount = stringManager.getSemiSpannable(count, colorBlue);
            SpannableString infoPreSum = stringManager.getRegSpannable(" | Сумма - ", colorGray3);
            SpannableString spanSum = stringManager.getSemiSpannable(sum, colorGreen);


            CharSequence finalSpan = TextUtils.concat(info, spanCount, infoPreSum, spanSum);


            tv_info.setText(finalSpan);

            tv_delete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    getListener().clickedDeleteProduct(product);
                }
            });

            product_view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    getListener().clickedEditProduct(product);
                }
            });

            product_view.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    getListener().clickedLongProduct(product);
                    return true;
                }
            });

            drag_la.addDragView(product_view, tv_position);
        }
    }

    @Override
    public void onSwap(View firstView, int firstPosition, View secondView, int secondPosition)
    {
        Collections.swap(getListener().getDocument().getListOfProducts(), firstPosition, secondPosition);
        updatePositionNums(firstPosition, secondPosition);
    }

    private void updatePositionNums(int firstPosition, int secondPosition)
    {
        View view1 = drag_la.getChildAt(firstPosition);
        TextView tv_position1 = view1.findViewById(R.id.tv_position);

        View view2 = drag_la.getChildAt(secondPosition);
        TextView tv_position2 = view2.findViewById(R.id.tv_position);

        tv_position1.setText(String.valueOf(secondPosition + 1));
        tv_position2.setText(String.valueOf(firstPosition + 1));
    }

    @Override
    public void updateBottomInfo()
    {
        int count = getListener().getDocument().getListOfProducts().size();
        double sum = GlobalHelper.countSum(getListener().getDocument());
        String sumStr = StringManager.formatNum(sum,false);

        tv_all_product_count.setText(String.valueOf(count));
        tv_all_product_sum.setText(sumStr);
    }
}
