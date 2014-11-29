package com.epita.mti.tinytube.util;

import android.text.format.DateUtils;

import java.util.Date;

/**
 * Created by mrollin on 28/11/14.
 */
public class DateUtil {

    /**
     * Convert a date into a "time ago" string
     * @param date the date
     * @return the well formed date
     */
    public static String timeAgoInWords(Date date) {
        return DateUtils.getRelativeTimeSpanString(
                date.getTime(),
                new Date().getTime(),
                DateUtils.HOUR_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL
        ).toString();
    }

}
