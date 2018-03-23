package org.bonitasoft.engine.embedded.bpm.api;

import java.io.IOException;

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
import org.bonitasoft.engine.session.SessionNotFoundException;

public interface EmbeddedBPM {
	final static public String EMBEDDED_BPM_SERVER_PATH = "embeddedBPM.serverPath";
	final static public String EMBEDDED_BPM_SETUP_PATH = "embeddedBPM.setupPath";
	final static public String EMBEDDED_BPM_CONTEXT_PATH = "embeddedBPM.contextFile";
	
	public void setPlatformAdminInformation(String username, String password);
	
	public void start() throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException, PlatformNotFoundException, CreationException, StartNodeException, InvalidPlatformCredentialsException, PlatformLoginException, PlatformLogoutException, SessionNotFoundException, IOException, InterruptedException;
	
	public void stop() throws StopNodeException, BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException, InvalidPlatformCredentialsException, PlatformLoginException;
}
