package br.com.arndroid.etdiet.test.forecast;

import junit.framework.TestCase;

import java.util.Calendar;
import java.util.Date;

import br.com.arndroid.etdiet.forecast.ForecastEntity;
import br.com.arndroid.etdiet.forecast.Forecaster;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.virtualweek.DaySummary;
import br.com.arndroid.etdiet.virtualweek.SettingsValues;
import br.com.arndroid.etdiet.virtualweek.UsageSummary;
import br.com.arndroid.etdiet.virtualweek.VirtualWeek;
import br.com.arndroid.etdiet.virtualweek.VirtualWeekEngine;

public class ForecasterTest extends TestCase {

    /**
     * We have four scenes here (see issue #144 for background knowledge):
     * - Scene 1: planned > daily allowance and reserves (weekly allowance + exercises) will cover
     * - Scene 2: planned > daily allowance but reserves won't cover
     * - Scene 3: planned <= daily allowance
     * - Scene 4: planned == daily allowance (actually sub scene from scene 3,
     *                       but the most common scenario. We'll cover it with specific tests)
     */

    private static final int EIGHT_HOURS = 28800000;
    private static final int TEN_HOURS = 36000000;
    private static final int TWELVE_HOURS = 43200000;
    private static final int FOURTEEN_HOURS = 50400000;
    private static final int SIXTEEN_HOURS = 57600000;
    private static final int EIGHTEEN_HOURS = 64800000;
    private static final int TWENTY_HOURS = 72000000;

    private Forecaster mForecaster;

    @Override
    protected void setUp() throws Exception {
        // Gets the advisor helper for this test.
        mForecaster = Forecaster.getInstance();
    }

    public void testScene4ForecastWithEmptyDayMustBeGreen() {

        final DaysEntity commonDaysEntity = new DaysEntity(null, null, 26.0f,
                EIGHT_HOURS, TEN_HOURS, 4.0f,
                TEN_HOURS, TWELVE_HOURS, 0.5f,
                TWELVE_HOURS, FOURTEEN_HOURS, 10.0f,
                FOURTEEN_HOURS, SIXTEEN_HOURS, 2.0f,
                SIXTEEN_HOURS, EIGHTEEN_HOURS, 8.0f,
                EIGHTEEN_HOURS, TWENTY_HOURS, 1.5f,
                null, null, null, null, null, null, null, null);

        final UsageSummary emptyUsageSummary = new UsageSummary();
        emptyUsageSummary.setBreakfastUsed(0);
        emptyUsageSummary.setBrunchUsed(0);
        emptyUsageSummary.setLunchUsed(0);
        emptyUsageSummary.setSnackUsed(0);
        emptyUsageSummary.setDinnerUsed(0);
        emptyUsageSummary.setSupperUsed(0);

        final DaySummary summary = new DaySummary();
        summary.setWeeklyAllowanceBeforeUsage(49.0f);
        summary.setTotalExercise(0.0f);
        summary.setEntity(commonDaysEntity);
        summary.setUsage(emptyUsageSummary);
        summary.setSettingsValues(new SettingsValues(
                Contract.ParametersHistory.EXERCISE_USE_MODE_USE_AND_ACCUMULATE,
                Contract.ParametersHistory.EXERCISE_USE_ORDER_USE_EXERCISES_FIRST));

        VirtualWeekEngine.computeSummary(summary);
        ForecastEntity forecastEntity = mForecaster.forecast(new Date(), summary);
        assertEquals(ForecastEntity.STRAIGHT_TO_GOAL, forecastEntity.getForecastType());
    }

    public void testScene4ForecastGreenUseMustBeGreen() {

        final DaysEntity commonDaysEntity = new DaysEntity(null, null, 26.0f,
                EIGHT_HOURS, TEN_HOURS, 4.0f,
                TEN_HOURS, TWELVE_HOURS, 0.5f,
                TWELVE_HOURS, FOURTEEN_HOURS, 10.0f,
                FOURTEEN_HOURS, SIXTEEN_HOURS, 2.0f,
                SIXTEEN_HOURS, EIGHTEEN_HOURS, 8.0f,
                EIGHTEEN_HOURS, TWENTY_HOURS, 1.5f,
                null, null, null, null, null, null, null, null);

        final UsageSummary greenSummary = new UsageSummary();
        greenSummary.setBreakfastUsed(4.0f);
        greenSummary.setBrunchUsed(0.5f);
        greenSummary.setLunchUsed(10.0f);
        greenSummary.setSnackUsed(2.0f);
        greenSummary.setDinnerUsed(0.0f);
        greenSummary.setSupperUsed(0.0f);

        final DaySummary summary = new DaySummary();
        summary.setWeeklyAllowanceBeforeUsage(49.0f);
        summary.setTotalExercise(0.0f);
        summary.setEntity(commonDaysEntity);
        summary.setUsage(greenSummary);
        summary.setSettingsValues(new SettingsValues(
                Contract.ParametersHistory.EXERCISE_USE_MODE_USE_AND_ACCUMULATE,
                Contract.ParametersHistory.EXERCISE_USE_ORDER_USE_EXERCISES_FIRST));

        VirtualWeekEngine.computeSummary(summary);
        ForecastEntity forecastEntity = mForecaster.forecast(new Date(), summary);
        assertEquals(ForecastEntity.STRAIGHT_TO_GOAL, forecastEntity.getForecastType());

        greenSummary.setDinnerUsed(8.0f);
        greenSummary.setSupperUsed(1.5f);
        VirtualWeekEngine.computeSummary(summary);
        forecastEntity = mForecaster.forecast(new Date(), summary);
        assertEquals(ForecastEntity.STRAIGHT_TO_GOAL, forecastEntity.getForecastType());
    }

    public void testScene4ForecastBlueUseMustBeBlue() {

        final DaysEntity commonDaysEntity = new DaysEntity(null, null, 26.0f,
                EIGHT_HOURS, TEN_HOURS, 4.0f,
                TEN_HOURS, TWELVE_HOURS, 0.5f,
                TWELVE_HOURS, FOURTEEN_HOURS, 10.0f,
                FOURTEEN_HOURS, SIXTEEN_HOURS, 2.0f,
                SIXTEEN_HOURS, EIGHTEEN_HOURS, 8.0f,
                EIGHTEEN_HOURS, TWENTY_HOURS, 1.5f,
                null, null, null, null, null, null, null, null);

        final UsageSummary blueSummary = new UsageSummary();
        blueSummary.setBreakfastUsed(4.0f);
        blueSummary.setBrunchUsed(0.5f);
        blueSummary.setLunchUsed(10.0f);
        blueSummary.setSnackUsed(2.0f);
        blueSummary.setDinnerUsed(8.0f);
        blueSummary.setSupperUsed(2.5f);

        final DaySummary summary = new DaySummary();
        summary.setWeeklyAllowanceBeforeUsage(3.0f);
        summary.setTotalExercise(2.0f);
        summary.setEntity(commonDaysEntity);
        summary.setUsage(blueSummary);
        summary.setSettingsValues(new SettingsValues(
                Contract.ParametersHistory.EXERCISE_USE_MODE_USE_AND_ACCUMULATE,
                Contract.ParametersHistory.EXERCISE_USE_ORDER_USE_EXERCISES_FIRST));

        VirtualWeekEngine.computeSummary(summary);
        ForecastEntity forecastEntity = mForecaster.forecast(new Date(), summary);
        assertEquals(ForecastEntity.OUT_OF_GOAL_WITH_ENOUGH_RESERVES, forecastEntity.getForecastType());

        blueSummary.setSupperUsed(6.5f);
        VirtualWeekEngine.computeSummary(summary);
        forecastEntity = mForecaster.forecast(new Date(), summary);
        assertEquals(ForecastEntity.OUT_OF_GOAL_WITH_ENOUGH_RESERVES, forecastEntity.getForecastType());
    }

    public void testScene4ForecastRedUseMustBeRed() {

        final DaysEntity commonDaysEntity = new DaysEntity(null, null, 26.0f,
                EIGHT_HOURS, TEN_HOURS, 4.0f,
                TEN_HOURS, TWELVE_HOURS, 0.5f,
                TWELVE_HOURS, FOURTEEN_HOURS, 10.0f,
                FOURTEEN_HOURS, SIXTEEN_HOURS, 2.0f,
                SIXTEEN_HOURS, EIGHTEEN_HOURS, 8.0f,
                EIGHTEEN_HOURS, TWENTY_HOURS, 1.5f,
                null, null, null, null, null, null, null, null);

        final UsageSummary redSummary = new UsageSummary();
        redSummary.setBreakfastUsed(4.0f);
        redSummary.setBrunchUsed(0.5f);
        redSummary.setLunchUsed(10.0f);
        redSummary.setSnackUsed(2.0f);
        redSummary.setDinnerUsed(8.0f);
        redSummary.setSupperUsed(7.0f);

        final DaySummary summary = new DaySummary();
        summary.setWeeklyAllowanceBeforeUsage(3.0f);
        summary.setTotalExercise(2.0f);
        summary.setEntity(commonDaysEntity);
        summary.setUsage(redSummary);
        summary.setSettingsValues(new SettingsValues(
                Contract.ParametersHistory.EXERCISE_USE_MODE_USE_AND_ACCUMULATE,
                Contract.ParametersHistory.EXERCISE_USE_ORDER_USE_EXERCISES_FIRST));

        VirtualWeekEngine.computeSummary(summary);
        ForecastEntity forecastEntity = mForecaster.forecast(new Date(), summary);
        assertEquals(ForecastEntity.OUT_OF_GOAL, forecastEntity.getForecastType());

        redSummary.setSupperUsed(999f);
        VirtualWeekEngine.computeSummary(summary);
        forecastEntity = mForecaster.forecast(new Date(), summary);
        assertEquals(ForecastEntity.OUT_OF_GOAL, forecastEntity.getForecastType());
    }

    public void testScene4ForecastYellowUseMustReturnCorrectValues() {

        final DaysEntity commonDaysEntity = new DaysEntity(null, null, 26.0f,
                EIGHT_HOURS, TEN_HOURS, 4.0f,
                TEN_HOURS, TWELVE_HOURS, 0.5f,
                TWELVE_HOURS, FOURTEEN_HOURS, 10.0f,
                FOURTEEN_HOURS, SIXTEEN_HOURS, 2.0f,
                SIXTEEN_HOURS, EIGHTEEN_HOURS, 8.0f,
                EIGHTEEN_HOURS, TWENTY_HOURS, 1.5f,
                null, null, null, null, null, null, null, null);

        final UsageSummary yellowSummary = new UsageSummary();
        yellowSummary.setBreakfastUsed(5.0f);
        yellowSummary.setBrunchUsed(0.0f);
        yellowSummary.setLunchUsed(0.0f);
        yellowSummary.setSnackUsed(0.0f);
        yellowSummary.setDinnerUsed(0.0f);
        yellowSummary.setSupperUsed(0.0f);

        final DaySummary summary = new DaySummary();
        summary.setWeeklyAllowanceBeforeUsage(0.0f);
        summary.setTotalExercise(0.0f);
        summary.setEntity(commonDaysEntity);
        summary.setUsage(yellowSummary);
        summary.setSettingsValues(new SettingsValues(
                Contract.ParametersHistory.EXERCISE_USE_MODE_USE_AND_ACCUMULATE,
                Contract.ParametersHistory.EXERCISE_USE_ORDER_USE_EXERCISES_FIRST));

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        VirtualWeekEngine.computeSummary(summary);
        ForecastEntity forecastEntity = mForecaster.forecast(calendar.getTime(), summary);
        assertEquals(ForecastEntity.OUT_OF_GOAL_BUT_CAN_RETURN, forecastEntity.getForecastType());

        calendar.set(Calendar.HOUR_OF_DAY, 14);
        VirtualWeekEngine.computeSummary(summary);
        forecastEntity = mForecaster.forecast(calendar.getTime(), summary);
        assertEquals(ForecastEntity.STRAIGHT_TO_GOAL, forecastEntity.getForecastType());

        yellowSummary.setLunchUsed(10.0f);
        VirtualWeekEngine.computeSummary(summary);
        forecastEntity = mForecaster.forecast(calendar.getTime(), summary);
        assertEquals(ForecastEntity.OUT_OF_GOAL_BUT_CAN_RETURN, forecastEntity.getForecastType());

        calendar.set(Calendar.HOUR_OF_DAY, 18);
        yellowSummary.setSnackUsed(2.0f);
        yellowSummary.setDinnerUsed(8.0f);
        VirtualWeekEngine.computeSummary(summary);
        forecastEntity = mForecaster.forecast(calendar.getTime(), summary);
        assertEquals(ForecastEntity.OUT_OF_GOAL_BUT_CAN_RETURN, forecastEntity.getForecastType());

        calendar.set(Calendar.HOUR_OF_DAY, 20);
        VirtualWeekEngine.computeSummary(summary);
        forecastEntity = mForecaster.forecast(calendar.getTime(), summary);
        assertEquals(ForecastEntity.STRAIGHT_TO_GOAL, forecastEntity.getForecastType());

        yellowSummary.setSupperUsed(1.5f);
        VirtualWeekEngine.computeSummary(summary);
        forecastEntity = mForecaster.forecast(calendar.getTime(), summary);
        assertEquals(ForecastEntity.OUT_OF_GOAL, forecastEntity.getForecastType());
    }

    public void testScene4WithExerciseSettingsMustReturnCorrectValues() {
        final DaysEntity commonDaysEntity = new DaysEntity(null, null, 26.0f,
                EIGHT_HOURS, TEN_HOURS, 4.0f,
                TEN_HOURS, TWELVE_HOURS, 0.5f,
                TWELVE_HOURS, FOURTEEN_HOURS, 10.0f,
                FOURTEEN_HOURS, SIXTEEN_HOURS, 2.0f,
                SIXTEEN_HOURS, EIGHTEEN_HOURS, 8.0f,
                EIGHTEEN_HOURS, TWENTY_HOURS, 1.5f,
                null, null, null, null, null, null, null, null);

        final UsageSummary someSummary = new UsageSummary();
        someSummary.setBreakfastUsed(5.0f);
        someSummary.setBrunchUsed(0.0f);
        someSummary.setLunchUsed(0.0f);
        someSummary.setSnackUsed(0.0f);
        someSummary.setDinnerUsed(0.0f);
        someSummary.setSupperUsed(0.0f);

        final DaySummary summary = new DaySummary();
        summary.setWeeklyAllowanceBeforeUsage(0.0f);
        summary.setTotalExercise(10.0f);
        summary.setEntity(commonDaysEntity);
        summary.setUsage(someSummary);
        summary.setSettingsValues(new SettingsValues(
                Contract.ParametersHistory.EXERCISE_USE_MODE_USE_DONT_ACCUMULATE,
                Contract.ParametersHistory.EXERCISE_USE_ORDER_USE_EXERCISES_FIRST));

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        VirtualWeekEngine.computeSummary(summary);
        ForecastEntity forecastEntity = mForecaster.forecast(calendar.getTime(), summary);
        assertEquals(ForecastEntity.OUT_OF_GOAL_WITH_ENOUGH_RESERVES, forecastEntity.getForecastType());

        summary.getSettingsValues().setExerciseUseMode(Contract.ParametersHistory.EXERCISE_USE_MODE_DONT_USE);
        VirtualWeekEngine.computeSummary(summary);
        forecastEntity = mForecaster.forecast(calendar.getTime(), summary);
        assertEquals(ForecastEntity.OUT_OF_GOAL_BUT_CAN_RETURN, forecastEntity.getForecastType());
    }

    public void testScene1MustReturnCorrectValues() {
        final DaysEntity plannedGreaterAllowedDaysEntity = new DaysEntity(null, null, 26.0f,
                EIGHT_HOURS, TEN_HOURS, 4.0f,
                TEN_HOURS, TWELVE_HOURS, 0.5f,
                TWELVE_HOURS, FOURTEEN_HOURS, 10.0f,
                FOURTEEN_HOURS, SIXTEEN_HOURS, 2.0f,
                SIXTEEN_HOURS, EIGHTEEN_HOURS, 9.0f,
                EIGHTEEN_HOURS, TWENTY_HOURS, 1.5f,
                null, null, null, null, null, null, null, null);

        final UsageSummary someUsage = new UsageSummary();
        someUsage.setBreakfastUsed(0.0f);
        someUsage.setBrunchUsed(0.0f);
        someUsage.setLunchUsed(0.0f);
        someUsage.setSnackUsed(0.0f);
        someUsage.setDinnerUsed(0.0f);
        someUsage.setSupperUsed(0.0f);

        final DaySummary withReservesDaySummary = new DaySummary();
        withReservesDaySummary.setWeeklyAllowanceBeforeUsage(0.0f);
        withReservesDaySummary.setTotalExercise(2.0f);
        withReservesDaySummary.setEntity(plannedGreaterAllowedDaysEntity);
        withReservesDaySummary.setUsage(someUsage);
        withReservesDaySummary.setSettingsValues(new SettingsValues(
                Contract.ParametersHistory.EXERCISE_USE_MODE_USE_DONT_ACCUMULATE,
                Contract.ParametersHistory.EXERCISE_USE_ORDER_USE_EXERCISES_FIRST));
        VirtualWeekEngine.computeSummary(withReservesDaySummary);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        ForecastEntity forecastEntity = mForecaster.forecast(calendar.getTime(), withReservesDaySummary);
        assertEquals(ForecastEntity.STRAIGHT_TO_GOAL, forecastEntity.getForecastType());

        someUsage.setBreakfastUsed(4.0f);
        VirtualWeekEngine.computeSummary(withReservesDaySummary);
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        forecastEntity = mForecaster.forecast(calendar.getTime(), withReservesDaySummary);
        assertEquals(ForecastEntity.STRAIGHT_TO_GOAL, forecastEntity.getForecastType());

        someUsage.setBreakfastUsed(5.0f);
        VirtualWeekEngine.computeSummary(withReservesDaySummary);
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        forecastEntity = mForecaster.forecast(calendar.getTime(), withReservesDaySummary);
        assertEquals(ForecastEntity.OUT_OF_GOAL_WITH_ENOUGH_RESERVES, forecastEntity.getForecastType());

        someUsage.setBreakfastUsed(6.0f);
        VirtualWeekEngine.computeSummary(withReservesDaySummary);
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        forecastEntity = mForecaster.forecast(calendar.getTime(), withReservesDaySummary);
        assertEquals(ForecastEntity.OUT_OF_GOAL_BUT_CAN_RETURN, forecastEntity.getForecastType());

        someUsage.setBreakfastUsed(29.0f);
        VirtualWeekEngine.computeSummary(withReservesDaySummary);
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        forecastEntity = mForecaster.forecast(calendar.getTime(), withReservesDaySummary);
        assertEquals(ForecastEntity.OUT_OF_GOAL, forecastEntity.getForecastType());
    }

    public void testScene2MustReturnCorrectValues() {
        final DaysEntity plannedGreaterAllowedDaysEntity = new DaysEntity(null, null, 26.0f,
                EIGHT_HOURS, TEN_HOURS, 4.0f,
                TEN_HOURS, TWELVE_HOURS, 0.5f,
                TWELVE_HOURS, FOURTEEN_HOURS, 10.0f,
                FOURTEEN_HOURS, SIXTEEN_HOURS, 2.0f,
                SIXTEEN_HOURS, EIGHTEEN_HOURS, 9.0f,
                EIGHTEEN_HOURS, TWENTY_HOURS, 1.5f,
                null, null, null, null, null, null, null, null);

        final UsageSummary someUsage = new UsageSummary();
        someUsage.setBreakfastUsed(0.0f);
        someUsage.setBrunchUsed(0.0f);
        someUsage.setLunchUsed(0.0f);
        someUsage.setSnackUsed(0.0f);
        someUsage.setDinnerUsed(0.0f);
        someUsage.setSupperUsed(0.0f);

        final DaySummary withoutReservesDaySummary = new DaySummary();
        withoutReservesDaySummary.setWeeklyAllowanceBeforeUsage(0.0f);
        withoutReservesDaySummary.setTotalExercise(0.0f);
        withoutReservesDaySummary.setEntity(plannedGreaterAllowedDaysEntity);
        withoutReservesDaySummary.setUsage(someUsage);
        withoutReservesDaySummary.setSettingsValues(new SettingsValues(
                Contract.ParametersHistory.EXERCISE_USE_MODE_USE_DONT_ACCUMULATE,
                Contract.ParametersHistory.EXERCISE_USE_ORDER_USE_EXERCISES_FIRST));
        VirtualWeekEngine.computeSummary(withoutReservesDaySummary);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        ForecastEntity forecastEntity = mForecaster.forecast(calendar.getTime(), withoutReservesDaySummary);
        assertEquals(ForecastEntity.OUT_OF_GOAL_BUT_CAN_RETURN, forecastEntity.getForecastType());

        someUsage.setBreakfastUsed(4.0f);
        VirtualWeekEngine.computeSummary(withoutReservesDaySummary);
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        forecastEntity = mForecaster.forecast(calendar.getTime(), withoutReservesDaySummary);
        assertEquals(ForecastEntity.OUT_OF_GOAL_BUT_CAN_RETURN, forecastEntity.getForecastType());

        someUsage.setBreakfastUsed(5.0f);
        VirtualWeekEngine.computeSummary(withoutReservesDaySummary);
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        forecastEntity = mForecaster.forecast(calendar.getTime(), withoutReservesDaySummary);
        assertEquals(ForecastEntity.OUT_OF_GOAL_BUT_CAN_RETURN, forecastEntity.getForecastType());

        someUsage.setBreakfastUsed(6.0f);
        VirtualWeekEngine.computeSummary(withoutReservesDaySummary);
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        forecastEntity = mForecaster.forecast(calendar.getTime(), withoutReservesDaySummary);
        assertEquals(ForecastEntity.OUT_OF_GOAL_BUT_CAN_RETURN, forecastEntity.getForecastType());

        someUsage.setBreakfastUsed(29.0f);
        VirtualWeekEngine.computeSummary(withoutReservesDaySummary);
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        forecastEntity = mForecaster.forecast(calendar.getTime(), withoutReservesDaySummary);
        assertEquals(ForecastEntity.OUT_OF_GOAL, forecastEntity.getForecastType());
    }

    public void testScene3MustReturnCorrectValues() {
        final DaysEntity plannedLowerAllowedDaysEntity = new DaysEntity(null, null, 26.0f,
                EIGHT_HOURS, TEN_HOURS, 4.0f,
                TEN_HOURS, TWELVE_HOURS, 0.5f,
                TWELVE_HOURS, FOURTEEN_HOURS, 10.0f,
                FOURTEEN_HOURS, SIXTEEN_HOURS, 2.0f,
                SIXTEEN_HOURS, EIGHTEEN_HOURS, 7.0f,
                EIGHTEEN_HOURS, TWENTY_HOURS, 1.5f,
                null, null, null, null, null, null, null, null);

        final UsageSummary someUsage = new UsageSummary();
        someUsage.setBreakfastUsed(0.0f);
        someUsage.setBrunchUsed(0.0f);
        someUsage.setLunchUsed(0.0f);
        someUsage.setSnackUsed(0.0f);
        someUsage.setDinnerUsed(0.0f);
        someUsage.setSupperUsed(0.0f);

        final DaySummary withReservesDaySummary = new DaySummary();
        withReservesDaySummary.setWeeklyAllowanceBeforeUsage(0.0f);
        withReservesDaySummary.setTotalExercise(2.0f);
        withReservesDaySummary.setEntity(plannedLowerAllowedDaysEntity);
        withReservesDaySummary.setUsage(someUsage);
        withReservesDaySummary.setSettingsValues(new SettingsValues(
                Contract.ParametersHistory.EXERCISE_USE_MODE_USE_DONT_ACCUMULATE,
                Contract.ParametersHistory.EXERCISE_USE_ORDER_USE_EXERCISES_FIRST));
        VirtualWeekEngine.computeSummary(withReservesDaySummary);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        ForecastEntity forecastEntity = mForecaster.forecast(calendar.getTime(), withReservesDaySummary);
        assertEquals(ForecastEntity.STRAIGHT_TO_GOAL, forecastEntity.getForecastType());

        someUsage.setBreakfastUsed(4.0f);
        VirtualWeekEngine.computeSummary(withReservesDaySummary);
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        forecastEntity = mForecaster.forecast(calendar.getTime(), withReservesDaySummary);
        assertEquals(ForecastEntity.STRAIGHT_TO_GOAL, forecastEntity.getForecastType());

        someUsage.setBreakfastUsed(6.0f);
        VirtualWeekEngine.computeSummary(withReservesDaySummary);
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        forecastEntity = mForecaster.forecast(calendar.getTime(), withReservesDaySummary);
        assertEquals(ForecastEntity.OUT_OF_GOAL_WITH_ENOUGH_RESERVES, forecastEntity.getForecastType());

        someUsage.setBreakfastUsed(7.0f);
        VirtualWeekEngine.computeSummary(withReservesDaySummary);
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        forecastEntity = mForecaster.forecast(calendar.getTime(), withReservesDaySummary);
        assertEquals(ForecastEntity.OUT_OF_GOAL_WITH_ENOUGH_RESERVES, forecastEntity.getForecastType());

        someUsage.setBreakfastUsed(8.0f);
        VirtualWeekEngine.computeSummary(withReservesDaySummary);
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        forecastEntity = mForecaster.forecast(calendar.getTime(), withReservesDaySummary);
        assertEquals(ForecastEntity.OUT_OF_GOAL_BUT_CAN_RETURN, forecastEntity.getForecastType());

        someUsage.setBreakfastUsed(29.0f);
        VirtualWeekEngine.computeSummary(withReservesDaySummary);
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        forecastEntity = mForecaster.forecast(calendar.getTime(), withReservesDaySummary);
        assertEquals(ForecastEntity.OUT_OF_GOAL, forecastEntity.getForecastType());
    }
}
