package com.smartdevsolutions.ilottoandroid;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.smartdevsolutions.ilottoandroid.ApiResource.DeviceResource;
import com.smartdevsolutions.ilottoandroid.ApiResource.TicketResource;
import com.smartdevsolutions.ilottoandroid.UserInterface.BetGridDisplay;
import com.smartdevsolutions.ilottoandroid.Utility.MyApplication;

/**
 * Created by teecodez on 12/1/2017.
 */

public class BetViewActivity extends AppCompatActivity

    {



        GridView mgridview;
        Button mcloseButton;
        Button mCancelButton;
        BetGridDisplay adapter;
        TextView mTID;
        TicketResource myticket;
        ProgressDialog progress;
        DeviceResource device;

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.bets_view_activity);
            setTitle("BETS");

            initialiseControls();





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

        }

        public void initialiseControls() {

            mgridview = (GridView) findViewById(R.id.betgrid);
            mcloseButton =(Button) findViewById(R.id.betcloseButton);
            mCancelButton = (Button) findViewById(R.id.cancelbetbutton);
            mTID = (TextView) findViewById(R.id.bet_ticketId_tv);
            myticket = ((MyApplication) getApplication()).getCheckTicketCurrentTicket();
            device = ((MyApplication) getApplication()).getCurrentTerminal();
            mTID.setText("TICKET ID: "+ myticket.getId());
            adapter = new BetGridDisplay(this,  null,myticket.getBetslips(), ((MyApplication)getApplication()).getFetchedGames().get(0).getBetTypes());
            adapter.notifyDataSetChanged();
            mgridview.invalidateViews();
            mgridview.setAdapter(adapter);


        }
}
