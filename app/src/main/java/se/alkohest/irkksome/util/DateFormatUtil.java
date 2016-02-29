package se.alkohest.irkksome.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class DateFormatUtil {
    private static final DateFormat timestampFormat = new SimpleDateFormat("HH:mm");
    private static final DateFormat timestampDayFormat = new SimpleDateFormat("HH:mm d MMM");
    private static final DateFormat timestampYearFormat = new SimpleDateFormat("HH:mm d MMM y");

    public static String getTimestamp(Date date) {
        return timestampFormat.format(date);
    }

    public static String getTimeDay(Date date) {
        long timeDiff = TimeUnit.DAYS.convert(new Date().getTime()-date.getTime(), TimeUnit.MILLISECONDS);

        if (timeDiff <= 0) {
            return timestampFormat.format(date);
        }
        else if (timeDiff < 365) {
            return timestampDayFormat.format(date);
        }
        else {
            return timestampYearFormat.format(date);
        }
    }
}
