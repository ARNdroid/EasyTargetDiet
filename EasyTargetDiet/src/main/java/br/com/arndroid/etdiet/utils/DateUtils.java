package br.com.arndroid.etdiet.utils;

import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private static final int DATE_ID_YEAR_START_POSITION = 0; // inclusive
    private static final int DATE_ID_YEAR_END_POSITION = 4; // exclusive
    private static final int DATE_ID_MONTH_START_POSITION = 4; // inclusive
    private static final int DATE_ID_MONTH_END_POSITION = 6; // exclusive
    private static final int DATE_ID_DAY_START_POSITION = 6; // inclusive
    private static final String DATE_ID_FORMAT_STRING = "yyyyMMdd";
    private static final String TIMESTAMP_FORMAT_STRING = "yyyyMMddHHmmss";
    private static final String DATE_FORMAT_STRING = "dd/MM/yyyy";
    private static final String DATE_AND_TIME_FORMAT_STRING = "dd/MM/yyyy HH:mm:ss";

    // Utility class.
    private DateUtils() {

    }

    public static void initDatePickerWithDateId(DatePicker picker, String dateId) {
        picker.init(getYearFromDateId(dateId), getMonthFromDateId(dateId) - 1, getDayFromDateId(dateId), null);
    }

    public static int getMinutesFromTimeAsInt(int time) {
        int onlyMinutes = time - hoursToMillis(getHoursFromTimeAsInt(time));
        return onlyMinutes / minutesToMillis(1);
    }

    public static int getHoursFromTimeAsInt(int time) {
        return  time / hoursToMillis(1);
    }

    public static int getYearFromDateId(String dateId) {
        return Integer.parseInt(getFormattedYearFromDateId(dateId));
    }

    public static String getFormattedYearFromDateId(String dateId) {
        return dateId.substring(DATE_ID_YEAR_START_POSITION, DATE_ID_YEAR_END_POSITION);
    }

    public static int getMonthFromDateId(String dateId) {
        return Integer.parseInt(dateId.substring(DATE_ID_MONTH_START_POSITION, DATE_ID_MONTH_END_POSITION));
    }

    public static int getDayFromDateId(String dateId) {
        return Integer.parseInt(getFormattedDayFromDateId(dateId));
    }

    public static String getFormattedDayFromDateId(String dateId) {
        return dateId.substring(DATE_ID_DAY_START_POSITION);
    }

    public static int getWeekdayFromDateId(String dateId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateIdToDate(dateId));
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static boolean isDateIdCurrentDate(String dateId) {
        return compareDateId(dateToDateId(new Date()), dateId) == 0;
    }

    public static int compareDateId(String dateId, String compareToDateId) {
        return dateId.compareTo(compareToDateId);
    }

    public static int hoursToMillis(int hours) {
        return hours * 60 * 60 * 1000;
    }

    public static int minutesToMillis(int minutes) {
        return minutes * 60 * 1000;
    }

    public static String datePickerToDateId(DatePicker picker) {
        Calendar cal = Calendar.getInstance();
        int year = picker.getYear();
        int month = picker.getMonth();
        int dayOfMonth = picker.getDayOfMonth();
        // The DatePicker.getMonth() return the month from a Calendar. It's safe here to suppress
        // the lint warning:
        // noinspection ResourceType
        cal.set(year, month, dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_ID_FORMAT_STRING);

        return sdf.format(cal.getTime());
    }

    public static String dateToDateId(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_ID_FORMAT_STRING);
        return sdf.format(date);
    }

    public static int timePickerToTimeAsInt(TimePicker picker) {
        return hoursToMillis(picker.getCurrentHour()) + minutesToMillis(picker.getCurrentMinute());
    }

    public static Date dateIdToDate(String dateId) {
        Calendar cal = Calendar.getInstance();
        // We don't care about Calendar.January, Calendar.February because the inspection will be
        // satisfied only using the literal constant.
        // noinspection ResourceType
        cal.set(getYearFromDateId(dateId), getMonthFromDateId(dateId) - 1, getDayFromDateId(dateId), 0, 0);
        return (cal.getTime());
    }

    public static String dateIdToFormattedString(String dateId) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_STRING);
        return sdf.format(dateIdToDate(dateId));
    }

    public static boolean dateIdStartsBefore(String before, String after) {
        return before.compareTo(after) < 0;
    }

    public static boolean dateIdStartsEqualsOrBefore(String before, String after) {
        return before.compareTo(after) <= 0;
    }

    public static String timeToFormattedString(int time) {
        final DecimalFormat df = new DecimalFormat("00");
        return df.format(getHoursFromTimeAsInt(time)) + ":" + df.format(getMinutesFromTimeAsInt(time));
    }

    public static int formattedStringToTime(String formattedString) {
        return hoursToMillis(Integer.parseInt(formattedString.substring(0, 2)))
                + minutesToMillis(Integer.parseInt(formattedString.substring(3, 5)));
    }
    public static int dateToTimeAsInt(Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return hoursToMillis(cal.get(Calendar.HOUR_OF_DAY)) + minutesToMillis(cal.get(Calendar.MINUTE));
    }

    public static String dateToTimestamp(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_FORMAT_STRING);
        return sdf.format(date);
    }

    public static String epochToFormattedString(long epoch) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_AND_TIME_FORMAT_STRING);
        return sdf.format(epochToDate(epoch));
    }

    public static Date epochToDate(long epoch) {
        return new Date(epoch);
    }
}
