package br.com.arndroid.etdiet.dialog.quickinsert;

import android.content.Context;

import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;

public abstract class BaseHintStrategy {
    private boolean mIsDateAHint;
    private boolean mIsMealAHint;
    private boolean mIsTimeAHint;
    private boolean mIsValueAHint;
    // Description hints are NOT executed here!

    @SuppressWarnings("UnusedDeclaration")
    public boolean isDateAHint() {
        return mIsDateAHint;
    }

    public void setDateAHint(boolean isDateAHint) {
        mIsDateAHint = isDateAHint;
    }

    public boolean isMealAHint() {
        return mIsMealAHint;
    }

    public void setMealAHint(boolean isMealAHint) {
        mIsMealAHint = isMealAHint;
    }

    public boolean isTimeAHint() {
        return mIsTimeAHint;
    }

    public void setTimeAHint(boolean isTimeAHint) {
        mIsTimeAHint = isTimeAHint;
    }

    public boolean isValueAHint() {
        return mIsValueAHint;
    }

    public void setValueAHint(boolean isValueAHint) {
        mIsValueAHint = isValueAHint;
    }

    public abstract void initialize(Context context, FoodsUsageEntity foodsUsageEntity);

    public boolean onDateChanged(Context context, FoodsUsageEntity foodsUsageEntity) {
        setDateAHint(false);
        return false;
    }

    public boolean onMealChanged(Context context, FoodsUsageEntity foodsUsageEntity) {
        setMealAHint(false);
        return false;
    }

    @SuppressWarnings({"SameReturnValue", "UnusedParameters", "UnusedReturnValue"})
    public boolean onTimeChanged(Context context, FoodsUsageEntity foodsUsageEntity) {
        setTimeAHint(false);
        return false;
    }

    @SuppressWarnings({"SameReturnValue", "UnusedParameters", "UnusedReturnValue"})
    public boolean onValueChanged(Context context, FoodsUsageEntity foodsUsageEntity) {
        setValueAHint(false);
        return false;
    }

    @Override
    public String toString() {
        return "BaseHintStrategy{" +
                "mIsDateAHint=" + mIsDateAHint +
                ", mIsMealAHint=" + mIsMealAHint +
                ", mIsTimeAHint=" + mIsTimeAHint +
                ", mIsValueAHint=" + mIsValueAHint +
                '}';
    }
}
