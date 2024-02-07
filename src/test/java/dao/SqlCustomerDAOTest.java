package dao;

import db.dbManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import domainModel.Customer;
import domainModel.membership.EmptyMembership;
import domainModel.membership.Membership;
import domainModel.membership.SilverMembershipDecorator;


public class SqlCustomerDAOTest {
    private SqlCustomerDAO customerDAO;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up database
   	
    	dbManager.setDatabase("amusementParkTest.db");
//    	dbManager.initDatabase();
    }    

    @BeforeEach
    public void init() throws SQLException, IOException {
        Connection connection = dbManager.getConnection();
        customerDAO = new SqlCustomerDAO(new SqlMembershipDAO());
        // Clear the "memberships" table        
        connection.prepareStatement("DELETE FROM memberships").executeUpdate();
        // Clear the "memberships_extensions" table        
        connection.prepareStatement("DELETE FROM memberships_extensions").executeUpdate();

        // Clear the "customers" table
        connection.prepareStatement("DELETE FROM customers").executeUpdate();
        
        // Insert some test data
    
        connection.prepareStatement("INSERT INTO customers values ('PTRMTH01','Mat','Patr') ").executeUpdate();
        connection.prepareStatement("INSERT INTO memberships values ('PTRMTH01','01/01/2023','31/12/2023') ").executeUpdate();
        connection.prepareStatement("INSERT INTO memberships_extensions values ('PTRMTH01','silver',0) ").executeUpdate();
        connection.prepareStatement("INSERT INTO customers values ('PTRMTH02','Mathy','Pat')").executeUpdate();
        connection.prepareStatement("INSERT INTO memberships values ('PTRMTH02','01/01/2023','31/12/2023') ").executeUpdate();
        connection.prepareStatement("INSERT INTO memberships_extensions values ('PTRMTH02','silver',0) ").executeUpdate();
    }
    
    @Test
    public void When_GetAllCustomer_Expect_Success() throws SQLException,Exception {
        List<Customer> customers = customerDAO.getAll();
        Assertions.assertEquals(2, customers.size());
    }    
    
    @Test
    public void When_GetCustomerByName_Expect_Success() throws SQLException,Exception {
    	Customer customer = customerDAO.get("PTRMTH01");
        Assertions.assertEquals("Mat", customer.getName());
        Assertions.assertEquals("PTRMTH01", customerDAO.get("PTRMTH01").getFiscalCode());        
    }
    
    @Test
    public void When_GetCustomerByFiscalCode_Expect_Null() throws SQLException,Exception {
    	Customer customer = customerDAO.get("?????");
        Assertions.assertNull(customer);
    }
    
    @Test
    public void When_AddCustomer_Expect_Success() throws SQLException,Exception {
        String TimeFormat = "dd/MM/yyyy";
        DateTimeFormatter dataTimeFormat = DateTimeFormatter.ofPattern(TimeFormat);
    	
        Membership membership =new SilverMembershipDecorator(new EmptyMembership(LocalDate.parse(LocalDate.now().format(dataTimeFormat),dataTimeFormat), LocalDate.parse("31/12/2030",dataTimeFormat)));    	
    	Customer customer = new Customer("Pa","Ma","PTRMTH03",membership);
        Assertions.assertDoesNotThrow(() -> customerDAO.insert(customer));    	
        Assertions.assertEquals(3, customerDAO.getAll().size());
    }
    
    @Test
    public void When_UpdateCustomer_Expect_Success() throws SQLException,Exception {
        String TimeFormat = "dd/MM/yyyy";
        DateTimeFormatter dataTimeFormat = DateTimeFormatter.ofPattern(TimeFormat);
        
        Membership membership =new SilverMembershipDecorator(new EmptyMembership(LocalDate.parse(LocalDate.now().format(dataTimeFormat),dataTimeFormat), LocalDate.parse("31/12/2030",dataTimeFormat)));    	
    	
    	Customer customer = new Customer("PAt","Math","PTRMTH01",membership);
    	Assertions.assertDoesNotThrow(() -> customerDAO.update(customer));
        Assertions.assertEquals(2, customerDAO.getAll().size());
        Assertions.assertEquals("Math", customerDAO.get("PTRMTH01").getName());
        Assertions.assertEquals("PTRMTH01", customerDAO.get("PTRMTH01").getFiscalCode());
    }
    
    @Test
    public void When_DeleteCustomer_Expect_Success() throws SQLException,Exception {
        Assertions.assertTrue(customerDAO.delete("PTRMTH02"));
        Assertions.assertEquals(1, customerDAO.getAll().size());
    }

    @Test
    public void When_DeleteCustomer_Expect_Failure() throws SQLException,Exception {
        Assertions.assertFalse(customerDAO.delete("????"));
        Assertions.assertEquals(2, customerDAO.getAll().size());
    }
}    