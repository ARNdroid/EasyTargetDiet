package br.com.arndroid.etdiet.provider.foodsusage;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import br.com.arndroid.etdiet.provider.AbstractEntity;
import br.com.arndroid.etdiet.provider.BaseProviderOperation;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.OperationParameters;
import br.com.arndroid.etdiet.provider.Provider;
import br.com.arndroid.etdiet.provider.parametershistory.ParametersHistoryEntity;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersEntity;

public class FoodsUsageOperation extends BaseProviderOperation {

    public FoodsUsageOperation() {
        UriMatcher matcher =  getUriMatcher();
        matcher.addURI(Contract.AUTHORITY, Contract.FoodsUsage.TABLE_NAME, Provider.FOODS_USAGE_URI_MATCH);
        matcher.addURI(Contract.AUTHORITY, Contract.FoodsUsage.TABLE_NAME + "/#", Provider.FOODS_USAGE_ITEM_URI_MATCH);
    }

    @Override
    public String getType(Uri uri) {
        switch (getUriMatcher().match(uri)) {
            case Provider.FOODS_USAGE_URI_MATCH:
                return Contract.FoodsUsage.CONTENT_TYPE;
            case Provider.FOODS_USAGE_ITEM_URI_MATCH:
                return Contract.FoodsUsage.CONTENT_ITEM_TYPE;
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
    public boolean isOperationAllowedForUri(int operation, Uri uri) {
        int match = getUriMatcher().match(uri);
        if(match == UriMatcher.NO_MATCH) throw new IllegalArgumentException("Unknown uri: " + uri);

        switch (operation) {
            case Provider.QUERY_OPERATION:
                return true;
            case Provider.INSERT_OPERATION:
                return match == Provider.FOODS_USAGE_URI_MATCH;
            case Provider.UPDATE_OPERATION:
                return true;
            case Provider.DELETE_OPERATION:
                return match == Provider.FOODS_USAGE_ITEM_URI_MATCH;
            default:
                throw new IllegalArgumentException("Unknown operation: " + operation);
        }
    }

    @Override
    public String[] defaultProjection() {
        return Contract.FoodsUsage.SIMPLE_LIST_PROJECTION;
    }

    @Override
    public String defaultSelection() {
        return Contract.FoodsUsage.DAY_AND_MEAL_SELECTION;
    }

    @Override
    public String defaultSortOrder() {
        return Contract.FoodsUsage.TIME_ASC_SORT_ORDER;
    }

    @Override
    public void onValidateParameters(int operation, Uri uri, OperationParameters parameters,
                                     Provider provider) {

        switch (operation) {
            case Provider.QUERY_OPERATION:
                switch (getUriMatcher().match(uri)) {
                    case Provider.FOODS_USAGE_URI_MATCH:
                        setDefaultParameters(parameters);
                        break;
                    case Provider.FOODS_USAGE_ITEM_URI_MATCH:
                        parameters.setSelection(Contract.FoodsUsage._ID + "=?");
                        parameters.setSelectionArgs(new String[] {uri.getLastPathSegment()});
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown uri: " + uri);
                }
                break;
            case Provider.INSERT_OPERATION:
                // We don't have default parameters for food usage.
                // Validation:
                final AbstractEntity entity = FoodsUsageEntity.fromContentValues(parameters.getValues());
                entity.validateOrThrow();
                break;

            case Provider.UPDATE_OPERATION:
                switch (getUriMatcher().match(uri)) {
                    case Provider.FOODS_USAGE_URI_MATCH:
                        // We don't have a good strategy to validate more than one record update at same time...
                        // The app logic must assure:
                        // - The update will result in a valid value;
                        // - The id column won't be modified.
                        break;
                    case Provider.FOODS_USAGE_ITEM_URI_MATCH:
                        // We have a strategy to validate one record update:
                        // 1. Get current values in DB;
                        // 2. Create a entity with the join of current values and modified values;
                        // 3. Call entity.validateOrThrow().
                        // In addition, we may verify if the user are trying to modify the id column.
                        // But we are NOT doing this here.
                        // The interface must assure:
                        // - The update will result in a valid value;
                        // - The id column won't be modified.
                        parameters.setSelection(Contract.FoodsUsage._ID + "=?");
                        parameters.setSelectionArgs(new String[] {uri.getLastPathSegment()});
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown uri: " + uri);
                }

            case Provider.DELETE_OPERATION:
                switch (getUriMatcher().match(uri)) {
                    case Provider.FOODS_USAGE_URI_MATCH:
                        // We can't be here because we didn't allow delete for multiple rows.
                        // If we are here there is a bug in Provider...
                        throw new IllegalStateException("Invalid try to delete foods usage multiple rows");
                    case Provider.FOODS_USAGE_ITEM_URI_MATCH:
                        parameters.setSelection(Contract.FoodsUsage._ID + "=?");
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

    private static final String TAG = "==>ETD/" + FoodsUsageOperation.class.getSimpleName();
    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;
}
