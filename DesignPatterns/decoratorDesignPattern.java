// package DesignPatterns;

// Decorator Design Pattern
// When do we need it?
// When we want to add new features/behaviors to objects dynamically at runtime without modifying existing class code.
// Helps follow Open/Closed Principle (open for extension, closed for modification).

// Example use cases:
// Coffee shop: base coffee + add-ons (milk, sugar, cream).
// Text editor: plain text + bold + underline.
// Car service: base service + additional polish, wash, insurance.
// Pizza Topings: base pizza + additional toppings.

//  Flow of Code
// Component Interface → defines the core functionality.
// Concrete Component → base implementation.
// Decorator (Abstract Class) → wraps a component.
// Concrete Decorators → extend functionality (e.g., add milk, sugar).
// Main Class → client can decorate objects flexibly.

interface Coffee{
    String getDescription();
    int getCost();
}
class SimpleCoffee implements Coffee{
    @Override
    public String getDescription(){
         return "Simple Coffee";
    }
    @Override
    public int getCost(){
        return 50;
    }
}
abstract class CoffeeDecorator implements Coffee{
    protected Coffee decoratedCoffee;
    public CoffeeDecorator(Coffee coffee){
        this.decoratedCoffee=coffee;
    }
    public String getDescription(){
        return decoratedCoffee.getDescription();
    }
    public int getCost(){
        return decoratedCoffee.getCost();
    }
}
class MilkDecorator extends CoffeeDecorator{
    public MilkDecorator(Coffee coffee){
        super(coffee);
    }
    @Override 
    public String getDescription(){
        return super.getDescription()+",Milk";
    }
    public int getCost(){
        return super.getCost()+20;
    }
}
class SugarDecorator extends CoffeeDecorator{
    public SugarDecorator(Coffee coffee){
        super(coffee);
    }
    @Override 
    public String getDescription(){
        return super.getDescription()+",Sugar";
    }
    public int getCost(){
        return super.getCost()+10;
    }
}
class ChocolateDecorator extends CoffeeDecorator{
    public ChocolateDecorator(Coffee coffee){
        super(coffee);
    }
    @Override 
    public String getDescription(){
        return super.getDescription()+",Chocolate";
    }
    public int getCost(){
        return super.getCost()+30;
    }
}
public class decoratorDesignPattern {
    public static void main(String args[]){
        Coffee coffee=new SimpleCoffee();
        System.out.println(coffee.getDescription()+":"+coffee.getCost()+"Rs");
        coffee=new MilkDecorator(coffee);
        System.out.println(coffee.getDescription()+":"+coffee.getCost()+"Rs");
        coffee=new ChocolateDecorator(coffee);
        System.out.println(coffee.getDescription()+":"+coffee.getCost()+"Rs");
        coffee=new SugarDecorator(coffee);
        System.out.println(coffee.getDescription()+":"+coffee.getCost()+"Rs");
        coffee=new SugarDecorator(coffee);
        System.out.println(coffee.getDescription()+":"+coffee.getCost()+"Rs");
    }
}
