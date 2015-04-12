package br.com.arndroid.etdiet.provider.weekdayparameters;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;

import br.com.arndroid.etdiet.provider.Contract;

public class WeekdayParametersManager {
    final private Context mContext;

    public WeekdayParametersManager(Context context) {
        mContext = context;
    }

    public WeekdayParametersEntity weekdayParametersFromWeekday(int weekday) {
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(Contract.WeekdayParameters.CONTENT_URI, null,
                    Contract.WeekdayParameters.ID_SELECTION, new String[] {String.valueOf(weekday)}, null);
            cursor.moveToFirst();
            return WeekdayParametersEntity.fromCursor(cursor);
        } finally {
            if(cursor != null) cursor.close();
        }
    }

    public void refresh(WeekdayParametersEntity entity) {

        entity.validateOrThrow();

        invertMealsStartAndEndTimeIfNecessary(entity);

        final Long id = entity.getId();
        if (id == null) {
            throw new IllegalStateException("WeekdayParameters doesn't allow insertions. Entity.getId() must be NOT null");
        }
        mContext.getContentResolver().update(
                ContentUris.withAppendedId(Contract.WeekdayParameters.CONTENT_URI, id),
                entity.toContentValues(), null, null);
    }

    private void invertMealsStartAndEndTimeIfNecessary(WeekdayParametersEntity entity) {
        int temp;
        if (entity.getBreakfastStartTime() > entity.getBreakfastEndTime()) {
            temp = entity.getBreakfastStartTime();
            entity.setBreakfastStartTime(entity.getBreakfastEndTime());
            entity.setBreakfastEndTime(temp);
        }
        if (entity.getBrunchStartTime() > entity.getBrunchEndTime()) {
            temp = entity.getBrunchStartTime();
            entity.setBrunchStartTime(entity.getBrunchEndTime());
            entity.setBrunchEndTime(temp);
        }
        if (entity.getLunchStartTime() > entity.getLunchEndTime()) {
            temp = entity.getLunchStartTime();
            entity.setLunchStartTime(entity.getLunchEndTime());
            entity.setLunchEndTime(temp);
        }
        if (entity.getSnackStartTime() > entity.getSnackEndTime()) {
            temp = entity.getSnackStartTime();
            entity.setSnackStartTime(entity.getSnackEndTime());
            entity.setSnackEndTime(temp);
        }
        if (entity.getDinnerStartTime() > entity.getDinnerEndTime()) {
            temp = entity.getDinnerStartTime();
            entity.setDinnerStartTime(entity.getDinnerEndTime());
            entity.setDinnerEndTime(temp);
        }
        if (entity.getSupperStartTime() > entity.getSupperEndTime()) {
            temp = entity.getSupperStartTime();
            entity.setSupperStartTime(entity.getSupperEndTime());
            entity.setSupperEndTime(temp);
        }
    }
}
