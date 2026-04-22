package com.ford.fdt.hmi.template;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ford.fdt.hmi.core.HmiThread;
import com.ford.fdt.hmi.core.application.HmiApplicationView;
import com.ford.fdt.hmi.core.application.bootstrap.FdtApplicationBootstrap;
import com.ford.fdt.hmi.core.application.bootstrap.FdtApplicationBootstrapEvent;
import com.ford.fdt.hmi.core.application.bootstrap.FdtApplicationBootstrapListener;
import com.ford.fdt.hmi.core.application.swt.SwtApplication;
import com.ford.fdt.hmi.core.batonmanager.command.RegisterToolChangeListener;
import com.ford.fdt.hmi.core.batonmanager.command.ToolChangeListener;
import com.ford.fdt.hmi.core.commands.CommandHelper;
import com.ford.otx.command.invoker.CommandInvoker;
import com.ford.otx.command.invoker.CommandInvokerException;

public class TemplateHmiApplication extends SwtApplication implements ToolChangeListener
{

    /** ApplicationView */
    private TemplateApplicationView templateApplicationView;
    /** fdtApplicationBootstrapListener */
    private FdtApplicationBootstrapListener fdtApplicationBootstrapListener;
    /** Logger instance for this class. */
    private static final transient Logger LOG = LoggerFactory.getLogger(TemplateHmiApplication.class);
    /** Application ended flag. */
    private boolean hostEnded = false;

    @Override
    public HmiApplicationView createView(final Composite parent)
    {
        templateApplicationView = TemplateApplicationView.createSingleton(parent);
        templateApplicationView.setHmiApplication(this);
        return templateApplicationView;
    }


    @Override
    public void toolChangeNotification(final String strTool)
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void toolReleaseResourceNotification(final String strTool)
    {
        // TODO Auto-generated method stub

    }


    @Override
    public boolean onStart()
    {
        // Register the custom browser functions here.
        final FdtApplicationBootstrap fdtApplicationBootstrap = this.getFdtApplicationBootstrap();
        if (fdtApplicationBootstrap != null)
        {
            fdtApplicationBootstrapListener = new FdtApplicationBootstrapListener()
            {
                @Override
                public void onFdtApplicationStarted(final FdtApplicationBootstrapEvent e)
                {
                    LOG.info("Calling Transmission clutch ApplicationView");
                    new HmiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {

                        }
                    }).start();
                    registerToolChangeListener();
                }


                @Override
                public void onFdtApplicationEnded(final FdtApplicationBootstrapEvent e)
                {
                    LOG.info("Calling closeApplicationView");
                    closeApplicationView();
                }
            };
            fdtApplicationBootstrap.addFdtApplicationBootstrapListener(fdtApplicationBootstrapListener);
            fdtApplicationBootstrap.start();
        }
        return true;
    }


    /**
        * Register for the Tool Change listener.
        */
    private void registerToolChangeListener()
    {
        try
        {
            final CommandInvoker cmdInvoker = CommandHelper.getCommandInvoker();
            if (cmdInvoker != null)
            {
                cmdInvoker.invoke(new RegisterToolChangeListener(this));
            }
        }
        catch (final CommandInvokerException e)
        {
            LOG.error("Failed to call RegisterToolChangeListener!", e);
        }
    }


    /**
     * Closes the application view.
     */
    public void closeApplicationView()
    {
        LOG.info("Calling closeApplicationView");
        final Display display = Display.getDefault();
        if (display != null && !display.isDisposed())
        {
            // Refresh the GUI.
            display.asyncExec(new Runnable()
            {
                @Override
                public void run()
                {
                    final HmiApplicationView hmiApplicationView = getHmiApplicationView();
                    if (hmiApplicationView != null && !hmiApplicationView.isDisposed() && !hostEnded)
                    {
                        LOG.info("Calling closeApplication");
                        getHmiApplicationManager().closeApplication(TemplateHmiApplication.this);
                        setHmiApplicationView(null);
                        templateApplicationView = null;
                    }
                }
            });
        }
    }


    public boolean onEnd()
    {
        LOG.info("onEnd");
        templateApplicationView.closeScreens();
        getHmiApplicationManager().removeAppExclusive();
        final FdtApplicationBootstrap fdtApplicationBootstrap = this.getFdtApplicationBootstrap();
        if (fdtApplicationBootstrap != null)
        {
            fdtApplicationBootstrap.stop();
            fdtApplicationBootstrap.removeFdtApplicationBootstrapListener(fdtApplicationBootstrapListener);
        }
        hostEnded = true;
        templateApplicationView.setHmiApplication(null);
        setFdtApplicationBootstrap(null);
        templateApplicationView.removeSingleton();
        return true;
    }

}