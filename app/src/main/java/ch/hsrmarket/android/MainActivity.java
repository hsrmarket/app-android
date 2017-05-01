package ch.hsrmarket.android;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.hsrmarket.android.adapter.ViewPagerAdapter;
import ch.hsrmarket.android.model.Article;

public class MainActivity extends AppCompatActivity {

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
            bundle.putSerializable("appointedCategory",t);

            CategoryFragment fragment = new CategoryFragment();
            fragment.setArguments(bundle);

            adapter.addFragment(fragment,getTabName(t));
        }

        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
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
}
