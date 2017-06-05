package com.king.khcareer.glory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/1 18:35
 */
public class GloryPageAdapter extends FragmentPagerAdapter {

    private List<BaseGloryPageFragment> fragmentList;
    private List<String> titleList;

    public GloryPageAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
    }

    public void addFragment(BaseGloryPageFragment fragment, String title) {
        fragmentList.add(fragment);
        titleList.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
