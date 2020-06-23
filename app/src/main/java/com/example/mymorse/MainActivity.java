package com.example.mymorse;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView logo;
    private int continu;// créée pour demander les permissions avant de passer à la suite

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // enlève la barre de titre
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //récup le uri sans permission plus loin dans le code
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        logo = (ImageView) findViewById(R.id.Logo);
        continu = 0;
        isReadStorageGranted();
        isRecordAudioGranted();
        isWriteStorageGranted();
    }


    public boolean onTouchEvent(MotionEvent event) {
        if (continu >= 3) {
            Intent translateActivity = new Intent(this, TranslateActivity.class);
            startActivity(translateActivity);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);//pour les animations
        }
        return true;
    }


    public boolean isReadStorageGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                continu++;
                return true;
            } else {

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else {
            continu++;
            return true;
        }
    }

    public boolean isWriteStorageGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                continu++;
                return true;
            } else {

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 4);
                return false;
            }
        } else {
            continu++;
            return true;
        }
    }

    public boolean isRecordAudioGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED) {
                continu++;
                return true;
            } else {

                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 3);
                return false;
            }
        } else {
            continu++;
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 3:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("record granted", "Permission: " + permissions[0] + "was " + grantResults[0]);
                    continu++;
                } else {
                }
                break;

            case 2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("read granted", "Permission: " + permissions[0] + "was " + grantResults[0]);
                    continu++;
                } else {
                }
                break;

            case 4:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("write granted", "Permission: " + permissions[0] + "was " + grantResults[0]);
                    continu++;
                } else {
                }
                break;
        }
    }
}
