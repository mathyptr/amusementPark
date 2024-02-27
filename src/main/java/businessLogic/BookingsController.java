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
    private MessagesBundle msgB;

    public BookingsController(AttractionsController attractionsController, CustomersController customersController, AttractionDAO bookingsDAO, MembershipDAO membershipDAO) {
        this.attractionsController = attractionsController;
        this.customersController = customersController;
        this.attractionsDAO = bookingsDAO;
        this.membershipDAO = membershipDAO;
        this.msgB = MessagesBundle.getInstance();
    }

    /**
     * Books an attraction for the given customer
     *
     * @param customerFiscalCode The fiscal code of the customer for whom to book the attraction for
     * @param courseId           The attraction id to book
     *
     * @throws Exception when attractionId doesn't exist and bubbles up exceptions
     */
    public void bookAttraction(String customerFiscalCode, int attractionId) throws Exception {
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
     * Check an attraction for the given customer
     *
     * @param customerFiscalCode The fiscal code of the customer for whom to check the attraction for
     * @param courseId           The attraction id to book
     *
     * @throws Exception when attractionId doesn't exist and bubbles up exceptions
     */
    public void checkBookAttraction(String customerFiscalCode, int attractionId) throws Exception {
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
     *
     * @param customerFiscalCode The fiscal code of the customer for whom to remove the booking
     * @param attractionId           The Attraction id to remove
     *
     * @return true if successful, false otherwise (i.e. customer not found in Attraction or attractionId not exiting)
     *
     * @throws Exception bubbles up exceptions
    */
    public boolean deleteAttractionBooking(String customerFiscalCode, int attractionId) throws Exception {
        return attractionsDAO.deleteBooking(customerFiscalCode, attractionId);
    }

    /**
     * Returns a list of the Attractions that the given user booked
     *
     * @param customerFiscalCode The fiscal code of the customer
     *
     * @return A list of Attractions that the given user booked
     *
     * @throws Exception bubbles up exceptions
     */
    public List<Attraction> getBookingsForCustomer(String customerFiscalCode) throws Exception {
        return attractionsDAO.getAttractionsForCustomer(customerFiscalCode);
    }
}
