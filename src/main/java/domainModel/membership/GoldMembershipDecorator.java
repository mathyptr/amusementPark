package domainModel.membership;
import java.time.LocalDateTime;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Decorator that lets the membership be always valid 
 * 
 *  */
public class GoldMembershipDecorator extends MembershipDecorator {
    private int uses;
    private final float goldPrice = 500;
    
	private final Logger logger = LogManager.getLogger("GoldMembershipDecorator");
	
    public GoldMembershipDecorator(Membership membership) {
        super(membership);
    }

    public GoldMembershipDecorator(Membership membership, int uses) {
        super(membership);
        this.uses = uses;
    }

    @Override
    public int getLocalUses() {
        return uses;
    }    
    
    @Override
    public String getUsesDescription() {     	
        return super.getUsesDescription() + msgB.GetResourceValue("Gold_uses") + uses + ", ";
    }    

    @Override
    public float getPrice() {
        return super.getPrice() + goldPrice;
    }
        
    @Override
    public boolean isValidForInterval(LocalDateTime start, LocalDateTime end) {
        if (!this.isDateIntervalValid(start, end)) return false;
        boolean isCurrTrue = true; 
        if (isCurrTrue) uses++;
        else logger.debug(msgB.GetResourceValue("Membership_Not_Valid_For_Interval")+"("+start.toString()+","+end.toString()+")");
        return super.isValidForInterval(start, end) || isCurrTrue;
    }
       
    @Override
    public HashMap<String, Integer> getUses() {
        this.membership.getUses().put("gold", uses);
        return this.membership.getUses();
    }
}
