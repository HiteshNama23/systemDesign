package LLD.ParkingLot;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ParkingService {
    private final ParkingLot parkingLot;
    private final SpotAllocationStrategy allocationStrategy;
    private final PricingStrategy pricingStrategy;
    private final WaitingListManager waitingList;

    private final Map<String,Ticket> activeTickets=new ConcurrentHashMap<>();
    private final ReentrantLock bookingLock = new ReentrantLock();

    public ParkingService(ParkingLot parkingLot,SpotAllocationStrategy allocationStrategy,PricingStrategy pricingStrategy,WaitingListManager waitingList){
        this.parkingLot=parkingLot;
        this.allocationStrategy=allocationStrategy;
        this.pricingStrategy=pricingStrategy;
        this.waitingList=waitingList;
    }
    public Ticket parkVehicle(Vehicle vehicle){
        bookingLock.lock();
        try{
            ParkingSpot spot=allocationStrategy.allocateSpot(parkingLot,vehicle);
            if(spot==null){
                waitingList.add(vehicle);
                return null;
            }
            Ticket ticket = new Ticket(spot.toString(),vehicle.getVehicleNumber());
            activeTickets.put(ticket.toString(),ticket);
            return ticket;
        }finally{
            bookingLock.unlock();
        }
    }
    public double exitVehicle(Ticket ticket){
        Ticket stored=activeTickets.remove(ticket.toString());
        Duration duration = Duration.between(stored.getEntryTime(),Instant.now());
        double occupancyRate=calculateOccupancyRate();
        double price=pricingStrategy.calculatePrice(duration,occupancyRate);
        processWaitingList();
        return price;
    }
    private void processWaitingList(){
        if(!waitingList.isEmpty()){
            Vehicle next=waitingList.poll();
            if(next!=null){
                parkVehicle(next);
            }
        }
    }
    private double calculateOccupancyRate(){
        int total=0;
        int occupied=0;
        for(ParkingFloor floor:parkingLot.getFloors()){
            for(ParkingSpot spot:floor.getSpots()){
                total++;
                if(!spot.isFree())occupied++;
            }
        }
        return (double)occupied/total;
    }
}
