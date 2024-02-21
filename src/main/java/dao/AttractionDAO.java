package dao;

import java.util.List;

import domainModel.Attraction;
import domainModel.Customer;

public interface AttractionDAO extends DAO<Attraction, Integer> {
    public int getNextID() throws Exception;

     /**
     * Returns the list of attractions that the given customer has booked
     *
     * @param fiscalCode The fiscal code of the customer to get the booked attractions
     * @return The list of attractions booked by the given customer
     * @throws Exception when the customer doesn't exist or if there are problems to access the data source
     */
    public List<Attraction> getAttractionsForCustomer(String fiscalCode) throws Exception;

    /**
     * Adds a booking for the given customer to the attraction with the given id
     *
     * @param fiscalCode The fiscal code of the customer to book the attraction for
     * @param attractionId The id of the attraction to book
     * @throws Exception when the attraction or the customer doesn't exist or if there are problems to access the data source
     */
    public void addBooking(String fiscalCode, Integer attractionId) throws Exception;

    /**
     * Removes a booking for the given customer from the attraction with the given id
     *
     * @param fiscalCode The fiscal code of the customer to remove the booking for
     * @param attractionId The id of the attraction to remove the booking from
     * @return true if successful, false otherwise (i.e. customer not found in attraction or attractionId not exiting)
     * @throws Exception if there are problems to access the data source
     */

    public boolean deleteBooking(String fiscalCode, Integer attractionId) throws Exception;

    /**
     * Returns the list of customers that are booked for the attraction with the given id.
     *
     * @param attractionId The id of the attraction to get the customers that are booked for it
     * @return The list of customers that are booked for the attraction with the given id
     * @throws Exception when the attraction doesn't exist or if there are problems to access the data source
     */
    public List<Customer> getAttendees(Integer attractionId) throws Exception;
}