package ch.hsrmarket.android;

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

            adapter.addFragment(fragment,t);
        }

        viewPager.setAdapter(adapter);
    }
}
