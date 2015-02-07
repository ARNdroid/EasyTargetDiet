package br.com.arndroid.etdiet.virtualweek;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;

import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.provider.days.DaysManager;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageManager;
import br.com.arndroid.etdiet.provider.parametershistory.ParametersHistoryManager;
import br.com.arndroid.etdiet.utils.DateUtils;

public class VirtualWeekEngine {

    public static final int DAYS_IN_A_WEEK = 7;
    public static final int LAST_WEEKDAY_INDEX = DAYS_IN_A_WEEK - 1;
    public static final int FIRST_WEEKDAY_INDEX = 0;
    public static final int NO_INDEX = -1;
    private static final float FLOAT_ZERO = 0.0f;
    private static final float FORCE_CHAIN_CALC = -1.0f;
    final private Context mContext;
    private WeekPeriod mWeekPeriod;
    final private DaySummary[] mDaySummaryArray = new DaySummary[DAYS_IN_A_WEEK];
    private float mInitialWeeklyAllowance;

    public VirtualWeekEngine(Context context, Date referenceDate) {
        mContext = context;

        for(int i = FIRST_WEEKDAY_INDEX; i < DAYS_IN_A_WEEK; i++) {
            mDaySummaryArray[i] = new DaySummary();
        }

        buildAllDaysForReferenceDate(referenceDate);
        calculateAllDays();
    }

    private void calculateAllDays() {

        for(int i = FIRST_WEEKDAY_INDEX; i < DAYS_IN_A_WEEK; i++) {
            mDaySummaryArray[i].setWeeklyAllowanceAfterUsage(FORCE_CHAIN_CALC);
            mDaySummaryArray[i].setExerciseToCarry(FORCE_CHAIN_CALC);
        }
        calculateDayAndNextIfNecessary(FIRST_WEEKDAY_INDEX);
    }

    public void calculateDayAndNextIfNecessary(int index) {
        final DaySummary currentSummary = mDaySummaryArray[index];

        currentSummary.setUsage(new FoodsUsageManager(mContext).usageSummaryForDateId(
                currentSummary.getEntity().getDateId()));
        // Making an alias, but correct and updated:
        final UsageSummary currentUsage = currentSummary.getUsage();

        float oldWeeklyAllowanceAfterUsage = currentSummary.getWeeklyAllowanceAfterUsage();
        float oldExerciseToCarry = currentSummary.getExerciseToCarry();


        if(index == FIRST_WEEKDAY_INDEX) {
            currentSummary.setWeeklyAllowanceBeforeUsage(mInitialWeeklyAllowance);
            currentSummary.setTotalExercise(currentUsage.getExerciseDone());
        } else {
            currentSummary.setWeeklyAllowanceBeforeUsage(mDaySummaryArray[index - 1].getWeeklyAllowanceAfterUsage());
            currentSummary.setTotalExercise(mDaySummaryArray[index - 1].getExerciseToCarry()
                    + currentUsage.getExerciseDone());

        }

        computeSummary(currentSummary);

        if(oldWeeklyAllowanceAfterUsage != currentSummary.getWeeklyAllowanceAfterUsage()
                || oldExerciseToCarry != currentSummary.getExerciseToCarry()) {
            if(index != LAST_WEEKDAY_INDEX) calculateDayAndNextIfNecessary(index + 1);
        }
    }

    public static void computeSummary(DaySummary summary) {
        /*
            We have extracted this method to be used for VirtualWeekEngine internal calculations
            and as a helper to forecast the value of my points at end of day.
         */

        final UsageSummary usage = summary.getUsage();
        summary.setDiaryAllowanceAfterUsage(summary.getEntity().getAllowed()
                - usage.getTotalUsed());

        boolean exerciseAfterUsageComputed = false;
        boolean weeklyAllowanceAfterUsageComputed = false;

        final int exerciseUseMode = summary.getSettingsValues().getExerciseUseMode();
        final int exerciseUseOrder = summary.getSettingsValues().getExerciseUseOrder();
        if(summary.getDiaryAllowanceAfterUsage() < FLOAT_ZERO
                && exerciseUseMode != Contract.ParametersHistory.EXERCISE_USE_MODE_DONT_USE
                && exerciseUseOrder == Contract.ParametersHistory.EXERCISE_USE_ORDER_USE_EXERCISES_FIRST) {
            summary.setDiaryAllowanceAfterUsage(
                    summary.getDiaryAllowanceAfterUsage()
                            + summary.getTotalExercise());
            summary.setExerciseAfterUsage(FLOAT_ZERO);
            exerciseAfterUsageComputed = true;
            if(summary.getDiaryAllowanceAfterUsage() > FLOAT_ZERO) {
                summary.setExerciseAfterUsage(
                        summary.getDiaryAllowanceAfterUsage());
                summary.setDiaryAllowanceAfterUsage(FLOAT_ZERO);
            }
        }

        if(summary.getDiaryAllowanceAfterUsage() < FLOAT_ZERO) {
            summary.setDiaryAllowanceAfterUsage(
                    summary.getDiaryAllowanceAfterUsage()
                            + summary.getWeeklyAllowanceBeforeUsage());
            summary.setWeeklyAllowanceAfterUsage(FLOAT_ZERO);
            weeklyAllowanceAfterUsageComputed = true;
            if(summary.getDiaryAllowanceAfterUsage() > FLOAT_ZERO) {
                summary.setWeeklyAllowanceAfterUsage(summary.getDiaryAllowanceAfterUsage());
                summary.setDiaryAllowanceAfterUsage(FLOAT_ZERO);
            }
        }

        if(summary.getDiaryAllowanceAfterUsage() < FLOAT_ZERO
                && exerciseUseMode != Contract.ParametersHistory.EXERCISE_USE_MODE_DONT_USE
                && exerciseUseOrder == Contract.ParametersHistory.EXERCISE_USE_ORDER_USE_WEEKLY_ALLOWANCE_FIRST) {
            summary.setDiaryAllowanceAfterUsage(summary.getDiaryAllowanceAfterUsage()
                + summary.getTotalExercise());
            summary.setExerciseAfterUsage(FLOAT_ZERO);
            exerciseAfterUsageComputed = true;
            if(summary.getDiaryAllowanceAfterUsage() > FLOAT_ZERO) {
                summary.setExerciseAfterUsage(summary.getDiaryAllowanceAfterUsage());
                summary.setDiaryAllowanceAfterUsage(FLOAT_ZERO);
            }
        }

        if(!weeklyAllowanceAfterUsageComputed) {
            summary.setWeeklyAllowanceAfterUsage(summary.getWeeklyAllowanceBeforeUsage());
        }

        if(!exerciseAfterUsageComputed) {
            summary.setExerciseAfterUsage(summary.getTotalExercise());
        }

        if(exerciseUseMode == Contract.ParametersHistory.EXERCISE_USE_MODE_USE_AND_ACCUMULATE) {
            summary.setExerciseToCarry(summary.getExerciseAfterUsage());
        } else {
            summary.setExerciseToCarry(FLOAT_ZERO);
        }

        final float allowed = summary.getEntity().getAllowed();
        final float planned = summary.getEntity().getTotalGoalForMeals();
        float reservesAtStartOfDay = summary.getWeeklyAllowanceBeforeUsage();
        if (summary.getSettingsValues().getExerciseUseMode()
                != Contract.ParametersHistory.EXERCISE_USE_MODE_DONT_USE) {
            reservesAtStartOfDay += summary.getTotalExercise();
        }
        if (planned > allowed) {
            if (planned - allowed <= reservesAtStartOfDay) {
                // Scene 1: planned > allowed and reserves will cover.
                summary.setPlannedBeforeUsage(planned);
            } else {
                // Scene 2: planned > allowed but reserves won't cover.
                summary.setPlannedBeforeUsage(allowed + reservesAtStartOfDay);
            }
        } else {
            // Scene 3: planned <= allowed
            summary.setPlannedBeforeUsage(planned);
        }

        summary.setPlannedAfterUsage(summary.getPlannedBeforeUsage() - usage.getTotalUsed());
    }

    private void buildAllDaysForReferenceDate(Date referenceDate) {

        mWeekPeriod = new WeekPeriod(mContext, referenceDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mWeekPeriod.getInitialDate());
        for(int i = FIRST_WEEKDAY_INDEX; i < DAYS_IN_A_WEEK; i++) {
            mDaySummaryArray[i].setEntity(buildDayForDate(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        ParametersHistoryManager parametersHistoryManager = new ParametersHistoryManager(mContext);
        Date lastWeekday = DateUtils.dateIdToDate(mDaySummaryArray[LAST_WEEKDAY_INDEX].getEntity().getDateId());
        mInitialWeeklyAllowance = parametersHistoryManager.getWeeklyAllowanceForDate(
                lastWeekday);
        final int exerciseUseMode = parametersHistoryManager.getExerciseUseModeForDate(lastWeekday);
        final int exerciseUseOrder = parametersHistoryManager.getExerciseUseOrderForDate(lastWeekday);
        for(int i = FIRST_WEEKDAY_INDEX; i < DAYS_IN_A_WEEK; i++) {
            mDaySummaryArray[i].setSettingsValues(new SettingsValues(exerciseUseMode, exerciseUseOrder));
        }
    }

    private DaysEntity buildDayForDate(Date date) {
        return new DaysManager(mContext).dayFromDate(date);
    }

    public void rebuildAndCalculateDayForIndex(int index) {
        final Date date = DateUtils.dateIdToDate(mDaySummaryArray[index].getEntity().getDateId());
        mDaySummaryArray[index].setEntity(buildDayForDate(date));
        calculateDayAndNextIfNecessary(index);
    }

    public Long getIdForIndex(int index) {
        return mDaySummaryArray[index].getEntity().getId();
    }

    public String getDateIdForIndex(int index) {
        return mDaySummaryArray[index].getEntity().getDateId();
    }

    public float getTotalUsedForIndex(int index) {
        return mDaySummaryArray[index].getUsage().getTotalUsed();
    }

    public float getExerciseAfterUsageForIndex(int index) {
        return mDaySummaryArray[index].getExerciseAfterUsage();
    }

    public float getAllowedForIndex(int index) {
        return mDaySummaryArray[index].getEntity().getAllowed();
    }

    public float getWeeklyAllowanceBeforeUsageForIndex(int index) {
        return mDaySummaryArray[index].getWeeklyAllowanceBeforeUsage();
    }

    public float getTotalExerciseForIndex(int index) {
        return mDaySummaryArray[index].getTotalExercise();
    }

    public float getWeeklyAllowanceAfterUsageForIndex(int index) {
        return mDaySummaryArray[index].getWeeklyAllowanceAfterUsage();
    }

    public float getDiaryAllowanceAfterUsageForIndex(int index) {
        return mDaySummaryArray[index].getDiaryAllowanceAfterUsage();
    }

    @SuppressWarnings("UnusedDeclaration")
    public Date getInitialDateForPeriod() {
        return mWeekPeriod.getInitialDate();
    }

    @SuppressWarnings("UnusedDeclaration")
    public Date getFinalDateForPeriod() {
        return mWeekPeriod.getFinalDate();
    }

    @SuppressWarnings("UnusedDeclaration")
    public Date getReferenceDateForPeriod() {
        return mWeekPeriod.getReferenceDate();
    }

    public DaySummary daySummaryForIndex(int index) {
        return new DaySummary(mDaySummaryArray[index]);
    }

    public DaySummary daySummaryForDateId(String dateId) {

        final int index = indexForDateId(dateId);
        return index == NO_INDEX ? null : mDaySummaryArray[index];
    }

    private int indexForDateId(String dateId) {

        for (int i = 0; i < mDaySummaryArray.length; i++) {
            if (mDaySummaryArray[i].getEntity().getDateId().equals(dateId)) {
                return i;
            }
        }
        return NO_INDEX;
    }
}
