package domainModel.membership;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

import util.MessagesBundle;

/**
 * Concrete class representing a membership that can never access the the Park.
 */
public class EmptyMembership implements Membership {
    /** Price of the membership */
    private final float price;

    /** Date from which the membership starts */
    private final LocalDate validFrom;

    /** Date in which the membership ends */
    private final LocalDate validUntil;

    private final HashMap<String, Integer> uses = new HashMap<>();

    /**
     * Constructor of a full membership
     *
     */
    public EmptyMembership(LocalDate validFrom, LocalDate validUntil) {
        MessagesBundle msgB = MessagesBundle.getInstance();       	
        if (!validFrom.isBefore(validUntil) && !validFrom.equals(validUntil))
            throw new IllegalArgumentException(msgB.GetResourceValue("Date_range_invalid"));

        this.price = 0;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    @Override
    public LocalDate getValidFrom() {
        return validFrom;
    }

    @Override
    public LocalDate getValidUntil() {
        return validUntil;
    }
    
    @Override
    public boolean isValidForInterval(LocalDateTime start, LocalDateTime end) {
        return false;
    }
    
    @Override
    public boolean isExpired() {
        return validUntil.isBefore(LocalDateTime.now().toLocalDate());
    }

    @Override
    public String getUsesDescription() {
        return "";
    }
    public void setUses(int uses) {

    }   
    @Override
    public HashMap<String, Integer> getUses() {
        return uses;
    }
    
    @Override
    public float getPrice() {
        return price;
    }
 
}
