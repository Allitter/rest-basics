package com.epam.esm.util;

import java.time.YearMonth;

/**
 * The type Date utils.
 */
public final class DateUtils {

    private static final int MIN_MONTH_NUMBER = 1;
    private static final int MAX_MONTH_NUMBER = 12;
    private static final int MIN_DAY_NUMBER = 0;
    private static final int FIRST_MONTH = 1;

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

    /**
     * Is leap.
     *
     * @param year the year
     * @return true if it is leap year
     */
    public static boolean isLeap(int year) {
        YearMonth yearMonth = YearMonth.of(year, FIRST_MONTH);
        return yearMonth.isLeapYear();
    }

    private DateUtils() {
    }
}
