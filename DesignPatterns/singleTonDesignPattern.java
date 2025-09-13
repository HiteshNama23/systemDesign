// Singleton Pattern
// What is it?
// Ensures a class has only one instance and provides a global point of access to it.

// When do we need it?
// Logging system.
// Database connection pool.
// Configuration manager.
// Caches.

// Way 1: Eager Initialization (Simple & Thread-safe)
class SingleTonEager{
    private static final SingleTonEager instance = new SingleTonEager();
    private SingleTonEager(){}
    public static SingleTonEager getInstance(){
        return instance;
    }
}
//  Pros: Simple, thread-safe.
//  Cons: Instance created even if never used.

// Way 2: Lazy Initialization (Not Thread-safe by default)
class SingleTonLazy{
    private static SingleTonLazy instance;
    private SingleTonLazy(){}
    public static SingleTonLazy getInstance(){
        if(instance == null){
            instance=new SingleTonLazy();
        }
        return instance;
    }
}
// Pros: Created only when needed.
// Cons: Not thread-safe (can create multiple instances if accessed by multiple threads simultaneously).

// Way 3: Thread-safe with Double-Checked Locking
class SingleTonThreadSafe{
    private static volatile SingleTonThreadSafe instance;
    private SingleTonThreadSafe(){}
    public static SingleTonThreadSafe getInstance(){
        if(instance==null){
            synchronized(SingleTonThreadSafe.class){
                if(instance==null){
                    instance=new SingleTonThreadSafe();
                }
            }
        }
        return instance;
    }
}
// Pros: Efficient, thread-safe, lazy.
// Cons: Slightly more complex.

// Way 4 (Bonus): Bill Pugh / Static Inner Class (Best Practice)
class SingleTonBillPugh{
    private SingleTonBillPugh(){}
    private static class Holder{
        private static final SingleTonBillPugh instance = new SingleTonBillPugh();
    }
    public static SingleTonBillPugh getInstance(){
        return Holder.instance;
    }
}
// Pros: Lazy, thread-safe, simple.
// Recommended in modern Java.

public class singleTonDesignPattern {
    public static void main(String args[]){
        SingleTonEager s1 = SingleTonEager.getInstance();
        SingleTonEager s2 = SingleTonEager.getInstance();

        System.out.println(s1 == s2);
    } 
}
