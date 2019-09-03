package com.dimfcompany.signpdfapp.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.local_db.sharedprefs.SharedPrefsHelper;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_User;
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
    public static final String FORMAT_FOR_CODE = "ddMMyyHHmmss";

    private final Context context;
    private final SharedPrefsHelper sharedPrefsHelper;
    private static final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static Random RANDOM = new Random();

    public StringManager(Context context, SharedPrefsHelper sharedPrefsHelper)
    {
        this.context = context;
        this.sharedPrefsHelper = sharedPrefsHelper;
    }

    public static String repeatingString(@Nullable String str, String element, int times)
    {

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < times; i++)
        {
            stringBuilder.append(element);
        }
        String resultString = stringBuilder.toString();

        if (str == null)
        {
            return resultString;
        }

        return str + resultString;
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

    public static String listOfStringToSingle(List<String> strings)
    {
        return listOfStringToSingle(strings, "\n");
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
        }
        else
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
        }
        else
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


    public static String transliterate(String message)
    {
        char[] abcCyr = {' ', 'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', 'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String[] abcLat = {" ", "a", "b", "v", "g", "d", "e", "e", "zh", "z", "i", "y", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ts", "ch", "sh", "sch", "", "i", "", "e", "ju", "ja", "A", "B", "V", "G", "D", "E", "E", "Zh", "Z", "I", "Y", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H", "Ts", "Ch", "Sh", "Sch", "", "I", "", "E", "Ju", "Ja", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++)
        {
            boolean haveToAdd = true;

            for (int x = 0; x < abcCyr.length; x++)
            {
                if (message.charAt(i) == abcCyr[x])
                {
                    builder.append(abcLat[x]);
                    haveToAdd = false;
                }
            }

            if (haveToAdd)
            {
                builder.append(message.charAt(i));
            }
        }
        return builder.toString();
    }

    public SpannableString getBoldSpannable(String str, int color)
    {
        Typeface segBold = Typeface.createFromAsset(context.getAssets(), "monextrabold.ttf");
        return getSpannableString(str, segBold, color);
    }

    public SpannableString getRegSpannable(String str, int color)
    {
        Typeface segReg = Typeface.createFromAsset(context.getAssets(), "monmedium.ttf");
        return getSpannableString(str, segReg, color);
    }

    public SpannableString getSemiSpannable(String str, int color)
    {
        Typeface segSemi = Typeface.createFromAsset(context.getAssets(), "monsemibold.ttf");
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

    public String getUserPrivateFolderUrl()
    {
        Model_User user = sharedPrefsHelper.getUserFromSharedPrefs();
        if (user == null)
        {
            return null;
        }

        return Constants.URL_BASE + "storage/" + user.getId() + "/";
    }

    public String getUserSignaturesUrl()
    {
        return getUserPrivateFolderUrl() + "signatures/";
    }

    public String getUserDocumentsUrl()
    {
        return getUserPrivateFolderUrl() + "documents/";
    }

    public String getUserChecksUrl()
    {
        return getUserPrivateFolderUrl() + "checks/";
    }

    public String getUserVauchersUrl()
    {
        return getUserPrivateFolderUrl() + "vauchers/";
    }

    public String getAnyUserPrivateUrl(long user_id)
    {
        return Constants.URL_BASE + "storage/" + user_id + "/";
    }

    public String getAnyUserSignaturesUrl(long user_id)
    {
        return getAnyUserPrivateUrl(user_id) + "signatures/";
    }

    public String getAnyUserDocumentsUrl(long user_id)
    {
        return getAnyUserPrivateUrl(user_id) + "documents/";
    }

    public String getAnyUserCheckUrl(long user_id)
    {
        return getAnyUserPrivateUrl(user_id) + "checks/";
    }

    public String getAnyUserVauchersUrl(long user_id)
    {
        return getAnyUserPrivateUrl(user_id) + "vauchers/";
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
        code += DateFormat.format(FORMAT_FOR_CODE, new Date()).toString();

        return code;
    }

    public static String getFullName(Model_User user)
    {
        if (user == null)
        {
            Log.e(TAG, "getFullName: error director is null");
            return null;
        }

        return user.getLast_name() + " " + user.getFirst_name();
    }

    public static String castToNullIfEmpty(String text)
    {
        if (text == null)
        {
            return null;
        }

        if (TextUtils.isEmpty(text))
        {
            return null;
        }

        return text;
    }

    public static String getEtText(EditText editText)
    {
        String str = editText.getText().toString().trim();
        if (TextUtils.isEmpty(str))
        {
            return null;
        }
        return str;
    }

    public static String getTextForMarker(Model_Document document)
    {
        String text = "";

        text = text + GlobalHelper.getDateString(document.getDate()) + " | ";
        if (document.getFio() != null)
        {
            text += document.getFio() + " | ";
        }

        if (document.getAdress() != null)
        {
            text += document.getAdress();
        }

        return text;
    }

}
