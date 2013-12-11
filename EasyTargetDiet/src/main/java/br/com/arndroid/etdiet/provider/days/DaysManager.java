package br.com.arndroid.etdiet.provider.days;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.parametershistory.ParametersHistoryManager;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersEntity;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersManager;
import br.com.arndroid.etdiet.util.DateUtil;

public class DaysManager {
    final private Context mContext;

    public DaysManager(Context context) {
        mContext = context;
    }

    public DaysEntity dayFromDate(Date date) {
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(Contract.Days.CONTENT_URI, null,
                    Contract.Days.DATE_ID_SELECTION, new String[] {DateUtil.dateToDateId(date)}, null);
            if(cursor.getCount() == 0) {
                return buildMemoryDayForDate(date);
            } else {
                cursor.moveToFirst();
                return DaysEntity.fromCursor(cursor);
            }
        } finally {
            if(cursor != null) cursor.close();
        }
    }

    private DaysEntity buildMemoryDayForDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        WeekdayParametersEntity parameters = new
                WeekdayParametersManager(mContext).weekdayParametersFromWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        return new DaysEntity(null,
                DateUtil.dateToDateId(date),
                new ParametersHistoryManager(mContext).getDailyAllowanceForDate(date),
                parameters.getBreakfastStartTime(), parameters.getBreakfastEndTime(), parameters.getBreakfastGoal(),
                parameters.getBrunchStartTime(), parameters.getBrunchEndTime(), parameters.getBrunchGoal(),
                parameters.getLunchStartTime(), parameters.getLunchEndTime(), parameters.getLunchGoal(),
                parameters.getSnackStartTime(), parameters.getSnackEndTime(), parameters.getSnackGoal(),
                parameters.getDinnerStartTime(), parameters.getDinnerEndTime(), parameters.getDinnerGoal(),
                parameters.getSupperStartTime(), parameters.getSupperEndTime(), parameters.getSupperGoal(),
                parameters.getExerciseGoal(),
                0, parameters.getLiquidGoal(),
                0, parameters.getOilGoal(),
                0, parameters.getSupplementGoal(),
                null);

    }

    public void refresh(DaysEntity entity) {

        entity.validateOrThrow();

        final Long id = entity.getId();
        if(id == null) {
            if (isLogEnabled) {
                Log.d(TAG, "About to insert entity=" + entity + "with Uri=" + Contract.Days.CONTENT_URI);
            }
            final Uri resultUri = mContext.getContentResolver().insert(Contract.Days.CONTENT_URI,
                    entity.toContentValues());
            entity.setId(Long.parseLong(resultUri.getLastPathSegment()));
            if (isLogEnabled) {
                Log.d(TAG, "Returning from insert: entity inserted with id=" + entity.getId() + " and dateId=" + entity.getDateId());
            }
        } else {
            mContext.getContentResolver().update(ContentUris.withAppendedId(Contract.Days.CONTENT_URI,id),
                    entity.toContentValues(), null, null);
            if (isLogEnabled) {
                Log.d(TAG, "Returning from update: entity updated =" + entity);
            }
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "==>ETD/" + DaysManager.class.getSimpleName();
    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;
}
