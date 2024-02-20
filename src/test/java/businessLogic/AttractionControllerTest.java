package businessLogic;

import dao.*;
import util.MessagesBundle;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;


class AttractionControllerTest {

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up database
    	dbManager.setDatabase("amusementParkTest.db");
        // Set up language    	
        MessagesBundle.SetLanguage("it", "IT");
    }

    @BeforeEach
    public void init() throws SQLException, IOException {
        Connection connection = dbManager.getConnection();
        // Set up database
    	dbManager.setDatabase("amusementParkTest.db");
        // Clear the "memberships" table        
        connection.prepareStatement("DELETE FROM memberships").executeUpdate();
        // Clear the "memberships_extensions" table        
        connection.prepareStatement("DELETE FROM memberships_extensions").executeUpdate();

        // Clear the "customers" table
        connection.prepareStatement("DELETE FROM customers").executeUpdate();
        
        // Clear the "attractions" table
        connection.prepareStatement("DELETE FROM attractions").executeUpdate();
        
        // Clear the "employees" table
        connection.prepareStatement("DELETE FROM employees").executeUpdate();        
}    
    
    
    @Test
    void When_AddingCourse_With_OccupiedEmployee_Expect_IllegalArgumentException() throws Exception {
        // Create DAOs
        SqlCustomerDAO customerDAO = new SqlCustomerDAO(new SqlMembershipDAO());
        EmployeeDAO employeeDAO = new SqlEmployeeDAO();
        AttractionDAO attractionDAO = new SqlAttractionDAO(employeeDAO, customerDAO);

        // Create controllers
        AttractionsController attractionsController = new AttractionsController(new EmployeesController(employeeDAO), attractionDAO);
        EmployeesController employeesController = new EmployeesController(employeeDAO);
        // Insert Employee
        employeesController.addPerson("Samu","Mar","MRNSML1", 550);
        employeesController.addPerson("Sam","Ma","MRNSML2", 650);
        employeesController.addPerson("Sa","Marr","MRNSML3", 850);        
        // Add Attraction
        attractionsController.addAttraction("Pressure", 10, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), "MRNSML1");
        // Add non overlapping Attraction with same employee
        attractionsController.addAttraction("Supremacy", 10, LocalDateTime.now().plusHours(5), LocalDateTime.now().plusHours(6), "MRNSML1");

        // Add overlapping Attraction with same employee
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> attractionsController.addAttraction("Uprising", 10,  LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(14), "MRNSML1"),
                "Expected addAttraction() to throw!!!"
        );
    }
}