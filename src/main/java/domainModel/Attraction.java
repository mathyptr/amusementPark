package domainModel;

import java.time.LocalDateTime;
import java.util.Objects;

import util.MessagesBundle;


public class Attraction {
    private final Integer id;
    private final String name;
    private final int maxCapacity;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

//MATHY   un'attrazione ha anche l'addetto: dobbiamo implementare la classe Employee 
//    private final Employee employee;  
    
    public Attraction(int id, String name, int maxCapacity, LocalDateTime startDate, LocalDateTime endDate) {
// MATHY   dobbiamo passare anche l'oggetto Employee
//        this.employee = employee;
    	
    	this.name = name;
        this.maxCapacity = maxCapacity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.id = id;

        if (endDate.equals(startDate) || endDate.isBefore(startDate))
            throw new IllegalArgumentException("endDate_is_smaller_startDate");
        

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attraction attraction = (Attraction) o;
        return Objects.equals(id, attraction.id);
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
//MATHY Realizziamo anche un metodo che ritorna l'addetto assegnato all'attrazione
/*    public Employee getEmployee() {
        return employee;
    }*/
  //MATHY Realizziamo anche un metodo che associa l'addetto assegnato all'attrazione
    /*    public  setEmployee(Employee employee) {
		this.employee = employee;
        }*/
}
