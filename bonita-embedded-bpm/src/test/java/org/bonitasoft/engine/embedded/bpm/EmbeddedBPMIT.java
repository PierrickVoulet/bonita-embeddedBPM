package org.bonitasoft.engine.embedded.bpm;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.engine.api.ApiAccessType;
import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.AlreadyExistsException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.CreationException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.platform.InvalidPlatformCredentialsException;
import org.bonitasoft.engine.platform.LoginException;
import org.bonitasoft.engine.platform.PlatformLoginException;
import org.bonitasoft.engine.platform.StopNodeException;
import org.bonitasoft.engine.platform.UnknownUserException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.util.APITypeManager;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class EmbeddedBPMIT {
	static final public String TEST_WORKSPACE = "bonita.test.workspace";
	
	static final private File WORKSPACE = new File(System.getProperty(TEST_WORKSPACE));
	
	static final private String EMBEDDED_BPM_SERVER = "server";
	static final private String EMBEDDED_BPM_SETUP = "setup";
	static final private String EMBEDDED_BPM_CONTEXT = "embeddedBPM-context.xml";

	static final private String TENANT_ADMIN_USERNAME_PASSWORD = "install";
	static final private String USER_USERNAME_PASSWORD = "User";
	
	static {
		APITypeManager.setAPITypeAndParams(ApiAccessType.LOCAL, new HashMap<String, String>());
	}
	
	@BeforeClass
	public static void buildPlatform() throws Exception {
		System.setProperty(EmbeddedBPM.EMBEDDED_BPM_SERVER_PATH, WORKSPACE + File.separator + EMBEDDED_BPM_SERVER);
		System.setProperty(EmbeddedBPM.EMBEDDED_BPM_SETUP_PATH, WORKSPACE + File.separator + EMBEDDED_BPM_SETUP);
		System.setProperty(EmbeddedBPM.EMBEDDED_BPM_CONTEXT_PATH, WORKSPACE + File.separator + EMBEDDED_BPM_CONTEXT);
		
		cleanTestWorkspace();
		
		// Copy fresh copy of resources before starting the platform for the first time
		FileUtils.copyDirectoryToDirectory(new File(EmbeddedBPMIT.class.getResource("/" + EMBEDDED_BPM_SETUP).getPath()), WORKSPACE);
		FileUtils.copyFileToDirectory(new File(EmbeddedBPMIT.class.getResource("/" + EMBEDDED_BPM_CONTEXT).getPath()), WORKSPACE);
		
		EmbeddedBPM.start();
    }
	
	@AfterClass
	public static void deletePlatform() throws StopNodeException, InvalidPlatformCredentialsException, BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException, PlatformLoginException, IOException {
		EmbeddedBPM.stop();
		
		cleanTestWorkspace();
	}
	
	private static void cleanTestWorkspace() throws IOException {
		if(WORKSPACE.exists()) {
			FileUtils.deleteDirectory(WORKSPACE);
		}
		WORKSPACE.mkdirs();
	}
	
	@Test
	public void tenantAdministratorLogin() throws UnknownUserException, BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException, LoginException {
		Assert.assertNotNull("Tenant administrator login is not valid", getTenantAdministratorSession());
	}

	
	@Test
	public void createUser() throws Exception {
		Assert.assertNotNull("Creation of user failed", createNewUser());
	}
	
	// TODO: do not work as intended as the Spring context is closed and not rebuilt completely
//	@Test
//	public void platformRestart() throws Exception {
//		Long userId = createNewUser().getId();
//		
//		EmbeddedBPM.stop();
//		EmbeddedBPM.start();
//		
//		Assert.assertNotNull("The platform has not been restarted with previous state", TenantAPIAccessor.getIdentityAPI(getTenantAdministratorSession()).getUser(userId));
//	}
	
	public APISession getTenantAdministratorSession() throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException, UnknownUserException, LoginException {
		return TenantAPIAccessor.getLoginAPI().login(TENANT_ADMIN_USERNAME_PASSWORD, TENANT_ADMIN_USERNAME_PASSWORD);
	}
	
	public User createNewUser() throws UnknownUserException, BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException, LoginException, AlreadyExistsException, CreationException {
		IdentityAPI identityAPI = TenantAPIAccessor.getIdentityAPI(getTenantAdministratorSession());
		
		String userName = USER_USERNAME_PASSWORD + "." + USER_USERNAME_PASSWORD + "." + new Date().getTime();
		return identityAPI.createUser(userName, USER_USERNAME_PASSWORD, USER_USERNAME_PASSWORD, USER_USERNAME_PASSWORD);
	}
}
