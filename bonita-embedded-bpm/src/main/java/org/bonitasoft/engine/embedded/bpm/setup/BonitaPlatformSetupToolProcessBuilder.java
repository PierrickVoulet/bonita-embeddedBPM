package org.bonitasoft.engine.embedded.bpm.setup;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.Map;

public class BonitaPlatformSetupToolProcessBuilder {
	public static int init(String setupPath) throws IOException, InterruptedException {
		return runCommand(new String[] { 
				/*System.getProperty("java.home") + File.separator + "bin" + File.separator + */"java",
				"-cp",
				"\"" + String.join(System.getProperty("path.separator"), new String[] { setupPath,  setupPath + File.separator + "platform_conf", setupPath + File.separator + "initial", setupPath + File.separator + "lib" + File.separator + "*"}) + "\"",
				"-Dspring.profiles.active=default",
				"-Dsysprop.bonita.db.vendor=h2",
				"org.bonitasoft.platform.setup.PlatformSetupApplication",
				"init",
				"-Dh2.noconfirm"
		}, null);
	}

	private static int runCommand(String[] command, Map<String, String> environmentExtra) throws IOException, InterruptedException {
		ProcessBuilder processBuilder = new ProcessBuilder(command);

		Map<String, String> environment = processBuilder.environment();
		if(environmentExtra != null) {
			environment.putAll(environmentExtra);
		}

		File log = new File("./log.txt");
		processBuilder.redirectErrorStream(true);
		processBuilder.redirectOutput(Redirect.appendTo(log));
		processBuilder.redirectOutput(Redirect.appendTo(log));
		
		Process process = processBuilder.start();
		return process.waitFor();
	}
}
