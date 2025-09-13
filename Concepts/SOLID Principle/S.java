// S — Single Responsibility Principle

// A class should have one reason to change—one focused responsibility. This keeps code easier to test, reuse, and modify.
class Order {
    private final String id;
    private final double amount;
    Order(String id, double amount) { this.id = id; this.amount = amount; }
    public String getId() { return id; }
    public double getAmount() { return amount; }
}

class PaymentProcessor {
    public boolean charge(Order order) { return order.getAmount() > 0; }
}

class EmailNotifier {
    public void sendReceipt(String orderId) {
        System.out.println("Receipt sent for " + orderId);
    }
}

class OrderService {
    private final PaymentProcessor paymentProcessor;
    private final EmailNotifier emailNotifier;
    OrderService(PaymentProcessor p, EmailNotifier e) {
        this.paymentProcessor = p; this.emailNotifier = e;
    }
    public void place(Order order) {
        if (paymentProcessor.charge(order)) {
            emailNotifier.sendReceipt(order.getId());
        }
    }
}

public class S{
    public static void main(String[] args) {
        Order order = new Order("A101", 500);
        OrderService service = new OrderService(new PaymentProcessor(), new EmailNotifier());
        service.place(order);
    }
}
