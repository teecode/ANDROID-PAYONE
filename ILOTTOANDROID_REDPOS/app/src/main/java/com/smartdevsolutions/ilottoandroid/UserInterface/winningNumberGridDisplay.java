package com.smartdevsolutions.ilottoandroid.UserInterface;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartdevsolutions.ilottoandroid.ApiResource.DailyGameResource;
import com.smartdevsolutions.ilottoandroid.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by T360-INNOVATIVZ on 30/05/2017.
 */

public class winningNumberGridDisplay extends BaseAdapter {
    private Context mContext;
    private final List<DailyGameResource> WinningNumbers;

    public winningNumberGridDisplay(Context c, List<DailyGameResource> WN ) {
        mContext = c;
        this.WinningNumbers = WN;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return WinningNumbers.size();
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
            grid = inflater.inflate(R.layout.winning_number_grid_single, null);

        } else {
            grid = (View) convertView;
        }


        TextView mgamename = (TextView) grid.findViewById(R.id.winning_game_name_tv);
        TextView mwin1 = (TextView) grid.findViewById(R.id.winning_number_1_tv);
        TextView mwin2 = (TextView) grid.findViewById(R.id.winning_number_2_tv);
        TextView mwin3 = (TextView) grid.findViewById(R.id.winning_number_3_tv);
        TextView mwin4 = (TextView) grid.findViewById(R.id.winning_number_4_tv);
        TextView mwin5 = (TextView) grid.findViewById(R.id.winning_number_5_tv);
        TextView mmac1 = (TextView) grid.findViewById(R.id.machine_number_1_tv);
        TextView mmac2 = (TextView) grid.findViewById(R.id.machine_number_2_tv);
        TextView mmac3 = (TextView) grid.findViewById(R.id.machine_number_3_tv);
        TextView mmac4 = (TextView) grid.findViewById(R.id.machine_number_4_tv);
        TextView mmac5 = (TextView) grid.findViewById(R.id.machine_number_5_tv);
        LinearLayout mmachinenumberframe = (LinearLayout) grid.findViewById(R.id.winning_machineframe) ;

        Date gamedate =WinningNumbers.get(position).getDate();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String date = format.format(gamedate);
        mgamename.setText( ""+ WinningNumbers.get(position).getGameName() +" - ["+ date + "]" );
        mwin1.setText(""+ WinningNumbers.get(position).getResult().getWinningBall1());
        mwin2.setText(""+ WinningNumbers.get(position).getResult().getWinningBall2());
        mwin3.setText(""+ WinningNumbers.get(position).getResult().getWinningBall3());
        mwin4.setText(""+ WinningNumbers.get(position).getResult().getWinningBall4());
        mwin5.setText(""+ WinningNumbers.get(position).getResult().getWinningBall5());
        if(WinningNumbers.get(position).isGhanaGame()== true)
            mmachinenumberframe.setVisibility(View.INVISIBLE);
        else
        {
            mmachinenumberframe.setVisibility(View.VISIBLE);
            mmac1.setText(""+ WinningNumbers.get(position).getResult().getMachineBall1() );
            mmac2.setText(""+ WinningNumbers.get(position).getResult().getMachineBall2() );
            mmac3.setText(""+ WinningNumbers.get(position).getResult().getMachineBall3() );
            mmac4.setText(""+ WinningNumbers.get(position).getResult().getMachineBall4() );
            mmac5.setText(""+ WinningNumbers.get(position).getResult().getMachineBall5() );
        }

        return grid;
    }
}