// Composite Design Pattern
// What is it?
// It lets you treat individual objects and compositions of objects (tree structures) uniformly.
// It’s used when you want to represent hierarchies (like folders, org charts, menus).

// When do we need it?
// Represent part-whole hierarchies.
// Example use cases:
// File System → Files & Directories.
// Company Structure → Employees, Departments.
// UI Components → Button, Panel (panel can contain other components).

// Flow of Code
// Component Interface → defines common operations.
// Leaf → represents simple objects (no children).
// Composite → represents objects that have children, implements child-management methods.
// Client → treats leaf and composite objects the same.

import java.util.*;
import java.util.Scanner;
interface FileSystem{
    void showDetails(String indent);
}

class File implements FileSystem{
    private String name;
    public File(String name){
        this.name=name;
    }
    @Override
    public void showDetails(String indent){
        System.out.println(indent+"File: "+ name);
    }
}
class Directory implements FileSystem{
    private String name;
    List<FileSystem>files=new ArrayList<>();
    public Directory(String name){
        this.name=name;
    }
    public void add(FileSystem file){
        files.add(file);
    }
    public void remove(FileSystem file){
        files.remove(file);
    }
    @Override
    public void showDetails(String indent){
        System.out.println(indent+"Directory: " + name);
        for(FileSystem fs:files){
            fs.showDetails(indent + "  ");
        }
    }
    public String toString() {
        return name;
    }
}

public class compositeDesignPattern {
    private static Scanner sc = new Scanner(System.in);
    private static void buildDirectory(Directory dir){
        while(true){
            System.out.print("\nInside "+dir.toString() + " - Add(file/dir/back): ");
            String input = sc.nextLine().toLowerCase();
            if(input.equals("file")){
                System.out.print("Enter file name: ");
                String fileName = sc.nextLine();
                dir.add(new File(fileName));
            }else if(input.equals("dir")){
                System.out.print("enter directory name: ");
                String dirName=sc.nextLine();
                Directory subDir=new Directory(dirName);
                dir.add(subDir);
                buildDirectory(subDir);
            }else if(input.equals("back")){
                return;
            }else{
                System.out.println("Invalid Input");
            }
        }
    }
    public static void main(String args[]){
        Directory rootDir=new Directory("Root");
        buildDirectory(rootDir);
        System.out.println("--File System Structure--");
        rootDir.showDetails("");
        sc.close();
    }
}
