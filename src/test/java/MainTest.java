import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import businessLogic.BookingsController;
import domainModel.Customer;

class MainTest {
    		
    @Test
    public void When_deleteAttractionBooking_Expect_Success() throws Exception { 
	
    	Customer customerMock = Mockito.mock(Customer.class);
		BookingsController bookingsControllerMock = Mockito.mock(BookingsController.class);
    	
    	Mockito.when(customerMock.getFiscalCode()).thenReturn("PTRMTH01");			
    	Mockito.when(bookingsControllerMock.deleteAttractionBooking(Mockito.anyString(),Mockito.anyInt())).thenReturn(true);
    	
        Assertions.assertTrue(bookingsControllerMock.deleteAttractionBooking(customerMock.getFiscalCode(),1));
	}	
    
}
