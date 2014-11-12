package br.com.arndroid.etdiet.test.utils;

import android.os.Parcel;
import android.test.AndroidTestCase;

import br.com.arndroid.etdiet.utils.ParcelUtils;

public class ParcelUtilsTest extends AndroidTestCase {

    public static final int INT_SENTINEL = -1;
    public static final long LONG_SENTINEL = -1L;
    public static final float FLOAT_SENTINEL = -1.0f;

    public void testWithNullMustReturnCorrectValues() {
        final Parcel parcel = Parcel.obtain();

        try {
            ParcelUtils.safeFromNullWriteInt(parcel, null, INT_SENTINEL);
            ParcelUtils.safeFromNullWriteLong(parcel, null, LONG_SENTINEL);
            ParcelUtils.safeFromNullWriteFloat(parcel, null, FLOAT_SENTINEL);
            parcel.setDataPosition(0);
            assertEquals(null, ParcelUtils.safeFromNullReadInt(parcel, INT_SENTINEL));
            assertEquals(null, ParcelUtils.safeFromNullReadLong(parcel, LONG_SENTINEL));
            assertEquals(null, ParcelUtils.safeFromNullReadFloat(parcel, FLOAT_SENTINEL));
        } finally {
            parcel.recycle();
        }
    }

    public void testWithNotNullMustReturnCorrectValues() {
        final int INT_VALUE = 10;
        final long LONG_VALUE = 11;
        final float FLOAT_VALUE = 12.1f;
        final Parcel parcel = Parcel.obtain();

        try {
            ParcelUtils.safeFromNullWriteInt(parcel, INT_VALUE, INT_SENTINEL);
            ParcelUtils.safeFromNullWriteLong(parcel, LONG_VALUE, LONG_SENTINEL);
            ParcelUtils.safeFromNullWriteFloat(parcel, FLOAT_VALUE, FLOAT_SENTINEL);
            parcel.setDataPosition(0);
            assertEquals(Integer.valueOf(INT_VALUE), ParcelUtils.safeFromNullReadInt(parcel, INT_SENTINEL));
            assertEquals(Long.valueOf(LONG_VALUE), ParcelUtils.safeFromNullReadLong(parcel, LONG_SENTINEL));
            assertEquals(FLOAT_VALUE, ParcelUtils.safeFromNullReadFloat(parcel, FLOAT_SENTINEL));
        } finally {
            parcel.recycle();
        }
    }
}
