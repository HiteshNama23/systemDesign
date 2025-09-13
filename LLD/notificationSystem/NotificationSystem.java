package LLD.notificationSystem;
import java.util.concurrent.*;
import java.util.Scanner;

public class NotificationSystem {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        ExecutorService executor = Executors.newFixedThreadPool(3);

        System.out.println("Enter number of notifications to send:");
        int count = Integer.parseInt(scanner.nextLine());
        for(int i=0;i<count;i++){
            System.out.println("Enter notification type (email/sms/push):");
            String type=scanner.nextLine();
            System.out.println("Enter your message: ");
            String message = scanner.nextLine();
            Notifier notifier = NotifierFactory.createNotifier(type);
            if(notifier == null){
                System.out.println("Invalid notification type.");
                continue;
            }
            executor.submit(()->notifier.send(message));
        }
        executor.shutdown();
        scanner.close();
    }    
} 

interface Notifier{
    void send(String message);
}

class EmailNotifier implements Notifier{
    public void send(String message){
        System.out.println("📧 Email sent: " + message);
    }
}

class SMSNotifier implements Notifier{
    public void send(String message){
        System.out.println("📱 SMS sent: " + message);
    }
}

class PushNotifier implements Notifier{
    public void send(String message){
        System.out.println("🔔 Push Notification sent: " + message);
    }
} 

class NotifierFactory{
    public static Notifier createNotifier(String type){
        switch(type.toLowerCase()){
            case "email" : return new EmailNotifier();
            case "sms" : return new SMSNotifier();
            case "push" : return new PushNotifier();
            default : return null;
        }
    }
}