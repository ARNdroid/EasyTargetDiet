package br.com.arndroid.etdapi.comm;

import android.content.Intent;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import br.com.arndroid.etdapi.BuildConfig;
import br.com.arndroid.etdapi.data.FoodsUsageData;

public final class ETDClient {

    // MessageId:
    /*
       Mind the gap:
       These int values CANNOT change because the client api and the server (ETD App) api
       may be NOT equal versions.
       When you add a new int constant DON'T FORGET to list it inside the @IntDef annotation.
    */
    @IntDef({MESSAGE_ID_FOOD_USAGE_SINGLE_V01, MESSAGE_ID_FOOD_USAGE_BULK_V01})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MessageId {}
    public static final int MESSAGE_ID_FOOD_USAGE_SINGLE_V01 = 0;
    public static final int MESSAGE_ID_FOOD_USAGE_BULK_V01 = 1;

    // Categories:
    public static final String CATEGORY_FOOD_USAGE = "br.com.arndroid.etdapi.comm.FOOD_USAGE";

    // Extra keys:
    public static final String EXTRA_KEY_MESSAGE_ID = "br.com.arndroid.etdapi.comm.EXTRA_KEY_MESSAGE_ID";
    public static final String EXTRA_KEY_VERSION = "br.com.arndroid.etdapi.comm.EXTRA_KEY_VERSION";
    public static final String EXTRA_KEY_DATA_BUNDLE = "br.com.arndroid.etdapi.comm.EXTRA_KEY_DATA_BUNDLE";

    // Utility:
    private ETDClient() {}

    public static Intent createMessage(@NonNull FoodsUsageData data) {
        if (data == null) {
            throw new IllegalArgumentException("IntentHelper.createMessage: data cannot be null.");
        }
        final Intent result = prepareNewIntent(Intent.ACTION_SEND, CATEGORY_FOOD_USAGE, MESSAGE_ID_FOOD_USAGE_SINGLE_V01,
                BuildConfig.VERSION_CODE);
        result.putExtra(EXTRA_KEY_DATA_BUNDLE, data.toBundle());
        return result;
    }

    private static Intent prepareNewIntent(final String action, final String category,
                                           final @MessageId int messageId, int versionCode) {
        final Intent result = new Intent(action);
        result.addCategory(category);
        result.putExtra(EXTRA_KEY_MESSAGE_ID, messageId);
        result.putExtra(EXTRA_KEY_VERSION, versionCode);
        return result;
    }
}