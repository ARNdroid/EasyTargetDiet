package br.com.arndroid.etdiet.provider;

import android.content.ContentValues;

public abstract class AbstractEntity {

    @SuppressWarnings("UnusedDeclaration")
    abstract public ContentValues toContentValues();

    @SuppressWarnings("UnusedDeclaration")
    abstract public ContentValues toContentValuesIgnoreNulls();

    abstract public void validateOrThrow();

    @SuppressWarnings("UnusedDeclaration")
    abstract public String getTableName();

    @SuppressWarnings({"SameReturnValue", "UnusedDeclaration"})
    abstract public String getIdColumnName();
}