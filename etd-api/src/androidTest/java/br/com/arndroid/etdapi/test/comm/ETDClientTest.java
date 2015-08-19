package br.com.arndroid.etdapi.test.comm;

import android.content.Intent;
import android.os.Bundle;

import junit.framework.TestCase;

import java.util.Date;

import br.com.arndroid.etdapi.comm.ETDClient;
import br.com.arndroid.etdapi.data.FoodsUsageData;
import br.com.arndroid.etdapi.data.FoodsUsageDataBuilder;
import br.com.arndroid.etdapi.data.Meal;

public class ETDClientTest extends TestCase {

    public void testNullDataMustThrow() {
        try {
            ETDClient.createMessage(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    public void testValidDataMustReturnCorrectValues() {
        final FoodsUsageData data = new FoodsUsageDataBuilder()
                .withDate(new Date())
                .withTime(10)
                .withMeal(Meal.BREAKFAST)
                .withValue(9.99f)
                .withDescription("Some description")
                .build();

        final Intent intent = ETDClient.createMessage(data);
        final Bundle bundle = intent.getBundleExtra(ETDClient.EXTRA_KEY_DATA_BUNDLE);
        final FoodsUsageData dataToCompare = FoodsUsageData.fromBundle(bundle);
        assertEquals(dataToCompare, data);
    }
}