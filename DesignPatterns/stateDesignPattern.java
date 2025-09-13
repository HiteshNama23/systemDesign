// State Design Pattern
// What is it?
// The State Pattern allows an object to change its behavior when its internal state changes.
// Instead of using a big if-else / switch for different states, we encapsulate state-specific behavior into separate classes.
// The object delegates behavior to its current state object.


// When do we need it?
// When an object’s behavior depends on its state, and it must change behavior at runtime.

// Useful when:
// A vending machine behaves differently when it has stock, no stock, or when money is inserted.
// A media player acts differently in Play, Pause, or Stop state.
// A TCP connection behaves differently in Established, Listening, or Closed state.

// Flow of Code
// State Interface → defines common behavior for all states.
// Concrete States → implement behavior specific to each state.
// Context (main object) → maintains a reference to current state, delegates actions to it.
// Main Class → client interacts with context, not with state classes directly.

import java.util.Scanner;

interface State{
   void play(MediaPlayer player);
   void pause(MediaPlayer player); 
   void stop(MediaPlayer player);
}

class PlayingState implements State{
    private static final PlayingState instance = new PlayingState();
    private PlayingState(){}
    public static PlayingState getInstance(){
        return instance;
    }
    public void play(MediaPlayer player){
        System.out.println("Already playing!");
    }
    public void pause(MediaPlayer player){
        System.out.println("Pausing the music..");
        player.setState(PausedState.getInstance());
    }
    public void stop(MediaPlayer player){
        System.out.println("Stopping the music..");
        player.setState(StoppedState.getInstance());
    }
}

class PausedState implements State{
    private static final PausedState instance = new PausedState();
    private PausedState(){}
    public static PausedState getInstance(){
        return instance;
    }
    public void play(MediaPlayer player){
        System.out.println("playing..");
        player.setState(PlayingState.getInstance());
    }
    public void pause(MediaPlayer player){
        System.out.println("Already in a Paused State");
    }
    public void stop(MediaPlayer player){
        System.out.println("Stopping the music..");
        player.setState(StoppedState.getInstance());
    }
}

class StoppedState implements State{
    private static final StoppedState instance = new StoppedState();
    private StoppedState(){}
    public static StoppedState getInstance(){
        return instance;
    }
    public void play(MediaPlayer player){
        System.out.println("playing..");
        player.setState(PlayingState.getInstance());
    }
    public void pause(MediaPlayer player){
        System.out.println("Already in a Stoped State so cant paused");
    }
    public void stop(MediaPlayer player){
        System.out.println("Already Stopped!");
    }
}

class MediaPlayer{
    private State state;
    public MediaPlayer(){
        this.state=StoppedState.getInstance();
    }
    public void setState(State state){
        this.state=state;
    }
    public void play(){
        state.play(this);
    }
    public void pause(){
        state.pause(this);
    }
    public void stop(){
        state.stop(this);
    }
}
public class stateDesignPattern {
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        MediaPlayer player=new MediaPlayer();
        while(true){
            System.out.print("\nEnter command (play/pause/stop/exit): ");
            String command=sc.nextLine().toLowerCase();
            switch(command){
                case "play":
                player.play();
                break;
                case "pause":
                player.pause();
                break;
                case "stop":
                player.stop();
                break;
                case "exit":
                System.out.println("Exiting media player");
                sc.close();
                return;
                default:
                System.out.println("Invalid command");
            }
        }
    }
}
