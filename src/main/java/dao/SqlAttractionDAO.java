package dao;

import domainModel.Attraction;
import domainModel.Customer;
import util.MessagesBundle;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SqlAttractionDAO implements AttractionDAO {
    private final EmployeeDAO employeeDAO;
    private final CustomerDAO customerDAO;
    private dbManager db;
    
    private MessagesBundle msgB;
	private final Logger logger = LogManager.getLogger("SqlAttractionDAO");    
    private static final String TimeFormat = "dd/MM/yyyy HH:mm";
    DateTimeFormatter dataTimeFormat = DateTimeFormatter.ofPattern(TimeFormat);
	
    public SqlAttractionDAO(EmployeeDAO emplyeeDAO, CustomerDAO customerDAO) {
        this.employeeDAO = emplyeeDAO;
        this.customerDAO = customerDAO;		
        this.msgB = MessagesBundle.getInstance();
        this.db = dbManager.getInstance();
    }

//    @Override
    public Attraction get(Integer id) throws SQLException {   	
        Connection connection = db.getConnection();
        Attraction attract = null;
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM attractions WHERE id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            attract = new Attraction(
                    id,
                    rs.getString("name"),
                    rs.getInt("max_capacity"),
                    rs.getInt("adrenaline"),
                    LocalDateTime.parse(rs.getString("start_date"),dataTimeFormat),
                    LocalDateTime.parse(rs.getString("end_date"),dataTimeFormat),
                    rs.getString("employee"),
                    rs.getString("description"),
                    rs.getString("status")
                    );            		            
        }

        rs.close();
        ps.close();

        db.closeConnection(connection);
        return attract;
    }

//    @Override
    public List<Attraction> getAll() throws SQLException {
        Connection connection =db.getConnection();
        List<Attraction> attractions = new ArrayList<>();
        Statement stmt = connection.createStatement();     
        
        ResultSet rs = stmt.executeQuery("SELECT * FROM attractions");

        while (rs.next()) {
        	Attraction c = new Attraction(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("max_capacity"),
                    rs.getInt("adrenaline"),
                    LocalDateTime.parse(rs.getString("start_date"),dataTimeFormat),
                    LocalDateTime.parse(rs.getString("end_date"),dataTimeFormat),
                    rs.getString("employee"),
                    rs.getString("description"),
                    rs.getString("status")
        		);        			
           
        		attractions.add(c);
        }

        rs.close();
        stmt.close();
        db.closeConnection(connection);
        return attractions;
    }

    @Override
    public void insert(Attraction attraction) throws SQLException {
    	logger.debug(msgB.GetResourceValue("debug_attraction_data_insert")+attraction.getName());    	
        Connection connection = db.getConnection();
        
        PreparedStatement ps = connection.prepareStatement("INSERT INTO attractions (name, max_capacity, adrenaline, start_date, end_date, employee,description,status) VALUES (?, ?, ?, ?, ?, ?,?,?)");
        // id is auto-incremented, so it's not needed
        ps.setString(1, attraction.getName());
        ps.setInt(2, attraction.getMaxCapacity());
        ps.setInt(3, attraction.getAdrenaline());
        ps.setString(4, dataTimeFormat.format(attraction.getStartDate()));
        ps.setString(5, dataTimeFormat.format(attraction.getEndDate()));
        ps.setString(6, attraction.getEmployeeFiscalCode());        
        ps.setString(7,attraction.getDescription());
        ps.setString(8,attraction.getStatus());
        
        ps.executeUpdate();

        ps.close();
        db.closeConnection(connection);
      	
    }

    @Override
    public void update(Attraction attraction) throws SQLException {
    	logger.debug(msgB.GetResourceValue("debug_attraction_data_update")+attraction.getName());    	    	
        Connection connection = db.getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE attractions SET name = ?, max_capacity = ?, adrenaline = ?, start_date = ?, end_date = ?, employee = ?, description = ?, status = ? WHERE id = ?");
        ps.setString(1, attraction.getName());
        ps.setInt(2, attraction.getMaxCapacity());
        ps.setInt(3, attraction.getAdrenaline());
        ps.setString(4, dataTimeFormat.format(attraction.getStartDate()));
        ps.setString(5, dataTimeFormat.format(attraction.getEndDate()));
        ps.setString(6, attraction.getEmployeeFiscalCode());
        ps.setString(7,attraction.getDescription());
        ps.setString(8,attraction.getStatus());       
        ps.setInt(9, attraction.getId());     
        
        ps.executeUpdate();

        ps.close();
        db.closeConnection(connection);    	
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
    	logger.debug(msgB.GetResourceValue("debug_attraction_data_delete")+Integer.toString(id));    	
    	
    	Attraction attraction = get(id);
        if (attraction == null) return false;

        Connection connection = db.getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM attractions WHERE id = ?");
        ps.setInt(1, id);
        int rows = ps.executeUpdate();

        ps.close();
        db.closeConnection(connection);
        return rows > 0;
    }

    @Override
    public int getNextID() throws SQLException {
        Connection connection = db.getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM attractions");
        int id = rs.getInt(1) + 1;

        rs.close();
        stmt.close();
        db.closeConnection(connection);
        return id;
    }

    @Override
    public List<Customer> getAttendees(Integer attractionId) throws Exception {
        Connection connection = db.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM bookings WHERE attraction = ?");
        ps.setInt(1, attractionId);
        ResultSet rs = ps.executeQuery();

        List<Customer> customers = new ArrayList<>();
        while (rs.next()) customers.add(customerDAO.get(rs.getString("customer")));

        rs.close();
        ps.close();
        db.closeConnection(connection);
        return customers;
    	
    }

    @Override
    public List<Attraction> getAttractionsForCustomer(String fiscalCode) throws Exception {
        
        Connection connection = db.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM bookings WHERE customer = ?");
        ps.setString(1, fiscalCode);
        ResultSet rs = ps.executeQuery();

        List<Attraction> attractions = new ArrayList<>();
        while (rs.next()) attractions.add(this.get(rs.getInt("attraction")));

        rs.close();
        ps.close();
        db.closeConnection(connection);
        return attractions;       
        
    }

    @Override
    public void addBooking(String fiscalCode, Integer attractionId) throws SQLException {
   	
        Connection connection = db.getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT OR IGNORE INTO bookings (customer, attraction) VALUES (?, ?)");
        ps.setString(1, fiscalCode);
        ps.setInt(2, attractionId);
        ps.executeUpdate();

        ps.close();

    }

    @Override
    public boolean deleteBooking(String fiscalCode, Integer attractionId) throws SQLException {
        Connection connection = db.getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM bookings WHERE customer = ? AND attraction = ?");
        ps.setString(1, fiscalCode);
        ps.setInt(2, attractionId);
        int rows = ps.executeUpdate();

        ps.close();
        db.closeConnection(connection);
        return rows > 0;    	
    }
}
