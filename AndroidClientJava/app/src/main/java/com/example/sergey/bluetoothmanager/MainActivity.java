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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public Button buttonEnableBT;
    public Button buttonDisableBT;
    public Button buttonSearch;
    public ListView listDevicesFound;
    public CheckBox checkBoxNoGba;
    public CheckBox checkBoxJoystick;
    public CheckBox checkBoxMouse;

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private List<String> discoveredDevices = new ArrayList<String>();
    public ArrayAdapter<String> btArrayAdapter;

    public BluetoothAdapter bluetoothAdapter;
    public BluetoothSocket bluetoothSocket = null;
    public OutputStream outStream = null;
    private BroadcastReceiver discoverDevicesReceiver,discoveryFinishedReceiver;

    public View.OnClickListener listenerEnableBT,listenerDisableBT,listenerSearchDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        buttonEnableBT = (Button) findViewById(R.id.buttonEnableBT);
        buttonDisableBT = (Button) findViewById(R.id.buttonDisableBT);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);

        checkBoxNoGba = (CheckBox) findViewById(R.id.checkBoxNoGba);
        checkBoxJoystick = (CheckBox) findViewById(R.id.checkBoxJoystick);
        checkBoxMouse = (CheckBox) findViewById(R.id.checkBoxMouse);

        listDevicesFound = (ListView) findViewById(R.id.listDevicesFound);
        btArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listDevicesFound.setAdapter(btArrayAdapter);

        createButtonsListeners();
        createCheckBoxesListeners();
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

    public void createCheckBoxesListeners(){
        checkBoxNoGba.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    checkBoxMouse.setClickable(false);
                    checkBoxMouse.setChecked(false);
                    checkBoxJoystick.setClickable(false);
                    checkBoxJoystick.setChecked(false);
                }
                else {
                    checkBoxMouse.setClickable(true);
                    checkBoxMouse.setChecked(false);
                    checkBoxJoystick.setClickable(true);
                    checkBoxJoystick.setChecked(false);
                }
            }
        });

        checkBoxMouse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    checkBoxNoGba.setClickable(false);
                    checkBoxNoGba.setChecked(false);
                    checkBoxJoystick.setClickable(false);
                    checkBoxJoystick.setChecked(false);

                }
                else {
                    checkBoxNoGba.setClickable(true);
                    checkBoxNoGba.setChecked(false);
                    checkBoxJoystick.setClickable(true);
                    checkBoxJoystick.setChecked(false);
                }
            }
        });

        checkBoxJoystick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    checkBoxNoGba.setClickable(false);
                    checkBoxNoGba.setChecked(false);
                    checkBoxMouse.setClickable(false);
                    checkBoxMouse.setChecked(false);

                }
                else {
                    checkBoxNoGba.setClickable(true);
                    checkBoxNoGba.setChecked(false);
                    checkBoxMouse.setClickable(true);
                    checkBoxMouse.setChecked(false);
                }
            }
        });
    }

    public void createListViewListener() {

        listDevicesFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(checkBoxNoGba.isChecked()) {
                    String string = listDevicesFound.getItemAtPosition(position).toString();
                    String[] adress = string.split("\n");
                    Toast toast;

                    BluetoothDevice device = bluetoothAdapter.getRemoteDevice(adress[1]);
                    try {
                        bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                    } catch ( IOException e) {
                        toast = Toast.makeText(getApplicationContext(),"UUID error",Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    try {
                        bluetoothSocket.connect();
                    } catch (IOException e) {
                        try {
                            toast = Toast.makeText(getApplicationContext(),"Connect error",Toast.LENGTH_SHORT);
                            toast.show();
                            bluetoothSocket.close();
                        } catch (IOException e2) {
                            toast = Toast.makeText(getApplicationContext(),"Closing socket error",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }

                    try {
                        outStream = bluetoothSocket.getOutputStream();
                    } catch (IOException e) {
                        toast = Toast.makeText(getApplicationContext(),"GetStream error",Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    byte[] msgBuffer = adress[0].getBytes();
                    try {
                        outStream.write(msgBuffer);
                    } catch (IOException e) {
                        toast = Toast.makeText(getApplicationContext(), "Write error", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    try {
                        bluetoothSocket.close();
                    } catch (IOException e) {
                        toast = Toast.makeText(getApplicationContext(), "Closing socket error",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }

                if(checkBoxJoystick.isChecked()) {
                    String string = listDevicesFound.getItemAtPosition(position).toString();
                    String[] adress = string.split("\n");
                    Toast toast;

                    Intent intent = new Intent(getBaseContext(),JoystickActivity.class);
                    intent.putExtra("key",adress[1]);
                    startActivity(intent);
                }
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
