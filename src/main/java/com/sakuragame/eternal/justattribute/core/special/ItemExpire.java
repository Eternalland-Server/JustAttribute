package com.sakuragame.eternal.justattribute.core.special;

import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ItemExpire {

    public final static String NBT_CONFIG_NODE = "justattribute.expire";
    public final static String NBT_EXPIRE_NODE = "justattribute.expired";
    public final static String DISPLAY_NODE = "display.expire";

    private final static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static long getMonthEndDay() {
        Calendar calendar = getCalendar();
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTimeInMillis() / 1000;
    }

    public static long getExpiredDay(int day) {
        Calendar calendar = getCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTimeInMillis() / 1000;
    }

    public static boolean isExpired(long time) {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis() >= time * 1000;
    }

    public static String formatting(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time * 1000);

        return ConfigFile.format.expire.replace("<expire>", FORMAT.format(calendar.getTime()));
    }

    private static Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }
}
