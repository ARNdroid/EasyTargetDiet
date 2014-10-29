package br.com.arndroid.etdiet.virtualweek;

public class SettingsValues {

    private int mExerciseUseMode;
    private int mExerciseUseOrder;

    public SettingsValues(int exerciseUseMode, int exerciseUseOrder) {
        mExerciseUseMode = exerciseUseMode;
        mExerciseUseOrder = exerciseUseOrder;
    }

    public SettingsValues(SettingsValues toClone) {
        mExerciseUseMode = toClone.getExerciseUseMode();
        mExerciseUseOrder = toClone.getExerciseUseOrder();
    }

    public int getExerciseUseMode() {
        return mExerciseUseMode;
    }

    public void setExerciseUseMode(int exerciseUseMode) {
        mExerciseUseMode = exerciseUseMode;
    }

    public int getExerciseUseOrder() {
        return mExerciseUseOrder;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setExerciseUseOrder(int exerciseUseOrder) {
        mExerciseUseOrder = exerciseUseOrder;
    }
}
