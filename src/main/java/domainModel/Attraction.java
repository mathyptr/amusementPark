package domainModel;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import db.dbManager;
import util.MessagesBundle;


public class Attraction {
    private final Integer id;
    private final String name;
    private final int maxCapacity;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final String employeeFiscalCode;
	private final Logger logger = LogManager.getLogger("Attraction.class");    
//    private final Employee employee;  
    
    public Attraction(int id, String name, int maxCapacity, LocalDateTime startDate, LocalDateTime endDate, String employeeFiscalCode) {

        this.employeeFiscalCode = employeeFiscalCode;
    	
    	this.name = name;
        this.maxCapacity = maxCapacity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.id = id;
        MessagesBundle msgB = MessagesBundle.getInstance();  
        if (endDate.equals(startDate) || endDate.isBefore(startDate))
            throw new IllegalArgumentException(msgB.GetResourceValue("endDate_is_smaller_startDate"));
        

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
//MATHY Realizziamo anche un metodo che ritorna il Codice Fiscale dell'addetto assegnato all'attrazione
    public String getEmployeeFiscalCode() {
        return employeeFiscalCode;
    }
  //MATHY Realizziamo anche un metodo che associa l'addetto assegnato all'attrazione
    /*    public  setEmployee(Employee employee) {
		this.employee = employee;
        }*/
}
