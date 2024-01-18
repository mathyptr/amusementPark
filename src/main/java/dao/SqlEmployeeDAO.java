package dao;

import domainModel.Employee;
import db.dbManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlEmployeeDAO implements EmployeeDAO {

    @Override
    public void insert(Employee employee) throws SQLException {
        Connection connection = dbManager.getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO employee (fiscal_code, name, surname, salary) VALUES (?, ?, ?, ?)");
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
        Connection connection = dbManager.getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE employee SET name = ?, surname = ?, salary = ? WHERE fiscal_code = ?");
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
        Connection connection = dbManager.getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM employee WHERE fiscal_code = ?");
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
        PreparedStatement ps = con.prepareStatement("SELECT * FROM employee WHERE fiscal_code = ?");
        ps.setString(1, fiscalCode);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
        	employee = new Employee(
                    rs.getString("fiscal_code"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getFloat("salary")
            );
        }

        rs.close();
        ps.close();
        dbManager.closeConnection(con);
        return employee;
    }

    @Override
    public List<Employee> getAll() throws SQLException {
        Connection connection = dbManager.getConnection();
        List<Employee> trainers = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM employee");

        while (rs.next()) {
            trainers.add(new Employee(
                    rs.getString("fiscal_code"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getFloat("salary")
            ));
        }
        return trainers;
    }    
    
    
}