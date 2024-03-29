package domainModel;

import java.time.LocalDateTime;
import java.util.Objects;

import util.MessagesBundle;


public class Attraction {
    private final Integer id;
    private final String name;
    private String description;
    private String status;
    private final int maxCapacity;
    private int adrenaline;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final String employeeFiscalCode;   

    
    public Attraction(int id, String name, int maxCapacity, int adrenaline, LocalDateTime startDate, LocalDateTime endDate, String employeeFiscalCode, String description, String status) {

        this.employeeFiscalCode = employeeFiscalCode;
    	
    	this.name = name;
    	this.description = description;
    	this.status = status;
        this.maxCapacity = maxCapacity;
        this.adrenaline = adrenaline;
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
    
    public int getAdrenaline() {
    	return adrenaline;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
    public String getEmployeeFiscalCode() {
        return employeeFiscalCode;
    }

    public String getDescription() {
        return description;
    }
       
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
    	this.status = status;
    }
    
}
