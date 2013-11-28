package br.com.arndroid.etdiet.test.provider.weekdayparameters;

import android.test.ProviderTestCase2;

import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.Provider;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersEntity;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersManager;

public class WeekdayParametersManagerTest extends ProviderTestCase2<Provider> {

    private WeekdayParametersManager mManager;

    public WeekdayParametersManagerTest() {
        super(Provider.class, Contract.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        // Important: calling the base class implementation of this method
        // where the "magic" of isolation is set up:
        super.setUp();

        // Gets the manager for this test.
        mManager = new WeekdayParametersManager(getMockContext());
    }

    public void testRefreshMustUpdate() {
        WeekdayParametersEntity insertedEntity = mManager.weekdayParametersFromWeekday(1);
        insertedEntity.setBreakfastStartTime(1);
        insertedEntity.setBreakfastEndTime(2);
        insertedEntity.setBreakfastGoal(3.0f);
        insertedEntity.setBrunchStartTime(4);
        insertedEntity.setBrunchEndTime(5);
        insertedEntity.setBrunchGoal(6.0f);
        insertedEntity.setLunchStartTime(7);
        insertedEntity.setLunchEndTime(8);
        insertedEntity.setLunchGoal(9.0f);
        insertedEntity.setSnackStartTime(10);
        insertedEntity.setSnackEndTime(11);
        insertedEntity.setSnackGoal(12.0f);
        insertedEntity.setDinnerStartTime(13);
        insertedEntity.setDinnerEndTime(14);
        insertedEntity.setDinnerGoal(15.0f);
        insertedEntity.setSupperStartTime(16);
        insertedEntity.setSupperEndTime(17);
        insertedEntity.setSupperGoal(18.0f);
        insertedEntity.setExerciseGoal(19.0f);
        insertedEntity.setLiquidGoal(20);
        insertedEntity.setOilGoal(21);
        insertedEntity.setSupplementGoal(22);

        mManager.refresh(insertedEntity);
        WeekdayParametersEntity retrievedEntity = mManager.weekdayParametersFromWeekday(1);
        assertEquals(insertedEntity, retrievedEntity);
    }

    public void testRefreshWithInvalidEntityMustThrow() {
        WeekdayParametersEntity invalidEntity = new WeekdayParametersEntity(null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null);
        try {
            mManager.refresh(invalidEntity);
            fail();
        } catch (Contract.TargetException e) {
            assertTrue(true);
        }
    }
}