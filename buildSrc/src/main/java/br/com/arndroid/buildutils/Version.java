package br.com.arndroid.buildutils;

/* package */ class Version {

    private int mMajor;
    private int mMinor;
    private int mHotfix;

    public Version(String versionName) {
        final String[] version = versionName.split("\\.");

        mMajor = Integer.parseInt(version[0]);
        mMinor = Integer.parseInt(version[1]);
        mHotfix = Integer.parseInt(version[2]);

        validateOrThrow();
    }

    private void validateOrThrow() {
        if (mMajor < 0) {
            throw new IllegalArgumentException("Version major = " + mMajor + ". Must be a positive integer");
        }
        if (mMinor < 0 || mMinor > 99) {
            throw new IllegalArgumentException("Version minor = " + mMinor + " Must be between 0 and 99.");
        }
        if (mHotfix < 0 || mHotfix > 999) {
            throw new IllegalArgumentException("Version hotfix = " + mHotfix + " Must be between 0 and 999.");
        }
    }

    public String versionForOutputFileName() {
        return mMajor + "_" + mMinor + "_" + mHotfix;
    }

    public int versionNumber() {
        return 100000 * mMajor + 1000 * mMinor + mHotfix;
    }
}
