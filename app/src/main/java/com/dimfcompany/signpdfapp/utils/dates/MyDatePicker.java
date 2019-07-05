package com.dimfcompany.signpdfapp.utils.dates;

import java.util.Date;

public interface MyDatePicker
{
    interface CallbackDatePicker
    {
        void onDateSelected(Date date);
        void onCancelled();
    }

    void selectYearMonthDay(String title, boolean min_today, int max_years_plus, CallbackDatePicker callback);
    void selectYearMonthDayHourMinute(String title, CallbackDatePicker callback);
}
