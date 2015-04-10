package br.com.arndroid.etdiet.virtualweek;

import android.os.Parcel;
import android.os.Parcelable;

import br.com.arndroid.etdiet.provider.days.DaysEntity;

public class DaySummary implements Parcelable {

    public static final int GOAL_TYPE_PLANNED = 0;
    public static final int GOAL_TYPE_LEFT = 1;

    // Mind the gap: it's a Parcelable. The order and the number of fields matter.
    private DaysEntity daysEntity;
    private UsageSummary usageSummary;
    private SettingsValues settingsValues;
    private float exerciseAfterUsage;
    private float exerciseToCarry;
    private float totalExercise; // Exercise done + (if allowed) exercise to carry from previous day
    private float diaryAllowanceAfterUsage; // and diary allowance before usage == DayEntity.allowed
    private float weeklyAllowanceBeforeUsage;
    private float weeklyAllowanceAfterUsage;
    private float goalBeforeUsage; // The initial goal of the day
    private float goalAfterUsage; // The remaining value to met the initial goal
    private int goalType; // If == GOAL_TYPE_LEFT, the user planned greater than diary allowance + reserves

    public DaySummary() {}

    public DaySummary(DaySummary toClone) {
        daysEntity = new DaysEntity(toClone.daysEntity);
        usageSummary = new UsageSummary(toClone.usageSummary);
        settingsValues = new SettingsValues(toClone.settingsValues);
        exerciseAfterUsage = toClone.exerciseAfterUsage;
        exerciseToCarry = toClone.exerciseToCarry;
        totalExercise = toClone.totalExercise;
        diaryAllowanceAfterUsage = toClone.diaryAllowanceAfterUsage;
        weeklyAllowanceBeforeUsage = toClone.weeklyAllowanceBeforeUsage;
        weeklyAllowanceAfterUsage = toClone.weeklyAllowanceAfterUsage;
        goalBeforeUsage = toClone.goalBeforeUsage;
        goalAfterUsage = toClone.goalAfterUsage;
        goalType = toClone.goalType;
    }

    public int getGoalType() {
        return goalType;
    }

    public void setGoalType(int goalType) {
        this.goalType = goalType;
    }

    public UsageSummary getUsage() {
        return usageSummary;
    }

    public void setUsage(UsageSummary usage) {
        this.usageSummary = usage;
    }

    public float getExerciseToCarry() {
        return exerciseToCarry;
    }

    public void setExerciseToCarry(float exerciseToCarry) {
        this.exerciseToCarry = exerciseToCarry;
    }

    public DaysEntity getEntity() {
        return daysEntity;
    }

    public void setEntity(DaysEntity entity) {
        this.daysEntity = entity;
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

    public float getGoalBeforeUsage() {
        return goalBeforeUsage;
    }

    public void setGoalBeforeUsage(float goalBeforeUsage) {
        this.goalBeforeUsage = goalBeforeUsage;
    }

    public float getGoalAfterUsage() {
        return goalAfterUsage;
    }

    public void setGoalAfterUsage(float goalAfterUsage) {
        this.goalAfterUsage = goalAfterUsage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {
        destination.writeParcelable(daysEntity, flags);
        destination.writeParcelable(usageSummary, flags);
        destination.writeParcelable(settingsValues,flags);
        destination.writeFloat(exerciseAfterUsage);
        destination.writeFloat(exerciseToCarry);
        destination.writeFloat(totalExercise);
        destination.writeFloat(diaryAllowanceAfterUsage);
        destination.writeFloat(weeklyAllowanceBeforeUsage);
        destination.writeFloat(weeklyAllowanceAfterUsage);
        destination.writeFloat(goalBeforeUsage);
        destination.writeFloat(goalAfterUsage);
        destination.writeInt(goalType);
    }

    public static final Parcelable.Creator<DaySummary> CREATOR
            = new Parcelable.Creator<DaySummary>() {

        public DaySummary createFromParcel(Parcel in) {
            final DaySummary result = new DaySummary();
            result.setEntity((DaysEntity) in.readParcelable(DaysEntity.class.getClassLoader()));
            result.setUsage((UsageSummary) in.readParcelable(UsageSummary.class.getClassLoader()));
            result.setSettingsValues((SettingsValues) in.readParcelable(SettingsValues.class.getClassLoader()));
            result.setExerciseAfterUsage(in.readFloat());
            result.setExerciseToCarry(in.readFloat());
            result.setTotalExercise(in.readFloat());
            result.setDiaryAllowanceAfterUsage(in.readFloat());
            result.setWeeklyAllowanceBeforeUsage(in.readFloat());
            result.setWeeklyAllowanceAfterUsage(in.readFloat());
            result.setGoalBeforeUsage(in.readFloat());
            result.setGoalAfterUsage(in.readFloat());
            result.setGoalType(in.readInt());

            return result;
        }

        public DaySummary[] newArray(int size) {
            return new DaySummary[size];
        }
    };
}
