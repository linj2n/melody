package cn.linj2n.melody.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static Date getYesterdayDate() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public static String getYesterdayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(getYesterdayDate());
    }

    public static Date daysAgo(int delta) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -delta);
        return cal.getTime();
    }
}
