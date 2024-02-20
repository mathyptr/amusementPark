package domainModel.membership;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;


public interface Membership {
	 
    String getUsesDescription();
    void setUses(int uses);
    HashMap<String, Integer> getUses();
        
    boolean isExpired();

    LocalDate getValidFrom();
    
    LocalDate getValidUntil();

    boolean isValidForInterval(LocalDateTime start, LocalDateTime end);

    float getPrice();

}
