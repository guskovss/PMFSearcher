/*
 * Pipeline Magnetic Field Searcher
 */
package ru.gss.pmfsearcher;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 * @version 1.1.0 21.01.2021
 * @author Sergey Guskov
 */
public class PMFSearcherApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        show(new PMFSearcherView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     * @param root root
     */
    @Override protected void configureWindow(final java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of PMFSearcherApp
     */
    public static PMFSearcherApp getApplication() {
        return Application.getInstance(PMFSearcherApp.class);
    }

    /**
     * Main method launching the application.
     * @param args args
     */
    public static void main(final String[] args) {
        launch(PMFSearcherApp.class, args);
    }
}
