package LLD.ParkingLot;

import java.time.Duration;

public interface PricingStrategy {
    double calculatePrice(Duration duration,double occupancyRate);
}
