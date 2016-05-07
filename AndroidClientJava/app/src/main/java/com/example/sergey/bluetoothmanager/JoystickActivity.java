package com.example.sergey.bluetoothmanager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

public class JoystickActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnTouchListener {

    public OutputStream outStream = null;
    public boolean action;

    public MenuActivity menuActivity;

    public Button buttonUp,buttonDown,buttonLeft,buttonRight,buttonA,buttonB,buttonX,buttonY,
    buttonStart,buttonSelect;

    public View.OnClickListener listenerTouchUp,listenerTouchDown,listenerTouchLeft,
            listenerTouchRight,listenerTouchY,listenerTouchZ,listenerTouchA,listenerTouchB,
            listenerTouchStart,listenerTouchSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);

        buttonUp = (Button) findViewById(R.id.buttonUp);
        buttonDown = (Button) findViewById(R.id.buttonDown);
        buttonLeft = (Button) findViewById(R.id.buttonLeft);
        buttonRight = (Button) findViewById(R.id.buttonRight);
        buttonA = (Button) findViewById(R.id.buttonA);
        buttonB = (Button) findViewById(R.id.buttonB);
        buttonX = (Button) findViewById(R.id.buttonX);
        buttonY = (Button) findViewById(R.id.buttonY);
        buttonSelect = (Button) findViewById(R.id.buttonSelect);
        buttonStart = (Button) findViewById(R.id.buttonStart);

        createButtonsListeners();
        createOneClickListeners();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Toast toast;

        try {
            outStream = menuActivity.bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            toast = Toast.makeText(getApplicationContext(),"GetStream error",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        String out = "endJoystick";
        byte[] msgBuffer = out.getBytes();

        if(menuActivity.k == 1)
            writeInStream(msgBuffer);
        menuActivity.k++;
    }

    public boolean onLongClick(View view) {

        NewThread thread = null;

        if (view == buttonUp) {
            thread = new NewThread("up");
        }
        else if (view == buttonDown)
            thread = new NewThread("down");
        else if (view == buttonLeft)
            thread = new NewThread("left");
        else if (view == buttonRight)
            thread = new NewThread("right");
        else if (view == buttonA)
            thread = new NewThread("A");
        else if (view == buttonB)
            thread = new NewThread("B");
        else if (view == buttonY)
            thread = new NewThread("Y");
        else if (view == buttonX)
            thread = new NewThread("X");
        else if (view == buttonStart)
            thread = new NewThread("start");
        else if (view == buttonSelect)
            thread = new NewThread("select");

        return false;
    }

    public boolean onTouch(View view,MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = true;
                break;
            case MotionEvent.ACTION_UP:
                action = false;
                break;
            default:
                break;
        }

        return false;
    }

    class NewThread implements Runnable {
        Thread thread;
        Toast toast;
        byte[] msgBuffer;

        NewThread(String out) {
            thread = new Thread(this);
            msgBuffer = out.getBytes();
            thread.start();
        }

        @Override
        public void run() {

            action = true;

            while(action) {
                writeInStream(msgBuffer);
            }

            action = true;
        }
    }

    public void createButtonsListeners() {
        buttonUp.setOnLongClickListener(this);
        buttonUp.setOnTouchListener(this);

        buttonDown.setOnLongClickListener(this);
        buttonDown.setOnTouchListener(this);

        buttonLeft.setOnLongClickListener(this);
        buttonLeft.setOnTouchListener(this);

        buttonRight.setOnLongClickListener(this);
        buttonRight.setOnTouchListener(this);

        buttonA.setOnLongClickListener(this);
        buttonA.setOnTouchListener(this);

        buttonB.setOnLongClickListener(this);
        buttonB.setOnTouchListener(this);

        buttonY.setOnLongClickListener(this);
        buttonY.setOnTouchListener(this);

        buttonX.setOnLongClickListener(this);
        buttonX.setOnTouchListener(this);

        buttonStart.setOnLongClickListener(this);
        buttonStart.setOnTouchListener(this);

        buttonSelect.setOnLongClickListener(this);
        buttonSelect.setOnTouchListener(this);
    }

    public void createOneClickListeners() {
        listenerTouchUp = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String out = "up";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);
            }
        };

        buttonUp.setOnClickListener(listenerTouchUp);

        listenerTouchDown = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String out = "down";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);
            }
        };

        buttonDown.setOnClickListener(listenerTouchDown);

        listenerTouchLeft = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String out = "left";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);
            }
        };

        buttonLeft.setOnClickListener(listenerTouchLeft);

        listenerTouchRight = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String out = "right";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);
            }
        };

        buttonRight.setOnClickListener(listenerTouchRight);

        listenerTouchA = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String out = "A";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);
            }
        };

        buttonA.setOnClickListener(listenerTouchA);

        listenerTouchB = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String out = "B";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);
            }
        };

        buttonB.setOnClickListener(listenerTouchB);

        listenerTouchY = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String out = "Y";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);
            }
        };

        buttonY.setOnClickListener(listenerTouchY);

        listenerTouchZ = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String out = "X";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);
            }
        };

        buttonX.setOnClickListener(listenerTouchZ);

        listenerTouchStart = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String out = "start";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);
            }
        };

        buttonStart.setOnClickListener(listenerTouchStart);

        listenerTouchSelect = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String out = "select";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);
            }
        };

        buttonSelect.setOnClickListener(listenerTouchSelect);
    }

    public void writeInStream(byte[] Buffer) {
        try {
            outStream.write(Buffer);
        } catch (IOException e) {
            Toast toast = Toast.makeText(getApplicationContext(), "Write error", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}