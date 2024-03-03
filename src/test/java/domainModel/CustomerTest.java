package domainModel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.sql.SQLException;


class CustomerTest {
    @Test   
    public void When_GetCustomerByName_Expect_Success() throws SQLException {
    	Customer customerMock = Mockito.mock(Customer.class);
    	Mockito.when(customerMock.getName()).thenReturn("Mathy");    		    	
        Assertions.assertEquals("Mathy", customerMock.getName());
    }    
    @Test   
    public void When_GetCustomerBySurname_Expect_Success() throws SQLException {
    	Customer customerMock = Mockito.mock(Customer.class);	
    	Mockito.when(customerMock.getSurname()).thenReturn("Pat");	
        Assertions.assertEquals("Pat", customerMock.getSurname());
    }    
    @Test   
    public void When_GetCustomerByFiscalCode_Expect_Failure() throws SQLException {
    	Customer customerMock = Mockito.mock(Customer.class);	
    	Mockito.when(customerMock.getFiscalCode()).thenReturn("PTRMTH01");	
        Assertions.assertNotEquals("PTRMTH02", customerMock.getFiscalCode());
    }        
}