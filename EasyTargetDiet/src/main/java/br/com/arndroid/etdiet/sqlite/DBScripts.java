package br.com.arndroid.etdiet.sqlite;

import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;
import java.util.Date;

import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.parametershistory.ParametersHistoryEntity;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersEntity;
import br.com.arndroid.etdiet.utils.DateUtils;

public class DBScripts {

    public static void scriptV00ToV01(SQLiteDatabase db) {
        // Tables Creation
        db.beginTransaction();
        try {
            // Days:
            db.execSQL("CREATE TABLE " + Contract.Days.TABLE_NAME + " ("
                    + Contract.Days._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + Contract.Days.DATE_ID + " TEXT NOT NULL, "
                    + Contract.Days.ALLOWED + " REAL NOT NULL, "
                    + Contract.Days.BREAKFAST_START_TIME + " INTEGER NOT NULL, "
                    + Contract.Days.BREAKFAST_END_TIME + " INTEGER NOT NULL, "
                    + Contract.Days.BREAKFAST_GOAL + " REAL NOT NULL, "
                    + Contract.Days.BRUNCH_START_TIME + " INTEGER NOT NULL, "
                    + Contract.Days.BRUNCH_END_TIME + " INTEGER NOT NULL, "
                    + Contract.Days.BRUNCH_GOAL + " REAL NOT NULL, "
                    + Contract.Days.LUNCH_START_TIME + " INTEGER NOT NULL, "
                    + Contract.Days.LUNCH_END_TIME + " INTEGER NOT NULL, "
                    + Contract.Days.LUNCH_GOAL + " REAL NOT NULL, "
                    + Contract.Days.SNACK_START_TIME + " INTEGER NOT NULL, "
                    + Contract.Days.SNACK_END_TIME + " INTEGER NOT NULL, "
                    + Contract.Days.SNACK_GOAL + " REAL NOT NULL, "
                    + Contract.Days.DINNER_START_TIME + " INTEGER NOT NULL, "
                    + Contract.Days.DINNER_END_TIME + " INTEGER NOT NULL, "
                    + Contract.Days.DINNER_GOAL + " REAL NOT NULL, "
                    + Contract.Days.SUPPER_START_TIME + " INTEGER NOT NULL, "
                    + Contract.Days.SUPPER_END_TIME + " INTEGER NOT NULL, "
                    + Contract.Days.SUPPER_GOAL + " REAL NOT NULL, "
                    + Contract.Days.EXERCISE_GOAL + " REAL NOT NULL, "
                    + Contract.Days.LIQUID_DONE + " INTEGER NOT NULL, "
                    + Contract.Days.LIQUID_GOAL + " INTEGER NOT NULL, "
                    + Contract.Days.OIL_DONE + " INTEGER NOT NULL, "
                    + Contract.Days.OIL_GOAL + " INTEGER NOT NULL, "
                    + Contract.Days.SUPPLEMENT_DONE + " INTEGER NOT NULL, "
                    + Contract.Days.SUPPLEMENT_GOAL + " INTEGER NOT NULL, "
                    + Contract.Days.NOTE + " TEXT);");
            db.execSQL("CREATE UNIQUE INDEX " + Contract.Days.TABLE_NAME + "_"
                    + Contract.Days.DATE_ID + "_idx "
                    + "ON " + Contract.Days.TABLE_NAME + " (" + Contract.Days.DATE_ID + ");");

            // Foods Usage:
            db.execSQL("CREATE TABLE " + Contract.FoodsUsage.TABLE_NAME + " ("
                    + Contract.FoodsUsage._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + Contract.FoodsUsage.DATE_ID + " TEXT NOT NULL, "
                    + Contract.FoodsUsage.MEAL + " INTEGER NOT NULL, "
                    + Contract.FoodsUsage.TIME + " INTEGER NOT NULL, "
                    + Contract.FoodsUsage.DESCRIPTION + " TEXT NOT NULL, "
                    + Contract.FoodsUsage.VALUE + " REAL NOT NULL);");
            db.execSQL("CREATE INDEX " + Contract.FoodsUsage.TABLE_NAME + "_"
                    + Contract.FoodsUsage.DATE_ID + "_" + Contract.FoodsUsage.MEAL + "_idx "
                    + "ON " + Contract.FoodsUsage.TABLE_NAME + " (" + Contract.FoodsUsage.DATE_ID + ", " + Contract.FoodsUsage.MEAL + ");");

            // Weekday Parameters:
            db.execSQL("CREATE TABLE " + Contract.WeekdayParameters.TABLE_NAME + " ("
                    + Contract.WeekdayParameters._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + Contract.WeekdayParameters.BREAKFAST_START_TIME + " INTEGER NOT NULL, "
                    + Contract.WeekdayParameters.BREAKFAST_END_TIME + " INTEGER NOT NULL, "
                    + Contract.WeekdayParameters.BREAKFAST_GOAL + " REAL NOT NULL, "
                    + Contract.WeekdayParameters.BRUNCH_START_TIME + " INTEGER NOT NULL, "
                    + Contract.WeekdayParameters.BRUNCH_END_TIME + " INTEGER NOT NULL, "
                    + Contract.WeekdayParameters.BRUNCH_GOAL + " REAL NOT NULL, "
                    + Contract.WeekdayParameters.LUNCH_START_TIME + " INTEGER NOT NULL, "
                    + Contract.WeekdayParameters.LUNCH_END_TIME + " INTEGER NOT NULL, "
                    + Contract.WeekdayParameters.LUNCH_GOAL + " REAL NOT NULL, "
                    + Contract.WeekdayParameters.SNACK_START_TIME + " INTEGER NOT NULL, "
                    + Contract.WeekdayParameters.SNACK_END_TIME + " INTEGER NOT NULL, "
                    + Contract.WeekdayParameters.SNACK_GOAL + " REAL NOT NULL, "
                    + Contract.WeekdayParameters.DINNER_START_TIME + " INTEGER NOT NULL, "
                    + Contract.WeekdayParameters.DINNER_END_TIME + " INTEGER NOT NULL, "
                    + Contract.WeekdayParameters.DINNER_GOAL + " REAL NOT NULL, "
                    + Contract.WeekdayParameters.SUPPER_START_TIME + " INTEGER NOT NULL, "
                    + Contract.WeekdayParameters.SUPPER_END_TIME + " INTEGER NOT NULL, "
                    + Contract.WeekdayParameters.SUPPER_GOAL + " REAL NOT NULL, "
                    + Contract.WeekdayParameters.EXERCISE_GOAL + " REAL NOT NULL, "
                    + Contract.WeekdayParameters.LIQUID_GOAL + " INTEGER NOT NULL, "
                    + Contract.WeekdayParameters.OIL_GOAL + " INTEGER NOT NULL, "
                    + Contract.WeekdayParameters.SUPPLEMENT_GOAL + " INTEGER NOT NULL);");

            // Parameters History:
            db.execSQL("CREATE TABLE " + Contract.ParametersHistory.TABLE_NAME + " ("
                    + Contract.ParametersHistory._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + Contract.ParametersHistory.TYPE + " INTEGER NOT NULL, "
                    + Contract.ParametersHistory.DATE + " TEXT NOT NULL, "
                    + Contract.ParametersHistory.INTEGRAL_NEW_VALUE + " INTEGER, "
                    + Contract.ParametersHistory.FLOATING_POINT_NEW_VALUE + " REAL, "
                    + Contract.ParametersHistory.TEXT_NEW_VALUE + " TEXT);");
            db.execSQL("CREATE UNIQUE INDEX " + Contract.ParametersHistory.TABLE_NAME + "_"
                    + Contract.ParametersHistory.TYPE + "_" + Contract.ParametersHistory.DATE + "_idx "
                    + "ON " + Contract.ParametersHistory.TABLE_NAME + " (" + Contract.ParametersHistory.TYPE + ", " + Contract.ParametersHistory.DATE + ");");
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        // Data Population
        // Weekday parameters default values:
        int breakfastStartTime = 21600000; // 06:00
        int breakfastEndTime = 35940000; // 09:59
        float breakfastGoal = 3.0f;
        int brunchStartTime = 36000000; // 10:00
        int brunchEndTime = 43140000; // 11:59
        float brunchGoal =  9.0f;
        int lunchStartTime = 43200000; //12:00
        int lunchEndTime = 53940000; // 14:59
        float lunchGoal = 8.0f;
        int snackStartTime = 54000000; // 15:00
        int snackEndTime = 64740000; // 17:59
        float snackGoal = 2.0f;
        int dinnerStartTime = 64800000; // 18:00
        int dinnerEndTime = 79140000; // 21:59
        float dinnerGoal = 9.0f;
        int supperStartTime = 79200000; // 22:00
        int supperEndTime = 86340000; // 23:59
        float supperGoal = 1.0f;
        float exerciseGoal = 0.0f;
        int liquidGoal = 8;
        int oilGoal = 2;
        int supplementGoal = 0;

        WeekdayParametersEntity weekdayParametersEntity = new WeekdayParametersEntity(null,
                breakfastStartTime,
                breakfastEndTime,
                breakfastGoal,
                brunchStartTime,
                brunchEndTime,
                brunchGoal,
                lunchStartTime,
                lunchEndTime,
                lunchGoal,
                snackStartTime,
                snackEndTime,
                snackGoal,
                dinnerStartTime,
                dinnerEndTime,
                dinnerGoal,
                supperStartTime,
                supperEndTime,
                supperGoal,
                exerciseGoal,
                liquidGoal,
                oilGoal,
                supplementGoal);

        db.beginTransaction();
        try {
            weekdayParametersEntity.setId((long) Calendar.SUNDAY);
            db.insert(Contract.WeekdayParameters.TABLE_NAME, null,
                    weekdayParametersEntity.toContentValuesIgnoreNulls());

            weekdayParametersEntity.setId((long)Calendar.MONDAY);
            db.insert(Contract.WeekdayParameters.TABLE_NAME, null,
                    weekdayParametersEntity.toContentValuesIgnoreNulls());

            weekdayParametersEntity.setId((long) Calendar.TUESDAY);
            db.insert(Contract.WeekdayParameters.TABLE_NAME, null,
                    weekdayParametersEntity.toContentValuesIgnoreNulls());

            weekdayParametersEntity.setId((long) Calendar.WEDNESDAY);
            db.insert(Contract.WeekdayParameters.TABLE_NAME, null,
                    weekdayParametersEntity.toContentValuesIgnoreNulls());

            weekdayParametersEntity.setId((long)Calendar.THURSDAY);
            db.insert(Contract.WeekdayParameters.TABLE_NAME, null,
                    weekdayParametersEntity.toContentValuesIgnoreNulls());

            weekdayParametersEntity.setId((long) Calendar.FRIDAY);
            db.insert(Contract.WeekdayParameters.TABLE_NAME, null,
                    weekdayParametersEntity.toContentValuesIgnoreNulls());

            weekdayParametersEntity.setId((long) Calendar.SATURDAY);
            db.insert(Contract.WeekdayParameters.TABLE_NAME, null,
                    weekdayParametersEntity.toContentValuesIgnoreNulls());

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }
        // ParametersHistory default values:
        float dailyAllowance = 26.0f;
        float weeklyAllowance = 49.0f;
        int trackingWeekday = Calendar.SATURDAY;
        int exerciseUseMode = Contract.ParametersHistory.EXERCISE_USE_MODE_USE_DONT_ACCUMULATE;
        int exerciseUseOrder = Contract.ParametersHistory.EXERCISE_USE_ORDER_USE_EXERCISES_FIRST;

        db.beginTransaction();
        try {
            String dateId = DateUtils.dateToDateId(new Date());
            ParametersHistoryEntity parametersHistoryEntity;

            parametersHistoryEntity = new ParametersHistoryEntity(null,
                    Contract.ParametersHistory.DAILY_ALLOWANCE_PARAMETER_TYPE, dateId, null,
                    dailyAllowance, null);
            db.insert(Contract.ParametersHistory.TABLE_NAME, null,
                    parametersHistoryEntity.toContentValuesIgnoreNulls());

            parametersHistoryEntity = new ParametersHistoryEntity(null,
                    Contract.ParametersHistory.WEEKLY_ALLOWANCE_PARAMETER_TYPE, dateId, null,
                    weeklyAllowance, null);
            db.insert(Contract.ParametersHistory.TABLE_NAME, null,
                    parametersHistoryEntity.toContentValuesIgnoreNulls());

            parametersHistoryEntity = new ParametersHistoryEntity(null,
                    Contract.ParametersHistory.TRACKING_WEEKDAY_PARAMETER_TYPE, dateId,
                    trackingWeekday, null, null);
            db.insert(Contract.ParametersHistory.TABLE_NAME, null,
                    parametersHistoryEntity.toContentValuesIgnoreNulls());

            parametersHistoryEntity = new ParametersHistoryEntity(null,
                    Contract.ParametersHistory.EXERCISE_USE_MODE_PARAMETER_TYPE, dateId,
                    exerciseUseMode, null, null);
            db.insert(Contract.ParametersHistory.TABLE_NAME, null,
                    parametersHistoryEntity.toContentValuesIgnoreNulls());

            parametersHistoryEntity = new ParametersHistoryEntity(null,
                    Contract.ParametersHistory.EXERCISE_USE_ORDER_PARAMETER_TYPE, dateId,
                    exerciseUseOrder, null, null);
            db.insert(Contract.ParametersHistory.TABLE_NAME, null,
                    parametersHistoryEntity.toContentValuesIgnoreNulls());

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public static void scriptV01ToV02(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            // Weights:
            db.execSQL("CREATE TABLE " + Contract.Weights.TABLE_NAME + " ("
                    + Contract.Weights._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + Contract.Weights.DATE_ID + " TEXT NOT NULL, "
                    + Contract.Weights.TIME + " INTEGER NOT NULL, "
                    + Contract.Weights.WEIGHT + " REAL NOT NULL, "
                    + Contract.Weights.NOTE + " TEXT);");
            db.execSQL("CREATE UNIQUE INDEX " + Contract.Weights.TABLE_NAME + "_"
                    + Contract.Weights.DATE_ID + "_" + Contract.Weights.TIME + "_idx "
                    + "ON " + Contract.Weights.TABLE_NAME + " (" + Contract.Weights.DATE_ID + ", " + Contract.Weights.TIME + ");");
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
