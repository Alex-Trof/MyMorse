package com.example.mymorse;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SharingActivity extends AppCompatActivity {
    private Button sendSms;
    private Button sendBluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharing_activity);

        sendSms = (Button) findViewById(R.id.sms);
        sendBluetooth = (Button) findViewById(R.id.bluetooth);

        sendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsSharingActivity = new Intent(SharingActivity.this, SmsSharingActivity.class);
                startActivity(smsSharingActivity);
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
            }
        });

        sendBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent blueToothActivity = new Intent(SharingActivity.this, BluetoothActivity.class);
                startActivity(blueToothActivity);
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
            }
        });
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
