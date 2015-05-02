package br.com.arndroid.buildutlis;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@SuppressWarnings("UnusedDeclaration")
public abstract class AbstractBuildProfile {

    /* VERSION CONTROL
     * Version string must be equals "major.minor.hotfix"
     * The restrictions for this values are:
     * - major >= 0
     * - 0 <= minor <= 99
    \* - 0 <= hotfix <= 99 */
    private static final String DEFAULT_VERSION_STRING = "0.11.0";

    // COMPILATION PROPERTIES:
    private static final int DEFAULT_SDK_VERSION_COMPILE = 22;
    private static final String DEFAULT_BUILD_TOOLS_VERSION = "22.0.1";
    private static final int DEFAULT_SDK_VERSION_MIN = 15;
    private static final int DEFAULT_SDK_VERSION_TARGET = DEFAULT_SDK_VERSION_COMPILE;

    // Contextual values:
    protected static final String CONTEXT_KEY_STORE_PASSWORD = "CONTEXT_KEY_STORE_PASSWORD";
    protected static final String CONTEXT_KEY_VERSION_USER_CONFIRMATION = "CONTEXT_KEY_VERSION_USER_CONFIRMATION";
    protected static final Map<String, String> sContextValues = new HashMap<>();

    // Members:
    private final String mModuleName;
    private String mStoreFile;
    private String mStorePassword;
    private String mKeyAlias;
    private int mVersionMajor;
    private int mVersionMinor;
    private int mVersionHotfix;

    public AbstractBuildProfile(String moduleName) {
        mModuleName = moduleName;
        getPropertiesFromMembers();
        getPropertiesFromFile();
        validatePropertiesOrThrow();
        confirmPropertiesWithUserOrThrow();
        getPropertiesFromUser();
    }

    protected void getPropertiesFromMembers() {
        final String[] version = getVersionString().split("\\.");
        mVersionMajor = Integer.parseInt(version[0]);
        mVersionMinor = Integer.parseInt(version[1]);
        mVersionHotfix = Integer.parseInt(version[2]);
    }

    // Implement this to define the TAG that is shown at gradle console
    protected abstract String getTag();

    // You may override this to work with custom version string
    protected String getVersionString() {
        return DEFAULT_VERSION_STRING;
    }

    // You may override this to work with custom target SDK version
    public int getTargetSdkVersion() {
        return DEFAULT_SDK_VERSION_TARGET;
    }

    // You may override this to work with custom min SDK version
    public int getMinSdkVersion() {
        return DEFAULT_SDK_VERSION_MIN;
    }

    // You may override this to work with custom build tools version
    public String getBuildToolsVersion() {
        return DEFAULT_BUILD_TOOLS_VERSION;
    }

    // You may override this to work with custom compile SDK version
    public int getCompileSdkVersion() {
        return DEFAULT_SDK_VERSION_COMPILE;
    }

    protected void getPropertiesFromFile() {
        Properties properties = new Properties();

        // /etd-app/local.properties
        try {
            properties.load(new FileInputStream(System.getProperty("user.dir") + "/etd-app/local.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Problems opening /etd-app/local.properties.", e);
        }

        // {module}/gradle.properties
        try {
            properties.load(new FileInputStream(System.getProperty("user.dir") + "/" + mModuleName + "/gradle.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Problems opening /" + mModuleName + "/gradle.properties.", e);
        }

        mStoreFile = properties.getProperty("store_file");
        mKeyAlias = properties.getProperty("key_alias");
    }

    protected void validatePropertiesOrThrow() {
        if (mVersionMajor < 0) {
            throw new IllegalArgumentException("Version major = " + mVersionMajor + ". Must be a positive integer");
        }
        if (mVersionMinor < 0 || mVersionMinor > 99) {
            throw new IllegalArgumentException("Version minor = " + mVersionMinor + " Must be between 0 and 99.");
        }
        if (mVersionHotfix < 0 || mVersionHotfix > 999) {
            throw new IllegalArgumentException("Version hotfix = " + mVersionHotfix + " Must be between 0 and 999.");
        }
    }

    // If properties already confirmed we don't ask again.
    // You may override to get another behavior.
    protected void confirmPropertiesWithUserOrThrow() {
        // Current version
        String answer;
        if (sContextValues.containsKey(CONTEXT_KEY_VERSION_USER_CONFIRMATION)) {
            System.out.println(getTag() + " ==> Current version is " + getVersionName() + " (" + getVersionCode() + ") and you have already answered '" + sContextValues.get(CONTEXT_KEY_VERSION_USER_CONFIRMATION) + "' to confirm it.");
            answer = sContextValues.get(CONTEXT_KEY_VERSION_USER_CONFIRMATION);
        } else {
            answer = getUserEntry("\n" + getTag() + " ==> Current version is " + getVersionName() + " (" + getVersionCode() + ")\n" + getTag() + " ==> Enter yEs to accept this version: ");
            if (answer == null) {
                System.out.println("\n" + getTag() + " ==> Version " + getVersionName() + " (" + getVersionCode() + ") accepted by default.");
                answer = "yEs";
            }
            sContextValues.put(CONTEXT_KEY_VERSION_USER_CONFIRMATION, answer);
        }
        if (!"yEs".equals(answer)) {
            System.out.println(getTag() + " ==> User entered '" + answer + "'. Build will fail (stop).");
            throw new IllegalArgumentException("Current version " + getVersionName() + " (" + getVersionCode() + ") NOT accepted.");
        }
    }

    protected void getPropertiesFromUser() {
        if (sContextValues.containsKey(CONTEXT_KEY_STORE_PASSWORD)) {
            System.out.println(getTag() + " ==> storePassword has already queried. Using this value.");
            mStorePassword = sContextValues.get(CONTEXT_KEY_STORE_PASSWORD);
        } else {
            mStorePassword = getUserEntry("\n" + getTag() + " ==> Enter storePassword: ");
            sContextValues.put(CONTEXT_KEY_STORE_PASSWORD, mStorePassword);
        }
    }

    public int getVersionCode() {
        return 100000 * mVersionMajor + 1000 * mVersionMinor + mVersionHotfix;
    }

    public String getVersionName() {
        return mVersionMajor + "." + mVersionMinor + "." + mVersionHotfix;
    }

    public String getVersionNameForOutputFileName() {
        return mVersionMajor + "_" + mVersionMinor + "_" + mVersionHotfix;
    }

    public String getStoreFile() {
        return mStoreFile;
    }

    public String getKeyAlias() {
        return mKeyAlias;
    }

    public String getStorePassword() {
        return mStorePassword;
    }

    public String getOutputFileNameForVariant(String variantName) {
        return getPrefixForOutputFileName() + getVersionNameForOutputFileName() + "-" + variantName.toUpperCase() + getExtensionForOutputFileName();
    }

    // Implement this to define the extension for final file
    protected abstract String getExtensionForOutputFileName();

    // Implement this to define the prefix (name of product?) for final file
    protected abstract String getPrefixForOutputFileName();

    private String getUserEntry(String message) {
        String userEntry;
        try {
            userEntry = System.console().readLine(message);
        }
        catch(Exception e) {
            // We know... catching Exception is not so good,
            // but it's enough here.
            System.out.println("\n" + getTag() + " ==> When getting following user entry:" + "\n" + getTag() + " ==> ----------------------------------" + message + "\n" + getTag() + " ==> ----------------------------------" + "\n" + getTag() + " ==> This exception occurred:" + "\n" + getTag() + " ==> ----------------------------------" + "\n" + getTag() + " ==> " + e + "\n" + getTag() + " ==> ----------------------------------" + "\n" + getTag() + " ==> Null will be returned");
            return null;
        }
        return userEntry;
    }
}