package org.bonitasoft.engine.embedded.bpm.setup;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.Date;
import java.util.Map;

public class BonitaPlatformSetupToolProcessBuilder {
	static private String LOG_FOLDER = "logs";
	
	public static int init(String embeddedBPMServerPath, String embeddedBPMSetupPath) throws IOException, InterruptedException {
		File embeddedBPMServerLogs = new File(embeddedBPMServerPath + File.separator + LOG_FOLDER);
		if(!embeddedBPMServerLogs.exists()) {
			embeddedBPMServerLogs.mkdirs();
		}
		
		return runCommand(new String[] { 
				"java",
				"-cp",
				"\"" + String.join(System.getProperty("path.separator"), new String[] { embeddedBPMSetupPath,  embeddedBPMSetupPath + File.separator + "platform_conf", embeddedBPMSetupPath + File.separator + "initial", embeddedBPMSetupPath + File.separator + "lib" + File.separator + "*"}) + "\"",
				"-Dspring.profiles.active=default",
				"-Dsysprop.bonita.db.vendor=h2",
				"org.bonitasoft.platform.setup.PlatformSetupApplication",
				"init",
				"-Dh2.noconfirm"
		}, null, new File(embeddedBPMServerLogs.getAbsolutePath() + File.separator + new Date().getTime() + "_log.txt"));
	}

	private static int runCommand(String[] command, Map<String, String> environmentExtra, File logFile) throws IOException, InterruptedException {
		ProcessBuilder processBuilder = new ProcessBuilder(command);

		Map<String, String> environment = processBuilder.environment();
		if(environmentExtra != null) {
			environment.putAll(environmentExtra);
		}

		processBuilder.redirectErrorStream(true);
		processBuilder.redirectOutput(Redirect.appendTo(logFile));
		processBuilder.redirectOutput(Redirect.appendTo(logFile));
		
		Process process = processBuilder.start();
		return process.waitFor();
	}
}
