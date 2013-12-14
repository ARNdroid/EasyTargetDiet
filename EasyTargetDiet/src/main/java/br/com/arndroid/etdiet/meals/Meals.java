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
    public static final int BREAKFAST = 0;
    public static final int BRUNCH = 1;
    public static final int LUNCH = 2;
    public static final int SNACK = 3;
    public static final int DINNER = 4;
    public static final int SUPPER = 5;
    public static final int EXERCISE = 6;

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
            case EXERCISE:
                return entity.getExerciseGoal();
            default:
                throw new IllegalArgumentException("Invalid meal=" + meal);
        }
    }
}