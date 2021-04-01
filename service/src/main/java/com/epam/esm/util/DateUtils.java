package com.epam.esm.util;

/**
 * The type Date utils.
 */
public final class DateUtils {

    /**
     * Is valid date.
     *
     * @param year  the year
     * @param month the month
     * @param day   the day
     * @return true if date valid
     */
    public static boolean isValidDate(int year, int month, int day) {
        return day > 0 && day <= daysInMonth(year, month) && month > 0 && month < 13;
    }

    /**
     * Days in month int.
     *
     * @param year  the year
     * @param month the month
     * @return the number of days
     */
    public static int daysInMonth(int year, int month) {
        int daysInMonth;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                daysInMonth = 31;
                break;
            case 2:
                if (isLeap(year)) {
                    daysInMonth = 29;
                } else {
                    daysInMonth = 28;
                }
                break;
            default:
                daysInMonth = 30;
        }
        return daysInMonth;
    }

    /**
     * Is leap.
     *
     * @param year the year
     * @return true if it is leap year
     */
    public static boolean isLeap(int year) {
        return (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0));
    }

    private DateUtils() {
    }
}
