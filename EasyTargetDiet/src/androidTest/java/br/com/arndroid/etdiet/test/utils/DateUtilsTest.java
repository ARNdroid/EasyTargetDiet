package br.com.arndroid.etdiet.test.utils;

import junit.framework.TestCase;

import java.util.Calendar;
import java.util.Date;

import br.com.arndroid.etdiet.utils.DateUtils;

public class DateUtilsTest extends TestCase {

    public void testDateToDateId() {
        final String expectedId = "19660518";
        Calendar calendar = Calendar.getInstance();
        calendar.set(1966, Calendar.MAY, 18);
        assertEquals(expectedId, DateUtils.dateToDateId(calendar.getTime()));
    }

    public void testDateIdStartsBefore() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1966, Calendar.MAY, 18);
        String btdFather = DateUtils.dateToDateId(calendar.getTime());
        calendar.set(2007, Calendar.SEPTEMBER, 18);
        String btdSon = DateUtils.dateToDateId(calendar.getTime());
        assertTrue(DateUtils.dateIdStartsBefore(btdFather, btdSon));
    }

    public void testDateIdStartsEqualsOrBefore() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1966, Calendar.MAY, 18);
        String btdFather = DateUtils.dateToDateId(calendar.getTime());
        calendar.set(2007, Calendar.SEPTEMBER, 18);
        String btdSon = DateUtils.dateToDateId(calendar.getTime());
        assertTrue(DateUtils.dateIdStartsEqualsOrBefore(btdFather, btdSon));
        assertTrue(DateUtils.dateIdStartsEqualsOrBefore(btdFather, btdFather));
        assertTrue(!DateUtils.dateIdStartsEqualsOrBefore(btdSon, btdFather));
    }

    public void testIsDateIdCurrentDate() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        assertTrue(DateUtils.isDateIdCurrentDate(DateUtils.dateToDateId(calendar.getTime())));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        assertFalse(DateUtils.isDateIdCurrentDate(DateUtils.dateToDateId(calendar.getTime())));
    }

    public void testCompareDateId() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        final String todayDateId = DateUtils.dateToDateId(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        final String tomorrowDateId = DateUtils.dateToDateId(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -2);
        final String yesterdayDateId = DateUtils.dateToDateId(calendar.getTime());
        assertEquals(DateUtils.compareDateId(todayDateId, DateUtils.dateToDateId(new Date())), 0);
        assertTrue(DateUtils.compareDateId(todayDateId, yesterdayDateId) > 0);
        assertTrue(DateUtils.compareDateId(todayDateId, tomorrowDateId) < 0);
    }
}
