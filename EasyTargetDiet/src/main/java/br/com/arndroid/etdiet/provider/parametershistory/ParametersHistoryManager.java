package br.com.arndroid.etdiet.provider.parametershistory;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.Date;

import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.util.DateUtil;

public class ParametersHistoryManager {

    private Context mContext;

    public ParametersHistoryManager(Context context) {
        mContext = context;
    }

    public float getDailyAllowanceForDate(Date referenceDate) {
        return getFloatParameter(Contract.ParametersHistory.DAILY_ALLOWANCE_PARAMETER_TYPE, referenceDate);
    }

    public void setDailyAllowance(float newValue) {
        setDailyAllowanceForDate(newValue, new Date());
    }

    public void setDailyAllowanceForDate(float newValue, Date newDate) {
        setFloatParameter(Contract.ParametersHistory.DAILY_ALLOWANCE_PARAMETER_TYPE, newValue, newDate);
    }

    public float getWeeklyAllowanceForDate(Date referenceDate) {
        return getFloatParameter(Contract.ParametersHistory.WEEKLY_ALLOWANCE_PARAMETER_TYPE, referenceDate);
    }

    public void setWeeklyAllowance(float newValue) {
        setWeeklyAllowanceForDate(newValue, new Date());
    }

    public void setWeeklyAllowanceForDate(float newValue, Date newDate) {
        setFloatParameter(Contract.ParametersHistory.WEEKLY_ALLOWANCE_PARAMETER_TYPE, newValue, newDate);
    }

    public int getTrackingWeekdayForDate(Date referenceDate) {
        return getIntParameter(Contract.ParametersHistory.TRACKING_WEEKDAY_PARAMETER_TYPE, referenceDate);
    }

    public void setTrackingWeekday(int newWeekday) {
        setTrackingWeekdayForDate(newWeekday, new Date());
    }

    public void setTrackingWeekdayForDate(int newWeekday, Date newDate) {
        setIntParameter(Contract.ParametersHistory.TRACKING_WEEKDAY_PARAMETER_TYPE, newWeekday, newDate);
    }

    public int getExerciseUseModeForDate(Date referenceDate) {
        return getIntParameter(Contract.ParametersHistory.EXERCISE_USE_MODE_PARAMETER_TYPE, referenceDate);
    }

    public void setExerciseUseMode(int newExerciseUseMode) {
        setExerciseUseModeForDate(newExerciseUseMode, new Date());
    }

    public void setExerciseUseModeForDate(int newExerciseUseMode, Date newDate) {
        setIntParameter(Contract.ParametersHistory.EXERCISE_USE_MODE_PARAMETER_TYPE, newExerciseUseMode, newDate);
    }

    public int getExerciseUseOrderForDate(Date referenceDate) {
        return getIntParameter(Contract.ParametersHistory.EXERCISE_USE_ORDER_PARAMETER_TYPE, referenceDate);
    }

    public void setExerciseUseOrder(int newUseOrder) {
        setExerciseUseOrderForDate(newUseOrder, new Date());
    }

    public void setExerciseUseOrderForDate(int newUseOrder, Date newDate) {
        setIntParameter(Contract.ParametersHistory.EXERCISE_USE_ORDER_PARAMETER_TYPE, newUseOrder, newDate);
    }

    private void setIntParameter(int parameterType, int value, Date date) {
        // If the value must be modified...
        if(getIntParameter(parameterType, date) != value) {
            ContentResolver resolver = mContext.getContentResolver();
            final String dateId = DateUtil.dateToDateId(date);
            Cursor c = null;
            try {
                c = resolver.query(Contract.ParametersHistory.CONTENT_URI, Contract.ParametersHistory.ID_PROJECTION,
                        Contract.ParametersHistory.DATE_AND_TYPE_SELECTION, new String[] {dateId, String.valueOf(parameterType)},
                        null);
                if(c.getCount() == 0) {
                    ParametersHistoryEntity entity = new ParametersHistoryEntity(null, parameterType, dateId, value, null, null);
                    resolver.insert(Contract.ParametersHistory.CONTENT_URI, entity.toContentValuesIgnoreNulls());
                } else if (c.getCount() == 1) {
                    c.moveToFirst();
                    ParametersHistoryEntity entity = new ParametersHistoryEntity(null, null, null, value, null, null);
                    resolver.update(Uri.withAppendedPath(Contract.ParametersHistory.CONTENT_URI,
                            String.valueOf(c.getLong(c.getColumnIndex(Contract.ParametersHistory._ID)))), entity.toContentValuesIgnoreNulls(),
                            null, null);
                } else {
                    throw new IllegalStateException("Found more than one parameters_history with same date for parameterType = "
                            + parameterType + " and date = " + date);
                }
            } finally {
                if(c != null) c.close();
            }
        }
    }

    private int getIntParameter(int parameterType, Date referenceDate) {
        ContentResolver resolver = mContext.getContentResolver();
        Cursor c = null;
        int result = Contract.ParametersHistory.UNDEFINED_INTEGRAL_PARAMATER_VALUE;
        try {
            c = resolver.query(Contract.ParametersHistory.CONTENT_URI,
                    Contract.ParametersHistory.INTEGRAL_PROJECTION,
                    Contract.ParametersHistory.TYPE_SELECTION,
                    new String[] {String.valueOf(parameterType)},
                    Contract.ParametersHistory.DATE_DESC_SORT_ORDER);
            if(c.getCount() > 0) {
                String referenceID = DateUtil.dateToDateId(referenceDate);
                c.moveToFirst();
                do {
                    result = c.getInt(c.getColumnIndex(Contract.ParametersHistory.INTEGRAL_NEW_VALUE));
                    if(DateUtil.dateIdStartsEqualsOrBefore(c.getString(c.getColumnIndex(Contract.ParametersHistory.DATE)), referenceID)) {
                        break;
                    }
                } while(c.moveToNext());

            } else {
                throw new IllegalStateException("Table parameters_history doesn't have records for parameterType = " + parameterType);
            }
        } finally {
            if(c != null) c.close();
        }
        return result;
    }

    private void setFloatParameter(int parameterType, float value, Date date) {
        // If the value must be modified...
        if(getFloatParameter(parameterType, date) != value) {
            ContentResolver resolver = mContext.getContentResolver();
            final String dateId = DateUtil.dateToDateId(date);
            Cursor c = null;
            try {
                c = resolver.query(Contract.ParametersHistory.CONTENT_URI, Contract.ParametersHistory.ID_PROJECTION,
                        Contract.ParametersHistory.DATE_AND_TYPE_SELECTION, new String[] {dateId, String.valueOf(parameterType)},
                        null);
                if(c.getCount() == 0) {
                    ParametersHistoryEntity entity = new ParametersHistoryEntity(null, parameterType, dateId, null, value, null);
                    resolver.insert(Contract.ParametersHistory.CONTENT_URI, entity.toContentValuesIgnoreNulls());
                } else if (c.getCount() == 1) {
                    c.moveToFirst();
                    ParametersHistoryEntity entity = new ParametersHistoryEntity(null, null, null, null, value, null);
                    resolver.update(Uri.withAppendedPath(Contract.ParametersHistory.CONTENT_URI,
                            String.valueOf(c.getLong(c.getColumnIndex(Contract.ParametersHistory._ID)))), entity.toContentValuesIgnoreNulls(),
                            null, null);
                } else {
                    throw new IllegalStateException("Found more than one parameters_history with same date for parameterType = "
                            + parameterType + " and date = " + date);
                }
            } finally {
                if(c != null) c.close();
            }
        }
    }

    private float getFloatParameter(int parameterType, Date referenceDate) {
        ContentResolver resolver = mContext.getContentResolver();
        Cursor c = null;
        float result = Contract.ParametersHistory.UNDEFINED_FLOATING_POINT_PARAMATER_VALUE;
        try {
            c = resolver.query(Contract.ParametersHistory.CONTENT_URI,
                    Contract.ParametersHistory.FLOATING_POINT_PROJECTION,
                    Contract.ParametersHistory.TYPE_SELECTION,
                    new String[] {String.valueOf(parameterType)},
                    Contract.ParametersHistory.DATE_DESC_SORT_ORDER);
            if(c.getCount() > 0) {
                String referenceID = DateUtil.dateToDateId(referenceDate);
                c.moveToFirst();
                do {
                    result = c.getInt(c.getColumnIndex(Contract.ParametersHistory.FLOATING_POINT_NEW_VALUE));
                    if(DateUtil.dateIdStartsEqualsOrBefore(c.getString(c.getColumnIndex(Contract.ParametersHistory.DATE)), referenceID)) {
                        break;
                    }
                } while(c.moveToNext());

            } else {
                throw new IllegalStateException("Table parameters_history doesn't have records for parameterType = " + parameterType);
            }
        } finally {
            if(c != null) c.close();
        }
        return result;
    }
}