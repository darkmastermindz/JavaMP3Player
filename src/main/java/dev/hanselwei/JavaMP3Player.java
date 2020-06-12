package dev.hanselwei;

import dev.hanselwei.Sound.MP3Player;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * Sample implementation of mp3spi main
 */
public class JavaMP3Player {

    private static String groovyMP3Example = "resources/example.mp3";

    public static void main(String[] args)  {
        playMP3(groovyMP3Example);
    }

    /**
     * Helper class to play MP3
     * @param mp3FileDir
     */
    private static void playMP3(String mp3FileDir){
        try {
            // Custom class based on mp3spi, see build.gradle
            MP3Player player = new MP3Player(mp3FileDir);
        } catch (IOException e) {
            System.err.println("File Directory invalid");
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Not a valid MP3 file");
        }
    }

}
