package br.com.arndroid.buildutlis;

public class ShrBuildProfile extends AbstractBuildProfile {

    public ShrBuildProfile(String moduleName) {
        super(moduleName);
    }

    @Override
    public String getTag() {
        return "SHR";
    }

    @Override
    protected String getExtensionForOutputFileName() {
        return ".aar";
    }

    @Override
    protected String getPrefixForOutputFileName() {
        return "ETD-SHR-";
    }
}
