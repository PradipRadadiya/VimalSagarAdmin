package com.example.jean.jcplayer.JCPlayerExceptions;

/**
 * Created by Pradip on 02/08/16.
 */

@SuppressWarnings("ALL")
public class AudioListNullPointerException extends Exception {
    public AudioListNullPointerException() {
        super("The playlist is empty or null");
    }
}
