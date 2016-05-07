package com.example.sergey.bluetoothmanager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class MenuActivity extends AppCompatActivity implements  View.OnTouchListener{

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    public BluetoothAdapter bluetoothAdapter;
    public static BluetoothSocket bluetoothSocket = null;
    public OutputStream outStream = null;
    public static int k;

    public Button buttonMyComp,buttonNoGba,buttonJoystick,buttonAimp,buttonLeftClick,buttonRightClick,
            buttonWheelUp,buttonWheelDown;

    public View.OnClickListener listenerMouseRightClick,listenerMouseLeftClick,listenerWheelUp,
            listenerWheelDown,listenerMyComp,listenerJoystick,listenerNoGba,listenerAimp;

    public TextView viewTouchpad;
    public Toast toast;

    public String out = "first";
    public float cordX = 0,cordY = 0,cordXCur,cordYCur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        buttonMyComp = (Button) findViewById(R.id.buttonMyComp);
        buttonNoGba = (Button) findViewById(R.id.buttonNoGba);
        buttonJoystick = (Button) findViewById(R.id.buttonJoystick);
        buttonAimp = (Button) findViewById(R.id.buttonAimp);
        buttonLeftClick = (Button) findViewById(R.id.buttonLeftClick);
        buttonRightClick = (Button) findViewById(R.id.buttonRightClick);
        buttonWheelUp = (Button) findViewById(R.id.buttonWheelUp);
        buttonWheelDown = (Button) findViewById(R.id.buttonWheelDown);

        viewTouchpad = (TextView) findViewById(R.id.viewTouchpad);

        viewTouchpad.setOnTouchListener(this);

        createButtonsListeners();

        connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Toast toast;
        String out = "end";
        byte[] msgBuffer = out.getBytes();

        writeInStream(msgBuffer);

        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            toast = Toast.makeText(getApplicationContext(),"Closing socket error",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void createButtonsListeners() {
        listenerMouseRightClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                out = "mouseRightClick";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);
            }
        };

        buttonRightClick.setOnClickListener(listenerMouseRightClick);

        listenerMouseLeftClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                out = "mouseLeftClick";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);
            }
        };

        buttonLeftClick.setOnClickListener(listenerMouseLeftClick);

        listenerWheelUp = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                out = "mouseWheelUp";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);
            }
        };

        buttonWheelUp.setOnClickListener(listenerWheelUp);

        listenerWheelDown = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                out = "mouseWheelDown";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);
            }
        };

        buttonWheelDown.setOnClickListener(listenerWheelDown);

        listenerMyComp = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                out = "openMyComp";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);
            }
        };

        buttonMyComp.setOnClickListener(listenerMyComp);

        listenerJoystick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                out = "joystick";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);

                k=0;
                Intent intent = new Intent(getBaseContext(),JoystickActivity.class);
                startActivity(intent);
            }
        };

        buttonJoystick.setOnClickListener(listenerJoystick);

        listenerNoGba = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                out = "openNoGba";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);
            }
        };

        buttonNoGba.setOnClickListener(listenerNoGba);

        listenerAimp = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                out = "openPlayer";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);

                Intent intent = new Intent(getBaseContext(),PlayerActivity.class);
                startActivity(intent);
            }
        };

        buttonAimp.setOnClickListener(listenerAimp);

    }

    public boolean onTouch(View view,MotionEvent event) {

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                return false;
            case MotionEvent.ACTION_MOVE:
                cordXCur = event.getX();
                cordYCur = event.getY();

                if(cordX == cordXCur && cordY == cordYCur) {
                    break;
                }

                if(cordXCur == cordX && cordYCur > cordY) {
                    out = "mouseUp";
                }

                else if (cordXCur == cordX && cordYCur < cordY) {
                    out = "mouseDown";
                }

                else if (cordXCur > cordX && cordYCur == cordY) {
                    out = "mouseRight";
                }

                else if (cordXCur < cordX && cordYCur == cordY) {
                    out = "mouseLeft";
                }

                else if (cordXCur > cordX && cordYCur > cordY) {
                    out = "mouseUpRight";
                }

                else if (cordXCur > cordX && cordYCur < cordY) {
                    out = "mouseUpLeft";
                }

                else if (cordXCur < cordX && cordYCur > cordY) {
                    out = "mouseDownRight";
                }
                else if (cordXCur < cordX && cordYCur < cordY) {
                    out = "mouseDownLeft";
                }
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);

                cordX = cordXCur;
                cordY = cordYCur;
                break;
            default:
                break;
        }

        return true;
    }

    public void writeInStream(byte[] Buffer) {
        try {
            outStream.write(Buffer);
        } catch (IOException e) {
            Toast toast = Toast.makeText(getApplicationContext(), "Write error", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void connect() {
        Intent intent = getIntent();
        String adress = intent.getStringExtra("keyConnect");

        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(adress);
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
    }
}
