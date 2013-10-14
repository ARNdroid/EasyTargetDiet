package br.com.arndroid.etdiet.test.parametershistory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ProviderTestCase2;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.Provider;
import br.com.arndroid.etdiet.provider.parametershistory.ParametersHistoryEntity;
import br.com.arndroid.etdiet.provider.parametershistory.ParametersHistoryManager;
import br.com.arndroid.etdiet.util.DateUtil;

public class ParametersHistoryManagerTest extends ProviderTestCase2<Provider> {

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
                DateUtil.dateToDateId(yesterday), Calendar.FRIDAY, null, null);
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
        calendar.add(Calendar.DAY_OF_MONTH, 10);
        Date present = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 10);
        Date future = calendar.getTime();
        ParametersHistoryEntity entity = new ParametersHistoryEntity(null, Contract.ParametersHistory.TRACKING_WEEKDAY_PARAMETER_TYPE,
                DateUtil.dateToDateId(paste), Calendar.FRIDAY, null, null);
        mDb.insert(Contract.ParametersHistory.TABLE_NAME, null, entity.toContentValuesIgnoreNulls());
        entity = new ParametersHistoryEntity(null, Contract.ParametersHistory.TRACKING_WEEKDAY_PARAMETER_TYPE,
                DateUtil.dateToDateId(future), Calendar.SUNDAY, null, null);
        mDb.insert(Contract.ParametersHistory.TABLE_NAME, null, entity.toContentValuesIgnoreNulls());

        // Paste:
        calendar.setTime(paste);
        calendar.add(Calendar.DAY_OF_MONTH, -5);
        int expectedWeekDay = Calendar.FRIDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(calendar.getTime()));
        calendar.setTime(paste);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        expectedWeekDay = Calendar.FRIDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(calendar.getTime()));
        calendar.setTime(paste);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        expectedWeekDay = Calendar.FRIDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(calendar.getTime()));
        calendar.setTime(paste);
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        expectedWeekDay = Calendar.FRIDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(calendar.getTime()));

        // Present:
        calendar.setTime(present);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        expectedWeekDay = Calendar.FRIDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(calendar.getTime()));
        calendar.setTime(present);
        expectedWeekDay = Calendar.SATURDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(calendar.getTime()));
        calendar.setTime(present);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        expectedWeekDay = Calendar.SATURDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(calendar.getTime()));
        calendar.setTime(present);
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        expectedWeekDay = Calendar.SATURDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(calendar.getTime()));

        // Future:
        calendar.setTime(future);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        expectedWeekDay = Calendar.SATURDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(calendar.getTime()));
        calendar.setTime(future);
        expectedWeekDay = Calendar.SUNDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(calendar.getTime()));
        calendar.setTime(future);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        expectedWeekDay = Calendar.SUNDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(calendar.getTime()));
        calendar.setTime(future);
        calendar.add(Calendar.DAY_OF_MONTH, 10);
        expectedWeekDay = Calendar.SUNDAY;
        assertEquals(expectedWeekDay, mManager.getTrackingWeekdayForDate(calendar.getTime()));
        calendar.setTime(future);
        calendar.add(Calendar.DAY_OF_MONTH, 1000);
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
        calendar.add(Calendar.DAY_OF_MONTH, 10);
        Date present = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 10);
        Date future = calendar.getTime();
        ParametersHistoryEntity entity = new ParametersHistoryEntity(null, Contract.ParametersHistory.DAILY_ALLOWANCE_PARAMETER_TYPE,
                DateUtil.dateToDateId(paste), null, 20.0f, null);
        mDb.insert(Contract.ParametersHistory.TABLE_NAME, null, entity.toContentValuesIgnoreNulls());
        entity = new ParametersHistoryEntity(null, Contract.ParametersHistory.DAILY_ALLOWANCE_PARAMETER_TYPE,
                DateUtil.dateToDateId(future), null, 30.0f, null);
        mDb.insert(Contract.ParametersHistory.TABLE_NAME, null, entity.toContentValuesIgnoreNulls());

        // Paste:
        calendar.setTime(paste);
        calendar.add(Calendar.DAY_OF_MONTH, -5);
        float expectedDailyAllowance = 20.0f;
        assertEquals(expectedDailyAllowance, mManager.getDailyAllowanceForDate(calendar.getTime()));
        calendar.setTime(paste);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        expectedDailyAllowance = 20.0f;
        assertEquals(expectedDailyAllowance, mManager.getDailyAllowanceForDate(calendar.getTime()));
        calendar.setTime(paste);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        expectedDailyAllowance = 20.0f;
        assertEquals(expectedDailyAllowance, mManager.getDailyAllowanceForDate(calendar.getTime()));
        calendar.setTime(paste);
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        expectedDailyAllowance = 20.0f;
        assertEquals(expectedDailyAllowance, mManager.getDailyAllowanceForDate(calendar.getTime()));

        // Present:
        calendar.setTime(present);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        expectedDailyAllowance = 20.0f;
        assertEquals(expectedDailyAllowance, mManager.getDailyAllowanceForDate(calendar.getTime()));
        calendar.setTime(present);
        expectedDailyAllowance = 26.0f;
        assertEquals(expectedDailyAllowance, mManager.getDailyAllowanceForDate(calendar.getTime()));
        calendar.setTime(present);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        expectedDailyAllowance = 26.0f;
        assertEquals(expectedDailyAllowance, mManager.getDailyAllowanceForDate(calendar.getTime()));
        calendar.setTime(present);
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        expectedDailyAllowance = 26.0f;
        assertEquals(expectedDailyAllowance, mManager.getDailyAllowanceForDate(calendar.getTime()));

        // Future:
        calendar.setTime(future);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        expectedDailyAllowance = 26.0f;
        assertEquals(expectedDailyAllowance, mManager.getDailyAllowanceForDate(calendar.getTime()));
        calendar.setTime(future);
        expectedDailyAllowance = 30.0f;
        assertEquals(expectedDailyAllowance, mManager.getDailyAllowanceForDate(calendar.getTime()));
        calendar.setTime(future);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        expectedDailyAllowance = 30.0f;
        assertEquals(expectedDailyAllowance, mManager.getDailyAllowanceForDate(calendar.getTime()));
        calendar.setTime(future);
        calendar.add(Calendar.DAY_OF_MONTH, 10);
        expectedDailyAllowance = 30.0f;
        assertEquals(expectedDailyAllowance, mManager.getDailyAllowanceForDate(calendar.getTime()));
        calendar.setTime(future);
        calendar.add(Calendar.DAY_OF_MONTH, 1000);
        expectedDailyAllowance = 30.0f;
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

    @SuppressWarnings("UnusedDeclaration")
    private void cursorContentShow(Cursor cursor) {
        cursor.moveToFirst();
        do {
            ParametersHistoryEntity entity = ParametersHistoryEntity.fromCursor(cursor);
            if(isLogEnabled) {
                Log.d(TAG, entity.toString());
            }
        } while (cursor.moveToNext());
    }

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "==>ETD/" + ParametersHistoryManagerTest.class.getSimpleName();
    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;
}