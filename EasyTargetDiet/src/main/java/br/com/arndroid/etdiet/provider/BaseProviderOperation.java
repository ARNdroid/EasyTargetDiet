package br.com.arndroid.etdiet.provider;

import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;

public abstract class BaseProviderOperation extends ProviderOperation {

    private final UriMatcher mUriMatcher;

    protected BaseProviderOperation() {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    }

    protected UriMatcher getUriMatcher() {
        return mUriMatcher;
    }

    public String[] defaultProjection() {
        return null;
    }

    public String defaultSelection() {
        return null;
    }

    public String defaultSortOrder() {
        return null;
    }

    @Override
    public void notify(int operation, Uri uri, Cursor cursor, Provider provider) {
        switch (operation) {
            case Provider.QUERY_OPERATION:
                cursor.setNotificationUri(provider.getContext().getContentResolver(), uri);
                break;

            case Provider.INSERT_OPERATION:
            case Provider.UPDATE_OPERATION:
            case Provider.DELETE_OPERATION:
                provider.getContext().getContentResolver().notifyChange(uri, null);
                break;

            default:
                throw new IllegalArgumentException("Unknown operation: " + operation);
        }
    }

    @Override
    public boolean continueOnConstraintViolation(int operation, Uri uri, OperationParameters parameters, SQLiteConstraintException constraintException, Provider provider) {
        return false;
    }

    protected void setDefaultParameters(OperationParameters parameters) {
        if(parameters.getProjection() == null) {
            String[] value = defaultProjection();
            if(value != null) parameters.setProjection(value);
        }
        if(parameters.getSelection() == null) {
            String value = defaultSelection();
            if(value != null) parameters.setSelection(value);
        }
        if(parameters.getSortOrder() == null) {
            String value = defaultSortOrder();
            if(value != null) parameters.setSortOrder(value);
        }
    }

}
