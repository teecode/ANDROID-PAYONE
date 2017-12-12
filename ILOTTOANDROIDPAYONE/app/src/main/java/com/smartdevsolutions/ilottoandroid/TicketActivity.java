package com.smartdevsolutions.ilottoandroid;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;


import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;








import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.smartdevsolutions.ilottoandroid.Barcoder.util.CodeUtils;
import com.smartdevsolutions.ilottoandroid.UserInterface.BetGridDisplay;

import com.smartdevsolutions.ilottoandroid.Utility.BetInput;

import com.smartdevsolutions.ilottoandroid.Utility.BetOutput;
import com.smartdevsolutions.ilottoandroid.Utility.CallService;
import com.smartdevsolutions.ilottoandroid.Utility.Customer;
import com.smartdevsolutions.ilottoandroid.Utility.MyApplication;
import com.smartdevsolutions.ilottoandroid.Utility.TicketInput;
import com.smartdevsolutions.ilottoandroid.Utility.TicketOutput;


import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import com.nbbse.mobiprint3.Printer;



/**
 * Created by T360-INNOVATIVZ on 29/05/2017.
 */

public class TicketActivity extends AppCompatActivity {


    TextView mgamename;
    TextView mgameamount;
    GridView mgridview;
    Button mPlayButton;
    Button mcloseButton;
    Button mCancelButton;
    BetGridDisplay adapter;
    private TicketActivity.PlayGameTask PlayTask = null;
    Gson gson;
    TicketInput myticket;
    ProgressDialog progress;
    Customer customer;
    private Bitmap bmp = null;



    private Printer print;
    protected static final String TAG = "PrintDemo";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticket_input_activity);
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
                                    adapter = new BetGridDisplay(TicketActivity.this, myticket.getBetslips(), null);
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
                                                finish();
                                            }
                                        }

        );

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPrinter()) {
                    showProgress("PLAYING GAME", "PLEASE WAIT...");
                    PlayTask = new TicketActivity.PlayGameTask();
                    PlayTask.execute((Void) null);
                }
            }
        });







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
        mcloseButton =(Button) findViewById(R.id.ticketcloseButton);
        mCancelButton = (Button) findViewById(R.id.cancelticketbutton);

        myticket = ((MyApplication) getApplication()).getCurrentTicket();
        customer = ((MyApplication) getApplication()).getCurrentCustomer();

        mgamename.setText("GAME NAME : "+myticket.getGamename());

        double amount = getAmount();


        mgameamount.setText("GAME AMNT : ₦" +amount);

        adapter = new BetGridDisplay(this, myticket.getBetslips(), null);
        adapter.notifyDataSetChanged();
        mgridview.invalidateViews();
        mgridview.setAdapter(adapter);


    }

    private double getAmount() {
        double amount = 0;
        for (BetInput bet: myticket.getBetslips()) {
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


                TicketInput myticket = ((MyApplication) getApplication()).getCurrentTicket();


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
                    Customer cust = ((MyApplication) getApplication()).getCurrentCustomer();
                    double prevamount = cust.getWalletbalance();
                    cust.setWalletbalance(prevamount - ticketOutput.getAmount());
                    ((MyApplication) getApplication()).setCurrentCustomer(cust);
                    Toast.makeText(TicketActivity.this, "Bet Placed Successfully " + ticketOutput.getId(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(TicketActivity.this, "Printing Ticket ", Toast.LENGTH_SHORT).show();
                    PrintTicket(ticketOutput);
                    //genrateByteprintgame(ticketOutput);
                   // genrateprintgame(ticketOutput);
                    AlertDialog.Builder builder = new AlertDialog.Builder(TicketActivity.this);
                    builder.setMessage("Do you want to Replay Bet?")
                            .setPositiveButton("Yes", dialogReplayListener)
                            .setNegativeButton("No", dialogReplayListener).show();


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

            //print = Printer.getInstance();
//            Bitmap Logo = BitmapFactory.decodeResource(getResources(), R.drawable.gclogo);
//            File mFile1 = Environment.getExternalStorageDirectory();
//            String fileName ="gclogo.bmp";
//            String sdPath = mFile1.getAbsolutePath().toString()+"/"+fileName;
//            InputStream is  = getResources().openRawResource(R.raw.gclogo);
//            print.printBitmap(sdPath);
//             Print bitmap if file exists


//

            print = Printer.getInstance();
            InputStream is = getResources().openRawResource(R.raw.gclogoprint);
            print.printBitmap(is);
//            print.printText(" GOLDEN CHANCE \n     LOTTO ", 2);
            print.printText("   "+ticket.getGamename(), 2);
            print.printText("TICKET ID:"+ticket.getId(), 2);

            print.printText("REG DATE : "+ticket.getDate(), 1);
            print.printText("CASHIER : "+customer.getUsername(), 1);
            print.printText("SHOP : "+customer.getShopcode(), 1);
            print.printText("AMOUNT:#"+ticket.getAmount(),  2);
            print.printText("--------------------------------", 1);


            for (BetOutput bet: ticket.getBets())
            {
                print.printText("NAP :"+ bet.getNap()+ "     STK/LN :#"+bet.getStake_per_line(),  1);
                print.printText("LINES :"+ bet.getNo_of_lines()+ "     AMT :#"+bet.getAmount(),  1);
                print.printText("BET1: "+ bet.getStakebet1(),  2);


                if(!bet.getStakebet2().equals(""))
                {
                    print.printText("AG: "+ bet.getStakebet2(),  2);
                }
                print.printText("--------------------------------", 1);
            }
            print.printText("         <<  GOODLUCK  >>",  1);
            print.printText("--------------------------------", 1);
            print.printText("            **"+ticket.getId() +"**", 1);
            print.printText("--------------------------------", 1);

            try {
                bmp = CodeUtils.CreateTwoDCode(("" + ticket.getId()));
                print.printBitmap(bmp);

            }catch(Exception ex)
            {

            }

            print.printEndLine();





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






}

