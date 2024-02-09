package businessLogic;


import dao.*;
import db.dbManager;
import domainModel.Attraction;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


class BookingsControllerTest {
    private BookingsController bookingsController;
    private AttractionsController attractionsController;
    private CustomersController customersController;
    private int testattraction1Id;
    private int testattraction2Id;
    private String testCustomerFiscalCode;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up database
    	dbManager.setDatabase("amusementParkTest.db");
        // Set up language    	
        MessagesBundle.SetLanguage("it", "IT");
    }

    @BeforeEach
    public void init() throws Exception {
        Connection connection = dbManager.getConnection();
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

        // Clear the "employees" table
      connection.prepareStatement("DELETE FROM bookings").executeUpdate(); 
        
        // Reset autoincrement counters
        connection.prepareStatement("DELETE FROM sqlite_sequence").executeUpdate();

        // Create DAOs
        SqlCustomerDAO customerDAO = new SqlCustomerDAO(new SqlMembershipDAO());
        EmployeeDAO employeeDAO = new SqlEmployeeDAO();
        AttractionDAO attractionDAO = new SqlAttractionDAO(employeeDAO, customerDAO);
        MembershipDAO membershipDAO = new SqlMembershipDAO();

        // Create controllers
        attractionsController = new AttractionsController(new EmployeesController(employeeDAO), attractionDAO);
        customersController = new CustomersController(customerDAO);
        bookingsController = new BookingsController(attractionsController, customersController, attractionDAO, membershipDAO);
        EmployeesController employeesController = new EmployeesController(employeeDAO);

        // Insert employee, customer and two attractions into the database
        employeesController.addPerson("Mathy", "Pat", "PTRMTH01", 1150);
        employeesController.addPerson("Mat", "Pa", "PTRMTH02", 1250);

        testCustomerFiscalCode = customersController.addPerson("Ma", "Patr", "PTRMTH03", new String[]{"workdays", "silver"}, LocalDate.now().plusYears(100));
        testattraction1Id = attractionsController.addAttraction("Starlight", 10, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "PTRMTH01");
        testattraction2Id = attractionsController.addAttraction("Madness", 10, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(4), "PTRMTH01");
    }


    @Test
    public void When_BookingButAlreadyBooked_Expected_Exception() throws Exception {
        bookingsController.bookAttraction(testCustomerFiscalCode, testattraction1Id);
        Assertions.assertThrows(
                RuntimeException.class,
                () -> bookingsController.bookAttraction(testCustomerFiscalCode, testattraction1Id),
                "Expected bookattraction() to throw!"
        );
    }

    @Test
    public void When_BookingButAlreadyBookedInSameTime_Expected_Exception() throws Exception {
        int id = attractionsController.addAttraction("Blackout", 10, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "PTRMTH02");
        bookingsController.bookAttraction(testCustomerFiscalCode, testattraction1Id);
        Assertions.assertThrows(
                RuntimeException.class,
                () -> bookingsController.bookAttraction(testCustomerFiscalCode, id),
                "Expected bookattraction() to throw!"
        );
    }

    @Test
    public void When_BookingButAttractionFull_Expected_RuntimeException() throws Exception {
        int attractionId = attractionsController.addAttraction("Blackout", 0, LocalDateTime.now().plusHours(9), LocalDateTime.now().plusHours(10), "PTRMTH01");

        Assertions.assertThrows(
                RuntimeException.class,
                () -> bookingsController.bookAttraction(testCustomerFiscalCode, attractionId),
                "Expected bookattraction() to throw!"
        );
    }

    @Test
    public void When_BookingWithNonExistingCustomer_Expected_RuntimeException() {
        Assertions.assertThrows(
                RuntimeException.class,
                () -> bookingsController.bookAttraction("nonExistingFiscalCode", testattraction1Id),
                "Expected bookattraction() to throw!"
        );
    }

    @Test
    public void When_BookingNonExistingAttraction_Expected_RuntimeException() {
        Assertions.assertThrows(
                RuntimeException.class,
                () -> bookingsController.bookAttraction(testCustomerFiscalCode, -1),
                "Expected bookattraction() to throw!"
        );
    }

    @Test
    public void When_BookingExistingAttraction_Expect_Success() {
        Assertions.assertDoesNotThrow(() -> bookingsController.bookAttraction(testCustomerFiscalCode, testattraction1Id));
    }

    @Test
    public void When_DeletingNonExistingAttractionBooking_Expect_ToReturnFalse() throws Exception {
        // Test for non-existing attractionId
        Assertions.assertFalse(bookingsController.deleteAttractionBooking(testCustomerFiscalCode, -1));
        // Test for existing attractionId, but user has not booked that attraction
        customersController.addPerson("M", "P", "PTRMTH06", new String[]{"workdays", "silver"}, LocalDate.now().plusDays(1));
        Assertions.assertFalse(bookingsController.deleteAttractionBooking("PTRMTH06", testattraction1Id));
    }

    @Test
    public void When_DeletingExistingAttractionBooking_Expect_ToReturnTrue() throws Exception {
        Assertions.assertDoesNotThrow(() -> bookingsController.bookAttraction(testCustomerFiscalCode, testattraction1Id));
        Assertions.assertDoesNotThrow(() -> bookingsController.bookAttraction(testCustomerFiscalCode, testattraction2Id));
        Assertions.assertTrue(bookingsController.deleteAttractionBooking(testCustomerFiscalCode, testattraction1Id));
    }

    @Test
    public void When_GettingBookedAttractionForCustomer_Expect_ToReturnTheAttractions() throws Exception {
        Assertions.assertDoesNotThrow(() -> bookingsController.bookAttraction(testCustomerFiscalCode, testattraction1Id));
        Assertions.assertDoesNotThrow(() -> bookingsController.bookAttraction(testCustomerFiscalCode, testattraction2Id));

        List<Attraction> l = bookingsController.getBookingsForCustomer(testCustomerFiscalCode);
        Assertions.assertEquals(l, Arrays.asList(attractionsController.getAttraction(testattraction1Id), attractionsController.getAttraction(testattraction2Id)));
    }

    @Test
    public void When_BookingForCustomer_Expect_UsesToIncreaseCorrectly() throws Exception {
        // Test if the membership is valid on next thursday
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY); // Thursday of the current week
        LocalDateTime workDay = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        // Test if the membership is valid on next Sunday
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); // Sunday of the current week
        // 
        c.add(Calendar.DATE, 7);
        LocalDateTime weekendDay = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        int workC = attractionsController.addAttraction("Blackout", 10, workDay, workDay.plusHours(1), "PTRMTH01");
        int workC2 = attractionsController.addAttraction("Hysteria", 10, workDay.plusHours(2), workDay.plusHours(3), "PTRMTH01");
        int weekendC = attractionsController.addAttraction("Supremacy", 10, weekendDay, weekendDay.plusHours(1), "PTRMTH01");

        // Book the attractions
        bookingsController.bookAttraction(testCustomerFiscalCode, workC);
        bookingsController.bookAttraction(testCustomerFiscalCode, workC2);
        bookingsController.bookAttraction(testCustomerFiscalCode, weekendC);

        HashMap<String, Integer> uses = customersController.getPerson(testCustomerFiscalCode).getMembership().getUses();
        Assertions.assertEquals(uses.get("workdays"), 1);
        Assertions.assertEquals(uses.get("silver"), 2);
    }
}
