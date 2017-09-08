package com.stasmobstudios.payconiqtest.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Stanislovas Mickus on 07/09/2017.
 */

public class DateUtil {
    public static String dateToString(final Date date, final String dateFormat) {
        SimpleDateFormat simpleDate = new SimpleDateFormat(dateFormat);
        return simpleDate.format(date);
    }

    public static Date stringToDate(final String dateString, final String dateFormat) {
        Date date;
        DateFormat df = new SimpleDateFormat(dateFormat);

        try {
            date = df.parse(dateString);
        } catch (ParseException e) {
            date = new Date(0);
            Log.e("DateUtil", e.getMessage());
        }

        return date;
    }
}
