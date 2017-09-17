package loggingService;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.xml.DOMConfigurator;

import java.util.ResourceBundle;


/**
 * This class will log message according to
 * serverity level in a log file
 *
 *
**/
public class MessageLogger {

    public static Logger LOGGER;
    String log4jFileConfigFile=this.getClass().getClassLoader().getResource("log4j.xml").getPath();
    private static String pathToRootDirectory = ResourceBundle.getBundle("config").getString("pathToRootDirectory");

    /**
     * Constructor for class Message Logger
     */
    public MessageLogger(){
        System.setProperty("rootDirectory", pathToRootDirectory);
        DOMConfigurator.configure(log4jFileConfigFile);
        LOGGER = Logger.getLogger("wikia.logger");
    }


    /**
     *
     * @param messagePriority priority message (DEBUG,INFO,WARN,ERROR,FATAL)
     * @param module name of package
     * @param className name of the class from where logging message is initiated
     * @param message log message
     */

    public void logMessage(Level messagePriority,
                           String module,
                           String className,
                           String message){



        String logMessage = module
                + " - " + className + " - "+message;




        if(messagePriority==Level.DEBUG){
          LOGGER.debug(logMessage);
        }
        else if(messagePriority==Level.INFO){
            LOGGER.info(logMessage);
        }
        else if(messagePriority==Level.WARN ){
            LOGGER.warn(logMessage);
        }
        else if(messagePriority==Level.ERROR){
            LOGGER.error(logMessage);
        }
        else if(messagePriority==Level.FATAL){
            LOGGER.fatal(logMessage);
        }
        else {
            LOGGER.info(logMessage);
        }
    }
}
