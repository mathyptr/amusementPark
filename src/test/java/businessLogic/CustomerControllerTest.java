package businessLogic;

import dao.CustomerDAO;
import dao.SqlCustomerDAO;
import dao.SqlMembershipDAO;
import dao.dbManager;
import domainModel.Customer;
import domainModel.membership.EmptyMembership;
import domainModel.membership.Membership;
import domainModel.membership.SilverMembershipDecorator;
import util.MessagesBundle;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;


class CustomerControllerTest {
    private CustomersController controller;
    private Customer customer;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up database
    	dbManager.getInstance().setDatabase("amusementParkTest.db");
        // Set up language    	
        MessagesBundle.getInstance().SetLanguage("it", "IT");
    }

    @BeforeEach
    public void init() throws Exception {
        Connection connection = dbManager.getInstance().getConnection();

        // Create Customer DAO and Customer Controller
        CustomerDAO customerDAO = new SqlCustomerDAO(new SqlMembershipDAO());
        controller = new CustomersController(customerDAO);

           // Clear the "memberships" table        
        connection.prepareStatement("DELETE FROM memberships").executeUpdate();
        // Clear the "memberships_extensions" table        
        connection.prepareStatement("DELETE FROM memberships_extensions").executeUpdate();

        // Clear the "customers" table
        connection.prepareStatement("DELETE FROM customers").executeUpdate();
        
        // Create test data (insert a customer)
        String TimeFormat = "dd/MM/yyyy";
        DateTimeFormatter dataTimeFormat = DateTimeFormatter.ofPattern(TimeFormat);
        
        Membership membership =new SilverMembershipDecorator(new EmptyMembership(LocalDate.parse(LocalDate.now().format(dataTimeFormat),dataTimeFormat), LocalDate.parse("31/12/2030",dataTimeFormat)));    	
        
        customer = new Customer( "Mar", "Sam", "MRRSML01", membership);
        customerDAO.insert(customer);
    }


    @Test
    public void when_AddingNewPerson_Expect_Success() throws Exception {
        String[] tmp = {"silver"};
        Assertions.assertDoesNotThrow(() -> controller.addPerson("Sam", "Marra","MRRSML10", tmp, LocalDate.parse("9999-01-01")));
        customer = controller.getPerson("MRRSML10");
        Assertions.assertEquals("MRRSML10", customer.getFiscalCode());
    }

    @Test
    public void when_AddingAlreadyExistingPerson_Expect_IllegalArgumentException() {
        String TimeFormat = "dd/MM/yyyy";
        DateTimeFormatter dataTimeFormat = DateTimeFormatter.ofPattern(TimeFormat);
    	String[] tmp = {"silver"};
        Assertions.assertThrows(
                Exception.class,
                () -> controller.addPerson("Sam", "Marra","MRRSML01",  tmp,  LocalDate.parse("31/12/2030",dataTimeFormat)),
                "Expected addPerson() to throw, but it didn't"
        );
    }

    @Test
    public void when_gettingExistingPerson_Expect_toReturnThatPerson() throws Exception {
        Assertions.assertEquals(customer, controller.getPerson(customer.getFiscalCode()));
    }

    @Test
    public void when_gettingNonExistingPerson_Expect_toReturnNull() throws Exception {
        Assertions.assertNull(controller.getPerson("MRRSML123456"));
    }

    @Test
    public void when_removingExistingPerson_Expect_toReturnTrue() throws Exception {
        Assertions.assertTrue(controller.removePerson(customer.getFiscalCode()));
    }

    @Test
    public void when_removingNonExistingPerson_Expect_toReturnFalse() throws Exception {
        Assertions.assertFalse(controller.removePerson("MRRSML123456"));
    }

    @Test
    public void when_AddingNewPerson_WithSpecificMembership_Expect_ThatMembershipToWork() throws Exception {
        String TimeFormat = "dd/MM/yyyy";
        DateTimeFormatter dataTimeFormat = DateTimeFormatter.ofPattern(TimeFormat);
    	String[] tmp = {"silver"};
        controller.addPerson( "Sa","Marra", "MRRSML11", tmp,  LocalDate.parse("31/12/2030",dataTimeFormat));
        customer = controller.getPerson("MRRSML11");

        // Test if the membership is valid on next Wednesday
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY); // Wednesday of the current week
        LocalDateTime start = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Assertions.assertFalse(customer.getMembership().isValidForInterval(start, start.plusDays(1)));

        // Test if the membership is valid on next Sunday
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); // Sunday of the current week
        c.add(Calendar.DATE, 7);
        LocalDateTime start2 = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Assertions.assertTrue(customer.getMembership().isValidForInterval(start2, start2));
    }
}