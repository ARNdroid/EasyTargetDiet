package br.com.arndroid.etdiet.test.provider.foodsusage;

import android.database.Cursor;
import android.test.ProviderTestCase2;

import br.com.arndroid.etdapi.data.Meal;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.Provider;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.utils.UrisUtils;

public class FoodsUsageOperationTest extends ProviderTestCase2<Provider> {

    public FoodsUsageOperationTest() {
        super(Provider.class, Contract.AUTHORITY);
    }

    public void testFoodsUsageSumWithoutUsageMustReturnZero() {
        Cursor c = null;
        try {
            String dateId = "20130918";
            c = getProvider().query(UrisUtils.withAppendedId(Contract.FoodsUsage.SUM_USAGE_URI,
                    dateId), null, null, null, null);
            c.moveToFirst();
            float total = c.getLong(c.getColumnIndex(Contract.FoodsUsage.SUM_VALUE));
            assertEquals(0.0f, total);

        } finally {
            if(c != null) c.close();
        }
    }

    public void testFoodsUsageSumWithUsageMustReturnCorrectValue() {
        Cursor c = null;
        String dateId = "20130918";
        FoodsUsageEntity entity;

        try {
            entity = new FoodsUsageEntity(null, dateId, Meal.BREAKFAST, 0, "none",
                    0.1f);
            getProvider().insert(Contract.FoodsUsage.CONTENT_URI, entity.toContentValuesIgnoreNulls());
            c = getProvider().query(UrisUtils.withAppendedId(Contract.FoodsUsage.SUM_USAGE_URI,
                    dateId), null, null, null, null);
            c.moveToFirst();
            float total = c.getFloat(c.getColumnIndex(Contract.FoodsUsage.SUM_VALUE));
            assertEquals(0.1f, total);
        } finally {
            if(c != null) c.close();
        }


        try {
            entity = new FoodsUsageEntity(null, dateId, Meal.BRUNCH, 0, "none", 0.2f);
            getProvider().insert(Contract.FoodsUsage.CONTENT_URI, entity.toContentValuesIgnoreNulls());
            c = getProvider().query(UrisUtils.withAppendedId(Contract.FoodsUsage.SUM_USAGE_URI,
                    dateId), null, null, null, null);
            c.moveToFirst();
            float total = c.getFloat(c.getColumnIndex(Contract.FoodsUsage.SUM_VALUE));
            assertEquals(0.3f, total);
        } finally {
            if(c != null) c.close();
        }

        try {
            entity = new FoodsUsageEntity(null, dateId, Meal.DINNER, 0, "none", 0.3f);
            getProvider().insert(Contract.FoodsUsage.CONTENT_URI, entity.toContentValuesIgnoreNulls());
            c = getProvider().query(UrisUtils.withAppendedId(Contract.FoodsUsage.SUM_USAGE_URI,
                    dateId), null, null, null, null);
            c.moveToFirst();
            float total = c.getFloat(c.getColumnIndex(Contract.FoodsUsage.SUM_VALUE));
            assertEquals(0.6f, total);
        } finally {
            if(c != null) c.close();
        }

        try {
            entity = new FoodsUsageEntity(null, dateId, Meal.EXERCISE, 0, "none", 888.8f);
            getProvider().insert(Contract.FoodsUsage.CONTENT_URI, entity.toContentValuesIgnoreNulls());
            c = getProvider().query(UrisUtils.withAppendedId(Contract.FoodsUsage.SUM_USAGE_URI,
                    dateId), null, null, null, null);
            c.moveToFirst();
            float total = c.getFloat(c.getColumnIndex(Contract.FoodsUsage.SUM_VALUE));
            assertEquals(0.6f, total);
        } finally {
            if(c != null) c.close();
        }

        try {
            entity = new FoodsUsageEntity(null, "20130919", Meal.BRUNCH, 0, "none", 999.9f);
            getProvider().insert(Contract.FoodsUsage.CONTENT_URI, entity.toContentValuesIgnoreNulls());
            c = getProvider().query(UrisUtils.withAppendedId(Contract.FoodsUsage.SUM_USAGE_URI,
                    dateId), null, null, null, null);
            c.moveToFirst();
            float total = c.getFloat(c.getColumnIndex(Contract.FoodsUsage.SUM_VALUE));
            assertEquals(0.6f, total);
        } finally {
            if(c != null) c.close();
        }
    }

    public void testExerciseSumWithoutExerciseMustReturnZero() {
        Cursor c = null;
        try {
            String dateId = "20130918";
            c = getProvider().query(UrisUtils.withAppendedId(Contract.FoodsUsage.SUM_EXERCISE_URI,
                    dateId), null, null, null, null);
            c.moveToFirst();
            float total = c.getLong(c.getColumnIndex(Contract.FoodsUsage.SUM_VALUE));
            assertEquals(0.0f, total);

        } finally {
            if(c != null) c.close();
        }
    }

    public void testExerciseSumWithExerciseMustReturnCorrectValue() {
        Cursor c = null;
        String dateId = "20130918";
        FoodsUsageEntity entity;

        try {
            entity = new FoodsUsageEntity(null, dateId, Meal.EXERCISE, 0, "none",
                    0.1f);
            getProvider().insert(Contract.FoodsUsage.CONTENT_URI, entity.toContentValuesIgnoreNulls());
            c = getProvider().query(UrisUtils.withAppendedId(Contract.FoodsUsage.SUM_EXERCISE_URI,
                    dateId), null, null, null, null);
            c.moveToFirst();
            float total = c.getFloat(c.getColumnIndex(Contract.FoodsUsage.SUM_VALUE));
            assertEquals(0.1f, total);
        } finally {
            if(c != null) c.close();
        }


        try {
            entity = new FoodsUsageEntity(null, dateId, Meal.EXERCISE, 0, "none", 0.2f);
            getProvider().insert(Contract.FoodsUsage.CONTENT_URI, entity.toContentValuesIgnoreNulls());
            c = getProvider().query(UrisUtils.withAppendedId(Contract.FoodsUsage.SUM_EXERCISE_URI,
                    dateId), null, null, null, null);
            c.moveToFirst();
            float total = c.getFloat(c.getColumnIndex(Contract.FoodsUsage.SUM_VALUE));
            assertEquals(0.3f, total);
        } finally {
            if(c != null) c.close();
        }

        try {
            entity = new FoodsUsageEntity(null, dateId, Meal.EXERCISE, 0, "none", 0.3f);
            getProvider().insert(Contract.FoodsUsage.CONTENT_URI, entity.toContentValuesIgnoreNulls());
            c = getProvider().query(UrisUtils.withAppendedId(Contract.FoodsUsage.SUM_EXERCISE_URI,
                    dateId), null, null, null, null);
            c.moveToFirst();
            float total = c.getFloat(c.getColumnIndex(Contract.FoodsUsage.SUM_VALUE));
            assertEquals(0.6f, total);
        } finally {
            if(c != null) c.close();
        }

        try {
            entity = new FoodsUsageEntity(null, dateId, Meal.BREAKFAST, 0, "none", 888.8f);
            getProvider().insert(Contract.FoodsUsage.CONTENT_URI, entity.toContentValuesIgnoreNulls());
            c = getProvider().query(UrisUtils.withAppendedId(Contract.FoodsUsage.SUM_EXERCISE_URI,
                    dateId), null, null, null, null);
            c.moveToFirst();
            float total = c.getFloat(c.getColumnIndex(Contract.FoodsUsage.SUM_VALUE));
            assertEquals(0.6f, total);
        } finally {
            if(c != null) c.close();
        }

        try {
            entity = new FoodsUsageEntity(null, "20130919", Meal.EXERCISE, 0, "none", 999.9f);
            getProvider().insert(Contract.FoodsUsage.CONTENT_URI, entity.toContentValuesIgnoreNulls());
            c = getProvider().query(UrisUtils.withAppendedId(Contract.FoodsUsage.SUM_EXERCISE_URI,
                    dateId), null, null, null, null);
            c.moveToFirst();
            float total = c.getFloat(c.getColumnIndex(Contract.FoodsUsage.SUM_VALUE));
            assertEquals(0.6f, total);
        } finally {
            if(c != null) c.close();
        }
    }
}
