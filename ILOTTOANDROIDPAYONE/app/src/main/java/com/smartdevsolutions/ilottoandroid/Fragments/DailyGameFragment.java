package com.smartdevsolutions.ilottoandroid.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartdevsolutions.ilottoandroid.R;
import com.smartdevsolutions.ilottoandroid.Utility.BetInput;
import com.smartdevsolutions.ilottoandroid.Utility.CallService;
import com.smartdevsolutions.ilottoandroid.Utility.Customer;
import com.smartdevsolutions.ilottoandroid.Utility.DailyGame;
import com.smartdevsolutions.ilottoandroid.Utility.MyApplication;
import com.smartdevsolutions.ilottoandroid.Utility.TicketInput;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by T360-INNOVATIVZ on 24/05/2017.
 */

public class DailyGameFragment extends Fragment {

    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;
    ListView lv;
    Gson gson ;
    List<DailyGame> dailygames;
    private View mdailygameframe;
    private DailyGameFragment.FetGamesTask fetchTask = null;
    ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.daily_game_fragment, container, false);
        initialiseComponents(rootView);
        lv.setOnItemClickListener(new DailyGameFragment.listselectedListener());
        showProgress("FETCHING GAMES", "PLEASE WAIT...");
        fetchTask = new DailyGameFragment.FetGamesTask();
        fetchTask.execute((Void) null);
        getActivity().setTitle("GAMES");
        return rootView;

    }



    private void initialiseComponents(View rootView)
    {
        lv = (ListView) rootView.findViewById(R.id.dailygamelist);
        mdailygameframe = rootView.findViewById(R.id.daily_game_frame);
        dailygames = new ArrayList<DailyGame>();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(  String Title, String Message) {
        progress = ProgressDialog.show(getActivity(), Title,
                Message, true);
    }

    private ArrayList<String> fillArrayAdapter() {
        ArrayList<String> retlist = new ArrayList<String>();
        for (DailyGame dailygame: dailygames) {
            DateFormat dateFormat = new SimpleDateFormat("h:mm a");
            String gamename = dailygame.getGamename() + "  [ " + dateFormat.format(dailygame.getenddatetimeInstance()) +"]";
            retlist.add(gamename);
        }
        return  retlist;
    }

    private ArrayList<DailyGame> Getdailygames(){

        ArrayList<DailyGame> returnlist = new ArrayList<DailyGame>();
        String url = (getString(R.string.service_url) +getString(R.string.service_opendailygame_controller));
        CallService mysevice = new CallService(url,"");
        //TypeToken type;
        String response =  mysevice.invokeGetService();
        if(response != ""   )
        {
            Type type = new TypeToken<ArrayList<DailyGame>>(){}.getType();
            gson = new Gson();
            returnlist = gson.fromJson(response, type);

        }

        return returnlist;
    }


    public class FetGamesTask extends AsyncTask<Void, Void, Boolean> {


        String url;
        String debugmessage;

        private Customer cust ;

        FetGamesTask() {
            this.url = (getString(R.string.service_url) +getString(R.string.service_opendailygame_controller));

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            try {
                List<DailyGame> savedgames  = ((MyApplication) getActivity().getApplication()).getFetchedGames();
                if(savedgames!= null &&  savedgames.isEmpty() == false)
                {
                    dailygames = GetActiveGames(savedgames);
                    return true;
                }
                else {

                    List<DailyGame> returnlist = new ArrayList<DailyGame>();
                    CallService mysevice = new CallService(url, "");
                    //TypeToken type;
                    String response = mysevice.invokeGetService();
                    if (response != "") {
                        Type type = new TypeToken<List<DailyGame>>() {
                        }.getType();
                        gson = new Gson();
                        returnlist = gson.fromJson(response, type);

                        dailygames = GetActiveGames(returnlist);
                        ((MyApplication) getActivity().getApplication()).setFetchedGames(returnlist);
                        return true;
                    }
                }


            } catch (Exception e) {
                debugmessage = e.getMessage();
                return false;
            }
            return  false   ;

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            try {
                progress.dismiss();
                if (success) {
                    ArrayList<String> menu = fillArrayAdapter();
                    if (menu.size() > 0) {
                        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, fillArrayAdapter());
                        lv.setAdapter(adapter);
                    } else {
                        String emptylist[] = {"No game Available"};
                        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, emptylist);
                        lv.setAdapter(adapter);
                        Toast.makeText(getActivity(), "No game Available", Toast.LENGTH_SHORT).show();
                    }

                    //finish();
                } else {
                    String emptylist[] = {debugmessage};
                    adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, emptylist);
                    lv.setAdapter(adapter);
                }
            }
            catch ( Exception e)
            {
                String emptylist[] = {e.getMessage(), "Please Reload DailyGames"};
                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, emptylist);
                lv.setAdapter(adapter);
            }
        }

        private List<DailyGame> GetActiveGames(List<DailyGame> dailygames) {
            List<DailyGame> returnlist = new ArrayList<DailyGame>();
            for (DailyGame game: dailygames )
            {

                if(game.isActive()== true && game.getenddatetimeInstance().after(new Date()) )
                    returnlist.add(game);
            }
            return returnlist;
        }

        @Override
        protected void onCancelled() {

        }
    }

    public class listselectedListener implements AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {

            try {

                DailyGame game = dailygames.get(position);
                TicketInput myticket = ((MyApplication) getActivity().getApplication()).getCurrentTicket();
                Customer customer = ((MyApplication) getActivity().getApplication()).getCurrentCustomer();
                if (myticket == null || (myticket.getGamename() != game.getGamename())) {
                    myticket = new TicketInput();
                    myticket.setGamename(game.getGamename());
                    myticket.setVersion(getString(R.string.service_version));
                    myticket.setCustomerID(customer.getId());
                    myticket.setBetslips(new ArrayList<BetInput>());
                }
                ((MyApplication) getActivity().getApplication()).setSelectedGame(game); //saves selcted game in the application context;
                ((MyApplication) getActivity().getApplication()).setCurrentTicket(myticket); // saves the ticket build up in the application context.


                gson = new Gson();
                Type type = new TypeToken<DailyGame>() {
                }.getType();
                String dailygamestring = gson.toJson(game, type);
                Fragment fragment = new BetTypeFragment();

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("game_fragment").commit();

            }
            catch (Exception Ex)
            {

            }
        }


    }
}
