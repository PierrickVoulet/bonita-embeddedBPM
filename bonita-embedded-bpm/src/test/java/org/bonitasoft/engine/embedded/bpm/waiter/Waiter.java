package org.bonitasoft.engine.embedded.bpm.waiter;

import java.io.Serializable;
import java.util.Date;

import org.bonitasoft.engine.session.APISession;

public abstract class Waiter {
	protected APISession apiSession = null;
	private Long limit = 2500L;
	private Long period = 500L;
	
	public Waiter(APISession apiSession) {
		this.apiSession = apiSession;
	}
	
	public Waiter(APISession apiSession, Long limit, Long period) {
		this(apiSession);
		this.limit = limit;
		this.period = period;
	}
	
	public Serializable execute() throws Exception {
		Serializable result = null;
		Long endDate = new Date(new Date().getTime() + limit).getTime();
		while(new Date().getTime() < endDate) {
			result = check();
			if(result != null) {
				if(result instanceof Boolean) {
					if((Boolean)result) {
						return result;
					}
				} else if(result instanceof Number) {
					if(((Number)result).doubleValue() > 0) {
						return result;
					}
				} else if(result instanceof String) {
					if(!((String)result).isEmpty()) {
						return result;
					}
				} else {
					return result;
				}
			}
			
			Thread.sleep(period);
		}

		return null;
	}
	
	abstract protected Serializable check() throws Exception;
}

