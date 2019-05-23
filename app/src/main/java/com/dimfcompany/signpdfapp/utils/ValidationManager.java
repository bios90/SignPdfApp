package com.dimfcompany.signpdfapp.utils;

import android.text.TextUtils;
import android.util.Patterns;

import com.dimfcompany.signpdfapp.base.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class ValidationManager
{
    public static boolean validateForRegister(String first_name, String last_name, String email, String password1, String password2)
    {
        if (TextUtils.isEmpty(first_name))
        {
            return false;
        }

        if (TextUtils.isEmpty(last_name))
        {
            return false;
        }

        if (TextUtils.isEmpty(email))
        {
            return false;
        }

        if (!isEmail(email))
        {
            return false;
        }

        if (TextUtils.isEmpty(password1))
        {
            return false;
        }

        if (TextUtils.isEmpty(password2))
        {
            return false;
        }

        if (!password1.equals(password2))
        {
            return false;
        }

        if (password1.length() < 8)
        {
            return false;
        }

        return true;
    }

    public static List<String> getRegistrationListOfErrors(String first_name, String last_name, String email, String password1, String password2)
    {
        List<String> errors = new ArrayList<>();

        if (TextUtils.isEmpty(first_name))
        {
            errors.add(Constants.ERROR_NO_FIRSTNAME);
        }

        if (TextUtils.isEmpty(last_name))
        {
            errors.add(Constants.ERROR_NO_SECONDNAME);
        }

        if (TextUtils.isEmpty(email))
        {
            errors.add(Constants.ERROR_NO_EMAIL);
        } else if (!isEmail(email))
        {
            errors.add(Constants.ERROR_EMAIL_NOT_VALID);
        }

        if (TextUtils.isEmpty(password1))
        {
            errors.add(Constants.ERROR_NO_PASSWORD);
        } else if (password1.length() < 8)
        {
            errors.add(Constants.ERROR_PASSWORD_TOO_SHORT);
        } else if (!password1.equals(password2))
        {
            errors.add(Constants.ERROR_PASSWORD_NOT_MATCH);
        }

        return errors;
    }

    public static boolean validateForLogin(String email, String password, String fb_token)
    {
        if (TextUtils.isEmpty(email))
        {
            return false;
        }

        if (!isEmail(email))
        {
            return false;
        }

        if (TextUtils.isEmpty(password))
        {
            return false;
        }

        if (TextUtils.isEmpty(fb_token))
        {
            return false;
        }

        return true;
    }

    public static List<String> getLoginListOfErrors(String email, String password, String fb_token)
    {
        List<String> errors = new ArrayList<>();

        if (TextUtils.isEmpty(email))
        {
            errors.add(Constants.ERROR_NO_EMAIL);
        }
        else if (!isEmail(email))
        {
            errors.add(Constants.ERROR_EMAIL_NOT_VALID);
        }

        if (TextUtils.isEmpty(password))
        {
            errors.add(Constants.ERROR_NO_PASSWORD);
        }
        else if (password.length() < 8)
        {
            errors.add(Constants.ERROR_PASSWORD_TOO_SHORT);
        }

        if(TextUtils.isEmpty(fb_token))
        {
            errors.add(Constants.ERROR_NO_FIRSTNAME);
        }

        return errors;
    }

    public static boolean isEmail(String email)
    {
        Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(email);
        return matcher.matches();
    }
}
