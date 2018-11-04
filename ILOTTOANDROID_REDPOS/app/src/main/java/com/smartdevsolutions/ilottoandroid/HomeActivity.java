package com.smartdevsolutions.ilottoandroid;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartdevsolutions.ilottoandroid.ApiResource.AddTicketResource;
import com.smartdevsolutions.ilottoandroid.ApiResource.DeviceResource;
import com.smartdevsolutions.ilottoandroid.Fragments.AccountSummaryFragment;
import com.smartdevsolutions.ilottoandroid.Fragments.CashiersBlockingFragment;
import com.smartdevsolutions.ilottoandroid.Fragments.CheckTicketFragment;
import com.smartdevsolutions.ilottoandroid.Fragments.DailyGameFragment;
import com.smartdevsolutions.ilottoandroid.Fragments.FundTransferFragment;
import com.smartdevsolutions.ilottoandroid.Fragments.PlaceBetFragment;
import com.smartdevsolutions.ilottoandroid.Fragments.ProfileFragment;
import com.smartdevsolutions.ilottoandroid.Fragments.RegisterSlipFragment;
import com.smartdevsolutions.ilottoandroid.Fragments.menuFragment;
import com.smartdevsolutions.ilottoandroid.Fragments.winningFragment;
import com.smartdevsolutions.ilottoandroid.Utility.Customer;
import com.smartdevsolutions.ilottoandroid.Utility.MyApplication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android_serialport_api.SerialProperties;
import hdx.HdxUtil;

/**
 * Created by BINARYCODES on 5/8/2017.
 */

public class HomeActivity extends SerialPortActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;
    private TextView TerminalMachineTextView;
    private Button mbetslipButton;
    private int menuItemID;
    private Gson gson ;
    private DeviceResource Device;
    private SerialProperties SerialProperties;
    ExecutorService pool = Executors.newSingleThreadExecutor();
    WakeLock lock;
    int printer_status = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        HdxUtil.SwitchSerialFunction(HdxUtil.SERIAL_FUNCTION_PRINTER);
        PowerManager pm = (PowerManager) getApplicationContext()
                .getSystemService(this.POWER_SERVICE);
        //lock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "HomeActivity");
        initialiseControls();
        SerialProperties = new SerialProperties();
        // String customerstring  = getIntent().getStringExtra("LoggedInCustomer");
        // Type type = new TypeToken<Customer>(){}.getType();
        Device = ((MyApplication) getApplication()).getCurrentTerminal();

        //TerminalMachineTextView.setText(" ₦"+ String.format("%.2f", Customer.getWalletbalance()));
        TerminalMachineTextView.setText(Device.getSerial());

        if(mbetslipButton!= null) {
            mbetslipButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddTicketResource myticket = ((MyApplication) getApplication()).getCurrentTicket();
                    if (myticket == null || myticket.getBetslips().size() <= 0) {
                        Toast.makeText(HomeActivity.this, "BetSlip is Currently Empty", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(HomeActivity.this, TicketActivity.class);
                        startActivity(intent);
                    }


                }
            });
        }

        ActionBar actionbar = getSupportActionBar();


        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // Do whatever you want here
                if(navigationView.getMenu().findItem(menuItemID) != null)
                    navigationView.getMenu().findItem(menuItemID).setChecked(false);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Do whatever you want here
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
         mDrawerToggle.syncState();
        actionbar.setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                                                             @Override
                                                             public boolean onNavigationItemSelected(MenuItem menuItem) {
                                                                 menuItemID=menuItem.getItemId();
                                                                 switch(menuItemID) {
                                                                     case R.id.home:
                                                                         setTitle(getResources().getString(R.string.home));
                                                                         Fragment fragment = new menuFragment();
                                                                         FragmentManager fragmentManager = getFragmentManager();
                                                                         fragmentManager.beginTransaction().replace(R.id.content_frame, fragment,"homefragment").addToBackStack( "homefragment" ).commit();
                                                                         mDrawerLayout.closeDrawer(navigationView);
                                                                         return true;
                                                                     case R.id.play:
                                                                         setTitle(getResources().getString(R.string.play));
                                                                         Fragment playfragment = new DailyGameFragment();
                                                                         FragmentManager playfragmentManager = getFragmentManager();
                                                                         playfragmentManager.beginTransaction().replace(R.id.content_frame, playfragment, "playfragment").addToBackStack( "homefragment" ).commit();
                                                                         mDrawerLayout.closeDrawer(navigationView);
                                                                         return true;
                                                                     case R.id.winningnumber:
                                                                         setTitle(getResources().getString(R.string.winningnumber));
                                                                         Fragment winningfragment = new winningFragment();
                                                                         FragmentManager winningfragmentManager = getFragmentManager();
                                                                         winningfragmentManager.beginTransaction().replace(R.id.content_frame, winningfragment, "winningfragment").addToBackStack( "homefragment" ).commit();
                                                                         mDrawerLayout.closeDrawer(navigationView);
                                                                         return true;
                                                                     case R.id.checkticket:
                                                                         setTitle(getResources().getString(R.string.checkticket));
                                                                         Fragment CheckTicketFragment = new CheckTicketFragment();
                                                                         FragmentManager CheckTicketfragmentManager = getFragmentManager();
                                                                         CheckTicketfragmentManager.beginTransaction().replace(R.id.content_frame, CheckTicketFragment, "checkticketFragment").addToBackStack( "homefragment" ).commit();
                                                                         mDrawerLayout.closeDrawer(navigationView);
                                                                         return true;
                                                                     case R.id.account:
                                                                         setTitle(getResources().getString(R.string.account));
                                                                         Fragment AccountFragment = new AccountSummaryFragment();
                                                                         FragmentManager AccountfragmentManager = getFragmentManager();
                                                                         AccountfragmentManager.beginTransaction().replace(R.id.content_frame, AccountFragment, "accountfragment").addToBackStack("homefragment").commit();
                                                                         mDrawerLayout.closeDrawer(navigationView);
                                                                         return true;
                                                                     case R.id.profile:
                                                                         setTitle(getResources().getString(R.string.profile));
                                                                         Fragment ProfileFragment = new ProfileFragment();
                                                                         FragmentManager ProfilefragmentManager = getFragmentManager();
                                                                         ProfilefragmentManager.beginTransaction().replace(R.id.content_frame, ProfileFragment, "profilefragment").addToBackStack( "homefragment" ).commit();
                                                                         mDrawerLayout.closeDrawer(navigationView);
                                                                         return true;
                                                                     case R.id.registeredslips:
                                                                         setTitle(getResources().getString(R.string.registeredslips));
                                                                         Fragment RegisteredSlipFragment = new RegisterSlipFragment();
                                                                         FragmentManager RegisteredSlipfragmentManager = getFragmentManager();
                                                                         RegisteredSlipfragmentManager.beginTransaction().replace(R.id.content_frame, RegisteredSlipFragment, "registeredslipfragment").addToBackStack( "homefragment" ).commit();
                                                                         mDrawerLayout.closeDrawer(navigationView);
                                                                         return true;
                                                                     case R.id.cashierblocking:
                                                                         setTitle(getResources().getString(R.string.cashierblocking));
                                                                         Fragment CashierBlockingFragment = new CashiersBlockingFragment();
                                                                         FragmentManager CashierBlockingfragmentManager = getFragmentManager();
                                                                         CashierBlockingfragmentManager.beginTransaction().replace(R.id.content_frame, CashierBlockingFragment, "cashierblockingFragment").addToBackStack( "homefragment" ).commit();
                                                                         mDrawerLayout.closeDrawer(navigationView);
                                                                         return true;
                                                                     case R.id.creditmanagement:
                                                                         setTitle(getResources().getString(R.string.creditmanagement));
                                                                         Fragment CreditManagementFragment = new FundTransferFragment();
                                                                         FragmentManager CreditManagementfragmentManager = getFragmentManager();
                                                                         CreditManagementfragmentManager.beginTransaction().replace(R.id.content_frame, CreditManagementFragment, "creditmanagementfragment").addToBackStack( "homefragment" ).commit();
                                                                         mDrawerLayout.closeDrawer(navigationView);
                                                                         return true;
                                                                     case R.id.logout:
                                                                         Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                                                                         startActivity(intent);




                                                                 }
                                                                 mDrawerLayout.closeDrawer(navigationView);
                                                                 return true;
                                                             }
                                                         }
        );

        //new WriteThread(0).start();

    }

    @Override
    protected void onDataReceived(byte[] buffer, int size, int n) {
        int i;
        String strTemp;
        if(SerialProperties.Status_Start_Falg == true)
        {
            for (i = 0; i < size; i++)
            {
                SerialProperties.Status_Buffer[getStatus_Buffer_Index()]=buffer[i];
                setStatus_Buffer_Index(1+i);
            }
        }

        if (SerialProperties.ver_start_falg == true) {
            for (i = 0; i < size; i++) {
                SerialProperties.strVer.append(String.format("%c",(char) buffer[i]));
            }

        }
		/*
		 * 	public static boolean flow_start_falg = false;
		byte [] flow_buffer=new byte[300];

		 * */

        StringBuilder str = new StringBuilder();
        StringBuilder strBuild = new StringBuilder();
        for (i = 0; i < size; i++) {
            if(SerialProperties.flow_start_falg == true)
            {
                if( (buffer[i] ==0x13) || ( buffer[i] ==0x11)  )
                {
                    SerialProperties.flow_buffer[0]= buffer[i];

                }
            }
            str.append(String.format(" %x", buffer[i]));
            strBuild.append(String.format("%c", (char) buffer[i]));
        }
        //Log.e(TAG, "onReceivedC= " + strBuild.toString());
        //Log.e(TAG, "onReceivedx= " + str.toString());

    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_CALL) {

            PlaceBetFragment playfragment = (PlaceBetFragment)getFragmentManager().findFragmentById(R.id.content_frame);
            if (playfragment != null && playfragment.isVisible()) {
                playfragment.myOnKeyDown(keyCode);
            }
            //((PlaceBetFragment).get(0)).myOnKeyDown(keyCode);
//            ((EventListFragment)fragments.get(1)).myOnKeyDown(keyCode);

            //and so on...
        }
        return super.onKeyDown(keyCode, event);
    }


    public void initialiseControls() {

        try {
            gson = new Gson();
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.navview);
            TerminalMachineTextView = (TextView) findViewById(R.id.terminalmachineid);
            mbetslipButton = (Button) findViewById(R.id.betslipbutton);

            Gson gson = new Gson();
            Type type = new TypeToken<Customer>() {
            }.getType();
            DeviceResource terminal = ((MyApplication) getApplication()).getCurrentTerminal();

            //this code block removes cashier blocking from pos functionalities
           // if (terminal.getCustomertype() != 3) {
                navigationView.getMenu().findItem(R.id.creditmanagement).setVisible(false);
                navigationView.getMenu().findItem(R.id.cashierblocking).setVisible(false);

            //}


            Fragment fragment = new menuFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }




    /* Called whenever we call invalidateOptionsMenu() */


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons

        return super.onOptionsItemSelected(item);
    }




    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */


    //---------------------------------------------------------------------------
    int getStatus_Buffer_Index()
    {
        return SerialProperties.Status_Buffer_Index;

    }
    void setStatus_Buffer_Index(int v)
    {
        SerialProperties.Status_Buffer_Index=v;
    }
    byte Get_Printer_Status()
    {
        SerialProperties.Status_Buffer[0]=0;
        SerialProperties.Status_Buffer[1]=0;
        SerialProperties.Status_Start_Falg = true;
        setStatus_Buffer_Index(0);
        sendCommand(0x1b,0x76);
        //Log.i(TAG,"Get_Printer_Status->0x1b,0x76");
        Time_Check_Start();

        while(true)
        {
            if(getStatus_Buffer_Index()>0)
            {

                SerialProperties.Status_Start_Falg = false;
                //Log.e(TAG,"Get_Printer_Status :"+Status_Buffer[0]);
                return SerialProperties.Status_Buffer[0] ;
            }
            if(TimeIsOver(5))
            {
                SerialProperties.Status_Start_Falg = false;
                //Log.e(TAG,"Get_Printer_Status->TIME OVER:"+Status_Buffer[0]);
                return (byte)0xff;

            }
            sleep(50);
        }


    }

    void Time_Check_Start() {
        SerialProperties.time.setToNow(); // ȡ��ϵͳʱ�䡣
        SerialProperties.TimeSecond = SerialProperties.time.second;


    }

    boolean TimeIsOver(int second) {

        SerialProperties.time.setToNow(); // ȡ��ϵͳʱ�䡣
        int t = SerialProperties.time.second;
        if (t < SerialProperties.TimeSecond) {
            t += 60;
        }

        if (t - SerialProperties.TimeSecond > second) {
            return true;
        }
        return false;
    }

    void PrinterPowerOnAndWaitReady()
    {

        //Status_Buffer_Index=0;
        //Status_Start_Falg = true;
        HdxUtil.SetPrinterPower(1);
        sleep(500);
    }

    private void sleep(int ms) {
        // Log.d(TAG,"start sleep "+ms);
        try {
            java.lang.Thread.sleep(ms);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Log.d(TAG,"end sleep "+ms);
    }

    void PrinterPowerOff()
    {
        HdxUtil.SetPrinterPower(0);
    }

    void Wait_Printer_Ready()
    {
        byte status;

        while(true)
        {
            status = Get_Printer_Status() ;
            if(status== 0xff)
            {
                //Log.e(TAG," time is out");
                return ;

            }

            if( (status & SerialProperties.HDX_ST_WORK)>0 )
            {

                //Log.d(TAG,"printer is busy");
            }
            else
            {
                //Log.d(TAG," printer is ready");
                return;

            }
            sleep(50);
        }
    }
    //返回真, 有纸, 返回假 没有纸
    boolean  Printer_Is_Normal()
    {
        byte status;


        status = Get_Printer_Status() ;

        if(status== 0xff)
        {
            //Log.e(TAG,"huck time is out");
            SerialProperties.Error_State="huck unkown err";
            return  false;

        }

        if( (status & SerialProperties.HDX_ST_NO_PAPER1 )>0 )
        {

            //Log.d(TAG,"huck is not paper");
            SerialProperties.Error_State=getResources().getString(R.string.IsOutOfPaper);
            return false;
        }
        else if( (status & SerialProperties.HDX_ST_HOT )>0 )
        {
            //Log.d(TAG,"huck is too hot");
            SerialProperties.Error_State=getResources().getString(R.string.PrinterNotNormal1);
            return false;
        }
        else
        {
           // Log.d(TAG," huck is ready");
            return true;
        }


    }
    //判断打印机装好纸 ,如果有 ,返回真,否者返回假
    boolean Warning_When_Not_Normal()
    {


			/*handler.sendMessage(handler.obtainMessage(DISABLE_BUTTON, 1,0, null));
			if(  Printer_Is_Normal() )
			{

				Log.i(TAG,"quck_Is_Normal ok");
				return true;
			}
			else
			{
				handler.sendMessage(handler.obtainMessage(SHOW_FONT_UPTAE_INFO, 1, 0, Error_State));
				Log.d(TAG," quck_Is not_Paper");
				return false;

			}*/
        return true;

    }
	/*
	 * 	public static boolean flow_start_falg = false;
	byte [] flow_buffer=new byte[300];

	 * */

    void flow_begin()
    {

        SerialProperties.flow_start_falg = true;
        SerialProperties.flow_buffer[0]=  0x0;
        //Log.i(TAG,"flow_begin ");

    }
    void flow_end()
    {

        SerialProperties.flow_start_falg = false;
        SerialProperties.flow_buffer[0]=  0x0;
        //Log.i(TAG,"flow_end ");
    }

    private void sendCommand(int... command) {
        try {
            for (int i = 0; i < command.length; i++) {
                mOutputStream.write(command[i]);
                // Log.e(TAG,"command["+i+"] = "+Integer.toHexString(command[i]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // / sleep(1);
    }




    private class WriteThread extends Thread {
        int  action_code;

        public WriteThread(int  code) {
            action_code = code;
        }

        public void run() {
            super.run();
            PrinterPowerOnAndWaitReady();
            if(!Warning_When_Not_Normal())
            {
                PrinterPowerOff();
                return;
            }

            lock.acquire();
            try {

                //Wait_Printer_Ready();
                switch(action_code)
                {
                    case 0:

                        sendCharacterDemo();

                        //Log.e("quck2", " print char test"   );
                        break;

                }
                // ConsoleActivity.this.sleep(14000);

            } finally {
                Wait_Printer_Ready();
                lock.release();
                PrinterPowerOff();
                //handler.sendMessage(handler.obtainMessage(ENABLE_BUTTON, 1,0, null));
            }

        }
    }

    private class GetVersionThread extends Thread {
        int type =0;
        public GetVersionThread(int type) {
            this.type=type;
        }

        public void run() {
            super.run();
            HdxUtil.SetPrinterPower(1);
            HomeActivity.this.sleep(500);
            lock.acquire();
            try {

                SerialProperties.strVer = new StringBuilder();
                SerialProperties.ver_start_falg = true;
                if(type == 0)
                {
                    byte[] start2 = { 0x1D, 0x67, 0x66 };
                    mOutputStream.write(start2);
                }
                else
                {
                    byte[] start2 = { 0x1D, 0x67, 0x33 };
                    mOutputStream.write(start2);
                }


            } catch (Exception e) {
               // Log.e(TAG, "quck =" + "here1");
                e.printStackTrace();
            } finally {
                lock.release();
                HomeActivity.this.sleep(500);
                // HdxUtil.SetPrinterPower(0);
            }

        }
    }
    private void sendCharacterDemo() {


        try {
            PrinterPowerOnAndWaitReady();

            sendCommand(0x0a);
            sendCommand(0x0a);

            sendCommand(0x1D, 0x21, 0x01);
            sendCommand(0x1D, 0x21, 0x10);// double height
            mOutputStream.write("  GOLDEN CHANCE ".getBytes("cp936"));
            mOutputStream.write("      LOTTO ".getBytes("cp936"));
            sendCommand(0x1D, 0x21, 0x00);
            sendCommand(0x1D, 0x21, 0x00);
            sendCommand(0x1b, 0x4a, 0x30);

             // cancel double height
            mOutputStream.write("          WELCOMES YOU".getBytes("cp936"));
            sendCommand(0x1b, 0x4a, 0x30);
            sendCommand(0x0a);

        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    private class InitThread extends Thread {
        int  action_code;

        public InitThread(int  code) {
            action_code = code;
        }

        public void run() {
            super.run();
            //PrinterPowerOnAndWaitReady();
            lock.acquire();
            try {

                switch(action_code)
                {

                    case 3:
                        Init_Data_When_Start();

                        break;
                    default:
                        break;
                }
                //ConsoleActivity.this.sleep(4000);

            } finally {

                lock.release();


            }

        }
    }
    void Init_Data_When_Start()
    {

        //handler.sendMessage(handler.obtainMessage(DISABLE_BUTTON, 1,0, null));
        //handler.sendMessage(handler.obtainMessage(SHOW_PROGRESS, 1, 0,getResources().getString(R.string.itpw3)));
        //PrinterPowerOnAndWaitReady();

        int i =15;// Integer.parseInt(strVer.toString());
        String[] city=getResources().getStringArray(R.array.language);
        SerialProperties.Printer_Info=getResources().getString(R.string.CurrentLanguageis);
        if(!city[i].isEmpty())
        {

            SerialProperties.Printer_Info +=city[i];
            SerialProperties.Printer_Info +="\n";
        }
        else
        {
            SerialProperties.Printer_Info="";
            SerialProperties.Printer_Info +="\n";
        }
        SerialProperties.iProgress=3;
       // handler.sendMessage(handler.obtainMessage(REFRESH_PROGRESS, 1, 0,null));
        String str;
        if(get_fw_version())
        {

            str=getResources().getString(R.string.currentFWV);
            str += SerialProperties.strVer.toString();
            str +="\n";
            SerialProperties.strVer=new StringBuilder(str);
        }
        if(Warning_When_Not_Normal())
        {
            //if(get_fw_version())
            {

                //str=getResources().getString(R.string.currentFWV);
                ///str +=strVer.toString();
                //str +="\n";
                //str2  =getResources().getString(R.string.HavePaper);
                //	str2 +="\n";
                //strVer=new StringBuilder(str);

                //handler.sendMessage(handler.obtainMessage(SHOW_PRINTER_INFO_WHEN_INIT, 1,0, null));
                //iProgress=50;
                //handler.sendMessage(handler.obtainMessage(REFRESH_PROGRESS, 1, 0,null));
                //handler.sendMessage(handler.obtainMessage(HIDE_PROGRESS, 1, 0,null));
                //handler.sendMessage(handler.obtainMessage(ENABLE_BUTTON, 1,0, null));
            }
        }


        //iProgress=50;
        //handler.sendMessage(handler.obtainMessage(REFRESH_PROGRESS, 1, 0,null));
        //handler.sendMessage(handler.obtainMessage(HIDE_PROGRESS, 1, 0,null));
        //handler.sendMessage(handler.obtainMessage(SHOW_FONT_UPTAE_INFO, 1, 0,getResources().getString(R.string.IsOutOfPaper)));
       // handler.sendMessage(handler.obtainMessage(ENABLE_BUTTON, 1,0, null));
        // handler.sendMessage(handler.obtainMessage(SHOW_PRINTER_INFO_WHEN_INIT, 1,0, null));




        //PrinterPowerOff();

    }





    // get current fw version
    public boolean get_fw_version() {
        HdxUtil.SetPrinterPower(0);
        HomeActivity.this.sleep(100);
        HdxUtil.SetPrinterPower(1);
        HomeActivity.this.sleep(800);

        //ConsoleActivity.strVer = new StringBuilder();
        //ConsoleActivity.ver_start_falg = true;
        byte[] start3 = { 0x1B, 0x23, 0x56 };

        try {
            mOutputStream.write(start3);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


		/*
		byte[] start2 = { 0x1D, 0x67, 0x66 };
		try {
			mOutputStream.write(start2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        sendCommand(0x1b, 0x4a, 0x30); // line feed
        sendCommand(0x1b, 0x4a, 0x30); // line feed
        sendCommand(0x1b, 0x4a, 0x30); // line feed
        sendCommand(0x1b, 0x4a, 0x30); // line feed
        HomeActivity.this.sleep(800);
        SerialProperties.strVer = new StringBuilder("900");
        SerialProperties.oldVer = new StringBuilder("900");
        return true ;
    }


    // get current fw version
    public boolean get_Language()
    {
        new GetVersionThread(1).start();
        Time_Check_Start();
        //Log.i(TAG," get_Language  "  );
        String strTemp;
        int i;
        while (true)
        {
            if (TimeIsOver(3))
            {
                //Log.e(TAG, " faild ,TimeIsOver " );
                return false;

            }
            HomeActivity.this.sleep(10);
            strTemp = SerialProperties.strVer.toString().trim();
            if (strTemp.length() >= 10)
            {
                i = strTemp.indexOf(':');//i = strTemp.indexOf(".bin");
                if (i == -1)
                {

                    //Log.e(TAG, " faild ,onDataReceivee= "+ strTemp.length());
                    //return false;
                }
                else
                {

                    strTemp = strTemp.substring(i + 2).trim();
                    SerialProperties.strVer = new StringBuilder(strTemp);
                    SerialProperties.ver_start_falg = false;
                    //Log.e(TAG, " ok ,onDataReceivet= "+ strVer.toString() );
                    try {
                        i = Integer.parseInt(SerialProperties.strVer.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                    return true;

                }

            }



        }

    }
    void int2ByteAtr(int pData, byte sumBuf[]) {
        for (int ix = 0; ix < 4; ++ix) {
            int offset = ix * 8;
            sumBuf[ix] = (byte) ((pData >> offset) & 0xff);
        }

    }

    // 4�ֽ����
    void Get_Buf_Sum(byte dataBuf[], int dataLen, byte sumBuf[]) {

        int i;
        long Sum = 0;
        // byte[] byteNum = new byte[8];
        long temp;

        for (i = 0; i < dataLen; i++) {
            if (dataBuf[i] < 0) {
                temp = dataBuf[i] & 0x7f;
                temp |= 0x80L;

            } else {
                temp = dataBuf[i];
            }
            Sum += temp;
            temp = dataBuf[i];

        }

        for (int ix = 0; ix < 4; ++ix) {
            int offset = ix * 8;
            sumBuf[ix] = (byte) ((Sum >> offset) & 0xff);
        }

    }
}



