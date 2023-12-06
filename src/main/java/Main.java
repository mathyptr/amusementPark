import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.AttractionDAO;
import db.dbManager;

import util.MessagesBundle;

/**
 * Welcome to a Muse ment Park!
 *
 */
public class Main 
{
	private static final Logger logger = LogManager.getLogger("Main.class");
    public static void main( String[] args )
    {
        dbManager.setDatabase("amusepark.db");
        try {
        	dbManager.getConnection();
        } catch (SQLException e) {
    		logger.error(e.getMessage());            
        }
        
        AttractionDAO attractionDAO;
     	MessagesBundle msgB= new MessagesBundle();
		msgB.SetLanguage("en", "US");	
		logger.info(MessagesBundle.GetResourceValue("welcome_messages"));
    }
}
