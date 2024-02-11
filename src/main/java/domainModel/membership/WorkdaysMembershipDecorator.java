package domainModel.membership;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashMap;

import util.MessagesBundle;

/**
 * Decorator that lets the membership be valid in workdays
 */
public class WorkdaysMembershipDecorator extends MembershipDecorator {
    int uses;

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
        return super.isValidForInterval(start, end) || isCurrTrue;
    }

    @Override
    public float getPrice() {
        return super.getPrice() + 250;
    }

    @Override
    public String getUsesDescription() {
        MessagesBundle msgB = MessagesBundle.getInstance();    
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