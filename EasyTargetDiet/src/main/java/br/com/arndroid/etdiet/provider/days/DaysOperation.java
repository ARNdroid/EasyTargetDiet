package br.com.arndroid.etdiet.provider.days;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import br.com.arndroid.etdiet.provider.AbstractEntity;
import br.com.arndroid.etdiet.provider.BaseProviderOperation;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.OperationParameters;
import br.com.arndroid.etdiet.provider.Provider;
import br.com.arndroid.etdiet.util.UriUtils;

public class DaysOperation extends BaseProviderOperation {

    public DaysOperation() {
        UriMatcher matcher =  getUriMatcher();
        // Refactor: Dealing with Uri by Authority + Path.
        matcher.addURI(Contract.AUTHORITY, Contract.Days.TABLE_NAME, Provider.DAYS_URI_MATCH);
        matcher.addURI(Contract.AUTHORITY, Contract.Days.TABLE_NAME + "/#", Provider.DAYS_ITEM_URI_MATCH);
    }

    @Override
    public String getType(Uri uri) {
        switch (getUriMatcher().match(uri)) {
            case Provider.DAYS_URI_MATCH:
                return Contract.Days.CONTENT_TYPE;
            case Provider.DAYS_ITEM_URI_MATCH:
                return Contract.Days.CONTENT_ITEM_TYPE;
            default:
                Log.w(TAG, "Unknown uri in getType(Uri): " + uri);
                return null;
        }
    }

    @Override
    public String tableName() {
        return Contract.Days.TABLE_NAME;
    }

    @Override
    public boolean isOperationAllowedForUri(int operation, Uri uri) {
        int match = getUriMatcher().match(uri);
        if(match == UriMatcher.NO_MATCH) throw new IllegalArgumentException("Unknown uri: " + uri);

        switch (operation) {
            case Provider.QUERY_OPERATION:
                return true;
            case Provider.INSERT_OPERATION:
                return match == Provider.DAYS_URI_MATCH;
            case Provider.UPDATE_OPERATION:
                return true;
            case Provider.DELETE_OPERATION:
                return false;
            default:
                throw new IllegalArgumentException("Unknown operation: " + operation);
        }
    }

    @Override
    public String defaultSortOrder() {
        return Contract.Days.DATE_ID_ASC;
    }

    @Override
    public void onValidateParameters(int operation, Uri uri, OperationParameters parameters,
                                     Provider provider) {

        switch (operation) {
            case Provider.QUERY_OPERATION:
                switch (getUriMatcher().match(uri)) {
                    case Provider.DAYS_URI_MATCH:
                        setDefaultParameters(parameters);
                        break;
                    case Provider.DAYS_ITEM_URI_MATCH:
                        // TODO: the follow string must be a constant
                        parameters.setSelection(Contract.Days._ID + "=?");
                        parameters.setSelectionArgs(new String[] {uri.getLastPathSegment()});
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown uri: " + uri);
                }
                break;
            case Provider.INSERT_OPERATION:
                // We don't have default parameters for days.
                // Validation:
                final AbstractEntity entity = DaysEntity.fromContentValues(parameters.getValues());
                entity.validateOrThrow();
                break;

            case Provider.UPDATE_OPERATION:
                switch (getUriMatcher().match(uri)) {
                    case Provider.DAYS_URI_MATCH:
                        // We don't have a good strategy to validate more than one record update at same time...
                        // The app logic must assure:
                        // - The update will result in a valid value;
                        // - The id column won't be modified.
                        break;
                    case Provider.DAYS_ITEM_URI_MATCH:
                        // We have a strategy to validate one record update:
                        // 1. Get current values in DB;
                        // 2. Create a entity with the join of current values and modified values;
                        // 3. Call entity.validateOrThrow().
                        // In addition, we may verify if the user are trying to modify the id column.
                        // But we are NOT doing this here.
                        // The interface must assure:
                        // - The update will result in a valid value;
                        // - The id column won't be modified.
                        parameters.setSelection(Contract.Days._ID + "=?");
                        parameters.setSelectionArgs(new String[] {uri.getLastPathSegment()});
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown uri: " + uri);
                }
                break;

            case Provider.DELETE_OPERATION:
                switch (getUriMatcher().match(uri)) {
                    case Provider.DAYS_URI_MATCH:
                        // We can't be here because we didn't allow delete for multiple rows.
                        // If we are here there is a bug in Provider...
                        throw new IllegalStateException("Invalid try to delete days multiple rows");
                    case Provider.DAYS_ITEM_URI_MATCH:
                        // We can't be here because we didn't allow delete from this table.
                        // If we are here there is a bug in Provider...
                        throw new IllegalStateException("Invalid try to delete from days");
                    default:
                        throw new IllegalArgumentException("Unknown uri: " + uri);
                }

            default:
                throw new IllegalArgumentException("Unknown operation: " + operation);
        }
    }

    @Override
    public void doNotifyOperations(int operation, Uri uri, Cursor cursor, Provider provider) {
        super.doNotifyOperations(operation, uri, cursor, provider);
        if(operation == Provider.INSERT_OPERATION) {
            Cursor c = null;
            try {
                // TODO: aff! The following code is not working. NPE in c...

                // ContentResolver resolver = provider.getContext().getContentResolver();
                // resolver.query(uri, null, null, null, null);
                // c.moveToFirst();
                // final Uri virtualUri = UriUtils.withAppendedId(Contract.Days.DATE_ID_CONTENT_URI,
                //        c.getString(c.getColumnIndex(Contract.Days.DATE_ID)));
                // resolver.notifyChange(virtualUri, null);
            } finally {
                if(c != null) c.close();
            }
        }
    }

    private static final String TAG = "==>ETD/" + DaysOperation.class.getSimpleName();
    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;    
}
