package br.com.arndroid.etdiet.test.util;

import junit.framework.TestCase;

import java.util.Calendar;
import java.util.Date;

import br.com.arndroid.etdiet.utils.CurrentDateId;
import br.com.arndroid.etdiet.utils.DateUtils;

public class CurrentDateIdTest extends TestCase {
    public void testSettingAnCurrentDateMustReturnCorrectValues() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(1966, Calendar.MAY, 18);
        final Date dateBeforeChanges = calendar.getTime();
        final String dateIdBeforeChanges = DateUtils.dateToDateId(dateBeforeChanges);
        calendar.set(1977, Calendar.FEBRUARY, 2);
        final Date dateAfterChanges = calendar.getTime();
        final String dateIdAfterChanges = DateUtils.dateToDateId(dateAfterChanges);

        CurrentDateId currentDateId = new CurrentDateId();
        currentDateId.setCurrentDateForTests(dateBeforeChanges);

        currentDateId.setCurrentDateId(dateIdBeforeChanges);
        assertEquals(dateIdBeforeChanges, currentDateId.getCurrentDateId());

        currentDateId.setCurrentDateForTests(dateAfterChanges);
        assertEquals(dateIdAfterChanges, currentDateId.getCurrentDateId());
    }

    public void testSettingNotCurrentDateMustReturnCorrectValues() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(1966, Calendar.MAY, 18);
        final Date initialDate = calendar.getTime();
        calendar.set(1977, Calendar.FEBRUARY, 2);
        final Date dateAfterChanges = calendar.getTime();
        calendar.set(2007, Calendar.SEPTEMBER, 19);
        final Date dateStored = calendar.getTime();
        final String dateIdStored = DateUtils.dateToDateId(dateStored);

        CurrentDateId currentDateId = new CurrentDateId();
        currentDateId.setCurrentDateForTests(initialDate);

        currentDateId.setCurrentDateId(dateIdStored);
        assertEquals(dateIdStored, currentDateId.getCurrentDateId());

        currentDateId.setCurrentDateForTests(dateAfterChanges);
        assertEquals(dateIdStored, currentDateId.getCurrentDateId());
    }
}
