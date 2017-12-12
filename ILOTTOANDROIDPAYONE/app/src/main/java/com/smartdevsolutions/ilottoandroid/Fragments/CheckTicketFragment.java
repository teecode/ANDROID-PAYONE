package com.smartdevsolutions.ilottoandroid.Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartdevsolutions.ilottoandroid.BetViewActivity;
import com.smartdevsolutions.ilottoandroid.R;
import com.smartdevsolutions.ilottoandroid.TicketActivity;
import com.smartdevsolutions.ilottoandroid.UserInterface.BetGridDisplay;
import com.smartdevsolutions.ilottoandroid.UserInterface.TicketGridDisplay;
import com.smartdevsolutions.ilottoandroid.Utility.CallService;
import com.smartdevsolutions.ilottoandroid.Utility.Customer;
import com.smartdevsolutions.ilottoandroid.Utility.MyApplication;
import com.smartdevsolutions.ilottoandroid.Utility.Ticket;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by T360-INNOVATIVZ on 18/06/2017.
 */

public class CheckTicketFragment extends Fragment {

    EditText mTicketID;
    Button mButtonGo;
    Button mPayoutButton;
    GridView mticketView;
    GridView mbetView;
    ProgressDialog progress;
    TicketGridDisplay adapter;
    BetGridDisplay betadapter;
    CheckTicketFragment.CheckTicketTask cticketTask;

    List<Ticket> mTickets;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.check_ticket_fragment, container, false);
        mTickets = new ArrayList<>();
        mTicketID = (EditText) rootView.findViewById(R.id.ticketid_tv);
        mButtonGo = (Button)  rootView.findViewById(R.id.ticketfindbutton);
        mPayoutButton = (Button)  rootView.findViewById(R.id.ticketpayout);
        mticketView =(GridView) rootView.findViewById(R.id.ticketgrid);


        mPayoutButton.setVisibility(View.INVISIBLE);

        mticketView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             int positiontoview = 0;

                                             @Override
                                             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                 positiontoview = position;

                                                 Ticket tick = mTickets.get(positiontoview);
                                                ((MyApplication) getActivity().getApplication()).setCheckTicketCurrentTicket(tick);
                                                 Intent intent = new Intent(getActivity(), BetViewActivity.class);
                                                 startActivity(intent);


                                             }
                                         });


        mButtonGo.setOnClickListener(GoClick );
        mPayoutButton.setOnClickListener(GoPayout );


        return rootView;
    }


    private Button.OnClickListener GoPayout = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(mTickets.size()> 0)
            {
                Ticket t = mTickets.get(0);
                if(t.getStatusid() == 2)
                {

                    showProgress("Paying Out Ticket", "Please Wait ...");
                    PayoutTicketTask pttask = new CheckTicketFragment.PayoutTicketTask(""+t.getId());
                    pttask.execute();
                }

            }

        }
    };

    private Button.OnClickListener GoClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!mTicketID.getText().equals(""))
            {
                String tid = mTicketID.getText().toString();


                if(!tid.equals(""))
                {
                    showProgress("Fetching Ticket", "Please Wait ...");
                    cticketTask = new CheckTicketFragment.CheckTicketTask(tid);
                    cticketTask.execute();
                }
            }
        }
    };


    public void showProgress(  String Title, String Message) {
        progress = ProgressDialog.show(getActivity(), Title,
                Message, true);
    }

    public class CheckTicketTask extends AsyncTask<Void, Void, Boolean> {


        String url;
        String tid;
        String debugmessage="";

        private List<Ticket> Tickets ;

        CheckTicketTask(String tid) {
            this.tid = tid;
           this.url = (getString(R.string.service_url) + getString(R.string.service_checkTicket_controller) + tid);
           Tickets = new ArrayList<Ticket>();

        }

        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            try {

                Gson gson = new Gson();
                Type type = new TypeToken<Ticket>(){}.getType();
                CallService mysevice = new CallService(url, "");
                String response =  mysevice.invokeGetService();
                if(response != ""   )
                {
                    type = new TypeToken<Ticket>(){}.getType();
                    Ticket t = gson.fromJson(response, type);
                    if(t!= null && t.getId()!=-100);
                        Tickets.add(t);
                    if(Tickets.size() > 0) {
                        debugmessage = "";
                        return true;
                    }
                    else {
                        debugmessage = "No Ticket Available";
                        return false;
                    }
                }
                else
                {
                    debugmessage += "No Ticket Found With ID " + tid;
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
                mTickets = Tickets;
                adapter = new TicketGridDisplay(getActivity(), mTickets);
                mticketView.setAdapter(adapter);

//                if(Tickets.get(0)!= null && Tickets.get(0).getBets().size() >0) {
//                    betadapter = new BetGridDisplay(getActivity(), null, Tickets.get(0).getBets());
//                    mbetView.setAdapter(betadapter);
//
//
//                }


                if(Tickets.size()>0)
                {
                    Customer cust = ((MyApplication) getActivity().getApplication()).getCurrentCustomer();
                    Ticket tick = Tickets.get(0);
                    if(tick.getStatusid() ==2 && tick.ispaid() == false && tick.getCustID() == cust.getId() )
                    {
                        mPayoutButton.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        mPayoutButton.setVisibility(View.INVISIBLE);
                    }
                }

            } else {
                mPayoutButton.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity() ,  debugmessage, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {

            progress.dismiss();
        }




    }

    public class PayoutTicketTask extends AsyncTask<Void, Void, Boolean> {


        String url;
        String tid;
        String debugmessage="";

        private Ticket paidoutTicket ;

        PayoutTicketTask(String tid) {
            this.tid = tid;
            this.url = (getString(R.string.service_url) + getString(R.string.service_TicketPayment_controller) + tid);
            //paidoutTicket = new ArrayList<Ticket>();

        }

        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            try {

                Gson gson = new Gson();
                Customer cust = ((MyApplication) getActivity().getApplication()).getCurrentCustomer();
                Type type = new TypeToken<Customer>(){}.getType();
                String json = gson.toJson(cust, type);
                CallService mysevice = new CallService(url, json);
                String response =  mysevice.invokePostService();
                if(response != ""   )
                {
                    type = new TypeToken<Ticket>(){}.getType();
                    Ticket t = gson.fromJson(response, type);
                    if(t!= null && t.getId()!=-100) {
                        paidoutTicket = t;
                        return true;
                    }
                    else {
                        debugmessage = t.getErrormessage();
                        return false;
                    }
                }
                else
                {
                    debugmessage += "No Ticket Found With ID " + tid;
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

                if(paidoutTicket != null && paidoutTicket.getId()!=-100)
                {
                    Toast.makeText(getActivity() , " Ticket Paid Out Successfully #"+ paidoutTicket.getWonamount(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getActivity() , debugmessage , Toast.LENGTH_SHORT).show();

                }

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

