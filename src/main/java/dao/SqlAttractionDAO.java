package dao;

import db.dbManager;
import domainModel.Attraction;


import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SqlAttractionDAO implements AttractionDAO {

    public SqlAttractionDAO() {
    }

//    @Override
    public Attraction get(Integer id) throws Exception {
        Connection connection = dbManager.getConnection();
        Attraction course = null;
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM courses WHERE id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            course = new Attraction(
                    id,
                    rs.getString("name"),
                    rs.getInt("max_capacity"),
                    LocalDateTime.parse(rs.getString("start_date")),
                    LocalDateTime.parse(rs.getString("end_date"))
                    );            		            
        }

        rs.close();
        ps.close();

        dbManager.closeConnection(connection);
        return course;
    }

//    @Override
    public List<Attraction> getAll() throws Exception {
        Connection connection =dbManager.getConnection();
        List<Attraction> attractions = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM courses");

        while (rs.next()) {
        	Attraction c = new Attraction(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("max_capacity"),
                    LocalDateTime.parse(rs.getString("start_date")),
                    LocalDateTime.parse(rs.getString("end_date"))
        		);        			
           
        		attractions.add(c);
        }

        rs.close();
        stmt.close();
        dbManager.closeConnection(connection);
        return attractions;
    }

    @Override
    public void insert(Attraction attraction) throws SQLException {
    }

    @Override
    public void update(Attraction attraction) throws SQLException {
    }

    @Override
    public boolean delete(Integer id) throws Exception {

        return true;
    }

    @Override
    public int getNextID() throws SQLException {
        Connection connection = dbManager.getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM attractions");
        int id = rs.getInt(1) + 1;

        rs.close();
        stmt.close();
        dbManager.closeConnection(connection);
        return id;
    }

 /*   @Override
    public List<Customer> getAttendees(Integer courseId) throws Exception {
    }*/

    @Override
    public List<Attraction> getAttractionsForCustomer(String fiscalCode) throws Exception {

        List<Attraction> attractions = new ArrayList<>();
        return attractions;
    }

    @Override
    public void addBooking(String fiscalCode, Integer courseId) throws SQLException {

    }

    @Override
    public boolean deleteBooking(String fiscalCode, Integer courseId) throws SQLException {
        return true;
    }
}
