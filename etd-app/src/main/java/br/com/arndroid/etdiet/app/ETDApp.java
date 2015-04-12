package br.com.arndroid.etdiet.app;

import android.app.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ETDApp extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(ETDApp.class);

    @Override
    public void onCreate() {
        super.onCreate();
        if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof LogExceptionHandler)) {
            /*
                Attention:
                Some exceptions will not be caught by this handler:
                - All exceptions before this statement (like super.onCreate() above);
                - Exceptions outside the main thread.
                Therefore, a weird and not logged exception may be in console but NOT with '==>ETD'
                string prefix. Be careful!
             */
            LOG.trace("About to set new default uncaught exception handler.");
            Thread.setDefaultUncaughtExceptionHandler(new LogExceptionHandler());
            LOG.trace("New default uncaught exception handler set.");
        }
    }
}
