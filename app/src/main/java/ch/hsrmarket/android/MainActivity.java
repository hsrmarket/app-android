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
import ch.hsrmarket.android.model.Account;
import ch.hsrmarket.android.model.Article;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;

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

            CategoryFragment fragment = new CategoryFragment();
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

        View navHead = navigationView.getHeaderView(0);
        TextView navName = (TextView) navHead.findViewById(R.id.nav_name);
        TextView navEmail = (TextView) navHead.findViewById(R.id.nav_email);

        if(accountJson.length() == 0){
            navigationView.inflateMenu(R.menu.drawer_logged_out);

            navName.setText("");
            navEmail.setText("");
        }else {
            navigationView.inflateMenu(R.menu.drawer_logged_in);

            Account account = Account.makeAccount(accountJson);
            navName.setText(account.getFirstName()+ " " +account.getLastName());
            navEmail.setText(account.getEmail());
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

        if (id == R.id.nav_login) {
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));

        }else if(id == R.id.nav_logout){
            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_credentials),Context.MODE_PRIVATE);
            sharedPref.edit().clear().commit();

            onStart();
        }

        drawer.closeDrawer(GravityCompat.START);
        return false;

    }
}
