package br.com.arndroid.etdiet.provider;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public abstract class BaseProviderOperator implements ProviderOperator {

    public static final int QUERY_OPERATION = 0;
    public static final int INSERT_OPERATION = 1;
    public static final int UPDATE_OPERATION = 2;
    public static final int DELETE_OPERATION = 3;
    private final UriMatcher mUriMatcher;

    public static final int FAIL = 0;

    public BaseProviderOperator() {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    }

    @Override
    public boolean handleUri(Uri uri) {
        return getUriMatcher().match(uri) != UriMatcher.NO_MATCH;
    }

    @Override
    public abstract String getType(Uri uri);

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder, Provider provider) {
        if(!isOperationAllowedForUri(QUERY_OPERATION, uri)) {
            return null;
        }

        OperationParameters parameters = new OperationParameters(projection, selection, selectionArgs,
                null, null, sortOrder, null);
        onValidateParameters(QUERY_OPERATION, uri, parameters, provider);

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(tableName());

        SQLiteDatabase db = provider.getOpenHelper().getReadableDatabase();
        Cursor c = qb.query(db, parameters.getProjection(), parameters.getSelection(),
                parameters.getSelectionArgs(), parameters.getGroupBy(), parameters.getHaving(),
                parameters.getSortOrder());
        doNotifyOperations(QUERY_OPERATION, uri, c, provider);

        return c;
    }

    protected void doNotifyOperations(int operation, Uri uri, Cursor cursor, Provider provider) {
        switch (operation) {
            case QUERY_OPERATION:
                cursor.setNotificationUri(provider.getContext().getContentResolver(), uri);
                break;

            case INSERT_OPERATION:
            case UPDATE_OPERATION:
            case DELETE_OPERATION:
                if (isLogEnabled) {
                    Log.d(TAG, "Inside doNotifyOperations and context=" + provider.getContext() + " and operation =" + operation);
                }
                provider.getContext().getContentResolver().notifyChange(uri, null);
                break;

            default:
                throw new IllegalArgumentException("Unknown operation: " + operation);
        }
    }

    protected UriMatcher getUriMatcher() {
        return mUriMatcher;
    }

    protected abstract String tableName();

    protected abstract void onValidateParameters(int operation, Uri uri, OperationParameters parameters,
                                                 Provider provider);

    protected abstract boolean isOperationAllowedForUri(int operation, Uri uri);

    @Override
    public Uri insert(Uri uri, ContentValues values, Provider provider) {

        if(!isOperationAllowedForUri(INSERT_OPERATION, uri)) {
            return null;
        }

        OperationParameters parameters = new OperationParameters(null, null, null, null, null, null,
                values);
        onValidateParameters(INSERT_OPERATION, uri, parameters, provider);

        if (parameters.getValues() == null) {
            throw new IllegalArgumentException("values == null");
        }

        SQLiteDatabase db = provider.getOpenHelper().getWritableDatabase();
        long idInserted;
        try {
            idInserted = db.insertOrThrow(tableName(), null, parameters.getValues());
        } catch (SQLiteConstraintException e) {
            // We have a constraint violation here.
            // An example is an attribute used like a "primary key" but not a DB PK. Since this
            // attribute will have an UNIQUE INDEX, an attempt to insert an equal value can throw
            // this constraint exception (and we know that just in the insert moment).
            // The operation, in the following call, has a chance to:
            //   i. Adjust DB and returns true in following call. We'll continue and try the same operation
            //      again. WARNING: this may create a infinite loop if the constraint violation continues
            //      to occurs again and again...
            //  ii. Throws a "better" exception. This is the suggested approach.
            // iii. Returns false and we'll throw the original exception. This is the default implementation.
            if(continueOnConstraintViolation(INSERT_OPERATION, uri, parameters, e, provider)) {
                return insert(uri, parameters.getValues(), provider);
            } else {
                throw e;
            }
        }

        Uri resultUri = Uri.withAppendedPath(uri, String.valueOf(idInserted));
        if (isLogEnabled) {
            Log.d(TAG, "insert, notifying uri=" + resultUri);
        }
        doNotifyOperations(INSERT_OPERATION, resultUri, null, provider);

        if(isLogEnabled) {
            Log.d(TAG, " ->insert()->idInserted = " + idInserted);
        }
        return resultUri;
    }

    private boolean continueOnConstraintViolation(int operation, Uri uri, OperationParameters parameters,
                                                  SQLiteConstraintException constraintException,
                                                  Provider provider) {
        return false;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs, Provider provider) {

        if(!isOperationAllowedForUri(UPDATE_OPERATION, uri)) {
            return FAIL;
        }

        OperationParameters parameters = new OperationParameters(null, selection, selectionArgs,
                null, null, null, values);
        onValidateParameters(UPDATE_OPERATION, uri, parameters, provider);

        if (parameters.getValues() == null) {
            throw new IllegalArgumentException("values must be not null");
        }

        SQLiteDatabase db = provider.getOpenHelper().getWritableDatabase();
        int rowsUpdated;
        try {
            rowsUpdated = db.update(tableName(), parameters.getValues(), parameters.getSelection(),
                    parameters.getSelectionArgs());
        } catch (SQLiteConstraintException e) {
            // We have a constraint violation here.
            // An example is an attribute used like a "primary key" but not a DB PK. Since this
            // attribute will have an UNIQUE INDEX, an attempt to insert an equal value can throw
            // this constraint exception (and we know that just in the insert moment).
            // The operation, in the following call, has a chance to:
            //   i. Adjust DB and returns true in following call. We'll continue and try the same operation
            //      again. WARNING: this may create a infinite loop if the constraint violation continues
            //      to occurs again and again...
            //  ii. Throws a "better" exception. This is the suggested approach.
            // iii. Returns false and we'll throw the original exception. This is the default implementation.
            if(continueOnConstraintViolation(UPDATE_OPERATION, uri, parameters, e, provider)) {
                return update(uri, parameters.getValues(), parameters.getSelection(),
                        parameters.getSelectionArgs(), provider);
            } else {
                throw e;
            }
        }

        if (rowsUpdated > FAIL) {
            if (isLogEnabled) {
                Log.d(TAG, "update, notifying uri=" + uri);
            }
            doNotifyOperations(UPDATE_OPERATION, uri, null, provider);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs, Provider provider) {

        if(!isOperationAllowedForUri(DELETE_OPERATION, uri)) {
            return FAIL;
        }

        OperationParameters parameters = new OperationParameters(null, selection, selectionArgs,
                null, null, null, null);
        onValidateParameters(DELETE_OPERATION, uri, parameters, provider);

        SQLiteDatabase db = provider.getOpenHelper().getWritableDatabase();
        int rowsDeleted = db.delete(tableName(), parameters.getSelection(), parameters.getSelectionArgs());
        if (rowsDeleted > FAIL) {
            if (isLogEnabled) {
                Log.d(TAG, "delete, notifying uri=" + uri);
            }
            doNotifyOperations(DELETE_OPERATION, uri, null, provider);
        }
        return rowsDeleted;
    }

    private static final String TAG = "==>ETD/" + BaseProviderOperator.class.getSimpleName();
    private static final boolean isLogEnabled = true;
}
