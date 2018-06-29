package com.example.jean.jcplayer.JCPlayerExceptions;



@SuppressWarnings("ALL")
public class AudioListNullPointerException extends Exception {
    public AudioListNullPointerException() {
        super("The playlist is empty or null");
    }
}
