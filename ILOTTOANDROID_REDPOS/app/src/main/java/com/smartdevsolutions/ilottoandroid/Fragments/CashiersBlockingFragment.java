package com.smartdevsolutions.ilottoandroid.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartdevsolutions.ilottoandroid.ApiResource.DeviceResource;
import com.smartdevsolutions.ilottoandroid.R;
import com.smartdevsolutions.ilottoandroid.Utility.CallService;
import com.smartdevsolutions.ilottoandroid.Utility.Customer;
import com.smartdevsolutions.ilottoandroid.Utility.CustomerStatusUpdateInput;
import com.smartdevsolutions.ilottoandroid.Utility.MyApplication;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by teecodez on 12/6/2017.
 */

public class CashiersBlockingFragment extends Fragment {


    Button mBlockButton;
    Button mUnBlockButton;
    Button mUnReloadButton;

    ProgressDialog progress;

    Spinner mUnblockcashierdrpdown;
    Spinner mblockcashierdrpdown;

    List<Customer> Cashiers;
    List<Customer> ActiveCashiers;
    List<Customer> InActiveCashiers;
    DeviceResource device;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.cashier_blocking_fragment, container, false);
        Cashiers = new ArrayList<Customer>();


        mBlockButton = (Button)  rootView.findViewById(R.id.blockbutton);
        mUnBlockButton = (Button)  rootView.findViewById(R.id.unblockbutton);
        mUnReloadButton = (Button)  rootView.findViewById(R.id.mblockreloadbutton);

        mblockcashierdrpdown = (Spinner) rootView.findViewById(R.id.mblockcashier_spinner) ;
        mUnblockcashierdrpdown = (Spinner) rootView.findViewById(R.id.munblockcashier_spinner) ;

        device = ((MyApplication) getActivity().getApplication()).getCurrentTerminal();

        List<Customer> loadedcashiers = ((MyApplication) getActivity().getApplication()).getCashiersInShop();

        try {
            loadCashiers(loadedcashiers);
        }
        catch (Exception ex)
        {
            getActivity().setTitle(getResources().getString(R.string.home));
            Fragment menufragment = new menuFragment();
            FragmentManager menufragmentManager = getFragmentManager();
            menufragmentManager.beginTransaction().replace(R.id.content_frame, menufragment).commit();
            Toast.makeText(getActivity(), "Please Wait Background Service trying to load Cashiers", Toast.LENGTH_SHORT).show();
        }



        mBlockButton.setOnClickListener(GoBlock );
        mUnBlockButton.setOnClickListener(GoUnBlock );
        mUnReloadButton.setOnClickListener(GoReload);







        return rootView;
    }

    private Button.OnClickListener GoBlock = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            if (device.getCustomertype() == 3) {
//
//                try {
//                    int selectedID = mblockcashierdrpdown.getSelectedItemPosition();
//                    int cid = ActiveCashiers.get(selectedID).getId();
//                    int sid = device.getShopid();
//                    showProgress("CASHIER BLOCKING", "BLOCKING " + ActiveCashiers.get(selectedID).getUsername() );
//                    CashierBlockingTask tsk = new CashierBlockingTask(cid, sid, true);
//                    tsk.execute();
//
//                } catch (Exception e) {
//                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
        }
    };

    private Button.OnClickListener GoUnBlock = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            if (device.getCustomertype() == 3) {
//
//                try {
//                    int selectedID = mUnblockcashierdrpdown.getSelectedItemPosition();
//                    int cid = InActiveCashiers.get(selectedID).getId();
//                    int sid = device.getShopid();
//                    showProgress("CASHIER UNBLOCKING", "UNBLOCKING " + InActiveCashiers.get(selectedID).getUsername() );
//                    CashierBlockingTask tsk = new CashierBlockingTask(cid, sid, false);
//                    tsk.execute();
//
//                } catch (Exception e) {
//                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
        }
    };

    private Button.OnClickListener GoReload = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            if (device.getCustomertype() == 3) {
//
//                try {
//                    int sid = device.getShopid();
//                    showProgress("RELOADING ", "FETCHING RECORDS " );
//                    LoadCustomerTask tsk = new LoadCustomerTask(sid);
//                    tsk.execute();
//
//                } catch (Exception e) {
//                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
        }
    };


    public  void loadCashiers(List<Customer> customers)
    {
        ActiveCashiers = new ArrayList<Customer>();
        InActiveCashiers = new ArrayList<Customer>();

            for (Customer c: customers) {
                if(c.getIsactive() == 1 && c.getCustomertype() == 2)
                ActiveCashiers.add(c);
                else if(c.getIsactive() == 2 && c.getCustomertype() == 2)
                InActiveCashiers.add(c);
            }

            List<String> clist = new ArrayList<>();

            clist.addAll(fillArrayAdapter(ActiveCashiers));

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_dropdown_item, clist);
            mblockcashierdrpdown.setAdapter(adapter);

            List<String> clist2 = new ArrayList<>();

            clist2.addAll(fillArrayAdapter(InActiveCashiers));

            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_dropdown_item, clist2);
            mUnblockcashierdrpdown.setAdapter(adapter2);



    }

    private ArrayList<String> fillArrayAdapter(List<Customer> customers) {
        ArrayList<String> retlist = new ArrayList<String>();
        for (Customer c: customers) {

            String cas = c.getUsername();
            retlist.add(cas);
        }
        return  retlist;
    }

    public void showProgress(  String Title, String Message) {
        progress = ProgressDialog.show(getActivity(), Title,
                Message, true);
    }


    public class CashierBlockingTask extends AsyncTask<Void, Void, Boolean> {

        int custID;

        int shopID;
        String url;
        boolean isblock;
        String debugmessage="";


        private Customer custOutput ;

        CashierBlockingTask( int CID, int SID, boolean isblock ) {
            this.custID = CID;
            this.shopID = SID;
            this.isblock =  isblock;
            this.url = (getString(R.string.service_url) +getString(R.string.service_CustomerStatusUpdate_controller));

        }

        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            try {




                CustomerStatusUpdateInput SI = new CustomerStatusUpdateInput();
                SI.setCustId(custID);
                //SI.setPostId(device.getId());
                SI.setStatus(isblock == true? 2 : 1);

                Gson gson = new Gson();
                Type type = new TypeToken<CustomerStatusUpdateInput>(){}.getType();
                String json = gson.toJson(SI, type);
                CallService mysevice = new CallService(url, json);
                String response =  mysevice.invokePostService();
                if(response != ""   )
                {
                    type = new TypeToken<Customer>(){}.getType();
                    custOutput = gson.fromJson(response, type);
                    if(custOutput!= null) {
                        if(custOutput.getId()!=-100) {
                            debugmessage = "";
                            return true;
                        }
                        else
                        {
                            debugmessage = custOutput.getErrormessage();
                            return false;
                        }
                    }
                    else {
                        debugmessage = "null received";
                        return false;
                    }
                }
                else
                {
                    debugmessage += "Poor Network);  byteList.add(Please Check Your Network and Try Again!!!";
                    return false ;
                }

            } catch (Exception e) {
                debugmessage = e.getMessage();
                return false;
            }


        }

        @Override
        protected void onPostExecute(final Boolean success) {

            try {
                progress.dismiss();
                if (success) {

                    Toast.makeText(getActivity(), "Block?Unblock Activity Successful " + debugmessage, Toast.LENGTH_SHORT).show();

                } else {
                    custOutput = null;
                    Toast.makeText(getActivity(), " UnAble To Perform Operation " + debugmessage, Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                custOutput = null;
                Toast.makeText(getActivity(), "UnAble To Perform Operation " + debugmessage, Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "post serv err: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {

            progress.dismiss();
        }




    }

    public class LoadCustomerTask extends AsyncTask<Void, Void, Boolean> {

        private int shopID;
        String debugmessage ="";
        private List<Customer> cashiers;
         public LoadCustomerTask(int shopID ) {

            this.shopID = shopID;
            cashiers=new ArrayList<Customer>();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                Gson gson = new Gson();

                String url = (getString(R.string.service_url) +getString(R.string.service_CustomersInShop_controller) + shopID);
                CallService mysevice = new CallService(url, "");
                String response =  mysevice.invokeGetService();
                if(response != ""   )
                {
                    Type type = new TypeToken<List<Customer>>(){}.getType();
                    cashiers = gson.fromJson(response, type);

                    return  true ;
                }
                else
                {
                    debugmessage += "Poor Network, Please Check Your Network and Try Again!!!";
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

                ((MyApplication) getActivity().getApplication()).setCashiersInShop(cashiers);
                loadCashiers(cashiers);
                Toast.makeText(getActivity(), "Cashiers Reloaded Successfully", Toast.LENGTH_SHORT );

            } else {
                Toast.makeText(getActivity(), debugmessage, Toast.LENGTH_SHORT).show();

            }
        }


        @Override
        protected void onCancelled() {
            progress.dismiss();

        }
    }

}
