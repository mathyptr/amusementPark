package businessLogic;

import dao.AttractionDAO;
import domainModel.Attraction;
import domainModel.Employee;
import util.MessagesBundle;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Collections.unmodifiableList;


public class AttractionsController {
    private final PeopleController<Employee> employeeController;
    private final AttractionDAO attractionDAO;

    public AttractionsController(PeopleController<Employee> employeeController, AttractionDAO attractionDAO) {
        this.employeeController = employeeController;
        this.attractionDAO = attractionDAO;
    }
    
    /**
     * Returns an Attraction
     *
     * @return The Attraction
     *
     * @throws Exception bubbles up exceptions
     */
    public Attraction getAttraction(int id) throws Exception {
        return attractionDAO.get(id);
    }

    /**
     * Returns a list of Attraction
     *
     * @return The list of Attraction
     *
     * @throws Exception bubbles up exceptions
     */
    public List<Attraction> getAll() throws Exception {
        return unmodifiableList(this.attractionDAO.getAll());
    }
    
    /**
     * Adds a new Attraction to the list
     *
     * @param name              The name of the Attraction
     * @param maxCapacity       The maximum attendees for the Attraction
     * @param startDate         The start date of the Attraction
     * @param endDate           The end date of the Attraction
     * @param trainerFiscalCode The fiscal code of the trainer for the Attraction
     *
     * @return The id of the newly created course
     *
     * @throws Exception If the employee is not found, bubbles up exceptions
     * @throws IllegalArgumentException If the employee is already occupied in the given time range
     */
    public int addAttraction(String name, int maxCapacity, LocalDateTime startDate, LocalDateTime endDate, String employeeFiscalCode) throws Exception {
        MessagesBundle msgB = MessagesBundle.getInstance();    	
    	Employee employee = employeeController.getPerson(employeeFiscalCode);
        if (employee == null)
            throw new IllegalArgumentException(msgB.GetResourceValue("Employee_Not_found"));

        // Check if the given Attraction is not already occupied for the given time range
        for (Attraction attract : this.attractionDAO.getAll()) {
            if (attract.getEmployeeFiscalCode().equals(employeeFiscalCode)) {
                if ((attract.getStartDate().isBefore(endDate) || attract.getStartDate().equals(endDate))
                        && (attract.getEndDate().isAfter(startDate) || attract.getEndDate().equals(startDate)))
                    throw new IllegalArgumentException(msgB.GetResourceValue("Attraction_busy_in_time_range") + attract.getId() + ")");
            }
        }

        Attraction attract = new Attraction(attractionDAO.getNextID(), name, maxCapacity, startDate, endDate, employee.getFiscalCode());
        attractionDAO.insert(attract);
        return attract.getId();
    }

    /**
    * Deletes the given Attraction from the list
    *
    * @param id The id of the Attraction to delete
    *
    * @return true if successful, false otherwise
    *
    * @throws Exception bubbles up exceptions of Co
    */
    public boolean removeAttraction(int id) throws Exception {
        return attractionDAO.delete(id);
    }
   
}
