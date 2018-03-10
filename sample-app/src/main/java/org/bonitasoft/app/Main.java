package org.bonitasoft.app;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.api.ApiAccessType;
import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.LoginAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.embedded.bpm.EmbeddedBPM;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.util.APITypeManager;

public class Main {
	static private String EMBEDDED_BPM_SERVER = "C:/Users/Pierrick/Documents/US/Adoption/embeddedBPM/output/bonita-embeddedBPM/bonita_embedded_engine/server";
	static private String EMBEDDED_BPM_SETUP = "C:/Users/Pierrick/Documents/US/Adoption/embeddedBPM/output/bonita-embeddedBPM/bonita_embedded_engine/setup";
	static private String EMBEDDED_BPM_CONTEXT_PATH = "C:/Users/Pierrick/Documents/US/Adoption/embeddedBPM/output/bonita-embeddedBPM/bonita_embedded_engine/embeddedBPM-context.xml";
	
    static public void main(String[] args) throws Exception {
    	System.setProperty(EmbeddedBPM.EMBEDDED_BPM_SERVER_PATH, EMBEDDED_BPM_SERVER);
    	System.setProperty(EmbeddedBPM.EMBEDDED_BPM_SETUP_PATH, EMBEDDED_BPM_SETUP);
    	
		// Start the Bonita BPM Engine
		EmbeddedBPM.start(EMBEDDED_BPM_CONTEXT_PATH);
		
		// Make some API calls to confirm it works well
		// - Login with tenant administrator
		Map<String, String> map = new HashMap<String, String>();
		APITypeManager.setAPITypeAndParams(ApiAccessType.LOCAL, map);
		LoginAPI loginAPI = TenantAPIAccessor.getLoginAPI();
		APISession tenantAdminSession = loginAPI.login("install", "install");
		System.out.println("-------- Install User ID: " + tenantAdminSession.getUserId());
		
		// - Create a user
		IdentityAPI identityAPI = TenantAPIAccessor.getIdentityAPI(tenantAdminSession);
		String userName = "test.test" + new Date().getTime();
		identityAPI.createUser(userName, "bpm", "Test", "Test");
		System.out.println("-------- Test Test User Id: " + identityAPI.getUserByUserName(userName).getId());
		
		// Stop the Bonita BPM Engine
		EmbeddedBPM.stop();
	}
}
