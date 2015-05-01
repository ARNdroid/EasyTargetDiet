package br.com.arndroid.buildutlis;

public class AppBuildProfile extends AbstractBuildProfile {

    public AppBuildProfile(String moduleName) {
        super(moduleName);
    }

    @Override
    public String getTag() {
        return "APP";
    }

    @Override
    protected String getExtensionForOutputFileName() {
        return ".apk";
    }

    @Override
    protected String getPrefixForOutputFileName() {
        return "ETD-APP-";
    }
}