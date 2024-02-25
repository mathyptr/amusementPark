package domainModel.membership;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import util.MessagesBundle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;


class SilverMembershipDecoratorTest {
    @BeforeAll
    static void setLanguage() {
        // Set up language
        MessagesBundle.SetLanguage("it", "IT");
    } 	
    @Test
    public void when_checkingIsValidForInterval_With_goodInterval_Expect_toReturnTrue() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        LocalDate startDate = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();       
        LocalDateTime start = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Membership membership = new SilverMembershipDecorator(new EmptyMembership(startDate, startDate.plusYears(1)));
        Assertions.assertTrue(membership.isValidForInterval(start, start.plusHours(1)));
    }

    @Test
    public void when_checkingIsValidForInterval_With_badInterval_Expect_toReturnFalse() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        LocalDateTime start = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Membership m = new SilverMembershipDecorator(new EmptyMembership(LocalDate.now(), LocalDate.now().plusYears(1)));
        
        Assertions.assertFalse(m.isValidForInterval(start, start.plusHours(1)));

    }
}