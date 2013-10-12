package br.com.arndroid.etdiet.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import br.com.arndroid.etdiet.provider.days.DaysOperation;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageOperation;
import br.com.arndroid.etdiet.provider.parametershistory.ParametersHistoryOperation;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersOperation;
import br.com.arndroid.etdiet.sqlite.DBOpenHelper;

import static br.com.arndroid.etdiet.provider.Contract.TargetException.FieldDescriptor;

public class Provider extends ContentProvider {

    public static final int QUERY_OPERATION = 0;
    public static final int INSERT_OPERATION = 1;
    public static final int UPDATE_OPERATION = 2;
    public static final int DELETE_OPERATION = 3;

    private static final UriMatcher sUriMatcher;
    public static final int DAYS_URI_MATCH = 0;
    public static final int DAYS_ITEM_URI_MATCH = 1;
    public static final int FOODS_USAGE_URI_MATCH = 2;
    public static final int FOODS_USAGE_ITEM_URI_MATCH = 3;
    public static final int WEEKDAY_PARAMETERS_URI_MATCH = 4;
    public static final int WEEKDAY_PARAMETERS_ITEM_URI_MATCH = 5;
    public static final int PARAMETERS_HISTORY_URI_MATCH = 6;
    public static final int PARAMETERS_HISTORY_ITEM_URI_MATCH = 7;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(Contract.AUTHORITY, Contract.FoodsUsage.TABLE_NAME, FOODS_USAGE_URI_MATCH);
        sUriMatcher.addURI(Contract.AUTHORITY, Contract.FoodsUsage.TABLE_NAME + "/#", FOODS_USAGE_ITEM_URI_MATCH);
        sUriMatcher.addURI(Contract.AUTHORITY, Contract.WeekdayParameters.TABLE_NAME, WEEKDAY_PARAMETERS_URI_MATCH);
        sUriMatcher.addURI(Contract.AUTHORITY, Contract.WeekdayParameters.TABLE_NAME + "/#", WEEKDAY_PARAMETERS_ITEM_URI_MATCH);
        sUriMatcher.addURI(Contract.AUTHORITY, Contract.ParametersHistory.TABLE_NAME, PARAMETERS_HISTORY_URI_MATCH);
        sUriMatcher.addURI(Contract.AUTHORITY, Contract.ParametersHistory.TABLE_NAME + "/#", PARAMETERS_HISTORY_ITEM_URI_MATCH);
    }

	private ProviderOperation providerOperationForUri(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case DAYS_URI_MATCH:
            case DAYS_ITEM_URI_MATCH:
                return new DaysOperation();

            case FOODS_USAGE_URI_MATCH:
            case FOODS_USAGE_ITEM_URI_MATCH:
                return new FoodsUsageOperation();

            case WEEKDAY_PARAMETERS_URI_MATCH:
            case WEEKDAY_PARAMETERS_ITEM_URI_MATCH:
                return new WeekdayParametersOperation();

            case PARAMETERS_HISTORY_URI_MATCH:
            case PARAMETERS_HISTORY_ITEM_URI_MATCH:
                return new ParametersHistoryOperation();

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    private DBOpenHelper mDBHelper;

    @Override
    public boolean onCreate() {
        mDBHelper = new DBOpenHelper(getContext());
        return true;
    }

    @Override
    public void shutdown() {
        // Do nothing. We are disabling warnings
        // suggesting a implementation.
    }

    @Override
    public String getType(Uri uri) {

        try {
            return providerOperationForUri(uri).getType(uri);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public SQLiteOpenHelper getOpenHelper() {
        return mDBHelper;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        final ProviderOperation operation = providerOperationForUri(uri);

        if(!operation.isOperationAllowedForUri(QUERY_OPERATION, uri)) {
            return null;
        }

        OperationParameters parameters = new OperationParameters(projection, selection, selectionArgs,
                null, null, sortOrder, null);
        operation.onValidateParameters(QUERY_OPERATION, uri, parameters, this);

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(operation.tableName());

        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor c = qb.query(db, parameters.getProjection(), parameters.getSelection(),
                parameters.getSelectionArgs(), parameters.getGroupBy(), parameters.getHaving(),
                parameters.getSortOrder());
        operation.notify(QUERY_OPERATION, uri, c, this);

        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final ProviderOperation operation = providerOperationForUri(uri);

        if(!operation.isOperationAllowedForUri(INSERT_OPERATION, uri)) {
            return null;
        }

        OperationParameters parameters = new OperationParameters(null, null, null, null, null, null,
                values);
        operation.onValidateParameters(INSERT_OPERATION, uri, parameters, this);

        if (parameters.getValues() == null) {
            throw new IllegalArgumentException("values == null");
        }

        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        long idInserted;
        try {
            idInserted = db.insertOrThrow(operation.tableName(), null, parameters.getValues());
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
            if(operation.continueOnConstraintViolation(INSERT_OPERATION, uri, parameters, e, this)) {
                return insert(uri, parameters.getValues());
            } else {
                throw e;
            }
        }

        Uri resultUri = Uri.withAppendedPath(uri, String.valueOf(idInserted));
        operation.notify(INSERT_OPERATION, resultUri, null, this);

        if(isLogEnabled) {
            Log.d(TAG,
                  " ->insert()" +
                  " ->idInserted = " + idInserted
            );
        }

        return resultUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final ProviderOperation operation = providerOperationForUri(uri);

        final int fail = 0;
        if(!operation.isOperationAllowedForUri(UPDATE_OPERATION, uri)) {
            return fail;
        }

        OperationParameters parameters = new OperationParameters(null, selection, selectionArgs,
                null, null, null, values);
        operation.onValidateParameters(UPDATE_OPERATION, uri, parameters, this);

        if (parameters.getValues() == null) {
            throw new IllegalArgumentException("values must be not null");
        }

        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int rowsUpdated;
        try {
            rowsUpdated = db.update(operation.tableName(), parameters.getValues(), parameters.getSelection(),
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
            if(operation.continueOnConstraintViolation(UPDATE_OPERATION, uri, parameters, e, this)) {
                return update(uri, parameters.getValues(), parameters.getSelection(), parameters.getSelectionArgs());
            } else {
                throw e;
            }
        }

        if (rowsUpdated > fail) {
            operation.notify(UPDATE_OPERATION, uri, null, this);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final ProviderOperation operation = providerOperationForUri(uri);
        final int fail = 0;
        if(!operation.isOperationAllowedForUri(DELETE_OPERATION, uri)) {
            return fail;
        }

        OperationParameters parameters = new OperationParameters(null, selection, selectionArgs,
                null, null, null, null);
        operation.onValidateParameters(DELETE_OPERATION, uri, parameters, this);


        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int rowsDeleted = db.delete(operation.tableName(), parameters.getSelection(), parameters.getSelectionArgs());
        if (rowsDeleted > fail) {
            operation.notify(DELETE_OPERATION, uri, null, this);
        }
        return rowsDeleted;
    }

    // Log

    private static final String TAG = "==>ETD/" + Provider.class.getSimpleName();
    private static final boolean isLogEnabled = true;
}