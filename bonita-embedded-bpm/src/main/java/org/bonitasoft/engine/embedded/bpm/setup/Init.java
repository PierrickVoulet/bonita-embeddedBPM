package org.bonitasoft.engine.embedded.bpm.setup;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.bonitasoft.platform.exception.PlatformException;
import org.bonitasoft.platform.setup.PlatformSetup;
import org.bonitasoft.platform.setup.PlatformSetupApplication;

public class Init {
	static public void main(String[] args) throws PlatformException, FileNotFoundException, IOException {
		System.setProperty("spring.profiles.active", (args.length > 0 ? args[0] : "default"));
		
		if(args.length > 1) {
			System.setProperty(PlatformSetup.BONITA_SETUP_FOLDER, args[1]);

			// Supposed to aggregate command line arguments based on properties files instead of adding into system properties directly
/*			Properties properties = new Properties();
			properties.load(new FileInputStream(args[1] + File.separator + "database.properties"));
			properties.load(new FileInputStream(args[1] + File.separator + "internal.properties"));
			for(Object key : properties.keySet()) {
				if(key != null && properties.get(key) != null) {
					System.setProperty(key.toString(), properties.get(key).toString());
				}
			}*/
		}

		PlatformSetupApplication.getPlatformSetup(args).init();
	}
}