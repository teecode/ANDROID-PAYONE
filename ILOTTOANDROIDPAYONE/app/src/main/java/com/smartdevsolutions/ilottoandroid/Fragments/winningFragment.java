package com.smartdevsolutions.ilottoandroid.Fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nbbse.mobiprint3.Printer;
import com.smartdevsolutions.ilottoandroid.BuildConfig;
import com.smartdevsolutions.ilottoandroid.R;
import com.smartdevsolutions.ilottoandroid.UserInterface.winningNumberGridDisplay;
import com.smartdevsolutions.ilottoandroid.Utility.CallService;
import com.smartdevsolutions.ilottoandroid.Utility.TicketInput;
import com.smartdevsolutions.ilottoandroid.Utility.WinningNumber;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by T360-INNOVATIVZ on 30/05/2017.
 */

public class winningFragment extends Fragment {

    TextView mdateTextView;
    Button mButtonGo;
    Button mPrintButton;
    GridView mwinningView;
    ProgressDialog progress;
    winningNumberGridDisplay adapter;
    WinningNumberTask winTask;
    List<WinningNumber> mwinningNumbers;
    private Printer print;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.winning_number_fragment, container, false);
        mwinningNumbers = new ArrayList<>();
        mdateTextView = (TextView) rootView.findViewById(R.id.winningDateeditText);
        mButtonGo = (Button)  rootView.findViewById(R.id.winninggobutton);
        mPrintButton = (Button)  rootView.findViewById(R.id.winningprintbutton);
        mwinningView =(GridView) rootView.findViewById(R.id.winninggrid);
        print = Printer.getInstance();
        showProgress("Fetching Winning Numbers", "Please Wait ...");
        winTask = new WinningNumberTask("");
        winTask.execute();

        mButtonGo.setOnClickListener(GoClick );
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

                mdateTextView.setText(sdf.format(myCalendar.getTime()));
            }


        };

        mdateTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date , myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        return rootView;
    }


    private Button.OnClickListener GoClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!mdateTextView.getText().equals(""))
            {
                String date = mdateTextView.getText().toString();


                if(!date.equals(""))
                {
                    showProgress("Fetching Winning Numbers", "Please Wait ...");
                    winTask = new WinningNumberTask(date);
                    winTask.execute();
                }



            }
        }
    };

    private Button.OnClickListener GoPrint = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mwinningNumbers.size() > 0 && checkPrinter() ) {
                printWinning();
            }
            else
            {
                Toast.makeText(getActivity(), "Cannot Print Empty Winnings", Toast.LENGTH_SHORT).show();
            }
        }
    };


    public boolean checkPrinter()
    {
        //print = Printer.getInstance();
        switch(print.getPaperStatus())
        {
            case Printer.PRINTER_EXIST_PAPER:
                return true;
            case Printer.PRINTER_NO_PAPER:
                Toast.makeText(getActivity(), "No Printing Paper " , Toast.LENGTH_SHORT).show();
                return false;
            case Printer.PRINTER_ERROR:
                Toast.makeText(getActivity(), "Printer Error " , Toast.LENGTH_SHORT).show();
                return false;
            case Printer.PRINTER_PAPER_ERROR:
                Toast.makeText(getActivity(), "Printer Paper Error " , Toast.LENGTH_SHORT).show();
                return false;
            default:
                Toast.makeText(getActivity(), "Somthing is wrong with printer "+print.getPaperStatus()  , Toast.LENGTH_SHORT).show();
                return false;

        }

    }

    public boolean checkPrinterStatus()
    {
        //print = Printer.getInstance();
        switch(print.getPrinterStatus())
        {
            case Printer.PRINTER_READY:
                return true;
            case Printer.PRINTER_NO_PAPER:
                Toast.makeText(getActivity(), "No Printing Paper " , Toast.LENGTH_SHORT).show();
                return false;
            case Printer.PRINTER_ERROR:
                Toast.makeText(getActivity(), "Printer Error " , Toast.LENGTH_SHORT).show();
                return false;

            default:
                Toast.makeText(getActivity(), "Somthing is wrong with printer " , Toast.LENGTH_SHORT).show();
                return false;

        }

    }

    public  void printWinning()
    {
        try {
            if (mwinningNumbers.size() > 0) {
                if (print.getPaperStatus() != 1) {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "No Paper.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }


//                File mFile1 = Environment.getExternalStorageDirectory();
//                String fileName ="gclogo.png";
//                Uri path = Uri.parse("android.resource://"+ BuildConfig.APPLICATION_ID+"/" + R.drawable.gclogo);
//                String sdPath = mFile1.getAbsolutePath().toString()+"/"+fileName;
//                print.printBitmap(path.getPath());
//                Bitmap Icon = BitmapFactory.decodeResource(getResources(), R.drawable.gctree);
                //                print.printBitmap(Icon);
//                print.printBitmap(Icon, Printer.BMP_PRINT_SLOW );
//                print.printBitmap(Icon, Printer.BMP_PRINT_FAST );
//                print.printBitmap(is, Printer.BMP_PRINT_SLOW);

                print = Printer.getInstance();
                InputStream is = getResources().openRawResource(R.raw.gclogoprint);
                print.printBitmap(is);

//                print.printText(" GOLDEN CHANCE \n     LOTTO ", 2);
//                print.printText("        WINNING NUMBERS", 1);



                for (WinningNumber win : mwinningNumbers) {

                    java.util.Date gamedate = win.getGameDateInstance();
                    DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    String date = format.format(gamedate);
                    print.printText((win.getGamename() + " [" + date + "]") , 1);

                    print.printText((win.getWinningNumbers()) , 2);

                    if (!win.isghana()) {

                        print.printText((win.getMachineNumbers()) , 2);
                    }

                    print.printText("--------------------------------", 1);


                }


                print.printEndLine();
                //Toast.makeText(getActivity() ,  "Printing Complete", Toast.LENGTH_SHORT).show();
//                getActivity().setResult(getActivity().RESULT_OK);
//                getActivity().finish();
            }

        }
        catch (Exception ex)
        {
            Toast.makeText(getActivity() ,  "Error Printing ", Toast.LENGTH_SHORT).show();
        }
    }



    public void showProgress(  String Title, String Message) {
        progress = ProgressDialog.show(getActivity(), Title,
                Message, true);
    }

    public class WinningNumberTask extends AsyncTask<Void, Void, Boolean> {


        String url;
        String debugmessage="";

        private List<WinningNumber> Winnings ;

        WinningNumberTask(String date) {
            if(date.equals("")) {
                this.url = (getString(R.string.service_url) + getString(R.string.service_winningnumber_controller));
            }
            else {
                this.url = (getString(R.string.service_url) + getString(R.string.service_winningnumberfordate_controller) + date);
            }
            Winnings = new ArrayList<WinningNumber>();

        }

        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            try {

                Gson gson = new Gson();
                Type type ;
                CallService mysevice = new CallService(url, "");
                String response =  mysevice.invokeGetService();
                if(response != ""   )
                {
                    type = new TypeToken<List<WinningNumber>>(){}.getType();
                    Winnings = gson.fromJson(response, type);
                    if(Winnings.size()> 0) {
                        debugmessage = "";
                        return true;
                    }
                    else {
                        debugmessage = "No winning available for date";
                        return false;
                    }
                }
                else
                {
                    debugmessage += "Poor Network, Please Check Your Network and Try Again!!!";
                    return false ;
                }

            } catch (Exception e) {
                debugmessage = e.getMessage();
                return false;
            }


        }

        @Override
        protected void onPostExecute(final Boolean success) {

            progress.dismiss();
            if (success) {
                mwinningNumbers = Winnings;
                adapter = new winningNumberGridDisplay(getActivity(), Winnings);
                mwinningView.setAdapter(adapter);

            } else {
                Toast.makeText(getActivity() ,  debugmessage, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {

            progress.dismiss();
        }




    }
}

