package br.com.arndroid.etdiet.virtualweek;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

public class DatabaseChangeObserver extends ContentObserver {

    public static final int DAY_CHANGE_TYPE = 0;
    public static final int FOODS_USAGE_CHANGE_TYPE = 1;
    public static final int PARAMETERS_HISTORY_CHANGE_TYPE = 2;
    public static final int WEEKDAY_PARAMETERS_CHANGE_TYPE = 3;

    private final int mChangeType;
    private final int mIndex;
    private final ChangeListener mObserver;

    public DatabaseChangeObserver(int changeType, int index, ChangeListener observer) {
        super(new Handler());
        mChangeType = changeType;
        mIndex = index;
        mObserver = observer;
    }

    @Override
    public void onChange(boolean selfChange) {
        onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        if (isLogEnabled) {
            Log.d(TAG, "Observer.onChange() called!!!!!!!!!");
        }
        mObserver.onContentChanged(mChangeType, mIndex);
    }

    public interface ChangeListener {
        public void onContentChanged(int changeType, int index);
    }

    private static final String TAG = "==>ETD/" + DatabaseChangeObserver.class.getSimpleName();
    private static final boolean isLogEnabled = true;
}
