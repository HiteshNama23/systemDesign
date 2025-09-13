// package DesignPatterns;
// import java.util.*;
import java.util.Scanner;
// Strategy Design Pattern
// When do we need it?
// When a class has multiple behaviors/algorithms, and you want to switch them dynamically without changing the main class code.
// Example:
// A payment system (PayPal, CreditCard, UPI).
// A sorting algorithm (QuickSort, MergeSort, BubbleSort).
// A navigation app (Drive, Walk, Bike).
// Instead of writing if-else everywhere, we encapsulate algorithms into separate classes and make them interchangeable.

//  Flow of Code
// Strategy Interface → defines common behavior.
// Concrete Strategies → different implementations of the behavior.
// Context Class → uses a strategy; can switch dynamically.
// Main Class → client sets which strategy to use.

interface PaymentStrategy{
    void pay(int amount);
}
class CreditCardPayment implements PaymentStrategy{
    private String cardNumber;
    public CreditCardPayment(String cardNumber){
     this.cardNumber=cardNumber;
    }
    @Override
    public void pay(int amount){
        System.out.println("Paid "+amount+" using credit card:"+cardNumber);
    }
}
class UPIPayment implements PaymentStrategy{
    private String UPIId;
    public UPIPayment(String ID){
        this.UPIId=ID;
    }
    @Override
    public void pay(int amount){
        System.out.println("Paid "+amount + " using UPI Id "+ UPIId);
    }
}
class PaymentContext{
    private PaymentStrategy strategy;
    public void setStrategy(PaymentStrategy strategy){
        this.strategy=strategy;
    }
    public void makePayment(int amount){
        if(strategy==null){
            System.out.println("first select the payment strategy");
        }else{
            strategy.pay(amount);
        }
        
    }
}
public class strategyDesignPattern{
    public static void main(String[] args){
        PaymentContext context=new PaymentContext();
        Scanner sc=new Scanner(System.in);
        int amount;
        String strategy;
        System.out.print("Enter the amount you want to pay: ");
        amount=sc.nextInt();
        sc.nextLine();
        System.out.print("choose payment method:(UPI/CreditCard) ");
        strategy=sc.nextLine();
        if(strategy.equalsIgnoreCase("upi")){
            System.out.print("enter your UPI ID: ");
            String Id=sc.nextLine();
            context.setStrategy(new UPIPayment(Id));
        }else if(strategy.equalsIgnoreCase("creditcard")){
            System.out.print("Enter your Credit Card Number :" );
            String numb=sc.nextLine();
            context.setStrategy(new CreditCardPayment(numb));
        }else{
            System.out.print("payment method u choosen is not exist try after some time");
            sc.close();
            return;
        }
        context.makePayment(amount);
        sc.close();
    }
}