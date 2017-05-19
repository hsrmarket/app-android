package ch.hsrmarket.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ch.hsrmarket.android.model.Account;

public class MyListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private int appointedMyList;
    private int accountId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.inflateMenu(R.menu.drawer_logged_in);

        int receivedMyList = getIntent().getIntExtra(getString(R.string.appointed_mylist),-1);
        navigationView.setCheckedItem(receivedMyList);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_credentials), Context.MODE_PRIVATE);
        String accountJson = sharedPref.getString(getString(R.string.secret_account),"");

        if(accountJson.length() == 0){
            finish();
        }else {
            Account currentAccount = Account.makeAccount(accountJson);
            setHeaderTexts(currentAccount.getFullName(), currentAccount.getEmail());
            accountId = currentAccount.getId();
        }

        setFragment(receivedMyList);
    }

    private void setFragment(int myId){
        if(appointedMyList != myId){
            FragmentManager fragmentManager = getSupportFragmentManager();

            Bundle bundle = new Bundle();
            bundle.putInt(getString(R.string.appointed_mylist),myId);
            bundle.putInt(getString(R.string.account_pass_id),accountId);
            bundle.putInt(getString(R.string.request_origin), ListFragment.ORIGIN_MY_LIST);

            ListFragment fragment = new ListFragment();
            fragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.my_list_layout,fragment);
            fragmentTransaction.commit();

            setTitle(getTitle(myId));
            appointedMyList = myId;
        }
    }

    //TODO back to old fragment
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean retVal = false;

        switch (id){
            case R.id.nav_logout:
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_credentials),Context.MODE_PRIVATE);
                sharedPref.edit().clear().commit();

                finish();
                break;

            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;

            case R.id.nav_articles:
                setFragment(R.id.nav_articles);
                retVal = true;
                break;

            case R.id.nav_sales:
                setFragment(R.id.nav_sales);
                retVal = true;
                break;

            case R.id.nav_purchases:
                setFragment(R.id.nav_purchases);
                retVal = true;
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return retVal;
    }

    private String getTitle(int myId){
        switch (myId){
            case R.id.nav_articles:
                return getString(R.string.title_articles);

            case R.id.nav_sales:
                return getString(R.string.title_sales);

            case R.id.nav_purchases:
                return getString(R.string.title_purchases);

            default:
                throw new AssertionError("Forgot to implement");
        }
    }

    private void setHeaderTexts(String name, String email){
        View navHead = navigationView.getHeaderView(0);
        TextView navName = (TextView) navHead.findViewById(R.id.nav_name);
        TextView navEmail = (TextView) navHead.findViewById(R.id.nav_email);

        navName.setText(name);
        navEmail.setText(email);
    }

}
