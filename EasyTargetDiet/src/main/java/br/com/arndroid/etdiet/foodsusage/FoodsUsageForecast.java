package br.com.arndroid.etdiet.foodsusage;

import java.util.Date;

public class FoodsUsageForecast {
    public static final int STRAIGHT_TO_GOAL = 0;
    public static final int GOING_TO_GOAL_WITH_HELP = 1;
    public static final int OUT_OF_GOAL_BUT_CAN_RETURN = 2;
    public static final int OUT_OF_GOAL = 3;

    private Date mReferenceDate;
    private float mUsed;
    private float mToUse;
    private float mForecastUsed;
    private int mForecastType;

    public Date getReferenceDate() {
        return mReferenceDate;
    }

    public void setReferenceDate(Date mReferenceDate) {
        this.mReferenceDate = mReferenceDate;
    }

    public float getUsed() {
        return mUsed;
    }

    public void setUsed(float mUsed) {
        this.mUsed = mUsed;
    }

    public float getToUse() {
        return mToUse;
    }

    public void setToUse(float mToUse) {
        this.mToUse = mToUse;
    }

    public float getForecastUsed() {
        return mForecastUsed;
    }

    public void setForecastUsed(float mForecastUsed) {
        this.mForecastUsed = mForecastUsed;
    }

    public int getForecastType() {
        return mForecastType;
    }

    public void setForecastType(int forecastType) {
        this.mForecastType = forecastType;
    }
}
