package com.example.sergey.bluetoothmanager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public Button buttonEnableBT;
    public Button buttonDisableBT;
    public Button buttonSearch;
    public ListView listDevicesFound;

    public ArrayAdapter<String> btArrayAdapter;

    public BluetoothAdapter bluetoothAdapter;

    public View.OnClickListener listenerEnableBT,listenerDisableBT,listenerSearchDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        buttonEnableBT = (Button) findViewById(R.id.buttonEnableBT);
        buttonDisableBT = (Button) findViewById(R.id.buttonDisableBT);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);

        listDevicesFound = (ListView) findViewById(R.id.listDevicesFound);
        btArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listDevicesFound.setAdapter(btArrayAdapter);

        createButtonsListeners();
        createListViewListener();

        registerReceiver(ActionFoundReceiver,new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }

    public void createButtonsListeners() {
        listenerEnableBT = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, 1);
                }
            }
        };

        buttonEnableBT.setOnClickListener(listenerEnableBT);

        listenerDisableBT = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.disable();
                }
            }
        };

        buttonDisableBT.setOnClickListener(listenerDisableBT);

        listenerSearchDevices = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bluetoothAdapter.isEnabled()) {
                    btArrayAdapter.clear();
                    bluetoothAdapter.startDiscovery();
                }
            }
        };

        buttonSearch.setOnClickListener(listenerSearchDevices);
    }

    public void createListViewListener() {

        listDevicesFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String string = listDevicesFound.getItemAtPosition(position).toString();
                String[] adress = string.split("\n");

                Intent intent = new Intent(getBaseContext(),MenuActivity.class);
                intent.putExtra("keyConnect",adress[1]);
                startActivity(intent);
            }
        });
    }

    private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device  = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                btArrayAdapter.add(device.getName()+"\n"+device.getAddress());
                btArrayAdapter.notifyDataSetChanged();
                bluetoothAdapter.cancelDiscovery();
            }
        }
    };
}
