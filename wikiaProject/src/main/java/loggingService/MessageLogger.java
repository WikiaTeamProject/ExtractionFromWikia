package loggingService;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * This class will log message according to
 * serverity level in a log file
 */
public class MessageLogger {

    public static final Logger LOGGER = Logger.getLogger("fileAppender");


    public void logMessage(Priority messagePriority,
                           String module,
                           String className,
                           String message){


        DOMConfigurator.configure("wikiaProject/src/main/resources/log4j.xml");

        String logMessage = messagePriority.toString() + " - " + module
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
