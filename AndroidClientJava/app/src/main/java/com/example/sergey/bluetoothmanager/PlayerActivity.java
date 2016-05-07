package com.example.sergey.bluetoothmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;

public class PlayerActivity extends AppCompatActivity {

    public OutputStream outStream = null;
    public MenuActivity menuActivity;

    public Button buttonPlayerPlay,buttonPlayerPause,buttonPlayerStop,buttonPlayerRev,
    buttonPlayerFwd,buttonPlayerVolUp,buttonPlayerVolDown,buttonPlayerBack5,buttonPlayerForward5;

    public View.OnClickListener listenerPlayerPlay,listenerPlayerStop,listenerPlayerPause,
            listenerPlayerRev,listenerPlayerFwd,listenerPlayerVolUp,listenerPlayerVolDown,
            listenerPlayerBack5,listenerPlayerForward5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        buttonPlayerPlay = (Button) findViewById(R.id.buttonPlayerPlay);
        buttonPlayerPause = (Button) findViewById(R.id.buttonPlayerPause);
        buttonPlayerStop = (Button) findViewById(R.id.buttonPlayerStop);
        buttonPlayerRev = (Button) findViewById(R.id.buttonPlayerRev);
        buttonPlayerFwd = (Button) findViewById(R.id.buttonPlayerFwd);
        buttonPlayerVolUp = (Button) findViewById(R.id.buttonVolUp);
        buttonPlayerVolDown = (Button) findViewById(R.id.buttonVolDown);
        buttonPlayerBack5 = (Button) findViewById(R.id.buttonBack5);
        buttonPlayerForward5 = (Button) findViewById(R.id.buttonForward5);

        createButtonsListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Toast toast;

        try {
            outStream = menuActivity.bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            toast = Toast.makeText(getApplicationContext(),"GetStream error", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        String out = "endPlayer";
        byte[] msgBuffer = out.getBytes();

        writeInStream(msgBuffer);
    }

    public void createButtonsListeners() {
        listenerPlayerPlay = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String out = "playerPlay";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);
            }
        };

        buttonPlayerPlay.setOnClickListener(listenerPlayerPlay);

        listenerPlayerPause = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String out = "playerPause";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);
            }
        };

        buttonPlayerPause.setOnClickListener(listenerPlayerPause);

        listenerPlayerStop = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String out = "playerStop";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);
            }
        };

        buttonPlayerStop.setOnClickListener(listenerPlayerStop);

        listenerPlayerRev = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String out = "playerRev";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);
            }
        };

        buttonPlayerRev.setOnClickListener(listenerPlayerRev);

        listenerPlayerFwd = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String out = "playerFwd";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);
            }
        };

        buttonPlayerFwd.setOnClickListener(listenerPlayerFwd);

        listenerPlayerForward5 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String out = "playerForward5";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);
            }
        };

        buttonPlayerForward5.setOnClickListener(listenerPlayerForward5);

        listenerPlayerBack5 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String out = "playerBack5";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);
            }
        };

        buttonPlayerBack5.setOnClickListener(listenerPlayerBack5);

        listenerPlayerVolUp = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String out = "playerVolUp";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);
            }
        };

        buttonPlayerVolUp.setOnClickListener(listenerPlayerVolUp);

        listenerPlayerVolDown = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String out = "playerVolDown";
                byte[] msgBuffer = out.getBytes();
                writeInStream(msgBuffer);
            }
        };

        buttonPlayerVolDown.setOnClickListener(listenerPlayerVolDown);
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
