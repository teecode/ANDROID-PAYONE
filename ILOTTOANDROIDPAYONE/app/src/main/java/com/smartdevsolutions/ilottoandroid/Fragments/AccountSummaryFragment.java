package com.smartdevsolutions.ilottoandroid.Fragments;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nbbse.mobiprint3.Printer;
import com.smartdevsolutions.ilottoandroid.HomeActivity;
import com.smartdevsolutions.ilottoandroid.R;
import com.smartdevsolutions.ilottoandroid.TicketActivity;
import com.smartdevsolutions.ilottoandroid.UserInterface.winningNumberGridDisplay;
import com.smartdevsolutions.ilottoandroid.Utility.AccountSummaryInput;
import com.smartdevsolutions.ilottoandroid.Utility.AccountSummaryOutput;
import com.smartdevsolutions.ilottoandroid.Utility.BetOutput;
import com.smartdevsolutions.ilottoandroid.Utility.CallService;
import com.smartdevsolutions.ilottoandroid.Utility.Customer;
import com.smartdevsolutions.ilottoandroid.Utility.MyApplication;
import com.smartdevsolutions.ilottoandroid.Utility.TicketInput;
import com.smartdevsolutions.ilottoandroid.Utility.TicketOutput;
import com.smartdevsolutions.ilottoandroid.Utility.WinningNumber;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AccountSummaryFragment extends Fragment {
    EditText mfromdateTextView;
    EditText mtodateTextView;
    Button mPrintButton;
    Button mgobutton;

    TextView staketxtview;
    TextView canctxtview;
    TextView salestxtview;
    TextView winntxtview;
    TextView baltxtview;

    ProgressDialog progress;
    Spinner mcashierdrpdown;
    List<Customer> Cashiers;
    Customer cust;

    String fd, td;

    AccountSummaryOutput Myout;
    private Printer print;





    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_account_summary, container, false);
        Cashiers = new ArrayList<Customer>();
        mfromdateTextView = (EditText) rootView.findViewById(R.id.accountfromdate);
        mtodateTextView = (EditText) rootView.findViewById(R.id.accounttodate);
        mgobutton = (Button)  rootView.findViewById(R.id.acctgobutton);
        mPrintButton = (Button)  rootView.findViewById(R.id.acctprintbutton);
        staketxtview = (TextView)  rootView.findViewById(R.id.accountstakes);
        canctxtview = (TextView)  rootView.findViewById(R.id.accountcancelled);
        salestxtview = (TextView)  rootView.findViewById(R.id.accountsales);
        winntxtview = (TextView)  rootView.findViewById(R.id.accountwinnings);
        baltxtview = (TextView)  rootView.findViewById(R.id.accountbalance);
        mcashierdrpdown = (Spinner) rootView.findViewById(R.id.cashier_spinner) ;
        print = Printer.getInstance();


        cust = ((MyApplication) getActivity().getApplication()).getCurrentCustomer();
        if(cust.getCustomertype() == 3)
        {
            try
            {
                showProgress("Getting Cashiers", "Please Wait ...");
                List<Customer> loadedcashiers = ((MyApplication) getActivity().getApplication()).getCashiersInShop();

                for (Customer c: loadedcashiers) {
                    Cashiers.add(c);
                }

                List<String> clist = new ArrayList<>();
                clist.add("ALL");
                clist.addAll(fillArrayAdapter());

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item, clist);
                mcashierdrpdown.setAdapter(adapter);
                progress.dismiss();
            }
            catch (Exception ex)
            {
                getActivity().setTitle(getResources().getString(R.string.home));
                Fragment menufragment = new menuFragment();
                FragmentManager menufragmentManager = getFragmentManager();
                menufragmentManager.beginTransaction().replace(R.id.content_frame, menufragment).commit();
                Toast.makeText(getActivity(), "Please Wait Background Service trying to load Cashiers", Toast.LENGTH_SHORT).show();
            }

        }
        else
        {
            Cashiers.add(cust);
            List<String> clist = fillArrayAdapter();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, clist);
            mcashierdrpdown.setAdapter(adapter);
        }



        mgobutton.setOnClickListener(GoClick );
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

        return rootView;
    }

    public  void printSummary()
    {
        try {
            if (Myout != null) {
                if (print.getPaperStatus() != 1) {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "No Paper.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }


                print = Printer.getInstance();
                InputStream is = getResources().openRawResource(R.raw.gclogoprint);
                print.printBitmap(is);
//                print.printText(" GOLDEN CHANCE \n     LOTTO ", 2);
//                print.printText("          ACCOUNT SUMMARY", 1);






//                    DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//                    String datefrom = format.format(fd);
//                    String dateto = format.format(td);

                    print.printText(( " FROM : [" + fd + "]") , 1);

                    print.printText(( " TO   : [" + td + "]") , 1);
                print.printText("--------------------------------", 1);
                print.printText(( " STAKE   : " + Myout.getStake() ) , 1);
                print.printText(( " CANC    : " + Myout.getCancelled()) , 1);
                print.printText(( " SALES   : " + Myout.getSales()) , 1);
                print.printText(( " WON     : " + Myout.getWinning()) , 1);
                print.printText("--------------------------------", 1);
                print.printText(( " BAL     : " + Myout.getBalance()) , 1);
                print.printText("--------------------------------", 1);

                print.printEndLine();

            }

        }
        catch (Exception ex)
        {
            Toast.makeText(getActivity() ,  "Error Printing ", Toast.LENGTH_SHORT).show();
        }
    }

    private Button.OnClickListener GoPrint = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            printSummary();

        }
    };

    private Button.OnClickListener GoClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(cust.getCustomertype() == 3) {
                if (mcashierdrpdown.getSelectedItemPosition() == 0) {
                    try {
                        int im = 2;
                        int cid = cust.getId();
                        int sid = cust.getShopid();
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
                            AcountSummaryTask tsk = new AcountSummaryTask(cid, sid, fdate, tdate, im);
                            tsk.execute();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Date Difference must be with 7days " , Toast.LENGTH_SHORT).show();
                        }
                    } catch (ParseException e) {
                        Toast.makeText(getActivity(), e.getMessage() , Toast.LENGTH_SHORT).show();
                    }


                } else {

                    try {
                        int im = 1;
                        int seleid = mcashierdrpdown.getSelectedItemPosition();
                        int cid = Cashiers.get(seleid - 1).getId();
                        int sid = cust.getShopid();
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
                            AcountSummaryTask tsk = new AcountSummaryTask(cid, sid, fdate, tdate, im);
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
            }
            else
            {
                try {
                    int im = 1;
                    int seleid = mcashierdrpdown.getSelectedItemPosition();
                    int cid = Cashiers.get(seleid).getId();
                    int sid = cust.getShopid();
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
                        AcountSummaryTask tsk = new AcountSummaryTask(cid, sid, fdate, tdate, im);
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


        }
    };

    private ArrayList<String> fillArrayAdapter() {
        ArrayList<String> retlist = new ArrayList<String>();
        for (Customer c: Cashiers) {

            String cas = c.getUsername();
            retlist.add(cas);
        }
        return  retlist;
    }

    public void showProgress(  String Title, String Message) {
        progress = ProgressDialog.show(getActivity(), Title,
                Message, true);
    }


    public class AcountSummaryTask extends AsyncTask<Void, Void, Boolean> {

        int custID;
        Date fromdate;
        Date todate;
        int shopID;
        String url;
        int inputmode;
        String debugmessage="";

        AccountSummaryOutput output ;

        private TicketOutput ticketOutput ;

        AcountSummaryTask( int CID, int SID, Date from, Date to, int IM) {
            this.custID = CID;
            this.shopID = SID;
            this.fromdate = from;
            this.todate = to;
            this.inputmode = IM;
            this.url = (getString(R.string.service_url) +getString(R.string.service_AccountSummaryCustomer_controller));

        }

        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            try {


               // TicketInput myticket = ((MyApplication)  getActivity().getApplication()).getCurrentTicket();


                AccountSummaryInput SI = new AccountSummaryInput();
                SI.setCustomer_id(custID);
                SI.setFromdate(fromdate);
                SI.setTodate(todate);
                SI.setInputmode(inputmode);
                SI.setShop_id(shopID);

                Gson gson = new Gson();
                Type type = new TypeToken<AccountSummaryInput>(){}.getType();
                String json = gson.toJson(SI, type);
                CallService mysevice = new CallService(url, json);
                String response =  mysevice.invokePostService();
                if(response != ""   )
                {
                    type = new TypeToken<AccountSummaryOutput>(){}.getType();
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
                    Myout = output;
                    staketxtview.setText(""+ output.getStake() );
                    canctxtview.setText(""+ output.getCancelled() );
                    salestxtview.setText(""+ output.getSales() );
                    winntxtview.setText(""+ output.getWinning() );
                    baltxtview.setText(""+ output.getBalance() );


                } else {
                    Myout = null;
                    Toast.makeText(getActivity(), "Error PLaying Ticket " + debugmessage, Toast.LENGTH_SHORT).show();
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




}
