package br.com.arndroid.etdiet.provider.days;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.AbstractEntity;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.utils.ParcelUtils;
import br.com.arndroid.etdiet.utils.StringUtils;

public class DaysEntity extends AbstractEntity implements Parcelable {

    public static final int NO_TIME = -1;
    private static final long SENTINEL_ID = -1L;
    private static final float SENTINEL_FLOAT_VALUE = -1.0f;
    private static final int SENTINEL_INT_VALUE = -1;

    // Attention: it's a Parcelable. The order and the number of fields matter.
    private Long id;
    private String dateId;
    private Float allowed;
    private Integer breakfastStartTime;
    private Integer breakfastEndTime;
    private Float breakfastGoal;
    private Integer brunchStartTime;
    private Integer brunchEndTime;
    private Float brunchGoal;
    private Integer lunchStartTime;
    private Integer lunchEndTime;
    private Float lunchGoal;
    private Integer snackStartTime;
    private Integer snackEndTime;
    private Float snackGoal;
    private Integer dinnerStartTime;
    private Integer dinnerEndTime;
    private Float dinnerGoal;
    private Integer supperStartTime;
    private Integer supperEndTime;
    private Float supperGoal;
    private Float exerciseGoal;
    private Integer liquidDone;
    private Integer liquidGoal;
    private Integer oilDone;
    private Integer oilGoal;
    private Integer supplementDone;
    private Integer supplementGoal;
    private String note;

	/*
	 * Implementation
	 */

    protected DaysEntity() {}

    public DaysEntity(Long id, String dateId, Float allowed, Integer breakfastStartTime, Integer breakfastEndTime,
                      Float breakfastGoal, Integer brunchStartTime, Integer brunchEndTime, Float brunchGoal,
                      Integer lunchStartTime, Integer lunchEndTime, Float lunchGoal, Integer snackStartTime,
                      Integer snackEndTime, Float snackGoal, Integer dinnerStartTime, Integer dinnerEndTime,
                      Float dinnerGoal, Integer supperStartTime, Integer supperEndTime, Float supperGoal,
                      Float exerciseGoal, Integer liquidDone, Integer liquidGoal, Integer oilDone,
                      Integer oilGoal, Integer supplementDone, Integer supplementGoal, String note) {
        this.id = id;
        this.dateId = dateId;
        this.allowed = allowed;
        this.breakfastStartTime = breakfastStartTime;
        this.breakfastEndTime = breakfastEndTime;
        this.breakfastGoal = breakfastGoal;
        this.brunchStartTime = brunchStartTime;
        this.brunchEndTime = brunchEndTime;
        this.brunchGoal = brunchGoal;
        this.lunchStartTime = lunchStartTime;
        this.lunchEndTime = lunchEndTime;
        this.lunchGoal = lunchGoal;
        this.snackStartTime = snackStartTime;
        this.snackEndTime = snackEndTime;
        this.snackGoal = snackGoal;
        this.dinnerStartTime = dinnerStartTime;
        this.dinnerEndTime = dinnerEndTime;
        this.dinnerGoal = dinnerGoal;
        this.supperStartTime = supperStartTime;
        this.supperEndTime = supperEndTime;
        this.supperGoal = supperGoal;
        this.exerciseGoal = exerciseGoal;
        this.liquidDone = liquidDone;
        this.liquidGoal = liquidGoal;
        this.oilDone = oilDone;
        this.oilGoal = oilGoal;
        this.supplementDone = supplementDone;
        this.supplementGoal = supplementGoal;
        this.note = StringUtils.isEmptyOrOnlySpaces(note) ? null : note;
    }

    public DaysEntity(DaysEntity toClone) {
        id = toClone.id;
        dateId = toClone.dateId;
        allowed = toClone.allowed;
        breakfastStartTime = toClone.breakfastStartTime;
        breakfastEndTime = toClone.breakfastEndTime;
        breakfastGoal = toClone.breakfastGoal;
        brunchStartTime = toClone.brunchStartTime;
        brunchEndTime = toClone.brunchEndTime;
        brunchGoal = toClone.brunchGoal;
        lunchStartTime = toClone.lunchStartTime;
        lunchEndTime = toClone.lunchEndTime;
        lunchGoal = toClone.lunchGoal;
        snackStartTime = toClone.snackStartTime;
        snackEndTime = toClone.snackEndTime;
        snackGoal = toClone.snackGoal;
        dinnerStartTime = toClone.dinnerStartTime;
        dinnerEndTime = toClone.dinnerEndTime;
        dinnerGoal = toClone.dinnerGoal;
        supperStartTime = toClone.supperStartTime;
        supperEndTime = toClone.supperEndTime;
        supperGoal = toClone.supperGoal;
        exerciseGoal = toClone.exerciseGoal;
        liquidDone = toClone.liquidDone;
        liquidGoal = toClone.liquidGoal;
        oilDone = toClone.oilDone;
        oilGoal = toClone.oilGoal;
        supplementDone = toClone.supplementDone;
        supplementGoal = toClone.supplementGoal;
        note = toClone.note;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Contract.Days._ID, id);
        cv.put(Contract.Days.DATE_ID, dateId);
        cv.put(Contract.Days.ALLOWED, allowed);
        cv.put(Contract.Days.BREAKFAST_START_TIME, breakfastStartTime);
        cv.put(Contract.Days.BREAKFAST_END_TIME, breakfastEndTime);
        cv.put(Contract.Days.BREAKFAST_GOAL, breakfastGoal);
        cv.put(Contract.Days.BRUNCH_START_TIME, brunchStartTime);
        cv.put(Contract.Days.BRUNCH_END_TIME, brunchEndTime);
        cv.put(Contract.Days.BRUNCH_GOAL, brunchGoal);
        cv.put(Contract.Days.LUNCH_START_TIME, lunchStartTime);
        cv.put(Contract.Days.LUNCH_END_TIME, lunchEndTime);
        cv.put(Contract.Days.LUNCH_GOAL, lunchGoal);
        cv.put(Contract.Days.SNACK_START_TIME, snackStartTime);
        cv.put(Contract.Days.SNACK_END_TIME, snackEndTime);
        cv.put(Contract.Days.SNACK_GOAL, snackGoal);
        cv.put(Contract.Days.DINNER_START_TIME, dinnerStartTime);
        cv.put(Contract.Days.DINNER_END_TIME, dinnerEndTime);
        cv.put(Contract.Days.DINNER_GOAL, dinnerGoal);
        cv.put(Contract.Days.SUPPER_START_TIME, supperStartTime);
        cv.put(Contract.Days.SUPPER_END_TIME, supperEndTime);
        cv.put(Contract.Days.SUPPER_GOAL, supperGoal);
        cv.put(Contract.Days.EXERCISE_GOAL, exerciseGoal);
        cv.put(Contract.Days.LIQUID_DONE, liquidDone);
        cv.put(Contract.Days.LIQUID_GOAL, liquidGoal);
        cv.put(Contract.Days.OIL_DONE, oilDone);
        cv.put(Contract.Days.OIL_GOAL, oilGoal);
        cv.put(Contract.Days.SUPPLEMENT_DONE, supplementDone);
        cv.put(Contract.Days.SUPPLEMENT_GOAL, supplementGoal);
        cv.put(Contract.Days.NOTE, note);
        return cv;
    }

    @Override
    public ContentValues toContentValuesIgnoreNulls() {
        ContentValues cv = new ContentValues();
        if (id != null) {
            cv.put(Contract.Days._ID, id);
        }
        if (dateId != null) {
            cv.put(Contract.Days.DATE_ID, dateId);
        }
        if (allowed != null) {
            cv.put(Contract.Days.ALLOWED, allowed);
        }
        if (breakfastStartTime != null) {
            cv.put(Contract.Days.BREAKFAST_START_TIME, breakfastStartTime);
        }
        if (breakfastEndTime != null) {
            cv.put(Contract.Days.BREAKFAST_END_TIME, breakfastEndTime);
        }
        if (breakfastGoal != null) {
            cv.put(Contract.Days.BREAKFAST_GOAL, breakfastGoal);
        }
        if (brunchStartTime != null) {
            cv.put(Contract.Days.BRUNCH_START_TIME, brunchStartTime);
        }
        if (brunchEndTime != null) {
            cv.put(Contract.Days.BRUNCH_END_TIME, brunchEndTime);
        }
        if (brunchGoal != null) {
            cv.put(Contract.Days.BRUNCH_GOAL, brunchGoal);
        }
        if (lunchStartTime != null) {
            cv.put(Contract.Days.LUNCH_START_TIME, lunchStartTime);
        }
        if (lunchEndTime != null) {
            cv.put(Contract.Days.LUNCH_END_TIME, lunchEndTime);
        }
        if (lunchGoal != null) {
            cv.put(Contract.Days.LUNCH_GOAL, lunchGoal);
        }
        if (snackStartTime != null) {
            cv.put(Contract.Days.SNACK_START_TIME, snackStartTime);
        }
        if (snackEndTime != null) {
            cv.put(Contract.Days.SNACK_END_TIME, snackEndTime);
        }
        if (snackGoal != null) {
            cv.put(Contract.Days.SNACK_GOAL, snackGoal);
        }
        if (dinnerStartTime != null) {
            cv.put(Contract.Days.DINNER_START_TIME, dinnerStartTime);
        }
        if (dinnerEndTime != null) {
            cv.put(Contract.Days.DINNER_END_TIME, dinnerEndTime);
        }
        if (dinnerGoal != null) {
            cv.put(Contract.Days.DINNER_GOAL, dinnerGoal);
        }
        if (supperStartTime != null) {
            cv.put(Contract.Days.SUPPER_START_TIME, supperStartTime);
        }
        if (supperEndTime != null) {
            cv.put(Contract.Days.SUPPER_END_TIME, supperEndTime);
        }
        if (supperGoal != null) {
            cv.put(Contract.Days.SUPPER_GOAL, supperGoal);
        }
        if (exerciseGoal != null) {
            cv.put(Contract.Days.EXERCISE_GOAL, exerciseGoal);
        }
        if (liquidDone != null) {
            cv.put(Contract.Days.LIQUID_DONE, liquidDone);
        }
        if (liquidGoal != null) {
            cv.put(Contract.Days.LIQUID_GOAL, liquidGoal);
        }
        if (oilDone != null) {
            cv.put(Contract.Days.OIL_DONE, oilDone);
        }
        if (oilGoal != null) {
            cv.put(Contract.Days.OIL_GOAL, oilGoal);
        }
        if (supplementDone != null) {
            cv.put(Contract.Days.SUPPLEMENT_DONE, supplementDone);
        }
        if (supplementGoal != null) {
            cv.put(Contract.Days.SUPPLEMENT_GOAL, supplementGoal);
        }
        if (note != null) {
            cv.put(Contract.Days.NOTE, note);
        }
        return cv;
    }

    @Override
    public void validateOrThrow() {

        if (dateId == null) {
            throwNullValueException(Contract.Days.DATE_ID);
        }
        if (allowed == null) {
            throwNullValueException(Contract.Days.ALLOWED);
        }

        if (breakfastStartTime == null) {
            throwNullValueException(Contract.Days.BREAKFAST_START_TIME);
        }
        if (breakfastEndTime == null) {
            throwNullValueException(Contract.Days.BREAKFAST_END_TIME);
        }
        if (breakfastGoal == null) {
            throwNullValueException(Contract.Days.BREAKFAST_GOAL);
        }

        if (brunchStartTime == null) {
            throwNullValueException(Contract.Days.BRUNCH_START_TIME);
        }
        if (brunchEndTime == null) {
            throwNullValueException(Contract.Days.BRUNCH_END_TIME);
        }
        if (brunchGoal == null) {
            throwNullValueException(Contract.Days.BRUNCH_GOAL);
        }

        if (lunchStartTime == null) {
            throwNullValueException(Contract.Days.LUNCH_START_TIME);
        }
        if (lunchEndTime == null) {
            throwNullValueException(Contract.Days.LUNCH_END_TIME);
        }
        if (lunchGoal == null) {
            throwNullValueException(Contract.Days.LUNCH_GOAL);
        }

        if (snackStartTime == null) {
            throwNullValueException(Contract.Days.SNACK_START_TIME);
        }
        if (snackEndTime == null) {
            throwNullValueException(Contract.Days.SNACK_END_TIME);
        }
        if (snackGoal == null) {
            throwNullValueException(Contract.Days.SNACK_GOAL);
        }

        if (dinnerStartTime == null) {
            throwNullValueException(Contract.Days.DINNER_START_TIME);
        }
        if (dinnerEndTime == null) {
            throwNullValueException(Contract.Days.DINNER_END_TIME);
        }
        if (dinnerGoal == null) {
            throwNullValueException(Contract.Days.DINNER_GOAL);
        }

        if (supperStartTime == null) {
            throwNullValueException(Contract.Days.SUPPER_START_TIME);
        }
        if (supperEndTime == null) {
            throwNullValueException(Contract.Days.SUPPER_END_TIME);
        }
        if (supperGoal == null) {
            throwNullValueException(Contract.Days.SUPPER_GOAL);
        }

        if (exerciseGoal == null) {
            throwNullValueException(Contract.Days.EXERCISE_GOAL);
        }

        if (liquidDone == null) {
            throwNullValueException(Contract.Days.LIQUID_DONE);
        }
        if (liquidGoal == null) {
            throwNullValueException(Contract.Days.LIQUID_GOAL);
        }

        if (oilDone == null) {
            throwNullValueException(Contract.Days.OIL_DONE);
        }
        if (oilGoal == null) {
            throwNullValueException(Contract.Days.OIL_GOAL);
        }

        if (supplementDone == null) {
            throwNullValueException(Contract.Days.SUPPLEMENT_DONE);
        }
        if (supplementGoal == null) {
            throwNullValueException(Contract.Days.SUPPLEMENT_GOAL);
        }

        // Note may be everything...
    }

    private void throwNullValueException(String columnName) {
        throw new Contract.TargetException(Contract.TargetException.NULL_VALUE,
                new Contract.TargetException.FieldDescriptor[] {new Contract.TargetException.FieldDescriptor(Contract.Days.TABLE_NAME,
                        columnName, null)}, null);
    }

    @Override
    public String getTableName() {
        return Contract.Days.TABLE_NAME;
    }

    @Override
    public String getIdColumnName() {
        return Contract.Days._ID;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;

        } else if (obj instanceof DaysEntity) {
            DaysEntity temp = (DaysEntity) obj;
            // id:
            if (this.id != null) {
                if (!this.id.equals(temp.id))
                    return false;
            } else {
                if (temp.id != null)
                    return false;
            }

            // dateId:
            if (this.dateId != null) {
                if (!this.dateId.equals(temp.dateId))
                    return false;
            } else {
                if (temp.dateId != null)
                    return false;
            }

            // allowed:
            if (this.allowed != null) {
                if (!this.allowed.equals(temp.allowed))
                    return false;
            } else {
                if (temp.allowed != null)
                    return false;
            }

            // breakfastStartTime:
            if (this.breakfastStartTime != null) {
                if(!this.breakfastStartTime.equals(temp.breakfastStartTime))
                    return false;
            } else {
                if (temp.breakfastStartTime != null)
                    return false;
            }

            // breakfastEndTime:
            if (this.breakfastEndTime != null) {
                if(!this.breakfastEndTime.equals(temp.breakfastEndTime))
                    return false;
            } else {
                if (temp.breakfastEndTime != null)
                    return false;
            }

            // breakfastGoal:
            if (this.breakfastGoal != null) {
                if(!this.breakfastGoal.equals(temp.breakfastGoal))
                    return false;
            } else {
                if (temp.breakfastGoal != null)
                    return false;
            }

            // brunchStartTime:
            if (this.brunchStartTime != null) {
                if(!this.brunchStartTime.equals(temp.brunchStartTime))
                    return false;
            } else {
                if (temp.brunchStartTime != null)
                    return false;
            }

            // brunchEndTime:
            if (this.brunchEndTime != null) {
                if(!this.brunchEndTime.equals(temp.brunchEndTime))
                    return false;
            } else {
                if (temp.brunchEndTime != null)
                    return false;
            }

            // brunchGoal:
            if (this.brunchGoal != null) {
                if(!this.brunchGoal.equals(temp.brunchGoal))
                    return false;
            } else {
                if (temp.brunchGoal != null)
                    return false;
            }

            // lunchStartTime:
            if (this.lunchStartTime != null) {
                if(!this.lunchStartTime.equals(temp.lunchStartTime))
                    return false;
            } else {
                if (temp.lunchStartTime != null)
                    return false;
            }

            // lunchEndTime:
            if (this.lunchEndTime != null) {
                if(!this.lunchEndTime.equals(temp.lunchEndTime))
                    return false;
            } else {
                if (temp.lunchEndTime != null)
                    return false;
            }

            // lunchGoal:
            if (this.lunchGoal != null) {
                if(!this.lunchGoal.equals(temp.lunchGoal))
                    return false;
            } else {
                if (temp.lunchGoal != null)
                    return false;
            }

            // snackStartTime:
            if (this.snackStartTime != null) {
                if(!this.snackStartTime.equals(temp.snackStartTime))
                    return false;
            } else {
                if (temp.snackStartTime != null)
                    return false;
            }

            // snackEndTime:
            if (this.snackEndTime != null) {
                if(!this.snackEndTime.equals(temp.snackEndTime))
                    return false;
            } else {
                if (temp.snackEndTime != null)
                    return false;
            }

            // snackGoal:
            if (this.snackGoal != null) {
                if(!this.snackGoal.equals(temp.snackGoal))
                    return false;
            } else {
                if (temp.snackGoal != null)
                    return false;
            }

            // dinnerStartTime:
            if (this.dinnerStartTime != null) {
                if(!this.dinnerStartTime.equals(temp.dinnerStartTime))
                    return false;
            } else {
                if (temp.dinnerStartTime != null)
                    return false;
            }

            // dinnerEndTime:
            if (this.dinnerEndTime != null) {
                if(!this.dinnerEndTime.equals(temp.dinnerEndTime))
                    return false;
            } else {
                if (temp.dinnerEndTime != null)
                    return false;
            }

            // dinnerGoal:
            if (this.dinnerGoal != null) {
                if(!this.dinnerGoal.equals(temp.dinnerGoal))
                    return false;
            } else {
                if (temp.dinnerGoal != null)
                    return false;
            }

            // supperStartTime:
            if (this.supperStartTime != null) {
                if(!this.supperStartTime.equals(temp.supperStartTime))
                    return false;
            } else {
                if (temp.supperStartTime != null)
                    return false;
            }

            // supperEndTime:
            if (this.supperEndTime != null) {
                if(!this.supperEndTime.equals(temp.supperEndTime))
                    return false;
            } else {
                if (temp.supperEndTime != null)
                    return false;
            }

            // supperGoal:
            if (this.supperGoal != null) {
                if(!this.supperGoal.equals(temp.supperGoal))
                    return false;
            } else {
                if (temp.supperGoal != null)
                    return false;
            }

            // exerciseGoal:
            if (this.exerciseGoal != null) {
                if(!this.exerciseGoal.equals(temp.exerciseGoal))
                    return false;
            } else {
                if (temp.exerciseGoal != null)
                    return false;
            }

            // liquidDone:
            if (this.liquidDone != null) {
                if(!this.liquidDone.equals(temp.liquidDone))
                    return false;
            } else {
                if (temp.liquidDone != null)
                    return false;
            }

            // liquidGoal:
            if (this.liquidGoal != null) {
                if(!this.liquidGoal.equals(temp.liquidGoal))
                    return false;
            } else {
                if (temp.liquidGoal != null)
                    return false;
            }

            // oilDone:
            if (this.oilDone != null) {
                if(!this.oilDone.equals(temp.oilDone))
                    return false;
            } else {
                if (temp.oilDone != null)
                    return false;
            }

            // oilGoal:
            if (this.oilGoal != null) {
                if(!this.oilGoal.equals(temp.oilGoal))
                    return false;
            } else {
                if (temp.oilGoal != null)
                    return false;
            }

            // supplementDone:
            if (this.supplementDone != null) {
                if(!this.supplementDone.equals(temp.supplementDone))
                    return false;
            } else {
                if (temp.supplementDone != null)
                    return false;
            }

            // supplementGoal:
            if (this.supplementGoal != null) {
                if(!this.supplementGoal.equals(temp.supplementGoal))
                    return false;
            } else {
                if (temp.supplementGoal != null)
                    return false;
            }

            // note:
            if (this.note != null) {
                if(!this.note.equals(temp.note))
                    return false;
            } else {
                if (temp.note != null)
                    return false;
            }

            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result;
        result = id == null? 0 : id.hashCode();
        result += dateId == null? 0: dateId.hashCode();
        result += allowed == null? 0: allowed.hashCode();
        result += breakfastStartTime == null? 0: breakfastStartTime.hashCode();
        result += breakfastEndTime == null? 0: breakfastEndTime.hashCode();
        result += breakfastGoal == null? 0: breakfastGoal.hashCode();
        result += brunchStartTime == null? 0: brunchStartTime.hashCode();
        result += brunchEndTime == null? 0: brunchEndTime.hashCode();
        result += brunchGoal == null? 0: brunchGoal.hashCode();
        result += lunchStartTime == null? 0: lunchStartTime.hashCode();
        result += lunchEndTime == null? 0: lunchEndTime.hashCode();
        result += lunchGoal == null? 0: lunchGoal.hashCode();
        result += snackStartTime == null? 0: snackStartTime.hashCode();
        result += snackEndTime == null? 0: snackEndTime.hashCode();
        result += snackGoal == null? 0: snackGoal.hashCode();
        result += dinnerStartTime == null? 0: dinnerStartTime.hashCode();
        result += dinnerEndTime == null? 0: dinnerEndTime.hashCode();
        result += dinnerGoal == null? 0: dinnerGoal.hashCode();
        result += supperStartTime == null? 0: supperStartTime.hashCode();
        result += supperEndTime == null? 0: supperEndTime.hashCode();
        result += supperGoal == null? 0: supperGoal.hashCode();
        result += exerciseGoal == null? 0: exerciseGoal.hashCode();
        result += liquidDone == null? 0: liquidDone.hashCode();
        result += liquidGoal == null? 0: liquidGoal.hashCode();
        result += oilDone == null? 0: oilDone.hashCode();
        result += oilGoal == null? 0: oilGoal.hashCode();
        result += supplementDone == null? 0: supplementDone.hashCode();
        result += supplementGoal == null? 0: supplementGoal.hashCode();
        result += note == null? 0: note.hashCode();
        return result;
    }

    @Override
    public String toString() {

        return "["
                + Contract.Days._ID + "=" + id + ", "
                + Contract.Days.DATE_ID + "=" + dateId + ", "
                + Contract.Days.ALLOWED + "=" + allowed + ", "
                + Contract.Days.BREAKFAST_START_TIME + "=" + breakfastStartTime + ", "
                + Contract.Days.BREAKFAST_END_TIME + "=" + breakfastEndTime + ", "
                + Contract.Days.BREAKFAST_GOAL + "=" + breakfastGoal + ", "
                + Contract.Days.BRUNCH_START_TIME + "=" + brunchStartTime + ", "
                + Contract.Days.BRUNCH_END_TIME + "=" + brunchEndTime + ", "
                + Contract.Days.BRUNCH_GOAL + "=" + brunchGoal + ", "
                + Contract.Days.LUNCH_START_TIME + "=" + lunchStartTime + ", "
                + Contract.Days.LUNCH_END_TIME + "=" + lunchEndTime + ", "
                + Contract.Days.LUNCH_GOAL + "=" + lunchGoal + ", "
                + Contract.Days.SNACK_START_TIME + "=" + snackStartTime + ", "
                + Contract.Days.SNACK_END_TIME + "=" + snackEndTime + ", "
                + Contract.Days.SNACK_GOAL + "=" + snackGoal + ", "
                + Contract.Days.DINNER_START_TIME + "=" + dinnerStartTime + ", "
                + Contract.Days.DINNER_END_TIME + "=" + dinnerEndTime + ", "
                + Contract.Days.DINNER_GOAL + "=" + dinnerGoal + ", "
                + Contract.Days.SUPPER_START_TIME + "=" + supperStartTime + ", "
                + Contract.Days.SUPPER_END_TIME + "=" + supperEndTime + ", "
                + Contract.Days.SUPPER_GOAL + "=" + supperGoal + ", "
                + Contract.Days.EXERCISE_GOAL + "=" + exerciseGoal + ", "
                + Contract.Days.LIQUID_DONE + "=" + liquidDone + ", "
                + Contract.Days.LIQUID_GOAL + "=" + liquidGoal + ", "
                + Contract.Days.OIL_DONE + "=" + oilDone + ", "
                + Contract.Days.OIL_GOAL + "=" + oilGoal + ", "
                + Contract.Days.SUPPLEMENT_DONE + "=" + supplementDone + ", "
                + Contract.Days.SUPPLEMENT_GOAL + "=" + supplementGoal + ", "
                + Contract.Days.NOTE + "=" + note
                + "]";
    }

    public void setStartTimeForMeal(int value, int meal) {
        switch (meal) {
            case Meals.BREAKFAST:
                setBreakfastStartTime(value);
                break;
            case Meals.BRUNCH:
                setBrunchStartTime(value);
                break;
            case Meals.LUNCH:
                setLunchStartTime(value);
                break;
            case Meals.SNACK:
                setSnackStartTime(value);
                break;
            case Meals.DINNER:
                setDinnerStartTime(value);
                break;
            case Meals.SUPPER:
                setSupperStartTime(value);
                break;
            case Meals.EXERCISE:
                throw new IllegalStateException("Is illegal to set startTime for Exercise");
            default:
                throw new IllegalArgumentException("Invalid meal=" + meal + ".");
        }
    }

    public void setEndTimeForMeal(int value, int meal) {
        switch (meal) {
            case Meals.BREAKFAST:
                setBreakfastEndTime(value);
                break;
            case Meals.BRUNCH:
                setBrunchEndTime(value);
                break;
            case Meals.LUNCH:
                setLunchEndTime(value);
                break;
            case Meals.SNACK:
                setSnackEndTime(value);
                break;
            case Meals.DINNER:
                setDinnerEndTime(value);
                break;
            case Meals.SUPPER:
                setSupperEndTime(value);
                break;
            case Meals.EXERCISE:
                throw new IllegalStateException("Is illegal to set endTime for Exercise");
            default:
                throw new IllegalArgumentException("Invalid meal=" + meal + ".");
        }
    }

    public void setGoalForMeal(float value, int meal) {
        switch (meal) {
            case Meals.BREAKFAST:
                setBreakfastGoal(value);
                break;
            case Meals.BRUNCH:
                setBrunchGoal(value);
                break;
            case Meals.LUNCH:
                setLunchGoal(value);
                break;
            case Meals.SNACK:
                setSnackGoal(value);
                break;
            case Meals.DINNER:
                setDinnerGoal(value);
                break;
            case Meals.SUPPER:
                setSupperGoal(value);
                break;
            case Meals.EXERCISE:
                setExerciseGoal(value);
                break;
            default:
                throw new IllegalArgumentException("Invalid meal=" + meal + ".");
        }
    }

    public int getStartTimeForMeal(int meal) {
        switch (meal) {
            case Meals.BREAKFAST:
                return getBreakfastStartTime();
            case Meals.BRUNCH:
                return getBrunchStartTime();
            case Meals.LUNCH:
                return getLunchStartTime();
            case Meals.SNACK:
                return getSnackStartTime();
            case Meals.DINNER:
                return getDinnerStartTime();
            case Meals.SUPPER:
                return getSupperStartTime();
            case Meals.EXERCISE:
                return NO_TIME;
            default:
                throw new IllegalArgumentException("Invalid meal=" + meal + ".");
        }
    }

    public int getEndTimeForMeal(int meal) {
        switch (meal) {
            case Meals.BREAKFAST:
                return getBreakfastEndTime();
            case Meals.BRUNCH:
                return getBrunchEndTime();
            case Meals.LUNCH:
                return getLunchEndTime();
            case Meals.SNACK:
                return getSnackEndTime();
            case Meals.DINNER:
                return getDinnerEndTime();
            case Meals.SUPPER:
                return getSupperEndTime();
            case Meals.EXERCISE:
                return NO_TIME;
            default:
                throw new IllegalArgumentException("Invalid meal=" + meal + ".");
        }
    }

    public float getGoalForMeal(int meal) {
        switch (meal) {
            case Meals.BREAKFAST:
                return getBreakfastGoal();
            case Meals.BRUNCH:
                return getBrunchGoal();
            case Meals.LUNCH:
                return getLunchGoal();
            case Meals.SNACK:
                return getSnackGoal();
            case Meals.DINNER:
                return getDinnerGoal();
            case Meals.SUPPER:
                return getSupperGoal();
            case Meals.EXERCISE:
                return getExerciseGoal();
            default:
                throw new IllegalArgumentException("Invalid meal=" + meal + ".");
        }
    }

    public float getTotalGoalForMeals() {
        float result = 0.0f;
        for (int i = 0; i < Meals.getMealsCount() - 1; i++) {
            result += getGoalForMeal(i);
        }
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDateId() {
        return dateId;
    }

    public void setDateId(String dateId) {
        this.dateId = dateId;
    }

    public Float getAllowed() {
        return allowed;
    }

    public void setAllowed(Float allowed) {
        this.allowed = allowed;
    }

    public Integer getBreakfastStartTime() {
        return breakfastStartTime;
    }

    public void setBreakfastStartTime(Integer breakfastStartTime) {
        this.breakfastStartTime = breakfastStartTime;
    }

    public Integer getBreakfastEndTime() {
        return breakfastEndTime;
    }

    public void setBreakfastEndTime(Integer breakfastEndTime) {
        this.breakfastEndTime = breakfastEndTime;
    }

    public Float getBreakfastGoal() {
        return breakfastGoal;
    }

    public void setBreakfastGoal(Float breakfastGoal) {
        this.breakfastGoal = breakfastGoal;
    }

    public Integer getBrunchStartTime() {
        return brunchStartTime;
    }

    public void setBrunchStartTime(Integer brunchStartTime) {
        this.brunchStartTime = brunchStartTime;
    }

    public Integer getBrunchEndTime() {
        return brunchEndTime;
    }

    public void setBrunchEndTime(Integer brunchEndTime) {
        this.brunchEndTime = brunchEndTime;
    }

    public Float getBrunchGoal() {
        return brunchGoal;
    }

    public void setBrunchGoal(Float brunchGoal) {
        this.brunchGoal = brunchGoal;
    }

    public Integer getLunchStartTime() {
        return lunchStartTime;
    }

    public void setLunchStartTime(Integer lunchStartTime) {
        this.lunchStartTime = lunchStartTime;
    }

    public Integer getLunchEndTime() {
        return lunchEndTime;
    }

    public void setLunchEndTime(Integer lunchEndTime) {
        this.lunchEndTime = lunchEndTime;
    }

    public Float getLunchGoal() {
        return lunchGoal;
    }

    public void setLunchGoal(Float lunchGoal) {
        this.lunchGoal = lunchGoal;
    }

    public Integer getSnackStartTime() {
        return snackStartTime;
    }

    public void setSnackStartTime(Integer snackStartTime) {
        this.snackStartTime = snackStartTime;
    }

    public Integer getSnackEndTime() {
        return snackEndTime;
    }

    public void setSnackEndTime(Integer snackEndTime) {
        this.snackEndTime = snackEndTime;
    }

    public Float getSnackGoal() {
        return snackGoal;
    }

    public void setSnackGoal(Float snackGoal) {
        this.snackGoal = snackGoal;
    }

    public Integer getDinnerStartTime() {
        return dinnerStartTime;
    }

    public void setDinnerStartTime(Integer dinnerStartTime) {
        this.dinnerStartTime = dinnerStartTime;
    }

    public Integer getDinnerEndTime() {
        return dinnerEndTime;
    }

    public void setDinnerEndTime(Integer dinnerEndTime) {
        this.dinnerEndTime = dinnerEndTime;
    }

    public Float getDinnerGoal() {
        return dinnerGoal;
    }

    public void setDinnerGoal(Float dinnerGoal) {
        this.dinnerGoal = dinnerGoal;
    }

    public Integer getSupperStartTime() {
        return supperStartTime;
    }

    public void setSupperStartTime(Integer supperStartTime) {
        this.supperStartTime = supperStartTime;
    }

    public Integer getSupperEndTime() {
        return supperEndTime;
    }

    public void setSupperEndTime(Integer supperEndTime) {
        this.supperEndTime = supperEndTime;
    }

    public Float getSupperGoal() {
        return supperGoal;
    }

    public void setSupperGoal(Float supperGoal) {
        this.supperGoal = supperGoal;
    }

    public Float getExerciseGoal() {
        return exerciseGoal;
    }

    public void setExerciseGoal(Float exerciseGoal) {
        this.exerciseGoal = exerciseGoal;
    }

    public Integer getLiquidDone() {
        return liquidDone;
    }

    public void setLiquidDone(Integer liquidDone) {
        this.liquidDone = liquidDone;
    }

    public Integer getLiquidGoal() {
        return liquidGoal;
    }

    public void setLiquidGoal(Integer liquidGoal) {
        this.liquidGoal = liquidGoal;
    }

    public Integer getOilDone() {
        return oilDone;
    }

    public void setOilDone(Integer oilDone) {
        this.oilDone = oilDone;
    }

    public Integer getOilGoal() {
        return oilGoal;
    }

    public void setOilGoal(Integer oilGoal) {
        this.oilGoal = oilGoal;
    }

    public Integer getSupplementDone() {
        return supplementDone;
    }

    public void setSupplementDone(Integer supplementDone) {
        this.supplementDone = supplementDone;
    }

    public Integer getSupplementGoal() {
        return supplementGoal;
    }

    public void setSupplementGoal(Integer supplementGoal) {
        this.supplementGoal = supplementGoal;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    /*
         * Factories
         */
    public static DaysEntity fromCursor(Cursor cursor) {
        if(cursor == null) {
            throw new IllegalArgumentException("Cursor must be not null.");
        }

        return new DaysEntity(
                cursor.getColumnIndex(Contract.Days._ID) == -1 ?
                    null : cursor.getLong(cursor.getColumnIndex(Contract.Days._ID)),
                cursor.getColumnIndex(Contract.Days.DATE_ID) == -1 ?
                    null : cursor.getString(cursor.getColumnIndex(Contract.Days.DATE_ID)),
                cursor.getColumnIndex(Contract.Days.ALLOWED) == -1 ?
                    null : cursor.getFloat(cursor.getColumnIndex(Contract.Days.ALLOWED)),
                cursor.getColumnIndex(Contract.Days.BREAKFAST_START_TIME) == -1 ?
                    null : cursor.getInt(cursor.getColumnIndex(Contract.Days.BREAKFAST_START_TIME)),
                cursor.getColumnIndex(Contract.Days.BREAKFAST_END_TIME) == -1 ?
                    null : cursor.getInt(cursor.getColumnIndex(Contract.Days.BREAKFAST_END_TIME)),
                cursor.getColumnIndex(Contract.Days.BREAKFAST_GOAL) == -1 ?
                    null : cursor.getFloat(cursor.getColumnIndex(Contract.Days.BREAKFAST_GOAL)),
                cursor.getColumnIndex(Contract.Days.BRUNCH_START_TIME) == -1 ?
                    null : cursor.getInt(cursor.getColumnIndex(Contract.Days.BRUNCH_START_TIME)),
                cursor.getColumnIndex(Contract.Days.BRUNCH_END_TIME) == -1 ?
                    null : cursor.getInt(cursor.getColumnIndex(Contract.Days.BRUNCH_END_TIME)),
                cursor.getColumnIndex(Contract.Days.BRUNCH_GOAL) == -1 ?
                    null : cursor.getFloat(cursor.getColumnIndex(Contract.Days.BRUNCH_GOAL)),
                cursor.getColumnIndex(Contract.Days.LUNCH_START_TIME) == -1 ?
                    null : cursor.getInt(cursor.getColumnIndex(Contract.Days.LUNCH_START_TIME)),
                cursor.getColumnIndex(Contract.Days.LUNCH_END_TIME) == -1 ?
                    null : cursor.getInt(cursor.getColumnIndex(Contract.Days.LUNCH_END_TIME)),
                cursor.getColumnIndex(Contract.Days.LUNCH_GOAL) == -1 ?
                    null : cursor.getFloat(cursor.getColumnIndex(Contract.Days.LUNCH_GOAL)),
                cursor.getColumnIndex(Contract.Days.SNACK_START_TIME) == -1 ?
                    null : cursor.getInt(cursor.getColumnIndex(Contract.Days.SNACK_START_TIME)),
                cursor.getColumnIndex(Contract.Days.SNACK_END_TIME) == -1 ?
                    null : cursor.getInt(cursor.getColumnIndex(Contract.Days.SNACK_END_TIME)),
                cursor.getColumnIndex(Contract.Days.SNACK_GOAL) == -1 ?
                    null : cursor.getFloat(cursor.getColumnIndex(Contract.Days.SNACK_GOAL)),
                cursor.getColumnIndex(Contract.Days.DINNER_START_TIME) == -1 ?
                    null : cursor.getInt(cursor.getColumnIndex(Contract.Days.DINNER_START_TIME)),
                cursor.getColumnIndex(Contract.Days.DINNER_END_TIME) == -1 ?
                    null : cursor.getInt(cursor.getColumnIndex(Contract.Days.DINNER_END_TIME)),
                cursor.getColumnIndex(Contract.Days.DINNER_GOAL) == -1 ?
                    null : cursor.getFloat(cursor.getColumnIndex(Contract.Days.DINNER_GOAL)),
                cursor.getColumnIndex(Contract.Days.SUPPER_START_TIME) == -1 ?
                    null : cursor.getInt(cursor.getColumnIndex(Contract.Days.SUPPER_START_TIME)),
                cursor.getColumnIndex(Contract.Days.SUPPER_END_TIME) == -1 ?
                    null : cursor.getInt(cursor.getColumnIndex(Contract.Days.SUPPER_END_TIME)),
                cursor.getColumnIndex(Contract.Days.SUPPER_GOAL) == -1 ?
                    null : cursor.getFloat(cursor.getColumnIndex(Contract.Days.SUPPER_GOAL)),
                cursor.getColumnIndex(Contract.Days.EXERCISE_GOAL) == -1 ?
                    null : cursor.getFloat(cursor.getColumnIndex(Contract.Days.EXERCISE_GOAL)),
                cursor.getColumnIndex(Contract.Days.LIQUID_DONE) == -1 ?
                    null : cursor.getInt(cursor.getColumnIndex(Contract.Days.LIQUID_DONE)),
                cursor.getColumnIndex(Contract.Days.LIQUID_GOAL) == -1 ?
                    null : cursor.getInt(cursor.getColumnIndex(Contract.Days.LIQUID_GOAL)),
                cursor.getColumnIndex(Contract.Days.OIL_DONE) == -1 ?
                    null : cursor.getInt(cursor.getColumnIndex(Contract.Days.OIL_DONE)),
                cursor.getColumnIndex(Contract.Days.OIL_GOAL) == -1 ?
                    null : cursor.getInt(cursor.getColumnIndex(Contract.Days.OIL_GOAL)),
                cursor.getColumnIndex(Contract.Days.SUPPLEMENT_DONE) == -1 ?
                    null : cursor.getInt(cursor.getColumnIndex(Contract.Days.SUPPLEMENT_DONE)),
                cursor.getColumnIndex(Contract.Days.SUPPLEMENT_GOAL) == -1 ?
                    null : cursor.getInt(cursor.getColumnIndex(Contract.Days.SUPPLEMENT_GOAL)),
                cursor.getColumnIndex(Contract.Days.NOTE) == -1 ?
                    null : cursor.getString(cursor.getColumnIndex(Contract.Days.NOTE)));
    }

    @SuppressWarnings("UnusedDeclaration")
    public static DaysEntity fromJoinInContentValues(ContentValues principal, ContentValues complement) {
        if (principal == null || complement == null) {
            throw new IllegalArgumentException("Principal and complement must be not null.");
        }

        DaysEntity result = new DaysEntity(null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null);

        // Id:
        if (principal.getAsLong(Contract.Days._ID) != null) {
            result.setId(principal.getAsLong(Contract.Days._ID));
        } else {
            result.setId(complement.getAsLong(Contract.Days._ID));
        }

        // Date Id:
        if (principal.getAsString(Contract.Days.DATE_ID) != null) {
            result.setDateId(principal.getAsString(Contract.Days.DATE_ID));
        } else {
            result.setDateId(complement.getAsString(Contract.Days.DATE_ID));
        }

        // Allowed:
        if (principal.getAsFloat(Contract.Days.ALLOWED) != null) {
            result.setAllowed(principal.getAsFloat(Contract.Days.ALLOWED));
        } else {
            result.setAllowed(complement.getAsFloat(Contract.Days.ALLOWED));
        }

        // Breakfast:
        if (principal.getAsInteger(Contract.Days.BREAKFAST_START_TIME) != null) {
            result.setBreakfastStartTime(principal.getAsInteger(Contract.Days.BREAKFAST_START_TIME));
        } else {
            result.setBreakfastStartTime(complement.getAsInteger(Contract.Days.BREAKFAST_START_TIME));
        }
        if (principal.getAsInteger(Contract.Days.BREAKFAST_END_TIME) != null) {
            result.setBreakfastEndTime(principal.getAsInteger(Contract.Days.BREAKFAST_END_TIME));
        } else {
            result.setBreakfastEndTime(complement.getAsInteger(Contract.Days.BREAKFAST_END_TIME));
        }
        if (principal.getAsFloat(Contract.Days.BREAKFAST_GOAL) != null) {
            result.setBreakfastGoal(principal.getAsFloat(Contract.Days.BREAKFAST_GOAL));
        } else {
            result.setBreakfastGoal(complement.getAsFloat(Contract.Days.BREAKFAST_GOAL));
        }

        // Brunch:
        if (principal.getAsInteger(Contract.Days.BRUNCH_START_TIME) != null) {
            result.setBrunchStartTime(principal.getAsInteger(Contract.Days.BRUNCH_START_TIME));
        } else {
            result.setBrunchStartTime(complement.getAsInteger(Contract.Days.BRUNCH_START_TIME));
        }
        if (principal.getAsInteger(Contract.Days.BRUNCH_END_TIME) != null) {
            result.setBrunchEndTime(principal.getAsInteger(Contract.Days.BRUNCH_END_TIME));
        } else {
            result.setBrunchEndTime(complement.getAsInteger(Contract.Days.BRUNCH_END_TIME));
        }
        if (principal.getAsFloat(Contract.Days.BRUNCH_GOAL) != null) {
            result.setBrunchGoal(principal.getAsFloat(Contract.Days.BRUNCH_GOAL));
        } else {
            result.setBrunchGoal(complement.getAsFloat(Contract.Days.BRUNCH_GOAL));
        }

        // Lunch:
        if (principal.getAsInteger(Contract.Days.LUNCH_START_TIME) != null) {
            result.setLunchStartTime(principal.getAsInteger(Contract.Days.LUNCH_START_TIME));
        } else {
            result.setLunchStartTime(complement.getAsInteger(Contract.Days.LUNCH_START_TIME));
        }
        if (principal.getAsInteger(Contract.Days.LUNCH_END_TIME) != null) {
            result.setLunchEndTime(principal.getAsInteger(Contract.Days.LUNCH_END_TIME));
        } else {
            result.setLunchEndTime(complement.getAsInteger(Contract.Days.LUNCH_END_TIME));
        }
        if (principal.getAsFloat(Contract.Days.LUNCH_GOAL) != null) {
            result.setLunchGoal(principal.getAsFloat(Contract.Days.LUNCH_GOAL));
        } else {
            result.setLunchGoal(complement.getAsFloat(Contract.Days.LUNCH_GOAL));
        }

        // Snack:
        if (principal.getAsInteger(Contract.Days.SNACK_START_TIME) != null) {
            result.setSnackStartTime(principal.getAsInteger(Contract.Days.SNACK_START_TIME));
        } else {
            result.setSnackStartTime(complement.getAsInteger(Contract.Days.SNACK_START_TIME));
        }
        if (principal.getAsInteger(Contract.Days.SNACK_END_TIME) != null) {
            result.setSnackEndTime(principal.getAsInteger(Contract.Days.SNACK_END_TIME));
        } else {
            result.setSnackEndTime(complement.getAsInteger(Contract.Days.SNACK_END_TIME));
        }
        if (principal.getAsFloat(Contract.Days.SNACK_GOAL) != null) {
            result.setSnackGoal(principal.getAsFloat(Contract.Days.SNACK_GOAL));
        } else {
            result.setSnackGoal(complement.getAsFloat(Contract.Days.SNACK_GOAL));
        }

        // Dinner:
        if (principal.getAsInteger(Contract.Days.DINNER_START_TIME) != null) {
            result.setDinnerStartTime(principal.getAsInteger(Contract.Days.DINNER_START_TIME));
        } else {
            result.setDinnerStartTime(complement.getAsInteger(Contract.Days.DINNER_START_TIME));
        }
        if (principal.getAsInteger(Contract.Days.DINNER_END_TIME) != null) {
            result.setDinnerEndTime(principal.getAsInteger(Contract.Days.DINNER_END_TIME));
        } else {
            result.setDinnerEndTime(complement.getAsInteger(Contract.Days.DINNER_END_TIME));
        }
        if (principal.getAsFloat(Contract.Days.DINNER_GOAL) != null) {
            result.setDinnerGoal(principal.getAsFloat(Contract.Days.DINNER_GOAL));
        } else {
            result.setDinnerGoal(complement.getAsFloat(Contract.Days.DINNER_GOAL));
        }

        // Supper:
        if (principal.getAsInteger(Contract.Days.SUPPER_START_TIME) != null) {
            result.setSupperStartTime(principal.getAsInteger(Contract.Days.SUPPER_START_TIME));
        } else {
            result.setSupperStartTime(complement.getAsInteger(Contract.Days.SUPPER_START_TIME));
        }
        if (principal.getAsInteger(Contract.Days.SUPPER_END_TIME) != null) {
            result.setSupperEndTime(principal.getAsInteger(Contract.Days.SUPPER_END_TIME));
        } else {
            result.setSupperEndTime(complement.getAsInteger(Contract.Days.SUPPER_END_TIME));
        }
        if (principal.getAsFloat(Contract.Days.SUPPER_GOAL) != null) {
            result.setSupperGoal(principal.getAsFloat(Contract.Days.SUPPER_GOAL));
        } else {
            result.setSupperGoal(complement.getAsFloat(Contract.Days.SUPPER_GOAL));
        }

        // Exercise:
        if (principal.getAsFloat(Contract.Days.EXERCISE_GOAL) != null) {
            result.setExerciseGoal(principal.getAsFloat(Contract.Days.EXERCISE_GOAL));
        } else {
            result.setExerciseGoal(complement.getAsFloat(Contract.Days.EXERCISE_GOAL));
        }

        // Liquid:
        if (principal.getAsInteger(Contract.Days.LIQUID_DONE) != null) {
            result.setLiquidDone(principal.getAsInteger(Contract.Days.LIQUID_DONE));
        } else {
            result.setLiquidDone(complement.getAsInteger(Contract.Days.LIQUID_DONE));
        }
        if (principal.getAsInteger(Contract.Days.LIQUID_GOAL) != null) {
            result.setLiquidGoal(principal.getAsInteger(Contract.Days.LIQUID_GOAL));
        } else {
            result.setLiquidGoal(complement.getAsInteger(Contract.Days.LIQUID_GOAL));
        }

        // Oil:
        if (principal.getAsInteger(Contract.Days.OIL_DONE) != null) {
            result.setOilDone(principal.getAsInteger(Contract.Days.OIL_DONE));
        } else {
            result.setOilDone(complement.getAsInteger(Contract.Days.OIL_DONE));
        }
        if (principal.getAsInteger(Contract.Days.OIL_GOAL) != null) {
            result.setOilGoal(principal.getAsInteger(Contract.Days.OIL_GOAL));
        } else {
            result.setOilGoal(complement.getAsInteger(Contract.Days.OIL_GOAL));
        }

        // Supplement:
        if (principal.getAsInteger(Contract.Days.SUPPLEMENT_DONE) != null) {
            result.setSupplementDone(principal.getAsInteger(Contract.Days.SUPPLEMENT_DONE));
        } else {
            result.setSupplementDone(complement.getAsInteger(Contract.Days.SUPPLEMENT_DONE));
        }
        if (principal.getAsInteger(Contract.Days.SUPPLEMENT_GOAL) != null) {
            result.setSupplementGoal(principal.getAsInteger(Contract.Days.SUPPLEMENT_GOAL));
        } else {
            result.setSupplementGoal(complement.getAsInteger(Contract.Days.SUPPLEMENT_GOAL));
        }

        // Note:
        if (principal.getAsString(Contract.Days.NOTE) != null) {
            result.setNote(principal.getAsString(Contract.Days.NOTE));
        } else {
            result.setNote(complement.getAsString(Contract.Days.NOTE));
        }

        return result;
    }

    @SuppressWarnings("UnusedDeclaration")
    public static DaysEntity fromContentValues(ContentValues values) {
        if (values == null) {
            throw new IllegalArgumentException("Values must be not null.");
        }

        return new DaysEntity(
                values.getAsLong(Contract.Days._ID),
                values.getAsString(Contract.Days.DATE_ID),
                values.getAsFloat(Contract.Days.ALLOWED),
                values.getAsInteger(Contract.Days.BREAKFAST_START_TIME),
                values.getAsInteger(Contract.Days.BREAKFAST_END_TIME),
                values.getAsFloat(Contract.Days.BREAKFAST_GOAL),
                values.getAsInteger(Contract.Days.BRUNCH_START_TIME),
                values.getAsInteger(Contract.Days.BRUNCH_END_TIME),
                values.getAsFloat(Contract.Days.BRUNCH_GOAL),
                values.getAsInteger(Contract.Days.LUNCH_START_TIME),
                values.getAsInteger(Contract.Days.LUNCH_END_TIME),
                values.getAsFloat(Contract.Days.LUNCH_GOAL),
                values.getAsInteger(Contract.Days.SNACK_START_TIME),
                values.getAsInteger(Contract.Days.SNACK_END_TIME),
                values.getAsFloat(Contract.Days.SNACK_GOAL),
                values.getAsInteger(Contract.Days.DINNER_START_TIME),
                values.getAsInteger(Contract.Days.DINNER_END_TIME),
                values.getAsFloat(Contract.Days.DINNER_GOAL),
                values.getAsInteger(Contract.Days.SUPPER_START_TIME),
                values.getAsInteger(Contract.Days.SUPPER_END_TIME),
                values.getAsFloat(Contract.Days.SUPPER_GOAL),
                values.getAsFloat(Contract.Days.EXERCISE_GOAL),
                values.getAsInteger(Contract.Days.LIQUID_DONE),
                values.getAsInteger(Contract.Days.LIQUID_GOAL),
                values.getAsInteger(Contract.Days.OIL_DONE),
                values.getAsInteger(Contract.Days.OIL_GOAL),
                values.getAsInteger(Contract.Days.SUPPLEMENT_DONE),
                values.getAsInteger(Contract.Days.SUPPLEMENT_GOAL),
                values.getAsString(Contract.Days.NOTE));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {

        ParcelUtils.safeFromNullWriteLong(destination, id, SENTINEL_ID);
        destination.writeString(dateId);
        ParcelUtils.safeFromNullWriteFloat(destination, allowed, SENTINEL_FLOAT_VALUE);
        ParcelUtils.safeFromNullWriteInteger(destination, breakfastStartTime, SENTINEL_INT_VALUE);
        ParcelUtils.safeFromNullWriteInteger(destination, breakfastEndTime, SENTINEL_INT_VALUE);
        ParcelUtils.safeFromNullWriteFloat(destination, breakfastGoal, SENTINEL_FLOAT_VALUE);
        ParcelUtils.safeFromNullWriteInteger(destination, brunchStartTime, SENTINEL_INT_VALUE);
        ParcelUtils.safeFromNullWriteInteger(destination, brunchEndTime, SENTINEL_INT_VALUE);
        ParcelUtils.safeFromNullWriteFloat(destination, brunchGoal, SENTINEL_FLOAT_VALUE);
        ParcelUtils.safeFromNullWriteInteger(destination, lunchStartTime, SENTINEL_INT_VALUE);
        ParcelUtils.safeFromNullWriteInteger(destination, lunchEndTime, SENTINEL_INT_VALUE);
        ParcelUtils.safeFromNullWriteFloat(destination, lunchGoal, SENTINEL_FLOAT_VALUE);
        ParcelUtils.safeFromNullWriteInteger(destination, snackStartTime, SENTINEL_INT_VALUE);
        ParcelUtils.safeFromNullWriteInteger(destination, snackEndTime, SENTINEL_INT_VALUE);
        ParcelUtils.safeFromNullWriteFloat(destination, snackGoal, SENTINEL_FLOAT_VALUE);
        ParcelUtils.safeFromNullWriteInteger(destination, dinnerStartTime, SENTINEL_INT_VALUE);
        ParcelUtils.safeFromNullWriteInteger(destination, dinnerEndTime, SENTINEL_INT_VALUE);
        ParcelUtils.safeFromNullWriteFloat(destination, dinnerGoal, SENTINEL_FLOAT_VALUE);
        ParcelUtils.safeFromNullWriteInteger(destination, supperStartTime, SENTINEL_INT_VALUE);
        ParcelUtils.safeFromNullWriteInteger(destination, supperEndTime, SENTINEL_INT_VALUE);
        ParcelUtils.safeFromNullWriteFloat(destination, supperGoal, SENTINEL_FLOAT_VALUE);
        ParcelUtils.safeFromNullWriteFloat(destination, exerciseGoal, SENTINEL_FLOAT_VALUE);
        ParcelUtils.safeFromNullWriteInteger(destination, liquidDone, SENTINEL_INT_VALUE);
        ParcelUtils.safeFromNullWriteInteger(destination, liquidGoal, SENTINEL_INT_VALUE);
        ParcelUtils.safeFromNullWriteInteger(destination, oilDone, SENTINEL_INT_VALUE);
        ParcelUtils.safeFromNullWriteInteger(destination, oilGoal, SENTINEL_INT_VALUE);
        ParcelUtils.safeFromNullWriteInteger(destination, supplementDone, SENTINEL_INT_VALUE);
        ParcelUtils.safeFromNullWriteInteger(destination, supplementGoal, SENTINEL_INT_VALUE);
        destination.writeString(note);
    }

    public static final Parcelable.Creator<DaysEntity> CREATOR
            = new Parcelable.Creator<DaysEntity>() {

        public DaysEntity createFromParcel(Parcel in) {
            final DaysEntity result = new DaysEntity();

            result.setId(ParcelUtils.safeFromNullReadLong(in, SENTINEL_ID));
            result.setDateId(in.readString());
            result.setAllowed(ParcelUtils.safeFromNullReadFloat(in, SENTINEL_FLOAT_VALUE));
            result.setBreakfastStartTime(ParcelUtils.safeFromNullReadInteger(in, SENTINEL_INT_VALUE));
            result.setBreakfastEndTime(ParcelUtils.safeFromNullReadInteger(in, SENTINEL_INT_VALUE));
            result.setBreakfastGoal(ParcelUtils.safeFromNullReadFloat(in, SENTINEL_FLOAT_VALUE));
            result.setBrunchStartTime(ParcelUtils.safeFromNullReadInteger(in, SENTINEL_INT_VALUE));
            result.setBrunchEndTime(ParcelUtils.safeFromNullReadInteger(in, SENTINEL_INT_VALUE));
            result.setBrunchGoal(ParcelUtils.safeFromNullReadFloat(in, SENTINEL_FLOAT_VALUE));
            result.setLunchStartTime(ParcelUtils.safeFromNullReadInteger(in, SENTINEL_INT_VALUE));
            result.setLunchEndTime(ParcelUtils.safeFromNullReadInteger(in, SENTINEL_INT_VALUE));
            result.setLunchGoal(ParcelUtils.safeFromNullReadFloat(in, SENTINEL_FLOAT_VALUE));
            result.setSnackStartTime(ParcelUtils.safeFromNullReadInteger(in, SENTINEL_INT_VALUE));
            result.setSnackEndTime(ParcelUtils.safeFromNullReadInteger(in, SENTINEL_INT_VALUE));
            result.setSnackGoal(ParcelUtils.safeFromNullReadFloat(in, SENTINEL_FLOAT_VALUE));
            result.setDinnerStartTime(ParcelUtils.safeFromNullReadInteger(in, SENTINEL_INT_VALUE));
            result.setDinnerEndTime(ParcelUtils.safeFromNullReadInteger(in, SENTINEL_INT_VALUE));
            result.setDinnerGoal(ParcelUtils.safeFromNullReadFloat(in, SENTINEL_FLOAT_VALUE));
            result.setSupperStartTime(ParcelUtils.safeFromNullReadInteger(in, SENTINEL_INT_VALUE));
            result.setSupperEndTime(ParcelUtils.safeFromNullReadInteger(in, SENTINEL_INT_VALUE));
            result.setSupperGoal(ParcelUtils.safeFromNullReadFloat(in, SENTINEL_FLOAT_VALUE));
            result.setExerciseGoal(ParcelUtils.safeFromNullReadFloat(in, SENTINEL_FLOAT_VALUE));
            result.setLiquidDone(ParcelUtils.safeFromNullReadInteger(in, SENTINEL_INT_VALUE));
            result.setLiquidGoal(ParcelUtils.safeFromNullReadInteger(in, SENTINEL_INT_VALUE));
            result.setOilDone(ParcelUtils.safeFromNullReadInteger(in, SENTINEL_INT_VALUE));
            result.setOilGoal(ParcelUtils.safeFromNullReadInteger(in, SENTINEL_INT_VALUE));
            result.setSupplementDone(ParcelUtils.safeFromNullReadInteger(in, SENTINEL_INT_VALUE));
            result.setSupplementGoal(ParcelUtils.safeFromNullReadInteger(in, SENTINEL_INT_VALUE));
            result.setNote(in.readString());

            return result;
        }

        public DaysEntity[] newArray(int size) {
            return new DaysEntity[size];
        }
    };
}