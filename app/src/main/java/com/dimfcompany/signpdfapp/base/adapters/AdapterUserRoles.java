package com.dimfcompany.signpdfapp.base.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.dimfcompany.signpdfapp.R;

public class AdapterUserRoles extends ArrayAdapter<Integer>
{
    private Context context;

    public AdapterUserRoles(@NonNull Context context)
    {
        super(context, R.layout.item_spinner_value);
        this.context = context;
    }

    @Override
    public int getCount()
    {
        return 3;
    }

    @Nullable
    @Override
    public Integer getItem(int position)
    {
        switch (position)
        {
            case 0:
                return 1;

            case 1:
                return 7;

            case 2:
                return 999;

            default:
                return 1;
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        return getItemTextView(position, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        return getItemTextView(position, parent);
    }

    private TextView getItemTextView(int position, ViewGroup parent)
    {
        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.item_spinner_choosed, parent, false);
        textView.setText(getStringFromPosition(position));
        textView.setTextColor(getTextColor(position));
        return textView;
    }

    private String getStringFromPosition(int position)
    {
        switch (position)
        {
            case 0:
                return "Пользователь";
            case 1:
                return "Администратор";
            case 2:
                return "Заблокирован";

            default:
                throw new RuntimeException("Error got wrong spinner position");
        }
    }

    private int getTextColor(int position)
    {
        switch (position)
        {
            case 0:
                return ResourcesCompat.getColor(context.getResources(), R.color.blue, null);
            case 1:
                return ResourcesCompat.getColor(context.getResources(), R.color.green, null);
            case 2:
                return ResourcesCompat.getColor(context.getResources(), R.color.redBase, null);

            default:
                throw new RuntimeException("Error got wrong spinner position");
        }
    }
}
