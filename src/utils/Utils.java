package utils;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class Utils
{
    public static final DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder()
            .appendDayOfMonth(2)
            .appendLiteral('.')
            .appendMonthOfYear(2)
            .appendLiteral('.')
            .appendYear(4, 4)
            .toFormatter();
    public static final PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
            .printZeroAlways()
            .appendHours()
            .appendLiteral(":")
            .appendMinutes()
            .toFormatter();
}
