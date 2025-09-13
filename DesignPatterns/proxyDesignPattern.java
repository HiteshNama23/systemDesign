// Proxy Design Pattern
// When do we need it?
// When we want a placeholder (proxy) to control access to a real object.
// Proxy adds an extra layer of security, logging, caching, or lazy initialization.

// Example use cases:
// Virtual Proxy → load heavy objects only when needed (e.g., image viewer).
// Protection Proxy → restrict access (e.g., admin vs user access).
// Remote Proxy → access objects in a different address space (RMI, APIs).

//  Flow of Code
// Subject Interface → common interface for Real and Proxy.
// RealSubject → the actual object with expensive or sensitive operations.
// Proxy → controls access to the RealSubject (adds checks, lazy loading, etc.).
// Main Class → client interacts with Proxy, not RealSubject directly.

import java.util.*;
import java.util.Scanner;

interface Internet{
    void connectTo(String website)throws Exception;
}

class RealInternet implements Internet{
    public void connectTo(String website){
        System.out.println("Connecting to "+website);
    }
}

class ProxyInternet implements Internet{
    private RealInternet realInternet = new RealInternet();
    private List<String> bannedSites =  Arrays.asList("abc.com","xyz.com");
    public void connectTo(String website) throws Exception{
        if(bannedSites.contains(website.toLowerCase())){
            throw new Exception("Access denied to " + website);
        }      
        realInternet.connectTo(website);
    }
}

public class proxyDesignPattern {
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        Internet internet = new ProxyInternet();
        System.out.print("Enter website domain to connect: ");
        String site=sc.nextLine();
        try{
            internet.connectTo(site);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        sc.close();
    }
}
