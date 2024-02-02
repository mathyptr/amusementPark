package businessLogic;


import dao.AttractionDAO;
import dao.MembershipDAO;
import dao.SqlMembershipDAO;
import domainModel.Attraction;
import domainModel.Customer;
import domainModel.membership.Membership;

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
    	Attraction c = attractionsController.getAttraction(attractionId);
        Customer customer = customersController.getPerson(customerFiscalCode);
        if (c == null) throw new RuntimeException("The given attraction id does not exist");
        if (customer == null) throw new RuntimeException("The given customer does not exist");

        List<Customer> attendees = attractionsDAO.getAttendees(attractionId);
        if (attendees.size() == c.getMaxCapacity())
            throw new RuntimeException("This attraction if full, can't book");
        if (attendees.contains(customer))
            throw new RuntimeException("The given customer is already booked for this attraction");

        getBookingsForCustomer(customerFiscalCode).forEach(attraction -> {
            LocalDateTime c1 = attraction.getStartDate().truncatedTo(ChronoUnit.HOURS);
            LocalDateTime c2 = c.getStartDate().truncatedTo(ChronoUnit.HOURS);
            if (c1.equals(c2))
                throw new RuntimeException("The given customer is already booked for a attraction at the same time");
        });
//MATHY controllare che    customer.getMembership() non ritorni NULL
        if (customer.getMembership().isExpired())
            throw new RuntimeException("The membership of the given user is expired");
        if (!customer.getMembership().isValidForInterval(c.getStartDate(), c.getEndDate()))
            throw new RuntimeException("The membership of the given user is not valid for this attraction");

        // Update membership (update uses field)
        membershipDAO.updateOfCustomer(customer.getFiscalCode(), customer.getMembership());

        attractionsDAO.addBooking(customer.getFiscalCode(), attractionId);
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
