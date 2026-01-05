package LLD.ParkingLot;

import java.time.Duration;

public class DynamicPricingStrategy implements PricingStrategy{
    @Override
    public double calculatePrice(Duration duration,double occupancyRate){
        double baseRatePerHour=50;
        double hours=Math.ceil(duration.toMinutes()/60.0);
        double demandMultiplier=1+occupancyRate;
        return baseRatePerHour*hours*demandMultiplier;
    }
}
