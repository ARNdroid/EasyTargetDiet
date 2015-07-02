package br.com.arndroid.etdapi.data;

import java.util.Date;

public final class FoodsUsageData {

    private Date mDate;
    private Integer mTime;
    private Meal mMeal;
    private String mDescription;
    private Float mValue;

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
}