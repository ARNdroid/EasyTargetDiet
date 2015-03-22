package br.com.arndroid.etdiet.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final Logger LOG = LoggerFactory.getLogger(DBOpenHelper.class);

    // Versions:
    private static final int FIRST_DATABASE_CREATION = 0;
    private static final int V_01 = 1;
    private static final int V_02 = 2;
    private static final int V_03 = 3;
    private static final int CURRENT_DATABASE_VERSION = V_03;

    public static final String ASSIGNMENT_KEY = "br.com.arndroid.etdiet.sqlite.databaseAssignment";
    public static final String ASSIGNMENT_VALUE = "br.com.arndroid.etdiet";
    public static final String BACKUP_FOLDER = File.separator + "EasyTargetDiet" + File.separator + "backup";
    public static final String DATA_BASE_NAME = "easy_target_diet_db";
    public static final String TEMP_DATA_BASE_NAME = "temp_" + DATA_BASE_NAME;
    private static final String DATABASE_VALIDATION_QUERY = "SELECT * FROM etd_metadata where key='%1s'";

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
            case V_02:
                LOG.trace("About to execute scriptV02ToV03");
                DBScripts.scriptV02ToV03(db);
                LOG.trace("scriptV02ToV03 executed");

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

    public static boolean validateDatabase(String pathToDatabase) {
        LOG.trace("validateDatabase(): method entered with pathToDatabase=='{}'.", pathToDatabase);
        SQLiteDatabase db = null;
        Cursor c = null;
        try {
            LOG.trace("validateDatabase(): about to open database.");
            db = SQLiteDatabase.openDatabase(pathToDatabase, null, SQLiteDatabase.OPEN_READONLY);
            LOG.trace("validateDatabase(): Database opened and db=='{}'.", db);
            if (db == null) {
                LOG.warn("validateDatabase(): db == null. Returning false.");
                return false;
            }
            final String rawQuery = String.format(DATABASE_VALIDATION_QUERY, ASSIGNMENT_KEY);
            LOG.trace("validateDatabase(): About to execute rawQuery=='{}'.", rawQuery);
            c = db.rawQuery(rawQuery, null);
            LOG.trace("validateDatabase(): rawQuery executed.");
            if (c.moveToFirst()) {
                LOG.trace("validateDatabase(): about to acquire assignment value.");
                final String assignment = c.getString(c.getColumnIndex("value"));
                LOG.trace("validateDatabase(): Acquired assignment value=='{}'.", assignment);
                if (ASSIGNMENT_VALUE.equals(assignment)) {
                    LOG.trace("validateDatabase(): Assignment valid. Returning true.");
                    return true;
                } else {
                    LOG.warn("validateDatabase(): Assignment NOT valid. Returning false.");
                    return false;
                }
            } else {
                LOG.warn("validateDatabase(): empty cursor acquired. Returning false.");
                return false;
            }
        } catch (Exception e) {
            LOG.error("validateDatabase(): exception occurred '{}'.", e);
            return false;
        } finally {
            if (c != null) c.close();
            if (db != null) db.close();
        }
    }
}
