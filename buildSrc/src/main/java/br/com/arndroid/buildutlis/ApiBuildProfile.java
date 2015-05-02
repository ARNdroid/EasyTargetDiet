package br.com.arndroid.buildutlis;

public class ApiBuildProfile extends AbstractBuildProfile {

    public ApiBuildProfile(String moduleName) {
        super(moduleName);
    }

    @Override
    public String getTag() {
        return "API";
    }

    @Override
    protected String getExtensionForOutputFileName() {
        return ".aar";
    }

    @Override
    protected String getPrefixForOutputFileName() {
        return "ETD-API-";
    }
}
