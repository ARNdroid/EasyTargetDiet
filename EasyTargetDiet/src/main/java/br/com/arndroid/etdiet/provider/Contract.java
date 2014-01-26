package br.com.arndroid.etdiet.provider;

import android.net.Uri;
import android.provider.BaseColumns;

import java.io.Serializable;

import br.com.arndroid.etdiet.meals.Meals;

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
	 * Exceptions
	 */

    /*
     * We use exception to error handling. We don't like but this is the providers way.
     * This class doesn't extend Exception because Providers API doesn't throw any checked exception.
     */

    public static class TargetException extends RuntimeException {

        // A descriptor for fields with problem:
        public static final class FieldDescriptor implements Serializable {

            final private String mTableName;
            final private String mColumnName;
            final private Object mValue;

            public FieldDescriptor(String tableName, String columnName, Object value) {
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

        public TargetException(int errorCode, FieldDescriptor[] fieldDescriptorArray, Throwable cause) {
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
	 * Days Definitions
	 */

    protected interface DaysColumns {

        // Table:
        public static final String TABLE_NAME = "days";

        // Columns:
        public static final String DATE_ID = "date_id";
        public static final String ALLOWED = "allowed";
        public static final String BREAKFAST_START_TIME = "breakfast_start_time";
        public static final String BREAKFAST_END_TIME = "breakfast_end_time";
        public static final String BREAKFAST_GOAL = "breakfast_goal";
        public static final String BRUNCH_START_TIME = "brunch_start_time";
        public static final String BRUNCH_END_TIME = "brunch_end_time";
        public static final String BRUNCH_GOAL = "brunch_goal";
        public static final String LUNCH_START_TIME = "lunch_start_time";
        public static final String LUNCH_END_TIME = "lunch_end_time";
        public static final String LUNCH_GOAL = "lunch_goal";
        public static final String SNACK_START_TIME = "snack_start_time";
        public static final String SNACK_END_TIME = "snack_end_time";
        public static final String SNACK_GOAL = "snack_goal";
        public static final String DINNER_START_TIME = "dinner_start_time";
        public static final String DINNER_END_TIME = "dinner_end_time";
        public static final String DINNER_GOAL = "dinner_goal";
        public static final String SUPPER_START_TIME = "supper_start_time";
        public static final String SUPPER_END_TIME = "supper_end_time";
        public static final String SUPPER_GOAL = "supper_goal";
        public static final String EXERCISE_GOAL = "exercise_goal";
        public static final String LIQUID_DONE = "liquid_done";
        public static final String LIQUID_GOAL = "liquid_goal";
        public static final String OIL_DONE = "oil_done";
        public static final String OIL_GOAL = "oil_goal";
        public static final String SUPPLEMENT_DONE = "supplement_done";
        public static final String SUPPLEMENT_GOAL = "supplement_goal";
        public static final String NOTE = "note";
    }

    public static final class Days implements BaseColumns, DaysColumns {

        // This utility class cannot be instantiated:
        private Days() {}

		/*
		 * URI's
		 */

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
        public static final Uri DATE_ID_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" +
                TABLE_NAME + "/date_id");

		/*
		    Projections
		 */
        public static final String[] ID_PROJECTION = {_ID};

		/*
		 * MIME types
		 */

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + TABLE_NAME;

        /*
		 * Selection
		 */

        public static final String ID_SELECTION = _ID + "=?";
        public static final String DATE_ID_SELECTION = DATE_ID + "=?";

		/*
		 * Sort order
		 */

        public static final String DATE_ID_ASC = DATE_ID + " ASC";

		/*
		 * Utility methods
		 */

        public static int fieldTypeForColumn(String columnName) {
            return fieldTypeForTableAndColumn(TABLE_NAME, columnName);
        }
    }

	/*
	 * Foods Usage Definitions
	 */

    protected interface FoodsUsageColumns {

        // Table:
        public static final String TABLE_NAME = "foods_usage";

        // Columns:
        public static final String DATE_ID = "date_id";
        public static final String MEAL = "meal";
        public static final String TIME = "time";
        public static final String DESCRIPTION = "description";
        public static final String VALUE = "value";
        public static final String SUM_VALUE = "sum_value"; // Calculated only.

    }

    public static final class FoodsUsage implements BaseColumns, FoodsUsageColumns {

        // This utility class cannot be instantiated:
        private FoodsUsage() {}

		/*
		 * URI's
		 */

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
        public static final Uri DATE_ID_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" +
                TABLE_NAME + "/date_id");
        public static final Uri SUM_USAGE_URI = Uri.parse("content://" + AUTHORITY + "/"
                + TABLE_NAME + "/sum_usage");
        public static final Uri SUM_EXERCISE_URI = Uri.parse("content://" + AUTHORITY + "/"
                + TABLE_NAME + "/sum_exercise");

		/*
		 * MIME types
		 */

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + TABLE_NAME;
        public static final String SUM_USAGE_TYPE = "float";
        public static final String SUM_EXERCISE_TYPE = "float";

		/*
		 * Projections
		 */

        public static final String[] SIMPLE_LIST_PROJECTION = {_ID, TIME, DESCRIPTION, VALUE};
        public static final String[] MEAL_AND_VALUE_PROJECTION = {MEAL, VALUE};
        public static final String[] SUM_VALUE_PROJECTION = {"sum(" + VALUE + ") as " + SUM_VALUE};

		/*
		 * Selection
		 */

        public static final String ID_SELECTION = _ID + "=?";
        public static final String DATE_ID_AND_MEAL_SELECTION = DATE_ID + "=? AND " + MEAL + "=?";
        public static final String DATE_ID_SELECTION = DATE_ID + "=?";
        public static final String DATE_ID_AND_NOT_EXERCISE_SELECTION = DATE_ID + "=? AND " + MEAL + "<>" + Meals.EXERCISE;
        public static final String DATE_ID_AND_EXERCISE_SELECTION = DATE_ID + "=? AND " + MEAL + "=" + Meals.EXERCISE;

		/*
		 * Sort order
		 */

        public static final String TIME_ASC_SORT_ORDER = TIME + " ASC";

		/*
		 * Utility methods
		 */

        public static int fieldTypeForColumn(String columnName) {
            return fieldTypeForTableAndColumn(TABLE_NAME, columnName);
        }
    }

	/*
	 * Weekday Parameters Definitions
	 */

    protected interface WeekdaysParametersColumns {

        // Table:
        public static final String TABLE_NAME = "weekday_parameters";

        // Columns:
        public static final String BREAKFAST_START_TIME = "breakfast_start_time";
        public static final String BREAKFAST_END_TIME = "breakfast_end_time";
        public static final String BREAKFAST_GOAL = "breakfast_goal";
        public static final String BRUNCH_START_TIME = "brunch_start_time";
        public static final String BRUNCH_END_TIME = "brunch_end_time";
        public static final String BRUNCH_GOAL = "brunch_goal";
        public static final String LUNCH_START_TIME = "lunch_start_time";
        public static final String LUNCH_END_TIME = "lunch_end_time";
        public static final String LUNCH_GOAL = "lunch_goal";
        public static final String SNACK_START_TIME = "snack_start_time";
        public static final String SNACK_END_TIME = "snack_end_time";
        public static final String SNACK_GOAL = "snack_goal";
        public static final String DINNER_START_TIME = "dinner_start_time";
        public static final String DINNER_END_TIME = "dinner_end_time";
        public static final String DINNER_GOAL = "dinner_goal";
        public static final String SUPPER_START_TIME = "supper_start_time";
        public static final String SUPPER_END_TIME = "supper_end_time";
        public static final String SUPPER_GOAL = "supper_goal";
        public static final String EXERCISE_GOAL = "exercise_goal";
        public static final String LIQUID_GOAL = "liquid_goal";
        public static final String OIL_GOAL = "oil_goal";
        public static final String SUPPLEMENT_GOAL = "supplement_goal";
    }

    public static final class WeekdayParameters implements BaseColumns, WeekdaysParametersColumns {

        // This utility class cannot be instantiated:
        private WeekdayParameters() {}

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
		 * Selection
		 */

        public static final String ID_SELECTION = _ID + "=?";

		/*
		 * Utility methods
		 */

        public static int fieldTypeForColumn(String columnName) {
            return fieldTypeForTableAndColumn(TABLE_NAME, columnName);
        }
    }

    /*
	 * Parameters History Definitions
	 */

    protected interface ParametersHistoryColumns {

        // Table:
        public static final String TABLE_NAME = "parameters_history";

        // Columns:
        public static final String TYPE = "type";
        public static final String DATE = "date";
        public static final String INTEGRAL_NEW_VALUE = "integral_new_value";
        public static final String FLOATING_POINT_NEW_VALUE = "floating_point_new_value";
        public static final String TEXT_NEW_VALUE = "text_new_value";

    }

    public static final class ParametersHistory implements BaseColumns, ParametersHistoryColumns {

        // This utility class cannot be instantiated:
        private ParametersHistory() {}

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
		    Type constants
		 */

        /*
            Warning!
            The value of follow constants can NOT be changed or
            we will break legacy data in DB.
         */

        public static final int DAILY_ALLOWANCE_PARAMETER_TYPE = 0;
        public static final int WEEKLY_ALLOWANCE_PARAMETER_TYPE = 1;
        public static final int TRACKING_WEEKDAY_PARAMETER_TYPE = 2;
        public static final int EXERCISE_USE_MODE_PARAMETER_TYPE = 3;
        public static final int EXERCISE_USE_ORDER_PARAMETER_TYPE = 4;

        public static final int UNDEFINED_INTEGRAL_PARAMATER_VALUE = -1;
        public static final float UNDEFINED_FLOATING_POINT_PARAMATER_VALUE = -1.0f;

        public static final int EXERCISE_USE_ORDER_USE_EXERCISES_FIRST = 0;
        public static final int EXERCISE_USE_ORDER_USE_WEEKLY_ALLOWANCE_FIRST = 1;

        public static final int EXERCISE_USE_MODE_DONT_USE = 0;
        public static final int EXERCISE_USE_MODE_USE_DONT_ACCUMULATE = 1;
        public static final int EXERCISE_USE_MODE_USE_AND_ACCUMULATE = 2;

		/*
		 * Projections
		 */

        public static final String[] ID_PROJECTION = {_ID};
        public static final String[] INTEGRAL_PROJECTION = {_ID, DATE, INTEGRAL_NEW_VALUE};
        public static final String[] FLOATING_POINT_PROJECTION = {_ID, DATE, FLOATING_POINT_NEW_VALUE};
        public static final String[] TEXT_PROJECTION = {_ID, DATE, TEXT_NEW_VALUE};
        public static final String[] ALL_COLS_PROJECTION = {_ID, TYPE, DATE, INTEGRAL_NEW_VALUE, FLOATING_POINT_NEW_VALUE, TEXT_NEW_VALUE};

		/*
		 * Selection
		 */

        public static final String ID_SELECTION = _ID + "=?";
        public static final String TYPE_SELECTION = TYPE + "=?";
        public static final String DATE_AND_TYPE_SELECTION = DATE + "=? AND " + TYPE + "=?";

		/*
		 * Sort order
		 */

        public static final String DATE_DESC_SORT_ORDER = DATE + " DESC";

		/*
		 * Utility methods
		 */

        public static int fieldTypeForColumn(String columnName) {
            return fieldTypeForTableAndColumn(TABLE_NAME, columnName);
        }
    }

    /*
	 * Weight Definitions
	 */

    protected interface WeightsColumns {

        // Table:
        public static final String TABLE_NAME = "weights";

        // Columns:
        public static final String DATE_ID = "date_id";
        public static final String TIME = "time";
        public static final String WEIGHT = "weight";
        public static final String NOTE = "note";
    }

    public static final class Weights implements BaseColumns, WeightsColumns {

        // This utility class cannot be instantiated:
        private Weights() {}

		/*
		 * URI's
		 */

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
        public static final Uri DATE_ID_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" +
                TABLE_NAME + "/date_id");

        /*
            Projections
         */
        public static final String[] ID_PROJECTION = {_ID};

		/*
		 * MIME types
		 */

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + TABLE_NAME;

        /*
		 * Selection
		 */

        public static final String ID_SELECTION = _ID + "=?";
        public static final String DATE_ID_AND_TIME_SELECTION = DATE_ID + "=? AND " + TIME + "=?";

		/*
		 * Sort order
		 */

        public static final String DATE_AND_TIME_DESC = DATE_ID + " DESC, " + TIME + " DESC";

		/*
		 * Utility methods
		 */

        public static int fieldTypeForColumn(String columnName) {
            return fieldTypeForTableAndColumn(TABLE_NAME, columnName);
        }
    }

    private static int fieldTypeForTableAndColumn(String tableName, String columnName) {
        if(FoodsUsage.TABLE_NAME.equals(tableName)) {
            if (FoodsUsage._ID.equals(columnName)) {
                return FIELD_TYPE_LONG;
            } else if (FoodsUsage.DATE_ID.equals(columnName)) {
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

        } else if(WeekdayParameters.TABLE_NAME.equals(tableName)) {
            if (WeekdayParameters._ID.equals(columnName)) {
                return FIELD_TYPE_LONG;
            } else if (WeekdayParameters.BREAKFAST_START_TIME.equals(columnName)) {
                return FIELD_TYPE_INTEGER;
            } else if (WeekdayParameters.BREAKFAST_END_TIME.equals(columnName)) {
                return FIELD_TYPE_INTEGER;
            } else if (WeekdayParameters.BREAKFAST_GOAL.equals(columnName)) {
                return FIELD_TYPE_FLOAT;
            } else if (WeekdayParameters.BRUNCH_START_TIME.equals(columnName)) {
                return FIELD_TYPE_INTEGER;
            } else if (WeekdayParameters.BRUNCH_END_TIME.equals(columnName)) {
                return FIELD_TYPE_INTEGER;
            } else if (WeekdayParameters.BRUNCH_GOAL.equals(columnName)) {
                return FIELD_TYPE_FLOAT;
            } else if (WeekdayParameters.LUNCH_START_TIME.equals(columnName)) {
                return FIELD_TYPE_INTEGER;
            } else if (WeekdayParameters.LUNCH_END_TIME.equals(columnName)) {
                return FIELD_TYPE_INTEGER;
            } else if (WeekdayParameters.LUNCH_GOAL.equals(columnName)) {
                return FIELD_TYPE_FLOAT;
            } else if (WeekdayParameters.SNACK_START_TIME.equals(columnName)) {
                return FIELD_TYPE_INTEGER;
            } else if (WeekdayParameters.SNACK_END_TIME.equals(columnName)) {
                return FIELD_TYPE_INTEGER;
            } else if (WeekdayParameters.SNACK_GOAL.equals(columnName)) {
                return FIELD_TYPE_FLOAT;
            } else if (WeekdayParameters.DINNER_START_TIME.equals(columnName)) {
                return FIELD_TYPE_INTEGER;
            } else if (WeekdayParameters.DINNER_END_TIME.equals(columnName)) {
                return FIELD_TYPE_INTEGER;
            } else if (WeekdayParameters.DINNER_GOAL.equals(columnName)) {
                return FIELD_TYPE_FLOAT;
            } else if (WeekdayParameters.SUPPER_START_TIME.equals(columnName)) {
                return FIELD_TYPE_INTEGER;
            } else if (WeekdayParameters.SUPPER_END_TIME.equals(columnName)) {
                return FIELD_TYPE_INTEGER;
            } else if (WeekdayParameters.SUPPER_GOAL.equals(columnName)) {
                return FIELD_TYPE_FLOAT;
            } else if (WeekdayParameters.EXERCISE_GOAL.equals(columnName)) {
                return FIELD_TYPE_FLOAT;
            } else if (WeekdayParameters.LIQUID_GOAL.equals(columnName)) {
                return FIELD_TYPE_INTEGER;
            } else if (WeekdayParameters.OIL_GOAL.equals(columnName)) {
                return FIELD_TYPE_INTEGER;
            } else if (WeekdayParameters.SUPPLEMENT_GOAL.equals(columnName)) {
                return FIELD_TYPE_INTEGER;
            } else {
                throw new IllegalArgumentException("Unknown column " + columnName + " for table " + tableName);
            }

        } else if (ParametersHistory.TABLE_NAME.equals(tableName)) {
            if (ParametersHistory._ID.equals(columnName)) {
                return FIELD_TYPE_LONG;
            } else if (ParametersHistory.TYPE.equals(columnName)) {
                return FIELD_TYPE_INTEGER;
            } else if (ParametersHistory.DATE.equals(columnName)) {
                return FIELD_TYPE_STRING;
            } else if (ParametersHistory.INTEGRAL_NEW_VALUE.equals(columnName)) {
                return FIELD_TYPE_INTEGER;
            } else if (ParametersHistory.FLOATING_POINT_NEW_VALUE.equals(columnName)) {
                return FIELD_TYPE_FLOAT;
            } else if (ParametersHistory.TEXT_NEW_VALUE.equals(columnName)) {
                return FIELD_TYPE_STRING;
            } else {
                throw new IllegalArgumentException("Unknown column " + columnName + " for table " + tableName);
            }

        } else if (Weights.TABLE_NAME.equals(tableName)) {
            if (Weights._ID.equals(columnName)) {
                return FIELD_TYPE_LONG;
            } else if (Weights.DATE_ID.equals(columnName)) {
                return FIELD_TYPE_STRING;
            } else if (Weights.WEIGHT.equals(columnName)) {
                return FIELD_TYPE_FLOAT;
            } else if (Weights.NOTE.equals(columnName)) {
                return FIELD_TYPE_STRING;
            } else {
                throw new IllegalArgumentException("Unknown column " + columnName + " for table " + tableName);
            }

        } else {
            throw new IllegalArgumentException("Unknown table " + tableName);
        }
    }
}
