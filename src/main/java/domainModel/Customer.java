package domainModel;
import domainModel.membership.Membership;


public class Customer extends Person {
    private Membership membership;

    public Customer(String surname, String name, String fiscalCode, Membership membership) {
        super(surname, name, fiscalCode);
        this.membership = membership;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "fiscalCode='" + getFiscalCode() + '\'' +
                ", name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", membership=" + getMembership() +
                '}';
    }
}