package com.smartdevsolutions.ilottoandroid;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartdevsolutions.ilottoandroid.Fragments.AccountSummaryFragment;
import com.smartdevsolutions.ilottoandroid.Fragments.CashiersBlockingFragment;
import com.smartdevsolutions.ilottoandroid.Fragments.CheckTicketFragment;
import com.smartdevsolutions.ilottoandroid.Fragments.DailyGameFragment;
import com.smartdevsolutions.ilottoandroid.Fragments.FundTransferFragment;
import com.smartdevsolutions.ilottoandroid.Fragments.PlaceBetFragment;
import com.smartdevsolutions.ilottoandroid.Fragments.ProfileFragment;
import com.smartdevsolutions.ilottoandroid.Fragments.RegisterSlipFragment;
import com.smartdevsolutions.ilottoandroid.Fragments.menuFragment;
import com.smartdevsolutions.ilottoandroid.Fragments.winningFragment;
import com.smartdevsolutions.ilottoandroid.Utility.Customer;
import com.smartdevsolutions.ilottoandroid.Utility.MyApplication;
import com.smartdevsolutions.ilottoandroid.Utility.Ticket;
import com.smartdevsolutions.ilottoandroid.Utility.TicketInput;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Vector;

/**
 * Created by BINARYCODES on 5/8/2017.
 */

public class HomeActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;
    private TextView walletbalTV;
    private Button mbetslipButton;
    private int menuItemID;
    private Gson gson ;
    private Customer Customer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        initialiseControls();
        // String customerstring  = getIntent().getStringExtra("LoggedInCustomer");
        // Type type = new TypeToken<Customer>(){}.getType();
        Customer = ((MyApplication) getApplication()).getCurrentCustomer();

        walletbalTV.setText(" ₦"+ String.format("%.2f", Customer.getWalletbalance()));

        if(mbetslipButton!= null) {
            mbetslipButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TicketInput myticket = ((MyApplication) getApplication()).getCurrentTicket();
                    if (myticket == null || myticket.getBetslips().size() <= 0) {
                        Toast.makeText(HomeActivity.this, "BetSlip is Currently Empty", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(HomeActivity.this, TicketActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }

        ActionBar actionbar = getSupportActionBar();


        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // Do whatever you want here
                if(navigationView.getMenu().findItem(menuItemID) != null)
                    navigationView.getMenu().findItem(menuItemID).setChecked(false);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Do whatever you want here
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
         mDrawerToggle.syncState();
        actionbar.setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                                                             @Override
                                                             public boolean onNavigationItemSelected(MenuItem menuItem) {
                                                                 menuItemID=menuItem.getItemId();
                                                                 switch(menuItemID) {
                                                                     case R.id.home:
                                                                         setTitle(getResources().getString(R.string.home));
                                                                         Fragment fragment = new menuFragment();
                                                                         FragmentManager fragmentManager = getFragmentManager();
                                                                         fragmentManager.beginTransaction().replace(R.id.content_frame, fragment,"homefragment").addToBackStack( "homefragment" ).commit();
                                                                         mDrawerLayout.closeDrawer(navigationView);
                                                                         return true;
                                                                     case R.id.play:
                                                                         setTitle(getResources().getString(R.string.play));
                                                                         Fragment playfragment = new DailyGameFragment();
                                                                         FragmentManager playfragmentManager = getFragmentManager();
                                                                         playfragmentManager.beginTransaction().replace(R.id.content_frame, playfragment, "playfragment").addToBackStack( "homefragment" ).commit();
                                                                         mDrawerLayout.closeDrawer(navigationView);
                                                                         return true;
                                                                     case R.id.winningnumber:
                                                                         setTitle(getResources().getString(R.string.winningnumber));
                                                                         Fragment winningfragment = new winningFragment();
                                                                         FragmentManager winningfragmentManager = getFragmentManager();
                                                                         winningfragmentManager.beginTransaction().replace(R.id.content_frame, winningfragment, "winningfragment").addToBackStack( "homefragment" ).commit();
                                                                         mDrawerLayout.closeDrawer(navigationView);
                                                                         return true;
                                                                     case R.id.checkticket:
                                                                         setTitle(getResources().getString(R.string.checkticket));
                                                                         Fragment CheckTicketFragment = new CheckTicketFragment();
                                                                         FragmentManager CheckTicketfragmentManager = getFragmentManager();
                                                                         CheckTicketfragmentManager.beginTransaction().replace(R.id.content_frame, CheckTicketFragment, "checkticketFragment").addToBackStack( "homefragment" ).commit();
                                                                         mDrawerLayout.closeDrawer(navigationView);
                                                                         return true;
                                                                     case R.id.account:
                                                                         setTitle(getResources().getString(R.string.account));
                                                                         Fragment AccountFragment = new AccountSummaryFragment();
                                                                         FragmentManager AccountfragmentManager = getFragmentManager();
                                                                         AccountfragmentManager.beginTransaction().replace(R.id.content_frame, AccountFragment, "accountfragment").addToBackStack("homefragment").commit();
                                                                         mDrawerLayout.closeDrawer(navigationView);
                                                                         return true;
                                                                     case R.id.profile:
                                                                         setTitle(getResources().getString(R.string.profile));
                                                                         Fragment ProfileFragment = new ProfileFragment();
                                                                         FragmentManager ProfilefragmentManager = getFragmentManager();
                                                                         ProfilefragmentManager.beginTransaction().replace(R.id.content_frame, ProfileFragment, "profilefragment").addToBackStack( "homefragment" ).commit();
                                                                         mDrawerLayout.closeDrawer(navigationView);
                                                                         return true;
                                                                     case R.id.registeredslips:
                                                                         setTitle(getResources().getString(R.string.registeredslips));
                                                                         Fragment RegisteredSlipFragment = new RegisterSlipFragment();
                                                                         FragmentManager RegisteredSlipfragmentManager = getFragmentManager();
                                                                         RegisteredSlipfragmentManager.beginTransaction().replace(R.id.content_frame, RegisteredSlipFragment, "registeredslipfragment").addToBackStack( "homefragment" ).commit();
                                                                         mDrawerLayout.closeDrawer(navigationView);
                                                                         return true;
                                                                     case R.id.cashierblocking:
                                                                         setTitle(getResources().getString(R.string.cashierblocking));
                                                                         Fragment CashierBlockingFragment = new CashiersBlockingFragment();
                                                                         FragmentManager CashierBlockingfragmentManager = getFragmentManager();
                                                                         CashierBlockingfragmentManager.beginTransaction().replace(R.id.content_frame, CashierBlockingFragment, "cashierblockingFragment").addToBackStack( "homefragment" ).commit();
                                                                         mDrawerLayout.closeDrawer(navigationView);
                                                                         return true;
                                                                     case R.id.creditmanagement:
                                                                         setTitle(getResources().getString(R.string.creditmanagement));
                                                                         Fragment CreditManagementFragment = new FundTransferFragment();
                                                                         FragmentManager CreditManagementfragmentManager = getFragmentManager();
                                                                         CreditManagementfragmentManager.beginTransaction().replace(R.id.content_frame, CreditManagementFragment, "creditmanagementfragment").addToBackStack( "homefragment" ).commit();
                                                                         mDrawerLayout.closeDrawer(navigationView);
                                                                         return true;
                                                                     case R.id.logout:
                                                                         Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                                                                         startActivity(intent);




                                                                 }
                                                                 mDrawerLayout.closeDrawer(navigationView);
                                                                 return true;
                                                             }
                                                         }
        );


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_CALL) {

            PlaceBetFragment playfragment = (PlaceBetFragment)getFragmentManager().findFragmentById(R.id.content_frame);
            if (playfragment != null && playfragment.isVisible()) {
                playfragment.myOnKeyDown(keyCode);
            }
            //((PlaceBetFragment).get(0)).myOnKeyDown(keyCode);
//            ((EventListFragment)fragments.get(1)).myOnKeyDown(keyCode);

            //and so on...
        }
        return super.onKeyDown(keyCode, event);
    }


    public void initialiseControls() {

        try {
            gson = new Gson();
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.navview);
            walletbalTV = (TextView) findViewById(R.id.walletbalanceTV);
            mbetslipButton = (Button) findViewById(R.id.betslipbutton);

            Gson gson = new Gson();
            Type type = new TypeToken<Customer>() {
            }.getType();
            Customer cust = ((MyApplication) getApplication()).getCurrentCustomer();

            if (cust.getCustomertype() != 3) {
                navigationView.getMenu().findItem(R.id.creditmanagement).setVisible(false);
                navigationView.getMenu().findItem(R.id.cashierblocking).setVisible(false);

            }


            Fragment fragment = new menuFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }




    /* Called whenever we call invalidateOptionsMenu() */


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons

        return super.onOptionsItemSelected(item);
    }




    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */



}