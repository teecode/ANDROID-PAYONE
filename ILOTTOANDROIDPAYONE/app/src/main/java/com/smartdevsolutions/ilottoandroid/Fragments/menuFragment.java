package com.smartdevsolutions.ilottoandroid.Fragments;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartdevsolutions.ilottoandroid.HomeActivity;
import com.smartdevsolutions.ilottoandroid.LoginActivity;
import com.smartdevsolutions.ilottoandroid.R;
import com.smartdevsolutions.ilottoandroid.UserInterface.CustomGrid;
import com.smartdevsolutions.ilottoandroid.Utility.Customer;
import com.smartdevsolutions.ilottoandroid.Utility.MyApplication;

import java.lang.reflect.Type;

/**
 * Created by BINARYCODES on 5/13/2017.
 */

public class menuFragment extends Fragment {

    GridView grid;
    String[] web ;
    int[] managerimageId = {
            R.drawable.play,
            R.drawable.winningnumber,
            R.drawable.checkticket,
            R.drawable.account,
            R.drawable.profile,
            R.drawable.registeredslips,
            R.drawable.summarysheet,
            R.drawable.cashierblocking,
            R.drawable.creditmanagement,
            R.drawable.logout,
    };

    int[] cashierimageId = {
            R.drawable.play,
            R.drawable.winningnumber,
            R.drawable.checkticket,
            R.drawable.account,
            R.drawable.profile,
            R.drawable.registeredslips,
            R.drawable.summarysheet,
            R.drawable.logout,
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_fragment, container, false);
        Gson gson = new Gson();
        Type type = new TypeToken<Customer>(){}.getType();
        Customer cust = ((MyApplication) getActivity().getApplication()).getCurrentCustomer();
        web = (cust.getCustomertype() == 3)?  getResources().getStringArray(R.array.managermenu) : getResources().getStringArray(R.array.cashiermenu);
        int[] menuimages = (cust.getCustomertype() == 3)? managerimageId : cashierimageId;
        CustomGrid adapter = new CustomGrid(getActivity(), web, menuimages );
        grid=(GridView)rootView.findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(), "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
                String Clicked = web[+ position];
                if(Clicked.equals(getResources().getString(R.string.play)))
                {
                    getActivity().setTitle(getResources().getString(R.string.play));
                    Fragment playfragment = new DailyGameFragment();
                    FragmentManager playfragmentManager = getFragmentManager();
                    playfragmentManager.beginTransaction().replace(R.id.content_frame, playfragment).addToBackStack( "tag" ).commit();
                }
                else if(Clicked.equals(getResources().getString(R.string.winningnumber)))
                {
                    getActivity().setTitle(getResources().getString(R.string.winningnumber));
                    Fragment winningfragment = new winningFragment();
                    FragmentManager winningfragmentManager = getFragmentManager();
                    winningfragmentManager.beginTransaction().replace(R.id.content_frame, winningfragment).addToBackStack( "tag" ).commit();
                }
                else if(Clicked.equals(getResources().getString(R.string.checkticket)))
                {
                    getActivity().setTitle(getResources().getString(R.string.checkticket));
                    Fragment CheckTicketFragment = new CheckTicketFragment();
                    FragmentManager CheckTicketfragmentManager = getFragmentManager();
                    CheckTicketfragmentManager.beginTransaction().replace(R.id.content_frame, CheckTicketFragment).addToBackStack( "tag" ).commit();
                }
                else if(Clicked.equals(getResources().getString(R.string.account)))
                {
                    getActivity().setTitle(getResources().getString(R.string.account));
                    Fragment AccountFragment = new AccountSummaryFragment();
                    FragmentManager AccountfragmentManager = getFragmentManager();
                    AccountfragmentManager.beginTransaction().replace(R.id.content_frame, AccountFragment).addToBackStack( "tag" ).commit();
                }
                else if(Clicked.equals(getResources().getString(R.string.profile)))
                {
                    getActivity().setTitle(getResources().getString(R.string.profile));
                    Fragment ProfileFragment = new ProfileFragment();
                    FragmentManager ProfilefragmentManager = getFragmentManager();
                    ProfilefragmentManager.beginTransaction().replace(R.id.content_frame, ProfileFragment).addToBackStack( "tag" ).commit();
                }
                else if(Clicked.equals(getResources().getString(R.string.registeredslips)))
                {
                    getActivity().setTitle(getResources().getString(R.string.registeredslips));
                    Fragment RegisteredSlipFragment = new RegisterSlipFragment();
                    FragmentManager RegisteredSlipfragmentManager = getFragmentManager();
                    RegisteredSlipfragmentManager.beginTransaction().replace(R.id.content_frame, RegisteredSlipFragment).addToBackStack( "tag" ).commit();
                }
                else if(Clicked.equals(getResources().getString(R.string.cashierblocking)))
                {
                    getActivity().setTitle(getResources().getString(R.string.cashierblocking));
                    Fragment CashierBlockingFragment = new CashiersBlockingFragment();
                    FragmentManager CashierBlockingfragmentManager = getFragmentManager();
                    CashierBlockingfragmentManager.beginTransaction().replace(R.id.content_frame, CashierBlockingFragment).addToBackStack( "tag" ).commit();
                }
                else if(Clicked.equals(getResources().getString(R.string.creditmanagement)))
                {
                    getActivity().setTitle(getResources().getString(R.string.creditmanagement));
                    Fragment CreditManagementFragment = new FundTransferFragment();
                    FragmentManager CreditManagementfragmentManager = getFragmentManager();
                    CreditManagementfragmentManager.beginTransaction().replace(R.id.content_frame, CreditManagementFragment).addToBackStack( "tag" ).commit();
                }
                else if(Clicked.equals(getResources().getString(R.string.logout)))
                {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }


            }
        });



        return rootView;

    }









}
