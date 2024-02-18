package dao;

import domainModel.Employee;
import util.MessagesBundle;
import db.dbManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SqlEmployeeDAO implements EmployeeDAO {
	private final Logger logger = LogManager.getLogger("SqlEmployeeDAO");
    @Override
    public void insert(Employee employee) throws SQLException {
    	logger.debug(MessagesBundle.GetResourceValue("debug_employee_data_insert")+employee.toString());    	
        Connection connection = dbManager.getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO employees (fiscal_code, name, surname, salary) VALUES (?, ?, ?, ?)");
        ps.setString(1, employee.getFiscalCode());
        ps.setString(2, employee.getName());
        ps.setString(3, employee.getSurname());
        ps.setFloat(4, employee.getSalary());
        ps.executeUpdate();
        ps.close();
        dbManager.closeConnection(connection);
    }

    @Override
    public void update(Employee employee) throws SQLException {
    	logger.debug(MessagesBundle.GetResourceValue("debug_employee_data_update")+employee.toString());    	
        Connection connection = dbManager.getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE employees SET name = ?, surname = ?, salary = ? WHERE fiscal_code = ?");
        ps.setString(1, employee.getName());
        ps.setString(2, employee.getSurname());
        ps.setFloat(3, employee.getSalary());
        ps.setString(4, employee.getFiscalCode());
        ps.executeUpdate();
        ps.close();
        dbManager.closeConnection(connection);
    }

    @Override
    public boolean delete(String fiscalCode) throws SQLException {
    	logger.debug(MessagesBundle.GetResourceValue("debug_employee_data_delete")+fiscalCode);    	
        Connection connection = dbManager.getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM employees WHERE fiscal_code = ?");
        ps.setString(1, fiscalCode);
        int rows = ps.executeUpdate();
        ps.close();
        dbManager.closeConnection(connection);
        return rows > 0;
    }
    @Override
    public Employee get(String fiscalCode) throws SQLException {    	
        Connection con = dbManager.getConnection();
        Employee employee = null;
        PreparedStatement ps = con.prepareStatement("SELECT * FROM employees WHERE fiscal_code = ?");
        ps.setString(1, fiscalCode);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
        	employee = new Employee(
                    rs.getString("surname"),        			
                    rs.getString("name"),
                    rs.getString("fiscal_code"),
                    rs.getFloat("salary")
            );
        }

        rs.close();
        ps.close();
        dbManager.closeConnection(con);
        if(employee!=null)
        	logger.debug(MessagesBundle.GetResourceValue("debug_employee_data")+employee.toString());
        else
        	logger.debug(MessagesBundle.GetResourceValue("Employee_Not_found")+fiscalCode);        
        return employee;
    }

    @Override
    public List<Employee> getAll() throws SQLException {
        Connection connection = dbManager.getConnection();
        List<Employee> employees = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM employees");

        while (rs.next()) {
        	employees.add(new Employee(
                    rs.getString("fiscal_code"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getFloat("salary")
            ));
        }
        return employees;
    }    
    
    
}