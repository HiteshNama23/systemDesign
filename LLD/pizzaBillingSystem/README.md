# Pizza Billing System – System Design

## 1. Problem Statement
Design a pizza billing system that:
- Handles orders concurrently.
- Calculates final bills with discounts, tax, and possible extensions like delivery charges.
- Manages stock inventory.
- Notifies users of order status.
- Applies rate limiting to avoid overload.

## 2. Functional Requirements
- Place pizza orders.
- Apply promotional discounts.
- Compute subtotal, discount, tax.
- Update inventory safely.
- Notify user about order status.
- Rate-limit order submissions.

## 3. Non-Functional Requirements
- Scalability (multiple concurrent orders).
- Reliability (order completion consistency).
- Fault tolerance (failed payment or insufficient stock should not corrupt state).
- Performance (fast checkout).
- Security (safe payment handling).
- Extensibility (add new billing features like coupons, loyalty, surge pricing).

## 4. HLD
- **API Layer** → Accept orders.
- **Order Service** → Process business logic, billing decorators, inventory updates.
- **Inventory Service** → Manages stock with locks.
- **Billing Decorators** → Compute final bill with subtotal, discounts, tax.
- **Payment Service** → Charge customers.
- **Notifier Service** → Inform users.
- **Rate Limiter** → Protect system from overload.

## 5. Back-of-the-Envelope
- Assume 1k concurrent users.
- Each order requires 2-3 DB reads + 1 write.
- With caching, 5k RPS capacity.
- Rate limiter per node ensures fairness.

## 6. Schema Design
### Tables
- **Menu(id, name, price)**
- **Inventory(itemId, stock)**
- **Order(id, userId, status, promoCode, amount)**
- **OrderItem(orderId, menuItemId, qty)**

## 7. Database Choice
- **SQL** for strong consistency of orders.
- **Cache** (Redis) for menu and stock lookups.
- **Message Queue** (Kafka) for async notifications.

## 8. Scalability
- Horizontal scaling of OrderService.
- Rate-limiting at gateway.
- Load balancer to distribute requests.

## 9. Concurrency Control
- Inventory updates with fine-grained `ReentrantLock` per item.
- Atomic integers for stock count.
- Executor pool for parallel order processing.

## 10. Reliability & Fault Tolerance
- Failed orders trigger stock restock.
- Payment failures rollback state.
- Retry queue for notifier.

## 11. Security
- Token-based auth for APIs.
- Secure payment gateway integration.

## 12. Low-Level Design (LLD)
- **Factory Pattern** → `DiscountFactory` creates discount strategies.  
- **Strategy Pattern** → `DiscountStrategy` implementations (`NoDiscount`, `PercentageDiscount`).  
- **Decorator Pattern** → `Bill` interface with `BaseBill`, `DiscountDecorator`, `TaxDecorator`.  
- **Observer Pattern (simplified)** → Notifier informs user about status.  
- **Concurrency** → Locks & executor pool.  
- **Rate Limiter** → Token bucket.

## 13. Class Relationships
- `OrderService` depends on `Inventory`, `PaymentProcessor`, `Notifier`.  
- `Order` aggregates `OrderItem`.  
- `DiscountFactory` produces `DiscountStrategy`.  
- `Bill` implemented by `BaseBill` and wrapped by `BillDecorator` subclasses.  
- `DiscountDecorator` and `TaxDecorator` wrap `Bill` dynamically.  
- `TokenBucketRateLimiter` controls request frequency.

## 14. Tradeoffs
- Decorator vs hardcoding billing: Decorator gives flexibility but adds layers.  
- SQL vs NoSQL: SQL ensures order consistency but may limit horizontal scalability.  
- Locks vs Optimistic concurrency: Locks prevent race conditions but can reduce throughput.

## 15. Improvements
- Add DeliveryChargeDecorator, LoyaltyPointsDecorator.  
- Introduce async payment confirmation.  
- Implement distributed rate limiting (e.g., Redis).  
- Scale Notifier with message queues.

---
