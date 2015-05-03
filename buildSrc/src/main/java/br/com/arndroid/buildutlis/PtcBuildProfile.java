package br.com.arndroid.buildutlis;

public class PtcBuildProfile extends AbstractBuildProfile {

    public PtcBuildProfile(String moduleName) {
        super(moduleName);
    }

    @Override
    public String getTag() {
        return "PTC";
    }

    @Override
    protected String getExtensionForOutputFileName() {
        return ".apk";
    }

    @Override
    protected String getPrefixForOutputFileName() {
        return "ETD-PTC-";
    }
}
