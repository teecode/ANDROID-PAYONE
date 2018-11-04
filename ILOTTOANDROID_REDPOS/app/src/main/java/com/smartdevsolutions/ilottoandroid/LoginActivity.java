package com.smartdevsolutions.ilottoandroid;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.smartdevsolutions.ilottoandroid.ApiResource.DeviceResource;
import com.smartdevsolutions.ilottoandroid.ApiResource.LoginResource;
import com.smartdevsolutions.ilottoandroid.ApiResource.RegisterDeviceRequestResource;
import com.smartdevsolutions.ilottoandroid.Data.DBHelper;
import com.smartdevsolutions.ilottoandroid.Utility.CallService;
import com.smartdevsolutions.ilottoandroid.Utility.Customer;
import com.smartdevsolutions.ilottoandroid.Utility.MyApplication;
import com.smartdevsolutions.ilottoandroid.Utility.Tuple;

import org.joda.time.DateTime;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
   // private Customer cust;

    private UserLoginTask mAuthTask = null;
    private RegisterDeviceTask mRegDeviceTask = null;

    // UI references.
    private AutoCompleteTextView mTerminalNo;
   // private EditText mPasswordView;
    private View mProgressView;
    Button mEmailSignInButton;
    private ProgressDialog progress;
    private View mLoginFormView;
    private TextView errorMsg;
    private DeviceResource _license;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    boolean isnewandroid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);
        initialiserControls();
        switch (checklicense()) {
            case 1:
                mEmailSignInButton.setEnabled(true);

                break;
            case -1:
                try {
                    showProgress("Registering Device", "Setting Up First Run");
                    mRegDeviceTask = new RegisterDeviceTask(getDeviceId(this), this);
                    mRegDeviceTask.execute();
                    //mRegDeviceTask.wait();
                    //checklicense();
                }catch (Exception ex){
                    Toast.makeText(this, "Error Fetching License", Toast.LENGTH_LONG).show();
                }
                break;
            case -2:
                Toast.makeText(this, "Your license has expired, please contact sys admin", Toast.LENGTH_LONG).show();
                reactivateDevice();
                break;
            case -3:
                Toast.makeText(this, "Your License would expire on "+ _license.getReActivateDate().toString(), Toast.LENGTH_LONG).show();
                reactivateDevice();
                break;
        }


    }

    private void initialiserControls() {
        isnewandroid = false;
        // Set up the login form.
        mTerminalNo = (AutoCompleteTextView) findViewById(R.id.terminalNo);
        mTerminalNo.setEnabled(false);
        //  populateAutoComplete();
        errorMsg = (TextView) findViewById(R.id.errorMsg);


        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mEmailSignInButton.setEnabled(false);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        try {
            String TID = getDeviceId(this);
            mTerminalNo.setText(_license.getSerial());
            mTerminalNo.setEnabled(false);
            //mTerminalNo.setText(TID);
            if (TID == null || TID == "") {
                isnewandroid = true;
            } else {
                mTerminalNo.setEnabled(false);
            }
        } catch (Exception ex) {
            isnewandroid = true;
        }

        if (isnewandroid) {

            int permissionCheck = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                try {
                    String TID = getDeviceId(this);
                    mTerminalNo.setText(TID);
                    //  mTerminalNo.setEnabled(false);
                } catch (Exception ex) {

                }
            } else {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_PHONE_STATE)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.READ_PHONE_STATE},
                                MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }

            }
        }
    }

    public void reactivateDevice(){
        try {
            Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
            String url = (getString(R.string.service_url) + getString(R.string.service_activate) );
            CallService mysevice = new CallService(url, "");
            showProgress("Trying Activation", "Checking License Activation");
            Tuple<Integer, String> responsetuple = mysevice.invokePostService(null);
            progress.dismiss();
            String response = responsetuple.item2();
            if (responsetuple.item1() == 200) {
                Type type = new TypeToken<List<DeviceResource>>() {}.getType();
                DeviceResource activationlicense = gson.fromJson(response, type);
                if(!activationlicense.getReActivateDate().equals(_license.getReActivateDate())){
                    boolean alertdone = false;
                    if(activationlicense.getReActivateDate().after(_license.getReActivateDate()))
                        alertdone = true;
                    _license.setReActivateDate(activationlicense.getReActivateDate());
                    DBHelper helper = new DBHelper(this);
                    helper.updateConfiguration("license", gson.toJson(_license), null);
                    if(alertdone) {
                        Toast.makeText(this, "License has been renewed successfully", Toast.LENGTH_LONG).show();
                        Toast.makeText(this, "Please restart the application", Toast.LENGTH_LONG).show();
                        mEmailSignInButton.setEnabled(true);
                    }

                }
                else{
                    Toast.makeText(this, "License has not been renewed", Toast.LENGTH_LONG).show();
                }


            }
            else {
                Toast.makeText(this, "Error"+ responsetuple.item1() + ":" +responsetuple.item2(), Toast.LENGTH_LONG).show();
            }

        } catch (Exception ex) {
            Toast.makeText(this, "Error"+ ex.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    public void showProgress(  String Title, String Message) {
        progress = ProgressDialog.show(this, Title,
                Message, true);
    }

    public int checklicense() {
        DBHelper db = new DBHelper(this);

        if (db.iskeyavailable("license") == false)
            return -1;
        else {
            Cursor license = db.getData("license");
            license.moveToFirst();
            while (license.isAfterLast() == false) {
                String licensevalue = license.getString(license.getColumnIndex("value"));

                Type type = new TypeToken<DeviceResource>() {
                }.getType();
                Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
                _license = gson.fromJson(licensevalue, type);
                license.moveToNext();
            }

            Date warningdate = (new DateTime(_license.getReActivateDate())).minusDays(5).toDate();
            Date today = new Date();
            if (_license.getReActivateDate().before(today)) {
                //expired
                return -2;
            }
            else if (today.equals(warningdate) || today.after(warningdate)){
                //reminder
                return -3;
            }

            return 1;

//

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String TID = getDeviceId(this);
                    mTerminalNo.setText(TID);
                    //mTerminalNo.setEnabled(false);


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_CALL) {
            showProgress(true);
            mAuthTask = new UserLoginTask(mTerminalNo.getText().toString(), _license.getPassword(), this);
            mAuthTask.execute((Void) null);
        }
        return super.onKeyDown(keyCode, event);
    }

//    private void populateAutoComplete() {
//        getLoaderManager().initLoader(0, null, this);
//    }


    public String getDeviceId(Context context) {
        try {
            // TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //return  null; // comment this for normal phone
            // return telephonyManager.getDeviceId();   // comment this for payone devicde
            return "12345678";
            //return "863835025632642";  // for test purpose , password = 1234 cashieracct = 11223-Testcash33

        } catch (SecurityException ex) {
            return null;
        } catch (Exception ex) {
            return null;
        }


    }

    public String getDeviceId(Context context, int slot) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //return  null; // comment this for normal phone
            return telephonyManager.getDeviceId();   // comment this for payone devicde
            //return "863835025649356";
            //return "863835025632642";  // for test purpose , password = 1234 cashieracct = 11223-Testcash33

        } catch (SecurityException ex) {
            return null;
        } catch (Exception ex) {
            return null;
        }

    }

    public String getDeviceSerialNo(Context context) {
        try {
            return Build.SERIAL;
        } catch (Exception ex) {
            return null;
        }
    }


    public String getAndroidID(Context context) {
        try {
            String android_id = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            return android_id;
        } catch (Exception ex) {
            return null;
        }
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
        //mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String termno = mTerminalNo.getText().toString();
        String password = _license.getPassword();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            //mPasswordView.setError(getString(R.string.error_invalid_password));
            //focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(termno)) {
            mTerminalNo.setError(getString(R.string.error_field_required));
            focusView = mTerminalNo;
            cancel = true;
        } else if (!isTermNoValid(termno)) {
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
        String debugmessage = "";
        Context ctx;

        private DeviceResource device;

        UserLoginTask(String terminalNo, String password, Context ctx) {
            this.ctx = ctx;
            mterminalNo = terminalNo;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            try {
                LoginResource logininput = new LoginResource();
                logininput.setUsername(mterminalNo);
                logininput.setPassword(mPassword);


                Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
                Type type = new TypeToken<LoginResource>() {
                }.getType();
                String json = gson.toJson(logininput, type);
                String url = (getString(R.string.service_url) + getString(R.string.service_login));
                CallService mysevice = new CallService(url, json);
                Tuple<Integer, String> responseTuple = mysevice.invokePostService(null);
                int responsecode = responseTuple.item1();
                String response = responseTuple.item2();
                if (responsecode == 200) {
                    type = new TypeToken<DeviceResource>() {
                    }.getType();
                    device = gson.fromJson(response, type);
                    return  true;
                } else {
                    debugmessage += "Error " + responsecode + " " + response;
                    return false;
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
                Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
                Type type = new TypeToken<DeviceResource>(){}.getType();
                String devicestring = gson.toJson(device, type);

                Intent myIntent = new Intent(ctx, HomeActivity.class);
                myIntent.putExtra("LoggedInTerminal", devicestring);
                ((MyApplication) getApplication()).setCurrentTerminal(device);

//                try {
//                    if (cust.getCustomertype() == 3) {
//                        LoadShopCashierToApplication(cust.getShopid());
//                    }
//                } catch (Exception e) {
//
//                }

                ctx.startActivity(myIntent);
                //finish();
            } else {

                errorMsg.setText(debugmessage);
            }
        }

        public void LoadShopCashierToApplication(int shopId) {
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
        String debugmessage = "";
        Context ctx;
        private List<Customer> cashiers;

        private Customer cust;

        public LoadCustomerTask(int shopID, Context ctx) {
            this.ctx = ctx;
            this.shopID = shopID;
            cashiers = new ArrayList<Customer>();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                Gson gson = new Gson();

                String url = (getString(R.string.service_url) + getString(R.string.service_CustomersInShop_controller) + shopID);
                CallService mysevice = new CallService(url, "");
                String response = mysevice.invokeGetService();
                if (response != "") {
                    Type type = new TypeToken<List<Customer>>() {
                    }.getType();
                    cashiers = gson.fromJson(response, type);

                    return true;
                } else {
                    debugmessage += "Poor Network, Please Check Your Network and Try Again!!!";
                    return false;
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
                Toast.makeText(ctx, "Cashiers Generated", Toast.LENGTH_SHORT);

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

    public class RegisterDeviceTask extends AsyncTask<Void, Void, Boolean> {

        private final String _SerialNumber;
        String debugmessage = "";
        Context ctx;

        private DeviceResource deviceResource;

        RegisterDeviceTask(String serialNo, Context ctx) {
            this.ctx = ctx;
            _SerialNumber = serialNo;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            try {

                RegisterDeviceRequestResource rdrr = new RegisterDeviceRequestResource();
                rdrr.setImiEs(new ArrayList<String>() {{
                    add(_SerialNumber);
                }});
                rdrr.setSerial(_SerialNumber);

                Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();

                Type type = new TypeToken<RegisterDeviceRequestResource>() {
                }.getType();
                String json = gson.toJson(rdrr, type);
                String url = (getString(R.string.service_url) + getString(R.string.service_register_device));
                CallService mysevice = new CallService(url, json);
                Tuple<Integer, String> response = mysevice.invokePostService(null);
                if (response.item1() == 200) {
                    type = new TypeToken<DeviceResource>() {}.getType();
                    String responseString = response.item2();
                    deviceResource = gson.fromJson(responseString, type);
                   return (SaveLicense(responseString) == 1);
                    //return true;
                } else {
                    debugmessage += response.item1() + ":" + response.item2();
                    return false;
                }

            } catch (Exception e) {
                debugmessage = e.getMessage();
                return false;
            }


        }

        private int SaveLicense(String responseString) {
            DBHelper db = new DBHelper(ctx);
            Cursor license = db.getData("license");
            license.moveToFirst();
            if(  license.isAfterLast() == true ) {

                db.insertConfiguration("license", responseString, "license");
                db.getData("license");
                return 1;
            }
            else{
                license.moveToFirst();
                while(license.isAfterLast() == false){
                    String terminalid = license.getString(license.getColumnIndex("value"));
                    license.moveToNext();
                }
                return 0;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            progress.dismiss();
//            Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse("https://winnersgoldenchance.com/gameplay/android-debug.apk"));
//            startActivity(intent);

            if (success) {
                if (deviceResource != null) {
                    _license = deviceResource;
                    mTerminalNo.setText(deviceResource.getSerial());
                    mEmailSignInButton.setEnabled(true);
                    Toast.makeText(ctx, "Device Registered Successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ctx, debugmessage, Toast.LENGTH_LONG).show();
                    //debugmessage = cust.getErrormessage();

                }
                //finish();
            } else {

                errorMsg.setText(debugmessage);
            }
        }

        public void LoadShopCashierToApplication(int shopId) {
            LoadCustomerTask Ctask = new LoadCustomerTask(shopId, ctx);
            Ctask.execute();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}



