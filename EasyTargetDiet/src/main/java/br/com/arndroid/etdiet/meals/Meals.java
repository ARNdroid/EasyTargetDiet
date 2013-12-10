package br.com.arndroid.etdiet.meals;

import android.content.Context;

import java.util.Date;

import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.provider.days.DaysManager;

public final class Meals {

    // Utility class
    private Meals() {

    }

    // TODO: This is a big problem. Actually the real values are loaded from a string array resource!
    // TODO: we need to think a solution more secure!
    // The value of this constants can NOT
    // be changed since their values are stored
    // in database
    static final public int EXERCISE = -1;
    static final public int BREAKFAST = 0;
    static final public int BRUNCH = 1;
    static final public int LUNCH = 2;
    static final public int SNACK = 3;
    static final public int DINNER = 4;
    static final public int SUPPER = 5;

    public static int preferredMealForTimeInDate(Context context, int time, Date date) {
        final DaysManager manager = new DaysManager(context);
        DaysEntity entity = manager.dayFromDate(date);

        if (time >= entity.getBreakfastStartTime() && time <= entity.getBreakfastEndTime()) {
            return BREAKFAST;
        } else if (time >= entity.getBrunchStartTime() && time <= entity.getBrunchEndTime()) {
            return BRUNCH;
        } else if (time >= entity.getLunchStartTime() && time <= entity.getLunchEndTime()) {
            return LUNCH;
        } else if (time >= entity.getSnackStartTime() && time <= entity.getSnackEndTime()) {
            return SNACK;
        } else if (time >= entity.getDinnerStartTime() && time <= entity.getDinnerEndTime()) {
            return DINNER;
        } else if (time >= entity.getSupperStartTime() && time <= entity.getSupperEndTime()) {
            return SUPPER;
        } else {
            return BREAKFAST;
        }
    }

    public static float preferredUsageForMealInDate(Context context, int meal, Date date) {
        final DaysManager manager = new DaysManager(context);
        DaysEntity entity = manager.dayFromDate(date);
        switch (meal) {
            case BREAKFAST:
                return entity.getBreakfastGoal();
            case BRUNCH:
                return entity.getBrunchGoal();
            case LUNCH:
                return entity.getLunchGoal();
            case SNACK:
                return entity.getSnackGoal();
            case DINNER:
                return entity.getDinnerGoal();
            case SUPPER:
                return entity.getSupperGoal();
            default:
                throw new IllegalArgumentException("Invalid meal=" + meal);
        }
    }
}