import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import db.*;
import util.MessagesBundle;

/**
 * Welcome to a Muse ment Park!
 *
 */
public class Main 
{
    public static void main( String[] args )
    {
    	final Logger logger = LogManager.getLogger("MyLog");
    	
     	MessagesBundle msgB= new MessagesBundle();
		msgB.SetLanguage("en", "US");
		System.out.println(MessagesBundle.GetResourceValue("welcome_messages"));
		logger.info(MessagesBundle.GetResourceValue("welcome_messages") + new Date());
    }
}
