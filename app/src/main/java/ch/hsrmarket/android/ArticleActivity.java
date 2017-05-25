package ch.hsrmarket.android;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import ch.hsrmarket.android.adapter.ViewPagerAdapter;

public class ArticleActivity extends AppCompatActivity{

    public static final int DISPLAY_WITH_BUY = 13;
    public static final int DISPLAY_ONLY = 21;
    public static final int DISPLAY_PURCHASE = 34;
    public static final int DISPLAY_SALE = 35;
    public static final int DISPLAY_ADD = 55;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle = getIntent().getExtras();
        int displayMode = bundle.getInt(getString(R.string.article_display_mode),-1);
        int accountId = bundle.getInt(getString(R.string.pass_account_id),-1);

        switch (displayMode){
            case DISPLAY_PURCHASE:
                adapter.addFragment(getArticleFragment(bundle),getString(R.string.tab_article));
                adapter.addFragment(getAccountFragment(accountId),getString(R.string.tab_seller));
                break;

            case DISPLAY_SALE:
                adapter.addFragment(getArticleFragment(bundle),getString(R.string.tab_article));
                adapter.addFragment(getAccountFragment(accountId),getString(R.string.tab_buyer));
                break;

            case DISPLAY_ONLY:
            case DISPLAY_WITH_BUY:
            case DISPLAY_ADD:
                adapter.addFragment(getArticleFragment(bundle),getString(R.string.tab_article));
                break;

        }

        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if(displayMode != DISPLAY_PURCHASE && displayMode != DISPLAY_SALE) {
            tabLayout.setVisibility(View.GONE);
        }
    }

    private Fragment getArticleFragment(Bundle bundle){
        ArticleFragment fragment = new ArticleFragment();
        fragment.setArguments(bundle);
        return  fragment;
    }


    private Fragment getAccountFragment(int accountId){
        AccountFragment fragment = new AccountFragment();

        Bundle args = new Bundle();
        args.putInt(getString(R.string.account_display_mode),AccountFragment.DISPLAY_ONLY);
        args.putInt(getString(R.string.pass_account_id),accountId);

        fragment.setArguments(args);
        return  fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return(super.onOptionsItemSelected(item));
    }

}
