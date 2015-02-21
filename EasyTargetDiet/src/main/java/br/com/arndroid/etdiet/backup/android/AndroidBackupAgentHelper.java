package br.com.arndroid.etdiet.backup.android;

import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;

import br.com.arndroid.etdiet.BuildConfig;
import br.com.arndroid.etdiet.sqlite.DBOpenHelper;
import br.com.arndroid.etdiet.utils.PreferencesUtils;

public class AndroidBackupAgentHelper extends BackupAgentHelper {
    @Override
    public void onCreate() {
        super.onCreate();

        addHelper("PREFERENCES_BACKUP", new SharedPreferencesBackupHelper(this,
                PreferencesUtils.PREFERENCES_FILE_NAME));
    }
}
