package com.smartdevsolutions.ilottoandroid.UserInterface;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartdevsolutions.ilottoandroid.R;
import com.smartdevsolutions.ilottoandroid.Utility.Bet;
import com.smartdevsolutions.ilottoandroid.Utility.BetInput;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by T360-INNOVATIVZ on 29/05/2017.
 */

public class BetGridDisplay extends BaseAdapter {
    private Context mContext;
    private final List<BetInput> BetSlip ;
    private final List<Bet> Bets;
    private final boolean istickettype ; //false is BetInput true is Bet

    public BetGridDisplay(Context c, List<BetInput> betslip , List<Bet> bet) {



            this.mContext = c;
            this.BetSlip = betslip;
            this.Bets = bet;

            if (betslip != null)
                istickettype = false;
            else
                istickettype = true;


    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if(istickettype)
            return Bets.size();
        return  BetSlip.size();

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
                grid = inflater.inflate(R.layout.bet_grid_single, null);


            } else {
                grid = (View) convertView;
            }
        try {
            TextView mbetgridnaptextView = (TextView) grid.findViewById(R.id.bet_grid_nap_tv);
            TextView mbetgridlinestextView = (TextView) grid.findViewById(R.id.bet_grid_line_tv);
            TextView mbetgridatklintextView = (TextView) grid.findViewById(R.id.bet_grid_stkperline_tv);
            TextView mbetgridamounttextView = (TextView) grid.findViewById(R.id.bet_grid_amount_tv);
            TextView mbetgridbet1textView = (TextView) grid.findViewById(R.id.bet_grid_bet1_tv);
            TextView mbetgridbet2textView = (TextView) grid.findViewById(R.id.bet_grid_bet2_tv);
            TextView mbetstatus = (TextView) grid.findViewById(R.id.bet_grid_status_tv);
            LinearLayout mbetstatuslayout = (LinearLayout) grid.findViewById(R.id.bet_status_panel);

            if (istickettype == true) {


                mbetgridnaptextView.setText("[#" + (position + 1) + "] NAP :" + Bets.get(position).getBettypenap());
                mbetgridlinestextView.setText("LINES :" + Bets.get(position).getNooflines());
                mbetgridatklintextView.setText("STK/LN :₦" + Bets.get(position).getStakeperline());
                mbetgridamounttextView.setText("AMT :₦" + Bets.get(position).getAmount());
                mbetgridbet1textView.setText("BET1 :" + Bets.get(position).getBet1());
                mbetgridbet2textView.setText("BET2 :" + Bets.get(position).getBet2());

                if (!Bets.get(position).getBettypenap().equals("AGS") && !Bets.get(position).getBettypenap().equals("AG"))
                    mbetgridbet2textView.setVisibility(View.GONE);

                mbetstatus.setText(Bets.get(position).getStatusmessage());

                if(Bets.get(position).getStatusid() == 2)
                {
                    mbetstatus.setTextColor(Color.GREEN);
                }
                else if(Bets.get(position).getStatusid() == 3)
                {
                    mbetstatus.setTextColor(Color.RED);
                }
            }

            else {


                mbetgridnaptextView.setText("[#" + (position + 1) + "] NAP :" + BetSlip.get(position).getNap());
                mbetgridlinestextView.setText("LINES :" + BetSlip.get(position).getLines());
                mbetgridatklintextView.setText("STK/LN :₦" + BetSlip.get(position).getStakeperline());
                mbetgridamounttextView.setText("AMT :₦" + BetSlip.get(position).getAmount());
                mbetgridbet1textView.setText("BET1 :" + BetSlip.get(position).getBet1Text());
                mbetgridbet2textView.setText("BET2 :" + BetSlip.get(position).getBet2Text());

                if (!BetSlip.get(position).getNap().equals("AGS") && !BetSlip.get(position).getNap().equals("AG"))
                    mbetgridbet2textView.setVisibility(View.GONE);

                mbetstatuslayout.setVisibility(View.GONE);
            }
        }
        catch (Exception ex)
        {

        }
        return grid;
    }
}
