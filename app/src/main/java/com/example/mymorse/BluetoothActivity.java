package com.example.mymorse;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {
    private Button on;
    private Button off;
    private Button displayList;
    private Button listen;
    private ListView listAvaibleDevices;
    private BluetoothAdapter mBluetoothAdapter;
    private static final int ENABLE_BLUETOOTH = 1;
    private ArrayList<String> devicesNames;
    private ArrayList<BluetoothDevice> devices;
    private BluetoothDevice choosenDevice;
    private IntentFilter filter;
    static final UUID MY_UUID = UUID.fromString("8dd638c4-9919-11ea-bb37-0242ac130002");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        on = (Button) findViewById(R.id.blueOn);
        off = (Button) findViewById(R.id.blueOff);
        displayList = (Button) findViewById(R.id.displayList);
        listen = (Button) findViewById(R.id.listen);
        listAvaibleDevices = (ListView) findViewById(R.id.avaibleDevices);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initBluetooth();
            }
        });

        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothAdapter.disable();
                Toast.makeText(getApplicationContext(), "Turned off", Toast.LENGTH_LONG).show();
            }
        });

        displayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devicesNames = new ArrayList<>();
                devices = new ArrayList<>();
                mBluetoothAdapter.startDiscovery();
            }
        });

        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerThread serverthread = new ServerThread();
                serverthread.start();
            }
        });

        listAvaibleDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final ArrayAdapter adapter = new ArrayAdapter(BluetoothActivity.this, android.R.layout.simple_list_item_1, devicesNames);
                listAvaibleDevices.setAdapter(adapter);
                String deviceName = (String) adapter.getItem(position);
                deviceOnName(deviceName);
                ClientThread clientThread = new ClientThread(choosenDevice);
                clientThread.start();
            }
        });
    }

    //récupérer le device correspondant à un nom donné
    private void deviceOnName(String name) {
        for(int i = 0; i < devices.size() ; i++ ) {
            BluetoothDevice device = devices.get(i);
            String deviceName = device.getName();
            if(deviceName.equals(name)) {
                choosenDevice = device;
            }
        }
    }

    //à chaque fois qu'un device est détecté on le rajoute à notre list de devices
    public final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                if ( (!devicesNames.contains(deviceName)) && deviceName != null ) {
                    devicesNames.add(deviceName);
                    devices.add(device);
                    Toast.makeText(getApplicationContext(), "Showing Device",Toast.LENGTH_SHORT).show();
                }
                final ArrayAdapter adapter = new ArrayAdapter(context,android.R.layout.simple_list_item_1, devicesNames);
                listAvaibleDevices.setAdapter(adapter);
            }
        }};

    private void initBluetooth() {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent , ENABLE_BLUETOOTH);
            Toast.makeText(getApplicationContext(), "Turned on",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
    }


    public class ServerThread extends Thread {

        private final BluetoothServerSocket mmServerSocket;

        public ServerThread() {
            BluetoothServerSocket tmp = null;
            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("MyMorse", MY_UUID);
            } catch (IOException e) {
                Log.i("sock listen fail", "Socket’s listen() method failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            while(true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.i("serversock close", "create() failed", e);
                }
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        Log.i("servercannot closet", "create() failed", e);
                    }
                    break;
                }
            }
        }
    }





    public class ClientThread extends Thread {
        private BluetoothDevice myDevice;
        private BluetoothSocket btSocket;

        public ClientThread(BluetoothDevice device) {
            myDevice = device;
            BluetoothSocket tmp = null;

            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e("errorconnect", "create() failed", e);
            }
            btSocket = tmp;
        }

        public void run() {
            mBluetoothAdapter.cancelDiscovery();
            try {
                btSocket.connect();

            } catch (IOException e) {
                Log.i("connectionfail", " connectionFailed()");
                try {
                    btSocket.close();
                } catch (IOException e2) {
                    Log.i("closefail", "unable to close() socket during connection failure", e2);
                }
            }
        }
    }
}
