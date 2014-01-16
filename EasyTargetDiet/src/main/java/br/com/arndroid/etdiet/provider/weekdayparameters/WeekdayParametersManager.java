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

        final Long id = entity.getId();
        if(id == null) {
            throw new IllegalStateException("WeekdayParameters doesn't allow insertions. Entity.getId() must be NOT null");
        }
        mContext.getContentResolver().update(
                ContentUris.withAppendedId(Contract.WeekdayParameters.CONTENT_URI, id),
                entity.toContentValues(), null, null);
    }
}
