package dao;

import domainModel.Attraction;
import domainModel.Employee;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

import db.dbManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;


public class SqlAttractionDAOTest {
    private SqlAttractionDAO attractionDAO;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up database
    	dbManager.setDatabase("amusementParkTest.db");
//    	dbManager.initDatabase();
    }

    @BeforeEach
    public void init() throws SQLException, IOException {
        Connection connection = dbManager.getConnection();
        attractionDAO = new SqlAttractionDAO();

        // Clear the "trainers" table
        connection.prepareStatement("DELETE FROM attractions").executeUpdate();
        
        // Insert some test data
        connection.prepareStatement("INSERT INTO attractions (1,'Starlight',50, '01/01/2023','31/12/2023') ").executeUpdate();
        connection.prepareStatement("INSERT INTO attractions (2,'Madness',60,'01/02/2023','01/11/2023')").executeUpdate();
    }

    @Test
    public void When_GetAllAttractions_Expect_Success() throws SQLException {
        List<Attraction> attractions = attractionDAO.getAll();
        Assertions.assertEquals(2, attractions.size());
    }

    @Test
    public void When_GetAttractionByName_Expect_Success() throws SQLException {
    	Attraction attraction = attractionDAO.get(1);
        Assertions.assertEquals("Starlight", attraction.getName());
//        Assertions.assertEquals("surname1", attraction.getEmployee());
        Assertions.assertEquals(50, attraction.getMaxCapacity());
    }

    @Test
    public void When_GetAttractionById_Expect_Null() throws SQLException {
    	Attraction attraction = attractionDAO.get(123);
        Assertions.assertNull(attraction);
    }

    @Test
    public void When_AddAttraction_Expect_Success() throws SQLException {
    	Attraction attraction = new Attraction(3,"Blackout",100,LocalDateTime.now().plusMonths(1) ,LocalDateTime.now().plusMonths(4) );
        Assertions.assertDoesNotThrow(() -> attractionDAO.insert(attraction));
        Assertions.assertEquals(3, attractionDAO.getAll().size());
    }

    @Test
    public void When_UpdateAttraction_Expect_Success() throws SQLException {
    	Attraction attraction = new Attraction(3,"Hysteria",90,LocalDateTime.now().plusMonths(1) ,LocalDateTime.now().plusMonths(4) );
    	Assertions.assertDoesNotThrow(() -> attractionDAO.update(attraction));
        Assertions.assertEquals(3, attractionDAO.getAll().size());
        Assertions.assertEquals("Hysteria", attractionDAO.get(3).getName());
        Assertions.assertEquals(90, attractionDAO.get(1).getMaxCapacity());
    }

    @Test
    public void When_DeleteAttraction_Expect_Success() throws SQLException {
        Assertions.assertTrue(attractionDAO.delete(3));
        Assertions.assertEquals(2, attractionDAO.getAll().size());
    }

    @Test
    public void When_DeleteAttraction_Expect_Failure() throws SQLException {
        Assertions.assertFalse(attractionDAO.delete(3));
        Assertions.assertEquals(3, attractionDAO.getAll().size());
    }
}
