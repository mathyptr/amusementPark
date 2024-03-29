package dao;

import java.sql.SQLException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import junit.framework.TestCase;

public class dbManagerTest extends TestCase {

    @Test
	public void testGetInstance_Expect_Success() {
        // Set up database
    	dbManager.getInstance().setDatabase("amusementParkTest.db");	
        Assertions.assertNotNull(dbManager.getInstance());
	}

	public void testGetConnectionString() {
		fail("Not yet implemented");
	}
	
    @Test
	public void testGetConnection_Expect_Success()throws SQLException  {
    	dbManager.getInstance().setDatabase("amusementParkTest.db");			
        Assertions.assertDoesNotThrow(() -> dbManager.getInstance().getConnection()); 		
	}
  
    
	public void testCloseConnection() {
		fail("Not yet implemented");
	}

}
