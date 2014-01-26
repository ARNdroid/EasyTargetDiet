package br.com.arndroid.etdiet.provider.weights;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.net.Uri;
import android.util.Log;

import br.com.arndroid.etdiet.provider.BaseProviderOperator;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.OperationParameters;
import br.com.arndroid.etdiet.provider.Provider;
import br.com.arndroid.etdiet.utils.UrisUtils;

public class WeightsOperator extends BaseProviderOperator {

    // Safe change Weights.Uri: add line for a new uri.
    private static final int WEIGHTS_URI_MATCH = 1;
    private static final int WEIGHTS_ITEM_URI_MATCH = 2;

    public WeightsOperator() {
        UriMatcher matcher =  getUriMatcher();
        // Safe change Weights.Uri: add line for a new uri.
        matcher.addURI(Contract.Weights.CONTENT_URI.getAuthority(),
                UrisUtils.pathForUriMatcherFromUri(Contract.Weights.CONTENT_URI), WEIGHTS_URI_MATCH);
        matcher.addURI(Contract.Weights.CONTENT_URI.getAuthority(),
                UrisUtils.pathForUriMatcherFromUri(Contract.Weights.CONTENT_URI) + "/#", 
                WEIGHTS_ITEM_URI_MATCH);
    }

    @Override
    public String getType(Uri uri) {
        switch (getUriMatcher().match(uri)) {
            // Safe change Weights.Uri: add line for a new uri.
            case WEIGHTS_URI_MATCH:
                return Contract.Weights.CONTENT_TYPE;
            case WEIGHTS_ITEM_URI_MATCH:
                return Contract.Weights.CONTENT_ITEM_TYPE;
            default:
                Log.w(TAG, "Unknown uri in getType(Uri): " + uri);
                return null;
        }
    }

    @Override
    public String tableName() {
        return Contract.Weights.TABLE_NAME;
    }

    @Override
    public boolean isOperationProhibitedForUri(int operation, Uri uri) {
        int match = getUriMatcher().match(uri);
        if(match == UriMatcher.NO_MATCH) throw new IllegalArgumentException("Unknown uri: " + uri);

        // Safe change Weights.Uri: evaluate line addition for new uri.
        switch (operation) {
            case QUERY_OPERATION:
                return false;
            case INSERT_OPERATION:
                return match != WEIGHTS_URI_MATCH;
            case UPDATE_OPERATION:
                return false;
            case DELETE_OPERATION:
                return false;
            default:
                throw new IllegalArgumentException("Unknown operation: " + operation);
        }
    }

    @Override
    public void onValidateParameters(int operation, Uri uri, OperationParameters parameters,
                                     Provider provider) {

        // Safe change Weights.Uri: evaluate line(s) addition for new uri.
        if (getUriMatcher().match(uri) == WEIGHTS_ITEM_URI_MATCH) {
            parameters.setSelection(Contract.Weights.ID_SELECTION);
            parameters.setSelectionArgs(new String[] {uri.getLastPathSegment()});
        }
    }

    private static final String TAG = "==>ETD/" + WeightsOperator.class.getSimpleName();
    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;    
}