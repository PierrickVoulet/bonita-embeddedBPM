package org.bonitasoft.engine.embedded.bpm.waiter;

import java.io.Serializable;

import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceNotFoundException;
import org.bonitasoft.engine.session.APISession;

public class TaskExecutionWaiter extends Waiter {
	private Long activityInstanceId;
	
	public TaskExecutionWaiter(APISession apiSession, Long activityInstanceId) {
		super(apiSession);
		
		this.activityInstanceId = activityInstanceId;
	}

	@Override
	protected Serializable check() throws Exception {
		try {
			return TenantAPIAccessor.getProcessAPI(apiSession).getArchivedActivityInstance(activityInstanceId);
		} catch (ActivityInstanceNotFoundException e) {}
		
		return null;
	}
}
