package org.bonitasoft.engine.embedded.bpm.setup;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.Date;
import java.util.Map;

public class BonitaPlatformSetupToolProcessBuilder {
	static private String LOG_FOLDER = "logs";
	
	public static int init(String embeddedBPMWorkspacePath, String setupPath) throws IOException, InterruptedException {
		File embeddedBPMWorkspaceFile = new File(embeddedBPMWorkspacePath + File.separator + LOG_FOLDER);
		if(!embeddedBPMWorkspaceFile.exists()) {
			embeddedBPMWorkspaceFile.mkdirs();
		}
		
		return runCommand(new String[] { 
				"java",
				"-cp",
				"\"" + String.join(System.getProperty("path.separator"), new String[] { setupPath,  setupPath + File.separator + "platform_conf", setupPath + File.separator + "initial", setupPath + File.separator + "lib" + File.separator + "*"}) + "\"",
				"-Dspring.profiles.active=default",
				"-Dsysprop.bonita.db.vendor=h2",
				"org.bonitasoft.platform.setup.PlatformSetupApplication",
				"init",
				"-Dh2.noconfirm"
		}, null, new File(embeddedBPMWorkspaceFile.getAbsolutePath() + File.separator + new Date().getTime() + "_log.txt"));
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
