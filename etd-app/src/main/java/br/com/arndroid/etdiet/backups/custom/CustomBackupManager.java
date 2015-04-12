package br.com.arndroid.etdiet.backups.custom;

import android.content.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import br.com.arndroid.etdiet.sqlite.DBOpenHelper;
import br.com.arndroid.etdiet.utils.DateUtils;
import br.com.arndroid.etdiet.virtualweek.VirtualWeek;

public class CustomBackupManager {

    private static final Logger LOG = LoggerFactory.getLogger(CustomBackupManager.class);

    private final Context mContext;
    private final FileSystemOperations mOperations;

    public CustomBackupManager(Context context) {
        this(context, new FileSystemOperations());
    }

    public CustomBackupManager(Context context, FileSystemOperations fileSystemOperations) {
        mContext = context;
        mOperations = fileSystemOperations;
    }

    public BackupOperationResult doBackup() {
        LOG.trace("doBackup(): operation started.");

        if (!mOperations.isExternalStorageWritable()) {
            LOG.warn("doBackup(): operation exited with SD card unavailable.");
            return BackupOperationResult.SD_CARD_UNAVAILABLE;
        }

        final File source = mContext.getDatabasePath(DBOpenHelper.DATA_BASE_NAME);
        LOG.trace("doBackup(): source file set to '{}'.", source.getPath());

        final String targetFileName =  "ETD_" + DateUtils.dateToTimestamp(new Date()) + ".backup";
        final File target = mOperations.getFile(mOperations.getExternalBackupDirectory().getPath(), targetFileName);
        LOG.trace("doBackup(): target file set to '{}'.", target.getPath());
        final boolean result = mOperations.deleteFileIfExists(target);
        if (result) {
            LOG.info("doBackup(): target file already exists. It was deleted.");
        }

        try {
            LOG.trace("doBackup(): about to copy file from source to target.");
            mOperations.fileCopy(source, target);
            LOG.trace("doBackup(): copied file from source to target.");
        } catch (IOException e) {
            LOG.error("doBackup(): IOException:\n{}", e.getMessage());
            LOG.trace("doBackup(): exited with IOException.");
            return BackupOperationResult.IO_EXCEPTION;
        } catch (SecurityException e) {
            LOG.error("doBackup(): SecurityException:\n{}", e.getMessage());
            LOG.trace("doBackup(): exited with SecurityException.");
            return BackupOperationResult.SECURITY_EXCEPTION;
        }

        LOG.trace("doBackup(): finished successful.");
        return BackupOperationResult.SUCCESS;
    }

    public RestoreOperationResult doRestore(RestoreFileInfo fileInfo) {
        LOG.trace("doRestore(): method entered with fileInfo='{}'.", fileInfo);

        if (!mOperations.isExternalStorageReadable()) {
            LOG.warn("doRestore(): exited with SD card unavailable.");
            return RestoreOperationResult.SD_CARD_UNAVAILABLE;
        }

        if (!mOperations.validateDatabase(fileInfo.getDirName(), fileInfo.getFileName())) {
            LOG.warn("doRestore(): exited with an invalid restore candidate file.");
            return RestoreOperationResult.INVALID_CANDIDATE_FILE;
        }

        final File source = mOperations.getFile(fileInfo.getDirName(), fileInfo.getFileName());
        LOG.trace("doRestore(): source file set to '{}'.", source.getPath());

        final File temp = mContext.getDatabasePath(DBOpenHelper.TEMP_DATA_BASE_NAME);
        LOG.trace("doRestore(): temp file set to '{}'.", temp.getPath());
        final boolean result = mOperations.deleteFileIfExists(temp);
        if (result) {
            LOG.trace("doRestore(): temp file already exists. It was deleted.");
        }

        final File target = mContext.getDatabasePath(DBOpenHelper.DATA_BASE_NAME);
        LOG.trace("doRestore(): target file set to '{}'.", target.getPath());

        try {
            LOG.trace("doRestore(): about to copy file from target to temp.");
            mOperations.fileCopy(target, temp);
            LOG.trace("doRestore(): copied file from target to temp.");
        } catch (IOException e) {
            LOG.error("doRestore(): IOException:\n{}", e.getMessage());
            LOG.trace("doRestore(): exited with IOException.");
            return RestoreOperationResult.IO_EXCEPTION;
        } catch (SecurityException e) {
            LOG.error("doRestore(): SecurityException:\n{}", e.getMessage());
            LOG.trace("doRestore(): exited with SecurityException.");
            return RestoreOperationResult.SECURITY_EXCEPTION;
        }

        LOG.trace("doRestore(): about to close database.");
        mOperations.closeDatabase(mContext);
        LOG.trace("doRestore(): database closed.");

        try {
            LOG.trace("doRestore(): about to copy file from source to target.");
            mOperations.fileCopy(source, target);
            LOG.trace("doRestore(): copied file from source to target.");
        } catch (IOException e) {
            LOG.error("doRestore(): IOException:\n{}", e.getMessage());
            LOG.trace("doRestore(): exited with IOException.");
            return RestoreOperationResult.IO_EXCEPTION;
        } catch (SecurityException e) {
            LOG.error("doRestore(): SecurityException:\n{}", e.getMessage());
            LOG.trace("doRestore(): exited with SecurityException.");
            return RestoreOperationResult.SECURITY_EXCEPTION;
        } finally {
            LOG.trace("doRestore(): about to open database.");
            mOperations.openDatabase(mContext);
            LOG.trace("doRestore(): database opened.");
            LOG.trace("doRestore(): about to inform VirtualWeek of current database restore.");
            VirtualWeek.getInstance(mContext).informDatabaseRestore();
            LOG.trace("doRestore(): VirtualWeek informed of current database restore.");
        }

        LOG.trace("doRestore(): finished successful.");
        return RestoreOperationResult.SUCCESS;
    }

    public List<RestoreFileInfo> getAllRestoreCandidates() {
        ArrayList<RestoreFileInfo> result = new ArrayList<>();
        final File parentBackupFolder = mOperations.getExternalBackupDirectory();
        LOG.trace("getAllRestoreCandidates(): about to get all files for '{}'.", parentBackupFolder);
        File[] files = mOperations.listAllFiles(parentBackupFolder);
        for (File file : files) {
            if (!file.isDirectory()) {
                result.add(new RestoreFileInfo(parentBackupFolder.getPath(), file.getName(), file.lastModified()));
            }
        }

        LOG.trace("getAllRestoreCandidates(): sorting candidates.");
        Collections.sort(result);
        LOG.trace("getAllRestoreCandidates(): returning '{}' candidates.", result.size());
        return result;
    }

    public void deleteCandidate(RestoreFileInfo candidate) {
        final File candidateFile = new File(candidate.getDirName(), candidate.getFileName());
        mOperations.deleteFileIfExists(candidateFile);
    }

    public enum BackupOperationResult {
        SUCCESS,
        SD_CARD_UNAVAILABLE,
        IO_EXCEPTION,
        SECURITY_EXCEPTION
    }

    public enum RestoreOperationResult {
        SUCCESS,
        SD_CARD_UNAVAILABLE,
        IO_EXCEPTION,
        SECURITY_EXCEPTION,
        INVALID_CANDIDATE_FILE
    }
}
