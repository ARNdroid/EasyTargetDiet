package br.com.arndroid.etdiet.test.meals;

import android.test.ProviderTestCase2;

import java.util.Calendar;
import java.util.Date;

import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.Provider;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageManager;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersEntity;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersManager;
import br.com.arndroid.etdiet.utils.DateUtils;

public class MealsTest extends ProviderTestCase2<Provider> {

    private static final int NO_TIME = -1;
    private static final int ONE_MINUTES = 60000;
    private static final int SIX_HOURS = 21600000;
    private static final int SEVEN_HOURS = 25200000;
    private static final int EIGHT_HOURS = 28800000;
    private static final int NINE_HOURS = 32400000;
    private static final int TEN_HOURS = 36000000;
    private static final int ELEVEN_HOURS = 39600000;
    private static final int TWELVE_HOURS = 43200000;
    private static final int THIRTEEN_HOURS = 46800000;
    private static final int FOURTEEN_HOURS = 50400000;
    private static final int FIFTEEN_HOURS = 54000000;
    private static final int SIXTEEN_HOURS = 57600000;
    private static final int EIGHTEEN_HOURS = 64800000;
    private static final int NINETEEN_HOURS = 68400000;
    private static final int TWENTY_HOURS = 72000000;
    private static final int TWENTY_ONE_HOURS = 75600000;
    private static final int TWENTY_TWO_HOURS = 79200000;

    private FoodsUsageManager mFoodsUsageManager;
    private WeekdayParametersManager mWeekdayParametersManager;

    public MealsTest() {
        super(Provider.class, Contract.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        // Important: calling the base class implementation of this method
        // where the "magic" of isolation is set up:
        super.setUp();

        // Gets the manager for this test.
        mFoodsUsageManager = new FoodsUsageManager(getMockContext());
        mWeekdayParametersManager = new WeekdayParametersManager(getMockContext());
    }

    public void testPreferredMealForTimeInDateWithOneInPeriodMustReturnCorrectValues() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        WeekdayParametersEntity common = mWeekdayParametersManager.weekdayParametersFromWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        common.setBreakfastStartTime(EIGHT_HOURS);
        common.setBreakfastEndTime(TEN_HOURS);
        common.setBrunchStartTime(TEN_HOURS);
        common.setBrunchEndTime(TWELVE_HOURS);
        common.setLunchStartTime(TWELVE_HOURS);
        common.setLunchEndTime(FOURTEEN_HOURS);
        common.setSnackStartTime(FOURTEEN_HOURS);
        common.setSnackEndTime(SIXTEEN_HOURS);
        common.setDinnerStartTime(SIXTEEN_HOURS);
        common.setDinnerEndTime(EIGHTEEN_HOURS);
        common.setSupperStartTime(EIGHTEEN_HOURS);
        common.setSupperEndTime(TWENTY_HOURS);
        mWeekdayParametersManager.refresh(common);

        assertEquals(Meals.SNACK, Meals.preferredMealForTimeInDate(getMockContext(), FIFTEEN_HOURS,
                calendar.getTime(), false));
    }

    public void testPreferredMealForTimeInDateWithZeroInPeriodAndOneClosestWithZeroPointsMustReturnCorrectValues() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        final String dateId = DateUtils.dateToDateId(calendar.getTime());

        WeekdayParametersEntity equalsSpaced = mWeekdayParametersManager.weekdayParametersFromWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        equalsSpaced.setBreakfastStartTime(SIX_HOURS);
        equalsSpaced.setBreakfastEndTime(SEVEN_HOURS);
        equalsSpaced.setBrunchStartTime(NINE_HOURS);
        equalsSpaced.setBrunchEndTime(TEN_HOURS);
        equalsSpaced.setLunchStartTime(TWELVE_HOURS);
        equalsSpaced.setLunchEndTime(THIRTEEN_HOURS);
        equalsSpaced.setSnackStartTime(FIFTEEN_HOURS);
        equalsSpaced.setSnackEndTime(SIXTEEN_HOURS);
        equalsSpaced.setDinnerStartTime(EIGHTEEN_HOURS);
        equalsSpaced.setDinnerEndTime(NINETEEN_HOURS);
        equalsSpaced.setSupperStartTime(TWENTY_ONE_HOURS);
        equalsSpaced.setSupperEndTime(TWENTY_TWO_HOURS);
        mWeekdayParametersManager.refresh(equalsSpaced);

        FoodsUsageEntity food = new FoodsUsageEntity(null, dateId, Meals.BRUNCH, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);

        assertEquals(Meals.BREAKFAST, Meals.preferredMealForTimeInDate(getMockContext(), EIGHT_HOURS,
                calendar.getTime(), false));
        assertEquals(Meals.LUNCH, Meals.preferredMealForTimeInDate(getMockContext(), ELEVEN_HOURS,
                calendar.getTime(), false));
    }

    public void testPreferredMealForTimeInDateWithZeroInPeriodAndZeroClosestWithZeroPointsAndOneClosestMustReturnCorrectValues() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        final String dateId = DateUtils.dateToDateId(calendar.getTime());

        WeekdayParametersEntity equalsSpaced = mWeekdayParametersManager.weekdayParametersFromWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        equalsSpaced.setBreakfastStartTime(SIX_HOURS);
        equalsSpaced.setBreakfastEndTime(SEVEN_HOURS);
        equalsSpaced.setBrunchStartTime(NINE_HOURS);
        equalsSpaced.setBrunchEndTime(TEN_HOURS);
        equalsSpaced.setLunchStartTime(TWELVE_HOURS);
        equalsSpaced.setLunchEndTime(THIRTEEN_HOURS);
        equalsSpaced.setSnackStartTime(FIFTEEN_HOURS);
        equalsSpaced.setSnackEndTime(SIXTEEN_HOURS);
        equalsSpaced.setDinnerStartTime(EIGHTEEN_HOURS);
        equalsSpaced.setDinnerEndTime(NINETEEN_HOURS);
        equalsSpaced.setSupperStartTime(TWENTY_ONE_HOURS);
        equalsSpaced.setSupperEndTime(TWENTY_TWO_HOURS);
        mWeekdayParametersManager.refresh(equalsSpaced);

        FoodsUsageEntity food = new FoodsUsageEntity(null, dateId, Meals.BREAKFAST, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.BRUNCH, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.LUNCH, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);

        assertEquals(Meals.BREAKFAST, Meals.preferredMealForTimeInDate(getMockContext(),
                EIGHT_HOURS - ONE_MINUTES, calendar.getTime(), false));
        assertEquals(Meals.BRUNCH, Meals.preferredMealForTimeInDate(getMockContext(),
                EIGHT_HOURS + ONE_MINUTES, calendar.getTime(), false));
    }

    public void testPreferredMealForTimeInDateWithZeroInPeriodAndZeroClosestWithZeroPointsAndManyClosestMustReturnCorrectValues() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        final String dateId = DateUtils.dateToDateId(calendar.getTime());

        WeekdayParametersEntity equalsSpaced = mWeekdayParametersManager.weekdayParametersFromWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        equalsSpaced.setBreakfastStartTime(SIX_HOURS);
        equalsSpaced.setBreakfastEndTime(SEVEN_HOURS);
        equalsSpaced.setBrunchStartTime(NINE_HOURS);
        equalsSpaced.setBrunchEndTime(TEN_HOURS);
        equalsSpaced.setLunchStartTime(TWELVE_HOURS);
        equalsSpaced.setLunchEndTime(THIRTEEN_HOURS);
        equalsSpaced.setSnackStartTime(FIFTEEN_HOURS);
        equalsSpaced.setSnackEndTime(SIXTEEN_HOURS);
        equalsSpaced.setDinnerStartTime(EIGHTEEN_HOURS);
        equalsSpaced.setDinnerEndTime(NINETEEN_HOURS);
        equalsSpaced.setSupperStartTime(TWENTY_ONE_HOURS);
        equalsSpaced.setSupperEndTime(TWENTY_TWO_HOURS);
        mWeekdayParametersManager.refresh(equalsSpaced);

        FoodsUsageEntity food = new FoodsUsageEntity(null, dateId, Meals.BREAKFAST, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.BRUNCH, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.LUNCH, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);

        assertEquals(Meals.BRUNCH, Meals.preferredMealForTimeInDate(getMockContext(),
                EIGHT_HOURS, calendar.getTime(), false));
        assertEquals(Meals.LUNCH, Meals.preferredMealForTimeInDate(getMockContext(),
                ELEVEN_HOURS, calendar.getTime(), false));
    }

    public void testPreferredMealForTimeInDateWithZeroInPeriodAndManyClosestWithZeroPointsAndOneClosestMustReturnCorrectValues() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        WeekdayParametersEntity brunchAndLunchOverlaps = mWeekdayParametersManager.weekdayParametersFromWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        brunchAndLunchOverlaps.setBreakfastStartTime(SIX_HOURS);
        brunchAndLunchOverlaps.setBreakfastEndTime(SEVEN_HOURS);
        brunchAndLunchOverlaps.setBrunchStartTime(NINE_HOURS);
        brunchAndLunchOverlaps.setBrunchEndTime(TEN_HOURS);
        brunchAndLunchOverlaps.setLunchStartTime(NINE_HOURS);
        brunchAndLunchOverlaps.setLunchEndTime(TEN_HOURS);
        brunchAndLunchOverlaps.setSnackStartTime(FIFTEEN_HOURS);
        brunchAndLunchOverlaps.setSnackEndTime(SIXTEEN_HOURS);
        brunchAndLunchOverlaps.setDinnerStartTime(EIGHTEEN_HOURS);
        brunchAndLunchOverlaps.setDinnerEndTime(NINETEEN_HOURS);
        brunchAndLunchOverlaps.setSupperStartTime(TWENTY_ONE_HOURS);
        brunchAndLunchOverlaps.setSupperEndTime(TWENTY_TWO_HOURS);
        mWeekdayParametersManager.refresh(brunchAndLunchOverlaps);

        assertEquals(Meals.BREAKFAST, Meals.preferredMealForTimeInDate(getMockContext(),
                EIGHT_HOURS - ONE_MINUTES, calendar.getTime(), false));
    }

    public void testPreferredMealForTimeInDateWithZeroInPeriodAndManyClosestWithZeroPointsAndManyClosestMustReturnCorrectValues() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        WeekdayParametersEntity brunchAndLunchOverlaps = mWeekdayParametersManager.weekdayParametersFromWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        brunchAndLunchOverlaps.setBreakfastStartTime(SIX_HOURS);
        brunchAndLunchOverlaps.setBreakfastEndTime(SEVEN_HOURS);
        brunchAndLunchOverlaps.setBrunchStartTime(NINE_HOURS);
        brunchAndLunchOverlaps.setBrunchEndTime(TEN_HOURS);
        brunchAndLunchOverlaps.setLunchStartTime(NINE_HOURS);
        brunchAndLunchOverlaps.setLunchEndTime(TEN_HOURS);
        brunchAndLunchOverlaps.setSnackStartTime(FIFTEEN_HOURS);
        brunchAndLunchOverlaps.setSnackEndTime(SIXTEEN_HOURS);
        brunchAndLunchOverlaps.setDinnerStartTime(EIGHTEEN_HOURS);
        brunchAndLunchOverlaps.setDinnerEndTime(NINETEEN_HOURS);
        brunchAndLunchOverlaps.setSupperStartTime(TWENTY_ONE_HOURS);
        brunchAndLunchOverlaps.setSupperEndTime(TWENTY_TWO_HOURS);
        mWeekdayParametersManager.refresh(brunchAndLunchOverlaps);

        assertEquals(Meals.BRUNCH, Meals.preferredMealForTimeInDate(getMockContext(),
                EIGHT_HOURS + ONE_MINUTES, calendar.getTime(), false));
    }

    public void testPreferredMealForTimeInDateWithManyInPeriodAndOneWithZeroPointsMustReturnCorrectValues() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        final String dateId = DateUtils.dateToDateId(calendar.getTime());

        WeekdayParametersEntity overlapAll = mWeekdayParametersManager.weekdayParametersFromWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        overlapAll.setBreakfastStartTime(EIGHT_HOURS);
        overlapAll.setBreakfastEndTime(TWENTY_HOURS);
        overlapAll.setBrunchStartTime(TEN_HOURS);
        overlapAll.setBrunchEndTime(EIGHTEEN_HOURS);
        overlapAll.setLunchStartTime(TWELVE_HOURS);
        overlapAll.setLunchEndTime(SIXTEEN_HOURS);
        overlapAll.setSnackStartTime(TWELVE_HOURS);
        overlapAll.setSnackEndTime(SIXTEEN_HOURS);
        overlapAll.setDinnerStartTime(TEN_HOURS);
        overlapAll.setDinnerEndTime(EIGHTEEN_HOURS);
        overlapAll.setSupperStartTime(EIGHT_HOURS + ONE_MINUTES);
        overlapAll.setSupperEndTime(TWENTY_HOURS);
        mWeekdayParametersManager.refresh(overlapAll);

        FoodsUsageEntity food = new FoodsUsageEntity(null, dateId, Meals.BREAKFAST, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);

        assertEquals(Meals.SUPPER, Meals.preferredMealForTimeInDate(getMockContext(),
                NINE_HOURS, calendar.getTime(), false));
    }

    public void testPreferredMealForTimeInDateWithManyInPeriodAndManyWithZeroPointsAndOneStartingEarlyMustReturnCorrectValues() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        WeekdayParametersEntity overlapAll = mWeekdayParametersManager.weekdayParametersFromWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        overlapAll.setBreakfastStartTime(EIGHT_HOURS);
        overlapAll.setBreakfastEndTime(TWENTY_HOURS);
        overlapAll.setBrunchStartTime(TEN_HOURS);
        overlapAll.setBrunchEndTime(EIGHTEEN_HOURS);
        overlapAll.setLunchStartTime(TWELVE_HOURS);
        overlapAll.setLunchEndTime(SIXTEEN_HOURS);
        overlapAll.setSnackStartTime(TWELVE_HOURS);
        overlapAll.setSnackEndTime(SIXTEEN_HOURS);
        overlapAll.setDinnerStartTime(TEN_HOURS);
        overlapAll.setDinnerEndTime(EIGHTEEN_HOURS);
        overlapAll.setSupperStartTime(EIGHT_HOURS + ONE_MINUTES);
        overlapAll.setSupperEndTime(TWENTY_HOURS);
        mWeekdayParametersManager.refresh(overlapAll);

        assertEquals(Meals.BREAKFAST, Meals.preferredMealForTimeInDate(getMockContext(),
                NINE_HOURS, calendar.getTime(), false));
    }

    public void testPreferredMealForTimeInDateWithManyInPeriodAndManyWithZeroPointsAndManyStartingEarlyAndOneWithMinorPeriodMustReturnCorrectValues() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        WeekdayParametersEntity overlapAll = mWeekdayParametersManager.weekdayParametersFromWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        overlapAll.setBreakfastStartTime(EIGHT_HOURS);
        overlapAll.setBreakfastEndTime(TWENTY_HOURS);
        overlapAll.setBrunchStartTime(TEN_HOURS);
        overlapAll.setBrunchEndTime(EIGHTEEN_HOURS);
        overlapAll.setLunchStartTime(TWELVE_HOURS);
        overlapAll.setLunchEndTime(SIXTEEN_HOURS);
        overlapAll.setSnackStartTime(TWELVE_HOURS);
        overlapAll.setSnackEndTime(SIXTEEN_HOURS);
        overlapAll.setDinnerStartTime(TEN_HOURS);
        overlapAll.setDinnerEndTime(EIGHTEEN_HOURS);
        overlapAll.setSupperStartTime(EIGHT_HOURS);
        overlapAll.setSupperEndTime(TWENTY_HOURS - ONE_MINUTES);
        mWeekdayParametersManager.refresh(overlapAll);

        assertEquals(Meals.SUPPER, Meals.preferredMealForTimeInDate(getMockContext(),
                NINE_HOURS, calendar.getTime(), false));
    }

    public void testPreferredMealForTimeInDateWithManyInPeriodAndManyWithZeroPointsAndManyStartingEarlyAndManyWithMinorPeriodMustReturnCorrectValues() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        WeekdayParametersEntity overlapAll = mWeekdayParametersManager.weekdayParametersFromWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        overlapAll.setBreakfastStartTime(EIGHT_HOURS);
        overlapAll.setBreakfastEndTime(TWENTY_HOURS);
        overlapAll.setBrunchStartTime(TEN_HOURS);
        overlapAll.setBrunchEndTime(EIGHTEEN_HOURS);
        overlapAll.setLunchStartTime(TWELVE_HOURS);
        overlapAll.setLunchEndTime(SIXTEEN_HOURS);
        overlapAll.setSnackStartTime(TWELVE_HOURS);
        overlapAll.setSnackEndTime(SIXTEEN_HOURS);
        overlapAll.setDinnerStartTime(TEN_HOURS);
        overlapAll.setDinnerEndTime(EIGHTEEN_HOURS);
        overlapAll.setSupperStartTime(EIGHT_HOURS);
        overlapAll.setSupperEndTime(TWENTY_HOURS);
        mWeekdayParametersManager.refresh(overlapAll);

        assertEquals(Meals.BREAKFAST, Meals.preferredMealForTimeInDate(getMockContext(),
                NINE_HOURS, calendar.getTime(), false));
    }

    public void testPreferredMealForTimeInDateWithManyInPeriodAndZeroWithZeroPointsAndOneWithMinorPeriodMustReturnCorrectValues() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        final String dateId = DateUtils.dateToDateId(calendar.getTime());

        WeekdayParametersEntity overlapAll = mWeekdayParametersManager.weekdayParametersFromWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        overlapAll.setBreakfastStartTime(EIGHT_HOURS);
        overlapAll.setBreakfastEndTime(TWENTY_HOURS);
        overlapAll.setBrunchStartTime(TEN_HOURS);
        overlapAll.setBrunchEndTime(EIGHTEEN_HOURS);
        overlapAll.setLunchStartTime(TWELVE_HOURS + ONE_MINUTES);
        overlapAll.setLunchEndTime(SIXTEEN_HOURS);
        overlapAll.setSnackStartTime(TWELVE_HOURS);
        overlapAll.setSnackEndTime(SIXTEEN_HOURS);
        overlapAll.setDinnerStartTime(TEN_HOURS);
        overlapAll.setDinnerEndTime(EIGHTEEN_HOURS);
        overlapAll.setSupperStartTime(EIGHT_HOURS);
        overlapAll.setSupperEndTime(TWENTY_HOURS);
        mWeekdayParametersManager.refresh(overlapAll);

        FoodsUsageEntity food = new FoodsUsageEntity(null, dateId, Meals.BREAKFAST, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.BRUNCH, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.LUNCH, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.SNACK, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.DINNER, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.SUPPER, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);

        assertEquals(Meals.LUNCH, Meals.preferredMealForTimeInDate(getMockContext(),
                FIFTEEN_HOURS, calendar.getTime(), false));

    }

    public void testPreferredMealForTimeInDateWithManyInPeriodAndZeroWithZeroPointsAndManyWithMinorPeriodMustReturnCorrectValues() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        final String dateId = DateUtils.dateToDateId(calendar.getTime());

        WeekdayParametersEntity overlapAll = mWeekdayParametersManager.weekdayParametersFromWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        overlapAll.setBreakfastStartTime(EIGHT_HOURS);
        overlapAll.setBreakfastEndTime(TWENTY_HOURS);
        overlapAll.setBrunchStartTime(TEN_HOURS);
        overlapAll.setBrunchEndTime(EIGHTEEN_HOURS);
        overlapAll.setLunchStartTime(TWELVE_HOURS);
        overlapAll.setLunchEndTime(SIXTEEN_HOURS);
        overlapAll.setSnackStartTime(TWELVE_HOURS);
        overlapAll.setSnackEndTime(SIXTEEN_HOURS);
        overlapAll.setDinnerStartTime(TEN_HOURS);
        overlapAll.setDinnerEndTime(EIGHTEEN_HOURS);
        overlapAll.setSupperStartTime(EIGHT_HOURS);
        overlapAll.setSupperEndTime(TWENTY_HOURS);
        mWeekdayParametersManager.refresh(overlapAll);

        FoodsUsageEntity food = new FoodsUsageEntity(null, dateId, Meals.BREAKFAST, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.BRUNCH, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.LUNCH, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.SNACK, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.DINNER, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.SUPPER, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);

        assertEquals(Meals.SNACK, Meals.preferredMealForTimeInDate(getMockContext(),
                FIFTEEN_HOURS, calendar.getTime(), false));

    }

    public void testPreferredMealForTimeInDateFillModeWithOnePointsMinorGoalMustReturnCorrectValues() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        final String dateId = DateUtils.dateToDateId(calendar.getTime());

        WeekdayParametersEntity equalsSpaced = mWeekdayParametersManager.weekdayParametersFromWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        equalsSpaced.setBreakfastStartTime(SIX_HOURS);
        equalsSpaced.setBreakfastEndTime(SEVEN_HOURS);
        equalsSpaced.setBreakfastGoal(1.0f);
        equalsSpaced.setBrunchStartTime(NINE_HOURS);
        equalsSpaced.setBrunchEndTime(TEN_HOURS);
        equalsSpaced.setBrunchGoal(1.0f);
        equalsSpaced.setLunchStartTime(TWELVE_HOURS);
        equalsSpaced.setLunchEndTime(THIRTEEN_HOURS);
        equalsSpaced.setLunchGoal(1.0f);
        equalsSpaced.setSnackStartTime(FIFTEEN_HOURS);
        equalsSpaced.setSnackEndTime(SIXTEEN_HOURS);
        equalsSpaced.setSnackGoal(1.0f);
        equalsSpaced.setDinnerStartTime(EIGHTEEN_HOURS);
        equalsSpaced.setDinnerEndTime(NINETEEN_HOURS);
        equalsSpaced.setDinnerGoal(1.0f);
        equalsSpaced.setSupperStartTime(TWENTY_ONE_HOURS);
        equalsSpaced.setSupperEndTime(TWENTY_TWO_HOURS);
        equalsSpaced.setSupperGoal(1.0f);
        mWeekdayParametersManager.refresh(equalsSpaced);

        FoodsUsageEntity food = new FoodsUsageEntity(null, dateId, Meals.BREAKFAST, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.BRUNCH, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.LUNCH, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.DINNER, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.SUPPER, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);

        assertEquals(Meals.SNACK, Meals.preferredMealForTimeInDate(getMockContext(),
                NO_TIME, calendar.getTime(), true));
    }

    public void testPreferredMealForTimeInDateFillModeWithZeroPointsMinorGoalWithOneNoPointsMustReturnCorrectValues() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        final String dateId = DateUtils.dateToDateId(calendar.getTime());

        WeekdayParametersEntity equalsSpaced = mWeekdayParametersManager.weekdayParametersFromWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        equalsSpaced.setBreakfastStartTime(SIX_HOURS);
        equalsSpaced.setBreakfastEndTime(SEVEN_HOURS);
        equalsSpaced.setBreakfastGoal(1.0f);
        equalsSpaced.setBrunchStartTime(NINE_HOURS);
        equalsSpaced.setBrunchEndTime(TEN_HOURS);
        equalsSpaced.setBrunchGoal(0f);
        equalsSpaced.setLunchStartTime(TWELVE_HOURS);
        equalsSpaced.setLunchEndTime(THIRTEEN_HOURS);
        equalsSpaced.setLunchGoal(1.0f);
        equalsSpaced.setSnackStartTime(FIFTEEN_HOURS);
        equalsSpaced.setSnackEndTime(SIXTEEN_HOURS);
        equalsSpaced.setSnackGoal(1.0f);
        equalsSpaced.setDinnerStartTime(EIGHTEEN_HOURS);
        equalsSpaced.setDinnerEndTime(NINETEEN_HOURS);
        equalsSpaced.setDinnerGoal(1.0f);
        equalsSpaced.setSupperStartTime(TWENTY_ONE_HOURS);
        equalsSpaced.setSupperEndTime(TWENTY_TWO_HOURS);
        equalsSpaced.setSupperGoal(1.0f);
        mWeekdayParametersManager.refresh(equalsSpaced);

        FoodsUsageEntity food = new FoodsUsageEntity(null, dateId, Meals.BREAKFAST, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.LUNCH, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.SNACK, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.DINNER, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.SUPPER, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);

        assertEquals(Meals.BRUNCH, Meals.preferredMealForTimeInDate(getMockContext(),
                NO_TIME, calendar.getTime(), true));
    }

    public void testPreferredMealForTimeInDateFillModeWithZeroPointsMinorGoalWithZeroNoPointsMustReturnCorrectValues() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        final String dateId = DateUtils.dateToDateId(calendar.getTime());

        WeekdayParametersEntity equalsSpaced = mWeekdayParametersManager.weekdayParametersFromWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        equalsSpaced.setBreakfastStartTime(SIX_HOURS);
        equalsSpaced.setBreakfastEndTime(SEVEN_HOURS);
        equalsSpaced.setBreakfastGoal(1.0f);
        equalsSpaced.setBrunchStartTime(NINE_HOURS);
        equalsSpaced.setBrunchEndTime(TEN_HOURS);
        equalsSpaced.setBrunchGoal(1.0f);
        equalsSpaced.setLunchStartTime(TWELVE_HOURS);
        equalsSpaced.setLunchEndTime(THIRTEEN_HOURS);
        equalsSpaced.setLunchGoal(1.0f);
        equalsSpaced.setSnackStartTime(FIFTEEN_HOURS);
        equalsSpaced.setSnackEndTime(SIXTEEN_HOURS);
        equalsSpaced.setSnackGoal(1.0f);
        equalsSpaced.setDinnerStartTime(EIGHTEEN_HOURS);
        equalsSpaced.setDinnerEndTime(NINETEEN_HOURS);
        equalsSpaced.setDinnerGoal(1.0f);
        equalsSpaced.setSupperStartTime(TWENTY_ONE_HOURS);
        equalsSpaced.setSupperEndTime(TWENTY_TWO_HOURS);
        equalsSpaced.setSupperGoal(1.0f);
        mWeekdayParametersManager.refresh(equalsSpaced);

        FoodsUsageEntity food = new FoodsUsageEntity(null, dateId, Meals.BREAKFAST, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.BRUNCH, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.LUNCH, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.SNACK, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.DINNER, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.SUPPER, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);

        assertEquals(Meals.SUPPER, Meals.preferredMealForTimeInDate(getMockContext(),
                NO_TIME, calendar.getTime(), true));
    }

    public void testPreferredMealForTimeInDateFillModeWithZeroPointsMinorGoalWithManyNoPointsMustReturnCorrectValues() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        final String dateId = DateUtils.dateToDateId(calendar.getTime());

        WeekdayParametersEntity equalsSpaced = mWeekdayParametersManager.weekdayParametersFromWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        equalsSpaced.setBreakfastStartTime(SIX_HOURS);
        equalsSpaced.setBreakfastEndTime(SEVEN_HOURS);
        equalsSpaced.setBreakfastGoal(1.0f);
        equalsSpaced.setBrunchStartTime(NINE_HOURS);
        equalsSpaced.setBrunchEndTime(TEN_HOURS);
        equalsSpaced.setBrunchGoal(1.0f);
        equalsSpaced.setLunchStartTime(TWELVE_HOURS);
        equalsSpaced.setLunchEndTime(THIRTEEN_HOURS);
        equalsSpaced.setLunchGoal(1.0f);
        equalsSpaced.setSnackStartTime(FIFTEEN_HOURS);
        equalsSpaced.setSnackEndTime(SIXTEEN_HOURS);
        equalsSpaced.setSnackGoal(1.0f);
        equalsSpaced.setDinnerStartTime(EIGHTEEN_HOURS);
        equalsSpaced.setDinnerEndTime(NINETEEN_HOURS);
        equalsSpaced.setDinnerGoal(1.0f);
        equalsSpaced.setSupperStartTime(TWENTY_ONE_HOURS);
        equalsSpaced.setSupperEndTime(TWENTY_TWO_HOURS);
        equalsSpaced.setSupperGoal(1.0f);
        mWeekdayParametersManager.refresh(equalsSpaced);

        FoodsUsageEntity food = new FoodsUsageEntity(null, dateId, Meals.BREAKFAST, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.BRUNCH, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);

        assertEquals(Meals.LUNCH, Meals.preferredMealForTimeInDate(getMockContext(),
                NO_TIME, calendar.getTime(), true));
    }

    public void testPreferredMealForTimeInDateFillModeWithManyPointsMinorGoalWithOneNoPointsMustReturnCorrectValues() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        final String dateId = DateUtils.dateToDateId(calendar.getTime());

        WeekdayParametersEntity equalsSpaced = mWeekdayParametersManager.weekdayParametersFromWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        equalsSpaced.setBreakfastStartTime(SIX_HOURS);
        equalsSpaced.setBreakfastEndTime(SEVEN_HOURS);
        equalsSpaced.setBreakfastGoal(10.0f);
        equalsSpaced.setBrunchStartTime(NINE_HOURS);
        equalsSpaced.setBrunchEndTime(TEN_HOURS);
        equalsSpaced.setBrunchGoal(10.0f);
        equalsSpaced.setLunchStartTime(TWELVE_HOURS);
        equalsSpaced.setLunchEndTime(THIRTEEN_HOURS);
        equalsSpaced.setLunchGoal(10.0f);
        equalsSpaced.setSnackStartTime(FIFTEEN_HOURS);
        equalsSpaced.setSnackEndTime(SIXTEEN_HOURS);
        equalsSpaced.setSnackGoal(10.0f);
        equalsSpaced.setDinnerStartTime(EIGHTEEN_HOURS);
        equalsSpaced.setDinnerEndTime(NINETEEN_HOURS);
        equalsSpaced.setDinnerGoal(10.0f);
        equalsSpaced.setSupperStartTime(TWENTY_ONE_HOURS);
        equalsSpaced.setSupperEndTime(TWENTY_TWO_HOURS);
        equalsSpaced.setSupperGoal(10.0f);
        mWeekdayParametersManager.refresh(equalsSpaced);

        FoodsUsageEntity food = new FoodsUsageEntity(null, dateId, Meals.BREAKFAST, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.BRUNCH, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.LUNCH, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.DINNER, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.SUPPER, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);

        assertEquals(Meals.SNACK, Meals.preferredMealForTimeInDate(getMockContext(),
                NO_TIME, calendar.getTime(), true));
    }

    public void testPreferredMealForTimeInDateFillModeWithManyPointsMinorGoalWithZeroNoPointsMustReturnCorrectValues() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        final String dateId = DateUtils.dateToDateId(calendar.getTime());

        WeekdayParametersEntity equalsSpaced = mWeekdayParametersManager.weekdayParametersFromWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        equalsSpaced.setBreakfastStartTime(SIX_HOURS);
        equalsSpaced.setBreakfastEndTime(SEVEN_HOURS);
        equalsSpaced.setBreakfastGoal(10.0f);
        equalsSpaced.setBrunchStartTime(NINE_HOURS);
        equalsSpaced.setBrunchEndTime(TEN_HOURS);
        equalsSpaced.setBrunchGoal(10.0f);
        equalsSpaced.setLunchStartTime(TWELVE_HOURS);
        equalsSpaced.setLunchEndTime(THIRTEEN_HOURS);
        equalsSpaced.setLunchGoal(10.0f);
        equalsSpaced.setSnackStartTime(FIFTEEN_HOURS);
        equalsSpaced.setSnackEndTime(SIXTEEN_HOURS);
        equalsSpaced.setSnackGoal(10.0f);
        equalsSpaced.setDinnerStartTime(EIGHTEEN_HOURS);
        equalsSpaced.setDinnerEndTime(NINETEEN_HOURS);
        equalsSpaced.setDinnerGoal(10.0f);
        equalsSpaced.setSupperStartTime(TWENTY_ONE_HOURS);
        equalsSpaced.setSupperEndTime(TWENTY_TWO_HOURS);
        equalsSpaced.setSupperGoal(10.0f);
        mWeekdayParametersManager.refresh(equalsSpaced);

        FoodsUsageEntity food = new FoodsUsageEntity(null, dateId, Meals.BREAKFAST, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.BRUNCH, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.LUNCH, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.SNACK, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.DINNER, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.SUPPER, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);

        assertEquals(Meals.BREAKFAST, Meals.preferredMealForTimeInDate(getMockContext(),
                NO_TIME, calendar.getTime(), true));
    }

    public void testPreferredMealForTimeInDateFillModeWithManyPointsMinorGoalWithManyNoPointsMustReturnCorrectValues() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        final String dateId = DateUtils.dateToDateId(calendar.getTime());

        WeekdayParametersEntity equalsSpaced = mWeekdayParametersManager.weekdayParametersFromWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        equalsSpaced.setBreakfastStartTime(SIX_HOURS);
        equalsSpaced.setBreakfastEndTime(SEVEN_HOURS);
        equalsSpaced.setBreakfastGoal(10.0f);
        equalsSpaced.setBrunchStartTime(NINE_HOURS);
        equalsSpaced.setBrunchEndTime(TEN_HOURS);
        equalsSpaced.setBrunchGoal(10.0f);
        equalsSpaced.setLunchStartTime(TWELVE_HOURS);
        equalsSpaced.setLunchEndTime(THIRTEEN_HOURS);
        equalsSpaced.setLunchGoal(10.0f);
        equalsSpaced.setSnackStartTime(FIFTEEN_HOURS);
        equalsSpaced.setSnackEndTime(SIXTEEN_HOURS);
        equalsSpaced.setSnackGoal(10.0f);
        equalsSpaced.setDinnerStartTime(EIGHTEEN_HOURS);
        equalsSpaced.setDinnerEndTime(NINETEEN_HOURS);
        equalsSpaced.setDinnerGoal(10.0f);
        equalsSpaced.setSupperStartTime(TWENTY_ONE_HOURS);
        equalsSpaced.setSupperEndTime(TWENTY_TWO_HOURS);
        equalsSpaced.setSupperGoal(10.0f);
        mWeekdayParametersManager.refresh(equalsSpaced);

        FoodsUsageEntity food = new FoodsUsageEntity(null, dateId, Meals.BREAKFAST, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.BRUNCH, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.LUNCH, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.SNACK, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);

        assertEquals(Meals.DINNER, Meals.preferredMealForTimeInDate(getMockContext(),
                NO_TIME, calendar.getTime(), true));
    }

    public void testPreferredUsageForMealInDateMustReturnCorrectValues() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        final String dateId = DateUtils.dateToDateId(calendar.getTime());

        WeekdayParametersEntity equalsSpaced = mWeekdayParametersManager.weekdayParametersFromWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        equalsSpaced.setBreakfastStartTime(SIX_HOURS);
        equalsSpaced.setBreakfastEndTime(SEVEN_HOURS);
        equalsSpaced.setBreakfastGoal(10.0f);
        equalsSpaced.setBrunchStartTime(NINE_HOURS);
        equalsSpaced.setBrunchEndTime(TEN_HOURS);
        equalsSpaced.setBrunchGoal(10.0f);
        equalsSpaced.setLunchStartTime(TWELVE_HOURS);
        equalsSpaced.setLunchEndTime(THIRTEEN_HOURS);
        equalsSpaced.setLunchGoal(10.0f);
        equalsSpaced.setSnackStartTime(FIFTEEN_HOURS);
        equalsSpaced.setSnackEndTime(SIXTEEN_HOURS);
        equalsSpaced.setSnackGoal(10.0f);
        equalsSpaced.setDinnerStartTime(EIGHTEEN_HOURS);
        equalsSpaced.setDinnerEndTime(NINETEEN_HOURS);
        equalsSpaced.setDinnerGoal(10.0f);
        equalsSpaced.setSupperStartTime(TWENTY_ONE_HOURS);
        equalsSpaced.setSupperEndTime(TWENTY_TWO_HOURS);
        equalsSpaced.setSupperGoal(10.0f);
        mWeekdayParametersManager.refresh(equalsSpaced);

        FoodsUsageEntity food = new FoodsUsageEntity(null, dateId, Meals.BREAKFAST, NINE_HOURS, "", 11.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.BRUNCH, NINE_HOURS, "", 10.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.LUNCH, NINE_HOURS, "", 5.0f);
        mFoodsUsageManager.refresh(food);
        food = new FoodsUsageEntity(null, dateId, Meals.SNACK, NINE_HOURS, "", 1.0f);
        mFoodsUsageManager.refresh(food);

        assertEquals(0.0f, Meals.preferredUsageForMealInDate(getMockContext(),
                Meals.BREAKFAST, calendar.getTime()));
        assertEquals(0.0f, Meals.preferredUsageForMealInDate(getMockContext(),
                Meals.BRUNCH, calendar.getTime()));
        assertEquals(5.0f, Meals.preferredUsageForMealInDate(getMockContext(),
                Meals.LUNCH, calendar.getTime()));
        assertEquals(9.0f, Meals.preferredUsageForMealInDate(getMockContext(),
                Meals.SNACK, calendar.getTime()));
        assertEquals(10.0f, Meals.preferredUsageForMealInDate(getMockContext(),
                Meals.DINNER, calendar.getTime()));
        assertEquals(10.0f, Meals.preferredUsageForMealInDate(getMockContext(),
                Meals.SUPPER, calendar.getTime()));
    }
}
