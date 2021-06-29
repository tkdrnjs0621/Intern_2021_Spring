package com.tkdrnjs0621.demoapp_bcm;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.TableRow;

import java.util.Set;

public class BluetoothClickManager extends ClickManager{

    BluetoothAdapter adapter;
    Looper looper;
    final double coef_x = 1;
    final double coef_y = 1;
    final double offset_x =0;
    final double offset_y = 0;

    int screen_width;
    int screen_height;

    String boardname = "";
    String boardadress ="";
    BCMBluetoothService bcmbs;
    private int[] ConvertCoord(int ax, int ay)
    {
        return new int[] {(int)((coef_x*((double)(ax))+offset_x)/1920 * screen_width),
                (int)((coef_y*((double)(ay))+offset_y)/1280 * screen_height)};
    }


    BluetoothClickManager(ClickManagerCallback callback)
    {
        super(callback);
    }


    BluetoothClickManager(ClickManagerCallback callback, BluetoothAdapter bluetoothAdapter, Looper looper, int ScreenWidth, int ScreenHieght)
    {
        super(callback);

        screen_height = ScreenHieght;
        screen_width = ScreenWidth;
        this.adapter = bluetoothAdapter;
        this.looper = looper;

        Handler mHandler = new Handler(looper) {
            @Override
            public void handleMessage(Message msg) {

                // tv.setText(new String((byte [])msg.obj).trim());
                    String[] abc = new String((byte [])msg.obj).split(" ");


                int t = 0;
                while(t<abc[2].length())
                {
                    Log.i("t", Integer.toString(t));
                    Log.i("abc2",Integer.toString(abc[2].charAt(t) - '0'));
                    if(abc[2].charAt(t) - '0' >=0 && abc[2].charAt(t) - '0' <=9)
                    {

                    }
                    else
                    {
                        break;
                    }
                    t++;
                }
                if(t!=0)
                {
                    abc[2]= abc[2].substring(0,t);
                }
                t = 0;
                while(t<abc[1].length())
                {
                    Log.i("t", Integer.toString(t));
                    Log.i("abc1",Integer.toString(abc[1].charAt(t) - '0'));
                    if(abc[1].charAt(t) - '0' >=0 && abc[1].charAt(t) - '0' <=9)
                    {

                    }
                    else
                    {
                        break;
                    }
                    t++;
                }
                if(t!=0)
                {
                    abc[1] = abc[1].substring(0,t);
                }
                int[] cos = ConvertCoord(Integer.parseInt(abc[1]),Integer.parseInt(abc[2]));
                int xonscreen = cos[0];
                int yonscreen = cos[1];
                Log.i("Handler","x : "+xonscreen+" y : "+yonscreen);
                switch(Integer.parseInt(abc[0]))
                {
                    case 0: {

                        break;
                    }
                    case 1: {
                        performAction(0,xonscreen,yonscreen);
                        callback.performAction(0,xonscreen,yonscreen);
                        break;
                    }
                    case 2: {
                        performAction(1,xonscreen,yonscreen);

                        callback.performAction(1,xonscreen,yonscreen);
                        break;
                    }
                }
            }
        } ;

        if (bluetoothAdapter == null) {
            Log.i("bl","nulladapter");
            // Device doesn't support Bluetooth
        }
        else
        {
            Log.i("bl","not nulladapter");
        }
//
//        if (!bluetoothAdapter.isEnabled()) {
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.i("pdlist",deviceName);
                if(deviceName.contains("Jetson"))
                {
                    boardname = deviceName;
                    boardadress = deviceHardwareAddress;
                    break;
                }
            }

        }

        Log.i("fejifjewifjiewfj","mbs started");

        bcmbs = new BCMBluetoothService(mHandler, bluetoothAdapter);
        bcmbs.start();
    }

}