package com.smartdevsolutions.ilottoandroid.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartdevsolutions.ilottoandroid.R;
import com.smartdevsolutions.ilottoandroid.Utility.BetInput;
import com.smartdevsolutions.ilottoandroid.Utility.BetType;
import com.smartdevsolutions.ilottoandroid.Utility.Customer;
import com.smartdevsolutions.ilottoandroid.Utility.DailyGame;
import com.smartdevsolutions.ilottoandroid.Utility.MyApplication;
import com.smartdevsolutions.ilottoandroid.Utility.Ticket;
import com.smartdevsolutions.ilottoandroid.Utility.TicketInput;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by T360-INNOVATIVZ on 22/05/2017.
 */

public class BetTypeFragment extends Fragment {

    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;
    ListView lv;
    Gson gson ;
    DailyGame game;
    List<BetType> betTypes;
    private View mbettypeframe;
    private TextView mgamenameTextview;
    //private menuFragment.FetGamesTask fetchTask = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bet_type_fragment, container, false);
        initialiseComponents(rootView);
        List<String> btypes = fillArrayAdapter();
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, btypes );
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new BetTypeFragment.BetTypelistselectedListener());



        getActivity().setTitle("BETTYPES");
        return rootView;

    }

    private ArrayList<String> fillArrayAdapter() {
        ArrayList<String> retlist = new ArrayList<String>();
        for (BetType bet: betTypes) {

            String betname = bet.getCode();
            retlist.add(betname);
        }
        return  retlist;
    }



    private void initialiseComponents(View rootView)
    {
        lv = (ListView) rootView.findViewById(R.id.bettypelist);
        gson = new Gson();
        mbettypeframe = rootView.findViewById(R.id.bet_type_frame);
        betTypes = new ArrayList<BetType>();
        mgamenameTextview = (TextView) rootView.findViewById(R.id.bet_type_game_tv);
        TicketInput myticket =   ((MyApplication) getActivity().getApplication()).getCurrentTicket();

        mgamenameTextview.setText(myticket.getGamename());
        //Bundle bundle = this.getArguments();

        DailyGame sgame =   ((MyApplication) getActivity().getApplication()).getSelectedGame();
        if (sgame != null) {
//            String selectedgame = bundle.getString("selectedgame");
//            Type type = new TypeToken<DailyGame>(){}.getType();
//            DailyGame sgame = gson.fromJson(selectedgame, type);
            game = sgame;
            betTypes = sgame.getBettypes();
        }

        //Customer customer =   ((MyApplication) getActivity().getApplication()).getCurrentCustomer();
    }

    public class BetTypelistselectedListener implements AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {

            BetType bettype = betTypes.get(position);
            InitilialiseBetInput(bettype);
            gson = new Gson();
            Fragment fragment = new PlaceBetFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("bet_type_fragment").commit();
        }

        private void InitilialiseBetInput(BetType bettype)
        {
            BetInput mybet =   ((MyApplication) getActivity().getApplication()).getCurrentBet();
            TicketInput myticket = ((MyApplication) getActivity().getApplication()).getCurrentTicket();
            Customer customer =   ((MyApplication) getActivity().getApplication()).getCurrentCustomer();
            if(mybet== null || (mybet.getNap() != bettype.getNAP()))
            {
                mybet = new BetInput();
                mybet.setGamename(game.getGamename());
                mybet.setBetname(bettype.getCode());
                mybet.setGamecode(game.getGamecode());
                mybet.setGametype(bettype.getGametype());
                mybet.setMax_stake(bettype.getMaxstake());
                mybet.setMaxball(bettype.getMaxnoofballs());
                mybet.setMin_no_of_balls(bettype.getMinnoofballs());
                mybet.setMin_stake(bettype.getMinstake());
                mybet.setWinfactor(bettype.getWinfactor());
                mybet.setMinball(bettype.getMinnoofballs());
                mybet.setNap(bettype.getNAP());
                mybet.setBet1(new ArrayList<String>());
                mybet.setBet2(new ArrayList<String>());
            }
            ((MyApplication) getActivity().getApplication()).setCurrentBet(mybet);
        }

    }
}


