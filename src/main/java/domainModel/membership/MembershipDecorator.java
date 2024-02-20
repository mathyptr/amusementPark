package domainModel.membership;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * Abstract membership decorator
 */
public abstract class MembershipDecorator implements Membership {

    /** Membership to decorate */
    protected final Membership membership;

    /**
     * Constructs a new membership
     *
     */
    public MembershipDecorator(Membership membership) {
        this.membership = membership;
    }

    @Override
    public LocalDate getValidFrom() {
        return this.membership.getValidFrom();
    }

    @Override
    public LocalDate getValidUntil() {
        return this.membership.getValidUntil();
    }

    @Override
    public boolean isExpired() {
        return this.membership.isExpired();
    }

    @Override
    public boolean isValidForInterval(LocalDateTime start, LocalDateTime end) {
        return this.membership.isValidForInterval(start, end);
    }

    /**
     * Checks if the membership is valid in the given interval
     *
   */
    protected boolean isDateIntervalValid(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start)) return false;
        else return (this.membership.getValidFrom().isBefore(start.toLocalDate()) || this.membership.getValidFrom().equals(start.toLocalDate()))
                && (this.membership.getValidUntil().isAfter(end.toLocalDate()) || this.membership.getValidUntil().equals(end.toLocalDate()));
    }
    
    @Override
    public String getUsesDescription() {
        return this.membership.getUsesDescription();
    }

    @Override
    public HashMap<String, Integer> getUses() {
        return this.membership.getUses();
    }
    @Override    
    public void setUses(int uses) {

    }   
    @Override
    public float getPrice() {
        return this.membership.getPrice();
    }

    public int getLocalUses() {
        return 0;
    }

    public final Membership getMembership() {
        return this.membership;
    }

}
