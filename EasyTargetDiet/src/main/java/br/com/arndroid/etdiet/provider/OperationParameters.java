package br.com.arndroid.etdiet.provider;

import android.content.ContentValues;

public class OperationParameters {

    private String[] projection;
    private String selection;
    private String[] selectionArgs;
    private String groupBy;
    private String having;
    private String sortOrder;
    private ContentValues values;

    @SuppressWarnings("SameParameterValue")
    public OperationParameters(String[] projection, String selection, String[] selectionArgs,
                               String groupBy, String having, String sortOrder, ContentValues values) {
        this.projection = projection;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
        this.groupBy = groupBy;
        this.having = having;
        this.sortOrder = sortOrder;
        this.values = values;
    }

    public String getGroupBy() {
        return groupBy;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public String getHaving() {
        return having;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setHaving(String having) {
        this.having = having;
    }

    public String[] getProjection() {
        return projection;
    }

    @SuppressWarnings("SameParameterValue")
    public void setProjection(String[] projection) {
        this.projection = projection;
    }

    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public String[] getSelectionArgs() {
        return selectionArgs;
    }

    public void setSelectionArgs(String[] selectionArgs) {
        this.selectionArgs = selectionArgs;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public ContentValues getValues() {
        return values;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setValues(ContentValues values) {
        this.values = values;
    }
}
