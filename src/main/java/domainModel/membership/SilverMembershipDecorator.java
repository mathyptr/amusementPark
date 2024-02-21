package domainModel.membership;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import util.MessagesBundle;

/**
 * Decorator that lets the membership be valid on Saturday and Sunday
 */
public class SilverMembershipDecorator extends MembershipDecorator {
    int uses;
    private final float silverPrice = 80;
	private final Logger logger = LogManager.getLogger("SilverMembershipDecorator");
    public SilverMembershipDecorator(Membership membership) {
        super(membership);
    }

    public SilverMembershipDecorator(Membership membership, int uses) {
        super(membership);
        this.uses = uses;
    }

    @Override
    public int getLocalUses() {
        return uses;
    }    
    
/*    @Override
    public void setUses(int uses) {
    	this.uses = uses;
    } 
*/    
        
    @Override
    public String getUsesDescription() {
        MessagesBundle msgB = MessagesBundle.getInstance();      	
        return super.getUsesDescription() + msgB.GetResourceValue("Silver_uses") + uses + ", ";
    }    

    @Override
    public float getPrice() {
        return super.getPrice() + silverPrice;
    }
        
    @Override
    public boolean isValidForInterval(LocalDateTime start, LocalDateTime end) {
        if (!this.isDateIntervalValid(start, end)) return false;
        boolean isCurrTrue = (isOnWeekend(start) && isOnWeekend(end)); 
        if (isCurrTrue) uses++;
        else logger.debug(MessagesBundle.GetResourceValue("Memership_Not_Valid_For_Interval")+"("+start.toString()+","+end.toString()+")");
        return super.isValidForInterval(start, end) || isCurrTrue;
    }
    
    private boolean isOnWeekend(LocalDateTime date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }
    
    @Override
    public HashMap<String, Integer> getUses() {
        this.membership.getUses().put("silver", uses);
        return this.membership.getUses();
    }


}
