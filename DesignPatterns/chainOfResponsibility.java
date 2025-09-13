// Chain of Responsibility Pattern
// When do we need it?
// When multiple handlers can process a request, but only one (or some) should handle it, without the sender knowing which one.
// Requests are passed along a chain until someone handles them.

// Example use cases:
// Logging system (INFO, DEBUG, ERROR handlers).
// Event handling in GUIs.
// Support system (Tier-1 → Tier-2 → Tier-3).

//  Flow of Code
// Handler (Abstract Logger) → defines a method to handle requests & a reference to the next handler.
// Concrete Handlers → specific loggers (InfoLogger, DebugLogger, ErrorLogger).
// Chain Setup → link handlers together.
// Main Class → client sends request to the first handler; it propagates down the chain.
import java.util.Scanner;
abstract class Logger{
      public static int INFO=1;
      public static int DEBUG=2;
      public static int ERROR=3;

      protected int level;
      protected Logger nextLogger;

      public void setNextLogger(Logger nextLogger){
            this.nextLogger=nextLogger;
      }
      public void logMessage(int level,String message){
        if(this.level==level){
            write(message);
        }else if(nextLogger!=null){
            nextLogger.logMessage(level,message);
        }else{
            System.out.println("the logger you wanna print not exist");
        }
      }
      protected abstract void write(String message);
}

class InfoLogger extends Logger{
    public InfoLogger(int level){
        this.level=level;
    }
    @Override
    protected void write(String message){
        System.out.println("INFO: "+ message);
    }
}

class DebugLogger extends Logger{
    public DebugLogger(int level){
        this.level=level;
    }
    @Override
    protected void write(String message){
        System.out.println("Debug: " + message);
    }
}

class ErrorLogger extends Logger{
    public ErrorLogger(int level){
        this.level=level;
    }
    @Override
    protected void write(String message){
        System.out.println("Error: " + message);
    }
}

class LoggerChain{
    public static Logger getChainOfLoggers(){
        Logger errorLogger=new ErrorLogger(Logger.ERROR);
        Logger debugLogger=new DebugLogger(Logger.DEBUG);
        Logger infoLogger=new InfoLogger(Logger.INFO);

        infoLogger.setNextLogger(debugLogger);
        debugLogger.setNextLogger(errorLogger);

        return infoLogger;
    }
}

public class chainOfResponsibility {
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        Logger loggerChain=LoggerChain.getChainOfLoggers();

        System.out.print("Enter log level (1=INFO, 2=DEBUG, 3=ERROR): ");
        int level=sc.nextInt();

        sc.nextLine();
        System.out.print("enter log message: ");
        String message=sc.nextLine();

        loggerChain.logMessage(level,message);
        sc.close();
    }    
}
