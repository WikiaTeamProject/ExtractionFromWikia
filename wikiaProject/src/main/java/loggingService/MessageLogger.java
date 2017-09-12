


package loggingService;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.xml.DOMConfigurator;



/**
 * This class will log message according to
 * serverity level in a log file
 *
 *
**/
public class MessageLogger {

    public static final Logger LOGGER = Logger.getLogger("fileAppender");


/**
     *
     * @param messagePriority priority message (DEBUG,INFO,WARN,ERROR,FATAL)
     * @param module name of package
     * @param className name of the class from where logging message is initiated
     * @param message log message
     */

    public void logMessage(Priority messagePriority,
                           String module,
                           String className,
                           String message){


        String log4jFileConfigFile=this.getClass().getClassLoader().getResource("log4j.xml").getPath();


        DOMConfigurator.configure(log4jFileConfigFile);


        String logMessage = module
                + " - " + className + " - "+message;


        if(messagePriority==Priority.DEBUG){
          LOGGER.debug(logMessage);
        }
        else if(messagePriority==Priority.INFO){
            LOGGER.info(logMessage);
        }
        else if(messagePriority==Priority.WARN ){
            LOGGER.warn(logMessage);
        }
        else if(messagePriority==Priority.ERROR){
            LOGGER.error(logMessage);
        }
        else if(messagePriority==Priority.FATAL){
            LOGGER.fatal(logMessage);
        }
        else {
            LOGGER.info(logMessage);
        }
    }
}
