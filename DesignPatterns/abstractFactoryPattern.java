// When do we need it?
// When we need to create families of related objects without specifying their concrete classes.
// It’s like a factory of factories.

// Example use cases:
// UI toolkit: Different platforms (Windows, Mac, Linux) → each has its own Button, Checkbox, TextField.
// Database connections: SQL vs NoSQL families.
// Game characters: Medieval factory (Elf, Castle), Modern factory (Soldier, Bunker).

//  Flow of Code
// Abstract Product Interfaces → define product types.
// Concrete Products → specific implementations.
// Abstract Factory → interface for creating product families.
// Concrete Factories → produce related objects.
// Main Class → client asks factory to create objects based on family type.
import java.util.Scanner;
interface Button{
    void paint();
}
interface CheckBox{
    void paint();
}
class WindowsButton implements Button{
    public void paint(){
        System.out.println("Rendering a windows-style button");
    }
}
class MacButton implements Button{
    public void paint(){
        System.out.println("Rendering a mac-style button");
    }
}
class WindowsCheckBox implements CheckBox{
    public void paint(){
        System.out.println("Rendering a windows checkbox");
    }
}
class MacCheckBox implements CheckBox{
    public void paint(){
        System.out.println("Rendering a mac checkbox");
    }
}
interface GUIFactory{
    Button createButton();
    CheckBox createCheckbox();
}
class WindowsFactory implements GUIFactory{
     public Button createButton(){
        return new WindowsButton();
     }
     public CheckBox createCheckbox(){
        return new WindowsCheckBox();
     }
}
class MacFactory implements GUIFactory{
    public Button createButton(){
        return new MacButton();
    }
    public CheckBox createCheckbox(){
        return new MacCheckBox();
    }
}
public class abstractFactoryPattern {
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        System.out.print("enter your OS(mac/windows):");
        String osType=sc.nextLine();
        GUIFactory gfactory;
        if(osType.equalsIgnoreCase("mac")){
            gfactory=new MacFactory();
        }else if(osType.equalsIgnoreCase("windows")){
            gfactory=new WindowsFactory();
        }else{
            System.out.println("the os you want not exist");
            sc.close();
            return;
        }
        Button btn= gfactory.createButton();
        CheckBox cb= gfactory.createCheckbox();
        btn.paint();
        cb.paint();
        sc.close();
    }
}
