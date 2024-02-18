package dao;

import db.dbManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import domainModel.Customer;
import util.MessagesBundle;


public class SqlCustomerDAO implements CustomerDAO {

    private final MembershipDAO membershipDAO;
	private final Logger logger = LogManager.getLogger("SqlCustomerDAO");
	
    public SqlCustomerDAO(MembershipDAO membershipDAO) {
        this.membershipDAO = membershipDAO;
    }

    @Override
    public void insert(Customer customer) throws Exception {
    	logger.debug(MessagesBundle.GetResourceValue("debug_customer_data_insert")+customer.toString());    	
        Connection connection = dbManager.getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO customers (fiscal_code, name, surname) VALUES (?, ?, ?)");
        ps.setString(1, customer.getFiscalCode());
        ps.setString(2, customer.getName());
        ps.setString(3, customer.getSurname());
        ps.executeUpdate();

        membershipDAO.insertOfCustomer(customer.getFiscalCode(), customer.getMembership());

        ps.close();
        dbManager.closeConnection(connection);
    }

    @Override
    public void update(Customer customer) throws Exception {
    	logger.debug(MessagesBundle.GetResourceValue("debug_customer_data_update")+customer.toString());    	
        Connection connection = dbManager.getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE customers SET name = ?, surname = ? WHERE fiscal_code = ?");
        ps.setString(1, customer.getName());
        ps.setString(2, customer.getSurname());
        ps.setString(3, customer.getFiscalCode());
        ps.executeUpdate();

        membershipDAO.updateOfCustomer(customer.getFiscalCode(), customer.getMembership());
        ps.close();
        dbManager.closeConnection(connection);
    }

    @Override
    public boolean delete(String fiscalCode) throws SQLException {
    	logger.debug(MessagesBundle.GetResourceValue("debug_customer_data_delete")+fiscalCode);    	
        Connection connection = dbManager.getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM customers WHERE fiscal_code = ?");
        ps.setString(1, fiscalCode);
        int rows = ps.executeUpdate();

        ps.close();
        dbManager.closeConnection(connection);
        return rows > 0;
    }  
       
    @Override
    public Customer get(String fiscalCode) throws SQLException,Exception {    	
        Connection con = dbManager.getConnection();
        Customer customer = null;
        PreparedStatement ps = con.prepareStatement("SELECT * FROM customers WHERE fiscal_code = ?");
        ps.setString(1, fiscalCode);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            customer = new Customer(
                    rs.getString("surname"),            		
                    rs.getString("name"),
                    rs.getString("fiscal_code"),
                    membershipDAO.getOfCustomer(rs.getString("fiscal_code"))
            );
        }

        rs.close();
        ps.close();
        dbManager.closeConnection(con);
    	logger.debug(MessagesBundle.GetResourceValue("debug_customer_data")+customer.toString());        
        return customer;
    }

    @Override
    public List<Customer> getAll() throws SQLException,Exception {
        Connection connection = dbManager.getConnection();
        List<Customer> customers = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM customers");

        while (rs.next()) {
            customers.add(new Customer(
                    rs.getString("surname"),            		
                    rs.getString("name"),
                    rs.getString("fiscal_code"),
                    membershipDAO.getOfCustomer(rs.getString("fiscal_code"))
            ));
        }
        return customers;
    }
}
