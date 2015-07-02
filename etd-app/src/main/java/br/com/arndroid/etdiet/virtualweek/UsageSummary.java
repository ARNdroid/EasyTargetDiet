package br.com.arndroid.etdiet.virtualweek;

import android.os.Parcel;
import android.os.Parcelable;

import br.com.arndroid.etdapi.data.Meal;
import br.com.arndroid.etdiet.meals.Meals;

public class UsageSummary implements Parcelable {

    // Attention: it's a Parcelable. The order and the number of fields matter.
    private float exerciseDone;
    private float breakfastUsed;
    private float brunchUsed;
    private float lunchUsed;
    private float snackUsed;
    private float dinnerUsed;
    private float supperUsed;

    public UsageSummary() {}

    public UsageSummary(UsageSummary toClone) {
        exerciseDone = toClone.exerciseDone;
        breakfastUsed = toClone.breakfastUsed;
        brunchUsed = toClone.brunchUsed;
        lunchUsed = toClone.lunchUsed;
        snackUsed = toClone.snackUsed;
        dinnerUsed = toClone.dinnerUsed;
        supperUsed = toClone.supperUsed;
    }

    public float getUsageForMeal(Meal meal) {
        switch (meal) {
            case BREAKFAST:
                return getBreakfastUsed();
            case BRUNCH:
                return getBrunchUsed();
            case LUNCH:
                return getLunchUsed();
            case SNACK:
                return getSnackUsed();
            case DINNER:
                return getDinnerUsed();
            case SUPPER:
                return getSupperUsed();
            case EXERCISE:
                return getExerciseDone();
            default:
                throw new IllegalArgumentException("Invalid meal=" + meal + ".");
        }
    }

    public float getTotalUsed() {
        return getBreakfastUsed()
                + getBrunchUsed()
                + getLunchUsed()
                + getSnackUsed()
                + getDinnerUsed()
                + getSupperUsed();
    }

    public float getExerciseDone() {
        return exerciseDone;
    }

    public void setExerciseDone(float exerciseDone) {
        this.exerciseDone = exerciseDone;
    }

    public float getBreakfastUsed() {
        return breakfastUsed;
    }

    public void setBreakfastUsed(float breakfastUsed) {
        this.breakfastUsed = breakfastUsed;
    }

    public float getBrunchUsed() {
        return brunchUsed;
    }

    public void setBrunchUsed(float brunchUsed) {
        this.brunchUsed = brunchUsed;
    }

    public float getLunchUsed() {
        return lunchUsed;
    }

    public void setLunchUsed(float lunchUsed) {
        this.lunchUsed = lunchUsed;
    }

    public float getSnackUsed() {
        return snackUsed;
    }

    public void setSnackUsed(float snackUsed) {
        this.snackUsed = snackUsed;
    }

    public float getDinnerUsed() {
        return dinnerUsed;
    }

    public void setDinnerUsed(float dinnerUsed) {
        this.dinnerUsed = dinnerUsed;
    }

    public float getSupperUsed() {
        return supperUsed;
    }

    public void setSupperUsed(float supperUsed) {
        this.supperUsed = supperUsed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {
        destination.writeFloat(exerciseDone);
        destination.writeFloat(breakfastUsed);
        destination.writeFloat(brunchUsed);
        destination.writeFloat(lunchUsed);
        destination.writeFloat(snackUsed);
        destination.writeFloat(dinnerUsed);
        destination.writeFloat(supperUsed);
    }

    public static final Parcelable.Creator<UsageSummary> CREATOR
            = new Parcelable.Creator<UsageSummary>() {

        public UsageSummary createFromParcel(Parcel in) {
            final UsageSummary result = new UsageSummary();

            result.setExerciseDone(in.readFloat());
            result.setBreakfastUsed(in.readFloat());
            result.setBrunchUsed(in.readFloat());
            result.setLunchUsed(in.readFloat());
            result.setSnackUsed(in.readFloat());
            result.setDinnerUsed(in.readFloat());
            result.setSupperUsed(in.readFloat());

            return result;
        }

        public UsageSummary[] newArray(int size) {
            return new UsageSummary[size];
        }
    };
}
