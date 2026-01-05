package LLD.ParkingLot;
public interface SpotAllocationStrategy {
    ParkingSpot allocateSpot(ParkingLot lot, Vehicle vehicle);
}
