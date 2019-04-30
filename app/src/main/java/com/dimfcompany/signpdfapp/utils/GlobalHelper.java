package com.dimfcompany.signpdfapp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_Product;

import java.io.File;
import java.util.Date;
import java.util.Random;

public class GlobalHelper
{
    private static final String TAG = "GlobalHelper";

    public static final String FORMAT_FULL_MONTH = "d MMMM yyyy";


    private final Context context;

    public GlobalHelper(Context context)
    {
        this.context = context;
    }

    public static String getCurrentTimeFullString()
    {
        return getDateString(new Date(), FORMAT_FULL_MONTH);
    }

    public static String getDateString(Date date, String format)
    {
        return DateFormat.format(format, date).toString();
    }

    public static String getDateString(long timeInMillis, String format)
    {
        return DateFormat.format(format, new Date(timeInMillis)).toString();
    }


    public static int getRandomBeetwen(int min, int max)
    {
        final int random = new Random().nextInt((max - min) + 1) + min;
        return random;
    }

    public static String getCityOfDocument(Model_Document document)
    {
        String city = null;
        switch (document.getCity())
        {
            case 0:
                city = "Москва";
                break;
            case 1:
                city = "СПБ";
                break;
            case 2:
                city = "Сочи";
                break;
            case 3:
                city = "Самара";
                break;
        }

        return city;
    }

    public static void openPdf(AppCompatActivity activity, File file) throws Exception
    {
        Intent intent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            Uri uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", file);
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivity(intent);
        } else
        {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
            intent = Intent.createChooser(intent, "Открыть отчет");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
    }

    public static void shareFile(AppCompatActivity activity, File file) throws Exception
    {
        Intent intent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            Uri uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", file);
            intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setType("application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivity(Intent.createChooser(intent, "Отправить отчет"));

        } else
        {
            Uri uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", file);
            intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setType("application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivity(Intent.createChooser(intent, "Отправить отчет"));
        }
    }

    public static boolean validateProduct(Model_Product product)
    {
        if (!validateProductMaterial(product))
        {
            return false;
        }

        if (!validateProductColor(product))
        {
            return false;
        }

        if (!validateProductKrep(product))
        {
            return false;
        }

        if (!validateProductControl(product))
        {
            return false;
        }

        if (product.getCount() <= 0)
        {
            return false;
        }

        if (product.getPrice() <= 0)
        {
            return false;
        }

        if (product.getSum() <= 0)
        {
            return false;
        }

        return true;
    }

    public static boolean validateProductMaterial(Model_Product product)
    {
        if (product.getMaterial() == null)
        {
            return false;
        }

        if (TextUtils.isEmpty(product.getMaterial().getName()))
        {
            return false;
        }
        return true;
    }

    public static boolean validateProductColor(Model_Product product)
    {
        if (product.getColor() == null)
        {
            return false;
        }

        if (TextUtils.isEmpty(product.getColor().getName()))
        {
            return false;
        }

        return true;
    }


    public static boolean validateProductKrep(Model_Product product)
    {
        if (product.getKrep() == null)
        {
            return false;
        }

        if (TextUtils.isEmpty(product.getKrep().getName()))
        {
            return false;
        }

        return true;
    }

    public static boolean validateProductControl(Model_Product product)
    {
        if (product.getControl() == null)
        {
            return false;
        }

        if (TextUtils.isEmpty(product.getControl().getName()))
        {
            return false;
        }

        return true;
    }

    public static double countSum(Model_Document document)
    {
        double sum = 0;
        for (Model_Product product : document.getListOfProducts())
        {
            sum += product.getSum();
        }

        return sum;
    }


    public static double countItogoSum(Model_Document document)
    {
        double sum = countSum(document);

        sum += document.getMontage();
        sum += document.getDelivery();
        sum -= document.getSale();

        return sum;
    }


    public static double countItogoSum(Model_Document document, double montage, double delivery, double sale)
    {
        double sum = countSum(document);

        sum += montage;
        sum += delivery;
        sum -= sale;

        return sum;
    }

    public static double countItogoSumWithPercent(Model_Document document, double montage, double delivery, int percent)
    {
        double sum = countSum(document);

        sum += montage;
        sum += delivery;
        sum = sum - ((sum / 100.0f)*percent);

        return sum;
    }

    public static void invalidateRecursive(ViewGroup layout)
    {
        int count = layout.getChildCount();
        View child;
        for (int i = 0; i < count; i++)
        {
            child = layout.getChildAt(i);
            if (child instanceof ViewGroup)
                invalidateRecursive((ViewGroup) child);
            else
                child.invalidate();
        }
    }

    public float dpFromPx(final float px)
    {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public float pxFromDp(final float dp)
    {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static Integer getEtIntegerValue(EditText editText)
    {
        try
        {
            String str = editText.getText().toString().trim();
            str = str.replaceAll("[^\\d.]", "");
            return Integer.valueOf(str);
        } catch (Exception e)
        {
            return 0;
        }
    }

    public static Integer getEtIntegerPercent(EditText editText)
    {
        try
        {
            String str = editText.getText().toString().trim();
            str = str.replaceAll("[^\\d.]", "");
            int percent = Integer.valueOf(str);
            if(percent > 100)
            {
                percent = 100;
            }
            return percent;
        } catch (Exception e)
        {
            return 0;
        }
    }

    public static Float getEtFloatValue(EditText editText)
    {
        try
        {
            String str = editText.getText().toString().trim();
            str = str.replaceAll("[^\\d.]", "");
            return Float.valueOf(str);
        } catch (Exception e)
        {
            return 0f;
        }
    }

    public static Double getEtDoubleValue(EditText editText)
    {
        try
        {
            String str = editText.getText().toString().trim();
            str = str.replaceAll("[^\\d.]", "");
            return Double.valueOf(str);
        } catch (Exception e)
        {
            return 0d;
        }
    }

    public static int getPercentFromTwoNums(double sum, double sale)
    {
        if(sum == 0)
        {
            return 0;
        }

        Log.e(TAG, "Sum is "+sum+" | SaleRub is "+sale+" percent is "+(int)((sale * 100.0f) / sum) );
        return (int)((sale * 100.0f) / sum);
    }

    public static double getSumAfterPercentCut(double sum, int sale)
    {
        return (sum / 100) * sale;
    }
}
