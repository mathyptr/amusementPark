package domainModel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.LocalDateTime;


class AttractionTest {
    @Test
    public void When_CreatingNewCourse_With_InvalidDates_Expect_IllegalArgumentException() {
    	Employee employeeMock = Mockito.mock(Employee.class);
//    	String fiscalCode;
    	Mockito.when(employeeMock.getFiscalCode()).thenReturn("MRRSML01");
// fiscalCode=employeeMock.getFiscalCode();
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new Attraction(0, "ABC", 10, LocalDateTime.now(), LocalDateTime.now().minusMinutes(1), employeeMock.getFiscalCode()),
            "Expected constructor to throw, but it didn't"
        );

        LocalDateTime d = LocalDateTime.now();
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new Attraction(0, "A", 10, d, d, employeeMock.getFiscalCode()),
            "Expected constructor to throw, but it didn't"
        );
    }
}