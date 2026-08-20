package com.ford.fdt.hmi.template;

import com.ford.otx.osgi.utils.async.AsyncBundleActivator;
import com.ford.otx.osgi.utils.async.Initializer;
import com.ford.otx.osgi.utils.async.ServiceWaitCallback;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Activator.
 *
 * @author srd5kor11
 */
public class Activator extends AsyncBundleActivator
{
    /** Logger instance for this class. */
    private static final transient Logger LOG = LoggerFactory.getLogger(Activator.class);

    @Override
    public void startBundle(final BundleContext bundleContext, final Initializer initializer) throws Exception 
    {
        initializer.waitForServices(new ServiceWaitCallbackImpl());
    }

    /**
     * Callback for waiting for OSGi services
     */
    private static final class ServiceWaitCallbackImpl implements ServiceWaitCallback
    {
        @Override
        public void servicesAvailable(final Map<Class, Object> services, final BundleContext context) throws Exception
        {
            LOG.info("{}: start", Activator.class.getName());
            LOG.info("");
        }
    }

    @Override
    public void stop(final BundleContext context) throws Exception
    {
        LOG.info("{}: stop", Activator.class.getName());

    }
}