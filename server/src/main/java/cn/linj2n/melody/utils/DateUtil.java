package cn.linj2n.melody.utils;

import java.time.*;

public class DateUtil {

    private static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Shanghai");

    public static ZonedDateTime nowDateTime() {
        return ZonedDateTime.now(DEFAULT_ZONE);
    }

    public static LocalDate nowLocalDate() {
        return ZonedDateTime.now(DEFAULT_ZONE).toLocalDate();
    }

    public static ZonedDateTime getStartOfDay(LocalDate localDate) {
        return ZonedDateTime.of(localDate, LocalTime.MIN, DEFAULT_ZONE);
    }

    public static ZonedDateTime getEndOfDay(LocalDate localDate) {
        return ZonedDateTime.of(localDate, LocalTime.MAX, DEFAULT_ZONE);
    }

    public static ZonedDateTime getStartOfYesterday() {
       return ZonedDateTime.now(DEFAULT_ZONE).minusDays(1L).toLocalDate().atStartOfDay(DEFAULT_ZONE);
    }

}
