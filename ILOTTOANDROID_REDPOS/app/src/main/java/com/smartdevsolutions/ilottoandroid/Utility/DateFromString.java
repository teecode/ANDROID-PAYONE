package com.smartdevsolutions.ilottoandroid.Utility;

import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by T360-INNOVATIVZ on 19/05/2017.
 */

public  class DateFromString {

    @Nullable
    static Date GetDate(String date) {
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-ddddTHH:mm:ss").parse(date);
            return  date1;
        }
        catch(Exception ex)
        {
            return  null;
        }
    }
}
