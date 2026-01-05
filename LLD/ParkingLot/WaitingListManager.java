package LLD.ParkingLot;

import java.util.concurrent.ConcurrentLinkedQueue;

public class WaitingListManager {
    private final ConcurrentLinkedQueue<Vehicle>queue=new ConcurrentLinkedQueue<>();
    public void add(Vehicle vehicle){
        queue.offer(vehicle);
    }
    public Vehicle poll(){
        return queue.poll();
    }
    public boolean isEmpty(){
        return queue.isEmpty();
    }
}
