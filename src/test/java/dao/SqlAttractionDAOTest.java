package dao;

import domainModel.Attraction;
import util.MessagesBundle;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    	dbManager.getInstance().setDatabase("amusementParkTest.db");
        MessagesBundle.getInstance().SetLanguage("it", "IT");
    }

    @BeforeEach
    public void init() throws SQLException, IOException {
        Connection connection = dbManager.getInstance().getConnection();
        
        EmployeeDAO employeeDAO = new SqlEmployeeDAO();
        MembershipDAO membershipDAO = new SqlMembershipDAO();
        CustomerDAO customerDAO = new SqlCustomerDAO(membershipDAO);
        attractionDAO = new SqlAttractionDAO(employeeDAO, customerDAO);
        
        
        // Clear the "attractions" table
        connection.prepareStatement("DELETE FROM attractions").executeUpdate();
        
        // Insert some test data
        connection.prepareStatement("INSERT INTO attractions values (1,'Starlight',50,5, '01/01/2023 08:00','31/12/2023 18:00','PTRMTH','Starlight','ok') ").executeUpdate();
        connection.prepareStatement("INSERT INTO attractions values (2,'Madness',60, 10,'01/02/2023 08:00','01/11/2023 18:00','PTRMTH','Madness','ok')").executeUpdate();
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
        Assertions.assertEquals(50, attraction.getMaxCapacity());
    }

    @Test
    public void When_GetAttractionById_Expect_Null() throws SQLException {
    	Attraction attraction = attractionDAO.get(123);
        Assertions.assertNull(attraction);
    }

    @Test
    public void When_AddAttraction_Expect_Success() throws SQLException {
    	LocalDateTime start_date,end_date; 	
    	start_date=LocalDateTime.now().plusMonths(1) ;
    	end_date=LocalDateTime.now().plusMonths(4);
    	Attraction attraction = new Attraction(3,"Blackout",100,15,start_date,end_date,"PTRMTH","Blackout","ok");
        Assertions.assertDoesNotThrow(() -> attractionDAO.insert(attraction));
        Assertions.assertEquals(3, attractionDAO.getAll().size());
    }

    @Test
    public void When_UpdateAttraction_Expect_Success() throws SQLException {
    	Attraction attraction = new Attraction(2,"Hysteria",90,10,LocalDateTime.now().plusMonths(1) ,LocalDateTime.now().plusMonths(4),"PTRMTH","Hysteria","ok");
    	Assertions.assertDoesNotThrow(() -> attractionDAO.update(attraction));
        Assertions.assertEquals(2, attractionDAO.getAll().size());
        Assertions.assertEquals("Hysteria", attractionDAO.get(2).getName());
        Assertions.assertEquals(90, attractionDAO.get(2).getMaxCapacity());
    }

    @Test
    public void When_DeleteAttraction_Expect_Success() throws SQLException {
        Assertions.assertTrue(attractionDAO.delete(2));
        Assertions.assertEquals(1, attractionDAO.getAll().size());
    }

    @Test
    public void When_DeleteAttraction_Expect_Failure() throws SQLException {
        Assertions.assertFalse(attractionDAO.delete(2000));
        Assertions.assertEquals(2, attractionDAO.getAll().size());
    }
}
