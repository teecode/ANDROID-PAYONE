package com.smartdevsolutions.ilottoandroid.UserInterface;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartdevsolutions.ilottoandroid.ApiResource.AddBetSlipResource;
import com.smartdevsolutions.ilottoandroid.ApiResource.BetSlipResource;
import com.smartdevsolutions.ilottoandroid.ApiResource.BetTypeResource;
import com.smartdevsolutions.ilottoandroid.R;

import java.util.List;

/**
 * Created by T360-INNOVATIVZ on 29/05/2017.
 */

public class BetGridDisplay extends BaseAdapter {
    private Context mContext;
    private final List<AddBetSlipResource> BetSlip ;
    private final List<BetSlipResource> Bets;
    private final boolean istickettype ;
    private final List<BetTypeResource> betTypes;
    //false is BetInput true is Bet

    public BetGridDisplay(Context c, List<AddBetSlipResource> betslip , List<BetSlipResource> bet, List<BetTypeResource> betTypes) {

            this.mContext = c;
        this.betTypes = betTypes;
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

    public String getbetNap(int napId)
    {
        return "";
       // mContext.getA
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


                mbetgridnaptextView.setText("[#" + (position + 1) + "] NAP :" + Bets.get(position).getBetType().getName());
                mbetgridlinestextView.setText("LINES :" + Bets.get(position).getLines());
                mbetgridatklintextView.setText("STK/LN :₦" + Bets.get(position).getStakePerLine());
                mbetgridamounttextView.setText("AMT :₦" + Bets.get(position).getAmount());
                mbetgridbet1textView.setText("BET1 :" + Bets.get(position).getBet1String());
                mbetgridbet2textView.setText("BET2 :" + Bets.get(position).getBet2String());
                mbetstatus.setText(Bets.get(position).getStatus().getName());

                if (!Bets.get(position).getBetType().getName().equals("AGAINST") && !Bets.get(position).getBetType().getName().equals("AGAINSTSINGLE"))
                    mbetgridbet2textView.setVisibility(View.GONE);



                if(Bets.get(position).getStatus().getId() == 2)
                {
                    mbetstatus.setTextColor(Color.GREEN);
                }
                else if(Bets.get(position).getStatus().getId() == 3)
                {
                    mbetstatus.setTextColor(Color.RED);
                }
            }

            else {


                mbetgridnaptextView.setText("[#" + (position + 1) + "] NAP :" + BetSlip.get(position).getNap());
                mbetgridlinestextView.setText("LINES :" + BetSlip.get(position).getLines());
                mbetgridatklintextView.setText("STK/LN :₦" + BetSlip.get(position).getStakeperline());
                mbetgridamounttextView.setText("AMT :₦" + BetSlip.get(position).getAmount());
                mbetgridbet1textView.setText("BET1 :" + BetSlip.get(position).getBet1String());
                mbetgridbet2textView.setText("BET2 :" + BetSlip.get(position).getBet2String());

                if (!BetSlip.get(position).getNapName(betTypes).equals("AGS") && !BetSlip.get(position).getNapName(betTypes).equals("AG"))
                    mbetgridbet2textView.setVisibility(View.GONE);

                mbetstatuslayout.setVisibility(View.GONE);
            }
        }
        catch (Exception ex)
        {
            String x=  "I got here";
        }
        return grid;
    }
}
