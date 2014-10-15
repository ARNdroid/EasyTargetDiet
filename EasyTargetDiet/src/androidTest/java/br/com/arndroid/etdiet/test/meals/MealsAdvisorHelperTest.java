package br.com.arndroid.etdiet.test.meals;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.meals.MealsAdvisorHelper;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.virtualweek.UsageSummary;

public class MealsAdvisorHelperTest extends TestCase {

    private static final int ZERO_HOURS = 0;
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

    private MealsAdvisorHelper mAdvisorHelper;

    @Override
    protected void setUp() throws Exception {
        // Gets the advisor helper for this test.
        mAdvisorHelper = new MealsAdvisorHelper();
    }

    public void testFindMealsInPeriodWithEmptyCandidatesMustReturnCorrectValues() {
        final List<Integer> emptyCandidates = new ArrayList<Integer>(Meals.getMealsCount() - 1);
        final List<Integer> found = new ArrayList<Integer>(Meals.getMealsCount() - 1);

        final DaysEntity commonDaysEntity = new DaysEntity(null, null, null,
                EIGHT_HOURS, TEN_HOURS, null,
                TEN_HOURS, TWELVE_HOURS, null,
                TWELVE_HOURS, FOURTEEN_HOURS, null,
                FOURTEEN_HOURS, SIXTEEN_HOURS, null,
                SIXTEEN_HOURS, EIGHTEEN_HOURS, null,
                EIGHTEEN_HOURS, TWENTY_HOURS, null,
                null, null, null, null, null, null, null, null);
        mAdvisorHelper.findMealsInPeriod(ELEVEN_HOURS, commonDaysEntity, emptyCandidates, found);
        assertTrue(found.isEmpty());
    }

    public void testFindMealsInPeriodMustReturnCorrectValues(){
        final List<Integer> candidates = new ArrayList<Integer>(Meals.getMealsCount() - 1);
        candidates.add(Meals.BREAKFAST);
        candidates.add(Meals.BRUNCH);
        candidates.add(Meals.LUNCH);
        candidates.add(Meals.SNACK);
        candidates.add(Meals.DINNER);
        candidates.add(Meals.SUPPER);
        final List<Integer> found = new ArrayList<Integer>(Meals.getMealsCount() - 1);

        final DaysEntity commonDaysEntity = new DaysEntity(null, null, null,
                EIGHT_HOURS, TEN_HOURS, null,
                TEN_HOURS, TWELVE_HOURS, null,
                TWELVE_HOURS, FOURTEEN_HOURS, null,
                FOURTEEN_HOURS, SIXTEEN_HOURS, null,
                SIXTEEN_HOURS, EIGHTEEN_HOURS, null,
                EIGHTEEN_HOURS, TWENTY_HOURS, null,
                null, null, null, null, null, null, null, null);
        mAdvisorHelper.findMealsInPeriod(THIRTEEN_HOURS, commonDaysEntity, candidates, found);
        assertEquals(1, found.size());
        assertTrue(found.contains(Meals.LUNCH));

        mAdvisorHelper.findMealsInPeriod(EIGHTEEN_HOURS, commonDaysEntity, candidates, found);
        assertEquals(2, found.size());
        assertTrue(found.contains(Meals.DINNER));
        assertTrue(found.contains(Meals.SUPPER));

        final DaysEntity overlapAllDaysEntity = new DaysEntity(null, null, null,
                EIGHT_HOURS, TWENTY_HOURS, null,
                TEN_HOURS, EIGHTEEN_HOURS, null,
                TWELVE_HOURS, SIXTEEN_HOURS, null,
                TWELVE_HOURS, SIXTEEN_HOURS, null,
                TEN_HOURS, EIGHTEEN_HOURS, null,
                EIGHT_HOURS, TWENTY_HOURS, null,
                null, null, null, null, null, null, null, null);
        mAdvisorHelper.findMealsInPeriod(FOURTEEN_HOURS, overlapAllDaysEntity, candidates, found);
        assertEquals(6, found.size());
        assertTrue(found.contains(Meals.BREAKFAST));
        assertTrue(found.contains(Meals.BRUNCH));
        assertTrue(found.contains(Meals.LUNCH));
        assertTrue(found.contains(Meals.SNACK));
        assertTrue(found.contains(Meals.DINNER));
        assertTrue(found.contains(Meals.SUPPER));

        mAdvisorHelper.findMealsInPeriod(SEVEN_HOURS, overlapAllDaysEntity, candidates, found);
        assertTrue(found.isEmpty());

        mAdvisorHelper.findMealsInPeriod(TWENTY_ONE_HOURS, overlapAllDaysEntity, candidates, found);
        assertTrue(found.isEmpty());
    }

    public void testFindMealsWithNoPointsWithEmptyCandidatesMustReturnCorrectValues() {
        final List<Integer> emptyCandidates = new ArrayList<Integer>(Meals.getMealsCount() - 1);
        final List<Integer> found = new ArrayList<Integer>(Meals.getMealsCount() - 1);

        final UsageSummary emptyUsageSummary = new UsageSummary();
        emptyUsageSummary.setBreakfastUsed(0);
        emptyUsageSummary.setBrunchUsed(0);
        emptyUsageSummary.setLunchUsed(0);
        emptyUsageSummary.setSnackUsed(0);
        emptyUsageSummary.setDinnerUsed(0);
        emptyUsageSummary.setSupperUsed(0);
        mAdvisorHelper.findMealsWithNoPoints(emptyUsageSummary, emptyCandidates, found);
        assertTrue(found.isEmpty());
    }

    public void testFindMealsWithNoPointsMustReturnCorrectValues() {
        final List<Integer> candidates = new ArrayList<Integer>(Meals.getMealsCount() - 1);
        candidates.add(Meals.BREAKFAST);
        candidates.add(Meals.BRUNCH);
        candidates.add(Meals.LUNCH);
        candidates.add(Meals.SNACK);
        candidates.add(Meals.DINNER);
        candidates.add(Meals.SUPPER);
        final List<Integer> found = new ArrayList<Integer>(Meals.getMealsCount() - 1);

        final UsageSummary emptyUsageSummary = new UsageSummary();
        emptyUsageSummary.setBreakfastUsed(0);
        emptyUsageSummary.setBrunchUsed(0);
        emptyUsageSummary.setLunchUsed(0);
        emptyUsageSummary.setSnackUsed(0);
        emptyUsageSummary.setDinnerUsed(0);
        emptyUsageSummary.setSupperUsed(0);
        mAdvisorHelper.findMealsWithNoPoints(emptyUsageSummary, candidates, found);
        assertEquals(6, found.size());
        assertTrue(found.contains(Meals.BREAKFAST));
        assertTrue(found.contains(Meals.BRUNCH));
        assertTrue(found.contains(Meals.LUNCH));
        assertTrue(found.contains(Meals.SNACK));
        assertTrue(found.contains(Meals.DINNER));
        assertTrue(found.contains(Meals.SUPPER));

        final UsageSummary commonUsageSummary = new UsageSummary();
        commonUsageSummary.setBreakfastUsed(6);
        commonUsageSummary.setBrunchUsed(1);
        commonUsageSummary.setLunchUsed(10);
        commonUsageSummary.setSnackUsed(1);
        commonUsageSummary.setDinnerUsed(0);
        commonUsageSummary.setSupperUsed(0);
        mAdvisorHelper.findMealsWithNoPoints(commonUsageSummary, candidates, found);
        assertEquals(2, found.size());
        assertTrue(found.contains(Meals.DINNER));
        assertTrue(found.contains(Meals.SUPPER));

        final UsageSummary fullUsageSummary = new UsageSummary();
        fullUsageSummary.setBreakfastUsed(1);
        fullUsageSummary.setBrunchUsed(3);
        fullUsageSummary.setLunchUsed(5);
        fullUsageSummary.setSnackUsed(7);
        fullUsageSummary.setDinnerUsed(9);
        fullUsageSummary.setSupperUsed(11);
        mAdvisorHelper.findMealsWithNoPoints(fullUsageSummary, candidates, found);
        assertTrue(found.isEmpty());
    }

    public void testFindMealsWithMinorIntervalWithEmptyCandidatesMustReturnCorrectValues(){
        final List<Integer> emptyCandidates = new ArrayList<Integer>(Meals.getMealsCount() - 1);
        final List<Integer> found = new ArrayList<Integer>(Meals.getMealsCount() - 1);

        final DaysEntity commonDaysEntity = new DaysEntity(null, null, null,
                EIGHT_HOURS, TEN_HOURS, null,
                TEN_HOURS, TWELVE_HOURS, null,
                TWELVE_HOURS, FOURTEEN_HOURS, null,
                FOURTEEN_HOURS, SIXTEEN_HOURS, null,
                SIXTEEN_HOURS, EIGHTEEN_HOURS, null,
                EIGHTEEN_HOURS, TWENTY_HOURS, null,
                null, null, null, null, null, null, null, null);
        mAdvisorHelper.findMealsWithMinorInterval(commonDaysEntity, emptyCandidates, found);
        assertTrue(found.isEmpty());
    }

    public void testFindMealsWithMinorIntervalMustReturnCorrectValues() {
        final List<Integer> candidates = new ArrayList<Integer>(Meals.getMealsCount() - 1);
        candidates.add(Meals.BREAKFAST);
        candidates.add(Meals.BRUNCH);
        candidates.add(Meals.LUNCH);
        candidates.add(Meals.SNACK);
        candidates.add(Meals.DINNER);
        candidates.add(Meals.SUPPER);
        final List<Integer> found = new ArrayList<Integer>(Meals.getMealsCount() - 1);

        final DaysEntity commonDaysEntity = new DaysEntity(null, null, null,
                EIGHT_HOURS, TEN_HOURS, null,
                TEN_HOURS, TWELVE_HOURS, null,
                TWELVE_HOURS, FOURTEEN_HOURS, null,
                FOURTEEN_HOURS, SIXTEEN_HOURS, null,
                SIXTEEN_HOURS, EIGHTEEN_HOURS, null,
                EIGHTEEN_HOURS, TWENTY_HOURS, null,
                null, null, null, null, null, null, null, null);
        mAdvisorHelper.findMealsWithMinorInterval(commonDaysEntity, candidates, found);
        assertEquals(6, found.size());
        assertTrue(found.contains(Meals.BREAKFAST));
        assertTrue(found.contains(Meals.BRUNCH));
        assertTrue(found.contains(Meals.LUNCH));
        assertTrue(found.contains(Meals.SNACK));
        assertTrue(found.contains(Meals.DINNER));
        assertTrue(found.contains(Meals.SUPPER));

        final DaysEntity overlapAllDaysEntity = new DaysEntity(null, null, null,
                EIGHT_HOURS, TWENTY_HOURS, null,
                TEN_HOURS, EIGHTEEN_HOURS, null,
                TWELVE_HOURS, SIXTEEN_HOURS, null,
                TWELVE_HOURS, SIXTEEN_HOURS, null,
                TEN_HOURS, EIGHTEEN_HOURS, null,
                EIGHT_HOURS, TWENTY_HOURS, null,
                null, null, null, null, null, null, null, null);
        mAdvisorHelper.findMealsWithMinorInterval(overlapAllDaysEntity, candidates, found);
        assertEquals(2, found.size());
        assertTrue(found.contains(Meals.LUNCH));
        assertTrue(found.contains(Meals.SNACK));

        final DaysEntity lastIsMinorDaysEntity = new DaysEntity(null, null, null,
                ZERO_HOURS, SIX_HOURS, null,
                SIX_HOURS, ELEVEN_HOURS, null,
                ELEVEN_HOURS, FIFTEEN_HOURS, null,
                FIFTEEN_HOURS, EIGHTEEN_HOURS, null,
                EIGHTEEN_HOURS, TWENTY_HOURS, null,
                TWENTY_HOURS, TWENTY_ONE_HOURS, null,
                null, null, null, null, null, null, null, null);
        mAdvisorHelper.findMealsWithMinorInterval(lastIsMinorDaysEntity, candidates, found);
        assertEquals(1, found.size());
        assertTrue(found.contains(Meals.SUPPER));
    }

    public void testFindMealsStartingEarlyWithEmptyCandidatesMustReturnCorrectValues() {
        final List<Integer> emptyCandidates = new ArrayList<Integer>(Meals.getMealsCount() - 1);
        final List<Integer> found = new ArrayList<Integer>(Meals.getMealsCount() - 1);

        final DaysEntity commonDaysEntity = new DaysEntity(null, null, null,
                EIGHT_HOURS, TEN_HOURS, null,
                TEN_HOURS, TWELVE_HOURS, null,
                TWELVE_HOURS, FOURTEEN_HOURS, null,
                FOURTEEN_HOURS, SIXTEEN_HOURS, null,
                SIXTEEN_HOURS, EIGHTEEN_HOURS, null,
                EIGHTEEN_HOURS, TWENTY_HOURS, null,
                null, null, null, null, null, null, null, null);
        mAdvisorHelper.findMealsStartingEarly(commonDaysEntity, emptyCandidates, found);
        assertTrue(found.isEmpty());
    }

    public void testFindMealsStartingEarlyMustReturnCorrectValues() {
        final List<Integer> candidates = new ArrayList<Integer>(Meals.getMealsCount() - 1);
        candidates.add(Meals.BREAKFAST);
        candidates.add(Meals.BRUNCH);
        candidates.add(Meals.LUNCH);
        candidates.add(Meals.SNACK);
        candidates.add(Meals.DINNER);
        candidates.add(Meals.SUPPER);
        final List<Integer> found = new ArrayList<Integer>(Meals.getMealsCount() - 1);

        final DaysEntity commonDaysEntity = new DaysEntity(null, null, null,
                EIGHT_HOURS, TEN_HOURS, null,
                TEN_HOURS, TWELVE_HOURS, null,
                TWELVE_HOURS, FOURTEEN_HOURS, null,
                FOURTEEN_HOURS, SIXTEEN_HOURS, null,
                SIXTEEN_HOURS, EIGHTEEN_HOURS, null,
                EIGHTEEN_HOURS, TWENTY_HOURS, null,
                null, null, null, null, null, null, null, null);
        mAdvisorHelper.findMealsStartingEarly(commonDaysEntity, candidates, found);
        assertEquals(1, found.size());
        assertTrue(found.contains(Meals.BREAKFAST));

        final DaysEntity overlapAllDaysEntity = new DaysEntity(null, null, null,
                EIGHT_HOURS, TWENTY_HOURS, null,
                TEN_HOURS, EIGHTEEN_HOURS, null,
                TWELVE_HOURS, SIXTEEN_HOURS, null,
                TWELVE_HOURS, SIXTEEN_HOURS, null,
                TEN_HOURS, EIGHTEEN_HOURS, null,
                EIGHT_HOURS, TWENTY_HOURS, null,
                null, null, null, null, null, null, null, null);
        mAdvisorHelper.findMealsStartingEarly(overlapAllDaysEntity, candidates, found);
        assertEquals(2, found.size());
        assertTrue(found.contains(Meals.BREAKFAST));
        assertTrue(found.contains(Meals.SUPPER));

        final DaysEntity lastStartsEarlyDaysEntity = new DaysEntity(null, null, null,
                SIX_HOURS, ELEVEN_HOURS, null,
                ELEVEN_HOURS, FIFTEEN_HOURS, null,
                FIFTEEN_HOURS, EIGHTEEN_HOURS, null,
                EIGHTEEN_HOURS, TWENTY_HOURS, null,
                TWENTY_HOURS, TWENTY_ONE_HOURS, null,
                ZERO_HOURS, SIX_HOURS, null,
                null, null, null, null, null, null, null, null);
        mAdvisorHelper.findMealsStartingEarly(lastStartsEarlyDaysEntity, candidates, found);
        assertEquals(1, found.size());
        assertTrue(found.contains(Meals.SUPPER));
    }

    public void testFindMealsEndingBeforeTimeWithEmptyCandidatesMustReturnCorrectValues() {
        final List<Integer> emptyCandidates = new ArrayList<Integer>(Meals.getMealsCount() - 1);
        final List<Integer> found = new ArrayList<Integer>(Meals.getMealsCount() - 1);

        final DaysEntity commonDaysEntity = new DaysEntity(null, null, null,
                EIGHT_HOURS, TEN_HOURS, null,
                TEN_HOURS, TWELVE_HOURS, null,
                TWELVE_HOURS, FOURTEEN_HOURS, null,
                FOURTEEN_HOURS, SIXTEEN_HOURS, null,
                SIXTEEN_HOURS, EIGHTEEN_HOURS, null,
                EIGHTEEN_HOURS, TWENTY_HOURS, null,
                null, null, null, null, null, null, null, null);
        mAdvisorHelper.findMealsEndingBeforeTime(TWENTY_ONE_HOURS, commonDaysEntity, emptyCandidates, found);
        assertTrue(found.isEmpty());
    }

    public void testFindMealsEndingBeforeTimeMustReturnCorrectValues() {
        final List<Integer> candidates = new ArrayList<Integer>(Meals.getMealsCount() - 1);
        candidates.add(Meals.BREAKFAST);
        candidates.add(Meals.BRUNCH);
        candidates.add(Meals.LUNCH);
        candidates.add(Meals.SNACK);
        candidates.add(Meals.DINNER);
        candidates.add(Meals.SUPPER);
        final List<Integer> found = new ArrayList<Integer>(Meals.getMealsCount() - 1);

        final DaysEntity commonDaysEntity = new DaysEntity(null, null, null,
                EIGHT_HOURS, TEN_HOURS, null,
                TEN_HOURS, TWELVE_HOURS, null,
                TWELVE_HOURS, FOURTEEN_HOURS, null,
                FOURTEEN_HOURS, SIXTEEN_HOURS, null,
                SIXTEEN_HOURS, EIGHTEEN_HOURS, null,
                EIGHTEEN_HOURS, TWENTY_HOURS, null,
                null, null, null, null, null, null, null, null);
        mAdvisorHelper.findMealsEndingBeforeTime(TWENTY_ONE_HOURS, commonDaysEntity, candidates, found);
        assertEquals(6, found.size());
        assertTrue(found.contains(Meals.BREAKFAST));
        assertTrue(found.contains(Meals.BRUNCH));
        assertTrue(found.contains(Meals.LUNCH));
        assertTrue(found.contains(Meals.SNACK));
        assertTrue(found.contains(Meals.DINNER));
        assertTrue(found.contains(Meals.SUPPER));

        mAdvisorHelper.findMealsEndingBeforeTime(SEVEN_HOURS, commonDaysEntity, candidates, found);
        assertTrue(found.isEmpty());

        mAdvisorHelper.findMealsEndingBeforeTime(FOURTEEN_HOURS, commonDaysEntity, candidates, found);
        assertEquals(2, found.size());
        assertTrue(found.contains(Meals.BREAKFAST));
        assertTrue(found.contains(Meals.BRUNCH));
    }

    public void testFindClosestMealsEndingBeforeTimeWithEmptyCandidatesMustReturnCorrectValues() {
        final List<Integer> emptyCandidates = new ArrayList<Integer>(Meals.getMealsCount() - 1);
        final List<Integer> found = new ArrayList<Integer>(Meals.getMealsCount() - 1);

        final DaysEntity commonDaysEntity = new DaysEntity(null, null, null,
                EIGHT_HOURS, TEN_HOURS, null,
                TEN_HOURS, TWELVE_HOURS, null,
                TWELVE_HOURS, FOURTEEN_HOURS, null,
                FOURTEEN_HOURS, SIXTEEN_HOURS, null,
                SIXTEEN_HOURS, EIGHTEEN_HOURS, null,
                EIGHTEEN_HOURS, TWENTY_HOURS, null,
                null, null, null, null, null, null, null, null);
        mAdvisorHelper.findClosestMealsEndingBeforeTime(TWENTY_ONE_HOURS, commonDaysEntity, emptyCandidates, found);
        assertTrue(found.isEmpty());
    }

    public void testFindClosestMealsEndingBeforeTimeMustReturnCorrectValues() {
        final List<Integer> candidates = new ArrayList<Integer>(Meals.getMealsCount() - 1);
        candidates.add(Meals.BREAKFAST);
        candidates.add(Meals.BRUNCH);
        candidates.add(Meals.LUNCH);
        candidates.add(Meals.SNACK);
        candidates.add(Meals.DINNER);
        candidates.add(Meals.SUPPER);
        final List<Integer> found = new ArrayList<Integer>(Meals.getMealsCount() - 1);

        final DaysEntity commonDaysEntity = new DaysEntity(null, null, null,
                EIGHT_HOURS, TEN_HOURS, null,
                TEN_HOURS, TWELVE_HOURS, null,
                TWELVE_HOURS, FOURTEEN_HOURS, null,
                FOURTEEN_HOURS, SIXTEEN_HOURS, null,
                SIXTEEN_HOURS, EIGHTEEN_HOURS, null,
                EIGHTEEN_HOURS, TWENTY_HOURS, null,
                null, null, null, null, null, null, null, null);
        mAdvisorHelper.findClosestMealsEndingBeforeTime(TWENTY_ONE_HOURS, commonDaysEntity, candidates, found);
        assertEquals(1, found.size());
        assertTrue(found.contains(Meals.SUPPER));

        mAdvisorHelper.findClosestMealsEndingBeforeTime(SEVEN_HOURS, commonDaysEntity, candidates, found);
        assertTrue(found.isEmpty());

        mAdvisorHelper.findClosestMealsEndingBeforeTime(FOURTEEN_HOURS, commonDaysEntity, candidates, found);
        assertEquals(1, found.size());
        assertTrue(found.contains(Meals.BRUNCH));

        final DaysEntity overlapAllDaysEntity = new DaysEntity(null, null, null,
                EIGHT_HOURS, TWENTY_HOURS, null,
                TEN_HOURS, EIGHTEEN_HOURS, null,
                TWELVE_HOURS, SIXTEEN_HOURS, null,
                TWELVE_HOURS, SIXTEEN_HOURS, null,
                TEN_HOURS, EIGHTEEN_HOURS, null,
                EIGHT_HOURS, TWENTY_HOURS, null,
                null, null, null, null, null, null, null, null);
        mAdvisorHelper.findClosestMealsEndingBeforeTime(TWENTY_ONE_HOURS, overlapAllDaysEntity, candidates, found);
        assertEquals(2, found.size());
        assertTrue(found.contains(Meals.BREAKFAST));
        assertTrue(found.contains(Meals.SUPPER));
    }

    public void testFindMealsStartingAfterTimeWithEmptyCandidatesMustReturnCorrectValues() {
        final List<Integer> emptyCandidates = new ArrayList<Integer>(Meals.getMealsCount() - 1);
        final List<Integer> found = new ArrayList<Integer>(Meals.getMealsCount() - 1);

        final DaysEntity commonDaysEntity = new DaysEntity(null, null, null,
                EIGHT_HOURS, TEN_HOURS, null,
                TEN_HOURS, TWELVE_HOURS, null,
                TWELVE_HOURS, FOURTEEN_HOURS, null,
                FOURTEEN_HOURS, SIXTEEN_HOURS, null,
                SIXTEEN_HOURS, EIGHTEEN_HOURS, null,
                EIGHTEEN_HOURS, TWENTY_HOURS, null,
                null, null, null, null, null, null, null, null);
        mAdvisorHelper.findMealsStartingAfterTime(SEVEN_HOURS, commonDaysEntity, emptyCandidates, found);
        assertTrue(found.isEmpty());
    }

    public void testFindMealsStartingAfterTimeMustReturnCorrectValues() {
        final List<Integer> candidates = new ArrayList<Integer>(Meals.getMealsCount() - 1);
        candidates.add(Meals.BREAKFAST);
        candidates.add(Meals.BRUNCH);
        candidates.add(Meals.LUNCH);
        candidates.add(Meals.SNACK);
        candidates.add(Meals.DINNER);
        candidates.add(Meals.SUPPER);
        final List<Integer> found = new ArrayList<Integer>(Meals.getMealsCount() - 1);

        final DaysEntity commonDaysEntity = new DaysEntity(null, null, null,
                EIGHT_HOURS, TEN_HOURS, null,
                TEN_HOURS, TWELVE_HOURS, null,
                TWELVE_HOURS, FOURTEEN_HOURS, null,
                FOURTEEN_HOURS, SIXTEEN_HOURS, null,
                SIXTEEN_HOURS, EIGHTEEN_HOURS, null,
                EIGHTEEN_HOURS, TWENTY_HOURS, null,
                null, null, null, null, null, null, null, null);
        mAdvisorHelper.findMealsStartingAfterTime(SEVEN_HOURS, commonDaysEntity, candidates, found);
        assertEquals(6, found.size());
        assertTrue(found.contains(Meals.BREAKFAST));
        assertTrue(found.contains(Meals.BRUNCH));
        assertTrue(found.contains(Meals.LUNCH));
        assertTrue(found.contains(Meals.SNACK));
        assertTrue(found.contains(Meals.DINNER));
        assertTrue(found.contains(Meals.SUPPER));

        mAdvisorHelper.findMealsStartingAfterTime(TWENTY_ONE_HOURS, commonDaysEntity, candidates, found);
        assertTrue(found.isEmpty());

        mAdvisorHelper.findMealsStartingAfterTime(FOURTEEN_HOURS, commonDaysEntity, candidates, found);
        assertEquals(2, found.size());
        assertTrue(found.contains(Meals.DINNER));
        assertTrue(found.contains(Meals.SUPPER));
    }

    public void testFindClosestMealsStartingAfterTimeWithEmptyCandidatesMustReturnCorrectValues() {
        final List<Integer> emptyCandidates = new ArrayList<Integer>(Meals.getMealsCount() - 1);
        final List<Integer> found = new ArrayList<Integer>(Meals.getMealsCount() - 1);

        final DaysEntity commonDaysEntity = new DaysEntity(null, null, null,
                EIGHT_HOURS, TEN_HOURS, null,
                TEN_HOURS, TWELVE_HOURS, null,
                TWELVE_HOURS, FOURTEEN_HOURS, null,
                FOURTEEN_HOURS, SIXTEEN_HOURS, null,
                SIXTEEN_HOURS, EIGHTEEN_HOURS, null,
                EIGHTEEN_HOURS, TWENTY_HOURS, null,
                null, null, null, null, null, null, null, null);
        mAdvisorHelper.findClosestMealsStartingAfterTime(SEVEN_HOURS, commonDaysEntity, emptyCandidates, found);
        assertTrue(found.isEmpty());
    }

    public void testFindClosestMealsStartingAfterTimeMustReturnCorrectValues() {
        final List<Integer> candidates = new ArrayList<Integer>(Meals.getMealsCount() - 1);
        candidates.add(Meals.BREAKFAST);
        candidates.add(Meals.BRUNCH);
        candidates.add(Meals.LUNCH);
        candidates.add(Meals.SNACK);
        candidates.add(Meals.DINNER);
        candidates.add(Meals.SUPPER);
        final List<Integer> found = new ArrayList<Integer>(Meals.getMealsCount() - 1);

        final DaysEntity commonDaysEntity = new DaysEntity(null, null, null,
                EIGHT_HOURS, TEN_HOURS, null,
                TEN_HOURS, TWELVE_HOURS, null,
                TWELVE_HOURS, FOURTEEN_HOURS, null,
                FOURTEEN_HOURS, SIXTEEN_HOURS, null,
                SIXTEEN_HOURS, EIGHTEEN_HOURS, null,
                EIGHTEEN_HOURS, TWENTY_HOURS, null,
                null, null, null, null, null, null, null, null);
        mAdvisorHelper.findClosestMealsStartingAfterTime(SEVEN_HOURS, commonDaysEntity, candidates, found);
        assertEquals(1, found.size());
        assertTrue(found.contains(Meals.BREAKFAST));

        mAdvisorHelper.findClosestMealsStartingAfterTime(TWENTY_ONE_HOURS, commonDaysEntity, candidates, found);
        assertTrue(found.isEmpty());

        mAdvisorHelper.findClosestMealsStartingAfterTime(FOURTEEN_HOURS, commonDaysEntity, candidates, found);
        assertEquals(1, found.size());
        assertTrue(found.contains(Meals.DINNER));
    }

    public void testFindClosestNeighborsForTimeWithEmptyCandidatesMustReturnCorrectValues() {
        final List<Integer> emptyCandidates = new ArrayList<Integer>(Meals.getMealsCount() - 1);
        final List<Integer> found = new ArrayList<Integer>(Meals.getMealsCount() - 1);

        final DaysEntity equalsSpacedDaysEntity = new DaysEntity(null, null, null,
                SIX_HOURS, SEVEN_HOURS, null,
                NINE_HOURS, TEN_HOURS, null,
                TWELVE_HOURS, THIRTEEN_HOURS, null,
                FIFTEEN_HOURS, FOURTEEN_HOURS, null,
                EIGHTEEN_HOURS, NINETEEN_HOURS, null,
                TWENTY_ONE_HOURS, TWENTY_TWO_HOURS, null,
                null, null, null, null, null, null, null, null);
        mAdvisorHelper.findClosestNeighborsForTime(EIGHT_HOURS, equalsSpacedDaysEntity, emptyCandidates, found);
        assertTrue(found.isEmpty());
    }

    public void testFindClosestNeighborsForTimeMustReturnCorrectValues() {
        final List<Integer> candidates = new ArrayList<Integer>(Meals.getMealsCount() - 1);
        candidates.add(Meals.BREAKFAST);
        candidates.add(Meals.BRUNCH);
        candidates.add(Meals.LUNCH);
        candidates.add(Meals.SNACK);
        candidates.add(Meals.DINNER);
        candidates.add(Meals.SUPPER);
        final List<Integer> found = new ArrayList<Integer>(Meals.getMealsCount() - 1);

        final DaysEntity equalsSpacedDaysEntity = new DaysEntity(null, null, null,
                SIX_HOURS, SEVEN_HOURS, null,
                NINE_HOURS, TEN_HOURS, null,
                TWELVE_HOURS, THIRTEEN_HOURS, null,
                FIFTEEN_HOURS, FOURTEEN_HOURS, null,
                EIGHTEEN_HOURS, NINETEEN_HOURS, null,
                TWENTY_ONE_HOURS, TWENTY_TWO_HOURS, null,
                null, null, null, null, null, null, null, null);
        mAdvisorHelper.findClosestNeighborsForTime(EIGHT_HOURS, equalsSpacedDaysEntity, candidates, found);
        assertEquals(2, found.size());
        assertTrue(found.contains(Meals.BREAKFAST));
        assertTrue(found.contains(Meals.BRUNCH));

        mAdvisorHelper.findClosestNeighborsForTime(TWENTY_TWO_HOURS, equalsSpacedDaysEntity, candidates, found);
        assertEquals(1, found.size());
        assertTrue(found.contains(Meals.DINNER));

        mAdvisorHelper.findClosestNeighborsForTime(SIX_HOURS, equalsSpacedDaysEntity, candidates, found);
        assertEquals(1, found.size());
        assertTrue(found.contains(Meals.BRUNCH));
    }

    public void testFindMealsWithPointsMinorGoalWithEmptyCandidatesMustReturnCorrectValues() {
        final List<Integer> emptyCandidates = new ArrayList<Integer>(Meals.getMealsCount() - 1);
        final List<Integer> found = new ArrayList<Integer>(Meals.getMealsCount() - 1);

        final DaysEntity commonDaysEntity = new DaysEntity(null, null, null,
                EIGHT_HOURS, TEN_HOURS, 1.0f,
                TEN_HOURS, TWELVE_HOURS, 1.0f,
                TWELVE_HOURS, FOURTEEN_HOURS, 1.0f,
                FOURTEEN_HOURS, SIXTEEN_HOURS, 1.0f,
                SIXTEEN_HOURS, EIGHTEEN_HOURS, 1.0f,
                EIGHTEEN_HOURS, TWENTY_HOURS, 1.0f,
                null, null, null, null, null, null, null, null);
        final UsageSummary emptyUsageSummary = new UsageSummary();
        emptyUsageSummary.setBreakfastUsed(0);
        emptyUsageSummary.setBrunchUsed(0);
        emptyUsageSummary.setLunchUsed(0);
        emptyUsageSummary.setSnackUsed(0);
        emptyUsageSummary.setDinnerUsed(0);
        emptyUsageSummary.setSupperUsed(0);
        mAdvisorHelper.findMealsWithPointsMinorGoal(commonDaysEntity, emptyUsageSummary, emptyCandidates, found);
        assertTrue(found.isEmpty());
    }

    public void testFindMealsWithPointsMinorGoalMustReturnCorrectValues() {
        final List<Integer> candidates = new ArrayList<Integer>(Meals.getMealsCount() - 1);
        candidates.add(Meals.BREAKFAST);
        candidates.add(Meals.BRUNCH);
        candidates.add(Meals.LUNCH);
        candidates.add(Meals.SNACK);
        candidates.add(Meals.DINNER);
        candidates.add(Meals.SUPPER);
        final List<Integer> found = new ArrayList<Integer>(Meals.getMealsCount() - 1);

        final DaysEntity commonDaysEntity = new DaysEntity(null, null, null,
                EIGHT_HOURS, TEN_HOURS, 1.0f,
                TEN_HOURS, TWELVE_HOURS, 1.0f,
                TWELVE_HOURS, FOURTEEN_HOURS, 1.0f,
                FOURTEEN_HOURS, SIXTEEN_HOURS, 1.0f,
                SIXTEEN_HOURS, EIGHTEEN_HOURS, 1.0f,
                EIGHTEEN_HOURS, TWENTY_HOURS, 1.0f,
                null, null, null, null, null, null, null, null);
        final UsageSummary emptyUsageSummary = new UsageSummary();
        emptyUsageSummary.setBreakfastUsed(0);
        emptyUsageSummary.setBrunchUsed(0);
        emptyUsageSummary.setLunchUsed(0);
        emptyUsageSummary.setSnackUsed(0);
        emptyUsageSummary.setDinnerUsed(0);
        emptyUsageSummary.setSupperUsed(0);
        mAdvisorHelper.findMealsWithPointsMinorGoal(commonDaysEntity, emptyUsageSummary, candidates, found);
        assertEquals(6, found.size());
        assertTrue(found.contains(Meals.BREAKFAST));
        assertTrue(found.contains(Meals.BRUNCH));
        assertTrue(found.contains(Meals.LUNCH));
        assertTrue(found.contains(Meals.SNACK));
        assertTrue(found.contains(Meals.DINNER));
        assertTrue(found.contains(Meals.SUPPER));

        final UsageSummary fullUsageSummary = new UsageSummary();
        fullUsageSummary.setBreakfastUsed(1000);
        fullUsageSummary.setBrunchUsed(1000);
        fullUsageSummary.setLunchUsed(1000);
        fullUsageSummary.setSnackUsed(1000);
        fullUsageSummary.setDinnerUsed(1000);
        fullUsageSummary.setSupperUsed(1000);
        mAdvisorHelper.findMealsWithPointsMinorGoal(commonDaysEntity, fullUsageSummary, candidates, found);
        assertTrue(found.isEmpty());

        final UsageSummary onlySnackUsageSummary = new UsageSummary();
        onlySnackUsageSummary.setBreakfastUsed(1000);
        onlySnackUsageSummary.setBrunchUsed(1000);
        onlySnackUsageSummary.setLunchUsed(1000);
        onlySnackUsageSummary.setSnackUsed(0);
        onlySnackUsageSummary.setDinnerUsed(1000);
        onlySnackUsageSummary.setSupperUsed(1000);
        mAdvisorHelper.findMealsWithPointsMinorGoal(commonDaysEntity, onlySnackUsageSummary, candidates, found);
        assertEquals(1, found.size());
        assertTrue(found.contains(Meals.SNACK));
    }
}