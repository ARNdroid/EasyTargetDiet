package br.com.arndroid.etdiet.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import br.com.arndroid.etdiet.provider.Contract;

public class DBOpenHelper extends SQLiteOpenHelper {
	/*
	 * Implementation
	 */

    private static final String DATA_BASE_NAME = "easy_target_diet_db";
    private static final int CURRENT_DATABASE_VERSION = 1;

    public DBOpenHelper(Context context) {
        super(context, DATA_BASE_NAME, null, CURRENT_DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.beginTransaction();
        try {
            // Foods Usage:
            db.execSQL("CREATE TABLE " + Contract.FoodsUsage.TABLE_NAME + " ("
                    + Contract.FoodsUsage._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + Contract.FoodsUsage.DAY_ID + " TEXT NOT NULL, "
                    + Contract.FoodsUsage.MEAL + " INTEGER NOT NULL, "
                    + Contract.FoodsUsage.TIME + " INTEGER NOT NULL, "
                    + Contract.FoodsUsage.DESCRIPTION + " TEXT NOT NULL, "
                    + Contract.FoodsUsage.VALUE + " REAL NOT NULL);");
            db.execSQL("CREATE INDEX " + Contract.FoodsUsage.TABLE_NAME + "_"
                    + Contract.FoodsUsage.DAY_ID + "_" + Contract.FoodsUsage.MEAL + "_idx "
                    + "ON " + Contract.FoodsUsage.TABLE_NAME + " (" + Contract.FoodsUsage.DAY_ID + ", " + Contract.FoodsUsage.MEAL + ");");
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion == newVersion) {
            // Nothing to do (this may be called recursively).
            return;
        }

        // This version not admit any upgrade.
        throw new SQLiteException("Not prepared for database migration between versions " + oldVersion + " and " + newVersion);
    }
}
