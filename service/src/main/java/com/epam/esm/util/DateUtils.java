package com.epam.esm.util;

import java.time.YearMonth;

/**
 * The type Date utils.
 */
public final class DateUtils {

    private static final int MIN_MONTH_NUMBER = 1;
    private static final int MAX_MONTH_NUMBER = 12;
    private static final int MIN_DAY_NUMBER = 0;

    /**
     * Is valid date.
     *
     * @param year  the year
     * @param month the month
     * @param day   the day
     * @return true if date valid
     */
    public static boolean isValidDate(int year, int month, int day) {
        return day > MIN_DAY_NUMBER && day <= daysInMonth(year, month)
                && month >= MIN_MONTH_NUMBER && month <= MAX_MONTH_NUMBER;
    }

    /**
     * Days in month int.
     *
     * @param year  the year
     * @param month the month
     * @return the number of days
     */
    public static int daysInMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        return yearMonth.lengthOfMonth();
    }

    private DateUtils() {
    }
}
