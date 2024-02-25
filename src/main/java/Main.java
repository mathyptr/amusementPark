import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
import domainModel.Attraction;
import domainModel.Customer;
import util.MessagesBundle;

/**
 * Welcome to a Muse ment Park!
 *
 */
public class Main 
{
	private static MessagesBundle msgB = MessagesBundle.getInstance();
	
	public static void SplashScreen() {
		logger.info(msgB.GetResourceValue("welcome_messages_p0"));
		logger.info(msgB.GetResourceValue("welcome_messages_p1"));	
		logger.info(msgB.GetResourceValue("welcome_messages_p2"));	
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
	private static void Customer() throws Exception 
	{	    
		try {
			dbManager.getConnection();
		} catch (SQLException e) {
			logger.error(e.getMessage());            
		}
		MembershipDAO membershipDAO = new SqlMembershipDAO();
		EmployeeDAO employeeDAO= new SqlEmployeeDAO();      
		CustomerDAO customerDAO = new SqlCustomerDAO(membershipDAO);        
		AttractionDAO attractionDAO = new SqlAttractionDAO(employeeDAO, customerDAO);        

		CustomersController customersController = new CustomersController(customerDAO);
		EmployeesController employeesController = new EmployeesController(employeeDAO);        
		AttractionsController attractionController = new AttractionsController(employeesController, attractionDAO);
		BookingsController bookingsController = new BookingsController(attractionController, customersController, attractionDAO, membershipDAO);
		String customerCF="MRRSML01";
		logger.info(msgB.GetResourceValue("client_messages_p0")+customersController.getPerson(customerCF).getName());
		logger.info(msgB.GetResourceValue("client_messages_p1"));
		
		List<Attraction> listAttract = attractionController.getAll();
		int nmaxAttraction=0;
        for (Attraction attract : listAttract) {
        	logger.info(attract.getId()+": "+attract.getName());
        	nmaxAttraction++;
        }
        
		logger.info(msgB.GetResourceValue("client_messages_p2"));        
		bookingsController.bookAttraction(customerCF, listAttract.get(0).getId());
		bookingsController.bookAttraction("MRRSML02", listAttract.get(1).getId());
	
		logger.info(msgB.GetResourceValue("client_messages_p3"));
	}

	private static void Employee() throws Exception 
	{	    
		try {
			dbManager.getConnection();
		} catch (SQLException e) {
			logger.error(e.getMessage());            
		}
		MembershipDAO membershipDAO = new SqlMembershipDAO();
		EmployeeDAO employeeDAO= new SqlEmployeeDAO();      
		CustomerDAO customerDAO = new SqlCustomerDAO(membershipDAO);        
		AttractionDAO attractionDAO = new SqlAttractionDAO(employeeDAO, customerDAO);        

		CustomersController customersController = new CustomersController(customerDAO);
		EmployeesController employeesController = new EmployeesController(employeeDAO);        
		AttractionsController attractionController = new AttractionsController(employeesController, attractionDAO);

		String employeeCF="PTRMTH01";
		logger.info(msgB.GetResourceValue("employee_messages_p0")+employeesController.getPerson(employeeCF).getName());
		logger.info(msgB.GetResourceValue("employee_messages_p1"));

		List<Attraction> listAttract = attractionController.getAll();
		int nmaxAttraction=0;
        for (Attraction attract : listAttract) {        	
    		logger.info(attract.getId()+": "+attract.getName());
        	if(attract.getEmployeeFiscalCode().equals(employeeCF))
        		logger.info(msgB.GetResourceValue("employee_messages_p2"));
        	nmaxAttraction++;
        }
             
		logger.info(msgB.GetResourceValue("employee_messages_p3"));
	}
		
	
	
	private static void Manager() throws Exception 
	{	    
		try {
			dbManager.getConnection();
		} catch (SQLException e) {
			logger.error(e.getMessage());            
		}
		
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
		int attraction1=attractionController.addAttraction("Pressure", 10, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "PTRMTH01","Pressure");
		int attraction2=attractionController.addAttraction("Supremacy", 10, LocalDateTime.now(), LocalDateTime.now().plusHours(6), "PTRMTH02","Supremacy");
		int attraction3=attractionController.addAttraction("Uprising", 10,  LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(44), "PTRMTH01","Uprising");
		customersController.addPerson( "Samu","Marr", "MRRSML01", new String[]{"workdays","silver"}, LocalDate.now().plusYears(1));
		customersController.addPerson( "Sam","Mar", "MRRSML02", new String[]{"workdays"}, LocalDate.now().plusYears(1));
       	
		logger.info(customersController.getPerson("MRRSML01"));	
 }	
	
	private static final Logger logger = LogManager.getLogger("Main");
    public static void main( String[] args ) throws Exception
    {
		dbManager.setDatabase("amusepark.db");
		msgB.SetLanguage("it", "IT");    	
        SplashScreen();	       
        cleanDB();		
        Manager();
        Employee();
        Customer();
    }
    
}
