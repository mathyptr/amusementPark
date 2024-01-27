package businessLogic;

import dao.CustomerDAO;
import domainModel.Customer;
import domainModel.membership.Membership;
import domainModel.membership.EmptyMembership;
import domainModel.membership.WorkdaysMembershipDecorator;
import domainModel.membership.SilverMembershipDecorator;

import java.time.LocalDate;

public class CustomersController extends PeopleController<Customer> {

    public CustomersController(CustomerDAO customerDAO) {
        super(customerDAO);
    }
   
    public String addPerson(String name, String surname,String fiscalCode, String[] membershipDecorators, LocalDate membershipEndDate) throws Exception {
        Membership m = new EmptyMembership(LocalDate.now(), membershipEndDate);

        for (String s : membershipDecorators) {
            if (s.equals("weekend")) m = new SilverMembershipDecorator(m);
            else if (s.equals("workdays")) m = new WorkdaysMembershipDecorator(m);
        }

        Customer c = new Customer(name, surname,fiscalCode, m);
        return super.addPerson(c);
    }
}