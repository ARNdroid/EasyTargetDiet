package br.com.arndroid.etdiet.test.util;

import junit.framework.TestCase;

import java.util.Calendar;

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
}
