package org.bonitasoft.engine.embedded.bpm;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.engine.api.ApiAccessType;
import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.flownode.ArchivedActivityInstance;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.bpm.process.ActivationState;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstance;
import org.bonitasoft.engine.bpm.process.DesignProcessDefinition;
import org.bonitasoft.engine.bpm.process.InvalidProcessDefinitionException;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.bpm.process.impl.ProcessDefinitionBuilder;
import org.bonitasoft.engine.embedded.bpm.waiter.CaseExecutionWaiter;
import org.bonitasoft.engine.embedded.bpm.waiter.TaskExecutionWaiter;
import org.bonitasoft.engine.embedded.bpm.waiter.TaskPendingWaiter;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.platform.InvalidPlatformCredentialsException;
import org.bonitasoft.engine.platform.PlatformLoginException;
import org.bonitasoft.engine.platform.StopNodeException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.util.APITypeManager;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class EmbeddedBPMIT {
	static final public String TEST_WORKSPACE = "bonita.test.workspace";
	
	static final private File WORKSPACE = new File(System.getProperty(TEST_WORKSPACE));
	
	static final private String EMBEDDED_BPM_SERVER = "server";
	static final private String EMBEDDED_BPM_SETUP = "setup";
	static final private String EMBEDDED_BPM_CONTEXT = "embeddedBPM-context.xml";

	// Constants used by the tests
	
    private static final String PLATFORM_USER_NAME = "platformAdmin";
    private static final String PLATFORM_PASSWORD = "platform";
    private static final String TECHNICAL_USER_NAME = "install";
    private static final String TECHNICAL_PASSWORD = "install";
    private static final String USER_NAME = "walter.bates";
    private static final String USER_PASSWORD = "bpm";
    
    private static final String PROCESS_NAME = "Process";
    private static final String PROCESS_VERSION = "1.0";
    private static final String ACTOR_NAME = "Actor";
    private static final String START_EVENT_NAME = "Start";
    private static final String USER_TASK_NAME = "UserTask";
    private static final String AUTO_TASK_NAME = "AutoTask";
    private static final String END_EVENT_NAME = "End";
	
	@BeforeClass
	public static void buildPlatform() throws Exception {
		System.setProperty(EmbeddedBPM.EMBEDDED_BPM_SERVER_PATH, WORKSPACE + File.separator + EMBEDDED_BPM_SERVER);
		System.setProperty(EmbeddedBPM.EMBEDDED_BPM_SETUP_PATH, WORKSPACE + File.separator + EMBEDDED_BPM_SETUP);
		System.setProperty(EmbeddedBPM.EMBEDDED_BPM_CONTEXT_PATH, WORKSPACE + File.separator + EMBEDDED_BPM_CONTEXT);
		
		EmbeddedBPM.setPlatformAdminInformation(PLATFORM_USER_NAME, PLATFORM_PASSWORD);
		
		APITypeManager.setAPITypeAndParams(ApiAccessType.LOCAL, new HashMap<String, String>());
		
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
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void scenario() throws Exception {
        thrown.expect(ProcessDefinitionNotFoundException.class);
        
		APISession tenantAdminSession = TenantAPIAccessor.getLoginAPI().login(TECHNICAL_USER_NAME, TECHNICAL_PASSWORD);
        try {
        	// Create user
        	Assert.assertNotNull("Tenant administrator login", tenantAdminSession);
			IdentityAPI identityAPI = TenantAPIAccessor.getIdentityAPI(tenantAdminSession);
			Assert.assertNotNull("User creation", identityAPI.createUser(USER_NAME, USER_PASSWORD));

			APISession tenantUserSession = TenantAPIAccessor.getLoginAPI().login(USER_NAME, USER_PASSWORD);
	        try {
	        	// Deploy the process definition
	    		ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(tenantUserSession);
	            ProcessDefinition processDefinition = processAPI.deploy(buildProcessDefinition());
	            Assert.assertNotNull("Process definition creation", processDefinition);
	            
	            // Configure the process definition
	            Assert.assertNotNull("Process definition configuration", processAPI.addUserToActor(ACTOR_NAME, processDefinition, tenantUserSession.getUserId()));
	            
	            // Enable the process definition
	            processAPI.enableProcess(processDefinition.getId());
	            Assert.assertTrue("Process definition enabling", processAPI.getProcessDeploymentInfo(processDefinition.getId()).getActivationState() == ActivationState.ENABLED);
	            
	            // Start case
	            ProcessInstance processInstance = processAPI.startProcess(processDefinition.getId());
	    		Assert.assertNotNull("Process instance creation", processInstance);
	    		
	    		// Wait for the user task to be pending
	    		HumanTaskInstance humanTaskInstance = (HumanTaskInstance) new TaskPendingWaiter(tenantUserSession).execute();
	    		Assert.assertNotNull("Wait for the next pending task", humanTaskInstance);

	    		// Execute the pending user task
	    		ArchivedActivityInstance archivedActivityInstance = (ArchivedActivityInstance) new TaskExecutionWaiter(tenantUserSession, humanTaskInstance.getId()).execute();
	    		Assert.assertNotNull("Execute the pending task", archivedActivityInstance);
	    		
	    		// Wait for the case to be archived
	    		ArchivedProcessInstance archivedProcessInstance = (ArchivedProcessInstance) new CaseExecutionWaiter(tenantUserSession, processInstance.getId()).execute();
	    		Assert.assertNotNull("Case archiving", archivedProcessInstance);
	    		
	    		// Delete the archived case
	    		Assert.assertTrue("Remove the archived process instance", processAPI.deleteArchivedProcessInstances(processDefinition.getId(), 0, 1) == 1);
	    		
	    		// Disable the process
	            processAPI.disableProcess(processAPI.getProcessDeploymentInfo(processDefinition.getId()).getId());
	            Assert.assertTrue(processAPI.getProcessDeploymentInfo(processDefinition.getId()).getActivationState() == ActivationState.DISABLED);
	    		
	    		// Delete the process definition
	            processAPI.deleteProcessDefinition(processDefinition.getId());
	            processAPI.getProcessDefinition(processDefinition.getId());
	        } finally {
	    		TenantAPIAccessor.getLoginAPI().logout(tenantUserSession);
	        }
        } finally {
			TenantAPIAccessor.getLoginAPI().logout(tenantAdminSession);
        }
	}
	
	private static DesignProcessDefinition buildProcessDefinition() throws InvalidProcessDefinitionException {
        ProcessDefinitionBuilder processDefinitionBuilder = new ProcessDefinitionBuilder().createNewInstance(PROCESS_NAME, PROCESS_VERSION);
        
        // Actor
        processDefinitionBuilder.addActor(ACTOR_NAME, true);
        
        // Steps
        processDefinitionBuilder.addStartEvent(START_EVENT_NAME);
        processDefinitionBuilder.addUserTask(USER_TASK_NAME, ACTOR_NAME);
        processDefinitionBuilder.addAutomaticTask(AUTO_TASK_NAME);
        processDefinitionBuilder.addEndEvent(END_EVENT_NAME);
        
        // Transitions
        processDefinitionBuilder.addTransition(START_EVENT_NAME, USER_TASK_NAME);
        processDefinitionBuilder.addTransition(USER_TASK_NAME, AUTO_TASK_NAME);
        processDefinitionBuilder.addTransition(AUTO_TASK_NAME, END_EVENT_NAME);

        return processDefinitionBuilder.done();
    }
}
