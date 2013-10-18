package br.com.arndroid.etdiet.virtualweek;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

public class DatabaseChangeObserver extends ContentObserver {

    public static final int DAY_CHANGED_TYPE = 0;
    public static final int FOODS_USAGE_CHANGED_TYPE = 1;
    public static final int PARAMETERS_HISTORY_CHANGED_TYPE = 2;
    public static final int WEEKDAY_PARAMETERS_CHANGED_TYPE = 3;

    private final int mChangedType;
    private final int mIndex;
    private final ChangeListener mObserver;

    public DatabaseChangeObserver(int changedType, int index, ChangeListener observer) {
        super(new Handler());
        mChangedType = changedType;
        mIndex = index;
        mObserver = observer;
    }

    @Override
    public void onChange(boolean selfChange) {
        onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        mObserver.onContentChanged(mChangedType, mIndex);
    }

    public interface ChangeListener {
        public void onContentChanged(int changedType, int index);
    }
}
