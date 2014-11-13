package br.com.arndroid.etdiet.test.provider.parametershistory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ProviderTestCase2;
import android.util.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.Provider;
import br.com.arndroid.etdiet.provider.parametershistory.ParametersHistoryEntity;
import br.com.arndroid.etdiet.provider.parametershistory.ParametersHistoryManager;
import br.com.arndroid.etdiet.utils.DateUtils;

public class ParametersHistoryManagerTest extends ProviderTestCase2<Provider> {

    private static final Logger LOG = LoggerFactory.getLogger(ParametersHistoryManagerTest.class);

    private ParametersHistoryManager mManager;
    private SQLiteDatabase mDb;

    public ParametersHistoryManagerTest() {
        super(Provider.class, Contract.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        // Important: calling the base class implementation of this method
        // where the "magic" of isolation is set up:
        super.setUp();

        // Gets the manager for this test.
        mManager = new ParametersHistoryManager(getMockContext());

        // Gets the database for fixture manipulation.
        mDb = getProvider().getOpenHelper().getWritableDatabase();
    }

    public void testGetTrackingWeekDayForDateWithOneDbRowMustReturnCorrectValue() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = calendar.getTime();
        int expectedWeekDay = Calendar.SATURDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(yesterday));
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(today));
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(tomorrow));
    }

    public void testGetTrackingWeekDayForDateWithTwoDbRowMustReturnCorrectValue() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = calendar.getTime();
        ParametersHistoryEntity yesterdayEntity = new ParametersHistoryEntity(null, Contract.ParametersHistory.TRACKING_WEEKDAY_PARAMETER_TYPE,
                DateUtils.dateToDateId(yesterday), Calendar.FRIDAY, null, null);
        mDb.insert(Contract.ParametersHistory.TABLE_NAME, null, yesterdayEntity.toContentValuesIgnoreNulls());
        int expectedWeekDay = Calendar.FRIDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(yesterday));
        expectedWeekDay = Calendar.SATURDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(today));
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(tomorrow));
    }

    public void testGetTrackingWeekDayForDateWithThreeSpacedDbRowMustReturnCorrectValue() {
        /*
        We are going to test DB with history for three days: present - 10d, present and present + 10d.
        Because the 10d time space between two dates, we're calling them spaced.
         */
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -10);
        Date paste = calendar.getTime();
        LOG.trace("Inside testGetTrackingWeekDayForDateWithThreeSpacedDbRowMustReturnCorrectValue(): paste={}", paste);
        calendar.add(Calendar.DAY_OF_MONTH, 10);
        Date present = calendar.getTime();
        LOG.trace("Inside testGetTrackingWeekDayForDateWithThreeSpacedDbRowMustReturnCorrectValue(): present={}", present);
        calendar.add(Calendar.DAY_OF_MONTH, 10);
        Date future = calendar.getTime();
        LOG.trace("Inside testGetTrackingWeekDayForDateWithThreeSpacedDbRowMustReturnCorrectValue(): future={}", future);

        ParametersHistoryEntity entity = new ParametersHistoryEntity(null, Contract.ParametersHistory.TRACKING_WEEKDAY_PARAMETER_TYPE,
                DateUtils.dateToDateId(paste), Calendar.FRIDAY, null, null);
        mDb.insert(Contract.ParametersHistory.TABLE_NAME, null, entity.toContentValuesIgnoreNulls());
        entity = new ParametersHistoryEntity(null, Contract.ParametersHistory.TRACKING_WEEKDAY_PARAMETER_TYPE,
                DateUtils.dateToDateId(future), Calendar.SUNDAY, null, null);
        mDb.insert(Contract.ParametersHistory.TABLE_NAME, null, entity.toContentValuesIgnoreNulls());

        // Paste:
        calendar.setTime(paste);
        calendar.add(Calendar.DAY_OF_MONTH, -5);
        LOG.trace("Inside testGetTrackingWeekDayForDateWithThreeSpacedDbRowMustReturnCorrectValue(): paste - 5d={}", calendar.getTime());
        int expectedWeekDay = Calendar.FRIDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(calendar.getTime()));
        calendar.setTime(paste);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        LOG.trace("Inside testGetTrackingWeekDayForDateWithThreeSpacedDbRowMustReturnCorrectValue(): paste - 1d={}", calendar.getTime());
        expectedWeekDay = Calendar.FRIDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(calendar.getTime()));
        calendar.setTime(paste);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        LOG.trace("Inside testGetTrackingWeekDayForDateWithThreeSpacedDbRowMustReturnCorrectValue(): paste + 1d={}", calendar.getTime());
        expectedWeekDay = Calendar.FRIDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(calendar.getTime()));
        calendar.setTime(paste);
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        LOG.trace("Inside testGetTrackingWeekDayForDateWithThreeSpacedDbRowMustReturnCorrectValue(): paste + 5d={}", calendar.getTime());
        expectedWeekDay = Calendar.FRIDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(calendar.getTime()));

        // Present:
        calendar.setTime(present);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        LOG.trace("Inside testGetTrackingWeekDayForDateWithThreeSpacedDbRowMustReturnCorrectValue(): present - 1d={}", calendar.getTime());
        expectedWeekDay = Calendar.FRIDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(calendar.getTime()));
        calendar.setTime(present);
        LOG.trace("Inside testGetTrackingWeekDayForDateWithThreeSpacedDbRowMustReturnCorrectValue(): present + 0d={}", calendar.getTime());
        expectedWeekDay = Calendar.SATURDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(calendar.getTime()));
        calendar.setTime(present);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        LOG.trace("Inside testGetTrackingWeekDayForDateWithThreeSpacedDbRowMustReturnCorrectValue(): present + 1d={}", calendar.getTime());
        expectedWeekDay = Calendar.SATURDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(calendar.getTime()));
        calendar.setTime(present);
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        LOG.trace("Inside testGetTrackingWeekDayForDateWithThreeSpacedDbRowMustReturnCorrectValue(): present + 5d={}", calendar.getTime());
        expectedWeekDay = Calendar.SATURDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(calendar.getTime()));

        // Future:
        calendar.setTime(future);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        LOG.trace("Inside testGetTrackingWeekDayForDateWithThreeSpacedDbRowMustReturnCorrectValue(): future - 1d={}", calendar.getTime());
        expectedWeekDay = Calendar.SATURDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(calendar.getTime()));
        calendar.setTime(future);
        LOG.trace("Inside testGetTrackingWeekDayForDateWithThreeSpacedDbRowMustReturnCorrectValue(): future + 0d={}", calendar.getTime());
        expectedWeekDay = Calendar.SUNDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(calendar.getTime()));
        calendar.setTime(future);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        LOG.trace("Inside testGetTrackingWeekDayForDateWithThreeSpacedDbRowMustReturnCorrectValue(): future + 1d={}", calendar.getTime());
        expectedWeekDay = Calendar.SUNDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(calendar.getTime()));
        calendar.setTime(future);
        calendar.add(Calendar.DAY_OF_MONTH, 10);
        LOG.trace("Inside testGetTrackingWeekDayForDateWithThreeSpacedDbRowMustReturnCorrectValue(): future + 10d={}", calendar.getTime());
        expectedWeekDay = Calendar.SUNDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(calendar.getTime()));
        calendar.setTime(future);
        calendar.add(Calendar.DAY_OF_MONTH, 1000);
        LOG.trace("Inside testGetTrackingWeekDayForDateWithThreeSpacedDbRowMustReturnCorrectValue(): future + 1000d={}", calendar.getTime());
        expectedWeekDay = Calendar.SUNDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(calendar.getTime()));
    }

    public void testGetDailyAllowanceForDateWithThreeSpacedDbRowMustReturnCorrectValue() {
        /*
        We are going to test DB with history for three days: present - 10d, present and present + 10d.
        Because the 10d time space between two dates, we're calling them spaced.
         */
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -10);
        Date paste = calendar.getTime();
        LOG.trace("Inside testGetDailyAllowanceForDateWithThreeSpacedDbRowMustReturnCorrectValue(): paste={}", paste);
        calendar.add(Calendar.DAY_OF_MONTH, 10);
        Date present = calendar.getTime();
        LOG.trace("Inside testGetDailyAllowanceForDateWithThreeSpacedDbRowMustReturnCorrectValue(): present={}", present);
        calendar.add(Calendar.DAY_OF_MONTH, 10);
        Date future = calendar.getTime();
        LOG.trace("Inside testGetDailyAllowanceForDateWithThreeSpacedDbRowMustReturnCorrectValue(): future={}", future);

        final float pasteValue = 20.4f;
        final float presentValue = 26.0f;
        final float futureValue = 30.6f;

        ParametersHistoryEntity entity = new ParametersHistoryEntity(null, Contract.ParametersHistory.DAILY_ALLOWANCE_PARAMETER_TYPE,
                DateUtils.dateToDateId(paste), null, pasteValue, null);
        mDb.insert(Contract.ParametersHistory.TABLE_NAME, null, entity.toContentValuesIgnoreNulls());
        entity = new ParametersHistoryEntity(null, Contract.ParametersHistory.DAILY_ALLOWANCE_PARAMETER_TYPE,
                DateUtils.dateToDateId(future), null, futureValue, null);
        mDb.insert(Contract.ParametersHistory.TABLE_NAME, null, entity.toContentValuesIgnoreNulls());

        // Paste:
        calendar.setTime(paste);
        calendar.add(Calendar.DAY_OF_MONTH, -5);
        LOG.trace("Inside testGetDailyAllowanceForDateWithThreeSpacedDbRowMustReturnCorrectValue(): past - 5d={}", calendar.getTime());
        float expectedDailyAllowance = pasteValue;
        assertEquals(expectedDailyAllowance, mManager.getDailyAllowanceForDate(calendar.getTime()));
        calendar.setTime(paste);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        LOG.trace("Inside testGetDailyAllowanceForDateWithThreeSpacedDbRowMustReturnCorrectValue(): past - 1d={}", calendar.getTime());
        expectedDailyAllowance = pasteValue;
        assertEquals(expectedDailyAllowance, mManager.getDailyAllowanceForDate(calendar.getTime()));
        calendar.setTime(paste);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        LOG.trace("Inside testGetDailyAllowanceForDateWithThreeSpacedDbRowMustReturnCorrectValue(): past + 1d={}", calendar.getTime());
        expectedDailyAllowance = pasteValue;
        assertEquals(expectedDailyAllowance, mManager.getDailyAllowanceForDate(calendar.getTime()));
        calendar.setTime(paste);
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        LOG.trace("Inside testGetDailyAllowanceForDateWithThreeSpacedDbRowMustReturnCorrectValue(): past + 5d={}", calendar.getTime());
        expectedDailyAllowance = pasteValue;
        assertEquals(expectedDailyAllowance, mManager.getDailyAllowanceForDate(calendar.getTime()));

        // Present:
        calendar.setTime(present);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        LOG.trace("Inside testGetDailyAllowanceForDateWithThreeSpacedDbRowMustReturnCorrectValue(): present - 1d={}", calendar.getTime());
        expectedDailyAllowance = pasteValue;
        assertEquals(expectedDailyAllowance, mManager.getDailyAllowanceForDate(calendar.getTime()));
        calendar.setTime(present);
        LOG.trace("Inside testGetDailyAllowanceForDateWithThreeSpacedDbRowMustReturnCorrectValue(): preset + 0d={}", calendar.getTime());
        expectedDailyAllowance = presentValue;
        assertEquals(expectedDailyAllowance, mManager.getDailyAllowanceForDate(calendar.getTime()));
        calendar.setTime(present);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        LOG.trace("Inside testGetDailyAllowanceForDateWithThreeSpacedDbRowMustReturnCorrectValue(): present + 1d={}", calendar.getTime());
        expectedDailyAllowance = presentValue;
        assertEquals(expectedDailyAllowance, mManager.getDailyAllowanceForDate(calendar.getTime()));
        calendar.setTime(present);
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        LOG.trace("Inside testGetDailyAllowanceForDateWithThreeSpacedDbRowMustReturnCorrectValue(): present + 5d={}", calendar.getTime());
        expectedDailyAllowance = presentValue;
        assertEquals(expectedDailyAllowance, mManager.getDailyAllowanceForDate(calendar.getTime()));

        // Future:
        calendar.setTime(future);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        LOG.trace("Inside testGetDailyAllowanceForDateWithThreeSpacedDbRowMustReturnCorrectValue(): future - 1d={}", calendar.getTime());
        expectedDailyAllowance = presentValue;
        assertEquals(expectedDailyAllowance, mManager.getDailyAllowanceForDate(calendar.getTime()));
        calendar.setTime(future);
        expectedDailyAllowance = futureValue;
        LOG.trace("Inside testGetDailyAllowanceForDateWithThreeSpacedDbRowMustReturnCorrectValue(): future + 0d={}", calendar.getTime());
        assertEquals(expectedDailyAllowance, mManager.getDailyAllowanceForDate(calendar.getTime()));
        calendar.setTime(future);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        LOG.trace("Inside testGetDailyAllowanceForDateWithThreeSpacedDbRowMustReturnCorrectValue(): future + 1d={}", calendar.getTime());
        expectedDailyAllowance = futureValue;
        assertEquals(expectedDailyAllowance, mManager.getDailyAllowanceForDate(calendar.getTime()));
        calendar.setTime(future);
        calendar.add(Calendar.DAY_OF_MONTH, 10);
        LOG.trace("Inside testGetDailyAllowanceForDateWithThreeSpacedDbRowMustReturnCorrectValue(): future + 10d={}", calendar.getTime());
        expectedDailyAllowance = futureValue;
        assertEquals(expectedDailyAllowance, mManager.getDailyAllowanceForDate(calendar.getTime()));
        calendar.setTime(future);
        calendar.add(Calendar.DAY_OF_MONTH, 1000);
        LOG.trace("Inside testGetDailyAllowanceForDateWithThreeSpacedDbRowMustReturnCorrectValue(): future + 1000d={}", calendar.getTime());
        expectedDailyAllowance = futureValue;
        assertEquals(expectedDailyAllowance, mManager.getDailyAllowanceForDate(calendar.getTime()));
    }

    public void testSetTrackWeekdayForDateWithNoValueChangedMustDoNotModifyDataBase() {
        Cursor c = null;
        int expectedRows;
        try {
            c = getMockContentResolver().query(Contract.ParametersHistory.CONTENT_URI, Contract.ParametersHistory.ALL_COLS_PROJECTION,
                    Contract.ParametersHistory.TYPE_SELECTION, new String[] {String.valueOf(Contract.ParametersHistory.TRACKING_WEEKDAY_PARAMETER_TYPE)},
                    Contract.ParametersHistory.DATE_DESC_SORT_ORDER);
            expectedRows = c.getCount();
        } finally {
            if(c != null) c.close();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        mManager.setTrackingWeekdayForDate(Calendar.SATURDAY, calendar.getTime());
        try {
            c = getMockContentResolver().query(Contract.ParametersHistory.CONTENT_URI, Contract.ParametersHistory.ALL_COLS_PROJECTION,
                    Contract.ParametersHistory.TYPE_SELECTION, new String[] {String.valueOf(Contract.ParametersHistory.TRACKING_WEEKDAY_PARAMETER_TYPE)},
                    Contract.ParametersHistory.DATE_DESC_SORT_ORDER);
            assertEquals(expectedRows, c.getCount());
        } finally {
            if(c != null) c.close();
        }
    }

    public void testSetTrackingWeekdayForDateWithDateRecordedInDatabaseMustUpdateRecord() {
        Cursor c = null;
        int expectedRows;
        try {
            c = getMockContentResolver().query(Contract.ParametersHistory.CONTENT_URI, Contract.ParametersHistory.ALL_COLS_PROJECTION,
                    Contract.ParametersHistory.TYPE_SELECTION, new String[] {String.valueOf(Contract.ParametersHistory.TRACKING_WEEKDAY_PARAMETER_TYPE)},
                    Contract.ParametersHistory.DATE_DESC_SORT_ORDER);
            expectedRows = c.getCount();
        } finally {
            if(c != null) c.close();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        mManager.setTrackingWeekdayForDate(Calendar.SUNDAY, calendar.getTime());
        try {
            c = getMockContentResolver().query(Contract.ParametersHistory.CONTENT_URI, Contract.ParametersHistory.ALL_COLS_PROJECTION,
                    Contract.ParametersHistory.TYPE_SELECTION, new String[] {String.valueOf(Contract.ParametersHistory.TRACKING_WEEKDAY_PARAMETER_TYPE)},
                    Contract.ParametersHistory.DATE_DESC_SORT_ORDER);
            assertEquals(expectedRows, c.getCount());
        } finally {
            if(c != null) c.close();
        }
    }

    public void testSetTrackingWeekdayForDateWithoutDateRecordedInDatabaseMustInsertRecord() {
        Cursor c = null;
        int expectedRows;
        try {
            c = getMockContentResolver().query(Contract.ParametersHistory.CONTENT_URI, Contract.ParametersHistory.ALL_COLS_PROJECTION,
                    Contract.ParametersHistory.TYPE_SELECTION, new String[]{String.valueOf(Contract.ParametersHistory.TRACKING_WEEKDAY_PARAMETER_TYPE)},
                    Contract.ParametersHistory.DATE_DESC_SORT_ORDER);
            expectedRows = c.getCount();
        } finally {
            if(c != null) c.close();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        mManager.setTrackingWeekdayForDate(Calendar.SUNDAY, calendar.getTime());
        try {
            c = getMockContentResolver().query(Contract.ParametersHistory.CONTENT_URI, Contract.ParametersHistory.ALL_COLS_PROJECTION,
                    Contract.ParametersHistory.TYPE_SELECTION, new String[] {String.valueOf(Contract.ParametersHistory.TRACKING_WEEKDAY_PARAMETER_TYPE)},
                    Contract.ParametersHistory.DATE_DESC_SORT_ORDER);
            assertEquals(expectedRows + 1, c.getCount());
        } finally {
            if(c != null) c.close();
        }
    }

    public void testSetDailyAllowanceForDateWithNoValueChangedMustDoNotModifyDataBase() {
        Cursor c = null;
        int expectedRows;
        try {
            c = getMockContentResolver().query(Contract.ParametersHistory.CONTENT_URI, Contract.ParametersHistory.ALL_COLS_PROJECTION,
                    Contract.ParametersHistory.TYPE_SELECTION, new String[] {String.valueOf(Contract.ParametersHistory.DAILY_ALLOWANCE_PARAMETER_TYPE)},
                    Contract.ParametersHistory.DATE_DESC_SORT_ORDER);
            expectedRows = c.getCount();
        } finally {
            if(c != null) c.close();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        mManager.setDailyAllowanceForDate(26.0f, calendar.getTime());
        try {
            c = getMockContentResolver().query(Contract.ParametersHistory.CONTENT_URI, Contract.ParametersHistory.ALL_COLS_PROJECTION,
                    Contract.ParametersHistory.TYPE_SELECTION, new String[] {String.valueOf(Contract.ParametersHistory.DAILY_ALLOWANCE_PARAMETER_TYPE)},
                    Contract.ParametersHistory.DATE_DESC_SORT_ORDER);
            assertEquals(expectedRows, c.getCount());
        } finally {
            if(c != null) c.close();
        }
    }

    public void testSetDailyAllowanceForDateWithDateRecordedInDatabaseMustUpdateRecord() {
        Cursor c = null;
        int expectedRows;
        try {
            c = getMockContentResolver().query(Contract.ParametersHistory.CONTENT_URI, Contract.ParametersHistory.ALL_COLS_PROJECTION,
                    Contract.ParametersHistory.TYPE_SELECTION, new String[] {String.valueOf(Contract.ParametersHistory.DAILY_ALLOWANCE_PARAMETER_TYPE)},
                    Contract.ParametersHistory.DATE_DESC_SORT_ORDER);
            expectedRows = c.getCount();
        } finally {
            if(c != null) c.close();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        mManager.setDailyAllowanceForDate(99.0f, calendar.getTime());
        try {
            c = getMockContentResolver().query(Contract.ParametersHistory.CONTENT_URI, Contract.ParametersHistory.ALL_COLS_PROJECTION,
                    Contract.ParametersHistory.TYPE_SELECTION, new String[] {String.valueOf(Contract.ParametersHistory.DAILY_ALLOWANCE_PARAMETER_TYPE)},
                    Contract.ParametersHistory.DATE_DESC_SORT_ORDER);
            assertEquals(expectedRows, c.getCount());
        } finally {
            if(c != null) c.close();
        }
    }

    public void testSetDailyAllowanceForDateWithoutDateRecordedInDatabaseMustInsertRecord() {
        Cursor c = null;
        int expectedRows;
        try {
            c = getMockContentResolver().query(Contract.ParametersHistory.CONTENT_URI, Contract.ParametersHistory.ALL_COLS_PROJECTION,
                    Contract.ParametersHistory.TYPE_SELECTION, new String[]{String.valueOf(Contract.ParametersHistory.DAILY_ALLOWANCE_PARAMETER_TYPE)},
                    Contract.ParametersHistory.DATE_DESC_SORT_ORDER);
            expectedRows = c.getCount();
        } finally {
            if(c != null) c.close();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        mManager.setDailyAllowanceForDate(99.0f, calendar.getTime());
        try {
            c = getMockContentResolver().query(Contract.ParametersHistory.CONTENT_URI, Contract.ParametersHistory.ALL_COLS_PROJECTION,
                    Contract.ParametersHistory.TYPE_SELECTION, new String[] {String.valueOf(Contract.ParametersHistory.DAILY_ALLOWANCE_PARAMETER_TYPE)},
                    Contract.ParametersHistory.DATE_DESC_SORT_ORDER);
            assertEquals(expectedRows + 1, c.getCount());
        } finally {
            if(c != null) c.close();
        }
    }
}