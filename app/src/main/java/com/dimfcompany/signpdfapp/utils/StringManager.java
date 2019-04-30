package com.dimfcompany.signpdfapp.utils;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;

import com.dimfcompany.signpdfapp.utils.custom_classes.CustomTypefaceSpan;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class StringManager
{
    private static final String TAG = "StringManager";
    public static final String FORMAT_FOR_CODE = "ddMMyyHHmm";

    private AppCompatActivity activity;
    private static final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static Random RANDOM = new Random();

    public StringManager(AppCompatActivity activity)
    {
        this.activity = activity;
    }

    public static String randomStr()
    {
        int len = 20;
        StringBuilder sb = new StringBuilder(len);
        for (int a = 0; a <= len; a++)
        {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }
        return sb.toString();
    }

    public static String formatFioForOthcet(String fio)
    {
        if (TextUtils.isEmpty(fio))
        {
            return "";
        }
        String formated = null;

        List<String> wordsAsList = new ArrayList<String>(Arrays.asList(fio.split(" ")));

        for (int i = 0; i < wordsAsList.size(); i++)
        {
            String word = wordsAsList.get(i);
            word = word.trim();
            word = uppercaseFirstLetter(word);
            if (i > 0)
            {
                word = word.substring(0, 1);
                word += ".";
            }

            wordsAsList.set(i, word);
        }

        formated = listOfStringToSingle(wordsAsList, " ");
        return formated;
    }

    public static String listOfStringToSingle(List<String> strings, String separator)
    {
        StringBuilder sb = new StringBuilder();
        for (String element : strings)
        {
            sb.append(element);
            if (strings.indexOf(element) != (strings.size() - 1))
            {
                sb.append(separator);
            }
        }

        return sb.toString();
    }

    public static String uppercaseFirstLetter(String str)
    {
        String upperString = str.substring(0, 1).toUpperCase() + str.substring(1);
        return upperString;
    }


    public static String formatNum(Number val, boolean withTwo)
    {
        String pattern = null;
        if (withTwo)
        {
            pattern = "#,###,###.##";
        } else
        {
            pattern = "#,###,##0";
        }

        String value = String.valueOf(val);

        return formatWithPattern(value, pattern);
    }

    public static String formatNum(String value, boolean withTwo)
    {
        String pattern = null;
        if (withTwo)
        {
            pattern = "#,###,###.##";
        } else
        {
            pattern = "#,###,##0";
        }

        return formatWithPattern(value, pattern);
    }


    public static String formatWithPattern(String value, String pattern)
    {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
        formatSymbols.setDecimalSeparator('.');
        formatSymbols.setGroupingSeparator(' ');
        DecimalFormat df = new DecimalFormat(pattern, formatSymbols);
        return df.format(Double.valueOf(value));
    }


    public SpannableString getBoldSpannable(String str, int color)
    {
        Typeface segBold = Typeface.createFromAsset(activity.getAssets(), "monextrabold.ttf");
        return getSpannableString(str, segBold, color);
    }

    public SpannableString getRegSpannable(String str, int color)
    {
        Typeface segReg = Typeface.createFromAsset(activity.getAssets(), "monmedium.ttf");
        return getSpannableString(str, segReg, color);
    }

    public SpannableString getSemiSpannable(String str, int color)
    {
        Typeface segSemi = Typeface.createFromAsset(activity.getAssets(), "monsemibold.ttf");
        return getSpannableString(str, segSemi, color);
    }

    public SpannableString getSpannableString(String str, Typeface typeface, int color)
    {
        SpannableStringBuilder sb = new SpannableStringBuilder(str);

        TypefaceSpan segTupefaceSpan = new CustomTypefaceSpan("", typeface);
        sb.setSpan(segTupefaceSpan, 0, str.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(color), 0, str.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        return SpannableString.valueOf(sb);
    }

    public static String getCode(int city)
    {
        if (city > 3 || city < 0)
        {
            city = 0;
        }

        String code;

        switch (city)
        {
            case 0:
                code = "77";
                break;
            case 1:
                code = "78";
                break;
            case 2:
                code = "23";
                break;
            case 3:
                code = "63";
                break;

            default:
                code = "77";
        }
        code += "-";
        code+=DateFormat.format(FORMAT_FOR_CODE, new Date()).toString();

        return code;
    }

}
