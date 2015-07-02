package br.com.arndroid.etdapi.test.data;

import junit.framework.TestCase;

import java.util.Date;

import br.com.arndroid.etdapi.data.FoodsUsageData;
import br.com.arndroid.etdapi.data.FoodsUsageDataBuilder;
import br.com.arndroid.etdapi.data.Meal;

public class FoodsUsageDataBuilderTest extends TestCase {

    public void testFoodUsageDataBuilderMustReturnCorrectValues() {
        FoodsUsageData data;

        final Date now = new Date();
        data = new FoodsUsageDataBuilder()
                .withDate(now)
                .build();
        assertEquals(now, data.getDate());
        assertNull(data.getTime());
        assertNull(data.getMeal());
        assertNull(data.getDescription());
        assertNull(data.getValue());

        final Integer time = 1;
        data = new FoodsUsageDataBuilder()
                .withDate(now)
                .withTime(time)
                .build();
        assertEquals(now, data.getDate());
        assertEquals(time, data.getTime());
        assertNull(data.getMeal());
        assertNull(data.getDescription());
        assertNull(data.getValue());

        final Meal meal = Meal.BREAKFAST;
        data = new FoodsUsageDataBuilder()
                .withDate(now)
                .withTime(time)
                .withMeal(meal)
                .build();
        assertEquals(now, data.getDate());
        assertEquals(time, data.getTime());
        assertEquals(meal, data.getMeal());
        assertNull(data.getDescription());
        assertNull(data.getValue());

        final String description = "Some description.";
        data = new FoodsUsageDataBuilder()
                .withDate(now)
                .withTime(time)
                .withMeal(meal)
                .withDescription(description)
                .build();
        assertEquals(now, data.getDate());
        assertEquals(time, data.getTime());
        assertEquals(meal, data.getMeal());
        assertEquals(description, data.getDescription());
        assertNull(data.getValue());

        final Float value = 1.5f;
        data = new FoodsUsageDataBuilder()
                .withDate(now)
                .withTime(time)
                .withMeal(meal)
                .withDescription(description)
                .withValue(value)
                .build();
        assertEquals(now, data.getDate());
        assertEquals(time, data.getTime());
        assertEquals(meal, data.getMeal());
        assertEquals(description, data.getDescription());
        assertEquals(value, data.getValue());
    }

    public void testEqualsMustReturnCorrectValues() {
        final Date date = new Date();
        final Integer time = 1;
        final Meal meal = Meal.BREAKFAST;
        final String description = "Description...";
        final Float value = 2.5f;
        final FoodsUsageData dataToCompare = new FoodsUsageDataBuilder()
                .withDate(date)
                .withTime(time)
                .withMeal(meal)
                .withDescription(description)
                .withValue(value)
                .build();
        FoodsUsageData data;

        data = new FoodsUsageDataBuilder()
                .build();
        assertFalse(data.equals(dataToCompare));

        data = new FoodsUsageDataBuilder()
                .withDate(date)
                .build();
        assertFalse(data.equals(dataToCompare));

        data = new FoodsUsageDataBuilder()
                .withDate(date)
                .withTime(time)
                .build();
        assertFalse(data.equals(dataToCompare));

        data = new FoodsUsageDataBuilder()
                .withDate(date)
                .withTime(time)
                .withMeal(meal)
                .build();
        assertFalse(data.equals(dataToCompare));

        data = new FoodsUsageDataBuilder()
                .withDate(date)
                .withTime(time)
                .withMeal(meal)
                .withDescription(description)
                .build();
        assertFalse(data.equals(dataToCompare));

        data = new FoodsUsageDataBuilder()
                .withDate(date)
                .withTime(time)
                .withMeal(meal)
                .withDescription(description)
                .withValue(value)
                .build();
        assertEquals(data, dataToCompare);
    }
}