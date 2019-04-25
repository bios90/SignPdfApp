package com.dimfcompany.signpdfapp.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;

import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_Product;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GlobalHelper
{
    public static final String FORMAT_FULL_MONTH = "d MMMM yyyy";

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
        if(!validateProductMaterial(product))
        {
            return false;
        }

        if(!validateProductColor(product))
        {
            return false;
        }

        if(!validateProductKrep(product))
        {
            return false;
        }

        if(!validateProductControl(product))
        {
            return false;
        }

        if(product.getCount() <= 0)
        {
            return false;
        }

        if(product.getPrice() < 0)
        {
            return false;
        }

        if(product.getSum() < 0)
        {
            return false;
        }

        return true;
    }

    public static boolean validateProductMaterial(Model_Product product)
    {
        if(product.getMaterial() == null)
        {
            return false;
        }

        if(TextUtils.isEmpty(product.getMaterial().getName()))
        {
            return false;
        }
        return true;
    }

    public static boolean validateProductColor(Model_Product product)
    {
        if(product.getColor() == null)
        {
            return false;
        }

        if(TextUtils.isEmpty(product.getColor().getName()))
        {
            return false;
        }

        return true;
    }


    public static boolean validateProductKrep(Model_Product product)
    {
        if(product.getKrep() == null)
        {
            return false;
        }

        if(TextUtils.isEmpty(product.getKrep().getName()))
        {
            return false;
        }

        return true;
    }

    public static boolean validateProductControl(Model_Product product)
    {
        if(product.getControl() == null)
        {
            return false;
        }

        if(TextUtils.isEmpty(product.getControl().getName()))
        {
            return false;
        }

        return true;
    }

    public static double countSum(Model_Document document)
    {
        double sum = 0;
        for(Model_Product product : document.getListOfProducts())
        {
            sum+=product.getSum();
        }

        return sum;
    }
}
