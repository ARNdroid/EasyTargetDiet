package br.com.arndroid.etdiet.forecast;

import java.util.Date;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.utils.DateUtils;
import br.com.arndroid.etdiet.virtualweek.DaySummary;

public class ForecastEntity {

    private static final int ZERO_HOURS = 0;
    private static final int TWENTY_THREE_HOURS = 82800000;
    private static final int ZERO_MINUTES = 0;
    private static final int FIFTY_NINE_MINUTES = 3540000;

    public static final int STRAIGHT_TO_GOAL = 0;
    public static final int OUT_OF_GOAL_WITH_ENOUGH_RESERVES = 1;
    public static final int OUT_OF_GOAL_BUT_CAN_RETURN = 2;
    public static final int OUT_OF_GOAL = 3;

    private Date mReferenceDate;
    private float mUsed;
    private float mToUse;
    private float mForecastUsed;
    private float mBalanceFromDailyAllowance;
    private int mForecastType;

    @SuppressWarnings("UnusedDeclaration")
    public Date getReferenceDate() {
        return mReferenceDate;
    }

    public void setReferenceDate(Date mReferenceDate) {
        this.mReferenceDate = mReferenceDate;
    }

    public float getUsed() {
        return mUsed;
    }

    public void setUsed(float used) {
        mUsed = used;
    }

    public float getToUse() {
        return mToUse;
    }

    public void setToUse(float toUse) {
        mToUse = toUse;
    }

    public float getForecastUsed() {
        return mForecastUsed;
    }

    public void setForecastUsed(float forecastUsed) {
        mForecastUsed = forecastUsed;
    }

    public float getBalanceFromDailyAllowance() {
        return mBalanceFromDailyAllowance;
    }

    public void setBalanceFromDailyAllowance(float balance) {
        mBalanceFromDailyAllowance = balance;
    }

    public int getForecastType() {
        return mForecastType;
    }

    public void setForecastType(int forecastType) {
        mForecastType = forecastType;
    }

    public int getStringResourceIdForForecastType() {
        switch (getForecastType()) {
            case ForecastEntity.STRAIGHT_TO_GOAL:
                return R.string.forecast_straight_to_goal_description;
            case ForecastEntity.OUT_OF_GOAL_WITH_ENOUGH_RESERVES:
                return R.string.forecast_going_to_goal_with_help_description;
            case ForecastEntity.OUT_OF_GOAL_BUT_CAN_RETURN:
                return R.string.forecast_out_of_goal_but_can_return_description;
            case ForecastEntity.OUT_OF_GOAL:
                return R.string.forecast_out_of_goal_description;
            default:
                throw new IllegalStateException("Invalid forecast type=" + getForecastType());
        }
    }

    public static ForecastEntity getInstanceForDaySummary(DaySummary daySummary) {
        final String dateId = daySummary.getEntity().getDateId();
        final int resultCompareDateId = DateUtils.compareDateId(dateId, DateUtils.dateToDateId(new Date()));
        if (resultCompareDateId == 0) {
            // Present date:
            return Forecaster.getInstance().forecast(new Date(), daySummary);
        } else if (resultCompareDateId < 0) {
            // Paste date:
            return Forecaster.getInstance().forecast(new Date(
                    DateUtils.dateIdToDate(dateId).getTime()
                            + TWENTY_THREE_HOURS + FIFTY_NINE_MINUTES), daySummary);
        } else {
            // Future date:
            return Forecaster.getInstance().forecast(new Date(
                    DateUtils.dateIdToDate(dateId).getTime()
                            + ZERO_HOURS + ZERO_MINUTES), daySummary);
        }
    }
}
