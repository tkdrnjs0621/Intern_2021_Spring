package com.tkdrnjs0621.testapp;

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
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MyBluetoothService {
    private static final String TAG = "MY_APP_DEBUG_TAG";
    private Handler handler; // handler that gets info from Bluetooth service
    private final BluetoothAdapter adapter;
    private ConnectedThread mConnectedThread;
    private AcceptThread mAcceptThread;

    public MyBluetoothService(Handler ahandler, BluetoothAdapter aadapter)
    {
        handler = ahandler;
        adapter = aadapter;
    }

    // Defines several constants used when transmitting messages between the
    // service and the UI.
    private interface MessageConstants {
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        public static final int MESSAGE_TOAST = 2;

        // ... (Add other message types here as needed.)
    }

    public synchronized void start()
    {
        mAcceptThread = new AcceptThread();
        mAcceptThread.start();
    }

    public synchronized void connected(BluetoothSocket socket, BluetoothDevice
            device)
    {
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();
    }

    public class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);
                    Log.i("runonconnected","length: "+numBytes+", message: "+new String(mmBuffer).trim());
                    // Send the obtained bytes to the UI activity.
                    Message readMsg = handler.obtainMessage(
                            MessageConstants.MESSAGE_READ, numBytes, -1,
                            mmBuffer);
                    readMsg.sendToTarget();
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }
        }

        // Call this from the main activity to send data to the remote device.
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);

                // Share the sent message with the UI activity.
                Message writtenMsg = handler.obtainMessage(
                        MessageConstants.MESSAGE_WRITE, -1, -1, mmBuffer);
                writtenMsg.sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);

                // Send a failure message back to the activity.
                Message writeErrorMsg =
                        handler.obtainMessage(MessageConstants.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("toast",
                        "Couldn't send data to the other device");
                writeErrorMsg.setData(bundle);
                handler.sendMessage(writeErrorMsg);
            }
        }

        // Call this method from the main activity to shut down the connection.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }


    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;
        private static final String TAG = "Me";
        private static final String NAME = "bl1";
        private final UUID MY_UUID = UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666");

        public AcceptThread() {
            Log.i("ac","AcceptThread constructed");
            // Use a temporary object that is later assigned to mmServerSocket
            // because mmServerSocket is final.
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code.
                tmp = adapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's listen() method failed", e);
                Log.i("ac","no",e);
            }
            mmServerSocket = tmp;

            Log.i("acconstructor","serversocket availabe "+mmServerSocket.toString());
        }

        public void run() {
            Log.i("runonac","runonac");
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned.
            while (true) {
                Log.i("runoonac","looptop");
                try {
                    Log.i("runonac","beforeaccept");
                    socket = mmServerSocket.accept();
                    Log.i("runonac","afteraccept");
                } catch (IOException e) {
                    Log.e(TAG, "Socket's accept() method failed", e);
                    Log.i("runonac","fail",e);
                    break;
                }
                try
                {

                    if (socket != null) {
                        // A connection was accepted. Perform work associated with
                        // the connection in a separate thread.
//                    manageMyConnectedSocket(socket);
                        Log.i("runonac","callconected");
                        connected(socket, socket.getRemoteDevice());
                        mmServerSocket.close();
                        break;
                    }
                    else
                    {
                        Log.i("runonac","socketnull");
                    }
//                if (socket != null) {
//                    // A connection was accepted. Perform work associated with
//                    // the connection in a separate thread.
//                    manageMyConnectedSocket(socket);
//                    mmServerSocket.close();
//                    break;
//                }
                }
                catch(Exception e)
                {
                    Log.i("runonac","nooo "+e.getMessage());
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }
}