package com.smartdevsolutions.ilottoandroid.UserInterface;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartdevsolutions.ilottoandroid.R;
import com.smartdevsolutions.ilottoandroid.Utility.Bet;
import com.smartdevsolutions.ilottoandroid.Utility.BetInput;
import com.smartdevsolutions.ilottoandroid.Utility.Ticket;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by T360-INNOVATIVZ on 18/06/2017.
 */

public class TicketGridDisplay extends BaseAdapter {

    private Context mContext;
    private final List<Ticket> Tickets ;

    public TicketGridDisplay(Context c, List<Ticket> Tickets) {



        this.mContext = c;
        this.Tickets = Tickets;
       


    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
       
            return Tickets.size();
        

    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.ticket_grid_single, null);


        } else {
            grid = (View) convertView;
        }
        try
        {
            TextView mticketgridgamenametextView = (TextView) grid.findViewById(R.id.ticket_grid_gamename_tv);
            TextView mticketgridticketidtextView = (TextView) grid.findViewById(R.id.ticket_grid_ticketid_tv);
            TextView mticketgridbetcounttextView = (TextView) grid.findViewById(R.id.ticket_grid_betcount_tv);
            TextView mticketgridamounttextView = (TextView) grid.findViewById(R.id.ticket_grid_amount_tv);
            TextView mticketgridstatustextView = (TextView) grid.findViewById(R.id.ticket_grid_status_tv);
            TextView mticketgridwonamounttextView = (TextView) grid.findViewById(R.id.ticket_grid_amtwon_tv);



            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String gamename = Tickets.get(position).getGamename() + "  [ " + dateFormat.format(Tickets.get(position).getDateInstance()) +"]";

            mticketgridgamenametextView.setText("[#" + (position + 1) + "] GAME : " + gamename);
            mticketgridticketidtextView.setText("TICKET ID :" + Tickets.get(position).getId());
            mticketgridbetcounttextView.setText("BET COUNT :#" + Tickets.get(position).getBetcount());
            mticketgridamounttextView.setText("AMT :₦" + Tickets.get(position).getAmount());
            mticketgridstatustextView.setText("STATUS :" + Tickets.get(position).getStatusmessage().toUpperCase());
            mticketgridwonamounttextView.setText("WON-AMT :₦ :" + Tickets.get(position).getWonamount());

            if(Tickets.get(position).getStatusid() == 3) {
                mticketgridstatustextView.setTextColor(Color.RED);
                mticketgridticketidtextView.setTextColor(Color.RED);
            }
            else if(Tickets.get(position).getStatusid() == 2) {
                mticketgridstatustextView.setTextColor(Color.GREEN);
                mticketgridticketidtextView.setTextColor(Color.GREEN);
            }
        }
        catch (Exception ex)
        {

        }
        return grid;
    }
}
