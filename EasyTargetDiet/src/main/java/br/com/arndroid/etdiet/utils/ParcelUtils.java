package br.com.arndroid.etdiet.utils;

import android.os.Parcel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParcelUtils {

    // Utility
    protected ParcelUtils() {}

    public static void safeFromNullWriteLong(Parcel destination, Long value, long alternativeValue) {
        destination.writeLong(value == null ? alternativeValue : value);
    }

    public static Long safeFromNullReadLong(Parcel in, long nullSentinel) {
        final long valueRead = in.readLong();
        return valueRead == nullSentinel ? null : valueRead;
    }

    public static void safeFromNullWriteFloat(Parcel destination, Float value, float alternativeValue) {
        destination.writeFloat(value == null ? alternativeValue : value);
    }

    public static Float safeFromNullReadFloat(Parcel in, float nullSentinel) {
        final float valueRead = in.readFloat();
        return valueRead == nullSentinel ? null : valueRead;
    }

    public static void safeFromNullWriteInteger(Parcel destination, Integer value, int alternativeValue) {
        destination.writeInt(value == null ? alternativeValue : value);
    }

    public static Integer safeFromNullReadInteger(Parcel in, int nullSentinel) {
        final int valueRead = in.readInt();
        return valueRead == nullSentinel ? null : valueRead;
    }
}
