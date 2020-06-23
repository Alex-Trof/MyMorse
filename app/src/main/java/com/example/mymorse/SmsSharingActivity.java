package com.example.mymorse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.InetAddresses;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class SmsSharingActivity extends AppCompatActivity {
    private static final int ENABLE_SMS = 1;
    private EditText phoneNumber;
    private Button sending;
    private Button back;
    private Intent sendIntent;
    private String pNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_sharing);

        phoneNumber = (EditText) findViewById(R.id.number);
        sending = (Button) findViewById(R.id.send);
        back = (Button) findViewById(R.id.back);

        sending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent translateActivity = new Intent(SmsSharingActivity.this, TranslateActivity.class);
                startActivity(translateActivity);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_bottom,R.anim.slide_out_top);
    }

    protected void sendSMS() {
        pNumber = phoneNumber.getText().toString();
        sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        boolean sendingGood = isSendSmsGranted();

        if(sendingGood){
            sendingMmsIntent(pNumber);
        }
    }

    public void sendingMmsIntent(String pNumber){
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendIntent.putExtra("address", pNumber);
        String path = "/storage/emulated/0/myAudioFile.3gp";
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        Log.i("Path", "" + uri);
        sendIntent.setType("audio/3gp");
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(sendIntent,"Send"));
    }

    public boolean isSendSmsGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 2);
                return false;
            }
        }
        else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    sendingMmsIntent(pNumber);
                }else{
                }
                break;
        }
    }
}
