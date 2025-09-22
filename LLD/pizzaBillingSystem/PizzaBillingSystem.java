package LLD.pizzaBillingSystem;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class PizzaBillingSystem {
    public static void main(String args[]){
        Scanner scanner = new Scanner(System.in);
        Menu menu = Menu.sampleMenu();
        Inventory inventory = new Inventory(menu);
        OrderService orderService=new OrderService(inventory,new PaymentProcessor(),new ConsoleNotifier());
        TokenBucketRateLimiter rateLimiter = new TokenBucketRateLimiter(5,5);
        ExecutorService inputExecutor = Executors.newSingleThreadExecutor();
        System.out.println("Enter number of orders to simulate");
        int n=Integer.parseInt(scanner.nextLine());
        CountDownLatch latch = new CountDownLatch(n);
        for(int i=0;i<n;i++){
            int idx=i;
            inputExecutor.submit(()->{
                try{
                    if(!rateLimiter.tryConsume()){
                        System.out.println("Rate limit exceeded.try later");
                        latch.countDown();
                        return;
                    }
                    Order order=readOrder(menu,idx);
                    orderService.submitOrder(order,success->{
                        System.out.println("order "+order.getId()+" final status: "+(success ? "COMPLETED" : "FAILED"));
                        latch.countDown();
                    });
                }catch(Exception e){
                    System.out.println("Error placing order: "+e.getMessage());
                    latch.countDown();
                }
            });
        }
        try{
            latch.await();
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
        inputExecutor.shutdown();
        orderService.shutDown();
        scanner.close();
    }
    static Order readOrder(Menu menu,int idx){
        Random ran=new Random();
        List<MenuItem> items= new ArrayList<>(menu.getItems());
        int pick=ran.nextInt(items.size());
        MenuItem item=items.get(pick);
        int qty=1+ran.nextInt(2);
        Order order = new Order("user-" + idx, Collections.singletonList(new OrderItem(item.getId(), qty)));
        if (ran.nextDouble() < 0.3) order.setPromoCode("WELCOME10");
        return order;
    }
   
}
class Menu{
    private final List<MenuItem>items;
    Menu(List<MenuItem>items){
        this.items=items;
    }
    List<MenuItem>getItems(){
        return items;
    }
    static Menu sampleMenu(){
        List<MenuItem> list= new ArrayList<>();
        list.add(new MenuItem("MARG","Margherita",500));
        list.add(new MenuItem("PEPP","Pepperoni",700));
        list.add(new MenuItem("VEG","Veggie",650));
        return new Menu(list);
    }
}
class MenuItem{
     private final String id;
     private final String name;
     private final int priceInRs;
     MenuItem(String id,String name,int priceInRs){
        this.id=id;
        this.name=name;
        this.priceInRs=priceInRs;
     }
     String getId(){
        return id;
     }
     String getName(){
        return name;
     }
     int getPriceInRs(){
        return priceInRs;
     }
}
class Inventory{
    private final Map<String,AtomicInteger>stock=new ConcurrentHashMap<>();
    private final Map<String,ReentrantLock>locks=new ConcurrentHashMap<>();
    Inventory(Menu menu){
        for(MenuItem m : menu.getItems()){
            stock.put(m.getId(),new AtomicInteger(10));
            locks.put(m.getId(),new ReentrantLock());
        }
    }
    boolean tryReserve(String itemId,int qty){
        ReentrantLock lock = locks.get(itemId);
            if(lock==null)return false;
            lock.lock();
            try{
                AtomicInteger cur=stock.get(itemId);
                if(cur.get()>=qty){
                    cur.addAndGet(-qty);
                    return true;
                }else{
                    return false;
                }
            }finally{
                lock.unlock();
            }
    }
    void restock(String itemId,int qty){
        stock.computeIfPresent(itemId,(k,v)->{v.addAndGet(qty); return v;});
    }
    int available(String itemId){
        AtomicInteger a=stock.get(itemId);
        return a==null?0:a.get();
    }
}
class Order{
    private static final AtomicLong ID_GEN=new AtomicLong(1000);
    private final String id;
    private final String userId;
    private final List<OrderItem>items;
    private String promoCode;
    private volatile String status="PENDING..";
    Order(String userId,List<OrderItem>items){
        this.id="ORD-"+ID_GEN.getAndIncrement();
        this.userId=userId;
        this.items=items;
    }
    String getId(){
        return id;
    }
    String getUserId(){
        return userId;
    }
    List<OrderItem>getItems(){
        return items;
    }
    void setPromoCode(String p){
        this.promoCode=p;
    }
    String getPromoCode(){
        return promoCode;
    }
    void setStatus(String s){
        this.status=s;
    }
    String getStatus(){
        return status;
    }
}
class OrderItem{
    private final String menuItemId;
    private final int qty;
    OrderItem(String menuItemId,int qty){
        this.menuItemId=menuItemId;
        this.qty=qty;
    }
    String getMenuItemId(){
        return menuItemId;
    }
    int getQty(){
        return qty;
    }
}
// --- Strategy Pattern for Discounts ---
interface DiscountStrategy{
    int applyDiscountInRs(int subTotalAmount);
}
class NoDiscount implements DiscountStrategy{
    public int  applyDiscountInRs(int subTotalAmount){
        return 0;
    }
}
class PercentageDiscount implements DiscountStrategy{
    private final int percent;
    PercentageDiscount(int p){
        this.percent=p;
    }
    public int applyDiscountInRs(int subTotalAmount){
        return subTotalAmount*percent/100;
    }
}
class DiscountFactory{
    static DiscountStrategy fromCode(String code){
        if(code==null){
            return new NoDiscount();
        }
        if("Welcome10".equalsIgnoreCase(code)){
            return new PercentageDiscount(10);
        }
        return new NoDiscount();
    }
}
// --- Decorator Pattern for Billing ---
interface Bill{
    int getCostInRs();
    String getDescription();
}
class BaseBill implements Bill{
    private final Order order;
    BaseBill(Order order){
        this.order=order;
    }
    public int getCostInRs(){
        int subTotal=0;
        for(OrderItem it:order.getItems()){
            if("MARG".equals(it.getMenuItemId()))subTotal+=500*it.getQty();
            if("PEPP".equals(it.getMenuItemId()))subTotal+=700*it.getQty();
            if("VEG".equals(it.getMenuItemId()))subTotal+=650*it.getQty();
        }
        return subTotal;
    }
    public String getDescription(){
        return "Base subtotal";
    }
}
abstract class BillDecorator implements Bill{
    protected final Bill wrapped;
    BillDecorator(Bill wrapped){
        this.wrapped=wrapped;
    }
}
class DiscountDecorator extends BillDecorator{
    private final DiscountStrategy strategy;
    DiscountDecorator(Bill wrapped,DiscountStrategy strategy){
        super(wrapped);
        this.strategy=strategy;
    }
    public int getCostInRs(){
        int total=wrapped.getCostInRs();
        int discount=strategy.applyDiscountInRs(total);
        return total-discount;
    }
    public String getDescription(){
        return wrapped.getDescription()+" +discount";
    }
}
class TaxDecorator extends BillDecorator{
    private final int taxPercent;
    TaxDecorator(Bill wrapped,int taxPercent){
        super(wrapped);
        this.taxPercent=taxPercent;
    }
    public int getCostInRs(){
        int base=wrapped.getCostInRs();
        int tax=base*taxPercent/100;
        return base+tax;
    }
    public String getDescription(){
        return wrapped.getDescription()+" +tax";
    }
}
class PaymentProcessor{
    boolean charge(String orderId,int amountInRs){
        try{
            Thread.sleep(50);
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
        return true;
    }
}
interface Notifier{
    void notifyStatus(String orderId,String status);
}
class ConsoleNotifier implements Notifier{
    public void notifyStatus(String orderId,String status){
        System.out.println("Notify " + orderId + " -> " + status);
    }
}
class OrderService{
    private final ExecutorService workers = Executors.newFixedThreadPool(4);
    private final Inventory inventory;
    private final PaymentProcessor paymentProcessor;
    private final Notifier notifier;
    OrderService(Inventory inventory,PaymentProcessor paymentProcessor,Notifier notifier){
        this.inventory=inventory;
        this.paymentProcessor=paymentProcessor;
        this.notifier=notifier;
    }
    void submitOrder(Order order,Consumer<Boolean>callback){
        order.setStatus("Processing..");
        workers.submit(()->{
            boolean success=process(order);
            callback.accept(success);
        });
    }
    boolean process(Order order){
        for(OrderItem it:order.getItems()){
            boolean ok=inventory.tryReserve(it.getMenuItemId(),it.getQty());
            if(!ok){
                order.setStatus("FAILED.");
                notifier.notifyStatus(order.getId(),order.getStatus());
                return false;
            }
        }
        Bill bill=new BaseBill(order);
        DiscountStrategy ds=DiscountFactory.fromCode(order.getPromoCode());
        Bill discounted=new DiscountDecorator(bill,ds);
        Bill finalBill=new TaxDecorator(discounted,5);
        int total=finalBill.getCostInRs();
        boolean paid=paymentProcessor.charge(order.getId(),total);
        if(paid){
            order.setStatus("COMPLETED!!");
            notifier.notifyStatus(order.getId(),order.getStatus());
            return true;
        }else{
            order.setStatus("FAILED;<");
            notifier.notifyStatus(order.getId(),order.getStatus());
            for(OrderItem it:order.getItems()){
                inventory.restock(it.getMenuItemId(),it.getQty());
            }
            return false;
        }
    }
    void shutDown(){
        workers.shutdown();
        try{
            workers.awaitTermination(5,TimeUnit.SECONDS);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}
class TokenBucketRateLimiter{
    private final int capacity;
    private final AtomicInteger tokens;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    TokenBucketRateLimiter(int capacity, int refillPerSecond) {
        this.capacity = capacity;
        this.tokens = new AtomicInteger(this.capacity);
        scheduler.scheduleAtFixedRate(() -> {
            int cur;
            do {
                cur = tokens.get();
                int next = Math.min(capacity, cur + refillPerSecond);
                if (tokens.compareAndSet(cur, next)) break;
            } while (true);
        }, 0, 1, TimeUnit.SECONDS);
    }
    boolean tryConsume() {
        int cur;
        do {
            cur = tokens.get();
            if (cur <= 0) return false;
        } while (!tokens.compareAndSet(cur, cur - 1));
        return true;
    }
}