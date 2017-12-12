package com.smartdevsolutions.ilottoandroid.Fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nbbse.mobiprint3.Printer;
import com.smartdevsolutions.ilottoandroid.R;
import com.smartdevsolutions.ilottoandroid.UserInterface.winningNumberGridDisplay;
import com.smartdevsolutions.ilottoandroid.Utility.CallService;
import com.smartdevsolutions.ilottoandroid.Utility.Customer;
import com.smartdevsolutions.ilottoandroid.Utility.CustomerPasswordUpdateInput;
import com.smartdevsolutions.ilottoandroid.Utility.MyApplication;
import com.smartdevsolutions.ilottoandroid.Utility.WinningNumber;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by teecodez on 11/25/2017.
 */

public class ProfileFragment extends Fragment {
    TextView mLastname;
    TextView mFirstname;
    TextView mTerminalID;
    EditText mOldpasstextview;
    EditText mNewpasstextview;
    EditText mrenewpasstextview;
    Button mChangeButton;
    Button mCancelButton;
    ProgressDialog progress;
    Customer customercurr;

    ProfileFragment.ChangePasswordTask ptask;




    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);

        mLastname = (TextView) rootView.findViewById(R.id.profilelastname);
        mTerminalID = (TextView) rootView.findViewById(R.id.profiletermID);
        mFirstname = (TextView) rootView.findViewById(R.id.profilefirstname);
        mOldpasstextview = (EditText) rootView.findViewById(R.id.profileoldpassword);
        mNewpasstextview = (EditText) rootView.findViewById(R.id.profilenewpassword);
        mrenewpasstextview = (EditText) rootView.findViewById(R.id.profilerepassword);
        mChangeButton = (Button)  rootView.findViewById(R.id.profilechangebutton);
        mCancelButton = (Button)  rootView.findViewById(R.id.profilereturnbutton);

        customercurr =   ((MyApplication) getActivity().getApplication()).getCurrentCustomer();
        mFirstname.setText(customercurr.getFirstname());
        mLastname.setText(customercurr.getLastname());
        mTerminalID.setText(customercurr.getUsername());


        mChangeButton.setOnClickListener(GoClick );
        mCancelButton.setOnClickListener(ReturnHome );


        return rootView;
    }

    private Button.OnClickListener GoClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(!mOldpasstextview.getText().toString().equals("") && !mNewpasstextview.getText().toString().equals("") && !mrenewpasstextview.getText().toString().equals("") && mNewpasstextview.getText().toString().equals(mrenewpasstextview.getText().toString()))
            {
                showProgress("Changing Password", "Please Wait ...");

                String oldpass  = mOldpasstextview.getText().toString();
                String newpass  = mNewpasstextview.getText().toString();
                CustomerPasswordUpdateInput cpui = new CustomerPasswordUpdateInput();
                cpui.setCustID(customercurr.getId());
                cpui.setNewpass(newpass);
                cpui.setOldpass(oldpass);
                cpui.setPostingID(customercurr.getId());

                ptask = new ProfileFragment.ChangePasswordTask(cpui);
                ptask.execute();

            }
            else
            {
                Toast.makeText(getActivity() ,  "Please Fill Input Fields", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Button.OnClickListener ReturnHome = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            getActivity().setTitle(getResources().getString(R.string.home));
            Fragment HomeFragment = new menuFragment();
            FragmentManager HomefragmentManager = getFragmentManager();
            HomefragmentManager.beginTransaction().replace(R.id.content_frame, HomeFragment).commit();
        }
    };

    public void showProgress(  String Title, String Message) {
        progress = ProgressDialog.show(getActivity(), Title,
                Message, true);
    }

    public class ChangePasswordTask extends AsyncTask<Void, Void, Boolean> {


        String url;
        CustomerPasswordUpdateInput CPUI;
        Customer cust;
        String debugmessage="";



        ChangePasswordTask(CustomerPasswordUpdateInput cPUI) {
            this.url = (getString(R.string.service_url) + getString(R.string.service_CustomerPasswordUpdate_controller));
            this.CPUI = cPUI;
        }

        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            try {

                Gson gson = new Gson();
                Type type ;
                type = new TypeToken<CustomerPasswordUpdateInput>(){}.getType();
                String json = gson.toJson(CPUI, type);
                CallService mysevice = new CallService(url, json);
                String response =  mysevice.invokePostService();
                if(response != ""   )
                {
                    type = new TypeToken<Customer>(){}.getType();
                    cust = gson.fromJson(response, type);
                    if(cust.getId()!=-100) {
                        debugmessage = "  ";
                        return true;
                    }
                    else {
                        debugmessage = cust.getErrormessage();
                        return false;
                    }
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
                Toast.makeText(getActivity() ,  "Password Changed Successfully", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getActivity() ,  debugmessage, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {

            progress.dismiss();
        }




    }
}
