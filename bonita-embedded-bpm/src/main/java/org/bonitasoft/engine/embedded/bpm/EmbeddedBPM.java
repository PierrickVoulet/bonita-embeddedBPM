package org.bonitasoft.engine.embedded.bpm;

import java.io.IOException;

import org.bonitasoft.engine.api.PlatformAPI;
import org.bonitasoft.engine.api.PlatformAPIAccessor;
import org.bonitasoft.engine.api.PlatformLoginAPI;
import org.bonitasoft.engine.embedded.bpm.setup.BonitaPlatformSetupToolProcessBuilder;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.CreationException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.platform.InvalidPlatformCredentialsException;
import org.bonitasoft.engine.platform.PlatformLoginException;
import org.bonitasoft.engine.platform.PlatformLogoutException;
import org.bonitasoft.engine.platform.PlatformNotFoundException;
import org.bonitasoft.engine.platform.StartNodeException;
import org.bonitasoft.engine.platform.StopNodeException;
import org.bonitasoft.engine.session.PlatformSession;
import org.bonitasoft.engine.session.SessionNotFoundException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EmbeddedBPM {
	static private ConfigurableApplicationContext springContext = null;
    
    static private PlatformLoginAPI platformLoginAPI = null;

	static public void start(String embeddedBPMWorkspacePath, String setupPath, String springConfigPath) throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException, PlatformNotFoundException, CreationException, StartNodeException, InvalidPlatformCredentialsException, PlatformLoginException, PlatformLogoutException, SessionNotFoundException, IOException, InterruptedException {
		// Initialize the Platform DB if it does not exist yet - being done in external JVM to avoid Spring context conflicts
		BonitaPlatformSetupToolProcessBuilder.init(embeddedBPMWorkspacePath, setupPath);

		if(springConfigPath != null) {
			springContext = new ClassPathXmlApplicationContext(springConfigPath);
		}
		
		// Start the Platform
		platformLoginAPI = PlatformAPIAccessor.getPlatformLoginAPI();
		PlatformSession platformAdminSession = platformLoginAPI.login("platformAdmin", "platform");
		PlatformAPI platformAPI = PlatformAPIAccessor.getPlatformAPI(platformAdminSession);
		
		if(!platformAPI.isPlatformInitialized()) {
			platformAPI.initializePlatform();
		}
		if(!platformAPI.isNodeStarted()) {
			platformAPI.startNode();
		}

		// Logout any opened session
		if(platformAdminSession != null) {
			platformLoginAPI.logout(platformAdminSession);
		}
    }

    static public void stop() throws StopNodeException, BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException, InvalidPlatformCredentialsException, PlatformLoginException {
		PlatformSession platformAdminSession = platformLoginAPI.login("platformAdmin", "platform");
		PlatformAPI platformAPI = PlatformAPIAccessor.getPlatformAPI(platformAdminSession);
		
		// Stop the platform
		if(platformAPI.isNodeStarted()) {
			platformAPI.stopNode();
		}
		
		if(springContext != null) {
			springContext.close();
		}
    }
}
