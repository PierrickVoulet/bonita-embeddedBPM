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
	static public void main(String[] args) throws Exception {
		// Start the Bonita BPM Engine
		EmbeddedBPM.start();
		
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
