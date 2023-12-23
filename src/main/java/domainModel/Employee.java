package domainModel;

public class Employee extends Person {
    private float salary;

    public Employee(String surname, String name, String fiscalCode, float salary) {
        super(surname, name, fiscalCode);
        this.salary = salary;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                ", surname='" + getSurname() + '\'' +
                ", name='" + getName() + '\'' +
                ", fiscalCode='" + getFiscalCode() + '\'' +
                ", salary=" + salary +
                '}';
    }
}
