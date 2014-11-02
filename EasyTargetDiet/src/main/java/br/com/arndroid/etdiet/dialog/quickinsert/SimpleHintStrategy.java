package br.com.arndroid.etdiet.dialog.quickinsert;

import android.content.Context;

import java.util.Date;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersEntity;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersManager;
import br.com.arndroid.etdiet.utils.DateUtils;

public class SimpleHintStrategy extends BaseHintStrategy {

    private static final int NO_TIME = -1;

    @Override
    public void initialize(Context context, FoodsUsageEntity foodsUsageEntity) {
        final boolean isDateIdCurrentDate = DateUtils.isDateIdCurrentDate(foodsUsageEntity.getDateId());
        updateMeal(context, foodsUsageEntity, isDateIdCurrentDate);
        updateTime(context, foodsUsageEntity, isDateIdCurrentDate);
        updateValue(context, foodsUsageEntity);
    }

    @Override
    public boolean onDateChanged(Context context, FoodsUsageEntity foodsUsageEntity) {
        boolean result = super.onDateChanged(context, foodsUsageEntity);

        final boolean isDateIdCurrentDate = DateUtils.isDateIdCurrentDate(foodsUsageEntity.getDateId());

        if (updateMeal(context, foodsUsageEntity, isDateIdCurrentDate)) {
            result = true;
        }

        if (updateTime(context, foodsUsageEntity, isDateIdCurrentDate)) {
            result = true;
        }

        if (updateValue(context, foodsUsageEntity)) {
            result = true;
        }

        return result;
    }

    @Override
    public boolean onMealChanged(Context context, FoodsUsageEntity foodsUsageEntity) {
        boolean result = super.onMealChanged(context, foodsUsageEntity);

        final boolean isDateIdCurrentDate = DateUtils.isDateIdCurrentDate(foodsUsageEntity.getDateId());

        if (updateTime(context, foodsUsageEntity, isDateIdCurrentDate)) {
            result = true;
        }

        if (updateValue(context, foodsUsageEntity)) {
            result = true;
        }

        return result;
    }

    @Override
    public boolean onTimeChanged(Context context, FoodsUsageEntity foodsUsageEntity) {
        return super.onTimeChanged(context, foodsUsageEntity);
    }

    private boolean updateValue(Context context, FoodsUsageEntity foodsUsageEntity) {
        if (isValueAHint()) {
            foodsUsageEntity.setValue(Meals.preferredUsageForMealInDate(context, foodsUsageEntity.getMeal(),
                    DateUtils.dateIdToDate(foodsUsageEntity.getDateId())));
            return true;
        }
        return false;
    }

    private boolean updateTime(Context context, FoodsUsageEntity foodsUsageEntity, boolean isDateIdCurrentDate) {
        if (isTimeAHint()) {
            if (isDateIdCurrentDate) {
                foodsUsageEntity.setTime(DateUtils.dateToTimeAsInt(new Date()));
            } else {
                final WeekdayParametersEntity parametersEntity = new WeekdayParametersManager(context).weekdayParametersFromWeekday(DateUtils.getWeekdayFromDateId(foodsUsageEntity.getDateId()));
                foodsUsageEntity.setTime(parametersEntity.getStartTimeForMeal(foodsUsageEntity.getMeal()));
            }
            return true;
        }
        return false;
    }

    private boolean updateMeal(Context context, FoodsUsageEntity foodsUsageEntity, boolean isDateIdCurrentDate) {
        if (isMealAHint()) {
            final int tempTime = isDateIdCurrentDate ? DateUtils.dateToTimeAsInt(new Date()) : NO_TIME;
            foodsUsageEntity.setMeal(Meals.preferredMealForTimeInDate(context, tempTime,
                    DateUtils.dateIdToDate(foodsUsageEntity.getDateId()), !isDateIdCurrentDate));
            return true;
        }
        return false;
    }
}
