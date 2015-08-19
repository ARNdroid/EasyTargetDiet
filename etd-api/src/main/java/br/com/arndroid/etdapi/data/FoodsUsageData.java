package br.com.arndroid.etdapi.data;

import android.os.Bundle;

import java.util.Date;

public final class FoodsUsageData {

    private static final String BUNDLE_KEY_DATE = "BUNDLE_KEY_DATE";
    private static final String BUNDLE_KEY_TIME = "BUNDLE_KEY_TIME";
    private static final String BUNDLE_KEY_MEAL = "BUNDLE_KEY_MEAL";
    private static final String BUNDLE_KEY_DESCRIPTION = "BUNDLE_KEY_DESCRIPTION";
    private static final String BUNDLE_KEY_VALUE = "BUNDLE_KEY_VALUE";

    final private Date mDate;
    final private Integer mTime;
    final private Meal mMeal;
    final private String mDescription;
    final private Float mValue;

    /*package*/ FoodsUsageData(Date date, Integer time, Meal meal, String description, Float value) {
        mDate = date;
        mTime = time;
        mMeal = meal;
        mDescription = description;
        mValue = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;

        } else if (obj instanceof FoodsUsageData) {
            FoodsUsageData temp = (FoodsUsageData) obj;

            // mDate:
            if (this.mDate != null) {
                if(!this.mDate.equals(temp.mDate))
                    return false;
            } else {
                if (temp.mDate != null)
                    return false;
            }

            // mTime:
            if (this.mTime != null) {
                if (!this.mTime.equals(temp.mTime))
                    return false;
            } else {
                if (temp.mTime != null)
                    return false;
            }

            // mMeal:
            if (this.mMeal != null) {
                if (!this.mMeal.equals(temp.mMeal))
                    return false;
            } else {
                if (temp.mMeal != null)
                    return false;
            }

            // mDescription:
            if (this.mDescription != null) {
                if (!this.mDescription.equals(temp.mDescription))
                    return false;
            } else {
                if (temp.mDescription != null)
                    return false;
            }

            // mValue:
            if (this.mValue != null) {
                if (!this.mValue.equals(temp.mValue))
                    return false;
            } else {
                if (temp.mValue != null)
                    return false;
            }

            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result;
        result = mDate == null? 0: mDate.hashCode();
        result += mTime == null? 0: mTime.hashCode();
        result += mMeal == null? 0: mMeal.hashCode();
        result += mDescription == null? 0: mDescription.hashCode();
        result += mValue == null? 0: mValue.hashCode();
        return result;
    }

    @Override
    public String toString() {

        return "["
                + "Date=" + getDate() + ", "
                + "Time=" + getTime() + ", "
                + "Meal=" + getMeal() + ", "
                + "Description=" + getDescription() + ", "
                + "Value=" + getValue()
                + "]";
    }

    public Bundle toBundle() {
        final Bundle result = new Bundle();
        result.putLong(BUNDLE_KEY_DATE, mDate.getTime());
        result.putInt(BUNDLE_KEY_TIME, mTime);
        result.putInt(BUNDLE_KEY_MEAL, mMeal.getCorrelationId());
        result.putString(BUNDLE_KEY_DESCRIPTION, mDescription);
        result.putFloat(BUNDLE_KEY_VALUE, mValue);
        return result;
    }

    public Date getDate() {
        return mDate;
    }

    public Integer getTime() {
        return mTime;
    }

    public Meal getMeal() {
        return mMeal;
    }

    public String getDescription() {
        return mDescription;
    }

    public Float getValue() {
        return mValue;
    }

    public static FoodsUsageData fromBundle(Bundle bundle) {
        return new FoodsUsageData(new Date(bundle.getLong(BUNDLE_KEY_DATE)),
                bundle.getInt(BUNDLE_KEY_TIME),
                Meal.fromInteger(bundle.getInt(BUNDLE_KEY_MEAL)),
                bundle.getString(BUNDLE_KEY_DESCRIPTION),
                bundle.getFloat(BUNDLE_KEY_VALUE));
    }
}