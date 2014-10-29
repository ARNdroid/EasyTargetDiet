package br.com.arndroid.etdiet.virtualweek;

import br.com.arndroid.etdiet.provider.days.DaysEntity;

public class DaySummary {

    private DaysEntity entity;
    private UsageSummary usage;
    private SettingsValues settingsValues;
    private float exerciseAfterUsage;
    private float exerciseToCarry;
    private float totalExercise; // Exercise done + (if allowed) exercise to carry from previous day
    private float diaryAllowanceAfterUsage; // and diary allowance before usage == DayEntity.allowed
    private float weeklyAllowanceBeforeUsage;
    private float weeklyAllowanceAfterUsage;

    public DaySummary() {}

    public DaySummary(DaySummary toClone) {
        entity = new DaysEntity(toClone.entity);
        usage = new UsageSummary(toClone.usage);
        settingsValues = new SettingsValues(toClone.settingsValues);
        exerciseAfterUsage = toClone.exerciseAfterUsage;
        exerciseToCarry = toClone.exerciseToCarry;
        totalExercise = toClone.totalExercise;
        diaryAllowanceAfterUsage = toClone.diaryAllowanceAfterUsage;
        weeklyAllowanceBeforeUsage = toClone.weeklyAllowanceBeforeUsage;
        weeklyAllowanceAfterUsage = toClone.weeklyAllowanceAfterUsage;
    }

    public UsageSummary getUsage() {
        return usage;
    }

    public void setUsage(UsageSummary usage) {
        this.usage = usage;
    }


    public float getExerciseToCarry() {
        return exerciseToCarry;
    }

    public void setExerciseToCarry(float exerciseToCarry) {
        this.exerciseToCarry = exerciseToCarry;
    }

    public DaysEntity getEntity() {
        return entity;
    }

    public void setEntity(DaysEntity entity) {
        this.entity = entity;
    }

    public void setTotalExercise(float totalExercise) {
        this.totalExercise = totalExercise;
    }

    public float getTotalExercise() {
        return totalExercise;
    }

    public void setWeeklyAllowanceBeforeUsage(float weeklyAllowanceBeforeUsage) {
        this.weeklyAllowanceBeforeUsage = weeklyAllowanceBeforeUsage;
    }

    public float getWeeklyAllowanceBeforeUsage() {
        return weeklyAllowanceBeforeUsage;
    }

    public void setDiaryAllowanceAfterUsage(float diaryAllowanceAfterUsage) {
        this.diaryAllowanceAfterUsage = diaryAllowanceAfterUsage;
    }

    public float getDiaryAllowanceAfterUsage() {
        return diaryAllowanceAfterUsage;
    }

    public void setExerciseAfterUsage(float exerciseAfterUsage) {
        this.exerciseAfterUsage = exerciseAfterUsage;
    }

    public float getExerciseAfterUsage() {
        return exerciseAfterUsage;
    }

    public void setWeeklyAllowanceAfterUsage(float weeklyAllowanceAfterUsage) {
        this.weeklyAllowanceAfterUsage = weeklyAllowanceAfterUsage;
    }

    public float getWeeklyAllowanceAfterUsage() {
        return weeklyAllowanceAfterUsage;
    }

    public SettingsValues getSettingsValues() {
        return settingsValues;
    }

    public void setSettingsValues(SettingsValues settingsValues) {
        this.settingsValues = settingsValues;
    }
}
