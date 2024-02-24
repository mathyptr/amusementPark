package businessLogic;

import dao.AttractionDAO;
import dao.MembershipDAO;
import domainModel.Attraction;
import domainModel.Customer;
import util.MessagesBundle;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


public class BookingsController {
    private final AttractionsController attractionsController;
    private final CustomersController customersController;
    private final AttractionDAO attractionsDAO;
    private final MembershipDAO membershipDAO;

    public BookingsController(AttractionsController attractionsController, CustomersController customersController, AttractionDAO bookingsDAO, MembershipDAO membershipDAO) {
        this.attractionsController = attractionsController;
        this.customersController = customersController;
        this.attractionsDAO = bookingsDAO;
        this.membershipDAO = membershipDAO;
    }

    /**
     * Books an attraction for the given customer
    */
    public void bookAttraction(String customerFiscalCode, int attractionId) throws Exception {
        MessagesBundle msgB = MessagesBundle.getInstance();    	
    	Attraction c = attractionsController.getAttraction(attractionId);
        Customer customer = customersController.getPerson(customerFiscalCode);
              
        if (c == null) throw new RuntimeException( msgB.GetResourceValue("Attraction_ID_not_valid"));
        if (customer == null) throw new RuntimeException(msgB.GetResourceValue("Customer_Not_found"));

        List<Customer> attendees = attractionsDAO.getAttendees(attractionId);
        if (attendees.size() == c.getMaxCapacity())
            throw new RuntimeException(msgB.GetResourceValue("Attraction_Full"));
        if (attendees.contains(customer))
            throw new RuntimeException(msgB.GetResourceValue("Customer_already_booked_attraction"));

        getBookingsForCustomer(customerFiscalCode).forEach(attraction -> {
            LocalDateTime c1 = attraction.getStartDate().truncatedTo(ChronoUnit.HOURS);
            LocalDateTime c2 = c.getStartDate().truncatedTo(ChronoUnit.HOURS);
            if (c1.equals(c2))
                throw new RuntimeException(msgB.GetResourceValue("Customer_booked_attraction_same_time"));
        });

        if (customer.getMembership()==null)
             throw new RuntimeException(msgB.GetResourceValue("User_membership_null"));	
        if (customer.getMembership().isExpired())
            throw new RuntimeException(msgB.GetResourceValue("User_membership_expired"));
        if (!customer.getMembership().isValidForInterval(c.getStartDate(), c.getEndDate()))
            throw new RuntimeException(msgB.GetResourceValue("User_membership_not_valid"));

        // Update membership (update uses field)
        membershipDAO.updateOfCustomer(customer.getFiscalCode(), customer.getMembership());

        attractionsDAO.addBooking(customer.getFiscalCode(), attractionId);
    }

    /**
     * Books an attraction for the given customer
         */
    public void checkBookAttraction(String customerFiscalCode, int attractionId) throws Exception {
        MessagesBundle msgB = MessagesBundle.getInstance();    	
    	Attraction c = attractionsController.getAttraction(attractionId);
        Customer customer = customersController.getPerson(customerFiscalCode);
              
        if (c == null) throw new RuntimeException( msgB.GetResourceValue("Attraction_ID_not_valid"));
        if (customer == null) throw new RuntimeException(msgB.GetResourceValue("Customer_Not_found"));

        List<Customer> attendees = attractionsDAO.getAttendees(attractionId);
        if (attendees.size() == c.getMaxCapacity())
            throw new RuntimeException(msgB.GetResourceValue("Attraction_Full"));
        if (attendees.contains(customer))
            throw new RuntimeException(msgB.GetResourceValue("Customer_already_booked_attraction"));

        getBookingsForCustomer(customerFiscalCode).forEach(attraction -> {
            LocalDateTime c1 = attraction.getStartDate().truncatedTo(ChronoUnit.HOURS);
            LocalDateTime c2 = c.getStartDate().truncatedTo(ChronoUnit.HOURS);
            if (c1.equals(c2))
                throw new RuntimeException(msgB.GetResourceValue("Customer_booked_attraction_same_time"));
        });
        
        if (customer.getMembership()==null)
            throw new RuntimeException(msgB.GetResourceValue("User_membership_null"));        	
        if (customer.getMembership().isExpired())
            throw new RuntimeException(msgB.GetResourceValue("User_membership_expired"));
        if (!customer.getMembership().isValidForInterval(c.getStartDate(), c.getEndDate()))
            throw new RuntimeException(msgB.GetResourceValue("User_membership_not_valid"));

     }  
    
    /**
     * Deletes a booking for the given customer
   */
    public boolean deleteAttractionBooking(String customerFiscalCode, int attractionId) throws Exception {
        return attractionsDAO.deleteBooking(customerFiscalCode, attractionId);
    }

    /**
     * Returns a list of the attractions that the given user booked
      */
    public List<Attraction> getBookingsForCustomer(String customerFiscalCode) throws Exception {
        return attractionsDAO.getAttractionsForCustomer(customerFiscalCode);
    }
}
