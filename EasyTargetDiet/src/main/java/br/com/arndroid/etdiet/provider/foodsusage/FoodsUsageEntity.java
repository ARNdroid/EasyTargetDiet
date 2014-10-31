package br.com.arndroid.etdiet.provider.foodsusage;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import br.com.arndroid.etdiet.provider.AbstractEntity;
import br.com.arndroid.etdiet.provider.Contract;

import static br.com.arndroid.etdiet.provider.Contract.TargetException.FieldDescriptor;

public class FoodsUsageEntity extends AbstractEntity implements Parcelable {

    // Attention: it's a Parcelable. The order and the number of fields matter.
    private Long id;
    private String dateId;
    private Integer meal;
    private Integer time;
    private String description;
    private Float value;

	/*
	 * Implementation
	 */

    public FoodsUsageEntity(Long id, String dateId, Integer meal, Integer time, String description,
                            Float value) {
        this.id = id;
        this.dateId = dateId;
        this.meal = meal;
        this.time = time;
        this.description = description;
        this.value = value;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Contract.FoodsUsage._ID, id);
        cv.put(Contract.FoodsUsage.DATE_ID, dateId);
        cv.put(Contract.FoodsUsage.MEAL, meal);
        cv.put(Contract.FoodsUsage.TIME, time);
        cv.put(Contract.FoodsUsage.DESCRIPTION, description);
        cv.put(Contract.FoodsUsage.VALUE, value);
        return cv;
    }

    @Override
    public ContentValues toContentValuesIgnoreNulls() {
        ContentValues cv = new ContentValues();
        if (id != null) {
            cv.put(Contract.FoodsUsage._ID, id);
        }
        if (dateId != null) {
            cv.put(Contract.FoodsUsage.DATE_ID, dateId);
        }
        if (meal != null) {
            cv.put(Contract.FoodsUsage.MEAL, meal);
        }
        if (time != null) {
            cv.put(Contract.FoodsUsage.TIME, time);
        }
        if (description != null) {
            cv.put(Contract.FoodsUsage.DESCRIPTION, description);
        }
        if (value != null) {
            cv.put(Contract.FoodsUsage.VALUE, value);
        }
        return cv;
    }

    @Override
    public void validateOrThrow() {

        // Day (id) must be not null:
        if (dateId == null) {
            throwNullValueException(Contract.FoodsUsage.DATE_ID);
        }

        // Meal must be not null:
        if (meal == null) {
            throwNullValueException(Contract.FoodsUsage.MEAL);
        }

        // Time must be not null:
        if (time == null) {
            throwNullValueException(Contract.FoodsUsage.TIME);
        }

        // Description must be not null:
        if (description == null) {
            throwNullValueException(Contract.FoodsUsage.DESCRIPTION);
        }

        // Value must be not null:
        if (value == null) {
            throwNullValueException(Contract.FoodsUsage.VALUE);
        }
    }

    private void throwNullValueException(String columnName) {
        throw new Contract.TargetException(Contract.TargetException.NULL_VALUE,
                new FieldDescriptor[] {new FieldDescriptor(Contract.FoodsUsage.TABLE_NAME,
                        columnName, null)}, null);
    }

    @Override
    public String getTableName() {
        return Contract.FoodsUsage.TABLE_NAME;
    }

    @Override
    public String getIdColumnName() {
        return Contract.FoodsUsage._ID;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;

        } else if (obj instanceof FoodsUsageEntity) {
            FoodsUsageEntity temp = (FoodsUsageEntity) obj;
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
                if(!this.dateId.equals(temp.dateId))
                    return false;
            } else {
                if (temp.dateId != null)
                    return false;
            }

            // meal:
            if (this.meal != null) {
                if (!this.meal.equals(temp.meal))
                    return false;
            } else {
                if (temp.meal != null)
                    return false;
            }

            // time:
            if (this.time != null) {
                if (!this.time.equals(temp.time))
                    return false;
            } else {
                if (temp.time != null)
                    return false;
            }

            // description:
            if (this.description != null) {
                if (!this.description.equals(temp.description))
                    return false;
            } else {
                if (temp.description != null)
                    return false;
            }

            // value:
            if (this.value != null) {
                if (!this.value.equals(temp.value))
                    return false;
            } else {
                if (temp.value != null)
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
        result += meal == null? 0: meal.hashCode();
        result += time == null? 0: time.hashCode();
        result += description == null? 0: description.hashCode();
        result += value == null? 0: value.hashCode();
        return result;
    }

    @Override
    public String toString() {

        return "["
                + Contract.FoodsUsage._ID + "=" + id + ", "
                + Contract.FoodsUsage.DATE_ID + "=" + dateId + ", "
                + Contract.FoodsUsage.MEAL + "=" + meal + ", "
                + Contract.FoodsUsage.TIME + "=" + time + ", "
                + Contract.FoodsUsage.DESCRIPTION + "=" + description + ", "
                + Contract.FoodsUsage.VALUE + "=" + value
                + "]";
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

    public Integer getMeal() {
        return meal;
    }

    public void setMeal(Integer meal) {
        this.meal = meal;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {
        destination.writeLong(id);
        destination.writeString(dateId);
        destination.writeInt(meal);
        destination.writeInt(time);
        destination.writeString(description);
        destination.writeFloat(value);
    }

    /*
     * Factories
     */
    public static FoodsUsageEntity fromCursor(Cursor cursor) {
        if(cursor == null) {
            throw new IllegalArgumentException("Cursor must be not null.");
        }

        return new FoodsUsageEntity(
                cursor.getColumnIndex(Contract.FoodsUsage._ID) == -1 ?
                    null : cursor.getLong(cursor.getColumnIndex(Contract.FoodsUsage._ID)),
                cursor.getColumnIndex(Contract.FoodsUsage.DATE_ID) == -1 ?
                    null : cursor.getString(cursor.getColumnIndex(Contract.FoodsUsage.DATE_ID)),
                cursor.getColumnIndex(Contract.FoodsUsage.MEAL) == -1 ?
                    null : cursor.getInt(cursor.getColumnIndex(Contract.FoodsUsage.MEAL)),
                cursor.getColumnIndex(Contract.FoodsUsage.TIME) == -1 ?
                    null : cursor.getInt(cursor.getColumnIndex(Contract.FoodsUsage.TIME)),
                cursor.getColumnIndex(Contract.FoodsUsage.DESCRIPTION) == -1 ?
                    null : cursor.getString(cursor.getColumnIndex(Contract.FoodsUsage.DESCRIPTION)),
                cursor.getColumnIndex(Contract.FoodsUsage.VALUE) == -1 ?
                    null : cursor.getFloat(cursor.getColumnIndex(Contract.FoodsUsage.VALUE)));
    }

    @SuppressWarnings("UnusedDeclaration")
    public static FoodsUsageEntity fromJoinInContentValues(ContentValues principal, ContentValues complement) {
        if (principal == null || complement == null) {
            throw new IllegalArgumentException("Principal and complement must be not null.");
        }

        FoodsUsageEntity result = new FoodsUsageEntity(null, null, null, null, null, null);
        // id:
        if (principal.getAsLong(Contract.FoodsUsage._ID) != null) {
            result.setId(principal.getAsLong(Contract.FoodsUsage._ID));
        } else {
            result.setId(complement.getAsLong(Contract.FoodsUsage._ID));
        }
        // day_id:
        if (principal.getAsString(Contract.FoodsUsage.DATE_ID) != null) {
            result.setDateId(principal.getAsString(Contract.FoodsUsage.DATE_ID));
        } else {
            result.setDateId(complement.getAsString(Contract.FoodsUsage.DATE_ID));
        }
        // meal:
        if (principal.getAsInteger(Contract.FoodsUsage.MEAL) != null) {
            result.setMeal(principal.getAsInteger(Contract.FoodsUsage.MEAL));
        } else {
            result.setMeal(complement.getAsInteger(Contract.FoodsUsage.MEAL));
        }
        // time:
        if (principal.getAsInteger(Contract.FoodsUsage.TIME) != null) {
            result.setTime(principal.getAsInteger(Contract.FoodsUsage.TIME));
        } else {
            result.setTime(complement.getAsInteger(Contract.FoodsUsage.TIME));
        }
        // description:
        if (principal.getAsString(Contract.FoodsUsage.DESCRIPTION) != null) {
            result.setDescription(principal.getAsString(Contract.FoodsUsage.DESCRIPTION));
        } else {
            result.setDescription(complement.getAsString(Contract.FoodsUsage.DESCRIPTION));
        }
        // value:
        if (principal.getAsFloat(Contract.FoodsUsage.VALUE) != null) {
            result.setValue(principal.getAsFloat(Contract.FoodsUsage.VALUE));
        } else {
            result.setValue(complement.getAsFloat(Contract.FoodsUsage.VALUE));
        }

        return result;
    }

    @SuppressWarnings("UnusedDeclaration")
    public static FoodsUsageEntity fromContentValues(ContentValues values) {
        if (values == null) {
            throw new IllegalArgumentException("Values must be not null.");
        }

        return new FoodsUsageEntity(
                values.getAsLong(Contract.FoodsUsage._ID),
                values.getAsString(Contract.FoodsUsage.DATE_ID),
                values.getAsInteger(Contract.FoodsUsage.MEAL),
                values.getAsInteger(Contract.FoodsUsage.TIME),
                values.getAsString(Contract.FoodsUsage.DESCRIPTION),
                values.getAsFloat(Contract.FoodsUsage.VALUE));
    }

    public static final Parcelable.Creator<FoodsUsageEntity> CREATOR
            = new Parcelable.Creator<FoodsUsageEntity>() {

        public FoodsUsageEntity createFromParcel(Parcel in) {
            final Long id = in.readLong();
            final String dateId = in.readString();
            final Integer meal = in.readInt();
            final Integer time = in.readInt();
            final String description = in.readString();
            final Float value = in.readFloat();
            return new FoodsUsageEntity(id, dateId, meal, time, description, value);
        }

        public FoodsUsageEntity[] newArray(int size) {
            return new FoodsUsageEntity[size];
        }
    };
}