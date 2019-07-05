package com.dimfcompany.signpdfapp.utils;

import android.text.TextUtils;
import android.util.Patterns;

import com.dimfcompany.signpdfapp.base.Constants;

import java.util.ArrayList;
import java.util.Date;
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
        }
        else if (!isEmail(email))
        {
            errors.add(Constants.ERROR_EMAIL_NOT_VALID);
        }

        if (TextUtils.isEmpty(password1))
        {
            errors.add(Constants.ERROR_NO_PASSWORD);
        }
        else if (password1.length() < 8)
        {
            errors.add(Constants.ERROR_PASSWORD_TOO_SHORT);
        }
        else if (!password1.equals(password2))
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

        if (TextUtils.isEmpty(fb_token))
        {
            errors.add(Constants.ERROR_NO_FIRSTNAME);
        }

        return errors;
    }

    public static boolean validateUserAuthNew(String first_name, String last_name, String email, String password1, String password2, Integer role_id, boolean edit_mode)
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

        if (edit_mode)
        {
            if ((password1 != null) || (password2 != null))
            {
                if (!password1.equals(password2))
                {
                    return false;
                }

                if (password1.length() < 8)
                {
                    return false;
                }
            }
        }
        else
        {
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
        }

        return true;
    }


    public static List<String> getUserAuthNewErrors(String first_name, String last_name, String email, String password1, String password2, Integer role_id, boolean edit_mode)
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
        }
        else if (!isEmail(email))
        {
            errors.add(Constants.ERROR_EMAIL_NOT_VALID);
        }

        if (edit_mode)
        {
            if ((password1 != null) || (password2 != null))
            {
                if (!password1.equals(password2))
                {
                    errors.add(Constants.ERROR_PASSWORD_NOT_MATCH);
                }

                if (password1.length() < 8)
                {
                    errors.add(Constants.ERROR_PASSWORD_TOO_SHORT);
                }
            }
        }
        else
        {
            if (TextUtils.isEmpty(password1))
            {
                errors.add(Constants.ERROR_NO_PASSWORD);
            }
            else if (password1.length() < 8)
            {
                errors.add(Constants.ERROR_PASSWORD_TOO_SHORT);
            }
            else if (!password1.equals(password2))
            {
                errors.add(Constants.ERROR_PASSWORD_NOT_MATCH);
            }
        }
        return errors;
    }

    public static boolean validateDocumentSearch(Date date_min, Date date_max, Integer sum_min, Integer sum_max)
    {
        if (date_max != null && date_min != null)
        {
            if (date_min.after(date_max))
            {
                return false;
            }
        }

        if (sum_min != null && sum_max != null)
        {
            if (sum_min > sum_max)
            {
                return false;
            }
        }

        return true;
    }

    public static String getSearchErrors(Date date_min, Date date_max, Integer sum_min, Integer sum_max)
    {
        List<String> errors = new ArrayList<>();

        if (date_max != null && date_min != null)
        {
            if (date_min.after(date_max))
            {
                errors.add("Введите корректные даты");
            }
        }

        if (sum_min != null && sum_max != null)
        {
            if (sum_min > sum_max)
            {
                errors.add("Введите корректные суммы");
            }
        }

        return StringManager.listOfStringToSingle(errors);
    }

    public static boolean isEmail(String email)
    {
        Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(email);
        return matcher.matches();
    }
}
