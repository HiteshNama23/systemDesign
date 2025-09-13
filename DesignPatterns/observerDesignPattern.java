// package DesignPatterns;
import java.util.*;
import java.util.Scanner;
// Observer Design Pattern
// When do we need it?
// When one object’s change should automatically notify and update multiple dependent objects.
// Helps in publisher–subscriber scenarios.
// Example use cases:
// YouTube channel (subscribers notified when a new video is uploaded).
// Stock price system (observers notified when stock price changes).
// Weather monitoring system (displays update when weather changes).

// Flow of Code
// Subject (Publisher) → maintains a list of observers & notifies them on change.
// Observer Interface → defines update() method.
// Concrete Observers (Subscribers) → implement update method to react.
// Main Class → demonstrates attaching, detaching, and notifying observers.

interface Observer{
    void notify(String message);
}
class Channel{
    private List<Observer> subscribers= new ArrayList<>();
    public void addSubscriber(Observer obs){
        subscribers.add(obs);
    }
    public void removeSubscriber(Observer obs){
        subscribers.remove(obs);
    }
    public void notifyAll(String msg){
        for(Observer obs:subscribers){
            obs.notify(msg);
        }
    }
}
class Subscriber implements Observer{
    private String name;
    public Subscriber(String name){
        this.name=name;
    }
    @Override
    public void notify(String message){
        System.out.println(name+" received notification "+message);
    }
}
public class observerDesignPattern {
    public static void main(String args[]){
        Channel chn= new Channel();
        Scanner sc=new Scanner(System.in);
        Subscriber s1=new Subscriber("alice");
        Subscriber s2=new Subscriber("bob");
        chn.addSubscriber(s1);
        chn.addSubscriber(s2);
        System.out.print("enter the message you want to notify to subscribers: ");
        String message=sc.nextLine();
        chn.notifyAll(message);
        sc.close();
    }    
}
