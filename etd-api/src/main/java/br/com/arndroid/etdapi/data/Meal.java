package br.com.arndroid.etdapi.data;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum Meal {
    /*
        Mind the gap:
        Those correlation ids CANNOT change because they are stored in database.
     */
    BREAKFAST(0), BRUNCH(1), LUNCH(2), SNACK(3), DINNER(4), SUPPER(5), EXERCISE(6);

    public static final int sizeOfAll;
    public static final int sizeOfOnlyMeals;

    private static final Map<Integer, Meal> mapIntegerToMeal = new HashMap<>();
    private static final Set<Meal> onlyMealsUnmodifiableSet;

    static {
        // Static initializations here:
        final Meal[] meals = values();
        sizeOfAll = meals.length;
        for (Meal meal : meals) {
            mapIntegerToMeal.put(meal.getCorrelationId(), meal);
        }

        final EnumSet<Meal> notMealsSet = EnumSet.of(EXERCISE);
        onlyMealsUnmodifiableSet = Collections.unmodifiableSet(EnumSet.complementOf(notMealsSet));
        sizeOfOnlyMeals = onlyMealsUnmodifiableSet.size();
    }

    private final int mCorrelationId;

    Meal(Integer correlationId) {
        mCorrelationId = correlationId;
    }

    public Integer getCorrelationId() {
        return mCorrelationId;
    }

    // Returns meal from Integer. If invalid value, throws exception (except if value is also null).
    public static Meal fromInteger(Integer value) {
        if (value == null) return null;

        Meal result = mapIntegerToMeal.get(value);
        if (result == null) {
            throw new IllegalArgumentException("Invalid value=" + value);
        }
        return result;
    }

    public static Set<Meal> getOnlyMealsUnmodifiableSet() {
        return onlyMealsUnmodifiableSet;
    }
}

