// Adapter Design Pattern
// Problem
// You have two incompatible interfaces that need to work together.
// Example: You have a modern charger that only supports USB-C, but your laptop port is USB-A.
// Instead of rewriting one side, we use an Adapter to make them compatible.

// When do we use it?
// To make existing classes work with others without modifying their source code.
// Common in:
// Integrating old code with new APIs.
// Payment gateways (wrapping different providers).
// Wrapping legacy systems.

// Flow of Code
// Target Interface → what the client expects.
// Adaptee → an existing class with a different interface.
// Adapter → implements target interface, internally uses Adaptee.
// Client → works with Target interface without worrying about Adaptee.

// (Audio Player Adapter)
// We have:
// MediaPlayer (target interface).
// AudioPlayer (plays mp3 only).
// AdvancedMediaPlayer (can play mp4, vlc).
// MediaAdapter (bridge between them).

import java.util.Scanner;

interface MediaPlayers{
    void play(String audioType, String fileName);
}
interface AdvancedMediaPlayer{
    void playVlc(String fileName);
    void playMp4(String fileName);
}

class VlcPlayer implements AdvancedMediaPlayer{
    public void playVlc(String fileName){
        System.out.println("Playing vlc file: " + fileName);
    }
    public void playMp4(String fileName){
        System.out.println("MP4 not supported by VLC Player");
    }
}

class Mp4Player implements AdvancedMediaPlayer{
    public void playVlc(String fileName){
        System.out.println("VLC not supported by MP4 Player");
    }
    public void playMp4(String fileName){
        System.out.println("Playing mp4 file:" + fileName);
    }
}

class MediaAdapter implements MediaPlayers {
    AdvancedMediaPlayer advancedMusicPlayer;

    public MediaAdapter(String audioType) {
        if (audioType.equalsIgnoreCase("vlc")) {
            advancedMusicPlayer = new VlcPlayer();
        } else if (audioType.equalsIgnoreCase("mp4")) {
            advancedMusicPlayer = new Mp4Player();
        }
    }

    public void play(String audioType, String fileName) {
        if (audioType.equalsIgnoreCase("vlc")) {
            advancedMusicPlayer.playVlc(fileName);
        } else if (audioType.equalsIgnoreCase("mp4")) {
            advancedMusicPlayer.playMp4(fileName);
        }
    }
}

class AudioPlayer implements MediaPlayers {
    MediaAdapter mediaAdapter;

    public void play(String audioType, String fileName) {
        if (audioType.equalsIgnoreCase("mp3")) {
            System.out.println("Playing mp3 file: " + fileName);
        } else if (audioType.equalsIgnoreCase("vlc") || audioType.equalsIgnoreCase("mp4")) {
            mediaAdapter = new MediaAdapter(audioType);
            mediaAdapter.play(audioType, fileName);
        } else {
            System.out.println("Invalid media. " + audioType + " format not supported");
        }
    }
}

public class adapterDesignPattern {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        MediaPlayers player = new AudioPlayer();

        while (true) {
            System.out.print("\nEnter file type (mp3/mp4/vlc/exit): ");
            String type = sc.nextLine().toLowerCase();

            if (type.equals("exit")) {
                System.out.println("Exiting...");
                break;
            }

            System.out.print("Enter file name: ");
            String fileName = sc.nextLine();

            player.play(type, fileName);
        }

        sc.close();
    }
}
