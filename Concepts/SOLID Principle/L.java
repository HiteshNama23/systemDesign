// L — Liskov Substitution Principle

// Subtypes must be usable anywhere their base type is expected without breaking correctness. Model capabilities with the right abstractions so substitutions are safe.

interface Payment {
    boolean authorize(double amount);
    void capture(double amount);
}

class CreditCardPayment implements Payment {
    public boolean authorize(double amount) { return amount > 0 && amount <= 100000; }
    public void capture(double amount) { System.out.println("Captured card " + amount); }
}

class UpiPayment implements Payment {
    public boolean authorize(double amount) { return amount > 0 && amount <= 50000; }
    public void capture(double amount) { System.out.println("Captured UPI " + amount); }
}

class CheckoutService {
    public void pay(Payment payment, double amount) {
        if (payment.authorize(amount)) {
            payment.capture(amount);
        }
    }
}

public class L{
    public static void main(String[] args) {
        CheckoutService service = new CheckoutService();
        service.pay(new CreditCardPayment(), 2000);
        service.pay(new UpiPayment(), 2000);
    }
}
