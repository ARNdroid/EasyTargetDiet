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
    private int mExerciseUseMode;
    private int mExerciseUseOrder;

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

        currentSummary.setDiaryAllowanceAfterUsage(currentSummary.getEntity().getAllowed()
                - currentUsage.getTotalUsed());

        boolean exerciseAfterUsageComputed = false;
        boolean weeklyAllowanceAfterUsageComputed = false;

        if(currentSummary.getDiaryAllowanceAfterUsage() < FLOAT_ZERO
                && mExerciseUseMode != Contract.ParametersHistory.EXERCISE_USE_MODE_DONT_USE
                && mExerciseUseOrder == Contract.ParametersHistory.EXERCISE_USE_ORDER_USE_EXERCISES_FIRST) {
            currentSummary.setDiaryAllowanceAfterUsage(
                    currentSummary.getDiaryAllowanceAfterUsage()
                            + currentSummary.getTotalExercise());
            currentSummary.setExerciseAfterUsage(FLOAT_ZERO);
            exerciseAfterUsageComputed = true;
            if(currentSummary.getDiaryAllowanceAfterUsage() > FLOAT_ZERO) {
                currentSummary.setExerciseAfterUsage(
                        currentSummary.getDiaryAllowanceAfterUsage());
                currentSummary.setDiaryAllowanceAfterUsage(FLOAT_ZERO);
            }
        }

        if(currentSummary.getDiaryAllowanceAfterUsage() < FLOAT_ZERO) {
            currentSummary.setDiaryAllowanceAfterUsage(
                    currentSummary.getDiaryAllowanceAfterUsage()
                            + currentSummary.getWeeklyAllowanceBeforeUsage());
            currentSummary.setWeeklyAllowanceAfterUsage(FLOAT_ZERO);
            weeklyAllowanceAfterUsageComputed = true;
            if(currentSummary.getDiaryAllowanceAfterUsage() > FLOAT_ZERO) {
                currentSummary.setWeeklyAllowanceAfterUsage(currentSummary.getDiaryAllowanceAfterUsage());
                currentSummary.setDiaryAllowanceAfterUsage(FLOAT_ZERO);
            }
        }

        if(currentSummary.getDiaryAllowanceAfterUsage() < FLOAT_ZERO
                && mExerciseUseMode != Contract.ParametersHistory.EXERCISE_USE_MODE_DONT_USE
                && mExerciseUseOrder == Contract.ParametersHistory.EXERCISE_USE_ORDER_USE_WEEKLY_ALLOWANCE_FIRST) {
            currentSummary.setDiaryAllowanceAfterUsage(currentSummary.getDiaryAllowanceAfterUsage()
                + currentSummary.getTotalExercise());
            currentSummary.setExerciseAfterUsage(FLOAT_ZERO);
            exerciseAfterUsageComputed = true;
            if(currentSummary.getDiaryAllowanceAfterUsage() > FLOAT_ZERO) {
                currentSummary.setExerciseAfterUsage(currentSummary.getDiaryAllowanceAfterUsage());
                currentSummary.setDiaryAllowanceAfterUsage(FLOAT_ZERO);
            }
        }

        if(!weeklyAllowanceAfterUsageComputed) {
            currentSummary.setWeeklyAllowanceAfterUsage(currentSummary.getWeeklyAllowanceBeforeUsage());
        }

        if(!exerciseAfterUsageComputed) {
            currentSummary.setExerciseAfterUsage(currentSummary.getTotalExercise());
        }

        if(mExerciseUseMode == Contract.ParametersHistory.EXERCISE_USE_MODE_USE_AND_ACCUMULATE) {
            currentSummary.setExerciseToCarry(currentSummary.getExerciseAfterUsage());
        } else {
            currentSummary.setExerciseToCarry(FLOAT_ZERO);
        }

        if(oldWeeklyAllowanceAfterUsage != currentSummary.getWeeklyAllowanceAfterUsage()
                || oldExerciseToCarry != currentSummary.getExerciseToCarry()) {
            if(index != LAST_WEEKDAY_INDEX) calculateDayAndNextIfNecessary(index + 1);
        }
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
        mExerciseUseMode = parametersHistoryManager.getExerciseUseModeForDate(lastWeekday);
        mExerciseUseOrder = parametersHistoryManager.getExerciseUseOrderForDate(lastWeekday);
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
