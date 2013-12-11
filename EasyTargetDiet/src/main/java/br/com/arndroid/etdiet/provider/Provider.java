package br.com.arndroid.etdiet.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import br.com.arndroid.etdiet.provider.days.DaysOperator;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageOperator;
import br.com.arndroid.etdiet.provider.parametershistory.ParametersHistoryOperator;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersOperator;
import br.com.arndroid.etdiet.sqlite.DBOpenHelper;

public class Provider extends ContentProvider {

    final static private List<ProviderOperator> operators = new ArrayList<ProviderOperator>();

    static {
        // Safe change ProviderOperator: on new implementation add a new line.
        operators.add(new DaysOperator());
        operators.add(new FoodsUsageOperator());
        operators.add(new ParametersHistoryOperator());
        operators.add(new WeekdayParametersOperator());
    }

    private ProviderOperator providerOperatorForUri(Uri uri) {
        for(ProviderOperator operator : operators) {
            if(operator.handleUri(uri)) return operator;
        }
        throw new IllegalArgumentException("Not handled URI: " + uri);
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
            return providerOperatorForUri(uri).getType(uri);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public SQLiteOpenHelper getOpenHelper() {
        return mDBHelper;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return providerOperatorForUri(uri).query(uri, projection, selection, selectionArgs, sortOrder, this);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return providerOperatorForUri(uri).insert(uri, values, this);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return providerOperatorForUri(uri).update(uri, values, selection, selectionArgs, this);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return providerOperatorForUri(uri).delete(uri, selection, selectionArgs, this);
    }

    // Log
    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "==>ETD/" + Provider.class.getSimpleName();
    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;
}