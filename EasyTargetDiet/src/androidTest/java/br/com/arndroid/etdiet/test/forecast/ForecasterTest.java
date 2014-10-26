package br.com.arndroid.etdiet.test.forecast;

import junit.framework.TestCase;

import java.util.Calendar;
import java.util.Date;

import br.com.arndroid.etdiet.forecast.ForecastEntity;
import br.com.arndroid.etdiet.forecast.Forecaster;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.virtualweek.DaySummary;
import br.com.arndroid.etdiet.virtualweek.UsageSummary;

public class ForecasterTest extends TestCase {

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

    public void testForecastWithEmptyDayMustBeGreen() {

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

        final ForecastEntity forecastEntity = mForecaster.forecast(new Date(), summary);
        assertEquals(ForecastEntity.STRAIGHT_TO_GOAL, forecastEntity.getForecastType());
    }

    public void testForecastGreenUseMustBeGreen() {

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

        ForecastEntity forecastEntity = mForecaster.forecast(new Date(), summary);
        assertEquals(ForecastEntity.STRAIGHT_TO_GOAL, forecastEntity.getForecastType());

        greenSummary.setDinnerUsed(8.0f);
        greenSummary.setSupperUsed(1.5f);
        forecastEntity = mForecaster.forecast(new Date(), summary);
        assertEquals(ForecastEntity.STRAIGHT_TO_GOAL, forecastEntity.getForecastType());
    }

    public void testForecastBlueUseMustBeBlue() {

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

        ForecastEntity forecastEntity = mForecaster.forecast(new Date(), summary);
        assertEquals(ForecastEntity.GOING_TO_GOAL_WITH_HELP, forecastEntity.getForecastType());

        blueSummary.setSupperUsed(6.5f);
        forecastEntity = mForecaster.forecast(new Date(), summary);
        assertEquals(ForecastEntity.GOING_TO_GOAL_WITH_HELP, forecastEntity.getForecastType());
    }

    public void testForecastRedUseMustBeRed() {

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

        ForecastEntity forecastEntity = mForecaster.forecast(new Date(), summary);
        assertEquals(ForecastEntity.OUT_OF_GOAL, forecastEntity.getForecastType());

        redSummary.setSupperUsed(999f);
        forecastEntity = mForecaster.forecast(new Date(), summary);
        assertEquals(ForecastEntity.OUT_OF_GOAL, forecastEntity.getForecastType());
    }

    public void testForecastYellowUseMustReturnCorrectValues() {

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

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        ForecastEntity forecastEntity = mForecaster.forecast(calendar.getTime(), summary);
        assertEquals(ForecastEntity.OUT_OF_GOAL_BUT_CAN_RETURN, forecastEntity.getForecastType());

        calendar.set(Calendar.HOUR_OF_DAY, 14);
        forecastEntity = mForecaster.forecast(calendar.getTime(), summary);
        assertEquals(ForecastEntity.STRAIGHT_TO_GOAL, forecastEntity.getForecastType());

        yellowSummary.setLunchUsed(10.0f);
        forecastEntity = mForecaster.forecast(calendar.getTime(), summary);
        assertEquals(ForecastEntity.OUT_OF_GOAL_BUT_CAN_RETURN, forecastEntity.getForecastType());

        calendar.set(Calendar.HOUR_OF_DAY, 18);
        yellowSummary.setSnackUsed(2.0f);
        yellowSummary.setDinnerUsed(8.0f);
        forecastEntity = mForecaster.forecast(calendar.getTime(), summary);
        assertEquals(ForecastEntity.OUT_OF_GOAL_BUT_CAN_RETURN, forecastEntity.getForecastType());

        calendar.set(Calendar.HOUR_OF_DAY, 20);
        forecastEntity = mForecaster.forecast(calendar.getTime(), summary);
        assertEquals(ForecastEntity.STRAIGHT_TO_GOAL, forecastEntity.getForecastType());

        yellowSummary.setSupperUsed(1.5f);
        forecastEntity = mForecaster.forecast(calendar.getTime(), summary);
        assertEquals(ForecastEntity.OUT_OF_GOAL, forecastEntity.getForecastType());
    }
}
