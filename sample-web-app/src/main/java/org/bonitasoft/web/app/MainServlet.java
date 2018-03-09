package org.bonitasoft.web.app;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.engine.api.ApiAccessType;
import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.LoginAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.util.APITypeManager;

public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MainServlet() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
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
	
			response.getWriter().append("EmbeddedBPM success: " + tenantAdminSession.getUserId() + "/" + identityAPI.getUserByUserName(userName).getId());
		} catch(Exception e) {
			response.getWriter().append("EmbeddedBPM failure: " + e.getMessage());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
