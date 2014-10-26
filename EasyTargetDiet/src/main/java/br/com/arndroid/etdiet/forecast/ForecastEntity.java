package br.com.arndroid.etdiet.forecast;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.utils.DateUtils;

public class ForecastEntity implements Parcelable {

    public static final int STRAIGHT_TO_GOAL = 0;
    public static final int GOING_TO_GOAL_WITH_HELP = 1;
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
            case ForecastEntity.GOING_TO_GOAL_WITH_HELP:
                return R.string.forecast_going_to_goal_with_help_description;
            case ForecastEntity.OUT_OF_GOAL_BUT_CAN_RETURN:
                return R.string.forecast_out_of_goal_but_can_return_description;
            case ForecastEntity.OUT_OF_GOAL:
                return R.string.forecast_out_of_goal_description;
            default:
                throw new IllegalStateException("Invalid forecast type=" + getForecastType());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {
        destination.writeString(DateUtils.dateToDateId(mReferenceDate));
        destination.writeFloat(mUsed);
        destination.writeFloat(mToUse);
        destination.writeFloat(mForecastUsed);
        destination.writeFloat(mBalanceFromDailyAllowance);
        destination.writeInt(mForecastType);
    }

    public static final Parcelable.Creator<ForecastEntity> CREATOR
            = new Parcelable.Creator<ForecastEntity>() {

        public ForecastEntity createFromParcel(Parcel in) {
            final ForecastEntity result = new ForecastEntity();

            result.setReferenceDate(DateUtils.dateIdToDate(in.readString()));
            result.setUsed(in.readFloat());
            result.setToUse(in.readFloat());
            result.setForecastUsed(in.readFloat());
            result.setBalanceFromDailyAllowance(in.readFloat());
            result.setForecastType(in.readInt());

            return result;
        }

        public ForecastEntity[] newArray(int size) {
            return new ForecastEntity[size];
        }
    };
}
