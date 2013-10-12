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
}
