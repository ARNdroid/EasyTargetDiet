package br.com.arndroid.etdiet.test.backup.custom;

import android.test.AndroidTestCase;

import java.io.File;
import java.io.IOException;

import br.com.arndroid.etdiet.backups.custom.CustomBackupManager;
import br.com.arndroid.etdiet.backups.custom.FileSystemOperations;
import br.com.arndroid.etdiet.backups.custom.RestoreFileInfo;

import static org.mockito.Mockito.*;

public class CustomBackupManagerTest extends AndroidTestCase {

    public void testDoBackupWithoutIssuesMustReturnCorrectValue() {
        // We couldn't use real IO operations inside this test case. If FileSystemOperations are not mocked,
        // we'll get an IOException.
        final FileSystemOperations fixtureOperations = mock(FileSystemOperations.class);
        when(fixtureOperations.isExternalStorageWritable()).thenReturn(true);
        when(fixtureOperations.getExternalBackupDirectory()).thenReturn(new File("/external/backup/directory/"));
        when(fixtureOperations.getFile(anyString(), anyString())).thenReturn(new File("/external/backup/directory/ETD_20150317235959.backup"));
        when(fixtureOperations.deleteFileIfExists(any(File.class))).thenReturn(true);
        // We don't need to mock FileSystemOperations.fileCopy returning void. This return is the default behavior.
        final CustomBackupManager manager = new CustomBackupManager(getContext(), fixtureOperations);
        assertEquals(CustomBackupManager.BackupOperationResult.SUCCESS, manager.doBackup());
    }

    public void testDoBackupSDCardNotMountedMustReturnCorrectValue() {
        final FileSystemOperations fixtureOperations = mock(FileSystemOperations.class);
        when(fixtureOperations.isExternalStorageWritable()).thenReturn(false);
        final CustomBackupManager manager = new CustomBackupManager(getContext(), fixtureOperations);
        assertEquals(CustomBackupManager.BackupOperationResult.SD_CARD_UNAVAILABLE, manager.doBackup());
    }

    public void testDoBackupWithIOExceptionMustReturnCorrectValue() throws IOException {
        final FileSystemOperations fixtureOperations = mock(FileSystemOperations.class);
        when(fixtureOperations.isExternalStorageWritable()).thenReturn(true);
        when(fixtureOperations.getExternalBackupDirectory()).thenReturn(new File("/external/backup/directory/"));
        when(fixtureOperations.getFile(anyString(), anyString())).thenReturn(new File("/external/backup/directory/ETD_20150317235959.backup"));
        when(fixtureOperations.deleteFileIfExists(any(File.class))).thenReturn(true);
        doThrow(new IOException()).when(fixtureOperations).fileCopy(any(File.class), any(File.class));
        final CustomBackupManager manager = new CustomBackupManager(getContext(), fixtureOperations);
        assertEquals(CustomBackupManager.BackupOperationResult.IO_EXCEPTION, manager.doBackup());
    }

    public void testDoBackupWithSecurityExceptionMustReturnCorrectValue() throws IOException {
        final FileSystemOperations fixtureOperations = mock(FileSystemOperations.class);
        when(fixtureOperations.isExternalStorageWritable()).thenReturn(true);
        when(fixtureOperations.getExternalBackupDirectory()).thenReturn(new File("/external/backup/directory/"));
        when(fixtureOperations.getFile(anyString(), anyString())).thenReturn(new File("/external/backup/directory/ETD_20150317235959.backup"));
        when(fixtureOperations.deleteFileIfExists(any(File.class))).thenReturn(true);
        doThrow(new SecurityException()).when(fixtureOperations).fileCopy(any(File.class), any(File.class));
        final CustomBackupManager manager = new CustomBackupManager(getContext(), fixtureOperations);
        assertEquals(CustomBackupManager.BackupOperationResult.SECURITY_EXCEPTION, manager.doBackup());
    }

    public void testDoRestoreWithoutIssuesMustReturnCorrectValue() {
        // We couldn't use real IO operations inside this test case. If FileSystemOperations are not mocked,
        // we'll get an IOException.
        final FileSystemOperations fixtureOperations = mock(FileSystemOperations.class);
        when(fixtureOperations.isExternalStorageReadable()).thenReturn(true);
        when(fixtureOperations.validateDatabase(anyString(), anyString())).thenReturn(true);
        when(fixtureOperations.getFile(anyString(), anyString())).thenReturn(new File("/external/backup/directory/ETD_20150317235959.backup"));
        when(fixtureOperations.deleteFileIfExists(any(File.class))).thenReturn(true);
        // We don't need to mock methods returning void. This return is the default behavior.
        final CustomBackupManager manager = new CustomBackupManager(getContext(), fixtureOperations);
        final RestoreFileInfo fileInfo = new RestoreFileInfo("/external/backup/directory/", "ETD_20150317235959.backup", 0);
        assertEquals(CustomBackupManager.RestoreOperationResult.SUCCESS, manager.doRestore(fileInfo));
    }

    public void testDoRestoreWithSDCardNotMountedMustReturnCorrectValue() {
        // We couldn't use real IO operations inside this test case. If FileSystemOperations are not mocked,
        // we'll get an IOException.
        final FileSystemOperations fixtureOperations = mock(FileSystemOperations.class);
        when(fixtureOperations.isExternalStorageReadable()).thenReturn(false);
        when(fixtureOperations.validateDatabase(anyString(), anyString())).thenReturn(true);
        when(fixtureOperations.getFile(anyString(), anyString())).thenReturn(new File("/external/backup/directory/ETD_20150317235959.backup"));
        when(fixtureOperations.deleteFileIfExists(any(File.class))).thenReturn(true);
        // We don't need to mock methods returning void. This return is the default behavior.
        final CustomBackupManager manager = new CustomBackupManager(getContext(), fixtureOperations);
        final RestoreFileInfo fileInfo = new RestoreFileInfo("/external/backup/directory/", "ETD_20150317235959.backup", 0);
        assertEquals(CustomBackupManager.RestoreOperationResult.SD_CARD_UNAVAILABLE, manager.doRestore(fileInfo));
    }

    public void testDoRestoreWithInvalidCandidateMustReturnCorrectValue() {
        // We couldn't use real IO operations inside this test case. If FileSystemOperations are not mocked,
        // we'll get an IOException.
        final FileSystemOperations fixtureOperations = mock(FileSystemOperations.class);
        when(fixtureOperations.isExternalStorageReadable()).thenReturn(true);
        when(fixtureOperations.validateDatabase(anyString(), anyString())).thenReturn(false);
        when(fixtureOperations.getFile(anyString(), anyString())).thenReturn(new File("/external/backup/directory/ETD_20150317235959.backup"));
        when(fixtureOperations.deleteFileIfExists(any(File.class))).thenReturn(true);
        // We don't need to mock methods returning void. This return is the default behavior.
        final CustomBackupManager manager = new CustomBackupManager(getContext(), fixtureOperations);
        final RestoreFileInfo fileInfo = new RestoreFileInfo("/external/backup/directory/", "ETD_20150317235959.backup", 0);
        assertEquals(CustomBackupManager.RestoreOperationResult.INVALID_CANDIDATE_FILE, manager.doRestore(fileInfo));
    }

    public void testDoRestoreWithIOExceptionMustReturnCorrectValue() throws IOException {
        // We couldn't use real IO operations inside this test case. If FileSystemOperations are not mocked,
        // we'll get an IOException.
        final FileSystemOperations fixtureOperations = mock(FileSystemOperations.class);
        when(fixtureOperations.isExternalStorageReadable()).thenReturn(true);
        when(fixtureOperations.validateDatabase(anyString(), anyString())).thenReturn(true);
        when(fixtureOperations.getFile(anyString(), anyString())).thenReturn(new File("/external/backup/directory/ETD_20150317235959.backup"));
        when(fixtureOperations.deleteFileIfExists(any(File.class))).thenReturn(true);
        // We don't need to mock methods returning void. This return is the default behavior.
        doThrow(new IOException()).when(fixtureOperations).fileCopy(any(File.class), any(File.class));
        final CustomBackupManager manager = new CustomBackupManager(getContext(), fixtureOperations);
        final RestoreFileInfo fileInfo = new RestoreFileInfo("/external/backup/directory/", "ETD_20150317235959.backup", 0);
        assertEquals(CustomBackupManager.RestoreOperationResult.IO_EXCEPTION, manager.doRestore(fileInfo));
    }

    public void testDoRestoreWithSecurityExceptionMustReturnCorrectValue() throws IOException {
        // We couldn't use real IO operations inside this test case. If FileSystemOperations are not mocked,
        // we'll get an IOException.
        final FileSystemOperations fixtureOperations = mock(FileSystemOperations.class);
        when(fixtureOperations.isExternalStorageReadable()).thenReturn(true);
        when(fixtureOperations.validateDatabase(anyString(), anyString())).thenReturn(true);
        when(fixtureOperations.getFile(anyString(), anyString())).thenReturn(new File("/external/backup/directory/ETD_20150317235959.backup"));
        when(fixtureOperations.deleteFileIfExists(any(File.class))).thenReturn(true);
        // We don't need to mock methods returning void. This return is the default behavior.
        doThrow(new SecurityException()).when(fixtureOperations).fileCopy(any(File.class), any(File.class));
        final CustomBackupManager manager = new CustomBackupManager(getContext(), fixtureOperations);
        final RestoreFileInfo fileInfo = new RestoreFileInfo("/external/backup/directory/", "ETD_20150317235959.backup", 0);
        assertEquals(CustomBackupManager.RestoreOperationResult.SECURITY_EXCEPTION, manager.doRestore(fileInfo));
    }
}
