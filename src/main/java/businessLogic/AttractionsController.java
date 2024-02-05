package businessLogic;

import dao.AttractionDAO;
import domainModel.Attraction;
import domainModel.Employee;

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
     * Returns the given Attraction
     *   
     */
    public Attraction getAttraction(int id) throws Exception {
        return attractionDAO.get(id);
    }

    /**
     * Returns a read-only list of Attraction
    
     */
    public List<Attraction> getAll() throws Exception {
        return unmodifiableList(this.attractionDAO.getAll());
    }
    /**
     * Adds a new Attraction
     *
    */
    public int addAttraction(String name, int maxCapacity, LocalDateTime startDate, LocalDateTime endDate, String employeeFiscalCode) throws Exception {
    	Employee employee = employeeController.getPerson(employeeFiscalCode);
        if (employee == null)
            throw new IllegalArgumentException("Attraction not found");

        // Check if the given Attraction is not already occupied for the given time range
        for (Attraction attract : this.attractionDAO.getAll()) {
            if (attract.getEmployeeFiscalCode().equals(employeeFiscalCode)) {
                if ((attract.getStartDate().isBefore(endDate) || attract.getStartDate().equals(endDate))
                        && (attract.getEndDate().isAfter(startDate) || attract.getEndDate().equals(startDate)))
                    throw new IllegalArgumentException("The given Attraction is already occupied in the given time range (in Attraction #" + attract.getId() + ")");
            }
        }

        Attraction attract = new Attraction(attractionDAO.getNextID(), name, maxCapacity, startDate, endDate, employee.getFiscalCode());
        attractionDAO.insert(attract);
        return attract.getId();
    }

    /**
     * Deletes the given Attraction 
     */
    public boolean removeAttraction(int id) throws Exception {
        return attractionDAO.delete(id);
    }
   
}
