package businessLogic;

import dao.EmployeeDAO;
import domainModel.Employee;

public class EmployeesController extends PeopleController<Employee> {

    public EmployeesController(EmployeeDAO employeeDAO) {
        super(employeeDAO);
    }
}
