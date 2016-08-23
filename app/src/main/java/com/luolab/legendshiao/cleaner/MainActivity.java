package com.luolab.legendshiao.cleaner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends ActionBarActivity {

    TextView myLabel;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice mmDevice;
    BluetoothSocket mmSocket;
    OutputStream mmOutputStream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button connectButton = (Button)findViewById(R.id.connect);
        Button startButton = (Button)findViewById(R.id.start);
        Button stopButton = (Button)findViewById(R.id.stop);
        Button closeButton = (Button)findViewById(R.id.close);
        myLabel = (TextView)findViewById(R.id.label);

        //Open Button
        connectButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                try {
                    findBT();
                    openBT();
                }
                catch (IOException ex) { }
            }
        });

        //START Button : START BRUSH
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                try {
                    start();
                }
                catch (IOException ex) { }
            }
        });

        //STOP Button : STOP BRUSH
        stopButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                try {
                    stop();
                }
                catch (IOException ex) { }
            }
        });

        //Close button
        closeButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                try {
                    closeBT();
                }
                catch (IOException ex) { }
            }
        });

    }

    void findBT() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null) {
            myLabel.setText("Status : No bluetooth adapter available");
        }
        if(!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals("willy_cleaner"))
                {
                    mmDevice = device;
                    break;
                }
            }
        }
        myLabel.setText("Status : Bluetooth Device Found");
    }

    void openBT() throws IOException
    {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        myLabel.setText("Status : Bluetooth Opened");
    }
    void start() throws IOException
    {
        mmOutputStream.write('1');
        myLabel.setText("Status : START brushing...");
    }
    void stop() throws IOException
    {
        mmOutputStream.write('0');
        myLabel.setText("Status : STOP ACTION...");
    }
    void closeBT() throws IOException
    {
        mmOutputStream.close();
        mmSocket.close();
        myLabel.setText("Status : Bluetooth Closed");
    }
}



