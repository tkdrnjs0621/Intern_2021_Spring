package com.tkdrnjs0621.testapp;

import androidx.appcompat.app.AppCompatActivity;
//import com.tkdrnjs0621.testapp.MyBluetoothService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter bluetoothAdapter;
    String boardname = "";
    String boardadress ="";
    MyBluetoothService mbs;
    TextView tv;

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            tv.setText(new String((byte [])msg.obj).trim());
        }
    } ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView)findViewById(R.id.tv1);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

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
        Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
        Log.i("ee","eeee");

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
        if(boardname.equals(""))
        {
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(receiver, filter);
        }

        Log.i("fejifjewifjiewfj","mbs started");

        mbs = new MyBluetoothService(mHandler, bluetoothAdapter);
        mbs.start();
    }
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC addres
                if(deviceName.contains(("Jetson")))
                {
                    boardname = deviceName;
                    boardadress = deviceHardwareAddress;
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
   
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver);
    }

//    private class AcceptThread extends Thread {
//        private final BluetoothServerSocket mmServerSocket;
//        private static final String TAG = "Me";
//        private static final String NAME = "bl1";
//        private final UUID MY_UUID = UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666");
//
//        public AcceptThread() {
//            // Use a temporary object that is later assigned to mmServerSocket
//            // because mmServerSocket is final.
//            BluetoothServerSocket tmp = null;
//            try {
//                // MY_UUID is the app's UUID string, also used by the client code.
//                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
//            } catch (IOException e) {
//                Log.e(TAG, "Socket's listen() method failed", e);
//            }
//            mmServerSocket = tmp;
//        }
//
//        public void run() {
//            BluetoothSocket socket = null;
//            // Keep listening until exception occurs or a socket is returned.
//            while (true) {
//                try {
//                    socket = mmServerSocket.accept();
//                } catch (IOException e) {
//                    Log.e(TAG, "Socket's accept() method failed", e);
//                    break;
//                }
//
//                if (socket != null) {
//                    // A connection was accepted. Perform work associated with
//                    // the connection in a separate thread.
////                    manageMyConnectedSocket(socket);
//                    mbs.ConnectedThread(socket);
//                    mmServerSocket.close();
//                    break;
//                }
//
////                if (socket != null) {
////                    // A connection was accepted. Perform work associated with
////                    // the connection in a separate thread.
////                    manageMyConnectedSocket(socket);
////                    mmServerSocket.close();
////                    break;
////                }
//            }
//        }
//
//        // Closes the connect socket and causes the thread to finish.
//        public void cancel() {
//            try {
//                mmServerSocket.close();
//            } catch (IOException e) {
//                Log.e(TAG, "Could not close the connect socket", e);
//            }
//        }
//    }


}

