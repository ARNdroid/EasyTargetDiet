package br.com.arndroid.etdiet.provider.foodsusage;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import br.com.arndroid.etdiet.provider.BaseProviderOperator;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.OperationParameters;
import br.com.arndroid.etdiet.provider.Provider;
import br.com.arndroid.etdiet.utils.UrisUtils;

public class FoodsUsageOperator extends BaseProviderOperator {

    // Safe change FoodsUsage.Uri: add line for a new uri.
    public static final int FOODS_USAGE_URI_MATCH = 1;
    public static final int FOODS_USAGE_ITEM_URI_MATCH = 2;
    public static final int FOODS_USAGE_SUM_USAGE_URI_MATCH = 3;
    public static final int FOODS_USAGE_SUM_EXERCISE_URI_MATCH = 4;

    public FoodsUsageOperator() {
        UriMatcher matcher =  getUriMatcher();
        // Safe change FoodsUsage.Uri: add line for a new uri.
        matcher.addURI(Contract.FoodsUsage.CONTENT_URI.getAuthority(),
                UrisUtils.pathForUriMatcherFromUri(Contract.FoodsUsage.CONTENT_URI),
                FOODS_USAGE_URI_MATCH);
        matcher.addURI(Contract.FoodsUsage.CONTENT_URI.getAuthority(),
                UrisUtils.pathForUriMatcherFromUri(Contract.FoodsUsage.CONTENT_URI) + "/#",
                FOODS_USAGE_ITEM_URI_MATCH);
        matcher.addURI(Contract.FoodsUsage.SUM_USAGE_URI.getAuthority(),
                UrisUtils.pathForUriMatcherFromUri(Contract.FoodsUsage.SUM_USAGE_URI) + "/#",
                FOODS_USAGE_SUM_USAGE_URI_MATCH);
        matcher.addURI(Contract.FoodsUsage.SUM_EXERCISE_URI.getAuthority(),
                UrisUtils.pathForUriMatcherFromUri(Contract.FoodsUsage.SUM_EXERCISE_URI) + "/#",
                FOODS_USAGE_SUM_EXERCISE_URI_MATCH);
    }

    @Override
    public String getType(Uri uri) {
        switch (getUriMatcher().match(uri)) {
            // Safe change FoodsUsage.Uri: add line for a new uri.
            case FOODS_USAGE_URI_MATCH:
                return Contract.FoodsUsage.CONTENT_TYPE;
            case FOODS_USAGE_ITEM_URI_MATCH:
                return Contract.FoodsUsage.CONTENT_ITEM_TYPE;
            case FOODS_USAGE_SUM_USAGE_URI_MATCH:
                return Contract.FoodsUsage.SUM_USAGE_TYPE;
            case FOODS_USAGE_SUM_EXERCISE_URI_MATCH:
                return Contract.FoodsUsage.SUM_EXERCISE_TYPE;
            default:
                Log.w(TAG, "Unknown uri in getType(Uri): " + uri);
                return null;
        }
    }

    @Override
    public String tableName() {
        return Contract.FoodsUsage.TABLE_NAME;
    }

    @Override
    public boolean isOperationProhibitedForUri(int operation, Uri uri) {
        int match = getUriMatcher().match(uri);
        if(match == UriMatcher.NO_MATCH) throw new IllegalArgumentException("Unknown uri: " + uri);

        // Safe change FoodsUsage.Uri: add line for a new uri.
        switch (operation) {
            case QUERY_OPERATION:
                return false;
            case INSERT_OPERATION:
                return match != FOODS_USAGE_URI_MATCH;
            case UPDATE_OPERATION:
                return (match != FOODS_USAGE_URI_MATCH && match != FOODS_USAGE_ITEM_URI_MATCH);
            case DELETE_OPERATION:
                return match != FOODS_USAGE_ITEM_URI_MATCH;
            default:
                throw new IllegalArgumentException("Unknown operation: " + operation);
        }
    }

    @Override
    public void onValidateParameters(int operation, Uri uri, OperationParameters parameters,
                                     Provider provider) {

        // Safe change FoodsUsage.Uri: evaluate line(s) addition for new uri.
        switch (operation) {
            case QUERY_OPERATION:
                switch (getUriMatcher().match(uri)) {
                    case FOODS_USAGE_URI_MATCH:
                        break;
                    case FOODS_USAGE_ITEM_URI_MATCH:
                        parameters.setSelection(Contract.FoodsUsage.ID_SELECTION);
                        parameters.setSelectionArgs(new String[] {uri.getLastPathSegment()});
                        break;
                    case FOODS_USAGE_SUM_USAGE_URI_MATCH:
                        parameters.setProjection(Contract.FoodsUsage.SUM_VALUE_PROJECTION);
                        parameters.setSelection(Contract.FoodsUsage.DATE_ID_AND_NOT_EXERCISE_SELECTION);
                        parameters.setSelectionArgs(new String[] {uri.getLastPathSegment()});
                        break;
                    case FOODS_USAGE_SUM_EXERCISE_URI_MATCH:
                        parameters.setProjection(Contract.FoodsUsage.SUM_VALUE_PROJECTION);
                        parameters.setSelection(Contract.FoodsUsage.DATE_ID_AND_EXERCISE_SELECTION);
                        parameters.setSelectionArgs(new String[] {uri.getLastPathSegment()});
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown uri: " + uri);
                }
                break;

            case INSERT_OPERATION:
                break;

            case UPDATE_OPERATION:
                switch (getUriMatcher().match(uri)) {
                    case FOODS_USAGE_URI_MATCH:
                        break;
                    case FOODS_USAGE_ITEM_URI_MATCH:
                        parameters.setSelection(Contract.FoodsUsage.ID_SELECTION);
                        parameters.setSelectionArgs(new String[] {uri.getLastPathSegment()});
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown uri: " + uri);
                }
                break;

            case DELETE_OPERATION:
                switch (getUriMatcher().match(uri)) {
                    case FOODS_USAGE_URI_MATCH:
                        // We can't be here because we didn't allow delete for multiple rows.
                        // If we are here there is a bug in ..
                        throw new IllegalStateException("Invalid try to delete foods usage multiple rows");
                    case FOODS_USAGE_ITEM_URI_MATCH:
                        parameters.setSelection(Contract.FoodsUsage.ID_SELECTION);
                        parameters.setSelectionArgs(new String[] {uri.getLastPathSegment()});
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown uri: " + uri);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown operation: " + operation);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values, Provider provider) {
        final Uri resultUri = super.insert(uri, values, provider);

        if(resultUri != null) {
            final Uri extraUri = UrisUtils.withAppendedId(Contract.FoodsUsage.DATE_ID_CONTENT_URI,
                    values.getAsString(Contract.FoodsUsage.DATE_ID));
            final ContentResolver resolver = provider.getContext().getContentResolver();
            if (isLogEnabled) {
                Log.d(TAG, "insert, about to notify extraUri=" + extraUri + " and context=" + provider.getContext());
            }
            resolver.notifyChange(extraUri, null);
        }

        return resultUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs, Provider provider) {
        final int result = super.update(uri, values, selection, selectionArgs, provider);

        final ContentResolver resolver = provider.getContext().getContentResolver();

        if(result > FAIL) {
            /*
            We are making a extra notification to the **current** date_id always.
            If the field date_id is changing, only the updated value will be notified.
            Since we need both notifications in ContentObserver, the FoodsUsageOperator class
            deal with a change in date_id by a deletion of the old record and an insert of a new one
            with the updated value.
            */
            final String dateId;
            if(values.containsKey(Contract.FoodsUsage.DATE_ID)) {
                dateId = values.getAsString(Contract.FoodsUsage.DATE_ID);
            } else {
                Cursor c = null;
                try {
                    c = resolver.query(uri, null, null, null, null);
                    if(c.moveToFirst()) {
                        dateId = c.getString(c.getColumnIndex(Contract.FoodsUsage.DATE_ID));
                    } else {
                        dateId = null;
                    }
                } finally {
                    if (c != null) c.close();
                }
            }
            if(dateId != null) {
                final Uri extraUri = UrisUtils.withAppendedId(Contract.FoodsUsage.DATE_ID_CONTENT_URI,
                        dateId);
                resolver.notifyChange(extraUri, null);
            }
        }

        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs, Provider provider) {
        final ContentResolver resolver = provider.getContext().getContentResolver();
        Cursor c = null;
        Uri extraUri = null;
        try {
            c = resolver.query(uri, null, null, null, null);
            if(c.moveToFirst()) {
                extraUri = UrisUtils.withAppendedId(Contract.FoodsUsage.DATE_ID_CONTENT_URI,
                        c.getString(c.getColumnIndex(Contract.FoodsUsage.DATE_ID)));
            }
        } finally {
            if (c != null) c.close();
        }

        final int result = super.delete(uri, selection, selectionArgs, provider);

        if(result > FAIL && extraUri != null) {
            resolver.notifyChange(extraUri, null);
        }

        return result;
    }

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "==>ETD/" + FoodsUsageOperator.class.getSimpleName();
    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;
}
