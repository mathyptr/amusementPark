package domainModel.membership;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Decorator that lets the membership be valid in workdays
 */
public class WorkdaysMembershipDecorator extends MembershipDecorator {
    private int uses;

	private final Logger logger = LogManager.getLogger("MembershipDecorator");
	
    public WorkdaysMembershipDecorator(Membership membership) {
        super(membership);
    }

    public WorkdaysMembershipDecorator(Membership membership, int uses) {
        super(membership);
        this.uses = uses;
    }

    @Override
    public boolean isValidForInterval(LocalDateTime start, LocalDateTime end) {
        if (!this.isDateIntervalValid(start, end)) return false;
        boolean isCurrTrue = (isOnWeekdays(start) && isOnWeekdays(end));
        if (isCurrTrue) uses++;
        else logger.debug(msgB.GetResourceValue("Membership_Not_Valid_For_Interval")+"("+start.toString()+","+end.toString()+")");        
        return super.isValidForInterval(start, end) || isCurrTrue;
    }

    @Override
    public float getPrice() {
        return super.getPrice() + 250;
    }

    @Override
    public String getUsesDescription() { 
        return super.getUsesDescription() + msgB.GetResourceValue("Workdays_uses") + uses + ", ";
    }

    @Override
    public HashMap<String, Integer> getUses() {
        this.membership.getUses().put("workdays", uses);
        return this.membership.getUses();
    }

    private boolean isOnWeekdays(LocalDateTime date) {
        return date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY;
    }

    @Override
    public int getLocalUses() {
        return uses;
    } 
}