package org.bonitasoft.web.app;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.bonitasoft.engine.embedded.bpm.EmbeddedBPM;

public class EmbeddedBPMListener implements ServletContextListener {
	
	static private String EMBEDDED_BPM_SERVER = "embeddedBPMServer";
	static private String EMBEDDED_BPM_SETUP = "embeddedBPMSetup";
	static private String EMBEDDED_BPM_CONTEXT = "embeddedBPMContext";

	public void contextInitialized(ServletContextEvent servletContextEvent) {
    	System.setProperty(EmbeddedBPM.EMBEDDED_BPM_SERVER_PATH, servletContextEvent.getServletContext().getInitParameter(EMBEDDED_BPM_SERVER));
    	System.setProperty(EmbeddedBPM.EMBEDDED_BPM_SETUP_PATH, servletContextEvent.getServletContext().getInitParameter(EMBEDDED_BPM_SETUP));
    	System.setProperty(EmbeddedBPM.EMBEDDED_BPM_CONTEXT_PATH, servletContextEvent.getServletContext().getInitParameter(EMBEDDED_BPM_CONTEXT));

		try {
			// Start the Bonita BPM Engine
			EmbeddedBPM.start();
		} catch(Exception e) {
			System.out.println("EmbeddedBPM startup failure: " + e.getMessage());
		}
	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		try {
			// Stop the Bonita BPM Engine
			EmbeddedBPM.stop();
		} catch(Exception e) {
			System.out.println("EmbeddedBPM shutdown failure: " + e.getMessage());
		}
	}
}