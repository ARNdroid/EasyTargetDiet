package br.com.arndroid.etdiet.provider;

import android.net.Uri;
import android.provider.BaseColumns;

import java.io.Serializable;

public class Contract {

    // This utility class cannot be instantiated.
    private Contract() {}

	/*
	 * Global Definitions
	 */

    public static final String AUTHORITY = "br.com.arndroid.provider.etdiet";

    public static final int FIELD_TYPE_INTEGER = 1;
    public static final int FIELD_TYPE_LONG = 2;
    public static final int FIELD_TYPE_FLOAT = 3;
    public static final int FIELD_TYPE_STRING = 4;


	/*
	 * Implementation
	 */

    private static int fieldTypeForTableAndColumn(String tableName, String columnName) {
        if(FoodsUsage.TABLE_NAME.equals(tableName)) {
            if (FoodsUsage._ID.equals(columnName)) {
                return FIELD_TYPE_LONG;
            } else if (FoodsUsage.DAY_ID.equals(columnName)) {
                return FIELD_TYPE_STRING;
            } else if (FoodsUsage.MEAL.equals(columnName)) {
                return FIELD_TYPE_INTEGER;
            } else if (FoodsUsage.TIME.equals(columnName)) {
                return FIELD_TYPE_LONG;
            } else if (FoodsUsage.DESCRIPTION.equals(columnName)) {
                return FIELD_TYPE_STRING;
            } else if (FoodsUsage.VALUE.equals(columnName)) {
                return FIELD_TYPE_FLOAT;
            } else {
                throw new IllegalArgumentException("Unknown column " + columnName + " for table " + tableName);
            }
        } else {
            throw new IllegalArgumentException("Unknown table " + tableName);
        }
    }

	/*
	 * Exceptions
	 */

    /*
     * We use exception to error handling. We don't like but this is the providers way.
     * This class doesn't extend Exception because Providers API doesn't throw any checked exception.
     */

    public static class TargetException extends RuntimeException {

        // A descriptor for fields with problem:
        public static final class FieldDescriptor implements Serializable {

            private String mTableName;
            private String mColumnName;
            private Object mValue;

            protected FieldDescriptor(String tableName, String columnName, Object value) {
                mTableName = tableName;
                mColumnName = columnName;
                mValue = value;
            }

            public String getTableName() {
                return mTableName;
            }

            public String getColumnName() {
                return mColumnName;
            }

            public Object getValue() {
                return mValue;
            }

            private static final long serialVersionUID = 1L;
        }

        public static final int UNEXPECTED_ERROR = 0;
        public static final int NULL_VALUE = 1;
        public static final int INVALID_VALUE = 2;
        public static final int ID_UPDATED = 3;
        public static final int DUPLICATED_DATA = 4;

        private final int mErrorCode;
        private final FieldDescriptor[] mFieldDescriptorArray;
        private String mDetailMessage;

        protected TargetException(int errorCode, FieldDescriptor[] fieldDescriptorArray, Throwable cause) {
            super(cause);
            mErrorCode = errorCode;
            mFieldDescriptorArray = fieldDescriptorArray;
        }

        public int getErrorCode() {
            return mErrorCode;
        }

        public FieldDescriptor[] getFieldDescriptorArray() {
            // OK... we are returning the internal array and clients will have access
            // to our array and to fields descriptors. But we are not scared about
            // attacks here.
            return mFieldDescriptorArray;
        }

        @Override
        public String getMessage() {
            if (mDetailMessage == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Error code: ").append(getErrorCode()).append('\n');
                for (int i = 0; i < mFieldDescriptorArray.length; i++) {
                    sb.append("Field descriptor at position ").append(i).append(":\n")
                            .append("Table = '").append(mFieldDescriptorArray[i].getTableName())
                            .append("', column = '").append(mFieldDescriptorArray[i].getColumnName())
                            .append("', value = '").append(mFieldDescriptorArray[i].getValue() == null ? null : mFieldDescriptorArray[i].getValue().toString()).append("'\n");
                }
                mDetailMessage = sb.toString();
            }
            return mDetailMessage;
        }

        private static final long serialVersionUID = 1L;
    }

	/*
	 * Foods Usage Definitions
	 */

    protected interface FoodsUsageColumns {

        // Table:
        public static final String TABLE_NAME = "foods_usage";

        // Columns:
        public static final String DAY_ID = "day_id";
        public static final String MEAL = "meal";
        public static final String TIME = "time";
        public static final String DESCRIPTION = "description";
        public static final String VALUE = "value";

    }

    public static final class FoodsUsage implements BaseColumns, FoodsUsageColumns {

        // This utility class cannot be instantiated:
        private FoodsUsage() {}

		/*
		 * URI's
		 */

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

		/*
		 * MIME types
		 */

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + TABLE_NAME;

		/*
		 * Projections
		 */

        public static final String[] SIMPLE_LIST_PROJECTION = {_ID, TIME, DESCRIPTION, VALUE};

		/*
		 * Selection
		 */

        public static final String DAY_AND_MEAL_SELECTION = DAY_ID + "=? AND " + MEAL + "=?";

		/*
		 * Sort order
		 */

        public static final String DEFAULT_SORT_ORDER = TIME + " ASC";

		/*
		 * Utility methods
		 */

        public static int fieldTypeForColumn(String columnName) {
            return fieldTypeForTableAndColumn(TABLE_NAME, columnName);
        }
    }
}
