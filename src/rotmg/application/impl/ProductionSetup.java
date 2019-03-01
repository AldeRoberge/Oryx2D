package rotmg.application.impl;

import rotmg.application.api.ApplicationSetup;
import rotmg.parameters.Parameters;

public class ProductionSetup implements ApplicationSetup {

	static ProductionSetup instance;
	private final String SERVER = "realmofthemadgodhrd.appspot.com";
	private final String UNENCRYPTED = "http://" + this.SERVER;
	private final String ENCRYPTED = "https://" + this.SERVER;
	private final String ANALYTICS = "UA-101960510-3";
	private final String BUILD_LABEL = "RotMG #{VERSION}.{MINOR}";

	public ProductionSetup() {
		super();
	}

	public static ProductionSetup getInstance() {
		if (instance == null) {
			instance = new ProductionSetup();
		}

		return instance;
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
	public String getBuildLabel() {
		return this.BUILD_LABEL.replace("{VERSION}", Parameters.BUILD_VERSION).replace("{MINOR}", Parameters.MINOR_VERSION);
	}

	@Override
	public boolean useLocalTextures() {
		return false;
	}

	@Override
	public boolean isToolingEnabled() {
		return false;
	}

	@Override
	public boolean isGameLoopMonitored() {
		return false;
	}

	@Override
	public boolean isServerLocal() {
		return false;
	}

	@Override
	public boolean useProductionDialogs() {
		return true;
	}

	@Override
	public boolean areErrorsReported() {
		return false;
	}

	@Override
	public boolean areDeveloperHotkeysEnabled() {
		return false;
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