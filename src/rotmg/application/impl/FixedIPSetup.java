package rotmg.application.impl;

import rotmg.application.api.ApplicationSetup;
import rotmg.parameters.Parameters;

public class FixedIPSetup implements ApplicationSetup {

    private final String SERVER = "rotmgtesting.appspot.com";

    private final String UNENCRYPTED = "http://" + this.SERVER;

    private final String ENCRYPTED = "https://" + this.SERVER;

    private final String ANALYTICS = "UA-99999999-1";

    private final String BUILD_LABEL = "<font color=\'#9900FF\'>{IP}</font> #{VERSION}";

    private String ipAddress;

    public FixedIPSetup() {
        super();
    }

    public FixedIPSetup setAddress(String param1) {
        this.ipAddress = param1;
        return this;
    }

    @Override
    public String getAppEngineUrl(boolean param1) {
        return !!param1 ? this.UNENCRYPTED : this.ENCRYPTED;
    }

    @Override
    public String getAnalyticsCode() {
        return this.ANALYTICS;
    }

    @Override
    public boolean isServerLocal() {
        return false;
    }

    @Override
    public String getBuildLabel() {
        String loc1 = Parameters.BUILD_VERSION + "." + Parameters.MINOR_VERSION;
        return this.BUILD_LABEL.replace("{IP}", this.ipAddress).replace("{VERSION}", loc1);
    }

    @Override
    public boolean useLocalTextures() {
        return true;
    }

    @Override
    public boolean isToolingEnabled() {
        return true;
    }

    @Override
    public boolean isGameLoopMonitored() {
        return true;
    }

    @Override
    public boolean useProductionDialogs() {
        return false;
    }

    @Override
    public boolean areErrorsReported() {
        return false;
    }

    @Override
    public boolean areDeveloperHotkeysEnabled() {
        return true;
    }

    @Override
    public boolean isDebug() {
        return false;
    }

    @Override
    public String getServerDomain() {
        return this.SERVER;
    }
}