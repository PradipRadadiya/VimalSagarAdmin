package com.example.jean.jcplayer.JCPlayerExceptions;



@SuppressWarnings("ALL")
public class AudioUrlInvalidException extends Exception {
    public AudioUrlInvalidException(String url) {
        super("The url does not appear valid: " + url);
    }
}
