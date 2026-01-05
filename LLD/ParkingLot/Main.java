package LLD.ParkingLot;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        /* ========= INITIALIZE PARKING LOT ========= */

        List<ParkingFloor> floors = new ArrayList<>();

        for (int i = 1; i <= 2; i++) {
            List<ParkingSpot> spots = new ArrayList<>();
            for (int j = 1; j <= 5; j++) {
                spots.add(new ParkingSpot("F" + i + "S" + j, VehicleType.CAR));
            }
            floors.add(new ParkingFloor(i, spots));
        }

        ParkingLot parkingLot = new ParkingLot(floors);

        /* ========= SERVICES ========= */

        SpotAllocationStrategy allocationStrategy = new FirstAvailableStrategy();
        PricingStrategy pricingStrategy = new DynamicPricingStrategy();
        WaitingListManager waitingListManager = new WaitingListManager();

        ParkingService parkingService = new ParkingService(
                parkingLot,
                allocationStrategy,
                pricingStrategy,
                waitingListManager
        );

        /* ========= STORE ACTIVE TICKETS ========= */
        Map<String, Ticket> issuedTickets = new HashMap<>();

        System.out.println("=== Multi Level Parking System ===");

        while (true) {

            System.out.println("\n1. Park Vehicle");
            System.out.println("2. Exit Vehicle");
            System.out.println("3. Exit");
            System.out.print("Choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1: {
                    System.out.print("Enter vehicle number: ");
                    String vehicleNumber = sc.next();

                    Vehicle vehicle = new Vehicle(vehicleNumber, VehicleType.CAR);
                    Ticket ticket = parkingService.parkVehicle(vehicle);

                    if (ticket == null) {
                        System.out.println("Parking full. Added to waiting list.");
                    } else {
                        issuedTickets.put(ticket.getTicketId(), ticket);
                        System.out.println("Vehicle parked successfully");
                        System.out.println("Ticket ID: " + ticket.getTicketId());
                        System.out.println("Spot ID : " + ticket.getSpotId());
                    }
                    break;
                }

                case 2: {
                    System.out.print("Enter ticket ID: ");
                    String ticketId = sc.next();

                    Ticket ticket = issuedTickets.remove(ticketId);

                    if (ticket == null) {
                        System.out.println("Invalid ticket ID!");
                        break;
                    }

                    double amount = parkingService.exitVehicle(ticket);
                    System.out.println("Vehicle exited");
                    System.out.println("Amount to pay: ₹" + amount);
                    break;
                }

                case 3:
                    System.out.println("System shutting down...");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
