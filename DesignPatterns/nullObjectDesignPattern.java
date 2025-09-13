// Null Object Design Pattern
// Problem
// In normal code, if an object reference is null and we call a method on it → NullPointerException.
// Example:
// Customer c = null;
// c.getName(); //  throws NullPointerException

// Solution (Null Object Pattern)
// Instead of returning null, return a special object that represents "no data" but still conforms to the same interface.
// This avoids null checks everywhere.

// Flow of Code
// Define an abstract class or interface.
// Implement Real Object class (with real behavior).
// Implement Null Object class (with default/empty behavior).
// Client never deals with null, it always gets a valid object.

abstract class Customer{
    protected String name;
    public abstract String getName();
    public abstract boolean isNull();
}
class RealCustomer extends Customer{
    public RealCustomer(String name){
        this.name=name;
    }
    @Override
    public String getName(){
        return name;
    }
    @Override
    public boolean isNull(){
        return false;
    }
}
class NullCustomer extends Customer{
    @Override
    public String getName(){
        return "Not available!";
    }
    @Override
    public boolean isNull(){
        return true;
    }
}
class CustomerFactory{
    private static final String[] names={"Alice","Bob","Charlie"};
    public static Customer getCustomer(String name){
        for(String n:names){
            if(n.equalsIgnoreCase(name)){
                return new RealCustomer(n);
            }
        }
        return new NullCustomer();
    }
}
public class nullObjectDesignPattern {
    public static void main(String args[]){
        Customer c1=CustomerFactory.getCustomer("Alice");
        Customer c2=CustomerFactory.getCustomer("Daniel");
        System.out.println("Customer c1: "+c1.getName());
        System.out.println("Customer c2: "+c2.getName());
    }
}
