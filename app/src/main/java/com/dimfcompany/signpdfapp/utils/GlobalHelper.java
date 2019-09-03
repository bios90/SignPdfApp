package com.dimfcompany.signpdfapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.dimfcompany.signpdfapp.BuildConfig;
import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.AppClass;
import com.dimfcompany.signpdfapp.models.Model_Color;
import com.dimfcompany.signpdfapp.models.Model_Control;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_Krep;
import com.dimfcompany.signpdfapp.models.Model_Material;
import com.dimfcompany.signpdfapp.models.Model_Price_Element;
import com.dimfcompany.signpdfapp.models.Model_Product;
import com.dimfcompany.signpdfapp.models.Model_User;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

import java.io.File;
import java.util.Date;
import java.util.Random;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class GlobalHelper
{
    private static final String TAG = "GlobalHelper";

    public static final String FORMAT_FULL_MONTH = "d MMMM yyyy";
    public static final String FORMAT_LARAVEL = "yyyy-MM-dd HH:mm:ss";

    public static final String APP_VERSION = BuildConfig.VERSION_NAME;

    private final Context context;

    public GlobalHelper(Context context)
    {
        this.context = context;
    }

    public static boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) AppClass.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getCurrentTimeFullString()
    {
        return getDateString(new Date(), FORMAT_FULL_MONTH);
    }

    public static String getDateString(Date date)
    {
        return DateFormat.format(FORMAT_FULL_MONTH, date).toString();
    }

    public static String getDateString(Date date, String format)
    {
        if (date == null)
        {
            return null;
        }
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

    public static void openPdf(AppCompatActivity activity, File file)
    {
        Intent intent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            Uri uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", file);
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION);
        }
        else
        {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
            intent = Intent.createChooser(intent, "Открыть отчет");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        if (canDeviceOpenFile(intent, activity))
        {
            activity.startActivity(intent);
        }
    }

    public static boolean canDeviceOpenFile(Intent intent, AppCompatActivity activity)
    {
        if (intent.resolveActivity(activity.getPackageManager()) == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static void shareFile(AppCompatActivity activity, File file)
    {
        Intent intent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            Uri uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", file);
            intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setType("application/pdf");
            intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);

        }
        else
        {
            Uri uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", file);
            intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setType("application/pdf");
            intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
        }

        if (canDeviceOpenFile(intent, activity))
        {
            activity.startActivity(Intent.createChooser(intent, "Отправить отчет"));
        }
    }

    public static Uri getUriFromFile(File file)
    {
        return FileProvider.getUriForFile(AppClass.getApp(), AppClass.getApp().getPackageName() + ".provider", file);
    }


    public static void sendToPrint(AppCompatActivity activity, File file)
    {
//        file = ImageManager.getImageFileFromPdf(file);
        Uri uri = getUriFromFile(file);

//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);

        String type = "application/pdf";
        if (!FileManager.isPdf(file))
        {
            type = "image/*";
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType(type);
        intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);


        final String appPackageName = "ru.a402d.rawbtprinter";
        PackageManager pm = activity.getPackageManager();

        PackageInfo pi = null;
        if (pm != null)
        {
            try
            {
                pi = pm.getPackageInfo(appPackageName, 0);
            }
            catch (PackageManager.NameNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        if (pi == null)
        {
            try
            {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            }
            catch (android.content.ActivityNotFoundException anfe)
            {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
        else
        {
            intent.setPackage(appPackageName);
            activity.startActivity(intent);
        }
    }

    public static boolean validateProduct(Model_Product product)
    {
        if (!validateProductMaterial(product))
        {
            return false;
        }

//        if (!validateProductColor(product))
//        {
//            return false;
//        }
//
//        if (!validateProductKrep(product))
//        {
//            return false;
//        }
//
//        if (!validateProductControl(product))
//        {
//            return false;
//        }

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

    public static double countVaucherElementsSum(Model_Document document)
    {
        double sum = 0;
        for (Model_Price_Element price_element : document.getVaucher().getPrice_elements())
        {
            sum += price_element.getPrice();
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

        sum = sum - ((sum / 100.0f) * percent);

        sum += montage;
        sum += delivery;

        return sum;
    }

    public static double countSumMinusPercent(double sum, int percent)
    {
        return sum - ((sum / 100) * percent);
    }

    public static void invalidateRecursive(ViewGroup layout)
    {
        int count = layout.getChildCount();
        View child;
        for (int i = 0; i < count; i++)
        {
            child = layout.getChildAt(i);
            if (child instanceof ViewGroup)
            {
                invalidateRecursive((ViewGroup) child);
            }
            else
            {
                child.invalidate();
            }
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
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public static Integer getEtIntegerValueOrNull(EditText editText)
    {
        try
        {
            String str = editText.getText().toString().trim();
            str = str.replaceAll("[^\\d.]", "");
            return Integer.valueOf(str);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static Integer getEtIntegerPercent(EditText editText)
    {
        try
        {
            String str = editText.getText().toString().trim();
            str = str.replaceAll("[^\\d.]", "");
            int percent = Integer.valueOf(str);
            if (percent > 100)
            {
                percent = 100;
            }
            return percent;
        }
        catch (Exception e)
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
        }
        catch (Exception e)
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
        }
        catch (Exception e)
        {
            return 0d;
        }
    }

    public static int getPercentFromTwoNums(double sum, double sale)
    {
        if (sum == 0)
        {
            return 0;
        }

        Log.e(TAG, "Sum is " + sum + " | SaleRub is " + sale + " percent is " + (int) ((sale * 100.0f) / sum));
        return (int) ((sale * 100.0f) / sum);
    }

    public static double getSumAfterPercentCut(double sum, int sale)
    {
        return (sum / 100) * sale;
    }

    public static Model_Product copyProduct(Model_Product productOriginal)
    {
        if (productOriginal == null)
        {
            return null;
        }

        Model_Product productCopy = new Model_Product();

        productCopy.setId(productOriginal.getId());
        productCopy.setDocument_id(productOriginal.getDocument_id());
        productCopy.setHeight(productOriginal.getHeight());
        productCopy.setWidth(productOriginal.getWidth());
        productCopy.setCount(productOriginal.getCount());
        productCopy.setPrice(productOriginal.getPrice());
        productCopy.setSum(productOriginal.getSum());

        productCopy.setColor(copyColor(productOriginal.getColor()));
        productCopy.setControl(copyControl(productOriginal.getControl()));
        productCopy.setKrep(copyKrep(productOriginal.getKrep()));
        productCopy.setMaterial(copyMaterial(productOriginal.getMaterial()));

        return productCopy;
    }

    public static Model_Price_Element copyPriceElement(Model_Price_Element elementOriginal)
    {
        if (elementOriginal == null)
        {
            return null;
        }

        Model_Price_Element elementCopy = new Model_Price_Element();
        elementCopy.setId(elementOriginal.getId());
        elementCopy.setVaucher_id(elementCopy.getVaucher_id());
        elementCopy.setText(elementOriginal.getText());
        elementCopy.setPrice(elementOriginal.getPrice());
        return elementCopy;
    }

    public static Model_Color copyColor(Model_Color colorOriginal)
    {
        if (colorOriginal == null)
        {
            return null;
        }

        Model_Color colorCopy = new Model_Color();

        colorCopy.setId(colorOriginal.getId());
        colorCopy.setProduct_id(colorOriginal.getProduct_id());
        colorCopy.setName(colorOriginal.getName());

        return colorCopy;
    }

    public static Model_Control copyControl(Model_Control controlOriginal)
    {
        if (controlOriginal == null)
        {
            return null;
        }

        Model_Control controlCopy = new Model_Control();

        controlCopy.setId(controlOriginal.getId());
        controlCopy.setProduct_id(controlOriginal.getProduct_id());
        controlCopy.setName(controlOriginal.getName());

        return controlCopy;
    }

    public static Model_Krep copyKrep(Model_Krep krepOriginal)
    {
        if (krepOriginal == null)
        {
            return null;
        }

        Model_Krep krepCopy = new Model_Krep();

        krepCopy.setId(krepOriginal.getId());
        krepCopy.setProduct_id(krepOriginal.getProduct_id());
        krepCopy.setName(krepOriginal.getName());

        return krepCopy;
    }


    public static Model_Material copyMaterial(Model_Material materialOriginal)
    {
        if (materialOriginal == null)
        {
            return null;
        }

        Model_Material materialCopy = new Model_Material();

        materialCopy.setId(materialOriginal.getId());
        materialCopy.setProduct_id(materialOriginal.getProduct_id());
        materialCopy.setName(materialOriginal.getName());

        return materialCopy;
    }

    public static void resetDocumentIds(Model_Document document)
    {
        document.setId(0);

        for (Model_Product product : document.getListOfProducts())
        {
            product.setId(0);

            if (product.getColor() != null)
            {
                product.getColor().setId(0);
            }

            if (product.getControl() != null)
            {
                product.getControl().setId(0);
            }

            if (product.getKrep() != null)
            {
                product.getKrep().setId(0);
            }

            if (product.getMaterial() != null)
            {
                product.getMaterial().setId(0);
            }
        }

        if (document.getVaucher() != null)
        {
            document.getVaucher().setId(0);

            if (document.getVaucher().getPrice_elements() != null)
            {
                for (Model_Price_Element price_element : document.getVaucher().getPrice_elements())
                {
                    price_element.setId(0);
                }
            }
        }
    }

    public static Model_Document clearEmptyOrNullData(Model_Document document)
    {
        document.setFio(clearNullStrings(document.getFio()));
        document.setAdress(clearNullStrings(document.getAdress()));
        document.setPhone(clearNullStrings(document.getPhone()));
        document.setCode(clearNullStrings(document.getCode()));
        document.setOrder_form(clearNullStrings(document.getOrder_form()));
        document.setDop_info(clearNullStrings(document.getDop_info()));

        if (document.getListOfProducts() != null)
        {
            for (Model_Product product : document.getListOfProducts())
            {
                product.setWidth(clearNullStrings(product.getWidth()));
                product.setHeight(clearNullStrings(product.getHeight()));

                if (product.getMaterial() != null)
                {
                    product.getMaterial().setName(clearNullStrings(product.getMaterial().getName()));
                }

                if (product.getColor() != null)
                {
                    product.getColor().setName(clearNullStrings(product.getColor().getName()));
                }

                if (product.getKrep() != null)
                {
                    product.getKrep().setName(clearNullStrings(product.getKrep().getName()));
                }

                if (product.getControl() != null)
                {
                    product.getControl().setName(clearNullStrings(product.getControl().getName()));
                }
            }
        }

        if (document.getVaucher() != null)
        {
            document.getVaucher().setHeader(clearNullStrings(document.getVaucher().getHeader()));

            for (Model_Price_Element price_element : document.getVaucher().getPrice_elements())
            {
                price_element.setText(clearNullStrings(price_element.getText()));
            }
        }

        return document;
    }

    public static String clearNullStrings(String string)
    {
        if (string == null)
        {
            return null;
        }

        if (string.trim().equals("null"))
        {
            return null;
        }

        if (TextUtils.isEmpty(string.trim()))
        {
            return null;
        }

        return string;
    }

    public static void updatePositionNums(int firstPosition, int secondPosition, DragLinearLayout drag_la)
    {
        View view1 = drag_la.getChildAt(firstPosition);
        TextView tv_position1 = view1.findViewById(R.id.tv_position);

        View view2 = drag_la.getChildAt(secondPosition);
        TextView tv_position2 = view2.findViewById(R.id.tv_position);

        tv_position1.setText(String.valueOf(secondPosition + 1));
        tv_position2.setText(String.valueOf(firstPosition + 1));
    }

    public static int getRandomInt()
    {
        return getRandomInt(0, 9999);
    }

    public static int getRandomInt(final int min, final int max)
    {
        return new Random().nextInt((max - min) + 1) + min;
    }

    public static String getFullName(Model_User user)
    {
        return user.getLast_name() + " " + user.getFirst_name();
    }

    public static int getRadioGroupValue(RadioGroup radioGroup)
    {
        Integer value = null;
        for (int a = 0; a < radioGroup.getChildCount(); a++)
        {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(a);
            if (radioButton.isChecked())
            {
                value = a;
            }
        }

        if (value == null)
        {
            throw new RuntimeException("**** ERROR ON GETTING RADIO BUTTON VALUE - NO VALUE ****");
        }

        return value;
    }

    public static String getSortStringFromInt(Integer sortInt)
    {
        String sort = null;

        if (sortInt != null && sortInt != 999999)
        {
            switch (sortInt)
            {
                case 0:
                    sort = "id";
                    break;
                case 1:
                    sort = "first_name";
                    break;
                case 2:
                    sort = "last_name";
                    break;
                case 3:
                    sort = "email";
                    break;
                case 4:
                    sort = "role_id";
                    break;
                case 5:
                    sort = "admin_approved";
                    break;
                case 6:
                    sort = "app_version";
                    break;
            }
        }

        return sort;
    }

    public static String getSortStringFromIntRus(Integer sortInt)
    {
        String sort = null;

        if (sortInt != null && sortInt != 999999)
        {
            switch (sortInt)
            {
                case 0:
                    sort = "По умолчанию";
                    break;
                case 1:
                    sort = "По имени";
                    break;
                case 2:
                    sort = "По фамилии";
                    break;
                case 3:
                    sort = "По email";
                    break;
                case 4:
                    sort = "По доступу";
                    break;
                case 5:
                    sort = "По одобрениям";
                    break;
                case 6:
                    sort = "По версии приложения";
                    break;
            }
        }

        return sort;
    }

    public static int getRolesSpinnerId(Spinner spinner)
    {
        switch (spinner.getSelectedItemPosition())
        {
            case 0:
                return 1;
            case 1:
                return 7;
            case 2:
                return 999;

            default:
                throw new RuntimeException("Error invalid spinner role id");
        }
    }

    public static void setRadioSelectedAt(RadioGroup rg, int position)
    {
        RadioButton rb = (RadioButton) rg.getChildAt(position);
        rb.setChecked(true);
    }

    public static boolean isLocationEnabled()
    {
        int locationMode = 0;
        Context context = AppClass.getApp();
        String locationProviders;

        try
        {
            locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
        }
        catch (Settings.SettingNotFoundException e)
        {
            e.printStackTrace();
        }

        return locationMode != Settings.Secure.LOCATION_MODE_OFF;

    }

    public static String APP_VERSION()
    {
        return BuildConfig.VERSION_NAME;
    }

    public static double getRandomDouble(double min, double max)
    {
        Random r = new Random();
        return min + (max - min) * r.nextDouble();
    }

    public static Bitmap getBitmapFromView(View view)
    {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return bitmap;
    }

    public static void makeDialogTransparentBg(AlertDialog dialog)
    {
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
}
