package com.smartdevsolutions.ilottoandroid.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartdevsolutions.ilottoandroid.HomeActivity;
import com.smartdevsolutions.ilottoandroid.R;
import com.smartdevsolutions.ilottoandroid.TicketActivity;
import com.smartdevsolutions.ilottoandroid.Utility.BetInput;
import com.smartdevsolutions.ilottoandroid.Utility.Customer;
import com.smartdevsolutions.ilottoandroid.Utility.DailyGame;
import com.smartdevsolutions.ilottoandroid.Utility.InputFilterMinMax;
import com.smartdevsolutions.ilottoandroid.Utility.LottoCalculations;
import com.smartdevsolutions.ilottoandroid.Utility.MyApplication;
import com.smartdevsolutions.ilottoandroid.Utility.TicketInput;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by T360-INNOVATIVZ on 25/05/2017.
 */

public class PlaceBetFragment extends Fragment {

    TextView mGamename;
    TextView mBetType;
    TextView mBet1TextView;
    TextView mBet2TextView;
    TextView mstakeamountTextView;

    EditText mBet1EditText;
    EditText mBet2EditText;
    EditText mamountEditText;

    Button mbet1deletebutton;
    Button mbet2deletebutton;

    LinearLayout mBet1frame;
    LinearLayout mBet2frame;

    Button mAddtobetButton;
    Button mPlayButton;

    View FocusView;

    BetInput mybet;
    TicketInput myticket;
    Customer customer;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.place_bet_fragment, container, false);
        initialiseControls(rootView);
        InitialiseControlsWithValue();

        mBet1EditText.setOnEditorActionListener(mybet1EditorActionListener);
        mBet2EditText.setOnEditorActionListener(mybet2EditorActionListener);
        mamountEditText.setOnEditorActionListener(myamountEditorActionListener);

//        mBet1EditText.setOnFocusChangeListener(EditTextFocusChange);
//        mBet2EditText.setOnFocusChangeListener(EditTextFocusChange);
//        mamountEditText.setOnFocusChangeListener(EditTextFocusChange);


        if(mbet1deletebutton!= null) {
            mbet1deletebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deletebet1();
                }
            });
        }

        if(mbet2deletebutton!= null) {
            mbet2deletebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deletebet2();
                }
            });
        }

        if(mAddtobetButton!= null) {
            mAddtobetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (addtobet()) {
                        Fragment fragment = new BetTypeFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("place_bet").commit();
                    }

                }


            });
        }

        if(mPlayButton!= null) {
            mPlayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (addtobet()) {
                        Intent intent = new Intent(getActivity(), TicketActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }



        return rootView;
    }

    public void myOnKeyDown(int key_code){
        if(key_code == KeyEvent.KEYCODE_CALL) {
           if(mBet1EditText.hasFocus())
           {
               fillbet1();
           }
           else if(mBet2EditText.hasFocus())
            {
                fillbet2();
            }
           else if(mamountEditText.hasFocus())
           {
               String amountString = mamountEditText.getText().toString();
               if(checkamount(amountString)) {
                   mybet.setStakeperline(Double.parseDouble(amountString));
                   InputMethodManager inputManager =(InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                   inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                   FocusView = mAddtobetButton;
                   ReloadView();
               }
           }
           else
           {
               if (addtobet()) {
                   Fragment fragment = new BetTypeFragment();
                   FragmentManager fragmentManager = getFragmentManager();
                   fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("place_bet").commit();
               }
           }

        }

    }



    private boolean addtobet() {
        try {
            if (RevalidateBet()) {
                myticket.getBetslips().add(mybet);
                Toast.makeText(getActivity(), "Bet added to Bet Slip ", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), myticket.getBetslips().size() + "Bet(s) available in Bet Slip", Toast.LENGTH_SHORT).show();
                ((MyApplication) getActivity().getApplication()).setCurrentTicket(myticket);
                ((MyApplication) getActivity().getApplication()).setCurrentBet(null);
                return true;
            } else
                return false;
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(), "bet add err: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean RevalidateBet() {
        if(!mybet.getNap().equals("")  && mybet.getBet1().size()> 0 && mybet.getLines()>0 && mybet.getAmount() >= mybet.getMin_stake() && mybet.getAmount() <= mybet.getMax_stake())
            return true;
        return false;
    }


    private void deletebet1() {
        int index = mybet.getBet1().size();
        if(index > 0) {
            mybet.getBet1().remove(index - 1);

            ReloadView();
        }
        else {
            Toast.makeText(getActivity(), " Nothing to remove ", Toast.LENGTH_SHORT).show();
        }

        if(mybet.getBet1().size()< mybet.getMaxball())
            mBet1EditText.setEnabled(true);
        mBet1EditText.setFocusable(true);
        mBet1EditText.requestFocus();
    }

    private void deletebet2() {
        int index = mybet.getBet2().size();
        if(index > 0) {
            mybet.getBet2().remove(index - 1);

            ReloadView();
        }
        else {
            Toast.makeText(getActivity(), " Nothing to remove ", Toast.LENGTH_SHORT).show();
        }

        if(mybet.getNap().equals("AG") && mybet.getBet2().size()< 2)
            mBet2EditText.setEnabled(true);
        else if(mybet.getNap().equals("AGS") && mybet.getBet2().size()< mybet.getMaxball())
            mBet2EditText.setEnabled(true);
        mBet2EditText.setFocusable(true);
        mBet2EditText.requestFocus();
    }


    private void InitialiseControlsWithValue() {
        try
        {
         mybet =   ((MyApplication) getActivity().getApplication()).getCurrentBet();
         myticket = ((MyApplication) getActivity().getApplication()).getCurrentTicket();
         customer =   ((MyApplication) getActivity().getApplication()).getCurrentCustomer();

        mGamename.setText(myticket.getGamename());
        mBetType.setText(mybet.getNap());

        if(mybet.getNap().equals("AG") || mybet.getNap().equals("AGS"))
            mBet2EditText.setVisibility(View.VISIBLE);

        if(mybet.getBet1().size() > 0 || mybet.getBet2().size() > 0)
            ReloadView();
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(), "init err: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public  void initialiseControls(View rootView)
    {
         mGamename= (TextView) rootView.findViewById(R.id.place_bet_game_tv);
         mBetType = (TextView) rootView.findViewById(R.id.place_bet_bet_tv);
         mBet1TextView= (TextView) rootView.findViewById(R.id.bet1textview);
         mBet2TextView = (TextView) rootView.findViewById(R.id.bet2textview);
        mstakeamountTextView = (TextView) rootView.findViewById(R.id.amounttextview);

        mbet1deletebutton = (Button) rootView.findViewById(R.id.deletebet1button);
        mbet2deletebutton = (Button) rootView.findViewById(R.id.deletebet2button);

        mBet1frame = (LinearLayout) rootView.findViewById(R.id.bet1frame);
        mBet2frame = (LinearLayout) rootView.findViewById(R.id.bet2frame);


         mBet1EditText= (EditText) rootView.findViewById(R.id.bet1Edittext);
        mBet1EditText.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "90")});
         mBet2EditText = (EditText) rootView.findViewById(R.id.bet2Edittext);
        mBet2EditText.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "90")});
        mamountEditText = (EditText)rootView.findViewById(R.id.betstakeperlineEdittext);


        mAddtobetButton = (Button) rootView.findViewById(R.id.addtobetsbutton);
        mPlayButton = (Button) rootView.findViewById(R.id.playbutton);


        mBet2EditText.setVisibility(View.GONE);
        mBet1frame.setVisibility(View.GONE);
        mBet2frame.setVisibility(View.GONE);

        mBet1EditText.requestFocus();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

//    View.OnFocusChangeListener EditTextFocusChange =  new View.OnFocusChangeListener() {
//        @Override
//        public void onFocusChange(View view, boolean b) {
//            InputMethodManager inputManager =(InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
//            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//    };



    private void fillbet1() {
        try
        {
            String betInput = mBet1EditText.getText().toString();
            betInput = padbet(betInput);
            if(canaddbet1(betInput)) {
                mybet.getBet1().add(betInput);
                if (mybet.getBet1().size() >= mybet.getMaxball()) {
                    mBet1EditText.setText("");
                    mBet1EditText.setEnabled(false);

                    mBet1EditText.clearFocus();
                    if (mBet2EditText.getVisibility() == View.VISIBLE) {
                        FocusView = mBet2EditText;

                    } else {

                        FocusView = mamountEditText;

                    }
                } else {
                    mBet1EditText.setText("");
                    FocusView = mBet1EditText;


                }
                ReloadView();
            }
        }
        catch(Exception e)
            {
                Toast.makeText(getActivity(), "fill bet err: "+e.getMessage() , Toast.LENGTH_SHORT);
            }

        }

    @NonNull
    private String padbet(String betInput) {
        int num = Integer.parseInt(betInput);
        betInput = (num > 0 && num <10) ?("0"+num) : ""+num ;
        return betInput;
    }


    private void fillbet2() {
        try
        {
        String betInput = mBet2EditText.getText().toString();
            betInput = padbet(betInput);
        if(canaddbet2(betInput)) {
            mybet.getBet2().add(betInput);
            if(mybet.getNap().equals("AG") && mybet.getBet2().size()== 2) {
                mBet2EditText.setText("");
                mBet2EditText.setEnabled(false);
                mBet2EditText.clearFocus();
                FocusView = mamountEditText;

            }
            else if(mybet.getNap().equals("AGS") && mybet.getBet2().size()>= mybet.getMaxball()) {
                mBet2EditText.setText("");
                mBet2EditText.setEnabled(false);
                mBet2EditText.clearFocus();
                FocusView = mamountEditText;

            }
            else {
                mBet2EditText.setText("");
                FocusView = mBet2EditText;
//                View focusView ;
//                focusView = mBet2EditText;
//
//                focusView.clearFocus();
//                focusView.setFocusable(true);
//                focusView.requestFocus();

            }


            ReloadView();
        }
        }
        catch(Exception e)
        {
            Toast.makeText(getActivity(), "fill bet 2 err: "+e.getMessage() , Toast.LENGTH_SHORT);
        }
    }



    private EditText.OnEditorActionListener mybet1EditorActionListener = new EditText.OnEditorActionListener(){
        public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
            try {
                if (actionId == EditorInfo.IME_NULL && (event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.KEYCODE_CALL)) {
                    fillbet1();
                }
                return true;
            }
            catch (Exception e)
            {
                return false;
            }
        }
    };

    private EditText.OnEditorActionListener mybet2EditorActionListener = new EditText.OnEditorActionListener(){
        public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
            try {
                if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    fillbet2();
                }
                return true;
            }
            catch (Exception e)
            {
                return false;
            }
        }
    };

    private EditText.OnEditorActionListener myamountEditorActionListener = new EditText.OnEditorActionListener(){
        public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
            try {
                if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String amountString = mamountEditText.getText().toString();
                    if(checkamount(amountString)) {
                        mybet.setStakeperline(Double.parseDouble(amountString));
                        InputMethodManager inputManager =(InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                        FocusView = mAddtobetButton;
                        ReloadView();
                    }

                }
                return true;
            }
            catch (Exception e)
            {
                Toast.makeText(getActivity(), "Add Amount err: "+ e.getMessage() , Toast.LENGTH_SHORT);
                return false;
            }
        }
    };



    private void ReloadView() {

        try {
            if (mybet.getBet1().size() > 0) {
                mBet1TextView.setText("BET 1 :" + mybet.getBet1Text());
                mBet1frame.setVisibility(View.VISIBLE);
            } else {
                mBet1frame.setVisibility(View.INVISIBLE);
            }

            if (mybet.getBet2().size() > 0) {
                mBet2TextView.setText("BET 2 :" + mybet.getBet2Text());
                mBet2frame.setVisibility(View.VISIBLE);
            } else {
                mBet2frame.setVisibility(View.INVISIBLE);
            }

            if (mybet.getBet1().size() >= mybet.getMin_no_of_balls()) {
                LottoCalculations betcalculator = new LottoCalculations();
                mybet = betcalculator.DoCalculation(mybet);
                mstakeamountTextView.setText("LINES : " + mybet.getLines() + "  AMT: ₦" + mybet.getAmount());
            }

            FocusView.clearFocus();
            FocusView.setFocusable(true);
            FocusView.requestFocus();
            //Toast.makeText(getActivity(), ""+ FocusView.getId() , Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(), "Reload View err: "+ e.getMessage() , Toast.LENGTH_SHORT).show();

        }
    }



    private boolean canaddbet1(String betInput) {
        try {
            int num = Integer.parseInt(betInput);
            if (!betInput.equals("")  && !betInput.isEmpty() && num > 0 && num < 91 && Checkrepetition(betInput, mybet.getBet1()) == false && betMaxreached() == false)
                return true;
            return false;
        }
        catch (Exception ex)
        {
            return  false;
        }


    }

    private boolean checkamount(String amountString)
    {
        try {
            double amount = Double.parseDouble(amountString);
            if(amount <= mybet.getMax_stake() && amount >= mybet.getMin_stake())
                return true;
            else if(amount > mybet.getMax_stake())
            {
                mamountEditText.setError("Amount should not be more than" + mybet.getMax_stake());
                return false;
            }
            else if(amount < mybet.getMin_stake())
            {
                mamountEditText.setError("Amount should not be less than" + mybet.getMin_stake());
                return false;
            }
            return false;
        }
        catch (Exception ex)
        {
            return  false;
        }
    }

    private boolean canaddbet2(String betInput) {

        try {
            int num = Integer.parseInt(betInput);
            if(!betInput.equals("") && num > 0 && num < 91 && Checkrepetition(betInput, mybet.getBet2())== false )
            {
                if(mybet.getNap().equals("AG"))
                {
                    if(mybet.getBet2().size() ==0)
                    {
                        return true;
                    }
                    else {
                        int start = Integer.parseInt(mybet.getBet2().get(0));
                        int end = Integer.parseInt(betInput);
                        if (mybet.getBet2().size() < 2 && start < end)
                            return true;
                        else
                            {
                                Toast.makeText(getActivity(), "AGAINST BET NOT IN RIGHT ORDER", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                    }
                }
                else if(mybet.getNap().equals("AGS"))
                {
                    if(mybet.getBet2().size()< mybet.getMaxball())
                        return true;
                }
                return  false;
        }
        else
            return false;
        }
        catch (Exception ex)
        {
            return  false;
        }

    }


    private boolean Checkrepetition(String betInput, List<String> betstring) {

        for (String a:
                betstring) {
            if(a.equals(betInput)) {
                Toast.makeText(getActivity(), "repeated values", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return  false;
    }

    private boolean betMaxreached() {

        if(mybet.getBet1().size() >= mybet.getMaxball())
        {
            Toast.makeText(getActivity(), "MAX BALL IS "+ mybet.getMaxball(), Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;


    }

}





