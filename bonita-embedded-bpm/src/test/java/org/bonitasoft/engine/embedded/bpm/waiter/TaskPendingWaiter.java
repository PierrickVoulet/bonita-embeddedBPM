package org.bonitasoft.engine.embedded.bpm.waiter;

import java.io.Serializable;
import java.util.List;

import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.session.APISession;

public class TaskPendingWaiter extends Waiter {
	public TaskPendingWaiter(APISession apiSession) {
		super(apiSession);
	}

	@Override
	protected Serializable check() throws Exception {
		List<HumanTaskInstance> humanTaskInstances = TenantAPIAccessor.getProcessAPI(apiSession).getPendingHumanTaskInstances(apiSession.getUserId(), 0, 1, ActivityInstanceCriterion.DEFAULT);
		if(!humanTaskInstances.isEmpty()) {
			return humanTaskInstances.get(0);
		}
		
		return null;
	}
}
