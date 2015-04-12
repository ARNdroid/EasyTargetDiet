package br.com.arndroid.etdiet.backups.custom;

import android.content.ContentProviderClient;
import android.content.Context;
import android.os.Environment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.Provider;
import br.com.arndroid.etdiet.sqlite.DBOpenHelper;

public class FileSystemOperations {

    private static final Logger LOG = LoggerFactory.getLogger(FileSystemOperations.class);

    public boolean isExternalStorageWritable() {
        LOG.trace("isExternalStorageWritable(): method entered.");
        LOG.trace("isExternalStorageWritable(): about to get external storage state.");
        final String state = Environment.getExternalStorageState();
        LOG.trace("isExternalStorageWritable(): external storage state =='{}'.", state);
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            LOG.trace("isExternalStorageWritable(): returning state == MEDIA_MOUNTED.");
            return true;
        }
        LOG.warn("isExternalStorageWritable(): returning state != MEDIA_MOUNTED.");
        return false;
    }

    public boolean isExternalStorageReadable() {
        LOG.trace("isExternalStorageReadable(): method entered.");
        LOG.trace("isExternalStorageReadable(): about to get external storage state.");
        final String state = Environment.getExternalStorageState();
        LOG.trace("isExternalStorageReadable(): external storage state =={}", state);
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            LOG.trace("isExternalStorageReadable(): returning state == MEDIA_MOUNTED || state == MEDIA_MOUNTED_READ_ONLY.");
            return true;
        }
        LOG.warn("isExternalStorageReadable(): returning state != MEDIA_MOUNTED && state != MEDIA_MOUNTED_READ_ONLY.");
        return false;
    }

    public File getExternalBackupDirectory() {
        LOG.trace("getExternalBackupDirectory(): method entered.");
        final File result = new File(Environment.getExternalStorageDirectory(), DBOpenHelper.BACKUP_FOLDER);
        LOG.trace("getExternalBackupDirectory(): returning a file with path=='{}'.", result.getPath());
        return result;
    }

    public File getFile(String dirPath, String fileName) {
        LOG.trace("getFile(): method entered with dirPath=='{}' and fileName=='{}'.", dirPath, fileName);
        final File result = new File(dirPath, fileName);
        LOG.trace("getFile(): returning a file with path=='{}'.", result.getPath());
        return result;
    }

    public boolean deleteFileIfExists(File file) {
        LOG.trace("deleteFile(): method entered with file =='{}'.", file);
        if (file == null) {
            LOG.warn("deleteFile(): file is null. Aborting");
            throw new IllegalArgumentException("file cannot be null.");
        }
        LOG.trace("deleteFile(): file has path =='{}'.", file.getPath());
        if (file.exists()) {
            LOG.trace("deleteFile(): file exists. About to delete it.");
            final boolean fileDeleted = file.delete();
            LOG.trace("deleteFile(): file delete operation result=='{}'.", fileDeleted);
            return fileDeleted;
        } else {
            LOG.trace("deleteFile(): file doesn't exist. It's not necessary a delete operation.");
            return false;
        }
    }

    public void fileCopy(File source, File target) throws IOException {
        LOG.trace("fileCopy(): method entered with source=={} and target=='{}'.", source, target);
        if (source == null || target == null) {
            LOG.error("fileCopy(): copy operation could NOT be done: source and/or target file are null. Aborting.");
            throw new IllegalArgumentException("source and target cannot be null.");
        }
        LOG.trace("fileCopy(): source file with path=='{}'.", source.getPath());
        LOG.trace("fileCopy(): target file with path=='{}'.", target.getPath());
        if(!target.exists()) {
            LOG.trace("fileCopy(): target file doesn't exist. About to create it.");
            final boolean dirsMake = target.getParentFile().mkdirs();
            LOG.trace("fileCopy(): Did make target dirs? '{}'.", dirsMake);
            final boolean fileCreated = target.createNewFile();
            LOG.trace("fileCopy(): Did create target file? '{}'.", fileCreated);
        }
        LOG.trace("fileCopy(): about to create inChannel.");
        FileChannel inChannel = new FileInputStream(source).getChannel();
        LOG.trace("fileCopy(): about to create outChannel.");
        FileChannel outChannel = new FileOutputStream(target).getChannel();
        try {
            LOG.trace("fileCopy(): about to transfer inChannel -> outChannel.");
            inChannel.transferTo(0, inChannel.size(), outChannel);
            LOG.trace("fileCopy(): channels transference successfully completed.");
        } finally {
            if (inChannel != null) {
                LOG.trace("fileCopy(): about to close inChannel.");
                inChannel.close();
            }
            if (outChannel != null) {
                LOG.trace("fileCopy(): about to close outChannel.");
                outChannel.close();
            }
        }
    }

    public boolean validateDatabase(String dirName, String fileName) {
        LOG.trace("validateDatabase(): method entered with dirName=='{}' and fileName=='{}'.", dirName, fileName);
        final String pathToDatabase = dirName + File.separator + fileName;
        LOG.trace("validateDatabase(): about to call DBOpenHelper.validateDatabase() with pathToDatabase=='{}'.", pathToDatabase);
        final boolean result = DBOpenHelper.validateDatabase(pathToDatabase);
        LOG.trace("validateDatabase(): DBOpenHelper.validateDatabase() returned value=='{}'. Exiting with this value.", result);
        return result;
    }

    public void closeDatabase(Context context) {
        LOG.trace("closeDatabase(): method entered with context=='{}'.", context);
        ContentProviderClient providerClient = null;
        try {
            LOG.trace("closeDatabase(): about to acquire a content provider client.");
            providerClient = context.getContentResolver().acquireContentProviderClient(Contract.AUTHORITY);
            LOG.trace("closeDatabase(): content provider client=='{}'.", providerClient);
            LOG.trace("closeDatabase(): about to acquire a ETD content provider.");
            final Provider provider = (Provider) providerClient.getLocalContentProvider();
            LOG.trace("closeDatabase(): ETD content provider=='{}'.", provider);
            LOG.trace("closeDatabase(): about to call ETD provider.closeOpenHelper().");
            provider.closeOpenHelper();
            LOG.trace("closeDatabase(): ETD provider.closeOpenHelper() returned.");
        } finally {
            if (providerClient != null) {
                LOG.trace("closeDatabase(): about to release provider client.");
                providerClient.release();
            }
        }
    }

    public File[] listAllFiles(File parentFolder) {
        LOG.trace("listAllFiles(): method entered with parentFolder=='{}'.", parentFolder);
        if (parentFolder.exists()) {
            LOG.trace("listAllFiles(): parentFolder exists. Listing files.");
            final File [] result = parentFolder.listFiles();
            LOG.trace("listAllFiles(): returning File[] with length=='{}'.", result.length);
            return result;
        } else {
            LOG.trace("listAllFiles(): parentFolder doesn't exist. Returning empty File[].");
            return new File[0];
        }
    }

    public void openDatabase(Context context) {
        LOG.trace("openDatabase(): method entered with context=='{}'.", context);
        ContentProviderClient providerClient = null;
        try {
            LOG.trace("openDatabase(): about to acquire a content provider client.");
            providerClient = context.getContentResolver().acquireContentProviderClient(Contract.AUTHORITY);
            LOG.trace("openDatabase(): content provider client=='{}'.", providerClient);
            LOG.trace("openDatabase(): about to acquire a ETD content provider.");
            final Provider provider = (Provider) providerClient.getLocalContentProvider();
            LOG.trace("openDatabase(): ETD content provider=='{}'.", provider);
            LOG.trace("openDatabase(): about to call ETD provider.initializeOpenHelper().");
            provider.initializeOpenHelper();
            LOG.trace("openDatabase(): ETD provider.initializeOpenHelper() returned.");
        } finally {
            if (providerClient != null) {
                LOG.trace("openDatabase(): about to release provider client.");
                providerClient.release();
            }
        }
    }
}