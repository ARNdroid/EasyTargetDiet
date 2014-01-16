package br.com.arndroid.etdiet.provider.weekdayparameters;

import android.content.UriMatcher;
import android.net.Uri;
import android.util.Log;

import br.com.arndroid.etdiet.provider.BaseProviderOperator;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.OperationParameters;
import br.com.arndroid.etdiet.provider.Provider;
import br.com.arndroid.etdiet.utils.UrisUtils;

public class WeekdayParametersOperator extends BaseProviderOperator {

    // Safe change WeekdayParameters.Uri: add line for a new uri.
    public static final int WEEKDAY_PARAMETERS_URI_MATCH = 1;
    public static final int WEEKDAY_PARAMETERS_ITEM_URI_MATCH = 2;

    public WeekdayParametersOperator() {
        UriMatcher matcher =  getUriMatcher();
        // Safe change WeekdayParameters.Uri: add line for a new uri.
        matcher.addURI(Contract.WeekdayParameters.CONTENT_URI.getAuthority(),
                UrisUtils.pathForUriMatcherFromUri(Contract.WeekdayParameters.CONTENT_URI),
                WEEKDAY_PARAMETERS_URI_MATCH);
        matcher.addURI(Contract.WeekdayParameters.CONTENT_URI.getAuthority(),
                UrisUtils.pathForUriMatcherFromUri(Contract.WeekdayParameters.CONTENT_URI) + "/#",
                WEEKDAY_PARAMETERS_ITEM_URI_MATCH);
    }

    @Override
    public String getType(Uri uri) {
        switch (getUriMatcher().match(uri)) {
            // Safe change WeekdayParameters.Uri: add line for a new uri.
            case WEEKDAY_PARAMETERS_URI_MATCH:
                return Contract.WeekdayParameters.CONTENT_TYPE;
            case WEEKDAY_PARAMETERS_ITEM_URI_MATCH:
                return Contract.WeekdayParameters.CONTENT_ITEM_TYPE;
            default:
                Log.w(TAG, "Unknown uri in getType(Uri): " + uri);
                return null;
        }
    }

    @Override
    public String tableName() {
        return Contract.WeekdayParameters.TABLE_NAME;
    }

    @Override
    public boolean isOperationProhibitedForUri(int operation, Uri uri) {
        int match = getUriMatcher().match(uri);
        if(match == UriMatcher.NO_MATCH) throw new IllegalArgumentException("Unknown uri: " + uri);

        // Safe change WeekdayParameters.Uri: evaluate line addition for new uri.
        switch (operation) {
            case QUERY_OPERATION:
                return false;
            case INSERT_OPERATION:
                return true;
            case UPDATE_OPERATION:
                return false;
            case DELETE_OPERATION:
                return true;
            default:
                throw new IllegalArgumentException("Unknown operation: " + operation);
        }
    }

    @Override
    public void onValidateParameters(int operation, Uri uri, OperationParameters parameters,
                                     Provider provider) {

        // Safe change Days.Uri: evaluate line(s) addition for new uri.
        if(getUriMatcher().match(uri) == WEEKDAY_PARAMETERS_ITEM_URI_MATCH) {
            parameters.setSelection(Contract.WeekdayParameters.ID_SELECTION);
            parameters.setSelectionArgs(new String[] {uri.getLastPathSegment()});
        }
    }

    private static final String TAG = "==>ETD/" + WeekdayParametersOperator.class.getSimpleName();
    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;

}
