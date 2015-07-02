package br.com.arndroid.etdiet.test.provider.foodsusage;

import android.database.Cursor;
import android.test.ProviderTestCase2;

import java.util.Calendar;
import java.util.Date;

import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.Provider;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.provider.days.DaysManager;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageManager;
import br.com.arndroid.etdiet.utils.DateUtils;

public class FoodsUsageManagerTest extends ProviderTestCase2<Provider> {

    private FoodsUsageManager mManager;

    public FoodsUsageManagerTest() {
        super(Provider.class, Contract.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        // Important: calling the base class implementation of this method
        // where the "magic" of isolation is set up:
        super.setUp();

        // Gets the manager for this test.
        mManager = new FoodsUsageManager(getMockContext());
    }

    public void testRemoveMustDelete() {
        final String dateId = DateUtils.dateToDateId(new Date());
        final FoodsUsageEntity entity = new FoodsUsageEntity(null, dateId,
                Meals.getMealFromPosition(1), 2, "some description", 4.0f);
        mManager.refresh(entity);
        int totalRecords = -1;
        Cursor c = null;
        try {
            c = getMockContentResolver().query(Contract.FoodsUsage.CONTENT_URI, null,
                    Contract.FoodsUsage.DATE_ID_SELECTION, new String[] {dateId}, null);
            totalRecords = c.getCount();
        } finally {
            if (c != null) c.close();
        }

        mManager.remove(entity.getId());

        try {
            c = getMockContentResolver().query(Contract.FoodsUsage.CONTENT_URI, null,
                    Contract.FoodsUsage.DATE_ID_SELECTION, new String[] {dateId}, null);
            assertEquals(totalRecords - 1, c.getCount());
        } finally {
            if (c != null) c.close();
        }
   }

    public void testRefreshMustInsert() {
        final String dateId = DateUtils.dateToDateId(new Date());
        int totalRecords = -1;
        Cursor c = null;
        try {
            c = getMockContentResolver().query(Contract.FoodsUsage.CONTENT_URI, null,
                    Contract.FoodsUsage.DATE_ID_SELECTION, new String[] {dateId}, null);
            totalRecords = c.getCount();
        } finally {
            if (c != null) c.close();
        }

        final FoodsUsageEntity entity = new FoodsUsageEntity(null, dateId, Meals.getMealFromPosition(1),
                2, "some description", 3.0f);
        mManager.refresh(entity);

        try {
            c = getMockContentResolver().query(Contract.FoodsUsage.CONTENT_URI, null,
                    Contract.FoodsUsage.DATE_ID_SELECTION, new String[] {dateId}, null);
            assertEquals(totalRecords + 1, c.getCount());
        } finally {
            if (c != null) c.close();
        }
    }

    public void testRefreshWithoutDayMustInsertDay() {
        final String dateId = DateUtils.dateToDateId(new Date());
        Cursor c = null;
        try {
            c = getMockContentResolver().query(Contract.Days.CONTENT_URI, null,
                    Contract.Days.DATE_ID_SELECTION, new String[] {dateId}, null);
            assertEquals(0, c.getCount());
        } finally {
            if (c != null) c.close();
        }

        final FoodsUsageEntity entity = new FoodsUsageEntity(null, dateId, Meals.getMealFromPosition(1),
                2, "some description", 3.0f);
        mManager.refresh(entity);

        try {
            c = getMockContentResolver().query(Contract.Days.CONTENT_URI, null,
                    Contract.Days.DATE_ID_SELECTION, new String[] {dateId}, null);
            assertEquals(1, c.getCount());
        } finally {
            if (c != null) c.close();
        }
    }

    public void testRefreshWithDayMustNotInsertDay() {
        final String dateId = DateUtils.dateToDateId(new Date());
        final DaysEntity daysEntity = new DaysEntity(null, dateId,
                0.0f, 1, 2, 3.0f, 4, 5, 6.0f, 7, 8, 9.0f, 10, 11, 12.0f, 13, 14, 15.0f, 16, 17, 18.0f,
                19.0f, 20, 21, 22, 23, 24, 25, "some note");
        new DaysManager(getMockContext()).refresh(daysEntity);

        Cursor c = null;
        try {
            c = getMockContentResolver().query(Contract.Days.CONTENT_URI, null,
                    Contract.Days.DATE_ID_SELECTION, new String[] {dateId}, null);
            assertEquals(1, c.getCount());
        } finally {
            if (c != null) c.close();
        }

        final FoodsUsageEntity entity = new FoodsUsageEntity(null, dateId, Meals.getMealFromPosition(1),
                2, "some description", 3.0f);
        mManager.refresh(entity);

        try {
            c = getMockContentResolver().query(Contract.Days.CONTENT_URI, null,
                    Contract.Days.DATE_ID_SELECTION, new String[] {dateId}, null);
            assertEquals(1, c.getCount());
        } finally {
            if (c != null) c.close();
        }
    }

    public void testRefreshWithDateChangeMustDeleteAndInsertRecord() {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        final FoodsUsageEntity entity = new FoodsUsageEntity(null, DateUtils.dateToDateId(cal.getTime()),
                Meals.getMealFromPosition(1), 2, "some description", 3.0f);
        mManager.refresh(entity);
        final Long oldId = entity.getId();

        cal.add(Calendar.DAY_OF_MONTH, 1);
        entity.setDateId(DateUtils.dateToDateId(cal.getTime()));

        mManager.refresh(entity);

        assertTrue(!oldId.equals(entity.getId()));

        FoodsUsageEntity retrievedEntity = mManager.foodUsageFromId(entity.getId());
        assertEquals(entity, retrievedEntity);
    }

    public void testRefreshWithoutDateChangeMustPreserveRecord() {
        final String dateId = DateUtils.dateToDateId(new Date());
        final FoodsUsageEntity entity = new FoodsUsageEntity(null, dateId, Meals.getMealFromPosition(1),
                2, "some description", 3.0f);
        mManager.refresh(entity);

        final Long oldId = entity.getId();

        entity.setMeal(Meals.getMealFromPosition(2));
        entity.setTime(20);
        entity.setDescription("some description modified");
        entity.setValue(30.0f);

        mManager.refresh(entity);

        assertEquals(oldId, entity.getId());

        FoodsUsageEntity retrievedEntity = mManager.foodUsageFromId(oldId);
        assertEquals(entity, retrievedEntity);
    }

    public void testRefreshWithInvalidEntityMustThrow() {
        FoodsUsageEntity invalidEntity = new FoodsUsageEntity(null, null, null, null, null, null);
        try {
            mManager.refresh(invalidEntity);
            fail();
        } catch (Contract.TargetException e) {
            assertTrue(true);
        }
    }
}