/*
 * SubmarineModelApp.java
 */
package submarinemodel;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class SubmarineModelApp extends SingleFrameApplication {

    static Configuration config;
    static SubmarinePort port;
    static Sensors sensors;
    Log log;

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        show(new SubmarineModelView(this));

        Util.log.logToConsole(true);

        // shortcut
        log = Util.log;

        config = new Configuration();

        try {
            port = new SubmarinePort(config);

        } catch (PortNotFoundException e) {
            log.write("Port Not Found");
        }

        sensors = new Sensors(port);
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of SubmarineModelApp
     */
    public static SubmarineModelApp getApplication() {
        return Application.getInstance(SubmarineModelApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(SubmarineModelApp.class, args);
    }
}
