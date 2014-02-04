package br.com.arndroid.etdiet.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class LogExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(LogExceptionHandler.class);

    private Thread.UncaughtExceptionHandler defaultUEH;

    public LogExceptionHandler() {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }


    public void uncaughtException(Thread thread, Throwable ex) {

        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        ex.printStackTrace(printWriter);
        final String stackTrace = result.toString();
        LOG.error(stackTrace);
        printWriter.close();

        defaultUEH.uncaughtException(thread, ex);
    }
}
