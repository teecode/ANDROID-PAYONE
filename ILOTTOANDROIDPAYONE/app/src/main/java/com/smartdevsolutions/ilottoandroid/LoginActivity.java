package com.smartdevsolutions.ilottoandroid;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import com.smartdevsolutions.ilottoandroid.Utility.CallService;
import com.smartdevsolutions.ilottoandroid.Utility.Customer;
import com.smartdevsolutions.ilottoandroid.Utility.LoginInput;
import com.smartdevsolutions.ilottoandroid.Utility.MyApplication;
import com.smartdevsolutions.ilottoandroid.Utility.TicketInput;



import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity  {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private Customer cust;

    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mTerminalNo;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // Set up the login form.
        mTerminalNo = (AutoCompleteTextView) findViewById(R.id.terminalNo);
      //  populateAutoComplete();
        errorMsg =  (TextView)  findViewById(R.id.errorMsg);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mPasswordView.setTransformationMethod(PasswordTransformationMethod.getInstance());


        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        String TID =  getDeviceId(this);
        mTerminalNo.setText(TID);
        mTerminalNo.setEnabled(false);
        View focusView = null;
        focusView = mPasswordView;
        focusView.requestFocus();


//         String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
//        DeviceEngine deviceEngine = ((MyApplication) getApplication()).getDeviceEngine();
//        mTerminalNo.setText( deviceEngine.readSN());


    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_CALL) {
            showProgress(true);
            mAuthTask = new UserLoginTask(mTerminalNo.getText().toString(),  mPasswordView.getText().toString(), this);
            mAuthTask.execute((Void) null);
        }
        return super.onKeyDown(keyCode, event);
    }

//    private void populateAutoComplete() {
//        getLoaderManager().initLoader(0, null, this);
//    }


    public String getDeviceId(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mTerminalNo.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String termno = mTerminalNo.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(termno)) {
            mTerminalNo.setError(getString(R.string.error_field_required));
            focusView = mTerminalNo;
            cancel = true;
        }
        else if (!isTermNoValid(termno)) {
            mTerminalNo.setError(getString(R.string.error_invalid_termno));
            focusView = mTerminalNo;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(termno, password, this);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isTermNoValid(String termno) {
        return termno.length() >= 4;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 4;
    }



    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }





    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mterminalNo;
        private final String mPassword;
        String debugmessage ="";
        Context ctx;

        private Customer cust ;

        UserLoginTask(String terminalNo, String password, Context ctx) {
            this.ctx = ctx;
            mterminalNo = terminalNo;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            try {
                LoginInput logininput = new LoginInput();
                logininput.setUsername( mterminalNo);
                logininput.setPassword(mPassword);
                logininput.setVersion(getString(R.string.service_version));




                Gson gson = new Gson();
                Type type = new TypeToken<LoginInput>(){}.getType();
                String json = gson.toJson(logininput, type);
                String url = (getString(R.string.service_url) +getString(R.string.service_authentication_controller));
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
            mAuthTask = null;
            showProgress(false);
//            Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse("https://winnersgoldenchance.com/gameplay/android-debug.apk"));
//            startActivity(intent);

            if (success) {
                errorMsg.setText(debugmessage);
                Gson gson = new Gson();
                Type type = new TypeToken<Customer>(){}.getType();
                String customerstring = gson.toJson(cust, type);

                Intent myIntent = new Intent(ctx, HomeActivity.class);
                myIntent.putExtra("LoggedInCustomer", customerstring);
                 ((MyApplication) getApplication()).setCurrentCustomer(cust);

                try
                {
                    if(cust.getCustomertype() == 3)
                    {
                        LoadShopCashierToApplication(cust.getShopid());
                    }
                }
                catch (Exception e)
                {

                }

                ctx.startActivity(myIntent);
                //finish();
            } else {
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
                errorMsg.setText(debugmessage);
            }
        }
        public  void LoadShopCashierToApplication(int shopId)
        {
            LoadCustomerTask Ctask = new LoadCustomerTask(shopId, ctx);
            Ctask.execute();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public class LoadCustomerTask extends AsyncTask<Void, Void, Boolean> {

        private int shopID;
        String debugmessage ="";
        Context ctx;
        private List<Customer> cashiers;

        private Customer cust ;

        public LoadCustomerTask(int shopID ,Context ctx) {
            this.ctx = ctx;
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
            mAuthTask = null;
            showProgress(false);

            if (success) {

                ((MyApplication) getApplication()).setCashiersInShop(cashiers);
                Toast.makeText(ctx, "Cashiers Generated", Toast.LENGTH_SHORT );

            } else {
                Toast.makeText(ctx, debugmessage, Toast.LENGTH_SHORT).show();

            }
        }


        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}



