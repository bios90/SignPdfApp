package com.dimfcompany.signpdfapp.utils.dates;

import android.content.Context;

import com.dimfcompany.signpdfapp.R;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerDialog implements MyDatePicker
{
    private final Context context;

    public DatePickerDialog(Context context)
    {
        this.context = context;
    }

    @Override
    public void selectYearMonthDay(String title,boolean min_today, int max_years_plus, final CallbackDatePicker callback)
    {
        Calendar calendar_now = Calendar.getInstance();
        Calendar calendar_max = Calendar.getInstance();
        calendar_max.add(Calendar.YEAR,max_years_plus);

        SpinnerDatePickerDialogBuilder builder =  new SpinnerDatePickerDialogBuilder()
                .context(context)
                .callback(new com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                        Date date = calendar.getTime();
                        callback.onDateSelected(date);
                    }
                })
                .spinnerTheme(R.style.NumberPickerStyle)
                .showTitle(true)
                .showDaySpinner(true)
                .defaultDate(calendar_now.get(Calendar.YEAR), calendar_now.get(Calendar.MONTH), calendar_now.get(Calendar.DAY_OF_MONTH))
                .maxDate(calendar_max.get(Calendar.YEAR), calendar_max.get(Calendar.MONTH), calendar_max.get(Calendar.DAY_OF_MONTH));

        if(min_today)
        {
            builder.minDate(calendar_now.get(Calendar.YEAR), calendar_now.get(Calendar.MONTH), calendar_now.get(Calendar.DAY_OF_MONTH));
        }

        builder.build().show();
    }

    @Override
    public void selectYearMonthDayHourMinute(String title, CallbackDatePicker callback)
    {

    }
}
