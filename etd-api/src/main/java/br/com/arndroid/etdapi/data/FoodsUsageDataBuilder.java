package br.com.arndroid.etdapi.data;

import java.util.Date;

public final class FoodsUsageDataBuilder {

    private Date mDate;
    private Integer mTime;
    private Meal mMeal;
    private String mDescription;
    private Float mValue;

    public FoodsUsageDataBuilder withDate(Date date) {
        mDate = date;
        return this;
    }

    public FoodsUsageDataBuilder withTime(Integer time) {
        mTime = time;
        return this;
    }

    public FoodsUsageDataBuilder withMeal(Meal meal) {
        mMeal = meal;
        return this;
    }

    public FoodsUsageDataBuilder withDescription(String description) {
        mDescription = description;
        return this;
    }

    public FoodsUsageDataBuilder withValue(Float value) {
        mValue = value;
        return this;
    }

    public FoodsUsageData build() {
        return new FoodsUsageData(mDate, mTime, mMeal, mDescription, mValue);
    }
}