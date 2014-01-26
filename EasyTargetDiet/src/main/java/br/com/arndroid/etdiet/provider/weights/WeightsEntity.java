package br.com.arndroid.etdiet.provider.weights;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import br.com.arndroid.etdiet.provider.AbstractEntity;
import br.com.arndroid.etdiet.provider.Contract;

public class WeightsEntity extends AbstractEntity implements Parcelable{

    private Long id;
    private String dateId;
    private Integer time;
    private Float weight;
    private String note;

	/*
	 * Implementation
	 */

    public WeightsEntity(Long id, String dateId, Integer time, Float weight, String note) {
        this.id = id;
        this.dateId = dateId;
        this.time = time;
        this.weight = weight;
        this.note = note;
    }

    public WeightsEntity(WeightsEntity toClone) {
        id = toClone.id;
        dateId = toClone.dateId;
        time = toClone.time;
        weight = toClone.weight;
        note = toClone.note;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Contract.Weights._ID, id);
        cv.put(Contract.Weights.DATE_ID, dateId);
        cv.put(Contract.Weights.TIME, time);
        cv.put(Contract.Weights.WEIGHT, weight);
        cv.put(Contract.Weights.NOTE, note);
        return cv;
    }

    @Override
    public ContentValues toContentValuesIgnoreNulls() {
        ContentValues cv = new ContentValues();
        if (id != null) {
            cv.put(Contract.Weights._ID, id);
        }
        if (dateId != null) {
            cv.put(Contract.Weights.DATE_ID, dateId);
        }
        if (time != null) {
            cv.put(Contract.Weights.TIME, time);
        }
        if (weight != null) {
            cv.put(Contract.Weights.WEIGHT, weight);
        }
        if (note != null) {
            cv.put(Contract.Weights.NOTE, note);
        }
        return cv;
    }

    @Override
    public void validateOrThrow() {

        if (dateId == null) {
            throwNullValueException(Contract.Weights.DATE_ID);
        }
        if (time == null) {
            throwNullValueException(Contract.Weights.TIME);
        }
        if (weight == null) {
            throwNullValueException(Contract.Weights.WEIGHT);
        }

        // Note may be everything...
    }

    private void throwNullValueException(String columnName) {
        throw new Contract.TargetException(Contract.TargetException.NULL_VALUE,
                new Contract.TargetException.FieldDescriptor[] {
                        new Contract.TargetException.FieldDescriptor(Contract.Weights.TABLE_NAME,
                        columnName, null)}, null);
    }

    @Override
    public String getTableName() {
        return Contract.Weights.TABLE_NAME;
    }

    @Override
    public String getIdColumnName() {
        return Contract.Weights._ID;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;

        } else if (obj instanceof WeightsEntity) {
            WeightsEntity temp = (WeightsEntity) obj;
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

            // time:
            if (this.time != null) {
                if (!this.time.equals(temp.time))
                    return false;
            } else {
                if (temp.time != null)
                    return false;
            }

            // weight:
            if (this.weight != null) {
                if (!this.weight.equals(temp.weight))
                    return false;
            } else {
                if (temp.weight != null)
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
        result += time == null? 0: time.hashCode();
        result += weight == null? 0: weight.hashCode();
        result += note == null? 0: note.hashCode();
        return result;
    }

    @Override
    public String toString() {

        return "["
                + Contract.Weights._ID + "=" + id + ", "
                + Contract.Weights.DATE_ID + "=" + dateId + ", "
                + Contract.Weights.TIME + "=" + time + ", "
                + Contract.Weights.WEIGHT + "=" + weight + ", "
                + Contract.Weights.NOTE + "=" + note
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

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {
        destination.writeLong(id);
        destination.writeString(dateId);
        destination.writeInt(time);
        destination.writeFloat(weight);
        destination.writeString(note);
    }

    /*
     * Factories
     */
    public static WeightsEntity fromCursor(Cursor cursor) {
        if(cursor == null) {
            throw new IllegalArgumentException("Cursor must be not null.");
        }

        return new WeightsEntity(
                cursor.getColumnIndex(Contract.Weights._ID) == -1 ?
                    null : cursor.getLong(cursor.getColumnIndex(Contract.Weights._ID)),
                cursor.getColumnIndex(Contract.Weights.DATE_ID) == -1 ?
                    null : cursor.getString(cursor.getColumnIndex(Contract.Weights.DATE_ID)),
                cursor.getColumnIndex(Contract.Weights.TIME) == -1 ?
                        null : cursor.getInt(cursor.getColumnIndex(Contract.Weights.TIME)),
                cursor.getColumnIndex(Contract.Weights.WEIGHT) == -1 ?
                    null : cursor.getFloat(cursor.getColumnIndex(Contract.Weights.WEIGHT)),
                cursor.getColumnIndex(Contract.Weights.NOTE) == -1 ?
                    null : cursor.getString(cursor.getColumnIndex(Contract.Weights.NOTE)));
    }

    public static WeightsEntity fromJoinInContentValues(ContentValues principal, ContentValues complement) {
        if (principal == null || complement == null) {
            throw new IllegalArgumentException("Principal and complement must be not null.");
        }

        WeightsEntity result = new WeightsEntity(null, null, null, null, null);

        // Id:
        if (principal.getAsLong(Contract.Weights._ID) != null) {
            result.setId(principal.getAsLong(Contract.Weights._ID));
        } else {
            result.setId(complement.getAsLong(Contract.Weights._ID));
        }

        // Date Id:
        if (principal.getAsString(Contract.Weights.DATE_ID) != null) {
            result.setDateId(principal.getAsString(Contract.Weights.DATE_ID));
        } else {
            result.setDateId(complement.getAsString(Contract.Weights.DATE_ID));
        }

        // Time:
        if (principal.getAsInteger(Contract.Weights.TIME) != null) {
            result.setTime(principal.getAsInteger(Contract.Weights.TIME));
        } else {
            result.setTime(complement.getAsInteger(Contract.Weights.TIME));
        }

        // Weight:
        if (principal.getAsFloat(Contract.Weights.WEIGHT) != null) {
            result.setWeight(principal.getAsFloat(Contract.Weights.WEIGHT));
        } else {
            result.setWeight(complement.getAsFloat(Contract.Weights.WEIGHT));
        }

        // Note:
        if (principal.getAsString(Contract.Weights.NOTE) != null) {
            result.setNote(principal.getAsString(Contract.Weights.NOTE));
        } else {
            result.setNote(complement.getAsString(Contract.Weights.NOTE));
        }

        return result;
    }

    public static WeightsEntity fromContentValues(ContentValues values) {
        if (values == null) {
            throw new IllegalArgumentException("Values must be not null.");
        }

        return new WeightsEntity(
                values.getAsLong(Contract.Weights._ID),
                values.getAsString(Contract.Weights.DATE_ID),
                values.getAsInteger(Contract.Weights.TIME),
                values.getAsFloat(Contract.Weights.WEIGHT),
                values.getAsString(Contract.Weights.NOTE));
    }

    public static final Parcelable.Creator<WeightsEntity> CREATOR
            = new Parcelable.Creator<WeightsEntity>() {

        public WeightsEntity createFromParcel(Parcel in) {
            final Long id = in.readLong();
            final String dateId = in.readString();
            final Integer time = in.readInt();
            final Float weight = in.readFloat();
            final String note = in.readString();
            return new WeightsEntity(id, dateId, time, weight, note);
        }

        public WeightsEntity[] newArray(int size) {
            return new WeightsEntity[size];
        }
    };
}