package com.smartdevsolutions.ilottoandroid.Barcoder.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Barcode {
	
	private Handler handler = null;
	private static final String PROC_FILE = "/proc/barcode";
	
	RandomAccessFile raf = null;
	private boolean flag = true;
	private boolean readThreadIsRun = false;
	
	
	/**
	 * @param handler: send the message to the thread which holds this handler,<br>
	 * The message contains a barcode,The following code can be obtained through the barcode data<br>
	 * 	private Handler handler = new Handler() {<br>
	 *	public void handleMessage(Message msg) {<br>
	 *	&nbsp &nbsp &nbsp switch (msg.what) {<br>
	 *	&nbsp &nbsp &nbsp case 1:<br>
	 *	&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp String strData;<br>
	 *	&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp strData = ((String) msg.obj);<br>
	 *	&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp break;<br>
	 *	&nbsp &nbsp &nbsp default:<br>
	 *	&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp break;<br>
	 *	&nbsp &nbsp &nbsp }<br>
	 *	}<br>
	 *  };<br>
	 */
	public Barcode(Handler handler) {
		super();
		this.handler = handler;
	}

	/**
	 * Start scanning a barcode
	 */
	public void start() {
		flag = true;
		activeDevice();
		startReadThread();
	}
	
	/**
	 * Stop scanning a barcode
	 */
	public void stop() {
		flag = false;
		readThreadIsRun = false;
		if (raf != null) {
			try {
				raf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	private void activeDevice() {
		try {
			FileOutputStream fos = new FileOutputStream(PROC_FILE);
			String str = "active";
			byte[] data = str.getBytes();
			fos.write(data);
			fos.close();
			Log.d("chenyang", "fos is ok hhh");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void startReadThread() {
		Log.i("chenyang","readThreadIsRun = " + readThreadIsRun);
		Log.i("chenyang","flag = " + flag);
		if(!readThreadIsRun){
			readThreadIsRun = true;
			new Thread(runnable).start();
		}else{
			return;
		}
		
	}
	
	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			int i = -1;
			Log.d("chenyang", "thread start");

			try {
				raf = new RandomAccessFile(PROC_FILE, "r");
				Log.d("chenyang", "fis open");
				while(raf.read(new byte[50]) != -1){
					raf.seek(0);
				}
				raf.seek(0);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			byte[] arr = new byte[50];
			String str = "";
			while (flag) {
				try {
					i = raf.read(arr);
					//Log.d("chenyang", "read ret: " + i);
					if (i != -1) {
						if (i > 1) {
							arr[i] = 0;

							for (byte b : arr) {
								if (b == 0) {
									break;
								}

								if (((char) b >= 48) && ((char) b <= 57)) {
									//Log.d("chenyang", "(char) b: " + (char) b);
									str += (char) b;
									//Log.d("chenyang", "str:" + str);

								} else {
									if ((str.length() > 0) && ((char) b != 98)) {
										//Log.d("chenyang", "(char) b: " + (char) b);
										Message msg = new Message();
										msg.what = 1;
										msg.obj = str;
										handler.sendMessage(msg);
										//Log.d("chenyang", "str:" + str);
										str = "";
									}
								}
							}

						}
						
					}
					raf.seek(0);
					// Log.d("yanjunke", "Thread");
					Thread.sleep(20);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			try {
				raf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			arr = null;

		}

	};

}
