// O — Open/Closed Principle

// Software should be open for extension but closed for modification. Add new behavior via extension (e.g., new classes) without changing existing, tested code.

interface DiscountRule {
    double apply(double amount);
}

class NoDiscount implements DiscountRule {
    public double apply(double amount) { return amount; }
}

class PercentageDiscount implements DiscountRule {
    private final double percent;
    PercentageDiscount(double percent) { this.percent = percent; }
    public double apply(double amount) { return amount * (1 - percent); }
}

class ThresholdFixedDiscount implements DiscountRule {
    private final double threshold;
    private final double minus;
    ThresholdFixedDiscount(double threshold, double minus) {
        this.threshold = threshold; this.minus = minus;
    }
    public double apply(double amount) {
        return amount >= threshold ? amount - minus : amount;
    }
}

class Checkout {
    private DiscountRule rule;
    Checkout(DiscountRule rule) { this.rule = rule; }
    public double total(double amount) { return rule.apply(amount); }
    public void setRule(DiscountRule rule) { this.rule = rule; }
}

public class O {
    public static void main(String[] args) {
        Checkout checkout = new Checkout(new PercentageDiscount(0.1));
        System.out.println(checkout.total(1000));
        checkout.setRule(new ThresholdFixedDiscount(500, 100));
        System.out.println(checkout.total(1000));
    }
}
