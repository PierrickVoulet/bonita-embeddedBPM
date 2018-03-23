package org.bonitasoft.engine.embedded.bpm.api.impl;

import org.bonitasoft.engine.embedded.bpm.api.EmbeddedBPM;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator  {
    private ServiceRegistration registration;
    
    public void start(BundleContext context) throws Exception {
        registration = context.registerService(EmbeddedBPM.class.getName(), EmbeddedBPMImpl.getInstance(), null);
    }
    public void stop(BundleContext context) throws Exception {
        registration.unregister();
    }
}