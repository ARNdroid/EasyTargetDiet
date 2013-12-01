package br.com.arndroid.etdiet.virtualweek;

public class UsageSummary {
    private float exerciseDone;
    private float breakfastUsed;
    private float brunchUsed;
    private float lunchUsed;
    private float sneakUsed;
    private float dinnerUsed;
    private float supperUsed;

    public UsageSummary() {}

    public UsageSummary(UsageSummary toClone) {
        exerciseDone = toClone.exerciseDone;
        breakfastUsed = toClone.breakfastUsed;
        brunchUsed = toClone.brunchUsed;
        lunchUsed = toClone.lunchUsed;
        sneakUsed = toClone.sneakUsed;
        dinnerUsed = toClone.dinnerUsed;
        supperUsed = toClone.supperUsed;
    }

    public float getTotalUsed() {
        return getBreakfastUsed()
                + getBrunchUsed()
                + getLunchUsed()
                + getSneakUsed()
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

    public float getSneakUsed() {
        return sneakUsed;
    }

    public void setSneakUsed(float sneakUsed) {
        this.sneakUsed = sneakUsed;
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
}
