import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.AttractionDAO;
import dao.CustomerDAO;
import dao.MembershipDAO;
import dao.SqlMembershipDAO;
import dao.SqlAttractionDAO;
import dao.SqlCustomerDAO;
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

        MembershipDAO membershipDAO = new SqlMembershipDAO();
        
        AttractionDAO attractionDAO = new SqlAttractionDAO();
        
        CustomerDAO customerDAO = new SqlCustomerDAO(membershipDAO);        

/*
        String s;
        DateTimeFormatter df1 = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ITALIAN);        
        LocalDate date = LocalDate.parse("01/11/2023", df1);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.ITALIAN);
        LocalDateTime dateTime = LocalDateTime.parse("01/11/2023 00:00", df);
*/

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String caseStartDate = dateFormat.format(LocalDateTime.now());
        System.out.println(caseStartDate);
        LocalDateTime localdatetime = LocalDateTime.parse(caseStartDate, dateFormat);
        System.out.println(localdatetime);
        		
        int nattraction=0;
        try {
        	nattraction=attractionDAO.getNextID();
		} catch (Exception e) {
			logger.error(e.getMessage());  
		}
        logger.info("n attraction: "+Integer.toString(nattraction));
     	MessagesBundle msgB= new MessagesBundle();
		msgB.SetLanguage("en", "US");	
		logger.info(MessagesBundle.GetResourceValue("welcome_messages"));
    }
}
