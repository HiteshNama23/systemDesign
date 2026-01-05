# 📐 Class Diagram – Multi Level Parking System

This class diagram represents the **Low-Level Design (LLD)** of a **Multi-Level Parking System**.

Rendered using **Mermaid**, which is natively supported by GitHub.

---

## 🧩 UML Class Diagram

```mermaid
classDiagram

%% ================= ENUMS =================
class VehicleType {
  <<enumeration>>
  CAR
  BIKE
  TRUCK
}

%% ================= DOMAIN MODELS =================
class Vehicle {
  -String vehicleNumber
  -VehicleType type
}

class Ticket {
  -String ticketId
  -String spotId
  -String vehicleNumber
  -Instant entryTime
}

class ParkingSpot {
  -String spotId
  -VehicleType supportedType
  -boolean occupied
  +tryOccupy(Vehicle) boolean
  +release() void
}

class ParkingFloor {
  -int floorNumber
  -List~ParkingSpot~ spots
}

class ParkingLot {
  -List~ParkingFloor~ floors
}

%% ================= STRATEGY LAYER =================
class SpotAllocationStrategy {
  <<interface>>
  +allocateSpot(ParkingLot, Vehicle) ParkingSpot
}

class FirstAvailableStrategy

class PricingStrategy {
  <<interface>>
  +calculatePrice(Duration, double) double
}

class DynamicPricingStrategy

%% ================= SUPPORT =================
class WaitingListManager {
  -Queue~Vehicle~ queue
  +add(Vehicle) void
  +poll() Vehicle
}

%% ================= SERVICE =================
class ParkingService {
  -ParkingLot parkingLot
  -SpotAllocationStrategy allocationStrategy
  -PricingStrategy pricingStrategy
  -WaitingListManager waitingListManager
  +parkVehicle(Vehicle) Ticket
  +exitVehicle(Ticket) double
}

%% ================= RELATIONSHIPS =================
Vehicle --> VehicleType

ParkingLot "1" o-- "*" ParkingFloor
ParkingFloor "1" o-- "*" ParkingSpot

Ticket --> Vehicle
Ticket --> ParkingSpot

ParkingService --> ParkingLot
ParkingService --> SpotAllocationStrategy
ParkingService --> PricingStrategy
ParkingService --> WaitingListManager

FirstAvailableStrategy ..|> SpotAllocationStrategy
DynamicPricingStrategy ..|> PricingStrategy
