package org.bonitasoft.engine.embedded.bpm.setup;

import org.bonitasoft.platform.exception.PlatformException;
import org.bonitasoft.platform.setup.PlatformSetup;
import org.bonitasoft.platform.setup.PlatformSetupApplication;

public class Init {
	static public void main(String[] args) throws PlatformException {
		System.setProperty("spring.profiles.active", "default");
		System.setProperty(PlatformSetup.BONITA_SETUP_FOLDER, ".\\resources\\setup");

		PlatformSetupApplication.getPlatformSetup(args).init();
	}
}