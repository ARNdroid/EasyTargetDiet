package br.com.arndroid.etdiet.test.provider.days;

import android.test.ProviderTestCase2;

import java.util.Calendar;
import java.util.Date;

import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.Provider;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.provider.days.DaysManager;
import br.com.arndroid.etdiet.provider.parametershistory.ParametersHistoryManager;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersEntity;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersManager;
import br.com.arndroid.etdiet.util.DateUtil;

public class DaysManagerTest extends ProviderTestCase2<Provider> {

    private DaysManager mManager;

    public DaysManagerTest() {
        super(Provider.class, Contract.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        // Important: calling the base class implementation of this method
        // where the "magic" of isolation is set up:
        super.setUp();

        // Gets the manager for this test.
        mManager = new DaysManager(getMockContext());
    }

    public void testDayFromDateWithoutDataMustReturnsCorrectValues() {
        final Integer zero = 0;
        final Date date = new Date();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final DaysEntity daysEntity = mManager.dayFromDate(date);
        final WeekdayParametersEntity weekdayParametersEntity = new WeekdayParametersManager(getMockContext())
                .weekdayParametersFromWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        assertEquals(null, daysEntity.getId());
        assertEquals(DateUtil.dateToDateId(date), daysEntity.getDateId());
        assertEquals(new ParametersHistoryManager(getMockContext()).getDailyAllowanceForDate(date),
                daysEntity.getAllowed());
        assertEquals(weekdayParametersEntity.getBreakfastStartTime(), daysEntity.getBreakfastStartTime());
        assertEquals(weekdayParametersEntity.getBreakfastEndTime(), daysEntity.getBreakfastEndTime());
        assertEquals(weekdayParametersEntity.getBreakfastGoal(), daysEntity.getBreakfastGoal());
        assertEquals(weekdayParametersEntity.getBrunchStartTime(), daysEntity.getBrunchStartTime());
        assertEquals(weekdayParametersEntity.getBrunchEndTime(), daysEntity.getBrunchEndTime());
        assertEquals(weekdayParametersEntity.getBrunchGoal(), daysEntity.getBrunchGoal());
        assertEquals(weekdayParametersEntity.getLunchStartTime(), daysEntity.getLunchStartTime());
        assertEquals(weekdayParametersEntity.getLunchEndTime(), daysEntity.getLunchEndTime());
        assertEquals(weekdayParametersEntity.getLunchGoal(), daysEntity.getLunchGoal());
        assertEquals(weekdayParametersEntity.getSnackStartTime(), daysEntity.getSnackStartTime());
        assertEquals(weekdayParametersEntity.getSnackEndTime(), daysEntity.getSnackEndTime());
        assertEquals(weekdayParametersEntity.getSnackGoal(), daysEntity.getSnackGoal());
        assertEquals(weekdayParametersEntity.getDinnerStartTime(), daysEntity.getDinnerStartTime());
        assertEquals(weekdayParametersEntity.getDinnerEndTime(), daysEntity.getDinnerEndTime());
        assertEquals(weekdayParametersEntity.getDinnerGoal(), daysEntity.getDinnerGoal());
        assertEquals(weekdayParametersEntity.getSupperStartTime(), daysEntity.getSupperStartTime());
        assertEquals(weekdayParametersEntity.getSupperEndTime(), daysEntity.getSupperEndTime());
        assertEquals(weekdayParametersEntity.getSupperGoal(), daysEntity.getSupperGoal());
        assertEquals(weekdayParametersEntity.getExerciseGoal(), daysEntity.getExerciseGoal());
        assertEquals(zero, daysEntity.getLiquidDone());
        assertEquals(weekdayParametersEntity.getLiquidGoal(), daysEntity.getLiquidGoal());
        assertEquals(zero, daysEntity.getOilDone());
        assertEquals(weekdayParametersEntity.getOilGoal(), daysEntity.getOilGoal());
        assertEquals(zero, daysEntity.getSupplementDone());
        assertEquals(weekdayParametersEntity.getSupplementGoal(), daysEntity.getSupplementGoal());
        assertEquals(null, daysEntity.getNote());
    }

    public void testDayFromDateWithDataMustReturnsCorrectValues() {
        Date date = new Date();
        DaysEntity insertedEntity = new DaysEntity(null,
                DateUtil.dateToDateId(date),
                0.0f, 1, 2, 3.0f, 4, 5, 6.0f, 7, 8, 9.0f, 10, 11, 12.0f, 13, 14, 15.0f, 16, 17, 18.0f,
                19.0f, 20, 21, 22, 23, 24, 25, "some note");
        mManager.refresh(insertedEntity);

        DaysEntity retrievedEntity = mManager.dayFromDate(date);
        assertEquals(insertedEntity, retrievedEntity);

        insertedEntity.setNote("note adjusted");
        mManager.refresh(insertedEntity);
        retrievedEntity = mManager.dayFromDate(date);
        assertEquals(insertedEntity, retrievedEntity);
    }

    public void testRefreshWithInvalidEntityMustThrow() {
        DaysEntity invalidEntity = new DaysEntity(null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null);
        try {
            mManager.refresh(invalidEntity);
            fail();
        } catch (Contract.TargetException e) {
            assertTrue(true);
        }
    }
}