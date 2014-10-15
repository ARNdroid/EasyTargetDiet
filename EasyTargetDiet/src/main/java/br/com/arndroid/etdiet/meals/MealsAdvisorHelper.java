package br.com.arndroid.etdiet.meals;

import java.util.ArrayList;
import java.util.List;

import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.virtualweek.UsageSummary;

public class MealsAdvisorHelper {

    public void findMealsInPeriod(int time, DaysEntity daysEntity, List<Integer> candidates, List<Integer> result) {
        validateListParametersOrThrow(candidates, result);
        result.clear();

        for (Integer meal : candidates) {
            if (time >= getStartTimeInDaysEntityForMeal(daysEntity, meal) &&
                    time <= getEndTimeInDaysEntityForMeal(daysEntity, meal)) {
                result.add(meal);
            }
        }
    }

    public void findMealsWithNoPoints(UsageSummary usageSummary, List<Integer> candidates, List<Integer> result) {

        validateListParametersOrThrow(candidates, result);
        result.clear();

        for (Integer meal : candidates) {
            if (getMealUsageInSummaryForMeal(usageSummary, meal) == 0) {
                result.add(meal);
            }
        }
    }

    public void findMealsWithPointsMinorGoal(DaysEntity daysEntity, UsageSummary usageSummary, List<Integer> candidates, List<Integer> result) {

        validateListParametersOrThrow(candidates, result);
        result.clear();

        for (Integer meal : candidates) {
            if (getMealUsageInSummaryForMeal(usageSummary, meal)
                    < getGoalInDaysEntityForMeal(daysEntity, meal)) {
                result.add(meal);
            }
        }
    }

    public void findMealsWithMinorInterval(DaysEntity daysEntity, List<Integer> candidates,
                                           List<Integer> result) {

        validateListParametersOrThrow(candidates, result);
        result.clear();

        if (!candidates.isEmpty()) {
            result.add(candidates.get(0));
            int minorInterval = getEndTimeInDaysEntityForMeal(daysEntity, candidates.get(0)) -
                    getStartTimeInDaysEntityForMeal(daysEntity, candidates.get(0));
            for (int i = 1; i < candidates.size(); i++) {
                final int currentInterval = getEndTimeInDaysEntityForMeal(daysEntity, candidates.get(i)) -
                        getStartTimeInDaysEntityForMeal(daysEntity, candidates.get(i));
                if (currentInterval < minorInterval) {
                    result.clear();
                    result.add(candidates.get(i));
                    minorInterval = currentInterval;
                } else if (currentInterval == minorInterval) {
                    result.add(candidates.get(i));
                }
            }
        }
    }

    public void findMealsStartingEarly(DaysEntity daysEntity, List<Integer> candidates, List<Integer> result) {
        validateListParametersOrThrow(candidates, result);
        result.clear();

        if (!candidates.isEmpty()) {
            result.add(candidates.get(0));
            for (int i = 1; i < candidates.size(); i++) {
                if (getStartTimeInDaysEntityForMeal(daysEntity, candidates.get(i))
                        < getStartTimeInDaysEntityForMeal(daysEntity, result.get(0))) {
                    result.clear();
                    result.add(candidates.get(i));
                } else if (getStartTimeInDaysEntityForMeal(daysEntity, candidates.get(i))
                        == getStartTimeInDaysEntityForMeal(daysEntity, result.get(0))) {
                    result.add(candidates.get(i));
                }
            }
        }
    }

    public void findMealsEndingBeforeTime(int time, DaysEntity daysEntity, List<Integer> candidates, List<Integer> result) {
        validateListParametersOrThrow(candidates, result);
        result.clear();

        for (Integer meal : candidates) {
            if (getEndTimeInDaysEntityForMeal(daysEntity, meal) < time) {
                result.add(meal);
            }
        }
    }

    public void findClosestMealsEndingBeforeTime(int time, DaysEntity daysEntity, List<Integer> candidates, List<Integer> result) {
        validateListParametersOrThrow(candidates, result);
        List<Integer> tempResult = new ArrayList<Integer>(Meals.getMealsCount() - 1);
        findMealsEndingBeforeTime(time, daysEntity, candidates, tempResult);

        result.clear();
        if (!tempResult.isEmpty()) {
            result.add(tempResult.get(0));
            for (int i = 1; i < tempResult.size(); i++) {
                if (getEndTimeInDaysEntityForMeal(daysEntity, tempResult.get(i))
                        > getEndTimeInDaysEntityForMeal(daysEntity, result.get(0))) {
                    result.clear();
                    result.add(tempResult.get(i));
                } else if (getEndTimeInDaysEntityForMeal(daysEntity, tempResult.get(i))
                        == getEndTimeInDaysEntityForMeal(daysEntity, result.get(0))) {
                    result.add(tempResult.get(i));
                }
            }
        }
    }

    public void findMealsStartingAfterTime(int time, DaysEntity daysEntity, List<Integer> candidates, List<Integer> result) {
        validateListParametersOrThrow(candidates, result);
        result.clear();

        for (Integer meal : candidates) {
            if (getStartTimeInDaysEntityForMeal(daysEntity, meal) > time) {
                result.add(meal);
            }
        }
    }

    public void findClosestMealsStartingAfterTime(int time, DaysEntity daysEntity, List<Integer> candidates, List<Integer> result) {
        validateListParametersOrThrow(candidates, result);
        List<Integer> tempResult = new ArrayList<Integer>(Meals.getMealsCount() - 1);
        findMealsStartingAfterTime(time, daysEntity, candidates, tempResult);

        result.clear();
        if (!tempResult.isEmpty()) {
            result.add(tempResult.get(0));
            for (int i = 1; i < tempResult.size(); i++) {
                if (getStartTimeInDaysEntityForMeal(daysEntity, tempResult.get(i))
                        < getStartTimeInDaysEntityForMeal(daysEntity, result.get(0))) {
                    result.clear();
                    result.add(tempResult.get(i));
                } else if (getStartTimeInDaysEntityForMeal(daysEntity, tempResult.get(i))
                        == getStartTimeInDaysEntityForMeal(daysEntity, result.get(0))) {
                    result.add(tempResult.get(i));
                }
            }
        }
    }

    public void findClosestNeighborsForTime(int time, DaysEntity daysEntity, List<Integer> candidates, List<Integer> result) {
        /*
            Neighbors are only meals NOT in period with time (ie, meal end time < time or meal
            start time > time. PS: meal start time < meal end time is assured by WeekDayParametersManager)
         */

        validateListParametersOrThrow(candidates, result);
        result.clear();

        int minorDelta = Integer.MAX_VALUE;
        for (Integer meal : candidates) {
            final int startTime = getStartTimeInDaysEntityForMeal(daysEntity, meal);
            final int endTime = getEndTimeInDaysEntityForMeal(daysEntity, meal);
            boolean isNeighbor = false;
            int currentDelta = -1;
            if (endTime < time) {
                isNeighbor = true;
                currentDelta = time - endTime;
            } else if (startTime > time) {
                isNeighbor = true;
                currentDelta = startTime - time;
            }
            if (isNeighbor) {
                if (currentDelta < minorDelta) {
                    result.clear();
                    result.add(meal);
                    minorDelta = currentDelta;
                } else if (currentDelta == minorDelta) {
                    result.add(meal);
                }
            }
        }
    }

    private void validateListParametersOrThrow(List<Integer> candidates, List<Integer> result) {
        if (candidates == null || result == null) {
            throw new IllegalArgumentException("candidates and result must not null.");
        }
        if (candidates == result) {
            throw new IllegalArgumentException("candidates and result must be different lists.");
        }
    }

    private int getStartTimeInDaysEntityForMeal(DaysEntity entity, int meal) {
        switch (meal) {
            case Meals.BREAKFAST:
                return entity.getBreakfastStartTime();
            case Meals.BRUNCH:
                return entity.getBrunchStartTime();
            case Meals.LUNCH:
                return entity.getLunchStartTime();
            case Meals.SNACK:
                return entity.getSnackStartTime();
            case Meals.DINNER:
                return entity.getDinnerStartTime();
            case Meals.SUPPER:
                return entity.getSupperStartTime();
            default:
                throw new IllegalArgumentException("Invalid meal=" + meal + ".");
        }
    }

    private int getEndTimeInDaysEntityForMeal(DaysEntity entity, int meal) {
        switch (meal) {
            case Meals.BREAKFAST:
                return entity.getBreakfastEndTime();
            case Meals.BRUNCH:
                return entity.getBrunchEndTime();
            case Meals.LUNCH:
                return entity.getLunchEndTime();
            case Meals.SNACK:
                return entity.getSnackEndTime();
            case Meals.DINNER:
                return entity.getDinnerEndTime();
            case Meals.SUPPER:
                return entity.getSupperEndTime();
            default:
                throw new IllegalArgumentException("Invalid meal=" + meal + ".");
        }
    }

    private float getGoalInDaysEntityForMeal(DaysEntity entity, int meal) {
        switch (meal) {
            case Meals.BREAKFAST:
                return entity.getBreakfastGoal();
            case Meals.BRUNCH:
                return entity.getBrunchGoal();
            case Meals.LUNCH:
                return entity.getLunchGoal();
            case Meals.SNACK:
                return entity.getSnackGoal();
            case Meals.DINNER:
                return entity.getDinnerGoal();
            case Meals.SUPPER:
                return entity.getSupperGoal();
            default:
                throw new IllegalArgumentException("Invalid meal=" + meal + ".");
        }
    }

    private double getMealUsageInSummaryForMeal(UsageSummary summary, Integer meal) {
        switch (meal) {
            case Meals.BREAKFAST:
                return summary.getBreakfastUsed();
            case Meals.BRUNCH:
                return summary.getBrunchUsed();
            case Meals.LUNCH:
                return summary.getLunchUsed();
            case Meals.SNACK:
                return summary.getSnackUsed();
            case Meals.DINNER:
                return summary.getDinnerUsed();
            case Meals.SUPPER:
                return summary.getSupperUsed();
            default:
                throw new IllegalArgumentException("Invalid meal=" + meal + ".");
        }
    }
}
