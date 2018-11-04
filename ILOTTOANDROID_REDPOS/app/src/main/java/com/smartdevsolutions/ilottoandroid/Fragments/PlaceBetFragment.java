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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smartdevsolutions.ilottoandroid.ApiResource.AddBetSlipResource;
import com.smartdevsolutions.ilottoandroid.ApiResource.AddTicketResource;
import com.smartdevsolutions.ilottoandroid.ApiResource.BetTypeResource;
import com.smartdevsolutions.ilottoandroid.ApiResource.DailyGameResource;
import com.smartdevsolutions.ilottoandroid.ApiResource.DeviceResource;
import com.smartdevsolutions.ilottoandroid.R;
import com.smartdevsolutions.ilottoandroid.TicketActivity;
import com.smartdevsolutions.ilottoandroid.Utility.InputFilterMinMax;
import com.smartdevsolutions.ilottoandroid.Utility.LottoCalculations;
import com.smartdevsolutions.ilottoandroid.Utility.MyApplication;

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

    AddBetSlipResource mybet;
    AddTicketResource myticket;
    DeviceResource device;

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

    public DailyGameResource getTicketGame(){
        List<DailyGameResource> AlltodaysGames = (((MyApplication) getActivity().getApplication()).getFetchedGames());
        for (DailyGameResource dly:AlltodaysGames) {
            if(dly.getGameID()== myticket.getGame()){
                return  dly;
            }
        }
        return  null;
    }

    public BetTypeResource getCurrentBetBetType(){
        List<BetTypeResource> ALlbetTypes = getTicketGame().getBetTypes();
        for (BetTypeResource btp:ALlbetTypes) {
            if(btp.getBetTypeID()== mybet.getNap()){
                return  btp;
            }
        }
        return  null;
    }

    private boolean RevalidateBet() {
        BetTypeResource currentbettype = getCurrentBetBetType();
        if(!mybet.getNapName(((MyApplication) getActivity().getApplication()).getFetchedGames().get(0).getBetTypes()).equals("")  && mybet.getBet1().size()> 0 && mybet.getLines()>0 && mybet.getAmount() >= currentbettype.getMinimumStake() && mybet.getAmount() <= currentbettype.getMaximumStake())
            return true;
        return false;
    }


    private void deletebet1() {
        BetTypeResource currentbettype = getCurrentBetBetType();
        int index = mybet.getBet1().size();
        if(index > 0) {
            mybet.getBet1().remove(index - 1);
            ReloadView();
        }
        else {
            Toast.makeText(getActivity(), " Nothing to remove ", Toast.LENGTH_SHORT).show();
        }

        if(mybet.getBet1().size()< currentbettype.getMaximumNumberOfBalls())
            mBet1EditText.setEnabled(true);
        mBet1EditText.setFocusable(true);
        mBet1EditText.requestFocus();
    }

    private void deletebet2() {
        BetTypeResource currentbettype = getCurrentBetBetType();
        int index = mybet.getBet2().size();
        if(index > 0) {
            mybet.getBet2().remove(index - 1);

            ReloadView();
        }
        else {
            Toast.makeText(getActivity(), " Nothing to remove ", Toast.LENGTH_SHORT).show();
        }
       String NapName = currentbettype.getNap();
        if(NapName.equals("AG") && mybet.getBet2().size()< 2)
            mBet2EditText.setEnabled(true);
        else if(NapName.equals("AGS") && mybet.getBet2().size()< currentbettype.getMaximumNumberOfBalls())
            mBet2EditText.setEnabled(true);
        mBet2EditText.setFocusable(true);
        mBet2EditText.requestFocus();
    }


    private void InitialiseControlsWithValue() {
        try
        {

         mybet =   ((MyApplication) getActivity().getApplication()).getCurrentBet();
         myticket = ((MyApplication) getActivity().getApplication()).getCurrentTicket();
         device =   ((MyApplication) getActivity().getApplication()).getCurrentTerminal();
         BetTypeResource currentbettype = getCurrentBetBetType();
         DailyGameResource currentgame = getTicketGame();
        mGamename.setText(currentgame.getGameName());
        mBetType.setText(currentbettype.getNap());

        if(currentbettype.getNap().equals("AG") || currentbettype.getNap().equals("AGS"))
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
            BetTypeResource currentBetType = getCurrentBetBetType();
            String betInput = mBet1EditText.getText().toString();
            betInput = padbet(betInput);
            if(canaddbet1(betInput)) {
                mybet.getBet1().add(Integer.parseInt(betInput));
                if (mybet.getBet1().size() >= currentBetType.getMaximumNumberOfBalls()) {
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
            BetTypeResource currentBetType = getCurrentBetBetType();
        String betInput = mBet2EditText.getText().toString();
            betInput = padbet(betInput);
        if(canaddbet2(betInput)) {
            mybet.getBet2().add(Integer.parseInt(betInput));
            if(currentBetType.getNap().equals("AG") && mybet.getBet2().size()== 2) {
                mBet2EditText.setText("");
                mBet2EditText.setEnabled(false);
                mBet2EditText.clearFocus();
                FocusView = mamountEditText;

            }
            else if(currentBetType.getNap().equals("AGS") && mybet.getBet2().size()>= currentBetType.getMaximumNumberOfBalls()) {
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
            BetTypeResource currentBetType = getCurrentBetBetType();
            if (mybet.getBet1().size() > 0) {
                mBet1TextView.setText("BET 1 :" + mybet.getBet1String());
                mBet1frame.setVisibility(View.VISIBLE);
            } else {
                mBet1frame.setVisibility(View.INVISIBLE);
            }

            if (mybet.getBet2().size() > 0) {
                mBet2TextView.setText("BET 2 :" + mybet.getBet2String());
                mBet2frame.setVisibility(View.VISIBLE);
            } else {
                mBet2frame.setVisibility(View.INVISIBLE);
            }

            if (mybet.getBet1().size() >= currentBetType.getMinimumNumberOfBalls()) {
                LottoCalculations betcalculator = new LottoCalculations();
                mybet = betcalculator.DoCalculation(mybet, getTicketGame().getBetTypes());
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
            BetTypeResource currentBetType = getCurrentBetBetType();
            double amount = Double.parseDouble(amountString);
            if(amount <= currentBetType.getMaximumStake() && amount >= currentBetType.getMinimumStake())
                return true;
            else if(amount > currentBetType.getMaximumStake())
            {
                mamountEditText.setError("Amount should not be more than" + currentBetType.getMaximumStake());
                return false;
            }
            else if(amount < currentBetType.getMinimumStake())
            {
                mamountEditText.setError("Amount should not be less than" + currentBetType.getMinimumStake());
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
            BetTypeResource currentBetType = getCurrentBetBetType();
            int num = Integer.parseInt(betInput);
            if(!betInput.equals("") && num > 0 && num < 91 && Checkrepetition(betInput, mybet.getBet2())== false )
            {
                if(currentBetType.getNap().equals("AG"))
                {
                    if(mybet.getBet2().size() ==0)
                    {
                        return true;
                    }
                    else {
                        int start =mybet.getBet2().get(0);
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
                else if(currentBetType.getNap().equals("AGS"))
                {
                    if(mybet.getBet2().size()< currentBetType.getMaximumNumberOfBalls())
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


    private boolean Checkrepetition(String betInput, List<Integer> betstring) {

        for (Integer a:
                betstring) {
            if(a.equals(betInput)) {
                Toast.makeText(getActivity(), "repeated values", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return  false;
    }

    private boolean betMaxreached() {
        BetTypeResource currentBetType = getCurrentBetBetType();
        if(mybet.getBet1().size() >= currentBetType.getMaximumNumberOfBalls())
        {
            Toast.makeText(getActivity(), "MAX BALL IS "+ currentBetType.getMaximumNumberOfBalls(), Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;


    }

}





