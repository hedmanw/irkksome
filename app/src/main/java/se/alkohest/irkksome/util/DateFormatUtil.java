package se.alkohest.irkksome.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by wilhelm 2014-11-01.
 */
public class DateFormatUtil {
    private static final DateFormat timestampFormat = new SimpleDateFormat("HH:mm");
    private static final DateFormat timestampDayFormat = new SimpleDateFormat("HH:mm d MMM");

    public static String getTimestamp(Date date) {
        return timestampFormat.format(date);
    }

    public static String getTimePassed(Date date) {
        Calendar today = Calendar.getInstance();
//        today.add(Calendar.DAY_OF_YEAR, -1);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) && today.get(Calendar.DAY_OF_YEAR) > calendar.get(Calendar.DAY_OF_YEAR)) {
            return timestampDayFormat.format(date);
        } else {
            return timestampFormat.format(date);
        }
    }
}
