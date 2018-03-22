package org.bonitasoft.engine.embedded.bpm.waiter;

import java.io.Serializable;

import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstanceNotFoundException;
import org.bonitasoft.engine.session.APISession;

public class CaseExecutionWaiter extends Waiter {
	private Long processInstanceId;
	
	public CaseExecutionWaiter(APISession apiSession, Long processInstanceId) {
		super(apiSession);
		
		this.processInstanceId = processInstanceId;
	}

	@Override
	protected Serializable check() throws Exception {
		try {
			return TenantAPIAccessor.getProcessAPI(apiSession).getArchivedProcessInstance(processInstanceId);
		} catch (ArchivedProcessInstanceNotFoundException e) {}
		
		return null;
	}
}
