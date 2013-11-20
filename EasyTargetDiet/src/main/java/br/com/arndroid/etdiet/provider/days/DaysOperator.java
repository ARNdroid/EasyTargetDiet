package br.com.arndroid.etdiet.provider.days;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import br.com.arndroid.etdiet.provider.AbstractEntity;
import br.com.arndroid.etdiet.provider.BaseProviderOperator;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.OperationParameters;
import br.com.arndroid.etdiet.provider.Provider;
import br.com.arndroid.etdiet.util.UriUtil;

public class DaysOperator extends BaseProviderOperator {

    // Safe change Days.Uri: add line for a new uri.
    private static final int DAYS_URI_MATCH = 1;
    private static final int DAYS_ITEM_URI_MATCH = 2;

    public DaysOperator() {
        UriMatcher matcher =  getUriMatcher();
        // Safe change Days.Uri: add line for a new uri.
        matcher.addURI(Contract.Days.CONTENT_URI.getAuthority(),
                UriUtil.pathForUriMatcherFromUri(Contract.Days.CONTENT_URI), DAYS_URI_MATCH);
        matcher.addURI(Contract.Days.CONTENT_URI.getAuthority(),
                UriUtil.pathForUriMatcherFromUri(Contract.Days.CONTENT_URI) + "/#", DAYS_ITEM_URI_MATCH);
    }

    @Override
    public String getType(Uri uri) {
        switch (getUriMatcher().match(uri)) {
            // Safe change Days.Uri: add line for a new uri.
            case DAYS_URI_MATCH:
                return Contract.Days.CONTENT_TYPE;
            case DAYS_ITEM_URI_MATCH:
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

        // Safe change Days.Uri: evaluate line addition for new uri.
        switch (operation) {
            case QUERY_OPERATION:
                return true;
            case INSERT_OPERATION:
                return match == DAYS_URI_MATCH;
            case UPDATE_OPERATION:
                return true;
            case DELETE_OPERATION:
                return false;
            default:
                throw new IllegalArgumentException("Unknown operation: " + operation);
        }
    }

    @Override
    public void onValidateParameters(int operation, Uri uri, OperationParameters parameters,
                                     Provider provider) {

        // Safe change Days.Uri: evaluate line(s) addition for new uri.
        if (getUriMatcher().match(uri) == DAYS_ITEM_URI_MATCH) {
            parameters.setSelection(Contract.Days.ID_SELECTION);
            parameters.setSelectionArgs(new String[] {uri.getLastPathSegment()});
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values, Provider provider) {
        final Uri resultUri = super.insert(uri, values, provider);

        if(resultUri != null) {
            ContentResolver resolver = provider.getContext().getContentResolver();
            final Uri virtualUri = UriUtil.withAppendedId(Contract.Days.DATE_ID_CONTENT_URI,
                    values.getAsString(Contract.Days.DATE_ID));
            resolver.notifyChange(virtualUri, null);
        }

        return resultUri;
    }

    private static final String TAG = "==>ETD/" + DaysOperator.class.getSimpleName();
    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;    
}