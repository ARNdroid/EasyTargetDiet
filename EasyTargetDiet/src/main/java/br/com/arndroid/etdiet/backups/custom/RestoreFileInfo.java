package br.com.arndroid.etdiet.backups.custom;

import android.support.annotation.NonNull;

public class RestoreFileInfo implements Comparable {

    private final String fileName;
    private final String dirName;
    private final long lastModified;

    public RestoreFileInfo(String dirName, String fileName, long lastModified) {
        this.dirName = dirName;
        this.fileName = fileName;
        this.lastModified = lastModified;
    }

    public String getFileName() {
        return fileName;
    }

    public long getLastModified() {
        return lastModified;
    }

    public String getDirName() {
        return dirName;
    }

    @Override
    public String toString() {
        return "RestoreFileInfo{" +
                "fileName='" + fileName + '\'' +
                ", dirName='" + dirName + '\'' +
                ", lastModified=" + lastModified +
                '}';
    }

    @Override
    public int compareTo(@NonNull Object anotherObj) {
        final RestoreFileInfo anotherInfo = (RestoreFileInfo) anotherObj;
        // order is last modified desc:
        return (int) (anotherInfo.lastModified - lastModified);
    }
}
