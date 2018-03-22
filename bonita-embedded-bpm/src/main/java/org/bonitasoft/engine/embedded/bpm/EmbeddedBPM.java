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
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class EmbeddedBPM {
	static public String EMBEDDED_BPM_SERVER_PATH = "embeddedBPM.serverPath";
	static public String EMBEDDED_BPM_SETUP_PATH = "embeddedBPM.setupPath";
	static public String EMBEDDED_BPM_CONTEXT_PATH = "embeddedBPM.contextFile";
	
	static private ConfigurableApplicationContext springContext = null;
    
    static private PlatformLoginAPI platformLoginAPI = null;
    
    static private String platformAdminUsername = "platformAdmin";
    static private String platformAdminPassword = "platform";

    static public void setPlatformAdminInformation(String username, String password) {
    	platformAdminUsername = username;
    	platformAdminPassword = password;
    }
    
	static public void start() throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException, PlatformNotFoundException, CreationException, StartNodeException, InvalidPlatformCredentialsException, PlatformLoginException, PlatformLogoutException, SessionNotFoundException, IOException, InterruptedException {
		// Initialize the Platform DB if it does not exist yet - being done in external JVM to avoid Spring context conflicts
		BonitaPlatformSetupToolProcessBuilder.init(System.getProperty(EMBEDDED_BPM_SERVER_PATH), System.getProperty(EMBEDDED_BPM_SETUP_PATH));

		if(System.getProperty(EMBEDDED_BPM_CONTEXT_PATH) != null) {
			springContext = new FileSystemXmlApplicationContext(System.getProperty(EMBEDDED_BPM_CONTEXT_PATH));
		}
		
		// Start the Platform
		platformLoginAPI = PlatformAPIAccessor.getPlatformLoginAPI();
		PlatformSession platformAdminSession = platformLoginAPI.login(platformAdminUsername, platformAdminPassword);
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
		PlatformSession platformAdminSession = platformLoginAPI.login(platformAdminUsername, platformAdminPassword);
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
