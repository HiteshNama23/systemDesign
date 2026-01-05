# 📐 Class Diagram – Multi Level Parking System

This document contains the **UML Class Diagram** for the **Multi-Level Parking System** designed using **SOLID principles** and **design patterns**.

The diagram is written using **PlantUML** and can be rendered using:
- IntelliJ / VS Code PlantUML plugin
- Online PlantUML renderer
- GitHub (with PlantUML support)

---

## 🧩 UML Class Diagram (PlantUML)

```plantuml
@startuml
skinparam classAttributeIconSize 0

' ================= ENUMS =================

enum VehicleType {
  CAR
  BIKE
  TRUCK
}

' ================= DOMAIN MODELS =================

class Vehicle {
  - vehicleNumber : String
  - type : VehicleType
}

class Ticket {
  - ticketId : String
  - spotId : String
  - vehicleNumber : String
  - entryTime : Instant
}

class ParkingSpot {
  - spotId : String
  - supportedType : VehicleType
  - occupied : boolean
  + tryOccupy(vehicle : Vehicle) : boolean
  + release() : void
}

class ParkingFloor {
  - floorNumber : int
  - spots : List<ParkingSpot>
}

class ParkingLot {
  - floors : List<ParkingFloor>
}

' ================= STRATEGY LAYER =================

interface SpotAllocationStrategy {
  + allocateSpot(lot : ParkingLot, vehicle : Vehicle) : ParkingSpot
}

class FirstAvailableStrategy {
}

interface PricingStrategy {
  + calculatePrice(duration : Duration, occupancyRate : double) : double
}

class DynamicPricingStrategy {
}

' ================= SUPPORT =================

class WaitingListManager {
  - queue : Queue<Vehicle>
  + add(vehicle : Vehicle) : void
  + poll() : Vehicle
}

' ================= SERVICE =================

class ParkingService {
  - parkingLot : ParkingLot
  - allocationStrategy : SpotAllocationStrategy
  - pricingStrategy : PricingStrategy
  - waitingListManager : WaitingListManager
  + parkVehicle(vehicle : Vehicle) : Ticket
  + exitVehicle(ticket : Ticket) : double
}

' ================= RELATIONSHIPS =================

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

@enduml
