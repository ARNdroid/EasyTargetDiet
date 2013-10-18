package br.com.arndroid.etdiet.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;

public abstract class ProviderOperation {

    public abstract String getType(Uri uri);
    public abstract String tableName();
    public abstract void onValidateParameters(int operation, Uri uri, OperationParameters parameters,
                                              Provider provider);
    public abstract void doNotifyOperations(int operation, Uri uri, Cursor cursor, Provider provider);
    public abstract boolean isOperationAllowedForUri(int operation, Uri uri);
    public abstract boolean continueOnConstraintViolation(int operation, Uri uri, OperationParameters parameters,
                                                          SQLiteConstraintException constraintException,
                                                          Provider provider);
}
