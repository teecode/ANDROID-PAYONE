package com.smartdevsolutions.ilottoandroid.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartdevsolutions.ilottoandroid.SerialPortActivity;
import com.smartdevsolutions.ilottoandroid.Utility.MyApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;

/**
 * Created by chams on 4/6/2018.
 */

public abstract class SerialPortFragment extends Fragment {
    protected MyApplication mApplication;
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


    public void viewCreate(MyApplication MyApplication) {

        mApplication = MyApplication;
        try {
            mSerialPort = mApplication.getSerialPort();
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();

			/* Create a receiving thread */
            mReadThread = new ReadThread();
            mReadThread.start();
        } catch (SecurityException e) {
            //DisplayError(R.string.error_security);
        } catch (IOException e) {
            //DisplayError(R.string.error_unknown);
        } catch (InvalidParameterException e) {
            //DisplayError(R.string.error_configuration);
        }
        catch (Exception e) {
            //DisplayError(R.string.error_configuration);
        }
    }

    protected abstract void onDataReceived(final byte[] buffer, final int size,final int n);

    @Override
    public void onDetach() {
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
        super.onDetach();
    }

    @Override
    public void onDestroy() {
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

