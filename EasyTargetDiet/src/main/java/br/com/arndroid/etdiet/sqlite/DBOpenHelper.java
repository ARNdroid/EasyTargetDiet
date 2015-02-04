package br.com.arndroid.etdiet.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final Logger LOG = LoggerFactory.getLogger(DBOpenHelper.class);

    // Versions:
    private static final int FIRST_DATABASE_CREATION = 0;
    private static final int V_01 = 1;
    private static final int V_02 = 2;
    private static final int CURRENT_DATABASE_VERSION = V_02;

    private static final String DATA_BASE_NAME = "easy_target_diet_db";

    public DBOpenHelper(Context context) {
        super(context, DATA_BASE_NAME, null, CURRENT_DATABASE_VERSION);
        LOG.trace("Constructor started with CURRENT_DATABASE_VERSION={}", CURRENT_DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        LOG.trace("onCreate started with FIRST_DATABASE_CREATION={}", FIRST_DATABASE_CREATION);
        execScriptsFrom(db, FIRST_DATABASE_CREATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LOG.trace("onUpgrade started with oldVersion={} and newVersion={}", oldVersion, newVersion);
        execScriptsFrom(db, oldVersion);
    }

    private void execScriptsFrom(SQLiteDatabase db, int baselineVersion) {
        LOG.trace("execScriptsFrom started with baselineVersion={} and current version={}",
                baselineVersion, CURRENT_DATABASE_VERSION);
        switch (baselineVersion) {
            case FIRST_DATABASE_CREATION:
                LOG.trace("About to execute scriptV00ToV01");
                DBScripts.scriptV00ToV01(db);
                LOG.trace("scriptV00ToV01 executed");
            case V_01:
                LOG.trace("About to execute scriptV01ToV02");
                DBScripts.scriptV01ToV02(db);
                LOG.trace("scriptV01ToV02 executed");

            // Mind the gap: when inserting a new case DON'T COPY THE BREAK!
            // The break statement is used only for default clause.
            break;
            default:
                final String errorMessage = String.format(
                        "Invalid baselineVersion=%s with CURRENT_DATABASE_VERSION=%s",
                        baselineVersion, CURRENT_DATABASE_VERSION);
                throw new SQLiteException(errorMessage);
        }
    }
}
