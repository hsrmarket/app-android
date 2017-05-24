package ch.hsrmarket.android;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import ch.hsrmarket.android.adapter.ViewPagerAdapter;

public class ArticleActivity extends AppCompatActivity{

    public static final int DISPLAY_WITH_BUY = 13;
    public static final int DISPLAY_ONLY = 21;
    public static final int DISPLAY_PURCHASE = 34;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle = getIntent().getExtras();
        int chosenDisplay = bundle.getInt(getString(R.string.article_display_mode),-1);

        switch (chosenDisplay){
            case DISPLAY_ONLY:
            case DISPLAY_WITH_BUY:
            case DISPLAY_PURCHASE:
                ArticleFragment fragment = new ArticleFragment();
                fragment.setArguments(bundle);

                adapter.addFragment(fragment,getString(R.string.tab_article));
                break;

        }

        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
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
