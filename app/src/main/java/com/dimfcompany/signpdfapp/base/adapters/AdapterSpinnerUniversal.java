package com.dimfcompany.signpdfapp.base.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dimfcompany.signpdfapp.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterSpinnerUniversal<T> extends ArrayAdapter<T>
{
    private static final String TAG = "AdapterSpinnerWorkerRol";

    private List<T> objects = new ArrayList<>();
    private Context context;

    public AdapterSpinnerUniversal(@NonNull Context context, @NonNull List<T> objects)
    {
        super(context, R.layout.item_spinner_value, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public int getCount()
    {
        return objects.size();
    }

    @Nullable
    @Override
    public T getItem(int position)
    {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public List<T> getObjects()
    {
        return objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.item_spinner_choosed, parent, false);
        Object object = objects.get(position);
        textView.setText(getTextFromObject(object));
        return textView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.item_spinner_value, parent, false);
        Object object = objects.get(position);
        textView.setText(getTextFromObject(object));
        return textView;
    }

    private String getTextFromObject(Object object)
    {
        String text = "Cant Cast";
        if (object instanceof String)
        {
            text = (String) object;
        }

        return text;
    }
}
