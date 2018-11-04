package com.smartdevsolutions.ilottoandroid.Fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.PowerManager;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nbbse.mobiprint3.Printer;
import com.smartdevsolutions.ilottoandroid.ApiResource.DeviceResource;
import com.smartdevsolutions.ilottoandroid.ApiResource.TicketResource;
import com.smartdevsolutions.ilottoandroid.BetViewActivity;
import com.smartdevsolutions.ilottoandroid.R;
import com.smartdevsolutions.ilottoandroid.UserInterface.TicketGridDisplay;
import com.smartdevsolutions.ilottoandroid.Utility.CallService;
import com.smartdevsolutions.ilottoandroid.Utility.CustomerDateInput;
import com.smartdevsolutions.ilottoandroid.Utility.MyApplication;
import com.smartdevsolutions.ilottoandroid.Utility.TicketOutput;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android_serialport_api.CharacterSequence;
import hdx.HdxUtil;

/**
 * Created by teecodez on 11/29/2017.
 */

public class RegisterSlipFragment extends SerialPortFragment {
    EditText mfromdateTextView;
    EditText mtodateTextView;
    Button mCancelButton;
    Button mPrintButton;
    Button mgobutton;


    ProgressDialog progress;
    Spinner mcashierdrpdown;
    List<DeviceResource> devices;
    DeviceResource device;
    GridView mticketView;
    TicketGridDisplay adapter;

    String fd, td;

    List<TicketResource> Myout;
    List<RegisteredTicketSubList> PrintableList ;
    private Printer print;

    private CharacterSequence CharSeq;
    MyHandler Handler;

    private android_serialport_api.SerialProperties SerialProperties;
    ExecutorService pool = Executors.newSingleThreadExecutor();
    PowerManager.WakeLock lock;
    int printer_status = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.registered_slip_fragment, container, false);

        SerialProperties = new android_serialport_api.SerialProperties();
        CharSeq = new CharacterSequence();
        Handler = new MyHandler();
        super.viewCreate((MyApplication) getActivity().getApplication());

        HdxUtil.SwitchSerialFunction(HdxUtil.SERIAL_FUNCTION_PRINTER);
        PowerManager pm = (PowerManager) getActivity().getApplicationContext()
                .getSystemService(this.getActivity().POWER_SERVICE);
        //lock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "Registered Slips");

        devices = new ArrayList<DeviceResource>();
        mfromdateTextView = (EditText) rootView.findViewById(R.id.regfromdate);
        mtodateTextView = (EditText) rootView.findViewById(R.id.regtodate);
        mgobutton = (Button)  rootView.findViewById(R.id.reggobutton);
        mCancelButton = (Button)  rootView.findViewById(R.id.regcancelbutton);
        mPrintButton = (Button)  rootView.findViewById(R.id.regprintbutton);

        mticketView =(GridView) rootView.findViewById(R.id.regticketgrid);
        mcashierdrpdown = (Spinner) rootView.findViewById(R.id.regcashier_spinner) ;
        print = Printer.getInstance();


        device = ((MyApplication) getActivity().getApplication()).getCurrentTerminal();
//        if(cust.getCustomertype() == 3)
//        {
//            try
//            {
//                showProgress("Getting Cashiers", "Please Wait ...");
//                List<Customer> loadedcashiers = ((MyApplication) getActivity().getApplication()).getCashiersInShop();
//
//                for (Customer c: loadedcashiers) {
//                    Cashiers.add(c);
//                }
//
//                List<String> clist = new ArrayList<>();
//                clist.add("ALL");
//                clist.addAll(fillArrayAdapter());
//
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
//                        android.R.layout.simple_spinner_dropdown_item, clist);
//                mcashierdrpdown.setAdapter(adapter);
//                progress.dismiss();
//            }
//            catch (Exception ex)
//            {
//                getActivity().setTitle(getResources().getString(R.string.home));
//                Fragment menufragment = new menuFragment();
//                FragmentManager menufragmentManager = getFragmentManager();
//                menufragmentManager.beginTransaction().replace(R.id.content_frame, menufragment).commit();
//                Toast.makeText(getActivity(), "Please Wait Background Service trying to load Cashiers", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//        else
//        {
            devices.add(device);
            List<String> clist = fillArrayAdapter();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, clist);
            mcashierdrpdown.setAdapter(adapter);
        //}



        mgobutton.setOnClickListener(GoClick );
        mCancelButton.setOnClickListener(GoCancel );
        mPrintButton.setOnClickListener(GoPrint );




        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
            public void updateLabel() {

                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                mfromdateTextView.setText(sdf.format(myCalendar.getTime()));

            }


        };

        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
            public void updateLabel() {

                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                mtodateTextView.setText(sdf.format(myCalendar.getTime()));

            }


        };

        mfromdateTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date , myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mtodateTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date2 , myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        mticketView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int positiontoview = 0;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positiontoview = position;

                TicketResource tick = Myout.get(positiontoview);
                ((MyApplication) getActivity().getApplication()).setCheckTicketCurrentTicket(tick);
                Intent intent = new Intent(getActivity(), BetViewActivity.class);
                startActivity(intent);


            }
        });

        return rootView;
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

    private ArrayList<String> fillArrayAdapter() {
        ArrayList<String> retlist = new ArrayList<String>();
        for (DeviceResource c: devices) {

            String cas = c.getSerial();
            retlist.add(cas);
        }
        return  retlist;
    }

    public void showProgress(  String Title, String Message) {
        progress = ProgressDialog.show(getActivity(), Title,
                Message, true);
    }

    private Button.OnClickListener GoCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            getActivity().setTitle(getResources().getString(R.string.home));
            Fragment HomeFragment = new menuFragment();
            FragmentManager HomeFragmentManager = getFragmentManager();
            HomeFragmentManager.beginTransaction().replace(R.id.content_frame, HomeFragment).commit();

        }
    };

    private Button.OnClickListener GoPrint = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            PrintRegisteredSlips();

        }
    };

    private void PrintRegisteredSlips() {

        if(Myout!= null && Myout.size() >0) {

            ArrayList<RegisteredTicketSubList> subLists = new ArrayList<RegisteredTicketSubList>();
            RegisteredTicketSubList currentsublist = new RegisteredTicketSubList();
            currentsublist.set_tickets( new ArrayList<TicketResource>());

            for (TicketResource ticket:Myout)
            {
                Date date =  ticket.getDateRegistered();


                if(currentsublist.get_date() == null)
                    currentsublist.set_date(date);
                else
                {
                    String csldate = currentsublist.get_date().toString();
                    String cdate = date.toString();
                    if(!csldate.equalsIgnoreCase(cdate))
                    {
                        subLists.add(currentsublist);
                        currentsublist = new RegisteredTicketSubList();
                        currentsublist.set_tickets(new ArrayList<TicketResource>());
                        currentsublist.set_date(date);
                    }
                }

                currentsublist.get_tickets().add(ticket);
                currentsublist.set_amount( currentsublist.get_amount() + ticket.getAmount());
            }
            subLists.add(currentsublist);
            PrintableList = subLists;
            new WriteThread(0).start();
        }
        else{
            Snackbar.make(this.getView(), "Please Fetch Slips before initiating print", Snackbar.LENGTH_LONG).show();
            //Toast.makeText()
        }

    }

    private Button.OnClickListener GoClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            if(cust.getCustomertype() == 3) {
//                if (mcashierdrpdown.getSelectedItemPosition() == 0) {
//                    try {
//                        int im = 2;
//                        int cid = cust.getId();
//                        int sid = cust.getShopid();
//                        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
//                        fd = mfromdateTextView.getText().toString();
//                        td = mtodateTextView.getText().toString();
//                        Date fdate = parser.parse(fd);
//                        Date tdate = parser.parse(td);
//                        long startTime = fdate.getTime();
//                        long endTime = tdate.getTime();
//                        long diffTime = endTime - startTime;
//                        long diffDays = diffTime / (1000 * 60 * 60 * 24);
//                        if(diffDays <= 7) {
//                            showProgress("ACCOUNT SUMMARY", "GENERATING REGISTERED SLIPS");
//                            RegisteredSlipTask tsk = new RegisteredSlipTask(cid, sid, fdate, tdate, im);
//                            tsk.execute();
//                        }
//                        else
//                        {
//                            Toast.makeText(getActivity(), "Date Difference must be with 7days " , Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (ParseException e) {
//                        Toast.makeText(getActivity(), e.getMessage() , Toast.LENGTH_SHORT).show();
//                    }
//
//
//                } else {
//
//                    try {
//                        int im = 1;
//                        int seleid = mcashierdrpdown.getSelectedItemPosition();
//                        int cid = Cashiers.get(seleid - 1).getId();
//                        int sid = cust.getShopid();
//                        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
//                        fd = mfromdateTextView.getText().toString();
//                        td = mtodateTextView.getText().toString();
//                        Date fdate = parser.parse(mfromdateTextView.getText().toString());
//                        Date tdate = parser.parse(mtodateTextView.getText().toString());
//                        showProgress("ACCOUNT SUMMARY", "GENERATING SUMMARY");
//                        RegisteredSlipTask tsk = new RegisteredSlipTask(cid, sid, fdate, tdate, im);
//                        tsk.execute();
//                    } catch (ParseException e) {
//                        Toast.makeText(getActivity(), e.getMessage() , Toast.LENGTH_SHORT).show();
//                    }
//
//
//                }
//            }
//            else
//            {
                try {
                    int im = 1;
                    int seleid = mcashierdrpdown.getSelectedItemPosition();
                    //int cid = devices.getSerial();
                    int cid =21;
                   // int sid = device.get();
                   int sid = 1;
                    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                    fd = mfromdateTextView.getText().toString();
                    td = mtodateTextView.getText().toString();
                    Date fdate = parser.parse(fd);
                    Date tdate = parser.parse(td);
                    long startTime = fdate.getTime();
                    long endTime = tdate.getTime();
                    long diffTime = endTime - startTime;
                    long diffDays = diffTime / (1000 * 60 * 60 * 24);
                    if(diffDays <= 7) {
                        showProgress("ACCOUNT SUMMARY", "GENERATING SUMMARY");
                        RegisteredSlipTask tsk = new RegisteredSlipTask(cid, sid, fdate, tdate, im);
                        tsk.execute();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Date Difference must be with 7days " , Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    Toast.makeText(getActivity(), e.getMessage() , Toast.LENGTH_SHORT).show();
                }

            }


        //}
    };


    public class RegisteredSlipTask extends AsyncTask<Void, Void, Boolean> {

        int custID;
        Date fromdate;
        Date todate;
        int shopID;
        String url;
        int inputmode;
        String debugmessage="";

        List<TicketResource> output ;

        private TicketOutput ticketOutput ;

        RegisteredSlipTask( int CID, int SID, Date from, Date to, int IM) {
            this.custID = CID;
            this.shopID = SID;
            this.fromdate = from;
            this.todate = to;
            this.inputmode = IM;
            this.url = (getString(R.string.service_url) +getString(R.string.service_TicketsCustomerDate_controller));

        }

        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            try {


                // TicketInput myticket = ((MyApplication)  getActivity().getApplication()).getCurrentTicket();


                CustomerDateInput SI = new CustomerDateInput();
                SI.setCustID(custID);
                SI.setFromdate(fromdate);
                SI.setTodate(todate);
                SI.setInputmode(inputmode);
                SI.setShopID(shopID);
                SI.setStatus(-1);

                Gson gson = new Gson();
                Type type = new TypeToken<CustomerDateInput>(){}.getType();
                String json = gson.toJson(SI, type);
                CallService mysevice = new CallService(url, json);
                String response =  mysevice.invokePostService();
                if(response != ""   )
                {
                    type = new TypeToken<List<TicketResource>>(){}.getType();
                    output = gson.fromJson(response, type);
                    if(output!= null) {
                        debugmessage = "";
                        return true;
                    }
                    else {
                        debugmessage = "null received";
                        return false;
                    }
                }
                else
                {
                    debugmessage += "Poor Network; Please Check Your Network and Try Again!!!";
                    return false ;
                }

            } catch (Exception e) {
                debugmessage = e.getMessage();
                return false;
            }


        }

        @Override
        protected void onPostExecute(final Boolean success) {

            try {
                progress.dismiss();
                if (success) {
                    Myout = output;
                    adapter = new TicketGridDisplay(getActivity(), output);
                    mticketView.setAdapter(adapter);


                } else {
                    Myout = null;
                    Toast.makeText(getActivity(), "Error Fetching Report " + debugmessage, Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                Myout = null;
                Toast.makeText(getActivity(), "post serv err: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {

            progress.dismiss();
        }




    }

    public class RegisteredTicketSubList
    {
        private double _amount;
        private List<TicketResource> _tickets;
        private Date _date;

        public Date get_date() {
            return _date;
        }

        public double get_amount() {
            return _amount;
        }

        public List<TicketResource> get_tickets() {
            return _tickets;
        }

        public void set_amount(double _amount) {
            this._amount = _amount;
        }

        public void set_date(Date _date) {
            this._date = _date;
        }

        public void set_tickets(List<TicketResource> _tickets) {
            this._tickets = _tickets;
        }

    }


    private class MyHandler extends android.os.Handler {
        public void handleMessage(Message msg) {
            if (SerialProperties.stop == true)
                return;
            switch (msg.what) {
                case 0:
                    mPrintButton.setEnabled(false);
                    break;
                case 1 :
                    mPrintButton.setEnabled(true);
                    break;
            }
        }
    }


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
    boolean  Printer_Is_Normal(){
        byte status;


        status = Get_Printer_Status() ;

        if(status== 0xff)
        {
            //Log.e(TAG,"huck time is out");
            SerialProperties.Error_State="huck unkown err";
            Snackbar.make(this.getView(), "Printing Unknown Error", Snackbar.LENGTH_LONG).show();
            return  false;

        }

        if( (status & SerialProperties.HDX_ST_NO_PAPER1 )>0 )
        {

            //Log.d(TAG,"huck is not paper");
            SerialProperties.Error_State=getResources().getString(R.string.IsOutOfPaper);
            Snackbar.make(this.getView(), getResources().getString(R.string.IsOutOfPaper), Snackbar.LENGTH_LONG).show();
            return false;
        }
        else if( (status & SerialProperties.HDX_ST_HOT )>0 )
        {
            //Log.d(TAG,"huck is too hot");
            SerialProperties.Error_State=getResources().getString(R.string.PrinterNotNormal1);
            Snackbar.make(this.getView(), getResources().getString(R.string.PrinterNotNormal1), Snackbar.LENGTH_LONG).show();
            return false;
        }
        else
        {
            // Log.d(TAG," huck is ready");
            return true;
        }


    }

    boolean Warning_When_Not_Normal()
    {
        return true;
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

    public void SendRawFileToUart(int fileID,byte [] command_head,int delay_time,int delay_time2 )
    {
        byte[] byteNum = new byte[4];
        byte[] byteNumCrc = new byte[4];
        byte[] byteNumLen = new byte[4];
        int i;
        int temp;
        //Log.e(TAG,"  TEST_quck2");
        flow_begin();
        try {
            Resources r =getResources();;
            InputStream is = r.openRawResource(fileID);
            int count = is.available();
            byte[] b = new byte[count];
            is.read(b);
            byte SendBuf[] = new byte[count  +1023];
            Arrays.fill(SendBuf,(byte)0);

            //Log.e("quck2", " read file is .available()= "+ count  );
            //get command HEAD


            //get crc
            Get_Buf_Sum(b,count,byteNum);// 17	01 7E 00   CRC
            System.arraycopy(byteNum,0,byteNumCrc,0,4);
            //Log.e("quck2", "crc0  "+ String.format("0x%02x", byteNum[0] )  );
            //Log.e("quck2", "crc1  "+ String.format("0x%02x", byteNum[1] )	);
            //Log.e("quck2", "crc2  "+ String.format("0x%02x", byteNum[2] )	);
            //Log.e("quck2", "crc3  "+ String.format("0x%02x", byteNum[3] )  );


            //get len
            int2ByteAtr(count,byteNum); //58 54 01 00	LEN
            System.arraycopy(byteNum,0, byteNumLen,0,4);
            //Log.e("quck2", "len0  "+ String.format("0x%02x", byteNum[0] )  );
            //Log.e("quck2", "len1  "+ String.format("0x%02x", byteNum[1] )	);
            // Log.e("quck2", "len2  "+ String.format("0x%02x", byteNum[2] )	);
            // Log.e("quck2", "len3  "+ String.format("0x%02x", byteNum[3] )  );

            //send command_head
            //mOutputStream.write(command_head);
            //send crc
            //mOutputStream.write(byteNumCrc);
            //send len
            //mOutputStream.write(byteNumLen);
            //send bin file
            System.arraycopy(b,0,SendBuf,0, count);
            temp= (count +63)/64;
            byte[] databuf= new byte[64];
            sleep(delay_time);
            for(i=0;i<temp;i++)
            {
                System.arraycopy(SendBuf,i*64,databuf,0,64);

                //if((i%2) == 0)
                {
                    //sleep(delay_time2);

                }
                //Log.e("quck2", " updating ffont finish:"  +((i+1)*100)/temp +"%");
                SerialProperties.iProgress=((i+1)*100)/temp;
                //handler.sendMessage(handler.obtainMessage(REFRESH_PROGRESS, 1, 0,null));
                mOutputStream.write(databuf);
                flow_check_and_Wait(10);
                //sleep(delay_time2);

            }

            //Log.e("quck2", "all data have send!!  "   );
            sleep(3000);

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            sleep(200);
            flow_end();
            //HdxUtil.SetPrinterPower(0);
        }

        //handler.sendMessage(handler.obtainMessage(ENABLE_BUTTON, 1, 0,null));
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

    public void PrintBmp(int startx, Bitmap bitmap) throws IOException {
        // byte[] start1 = { 0x0d,0x0a};
        byte[] start2 = { 0x1D, 0x76, 0x30, 0x30, 0x00, 0x00, 0x01, 0x00 };

        int width = bitmap.getWidth() + startx;
        int height = bitmap.getHeight();
        Bitmap.Config m =bitmap.getConfig();
        // 332  272  ARGB_8888
        //Log.e(TAG,"width:  "+width+" height :"+height+"   m:"+ m);
        if (width > 384)
            width = 384;
        int tmp = (width + 7) / 8;
        byte[] data = new byte[tmp];
        byte xL = (byte) (tmp % 256);
        byte xH = (byte) (tmp / 256);
        start2[4] = xL;
        start2[5] = xH;
        start2[6] = (byte) (height % 256);
        ;
        start2[7] = (byte) (height / 256);
        ;
        mOutputStream.write(start2);
        for (int i = 0; i < height; i++) {

            for (int x = 0; x < tmp; x++)
                data[x] = 0;
            for (int x = startx; x < width; x++) {
                int pixel = bitmap.getPixel(x - startx, i);
                if (Color.red(pixel) == 0 || Color.green(pixel) == 0
                        || Color.blue(pixel) == 0) {
                    // 高位在左，所以使用128 右移
                    data[x / 8] += 128 >> (x % 8);// (byte) (128 >> (y % 8));
                }
            }

            while ((printer_status & 0x13) != 0) {
                //Log.e(TAG, "printer_status=" + printer_status);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                }
            }
            mOutputStream.write(data);
			/*
			 * try { Thread.sleep(5); } catch (InterruptedException e) { }
			 */
        }
    }


    boolean  flow_check_and_Wait(int timeout)
    {


        boolean flag=false;

        Time_Check_Start();

        while(true)
        {
            sleep(5);
            if(SerialProperties.flow_buffer[0]== 0)
            {
                return true;
                //flow_start_falg = false;
                //Log.e(TAG,"Get flow ready" );
                //return true ;
            }
            sleep(50);
            if(SerialProperties.flow_buffer[0]== 0x13)//暂停标志
            {

                if(flag ==false )
                {
                    flag=true;
                    // Log.e(TAG,"Get flow 13" );
                }

                continue;
                //flow_start_falg = false;

                //return true ;
            }

            if(SerialProperties.flow_buffer[0]== 0x11)
            {

                //Log.e(TAG,"Get flow 11" );
                SerialProperties.flow_buffer[0]=  0x0;
                return true;
                //flow_start_falg = false;
                //Log.e(TAG,"Get flow ready" );
                //return true ;
            }


            if(timeout !=0)
            {
                if(TimeIsOver(timeout))
                {

                    //Log.e(TAG,"Get_Printer flow timeout");
                    return false;

                }

            }

            sleep(50);
        }


    }

    private class WriteThread extends Thread {
        int  action_code;


        public WriteThread(int  code) {
            action_code = code;
        }

        public void run() {
            super.run();
            byte status;
            status = Get_Printer_Status() ;
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

                        sendSummaryPrinting();
//                        sendCommand(0x0a);
//                        sendCommand(0x1d,0x56,0x42,0x20);
//                        sendCommand(0x1d, 0x56, 0x30);
                        //Log.e("quck2", " print char test"   );
                        break;

                }
                // ConsoleActivity.this.sleep(14000);

            } finally {
                Wait_Printer_Ready();
                lock.release();
                PrinterPowerOff();
                Handler.sendMessage(Handler.obtainMessage(1, 1,0, null));
            }

        }
    }
    private void sendSummaryPrinting() {


        try {
            if (PrintableList != null) {
                //PrinterPowerOff();

                //PrinterPowerOnAndWaitReady();
                //PrinterPowerOnAndWaitReady();

                sendCommand(CharSeq.ClearCommand);
                sendCommand(CharSeq.ClearCommand);
                Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.gclogoprint);
                PrintBmp(2, icon);
                //SendRawFileToUart(R.raw.gclogoprint,new byte[0],0,0);
                //sendCommand(CharSeq.LineFeed);
                sendCommand(CharSeq.ClearCommand);
                //sendCommand(CharSeq.LineFeed);
                mOutputStream.write("        REGISTERED BETSLIPS".getBytes("cp936"));
                sendCommand(CharSeq.ClearCommand);

                mOutputStream.write(( "MACH ID : " + device.getSerial() + "").getBytes("cp936"));sendCommand(CharSeq.ClearCommand);
//                mOutputStream.write(( "CASHIER : " + cust.getUsername() + "").getBytes("cp936"));sendCommand(CharSeq.ClearCommand);
//                mOutputStream.write(( "SHOP : " + cust.getShopname() + "").getBytes("cp936"));sendCommand(CharSeq.ClearCommand);
                mOutputStream.write(( "DATE PRINTED : " + new SimpleDateFormat("yyyy-MMM-dd").format(new Date())+ "").getBytes("cp936"));sendCommand(CharSeq.ClearCommand);
                mOutputStream.write(( "TIME PRINTED : " + new SimpleDateFormat("HH-mm-ss").format(new Date())+ "").getBytes("cp936"));sendCommand(CharSeq.ClearCommand);
                mOutputStream.write(("--------------------------------").getBytes("cp936"));sendCommand(CharSeq.ClearCommand);

                for (RegisteredTicketSubList regslip: PrintableList)
                {
                    mOutputStream.write(( "SALES DATE   : " +  new SimpleDateFormat("yyyy-MMM-dd").format(regslip.get_date()) + "").getBytes("cp936"));sendCommand(CharSeq.ClearCommand);
                    mOutputStream.write(("--------------------------------").getBytes("cp936"));sendCommand(CharSeq.ClearCommand);
                    for (TicketResource ticket: regslip.get_tickets()
                         )
                    {
                        mOutputStream.write(( ticket.getId() + " = " + ticket.getAmount() ).getBytes("cp936"));sendCommand(CharSeq.ClearCommand);
                    }
                    mOutputStream.write(("--------------------------------").getBytes("cp936"));sendCommand(CharSeq.ClearCommand);
                    mOutputStream.write(( "TOTAL" + " = " + regslip.get_amount() ).getBytes("cp936"));sendCommand(CharSeq.ClearCommand);
                    mOutputStream.write(("--------------------------------").getBytes("cp936"));sendCommand(CharSeq.ClearCommand);sendCommand(CharSeq.ClearCommand);

                }


                mOutputStream.write(("   www.winnersgoldenchance.com").getBytes("cp936"));
                sendCommand(CharSeq.LineFeed);
                sendCommand(CharSeq.LineFeed);
                sendCommand(CharSeq.ClearCommand);


            }

        }

        catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }


    //----------------------------------------------------------------------------
}

