package br.com.arndroid.buildutils;

@SuppressWarnings("unused")
public class BuildUtils {

    public static int versionNumberForVersionName(String versionName) {
        final Version version = new Version(versionName);
        return version.versionNumber();
    }

    public static String getOutputFileNameFor(String prefix, String extension, String versionName, String variantName) {
        final Version version = new Version(versionName);
        return prefix + "-" + version.versionForOutputFileName() + "-" + variantName.toUpperCase() + extension;
    }
}
