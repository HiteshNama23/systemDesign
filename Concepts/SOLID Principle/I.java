// I — Interface Segregation Principle

// Prefer many small, specific interfaces over one fat interface so clients implement only what they need.
interface Printer {
    void print(String text);
}

interface Scanner {
    String scan();
}

interface Fax {
    void fax(String number, String text);
}

class SimplePrinter implements Printer {
    public void print(String text) { System.out.println("Print: " + text); }
}

class MultiFunctionMachine implements Printer, Scanner, Fax {
    public void print(String text) { System.out.println("MFP Print: " + text); }
    public String scan() { return "Scanned Page"; }
    public void fax(String number, String text) { System.out.println("Fax to " + number); }
}

public class I {
    public static void main(String[] args) {
        Printer printer = new SimplePrinter();
        printer.print("Hello World");

        MultiFunctionMachine mfp = new MultiFunctionMachine();
        mfp.print("Doc1");
        System.out.println(mfp.scan());
        mfp.fax("12345", "Fax this doc");
    }
}
