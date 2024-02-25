package domainModel.membership;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import util.MessagesBundle;

import java.time.LocalDate;
import java.time.LocalDateTime;

class EmptyMembershipTest {
	
    @BeforeAll
    static void setLanguage() {
        // Set up language
        MessagesBundle.getInstance().SetLanguage("it", "IT");
    }  	
    
    @Test
    public void when_creatingNewMembership_With_invalidInterval_Expect_IllegalArgumentException() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new EmptyMembership(LocalDate.now(), LocalDate.now().minusDays(10)),
                "Expected constructor to throw!!!!"
        );
    }

    @Test
    public void when_creatingNewMembership_With_validInterval_Expect_Success() {
        new EmptyMembership(LocalDate.now(), LocalDate.now());
        new EmptyMembership(LocalDate.now(), LocalDate.now().plusDays(1));
    }

    @Test
    public void when_checkingIsValidForInterval_Expect_toReturnAlwaysReturnFalse() {
        Membership m = new EmptyMembership( LocalDate.now(), LocalDate.now().plusYears(1));
        Assertions.assertFalse(m.isValidForInterval(LocalDateTime.now(), LocalDateTime.now().plusDays(1)));
        Assertions.assertFalse(m.isValidForInterval(LocalDateTime.now().minusDays(1), LocalDateTime.now()));
    }

    @Test
    public void when_checkingIsExpired_With_validMembership_Expect_toReturnFalse() {
        Membership m = new EmptyMembership(LocalDate.now(), LocalDate.now().plusYears(1));
        Assertions.assertFalse(m.isExpired());

        m = new EmptyMembership(LocalDate.now().minusDays(10), LocalDate.now());
        Assertions.assertFalse(m.isExpired());

        m = new EmptyMembership(LocalDate.now().plusDays(10), LocalDate.now().plusYears(1));
        Assertions.assertFalse(m.isExpired());

        m = new EmptyMembership(LocalDate.now(), LocalDate.now());
        Assertions.assertFalse(m.isExpired());
    }

    @Test
    public void when_checkingIsExpired_With_invalidMembership_Expect_toReturnTrue() {
        Membership m = new EmptyMembership(LocalDate.now().minusDays(10), LocalDate.now().minusDays(1));
        Assertions.assertTrue(m.isExpired());
    }
}
