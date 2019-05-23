package com.dimfcompany.signpdfapp.local_db.room;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter
{
    @TypeConverter
    public static Date toDate(Long dateLong)
    {
        if(dateLong == null)
        {
            return null;
        }

        return new Date(dateLong);
    }

    @TypeConverter
    public static Long toLong(Date dateDate)
    {
        if(dateDate == null)
        {
            return null;
        }

        return dateDate.getTime();
    }
}
