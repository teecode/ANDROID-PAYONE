package com.smartdevsolutions.ilottoandroid.Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartdevsolutions.ilottoandroid.R;
import com.smartdevsolutions.ilottoandroid.Utility.CallService;
import com.smartdevsolutions.ilottoandroid.Utility.Customer;
import com.smartdevsolutions.ilottoandroid.Utility.MyApplication;
import com.smartdevsolutions.ilottoandroid.Utility.ShopCustomerCreditInput;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by teecodez on 12/6/2017.
 */

public class FundTransferFragment extends Fragment {

    Button mfundButton;
    Button mReloadButton;
    EditText mamountEditText;
    ProgressDialog progress;
    Spinner mcashiersdropdown;
    List<Customer> Cashiers;
    Customer cust;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fund_transfer_fragment, container, false);
        Cashiers = new ArrayList<Customer>();



        mcashiersdropdown = (Spinner) rootView.findViewById(R.id.mfundcashiers_spinner) ;
        mfundButton = (Button)  rootView.findViewById(R.id.mfundbutton);
        mReloadButton = (Button)  rootView.findViewById(R.id.mfundreloadbutton);
        mamountEditText = (EditText)  rootView.findViewById(R.id.mfundamountEditText);
        cust = ((MyApplication) getActivity().getApplication()).getCurrentCustomer();

        int sid = cust.getShopid();
        showProgress("LOADING CASHIERS", "... "  );
        LoadCustomerTask tsk = new LoadCustomerTask(sid);
        tsk.execute();

        mfundButton.setOnClickListener(GoFund );
        mReloadButton.setOnClickListener(GoReload );
        return rootView;
    }

    private Button.OnClickListener GoFund = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (cust.getCustomertype() == 3) {

                try {
                    int selectedID = mcashiersdropdown.getSelectedItemPosition();
                    int cid = Cashiers.get(selectedID).getId();
                    int sid = cust.getShopid();
                    double amount = Double.parseDouble(mamountEditText.getText().toString());
                    showProgress("FUNDING CASHIER", "Cash funding ... " + Cashiers.get(selectedID).getUsername() );
                    CashierFundingTask tsk = new CashierFundingTask(cid, sid, amount);
                    tsk.execute();

                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private Button.OnClickListener GoReload = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (cust.getCustomertype() == 3) {

                try {
                    int sid = cust.getShopid();
                    showProgress("RELOADING ", "FETCHING RECORDS " );
                    LoadFundCustomerTask tsk = new LoadFundCustomerTask(sid);
                    tsk.execute();

                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    public  void loadCashiers(List<Customer> customers)
    {


        List<String> clist = new ArrayList<>();

        clist.addAll(fillArrayAdapter(customers));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, clist);
        mcashiersdropdown.setAdapter(adapter);



    }

    private ArrayList<String> fillArrayAdapter(List<Customer> customers) {
        ArrayList<String> retlist = new ArrayList<String>();
        for (Customer c: customers) {

            String cas = c.getUsername() + " : " +c.getWalletbalance();
            retlist.add(cas);
        }
        return  retlist;
    }

    public void showProgress(  String Title, String Message) {
        progress = ProgressDialog.show(getActivity(), Title,
                Message, true);
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
                Cashiers = cashiers;
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

    public class CashierFundingTask extends AsyncTask<Void, Void, Boolean> {

        int custID;
        double amount;
        int shopID;
        String url;

        String debugmessage="";


        private Customer custOutput ;

        CashierFundingTask( int CID, int SID, double amount ) {
            this.custID = CID;
            this.shopID = SID;
            this.amount = amount;
            this.url = (getString(R.string.service_url) +getString(R.string.service_CustomerCrediting_controller));

        }

        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            try {

                ShopCustomerCreditInput SI = new ShopCustomerCreditInput();
                SI.setCustomerID(custID);
                SI.setCustomerposting(cust.getId());
                SI.setAmount(amount);
                SI.setShopID(cust.getShopid());
                SI.setTransactiontype("WSC");

                Gson gson = new Gson();
                Type type = new TypeToken<ShopCustomerCreditInput>(){}.getType();
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

                    Toast.makeText(getActivity(), "Credited Successfully, new balance = " + custOutput.getWalletbalance(), Toast.LENGTH_SHORT).show();
                    mamountEditText.setText("");

                } else {
                    custOutput = null;
                    Toast.makeText(getActivity(), "Error Crediting Cashier " + debugmessage, Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                custOutput = null;
                Toast.makeText(getActivity(), "Error Crediting Cashier " + debugmessage, Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "post serv err: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {

            progress.dismiss();
        }




    }

    private class LoadFundCustomerTask extends AsyncTask<Void, Void, Boolean> {

        private int shopID;
        String debugmessage ="";
        private List<Customer> cashiers;
        public LoadFundCustomerTask(int shopID ) {

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
