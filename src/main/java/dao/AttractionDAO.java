package dao;

import java.util.List;

import domainModel.Attraction;
import domainModel.Customer;

public interface AttractionDAO extends DAO<Attraction, Integer> {
    public int getNextID() throws Exception;
 
    public List<Attraction> getAttractionsForCustomer(String fiscalCode) throws Exception;

    public void addBooking(String fiscalCode, Integer attractionId) throws Exception;

    public boolean deleteBooking(String fiscalCode, Integer attractionId) throws Exception;

    public List<Customer> getAttendees(Integer courseId) throws Exception;
/*    
	Attraction get(Integer id) throws Exception;//MATHY

	List<Attraction> getAll() throws Exception;//MATHY
*/	
}
