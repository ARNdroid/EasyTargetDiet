package br.com.arndroid.etdiet.provider.weights;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.provider.parametershistory.ParametersHistoryManager;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersEntity;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersManager;
import br.com.arndroid.etdiet.utils.DateUtils;

public class WeightsManager {
    final private Context mContext;

    public WeightsManager(Context context) {
        mContext = context;
    }

    public WeightsEntity weightFromId(Long id) {
        Cursor c = null;
        try {
            c = mContext.getContentResolver().query(ContentUris.withAppendedId(
                    Contract.Weights.CONTENT_URI, id), null, null, null, null);
            if(c.getCount() > 0) {
                c.moveToFirst();
                return WeightsEntity.fromCursor(c);
            } else {
                return null;
            }
        } finally {
            if (c != null) c.close();
        }
    }

    public void refresh(WeightsEntity entity) {

        entity.validateOrThrow();

        final Long id = entity.getId();
        if(id == null) {
            if (isLogEnabled) {
                Log.d(TAG, "About to insert entity=" + entity + "with Uri="
                        + Contract.Weights.CONTENT_URI);
            }
            final Uri resultUri = mContext.getContentResolver().insert(Contract.Weights.CONTENT_URI,
                    entity.toContentValues());
            entity.setId(Long.parseLong(resultUri.getLastPathSegment()));
            if (isLogEnabled) {
                Log.d(TAG, "Returning from insert: entity inserted with id=" + entity.getId()
                        + " and dateId=" + entity.getDateId());
            }
        } else {
            mContext.getContentResolver().update(ContentUris.withAppendedId(Contract.Weights.CONTENT_URI,id),
                    entity.toContentValues(), null, null);
            if (isLogEnabled) {
                Log.d(TAG, "Returning from update: entity updated =" + entity);
            }
        }
    }

    public void remove(Long id) {
        mContext.getContentResolver().delete(ContentUris.withAppendedId(Contract.Weights.CONTENT_URI,
                id), null, null);
    }

    public boolean entityWillCauseConstraintViolation(WeightsEntity entity) {
        Cursor c = null;
        try {
            c = mContext.getContentResolver().query(Contract.Weights.CONTENT_URI,
                    Contract.Weights.ID_PROJECTION, Contract.Weights.DATE_ID_AND_TIME_SELECTION,
                    new String[]{entity.getDateId(), entity.getTime().toString()}, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                final WeightsEntity resultEntity = WeightsEntity.fromCursor(c);
                return !resultEntity.getId().equals(entity.getId());
            }
        } finally {
            if (c != null) c.close();
        }
        return false;
    }

    public WeightsEntity getSuggestedNewWeight() {
        final Date now = new Date();
        WeightsEntity result = new WeightsEntity(null, DateUtils.dateToDateId(now),
                DateUtils.dateToTimeAsInt(now), 70.0f, null);
        Cursor c = null;
        try {
            c = mContext.getContentResolver().query(Contract.Weights.CONTENT_URI,
                    null, null, null, Contract.Weights.DATE_AND_TIME_DESC);
            if (c.getCount() > 0) {
                c.moveToFirst();
                result.setWeight(c.getFloat(c.getColumnIndex(Contract.Weights.WEIGHT)));
            }
        } finally {
            if (c != null) c.close();
        }
        return result;
    }

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "==>ETD/" + WeightsManager.class.getSimpleName();

    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;
}