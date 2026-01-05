package LLD.ParkingLot;
public class FirstAvailableStrategy implements SpotAllocationStrategy {

    @Override
    public ParkingSpot allocateSpot(ParkingLot lot, Vehicle vehicle) {
        for (ParkingFloor floor : lot.getFloors()) {
            for (ParkingSpot spot : floor.getSpots()) {
                if (spot.isFree() && spot.tryOccupy(vehicle)) {
                    return spot;
                }
            }
        }
        return null;
    }
}
