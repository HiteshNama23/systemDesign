package LLD.ParkingLot;

public class Vehicle {
    private final String vehicleNumber;
    private final VehicleType type;
    public Vehicle(String vehicleNumber,VehicleType type){
       this.vehicleNumber=vehicleNumber;
       this.type=type;
    }
    public VehicleType getType(){
        return type;
    }
    public String getVehicleNumber(){
        return vehicleNumber;
    }
}
