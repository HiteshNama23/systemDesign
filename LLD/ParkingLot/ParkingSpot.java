package LLD.ParkingLot;

import java.util.concurrent.locks.ReentrantLock;

public class ParkingSpot {
    private final String spotId;
    private final VehicleType supportedType;
    private boolean occupied;
    private Vehicle vehicle;
    private final ReentrantLock lock = new ReentrantLock();
    public ParkingSpot(String spotId,VehicleType supportedType){
        this.spotId=spotId;
        this.supportedType=supportedType;
    }
    public boolean tryOccupy(Vehicle v){
        lock.lock();
        try{
            if(occupied||v.getType()!=supportedType)return false;
            occupied=true;
            vehicle=v;
            return true;
        }finally{
            lock.unlock();
        }
    }
    public void release(){
        lock.lock();
        try{
            vehicle=null;
            occupied=false;
        }finally{
            lock.unlock();
        }
    }
    public boolean isFree(){
        return !occupied;
    }
}
