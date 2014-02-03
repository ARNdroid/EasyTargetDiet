package br.com.arndroid.etdiet.utils;

import android.database.Observable;
import android.util.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ExposedObservable<T> extends Observable<T> {

    private static final Logger LOG = LoggerFactory.getLogger(ExposedObservable.class);

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
                LOG.warn("ExposedObservable<T> finalized with list of observers not empty. Memory leak?");
                unregisterAll();
            }
        } finally {
            super.finalize();
        }
    }
}
