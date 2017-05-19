package ch.hsrmarket.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.hsrmarket.android.adapter.ViewPagerAdapter;
import ch.hsrmarket.android.api.ApiClient;
import ch.hsrmarket.android.model.Account;
import ch.hsrmarket.android.model.Article;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ApiClient.OnResponseListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Account currentAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ch.hsrmarket.android.R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        List<Article.Type> types = new ArrayList<>(Arrays.asList(Article.Type.values()));
        types.remove(Article.Type.UNKOWN);

        for(Article.Type t : types){
            Bundle bundle = new Bundle();
            bundle.putSerializable(getString(R.string.appointed_category),t);
            bundle.putInt(getString(R.string.request_origin), ListFragment.ORIGIN_CATEGORY);

            ListFragment fragment = new ListFragment();
            fragment.setArguments(bundle);

            adapter.addFragment(fragment,getTabName(t));
        }

        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        navigationView.getMenu().clear();
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_credentials), Context.MODE_PRIVATE);
        String accountJson = sharedPref.getString(getString(R.string.secret_account),"");

        if(accountJson.length() == 0){
            navigationView.inflateMenu(R.menu.drawer_logged_out);
            setHeaderTexts(getString(R.string.app_name),getString(R.string.app_url));
        }else {
            navigationView.inflateMenu(R.menu.drawer_logged_in);

            currentAccount = Account.makeAccount(accountJson);
            setHeaderTexts(currentAccount.getFullName(), currentAccount.getEmail());

            ApiClient apiClient = new ApiClient(getApplicationContext(),0,this,null);
            apiClient.getAccount(currentAccount.getId());
        }

        navigationView.setCheckedItem(R.id.nav_home);

    }

    private String getTabName(Article.Type type){
        switch (type){
            case BOOK:
                return getString(R.string.tab_books);
            case ELECTRONIC_DEVICE:
                return getString(R.string.tab_electronic_devices);
            case OFFICE_SUPPLY:
                return getString(R.string.tab_office_supplies);
            case OTHER:
                return getString(R.string.tab_others);
            default:
                return "Error";
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
        Intent myIntent = new Intent(getApplicationContext(),MyListActivity.class);

        switch (id){
            case R.id.nav_login:
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                break;

            case R.id.nav_logout:
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_credentials),Context.MODE_PRIVATE);
                sharedPref.edit().clear().commit();

                onStart();
                break;

            case R.id.nav_articles:
                myIntent.putExtra(getString(R.string.appointed_mylist),R.id.nav_articles);
                startActivity(myIntent);
                break;

            case R.id.nav_sales:
                myIntent.putExtra(getString(R.string.appointed_mylist),R.id.nav_sales);
                startActivity(myIntent);
                break;

            case R.id.nav_purchases:
                myIntent.putExtra(getString(R.string.appointed_mylist),R.id.nav_purchases);
                startActivity(myIntent);
                break;
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

    @Override
    public void onDataLoaded(Object data, int requestCode) {
        Account newAccount = (Account) data;

        if(!currentAccount.equals(newAccount)){
            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_credentials),Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString(getString(R.string.secret_account), newAccount.getJsonObject());
            editor.commit();

            setHeaderTexts(newAccount.getFullName(),newAccount.getEmail());
        }

    }
}
