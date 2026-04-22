package com.ford.fdt.hmi.template;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ford.etis.runtime.apps.otx.utils.types.hmi.Screen;
import com.ford.fdt.hmi.core.application.swt.TabbedSwtApplicationView;
import com.ford.fdt.hmi.core.commands.CommandHelper;
import com.ford.otx.services.ui.command.ScreenClosed;

public class TemplateApplicationView extends TabbedSwtApplicationView
{

    /** Logger instance for this class. */
    private static final transient Logger LOG = LoggerFactory.getLogger(TemplateApplicationView.class);

    /** Singleton instance of HealthApplicationView */
    private static TemplateApplicationView instance = null;
    /** Currently open screens. */
    private ArrayList<Screen> screens = new ArrayList<>();



    /**
     * Constructor.
     *
     * @param parent  Parent SWT Component.
     */
    private TemplateApplicationView(final Composite parent)
    {
        super(parent, null);
    }

    /**
     * Creates the single instance of the genericApplicationView.
     *
     * @param parent Parent SWT component.
     * @return UltrasonicApplicationView singleton.
     */
    public static synchronized TemplateApplicationView createSingleton( final Composite parent)
    {
        if (instance == null)
        {
            LOG.info("Creating Ultrasonic Crack Test application view singleton...");
            instance = new TemplateApplicationView(parent);
        }
        return instance;
    }

    /**
     * Removes the singleton instance.
     */
    protected static synchronized void removeSingleton()
    {
        if (instance != null)
        {
            // Maybe do something.
            instance = null;
        }
    }


    /**
     * Get the singleton instance of HealthApplicationView.
     * @return UltrasonicApplicationView
     */
    public static synchronized TemplateApplicationView getSingleton()
    {
        return instance;
    }

    /**
     * Closes the target screen.
     *
     * @param targetScreen Target screen.
     */
    private void closeScreen(final Screen targetScreen)
    {
        try
        {
            CommandHelper.getCommandInvoker().invoke(new ScreenClosed(targetScreen));
        }
        catch (final Exception e)
        {
            LOG.error("Could not invoke CloseScreen command.", e);
        }
    }

    /**
     * Closes all open screen tabs.
     */
    protected void closeScreens()
    {
        // Make a copy because "screens" array list will be modified.
        final List<Screen> openScreens = new ArrayList<Screen>(screens);

        for (Screen openScreen : openScreens)
        {
            closeScreen(openScreen);
        }
    }

}