package com.smartdevsolutions.ilottoandroid;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nbbse.mobiprint3.Printer;
import com.smartdevsolutions.ilottoandroid.ApiResource.AddBetSlipResource;
import com.smartdevsolutions.ilottoandroid.ApiResource.AddTicketResource;
import com.smartdevsolutions.ilottoandroid.ApiResource.DeviceResource;
import com.smartdevsolutions.ilottoandroid.Barcoder.util.CodeUtils;
import com.smartdevsolutions.ilottoandroid.UserInterface.BetGridDisplay;
import com.smartdevsolutions.ilottoandroid.Utility.BetOutput;
import com.smartdevsolutions.ilottoandroid.Utility.CallService;
import com.smartdevsolutions.ilottoandroid.Utility.MyApplication;
import com.smartdevsolutions.ilottoandroid.Utility.TicketInput;
import com.smartdevsolutions.ilottoandroid.Utility.TicketOutput;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android_serialport_api.CharacterSequence;
import android_serialport_api.SerialProperties;
import hdx.HdxUtil;


/**
 * Created by T360-INNOVATIVZ on 29/05/2017.
 */

public class TicketActivity extends SerialPortActivity {


    TextView mgamename;
    TextView mgameamount;
    GridView mgridview;
    Button mPlayButton;
    Button mAddButton;
    Button mcloseButton;
    Button mCancelButton;
    BetGridDisplay adapter;
    private TicketActivity.PlayGameTask PlayTask = null;
    Gson gson;
    AddTicketResource myticket;
    ProgressDialog progress;
    DeviceResource device;
    private Bitmap bmp = null;

    private CharacterSequence CharSeq;
    MyHandler Handler;

    private android_serialport_api.SerialProperties SerialProperties;
    ExecutorService pool = Executors.newSingleThreadExecutor();
    PowerManager.WakeLock lock;
    int printer_status = 0;



    private Printer print;
    protected static final String TAG = "PrintDemo";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticket_input_activity);
        HdxUtil.SwitchSerialFunction(HdxUtil.SERIAL_FUNCTION_PRINTER);
        PowerManager pm = (PowerManager) getApplicationContext()
                .getSystemService(this.POWER_SERVICE);
        //lock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "TicketPlay");
        Handler = new MyHandler();
        CharSeq = new CharacterSequence();
        initialiseControls();
        SerialProperties = new SerialProperties();
        setTitle("BETSLIP");
        print = Printer.getInstance();
        initialiseControls();




        mgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int positiontodelete = 0;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positiontodelete = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(TicketActivity.this);
                builder.setMessage("Delete this bet?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();




            }

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int choice) {
                    switch (choice) {
                        case DialogInterface.BUTTON_POSITIVE:
                            try {
                                myticket.getBetslips().remove(positiontodelete);
                                ((MyApplication) getApplication()).setCurrentTicket(myticket);
                                if(myticket.getBetslips().size() > 0) {
                                    adapter = new BetGridDisplay(TicketActivity.this, myticket.getBetslips(), null, ((MyApplication)getApplication()).getFetchedGames().get(0).getBetTypes());
                                    adapter.notifyDataSetChanged();
                                    mgridview.invalidateViews();
                                    // mgridview.setAdapter(adapter);
                                    mgameamount.setText("GAME AMNT : ₦" + getAmount());
                                }
                                else {
                                    finish();
                                }

                            } catch (Exception ex) {
                                // TODO let the user know the file couldn"t be deleted
                            }
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };

        });

        mcloseButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                finish();
                                            }
                                        }
        );

        mCancelButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                ((MyApplication) getApplication()).setCurrentTicket(null);
                                                ((MyApplication) getApplication()).setCurrentBet(null);
                                                ((MyApplication) getApplication()).setSelectedGame(null);
                                                Intent myIntent = new Intent(TicketActivity.this, HomeActivity.class);
                                                startActivity(myIntent);
                                            }
                                        }

        );

        mAddButton.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 finish();
                                             }
                                         }

        );


        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(checkPrinter()) {
                    showProgress("PLAYING GAME", "PLEASE WAIT...");
                    PlayTask = new TicketActivity.PlayGameTask();
                    PlayTask.execute((Void) null);
//                }
            }
        });







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
        if(keyCode == KeyEvent.KEYCODE_CALL) {
            showProgress("PLAYING GAME", "PLEASE WAIT...");
            PlayTask = new TicketActivity.PlayGameTask();
            PlayTask.execute((Void) null);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean checkPrinter()
    {
        //print = Printer.getInstance();
        switch(print.getPaperStatus())
        {
            case Printer.PRINTER_EXIST_PAPER:
                return true;
            case Printer.PRINTER_NO_PAPER:
                Toast.makeText(TicketActivity.this, "No Printing Paper " , Toast.LENGTH_SHORT).show();
                return false;
            case Printer.PRINTER_ERROR:
                Toast.makeText(TicketActivity.this, "Printer Error " , Toast.LENGTH_SHORT).show();
                return false;
            case Printer.PRINTER_PAPER_ERROR:
                Toast.makeText(TicketActivity.this, "Printer Paper Error " , Toast.LENGTH_SHORT).show();
                return false;
            default:
                Toast.makeText(TicketActivity.this, "Somthing is wrong with printer " , Toast.LENGTH_SHORT).show();
                return false;

        }

    }



    public void initialiseControls() {

        Gson gson = new Gson();
        mgamename = (TextView) findViewById(R.id.ticket_gamename_tv);
        mgameamount = (TextView) findViewById(R.id.ticket_gameamount_tv);
        mgridview = (GridView) findViewById(R.id.betgrid);
        mPlayButton= (Button) findViewById(R.id.playticketbutton);
        mAddButton = (Button) findViewById(R.id.addbetbutton);
        mcloseButton =(Button) findViewById(R.id.ticketcloseButton);
        mCancelButton = (Button) findViewById(R.id.cancelticketbutton);

        myticket = ((MyApplication) getApplication()).getCurrentTicket();
        device = ((MyApplication) getApplication()).getCurrentTerminal();

        mgamename.setText("GAME NAME : "+myticket.getGamename(((MyApplication)getApplication()).getFetchedGames()));

        double amount = getAmount();


        mgameamount.setText("GAME AMNT : ₦" +amount);

        adapter = new BetGridDisplay(this, myticket.getBetslips(), null, ((MyApplication)getApplication()).getFetchedGames().get(0).getBetTypes());
        adapter.notifyDataSetChanged();
        mgridview.invalidateViews();
        mgridview.setAdapter(adapter);


    }

    private double getAmount() {
        double amount = 0;
        for (AddBetSlipResource bet: myticket.getBetslips()) {
            amount += bet.getAmount();
        }
        return amount;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(  String Title, String Message) {
        progress = ProgressDialog.show(TicketActivity.this, Title,
                Message, true);
    }


    public class PlayGameTask extends AsyncTask<Void, Void, Boolean> {


        String url;
        String debugmessage="";

        private TicketOutput ticketOutput ;

        PlayGameTask() {
            this.url = (getString(R.string.service_url) +getString(R.string.service_playgame_controller));

        }

        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            try {


                AddTicketResource myticket = ((MyApplication) getApplication()).getCurrentTicket();


                Gson gson = new Gson();
                Type type = new TypeToken<TicketInput>(){}.getType();
                String json = gson.toJson(myticket, type);
                CallService mysevice = new CallService(url, json);
                String response =  mysevice.invokePostService();
                if(response != ""   )
                {
                    type = new TypeToken<TicketOutput>(){}.getType();
                    ticketOutput = gson.fromJson(response, type);
                    if(ticketOutput.getId()!=-100) {
                        debugmessage = "";
                        return true;
                    }
                    else {
                        debugmessage = ticketOutput.getCreationMessage();
                        return false;
                    }
                }
                else
                {
                    debugmessage += "Poor Network);  byteList.add(Please Check Your Network and Try Again!!!";
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
                    DeviceResource dev = ((MyApplication) getApplication()).getCurrentTerminal();
                   // double prevamount = dev.getWalletbalance();
                   // dev.setWalletbalance(prevamount - ticketOutput.getAmount());
                   // ((MyApplication) getApplication()).setCurrentTerminal(dev);
                    Toast.makeText(TicketActivity.this, "Bet Placed Successfully " + ticketOutput.getId(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(TicketActivity.this, "Printing Ticket ", Toast.LENGTH_SHORT).show();
                    PrintTicket(ticketOutput);
                    //genrateByteprintgame(ticketOutput);
                   // genrateprintgame(ticketOutput);
                    //luwa told me to remove alertDialog for replay
                    //AlertDialog.Builder builder = new AlertDialog.Builder(TicketActivity.this);
                    //builder.setMessage("Do you want to Replay Bet?").setPositiveButton("Yes", dialogReplayListener).setNegativeButton("No", dialogReplayListener).show();


                    //this is what i replaced the replay with
                    try {
                        ((MyApplication) getApplication()).setCurrentTicket(null);
                        ((MyApplication) getApplication()).setCurrentBet(null);
                        ((MyApplication) getApplication()).setSelectedGame(null);
                        Intent myIntent = new Intent(TicketActivity.this, HomeActivity.class);
                        startActivity(myIntent);

                    } catch (Exception ex) {
                        // TODO let the user know the file couldn"t be deleted
                    }


                } else {
                    Toast.makeText(TicketActivity.this, "Error PLaying Ticket " + debugmessage, Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                Toast.makeText(TicketActivity.this, "post serv err: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        public  void PrintTicket(TicketOutput ticket)
        {
           // Handler.sendMessage(Handler.obtainMessage(0, 1,0, null));
            new WriteThread(0, ticket).start();


//

//            print = Printer.getInstance();
//            InputStream is = getResources().openRawResource(R.raw.gclogoprint);
//            print.printBitmap(is);
////            print.printText(" GOLDEN CHANCE \n     LOTTO ", 2);
//            print.printText("   "+ticket.getGamename(), 2);
//            print.printText("TICKET ID:"+ticket.getId(), 2);
//
//            print.printText("REG DATE : "+ticket.getDate(), 1);
//            print.printText("CASHIER : "+customer.getUsername(), 1);
//            print.printText("SHOP : "+customer.getShopcode(), 1);
//            print.printText("AMOUNT:#"+ticket.getAmount(),  2);
//            print.printText("--------------------------------", 1);
//
//
//            for (BetOutput bet: ticket.getBets())
//            {
//                print.printText("NAP :"+ bet.getNap()+ "     STK/LN :#"+bet.getStake_per_line(),  1);
//                print.printText("LINES :"+ bet.getNo_of_lines()+ "     AMT :#"+bet.getAmount(),  1);
//                print.printText("BET1: "+ bet.getStakebet1(),  2);
//
//
//                if(!bet.getStakebet2().equals(""))
//                {
//                    print.printText("AG: "+ bet.getStakebet2(),  2);
//                }
//                print.printText("--------------------------------", 1);
//            }
//            print.printText("         <<  GOODLUCK  >>",  1);
//            print.printText("--------------------------------", 1);
//            print.printText("            **"+ticket.getId() +"**", 1);
//            print.printText("--------------------------------", 1);
//
//            try {
//                bmp = CodeUtils.CreateTwoDCode(("" + ticket.getId()));
//                print.printBitmap(bmp);
//
//            }catch(Exception ex)
//            {
//
//            }
//
//            print.printEndLine();





//        if(strcmp(Welcome.serverdate,"")!= 0)
//        {
//            sdkPrintStr( Welcome.print_message1, st_font, SDK_PRINT_MIDDLEALIGN, 0, spacing);
//            sdkPrintStr(concat("Whatsapp :", Welcome.whatsapp), st_font, SDK_PRINT_MIDDLEALIGN, 0, spacing);
//            sdkPrintStr(concat("Facebook :", Welcome.facebook), st_font, SDK_PRINT_MIDDLEALIGN, 0, spacing);
//            sdkPrintStr(concat("Instagram :", Welcome.instagram), st_font, SDK_PRINT_MIDDLEALIGN, 0, spacing);
//            sdkPrintStr(concat("Twitter :", Welcome.twitter), st_font, SDK_PRINT_MIDDLEALIGN, 0, spacing);
//            sdkPrintStr(concat("Web :", Welcome.website), st_font, SDK_PRINT_MIDDLEALIGN, 0, spacing);
//            sdkPrintStr(concat("Phone :", Welcome.contact_no), st_font, SDK_PRINT_MIDDLEALIGN, 0, spacing);
//
//        }
//        sdkPrintStr("</smartdev> ", st_font, SDK_PRINT_MIDDLEALIGN, 0, spacing);


        }



        @Override
        protected void onCancelled() {

            progress.dismiss();
        }

        DialogInterface.OnClickListener dialogReplayListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {
                switch (choice) {
                    case DialogInterface.BUTTON_POSITIVE:
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        try {
                            ((MyApplication) getApplication()).setCurrentTicket(null);
                            ((MyApplication) getApplication()).setCurrentBet(null);
                            ((MyApplication) getApplication()).setSelectedGame(null);
                            Intent myIntent = new Intent(TicketActivity.this, HomeActivity.class);
                            startActivity(myIntent);

                        } catch (Exception ex) {
                            // TODO let the user know the file couldn"t be deleted
                        }
                        break;
                }
            }
        };


    }


    //---------------------------------------------------------------------------
    private class MyHandler extends android.os.Handler {
        public void handleMessage(Message msg) {
            if (SerialProperties.stop == true)
                return;
            switch (msg.what) {
                case 0:
                    mPlayButton.setEnabled(false);
                    break;
                case 1 :
                    mPlayButton.setEnabled(true);
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
          //  Snackbar.make(this., "Printing Unknown Error", Snackbar.LENGTH_LONG).show();
            return  false;

        }

        if( (status & SerialProperties.HDX_ST_NO_PAPER1 )>0 )
        {

            //Log.d(TAG,"huck is not paper");
            SerialProperties.Error_State=getResources().getString(R.string.IsOutOfPaper);
            //Snackbar.make(this.getView(), getResources().getString(R.string.IsOutOfPaper), Snackbar.LENGTH_LONG).show();
            return false;
        }
        else if( (status & SerialProperties.HDX_ST_HOT )>0 )
        {
            //Log.d(TAG,"huck is too hot");
            SerialProperties.Error_State=getResources().getString(R.string.PrinterNotNormal1);
           // Snackbar.make(this.getView(), getResources().getString(R.string.PrinterNotNormal1), Snackbar.LENGTH_LONG).show();
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
        TicketOutput printticket;

        public WriteThread(int  code, TicketOutput ticket) {
            action_code = code;
            this.printticket = ticket;
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

                        sendPrintWinnings(printticket);
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
    private void sendPrintWinnings(TicketOutput ticket) {


        try {
            if (ticket != null) {
                //PrinterPowerOff();

                PrinterPowerOnAndWaitReady();
                //PrinterPowerOnAndWaitReady();

                sendCommand(CharSeq.ClearCommand);
                sendCommand(CharSeq.ClearCommand);
                Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.gclogoprint);
                PrintBmp(2, icon);
                //SendRawFileToUart(R.raw.gclogoprint,new byte[0],0,0);
                //sendCommand(CharSeq.LineFeed);
                sendCommand(CharSeq.ClearCommand);

//                sendCommand(CharSeq.DoubleHeight); // double height
//
//                mOutputStream.write("       GOLDEN CHANCE LOTTO".getBytes("cp936"));
//
//                sendCommand(CharSeq.DoubleHeightCancel);
               // sendCommand(CharSeq.LineFeed);
                mOutputStream.write("            GAME TICKET".getBytes("cp936"));
                sendCommand(CharSeq.ClearCommand);

                sendCommand(CharSeq.DoubleWeight);
                mOutputStream.write(("   "+ticket.getGamename()).getBytes("cp936"));
                sendCommand(CharSeq.ClearCommand);
                mOutputStream.write(("TICKET ID:"+ticket.getId()).getBytes("cp936"));
                sendCommand(CharSeq.DoubleWeightCancel);
                sendCommand(CharSeq.ClearCommand);
                mOutputStream.write(("REG DATE : "+ticket.getDate()).getBytes("cp936"));
                sendCommand(CharSeq.ClearCommand);
                mOutputStream.write(("CASHIER : "+device.getSerial()).getBytes("cp936"));
//                sendCommand(CharSeq.ClearCommand);
//                mOutputStream.write(("SHOP : "+device.getShopcode()).getBytes("cp936"));
                sendCommand(CharSeq.ClearCommand);
                sendCommand(CharSeq.DoubleWeight);
                mOutputStream.write(("AMOUNT:#"+ticket.getAmount()).getBytes("cp936"));
                sendCommand(CharSeq.DoubleWeightCancel);
                sendCommand(CharSeq.ClearCommand);
                mOutputStream.write(("--------------------------------").getBytes("cp936"));
                sendCommand(CharSeq.ClearCommand);


                for (BetOutput bet: ticket.getBets())
                {
                    mOutputStream.write(("NAP :"+ bet.getNap()+ "     STK/LN :#"+bet.getStake_per_line()).getBytes("cp936"));
                    sendCommand(CharSeq.ClearCommand);
                    mOutputStream.write(("LINES :"+ bet.getNo_of_lines()+ "     AMT :#"+bet.getAmount()).getBytes("cp936"));
                    sendCommand(CharSeq.ClearCommand);
                    sendCommand(CharSeq.DoubleWeight);
                    mOutputStream.write(("BET1: "+ bet.getStakebet1()).getBytes("cp936"));
                    sendCommand(CharSeq.DoubleWeightCancel);
                    sendCommand(CharSeq.ClearCommand);


                    if(!bet.getStakebet2().equals(""))
                    {
                        sendCommand(CharSeq.DoubleWeight);
                        mOutputStream.write(("AG: "+ bet.getStakebet2()).getBytes("cp936"));
                        sendCommand(CharSeq.DoubleWeightCancel);
                        sendCommand(CharSeq.LineFeed);
                    }
                    mOutputStream.write(("--------------------------------").getBytes("cp936"));
                    sendCommand(CharSeq.LineFeed);
                }
                //sendCommand(CharSeq.DoubleWeight);
                mOutputStream.write(("         <<  GOODLUCK  >>").getBytes("cp936"));
                //sendCommand(CharSeq.DoubleWeightCancel);
                sendCommand(CharSeq.ClearCommand);
                mOutputStream.write(("--------------------------------").getBytes("cp936"));
                sendCommand(CharSeq.ClearCommand);
                sendCommand(CharSeq.DoubleWeight);
                mOutputStream.write(("   **"+ticket.getId() +"**").getBytes("cp936"));
                sendCommand(CharSeq.DoubleWeightCancel);
                sendCommand(CharSeq.ClearCommand);
                mOutputStream.write(("--------------------------------").getBytes("cp936"));
                sendCommand(CharSeq.ClearCommand);
                mOutputStream.write(("   www.winnersgoldenchance.com").getBytes("cp936"));
                sendCommand(CharSeq.ClearCommand);
                sendCommand(CharSeq.LineFeed);
                sendCommand(CharSeq.LineFeed);
                //sendCommand(CharSeq.ClearCommand);
                try {
                    bmp = CodeUtils.CreateTwoDCode(("" + ticket.getId()));
                    //PrintBmp(1,bmp);
                    //print.printBitmap(bmp);

                }catch(Exception ex)
                {

                }

             //   print.printEndLine();




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

