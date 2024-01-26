package businessLogic;

import dao.EmployeeDAO;
import domainModel.Employee;


public class EmployeesController extends PeopleController<Employee> {

    public EmployeesController(EmployeeDAO employeeDAO) {
        super(employeeDAO);
    }
    
    /**
     * Add a new employee
   */
    public String addPerson(String name, String surname, String fiscalCode, float salary) throws Exception {
    	Employee employee = new Employee(name, surname,fiscalCode,salary);
        return super.addPerson(employee);
    }    
}