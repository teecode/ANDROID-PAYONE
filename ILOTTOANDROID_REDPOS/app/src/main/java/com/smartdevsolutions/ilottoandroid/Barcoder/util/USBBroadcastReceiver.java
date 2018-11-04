package com.smartdevsolutions.ilottoandroid.Barcoder.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class USBBroadcastReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
       if(intent.getAction().equals("android.hardware.usb.action.USB_STATE")){
		  if (intent.getExtras().getBoolean("connected")){
			  Toast.makeText(context, "MOUNTED", Toast.LENGTH_LONG).show();
		  }else{
			  Toast.makeText(context, "REMOVED", Toast.LENGTH_LONG).show();
		  }
	   }
	}

}
