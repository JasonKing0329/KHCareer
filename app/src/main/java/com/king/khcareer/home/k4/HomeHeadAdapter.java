package com.king.khcareer.home.k4;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/11/14 15:40
 */
public class HomeHeadAdapter extends FragmentPagerAdapter {

    private List<HomeHeadFragment> ftList;

    public HomeHeadAdapter(FragmentManager fm) {
        super(fm);
        ftList = new ArrayList<>();
    }

    public void addFragment(HomeHeadFragment fragment) {
        ftList.add(fragment);
    }

    @Override
    public HomeHeadFragment getItem(int position) {
        return ftList.get(position);
    }

    @Override
    public int getCount() {
        return ftList.size();
    }
}
