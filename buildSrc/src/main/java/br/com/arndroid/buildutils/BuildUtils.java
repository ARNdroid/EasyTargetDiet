package br.com.arndroid.buildutils;

@SuppressWarnings("unused")
public class BuildUtils {

    public static int versionCodeForVersionName(String versionName) {
        final Version version = new Version(versionName);
        return version.versionNumber();
    }

    public static String getOutputFileNameFor(String prefix, String extension, String versionName, String variantName) {
        final Version version = new Version(versionName);
        return prefix + "-" + version.versionForOutputFileName() + "-" + variantName.toUpperCase() + extension;
    }

    public static String buildSummaryFor(String module, String versionName, int versionCode, Throwable failure) {
        final String doubleDashLine  = "\n===========================================";
        final String titleLine       = "\n               BUILD SUMMARY";
        final String singleDashLine  = "\n-------------------------------------------";
        final String moduleLine      = "\n Module       :  %s";
        final String versionNameLine = "\n Version name :  %s";
        final String versionCodeLine = "\n Version code :  %d";
        final String failureLine     = "\n Failure      :  %s";

        @SuppressWarnings("StringBufferReplaceableByString")
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(doubleDashLine);
        stringBuilder.append(titleLine);
        stringBuilder.append(singleDashLine);
        stringBuilder.append(String.format(moduleLine, module));
        stringBuilder.append(String.format(versionNameLine, versionName));
        stringBuilder.append(String.format(versionCodeLine, versionCode));
        stringBuilder.append(String.format(failureLine, failure == null ? "NONE" : failure.toString()));
        stringBuilder.append(doubleDashLine);

        return stringBuilder.toString();
    }
}
