# Class Diagram – Pizza Billing System

```mermaid
classDiagram
    class Order {
        +id: String
        +userId: String
        +items: List<OrderItem>
        +promoCode: String
        +status: String
    }

    class OrderItem {
        +menuItemId: String
        +qty: int
    }

    class Menu {
        +items: List<MenuItem>
    }

    class MenuItem {
        +id: String
        +name: String
        +priceCents: int
    }

    class Inventory {
        +tryReserve(itemId: String, qty: int) boolean
        +restock(itemId: String, qty: int)
    }

    class OrderService {
        +submitOrder(order: Order, callback: Consumer)
        +process(order: Order) boolean
    }

    class PaymentProcessor {
        +charge(orderId: String, amountCents: int) boolean
    }

    class Notifier {
        <<interface>>
        +notifyStatus(orderId: String, status: String)
    }

    class ConsoleNotifier {
        +notifyStatus(orderId: String, status: String)
    }

    class DiscountStrategy {
        <<interface>>
        +applyDiscountCents(subtotal: int) int
    }

    class NoDiscount {
        +applyDiscountCents(subtotal: int) int
    }

    class PercentageDiscount {
        -percent: int
        +applyDiscountCents(subtotal: int) int
    }

    class DiscountFactory {
        +fromCode(code: String) DiscountStrategy
    }

    class Bill {
        <<interface>>
        +getCostCents(): int
        +getDescription(): String
    }

    class BaseBill {
        +getCostCents(): int
        +getDescription(): String
    }

    class BillDecorator {
        <<abstract>>
        -wrapped: Bill
    }

    class DiscountDecorator {
        +getCostCents(): int
        +getDescription(): String
    }

    class TaxDecorator {
        +getCostCents(): int
        +getDescription(): String
    }

    class TokenBucketRateLimiter {
        +tryConsume(): boolean
    }

    Order --> OrderItem
    OrderService --> Inventory
    OrderService --> PaymentProcessor
    OrderService --> Notifier
    DiscountFactory --> DiscountStrategy
    DiscountDecorator --> DiscountStrategy
    Bill <|.. BaseBill
    Bill <|.. BillDecorator
    BillDecorator <|-- DiscountDecorator
    BillDecorator <|-- TaxDecorator
    ConsoleNotifier ..|> Notifier
    NoDiscount ..|> DiscountStrategy
    PercentageDiscount ..|> DiscountStrategy
