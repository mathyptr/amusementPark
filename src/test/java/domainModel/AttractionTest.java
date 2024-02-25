package domainModel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import util.MessagesBundle;

import java.time.LocalDateTime;


class AttractionTest {
    @BeforeAll
    static void setLanguage() {
        // Set up language
        MessagesBundle.SetLanguage("it", "IT");
    } 	
    @Test
    public void When_CreatingNewAttraction_With_InvalidDates_Expect_IllegalArgumentException() {
    	Employee employeeMock = Mockito.mock(Employee.class);
    	Mockito.when(employeeMock.getFiscalCode()).thenReturn("MRRSML01");
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new Attraction(0, "Drones", 10, LocalDateTime.now(), LocalDateTime.now().minusMinutes(1), employeeMock.getFiscalCode(),"Drones","ok"),
            "Expected constructor to throw, but it didn't"
        );

        LocalDateTime d = LocalDateTime.now();
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new Attraction(0, "Euphoria", 10, d, d, employeeMock.getFiscalCode(),"Euphoria","ok"),
            "Expected constructor to throw, but it didn't"
        );
    }
}