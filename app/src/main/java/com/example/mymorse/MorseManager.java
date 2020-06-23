package com.example.mymorse;

import android.content.Context;
import android.media.MediaPlayer;


public class MorseManager {
    private static final int MY_PERMISSIONS_REQUEST_RECORD = 1;
    private String message;
    private MediaPlayer mediaPlayer;
    private Context context;
    private int myUri;


    public MorseManager(String mes, Context context) {
        this.message = mes;
        this.context=context;
    }

    public void toMorse(String mes) throws InterruptedException {
        String[] cutedMsg = mes.split("");

        //i commence à 1 car le premier element de cutedMsg est une chaine vide
        for (int i = 1 ; i < cutedMsg.length ; i++ ) {
            getLetterSound(cutedMsg[i]);
            mediaPlayer = MediaPlayer.create(context, myUri);
            mediaPlayer.start();
            int mediaTime = mediaPlayer.getDuration();
            Thread.sleep(mediaTime);
        }

        //pour libérer le mediaPlayer
        while(!mediaPlayer.isPlaying()) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    public void getLetterSound(String letter) {
        letter = letter.toLowerCase();
        switch (letter) {
            case "a":   myUri = R.raw.a;
            break;
            case "b":   myUri = R.raw.b;
            break;
            case "c":   myUri = R.raw.c;
            break;
            case "d":   myUri = R.raw.d;
            break;
            case "e":   myUri = R.raw.e;
            break;
            case "f":   myUri = R.raw.f;
            break;
            default:    myUri = R.raw.a;
        }
    }
}
