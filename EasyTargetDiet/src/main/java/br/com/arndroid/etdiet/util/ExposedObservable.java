package br.com.arndroid.etdiet.util;

import android.database.Observable;
import android.util.Log;

import java.util.List;

public class ExposedObservable<T> extends Observable<T> {
    /*  Our requirements:
        1. At least, clean the observers in finalize, generating a warning;
        2. Expose the list of observers;
        3. Be Generic;
     */
    public List<T> getAllObservers() {
        return mObservers;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            if (mObservers.size() > 0) {
                Log.w(TAG, "ExposedObservable<T> finalized with list of observers not empty." +
                        " This is a memory leak indication.");
                unregisterAll();
            }
        } finally {
            super.finalize();
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "==>ETD/" + ExposedObservable.class.getSimpleName();
    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;
}
