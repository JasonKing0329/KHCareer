package com.king.khcareer.player.page;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: FragmentPagerAdapter of PageFragment
 * <p/>作者：景阳
 * <p/>创建时间: 2017/11/20 16:09
 */
public class PageAdapter extends FragmentPagerAdapter {

    private List<PageFragment> ftList;

    public PageAdapter(FragmentManager fm) {
        super(fm);
        ftList = new ArrayList<>();
    }

    public void addFragment(PageFragment fragment) {
        ftList.add(fragment);
    }

    @Override
    public PageFragment getItem(int position) {
        return ftList.get(position);
    }

    @Override
    public int getCount() {
        return ftList == null ? 0:ftList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }
}
