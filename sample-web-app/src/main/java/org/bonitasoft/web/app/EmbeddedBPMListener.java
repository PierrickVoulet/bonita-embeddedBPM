package org.bonitasoft.web.app;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.bonitasoft.engine.embedded.bpm.EmbeddedBPM;

public class EmbeddedBPMListener implements ServletContextListener {
	static private String EMBEDDED_BPM_WEBAPP = System.getProperty("catalina.base") + "/wtpwebapps/sample-web-app/WEB-INF/classes";
	static private String EMBEDDED_BPM_WORKSPACE = System.getProperty("catalina.base") + "/bonita_embedded_engine";
	static private String EMBEDDED_BPM_SETUP = EMBEDDED_BPM_WEBAPP + "/setup";
	static private String EMBEDDED_BPM_EXTRA_CONFIG_FILE = "//embeddedBPM-cfg.xml";

	public void contextInitialized(ServletContextEvent arg0) {
		try {
			// Start the Bonita BPM Engine
			EmbeddedBPM.start(new File(EMBEDDED_BPM_WORKSPACE).getAbsolutePath(), new File(EMBEDDED_BPM_SETUP).getAbsolutePath(), new File(EMBEDDED_BPM_EXTRA_CONFIG_FILE).getAbsolutePath());
		} catch(Exception e) {
			System.out.println("EmbeddedBPM startup failure: " + e.getMessage());
		}
	}

	public void contextDestroyed(ServletContextEvent arg0) {
		try {
			// Stop the Bonita BPM Engine
			EmbeddedBPM.stop();
		} catch(Exception e) {
			System.out.println("EmbeddedBPM shutdown failure: " + e.getMessage());
		}
	}
}