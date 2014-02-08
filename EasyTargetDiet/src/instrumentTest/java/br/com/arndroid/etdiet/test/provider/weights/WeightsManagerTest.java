package br.com.arndroid.etdiet.test.provider.weights;

import android.database.Cursor;
import android.os.Parcel;
import android.test.ProviderTestCase2;

import java.util.Calendar;
import java.util.Date;

import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.Provider;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.provider.days.DaysManager;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.provider.weights.WeightsEntity;
import br.com.arndroid.etdiet.provider.weights.WeightsManager;
import br.com.arndroid.etdiet.utils.DateUtils;

public class WeightsManagerTest extends ProviderTestCase2<Provider> {

    private WeightsManager mManager;

    public WeightsManagerTest() {
        super(Provider.class, Contract.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        // Important: calling the base class implementation of this method
        // where the "magic" of isolation is set up:
        super.setUp();

        // Gets the manager for this test.
        mManager = new WeightsManager(getMockContext());
    }

    public void testEntityWillCauseConstraintViolationMustReturnsCorrectValue() {
        final Calendar calendar = Calendar.getInstance();

        final int time = 1;
        WeightsEntity entity = new WeightsEntity(null, DateUtils.dateToDateId(calendar.getTime()),
                time, 1.0f, null);
        assertFalse(mManager.entityWillCauseConstraintViolation(entity));

        WeightsEntity entityInDatabase = new WeightsEntity(entity);
        mManager.refresh(entityInDatabase);

        assertTrue(mManager.entityWillCauseConstraintViolation(entity));
        assertFalse(mManager.entityWillCauseConstraintViolation(entityInDatabase));

        entity.setTime(time + 1);
        assertFalse(mManager.entityWillCauseConstraintViolation(entity));

        entity.setTime(time);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        entity.setDateId(DateUtils.dateToDateId(calendar.getTime()));
        assertFalse(mManager.entityWillCauseConstraintViolation(entity));
    }

    public void testRemoveMustDelete() {
        final String dateId = DateUtils.dateToDateId(new Date());
        final Integer time = 1;
        final WeightsEntity entity = new WeightsEntity(null, dateId, time, 1.0f, "some note");
        mManager.refresh(entity);
        int totalRecords = -1;
        Cursor c = null;
        try {
            c = getMockContentResolver().query(Contract.Weights.CONTENT_URI, null,
                    Contract.Weights.DATE_ID_AND_TIME_SELECTION, new String[] {dateId, time.toString()}, null);
            totalRecords = c.getCount();
        } finally {
            if (c != null) c.close();
        }

        mManager.remove(entity.getId());

        try {
            c = getMockContentResolver().query(Contract.Weights.CONTENT_URI, null,
                    Contract.Weights.DATE_ID_AND_TIME_SELECTION, new String[] {dateId}, null);
            assertEquals(totalRecords - 1, c.getCount());
        } finally {
            if (c != null) c.close();
        }
    }

    public void testRefreshMustInsertOrUpdate() {
        final String dateId = DateUtils.dateToDateId(new Date());
        int totalRecords = -1;
        Cursor c = null;
        try {
            c = getMockContentResolver().query(Contract.Weights.CONTENT_URI, null, null, null, null);
            totalRecords = c.getCount();
        } finally {
            if (c != null) c.close();
        }

        final WeightsEntity entity = new WeightsEntity(null, dateId, 1, 2.0f, "some note");
        mManager.refresh(entity);

        try {
            c = getMockContentResolver().query(Contract.Weights.CONTENT_URI, null, null, null,
                    null);
            assertEquals(totalRecords + 1, c.getCount());
        } finally {
            if (c != null) c.close();
        }

        entity.setTime(entity.getTime() + 1);
        entity.setNote(entity.getNote() + " updated");
        mManager.refresh(entity);
        WeightsEntity entityUpdated = mManager.weightFromId(entity.getId());
        assertEquals(entity, entityUpdated);
    }
}