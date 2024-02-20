import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import businessLogic.AttractionsController;
import businessLogic.BookingsController;
import businessLogic.CustomersController;
import businessLogic.EmployeesController;
import dao.AttractionDAO;
import dao.CustomerDAO;
import dao.EmployeeDAO;
import dao.MembershipDAO;
import dao.SqlMembershipDAO;
import dao.SqlAttractionDAO;
import dao.SqlCustomerDAO;
import dao.SqlEmployeeDAO;
import dao.dbManager;
import domainModel.Customer;
import util.MessagesBundle;

/**
 * Welcome to a Muse ment Park!
 *
 */
public class Main 
{
	public static void SplashScreen() {
		logger.info(MessagesBundle.GetResourceValue("welcome_messages_p0"));
		logger.info(MessagesBundle.GetResourceValue("welcome_messages_p1"));	
		logger.info(MessagesBundle.GetResourceValue("welcome_messages_p2"));	
	}

	
	private static void cleanDB() throws Exception 
	{
	   Connection connection = dbManager.getConnection();
	   connection.prepareStatement("DELETE FROM memberships").executeUpdate();
         // Clear the "memberships_extensions" table        
       connection.prepareStatement("DELETE FROM memberships_extensions").executeUpdate();

         // Clear the "customers" table
       connection.prepareStatement("DELETE FROM customers").executeUpdate();
         
         // Clear the "attractions" table
       connection.prepareStatement("DELETE FROM attractions").executeUpdate();
         
         // Clear the "employees" table
       connection.prepareStatement("DELETE FROM employees").executeUpdate(); 

       // Clear the "employees" table
     connection.prepareStatement("DELETE FROM bookings").executeUpdate(); 
       
       // Reset autoincrement counters
       connection.prepareStatement("DELETE FROM sqlite_sequence").executeUpdate();
	}
	
	private static final Logger logger = LogManager.getLogger("Main");
    public static void main( String[] args ) throws Exception
    {
        dbManager.setDatabase("amusepark.db");
        MessagesBundle.SetLanguage("it", "IT");
        try {
        	dbManager.getConnection();
        } catch (SQLException e) {
    		logger.error(e.getMessage());            
        }
        
        MessagesBundle msgB = MessagesBundle.getInstance();     
        
        SplashScreen();	
        
        cleanDB();
        MembershipDAO membershipDAO = new SqlMembershipDAO();
        EmployeeDAO employeeDAO= new SqlEmployeeDAO();      
        CustomerDAO customerDAO = new SqlCustomerDAO(membershipDAO);        
        AttractionDAO attractionDAO = new SqlAttractionDAO(employeeDAO, customerDAO);        

        CustomersController customersController = new CustomersController(customerDAO);
        EmployeesController employeesController = new EmployeesController(employeeDAO);        
        AttractionsController attractionController = new AttractionsController(employeesController, attractionDAO);
        BookingsController bookingsController = new BookingsController(attractionController, customersController, attractionDAO, membershipDAO);

        employeesController.addPerson("Mathy", "Pat", "PTRMTH01", 1150);
        employeesController.addPerson("Mat", "Pa", "PTRMTH02", 1250);

        // Add an attraction
        int attraction1=attractionController.addAttraction("Pressure", 10, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), "PTRMTH01");
        int attraction2=attractionController.addAttraction("Supremacy", 10, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(6), "PTRMTH02");
       
        customersController.addPerson( "Samu","Marr", "MRRSML01", new String[]{"workdays","silver"}, LocalDate.now().plusYears(1));
        customersController.addPerson( "Sam","Mar", "MRRSML02", new String[]{"workdays"}, LocalDate.now().plusYears(1));
         
        bookingsController.bookAttraction("MRRSML01", attraction1);
        bookingsController.bookAttraction("MRRSML02", attraction2);       
        
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        		
        int nattraction=0;
        try {
        	nattraction=attractionDAO.getNextID();
		} catch (Exception e) {
			logger.error(e.getMessage());  
		}
        logger.info("n attraction: "+Integer.toString(nattraction));
       	
		logger.info(customersController.getPerson("MRRSML01").getMembership().getUses());
		logger.info(customersController.getPerson("MRRSML01").getMembership().getUsesDescription());
		logger.info(customersController.getPerson("MRRSML01"));	
		
    }
}
