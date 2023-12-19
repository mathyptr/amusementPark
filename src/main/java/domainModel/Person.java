package domainModel;

import java.util.Objects;

public abstract class Person {
	
    private final String surname;
    private final String name;
    private final String fiscalCode;
    
    public Person(String surname, String name,String fiscalCode) {
        this.surname = surname;
        this.name = name;
        this.fiscalCode = fiscalCode;        
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
        	return true;
        if (o == null || getClass() != o.getClass())
        	return false;
        
        Person person = (Person) o;
        
        return Objects.equals(fiscalCode, person.fiscalCode);
    }
}