package br.com.arndroid.etdiet.virtualweek;

import android.os.Parcel;
import android.os.Parcelable;

public class SettingsValues implements Parcelable {

    // Attention: it's a Parcelable. The order and the number of fields matter.
    private int mExerciseUseMode;
    private int mExerciseUseOrder;

    protected SettingsValues() {}

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {
        destination.writeInt(mExerciseUseMode);
        destination.writeInt(mExerciseUseOrder);
    }

    public static final Parcelable.Creator<SettingsValues> CREATOR
            = new Parcelable.Creator<SettingsValues>() {

        public SettingsValues createFromParcel(Parcel in) {
            final SettingsValues result = new SettingsValues();

            result.setExerciseUseMode(in.readInt());
            result.setExerciseUseOrder(in.readInt());

            return result;
        }

        public SettingsValues[] newArray(int size) {
            return new SettingsValues[size];
        }
    };
}
