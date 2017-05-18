package ch.hsrmarket.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ch.hsrmarket.android.api.ApiClient;
import ch.hsrmarket.android.model.Account;

public class MyListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
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

        //TODO receive a parameter
        //TODO set accordingly right menu
        navigationView.setCheckedItem(R.id.nav_articles);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_credentials), Context.MODE_PRIVATE);
        String accountJson = sharedPref.getString(getString(R.string.secret_account),"");

        if(accountJson.length() == 0){
            finish();
        }else {
            Account currentAccount = Account.makeAccount(accountJson);
            setHeaderTexts(currentAccount.getFullName(), currentAccount.getEmail());
        }

    }

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

        if(id == R.id.nav_logout){
            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_credentials),Context.MODE_PRIVATE);
            sharedPref.edit().clear().commit();

            finish();
        }else if(id == R.id.nav_home){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        drawer.closeDrawer(GravityCompat.START);
        return false;

    }

    private void setHeaderTexts(String name, String email){
        View navHead = navigationView.getHeaderView(0);
        TextView navName = (TextView) navHead.findViewById(R.id.nav_name);
        TextView navEmail = (TextView) navHead.findViewById(R.id.nav_email);

        navName.setText(name);
        navEmail.setText(email);
    }

}
