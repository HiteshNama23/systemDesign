package LLD.ParkingLot;

import java.time.Instant;
import java.util.UUID;

public class Ticket {
    private final String ticketId;
    private final String spotId;
    private final String vehicleNumber;
    private final Instant entryTime;
    public Ticket(String spotId,String vehicleNumber){
        this.ticketId=UUID.randomUUID().toString();
        this.spotId=spotId;
        this.vehicleNumber=vehicleNumber;
        this.entryTime=Instant.now();
    }
    public Instant getEntryTime(){
        return entryTime;
    }
    public String getSpotId(){
        return spotId;
    }
    public String getTicketId(){
        return ticketId;
    }
}
