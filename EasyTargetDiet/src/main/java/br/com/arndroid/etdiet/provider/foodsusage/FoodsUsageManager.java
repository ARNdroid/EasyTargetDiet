package br.com.arndroid.etdiet.provider.foodsusage;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.provider.days.DaysManager;
import br.com.arndroid.etdiet.util.DateUtil;
import br.com.arndroid.etdiet.virtualweek.UsageSummary;

public class FoodsUsageManager {

    private static final float FLOAT_ZERO = 0.0f;

    private Context mContext;

    public FoodsUsageManager(Context context) {
        mContext = context;
    }

    public UsageSummary usageSummaryForDateId(String dateId) {
        UsageSummary resultUsage = new UsageSummary();
        Cursor c = null;
        try {
            c = mContext.getContentResolver().query(
                    Contract.FoodsUsage.CONTENT_URI,
                    Contract.FoodsUsage.MEAL_AND_VALUE_PROJECTION,
                    Contract.FoodsUsage.DATE_ID_SELECTION,
                    new String[] {dateId},
                    null);

            float breakfastUsed = FLOAT_ZERO;
            float brunchUsed = FLOAT_ZERO;
            float lunchUsed = FLOAT_ZERO;
            float sneakUsed = FLOAT_ZERO;
            float dinnerUsed = FLOAT_ZERO;
            float supperUsed = FLOAT_ZERO;
            float exerciseDone = FLOAT_ZERO;

            if(c.getCount() > 0) {
                c.moveToFirst();
                do {
                    switch (c.getInt(c.getColumnIndex(Contract.FoodsUsage.MEAL))) {
                        case Meals.EXERCISE:
                            exerciseDone += c.getFloat(c.getColumnIndex(Contract.FoodsUsage.VALUE));
                            break;
                        case Meals.BREAKFAST:
                            breakfastUsed += c.getFloat(c.getColumnIndex(Contract.FoodsUsage.VALUE));
                            break;
                        case Meals.BUNCH:
                            brunchUsed += c.getFloat(c.getColumnIndex(Contract.FoodsUsage.VALUE));
                            break;
                        case Meals.LUNCH:
                            lunchUsed += c.getFloat(c.getColumnIndex(Contract.FoodsUsage.VALUE));
                            break;
                        case Meals.SNEAK:
                            sneakUsed += c.getFloat(c.getColumnIndex(Contract.FoodsUsage.VALUE));
                            break;
                        case Meals.DINNER:
                            dinnerUsed += c.getFloat(c.getColumnIndex(Contract.FoodsUsage.VALUE));
                            break;
                        case Meals.SUPPER:
                            supperUsed += c.getFloat(c.getColumnIndex(Contract.FoodsUsage.VALUE));
                            break;
                    }
                } while (c.moveToNext());
            }

            resultUsage.setExerciseDone(exerciseDone);
            resultUsage.setBreakfastUsed(breakfastUsed);
            resultUsage.setBrunchUsed(brunchUsed);
            resultUsage.setLunchUsed(lunchUsed);
            resultUsage.setSneakUsed(sneakUsed);
            resultUsage.setDinnerUsed(dinnerUsed);
            resultUsage.setSupperUsed(supperUsed);
            return resultUsage;

        } finally {
            if(c != null) c.close();
        }
    }

    public FoodsUsageEntity foodUsageFromId(Long id) {
        Cursor c = null;
        try {
            c = mContext.getContentResolver().query(ContentUris.withAppendedId(Contract.FoodsUsage.CONTENT_URI, id),
                    null, null, null, null);
            if(c.getCount() > 0) {
                c.moveToFirst();
                return FoodsUsageEntity.fromCursor(c);
            } else {
                return null;
            }
        } finally {
            if (c != null) c.close();
        }
    }

    public void refresh(FoodsUsageEntity entity) {

        entity.validateOrThrow();

        final Long id = entity.getId();
        if(id == null) {
            createDayIfNecessaryForDateId(entity.getDateId());
            final Uri resultUri = mContext.getContentResolver().insert(Contract.FoodsUsage.CONTENT_URI,
                    entity.toContentValues());
            entity.setId(Long.parseLong(resultUri.getLastPathSegment()));
        } else {
            if(isDateUpdate(entity)) {
                remove(id);
                entity.setId(null);
                refresh(entity);
            } else {
                mContext.getContentResolver().update(
                        ContentUris.withAppendedId(Contract.FoodsUsage.CONTENT_URI, id),
                        entity.toContentValues(), null, null);
            }
        }
    }

    public void remove(Long id) {
        mContext.getContentResolver().delete(ContentUris.withAppendedId(Contract.FoodsUsage.CONTENT_URI, id),
                null, null);
    }

    private boolean isDateUpdate(FoodsUsageEntity entity) {
        Cursor c = null;
        try {
            c = mContext.getContentResolver().query(ContentUris.withAppendedId(Contract.FoodsUsage.CONTENT_URI, entity.getId()),
                    null, null, null, null);
            c.moveToFirst();
            return !entity.getDateId().equals(c.getString(c.getColumnIndex(Contract.FoodsUsage.DATE_ID)));
        } finally {
            if (c != null) c.close();
        }
    }

    private void createDayIfNecessaryForDateId(String dateId) {
        final DaysManager daysManager = new DaysManager(mContext);
        DaysEntity daysEntity = daysManager.dayFromDate(DateUtil.dateIdToDate(dateId));
        if(daysEntity.getId() == null) daysManager.refresh(daysEntity);
    }
}
