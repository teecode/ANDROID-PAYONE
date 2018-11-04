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
import com.smartdevsolutions.ilottoandroid.ApiResource.AddBetSlipResource;
import com.smartdevsolutions.ilottoandroid.ApiResource.AddTicketResource;
import com.smartdevsolutions.ilottoandroid.ApiResource.BetTypeResource;
import com.smartdevsolutions.ilottoandroid.ApiResource.DailyGameResource;
import com.smartdevsolutions.ilottoandroid.ApiResource.DeviceResource;
import com.smartdevsolutions.ilottoandroid.R;
import com.smartdevsolutions.ilottoandroid.Utility.MyApplication;

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
    DailyGameResource game;
    List<BetTypeResource> betTypes;
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
        for (BetTypeResource bet: betTypes) {

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
        betTypes = new ArrayList<BetTypeResource>();
        mgamenameTextview = (TextView) rootView.findViewById(R.id.bet_type_game_tv);
        AddTicketResource myticket =   ((MyApplication) getActivity().getApplication()).getCurrentTicket();

        mgamenameTextview.setText(myticket.getGamename(((MyApplication) getActivity().getApplication()).getFetchedGames()));
        //Bundle bundle = this.getArguments();

        DailyGameResource sgame =   ((MyApplication) getActivity().getApplication()).getSelectedGame();
        if (sgame != null) {
//            String selectedgame = bundle.getString("selectedgame");
//            Type type = new TypeToken<DailyGame>(){}.getType();
//            DailyGame sgame = gson.fromJson(selectedgame, type);
            game = sgame;
            betTypes = sgame.getBetTypes();
        }

        //Customer customer =   ((MyApplication) getActivity().getApplication()).getCurrentTerminal();
    }

    public class BetTypelistselectedListener implements AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {

            BetTypeResource bettype = betTypes.get(position);
            InitilialiseBetInput(bettype);
            gson = new Gson();
            Fragment fragment = new PlaceBetFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("bet_type_fragment").commit();
        }

        private void InitilialiseBetInput(BetTypeResource bettype)
        {
            AddBetSlipResource mybet =   ((MyApplication) getActivity().getApplication()).getCurrentBet();
            AddTicketResource myticket = ((MyApplication) getActivity().getApplication()).getCurrentTicket();
            DeviceResource device =   ((MyApplication) getActivity().getApplication()).getCurrentTerminal();
            if(mybet== null || (mybet.getNap() != bettype.getBetTypeID()))
            {
                mybet = new AddBetSlipResource();
                mybet.setGame(game.getGameID());
//                mybet.setBetname(bettype.getCode());
//                mybet.setGamecode(game.getGamecode());
//                mybet.setGametype(bettype.getGametype());
//                mybet.setMax_stake(bettype.getMaxstake());
//                mybet.setMaxball(bettype.getMaxnoofballs());
//                mybet.setMin_no_of_balls(bettype.getMinnoofballs());
//                mybet.setMin_stake(bettype.getMinstake());
//                mybet.setWinfactor(bettype.getWinfactor());
//                mybet.setMinball(bettype.getMinnoofballs());
                mybet.setNap(bettype.getBetTypeID());
                mybet.setBet1(new ArrayList<Integer>());
                mybet.setBet2(new ArrayList<Integer>());
            }
            ((MyApplication) getActivity().getApplication()).setCurrentBet(mybet);
        }

    }
}


