package com.smartdevsolutions.ilottoandroid.UserInterface;


import android.support.v7.app.AppCompatActivity;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import android.app.Activity;

import android.os.Bundle;


import com.smartdevsolutions.ilottoandroid.Utility.MyApplication;

import android_serialport_api.SerialPort;

public abstract class SerialPortAppCompatActivity extends Activity {

    protected MyApplication  mApplication;
    protected SerialPort mSerialPort;
    protected OutputStream mOutputStream;
    private InputStream mInputStream;
    protected ReadThread mReadThread;
    //private static final String TAG = "SerialPortActivity";
    private int n = 0;

    class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            while(!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[64];

                    if (mInputStream == null) return;
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        onDataReceived(buffer, size,n);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (MyApplication) getApplication();
        try {
            mSerialPort = mApplication.getSerialPort();
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();

			/* Create a receiving thread */
            mReadThread = new ReadThread();
            mReadThread.start();
        } catch (Exception e)
        {
        }

    }

    protected abstract void onDataReceived(final byte[] buffer, final int size,final int n);

    @Override
    protected void onDestroy() {
        if (mReadThread != null)
            mReadThread.interrupt();
        mApplication.closeSerialPort();
        mSerialPort = null;
        try
        {
            mOutputStream.close();
            mInputStream.close();
        } catch (IOException e) {
        }
        super.onDestroy();
    }
}

/**
 * Created by T360-INNOVATIVZ on 28/06/2017.
 */


