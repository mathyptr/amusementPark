package dao;

import db.dbManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import domainModel.Employee;

public class SqlEmployeeDAOTest {
    private SqlEmployeeDAO employeeDAO;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up database
    	dbManager.setDatabase("amusementParkTest.db");
//    	dbManager.initDatabase();
    }    

    @BeforeEach
    public void init() throws SQLException, IOException {
        Connection connection = dbManager.getConnection();
        employeeDAO = new SqlEmployeeDAO();

        // Clear the "employees" table
        connection.prepareStatement("DELETE FROM employees").executeUpdate();
        
        // Insert some test data
    
        connection.prepareStatement("INSERT INTO employees values ('MRRSML01','Samuele','Marr', 1000) ").executeUpdate();
        connection.prepareStatement("INSERT INTO employees values ('MRRSML02','Sam','Marr', 1000)").executeUpdate();
    }
    
    @Test
    public void When_GetAllEmployee_Expect_Success() throws SQLException {
        List<Employee> employees = employeeDAO.getAll();
        Assertions.assertEquals(2, employees.size());
    }    
    
    @Test
    public void When_GetEmployeeByName_Expect_Success() throws SQLException {
    	Employee employee = employeeDAO.get("MRRSML01");
        Assertions.assertEquals("Samuele", employee.getName());
        Assertions.assertEquals(1000, employee.getSalary());
    }
    
    @Test
    public void When_GetEmployeeByFiscalCode_Expect_Null() throws SQLException {
    	Employee employee = employeeDAO.get("?????");
        Assertions.assertNull(employee);
    }
    
    @Test
    public void When_AddEmployee_Expect_Success() throws SQLException {
    	Employee employee = new Employee("MRRSML03","Samuele","Ma", 1000);
        Assertions.assertDoesNotThrow(() -> employeeDAO.insert(employee));    	
        Assertions.assertEquals(3, employeeDAO.getAll().size());
    }
    
    @Test
    public void When_UpdateEmployee_Expect_Success() throws SQLException {
    	Employee employee = new Employee("Marr","Samuele","MRRSML01", 1500);
    	Assertions.assertDoesNotThrow(() -> employeeDAO.update(employee));
        Assertions.assertEquals(2, employeeDAO.getAll().size());
        Assertions.assertEquals("Samuele", employeeDAO.get("MRRSML01").getName());
        Assertions.assertEquals(1500, employeeDAO.get("MRRSML01").getSalary());
    }
    
    @Test
    public void When_DeleteAttraction_Expect_Success() throws SQLException {
        Assertions.assertTrue(employeeDAO.delete("MRRSML02"));
        Assertions.assertEquals(1, employeeDAO.getAll().size());
    }

    @Test
    public void When_DeleteAttraction_Expect_Failure() throws SQLException {
        Assertions.assertFalse(employeeDAO.delete("????"));
        Assertions.assertEquals(2, employeeDAO.getAll().size());
    }
}    