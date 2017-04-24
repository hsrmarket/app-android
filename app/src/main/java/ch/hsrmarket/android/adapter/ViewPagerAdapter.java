package ch.hsrmarket.android.adapter;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ch.hsrmarket.android.R;
import ch.hsrmarket.android.model.Article;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> titles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        titles.add(title);
    }

    public void addFragment(Fragment fragment, Article.Type type) {
        fragments.add(fragment);
        titles.add(getTabName(type));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    private String getTabName(Article.Type type){
        switch (type){
            case BOOK:
                return Resources.getSystem().getString(R.string.tab_books);
            case ELECTRONIC_DEVICE:
                return Resources.getSystem().getString(R.string.tab_electronic_devices);
            case OFFICE_SUPPLY:
                return Resources.getSystem().getString(R.string.tab_office_supplies);
            case OTHER:
                return Resources.getSystem().getString(R.string.tab_others);
            default:
                return "Error";
        }
    }
}