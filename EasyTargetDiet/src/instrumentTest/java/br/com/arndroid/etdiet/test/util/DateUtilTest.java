package br.com.arndroid.etdiet.test.util;

import junit.framework.TestCase;

import java.util.Calendar;

import br.com.arndroid.etdiet.util.DateUtil;

public class DateUtilTest extends TestCase {

    public void testDateToDateId() {
        final String expectedId = "19660518";
        Calendar calendar = Calendar.getInstance();
        calendar.set(1966, Calendar.MAY, 18);
        assertEquals(expectedId, DateUtil.dateToDateId(calendar.getTime()));
    }

    public void testDateIdStartsBefore() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1966, Calendar.MAY, 18);
        String btdFather = DateUtil.dateToDateId(calendar.getTime());
        calendar.set(2007, Calendar.SEPTEMBER, 18);
        String btdSon = DateUtil.dateToDateId(calendar.getTime());
        assertTrue(DateUtil.dateIdStartsBefore(btdFather, btdSon));
    }

    public void testDateIdStartsEqualsOrBefore() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1966, Calendar.MAY, 18);
        String btdFather = DateUtil.dateToDateId(calendar.getTime());
        calendar.set(2007, Calendar.SEPTEMBER, 18);
        String btdSon = DateUtil.dateToDateId(calendar.getTime());
        assertTrue(DateUtil.dateIdStartsEqualsOrBefore(btdFather, btdSon));
        assertTrue(DateUtil.dateIdStartsEqualsOrBefore(btdFather, btdFather));
        assertTrue(!DateUtil.dateIdStartsEqualsOrBefore(btdSon, btdFather));
    }
}
