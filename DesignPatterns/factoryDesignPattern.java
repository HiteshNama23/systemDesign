// Factory Design Pattern
// When do we need it?
// When object creation logic is complex or varies depending on input, and we want to hide the instantiation details from the client.
// Instead of using new everywhere, we delegate object creation to a Factory class.

// Example use cases:
// Vehicle factory (Car, Bike, Truck).
// Shape factory (Circle, Square, Rectangle).
// Payment factory (CreditCard, PayPal, UPI).

//  Flow of Code
// Product Interface → common type for all products.
// Concrete Products → different implementations.
// Factory Class → contains logic to create objects based on input.
// Main Class → client asks the factory to create objects instead of using new.
import java.util.Scanner;
interface Shape{
    void draw();
}
class Circle implements Shape{
    public void draw(){
        System.out.println("Circle");
    }
}
class Triangle implements Shape{
    public void draw(){
        System.out.println("Triangle");
    }
}
class Square implements Shape{
    public void draw(){
        System.out.println("Square");
    }
}
class ShapeFactory{
    public Shape getShape(String shapeType){
        if(shapeType==null)return null;
        if(shapeType.equalsIgnoreCase("circle")){
            return new Circle();
        }else if(shapeType.equalsIgnoreCase("triangle")){
            return new Triangle();
        }else if(shapeType.equalsIgnoreCase("square")){
            return new Square();
        }
        return null;
    }
}
public class factoryDesignPattern{
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        ShapeFactory sf=new ShapeFactory();
        System.out.print("enter the shape you want(Circle/Triangle/Square):");
        String shape=sc.nextLine();
        Shape shapeObj=sf.getShape(shape);
        if(shapeObj!=null){
            shapeObj.draw();
        }else{
            System.out.println("the shape u want is not exist");
        }
        sc.close();
    }
}