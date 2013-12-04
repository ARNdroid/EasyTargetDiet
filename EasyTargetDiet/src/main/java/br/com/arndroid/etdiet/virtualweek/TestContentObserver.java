package br.com.arndroid.etdiet.virtualweek;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

public class TestContentObserver extends ContentObserver {

    public boolean mCalled = false;

    public TestContentObserver() {
        super(new Handler());
    }
    @Override
    public void onChange(boolean selfChange) {
        onChange(selfChange, null);
    }
    @Override
    public void onChange(boolean selfChange, Uri uri) {
        mCalled = true;
    }
}
