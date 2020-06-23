package com.example.mymorse;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static androidx.core.app.ActivityCompat.requestPermissions;

public class TranslateActivity extends AppCompatActivity {
    private EditText message;
    private Button translate;
    private MorseManager myMorseManager;//classe qui gère la traduction
    private Context context;
    private MediaRecorder mediaRecorder;//sert à enregistrer avec le micro
    private String filePath;//chemin vers le fichier enregistré par le mediaRecorder
    private File file;//permet d'accéder ici au fichier enregistré par le mediaRecorder

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        message = (EditText) findViewById(R.id.message);
        translate = (Button) findViewById(R.id.translate);
        context = getApplicationContext();


        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mess = message.getText().toString();
                if (!mess.equals("")) {
                    myMorseManager = new MorseManager(mess, context);
                    try {
                        startRecord();
                        myMorseManager.toMorse(mess);
                    } catch (InterruptedException | IOException e) {
                        Log.i("morse msg", "no more morse", e);
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;

                Intent sharingActivity = new Intent(TranslateActivity.this, SharingActivity.class);
                startActivity(sharingActivity);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);//pour les animations
    }

    public void startRecord() throws IOException {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        file = new File(Environment.getExternalStorageDirectory(), "myAudioFile.3gp");
        filePath = file.getPath();
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setOutputFile(filePath);
        mediaRecorder.prepare();
        mediaRecorder.start();
    }
}
