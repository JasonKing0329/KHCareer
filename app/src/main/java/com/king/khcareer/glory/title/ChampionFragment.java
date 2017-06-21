package com.king.khcareer.glory.title;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.king.khcareer.common.config.Constants;
import com.king.khcareer.glory.BaseGloryPageFragment;
import com.king.khcareer.settings.SettingProperty;
import com.king.mytennis.view.R;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/21 11:10
 */
public class ChampionFragment extends BaseGloryPageFragment {

    private int groupMode;

    private Fragment ftCurrent;
    private SeqChampionListFragment ftSeq;
    private ExpandChampionListFragment ftLevel;
    private ExpandChampionListFragment ftYear;
    private ExpandChampionListFragment ftCourt;

    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_parent_container;
    }

    @Override
    protected void onCreate(View view) {
        groupMode = -1;
        groupBy(SettingProperty.getGloryChampionGroupMode(getContext()));
    }

    @Override
    public void onDestroyView() {
        ftSeq = null;
        ftLevel = null;
        ftCourt = null;
        ftYear = null;
        ftCurrent = null;
        super.onDestroyView();
    }

    public void groupBy(int mode) {
        if (mode == groupMode) {
            return;
        }

        SettingProperty.setGloryChampionGroupMode(getActivity(), mode);
        groupMode = mode;

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        if (ftCurrent != null) {
            ft.hide(ftCurrent);
        }

        if (mode == Constants.GROUP_BY_LEVEL) {
            if (ftLevel == null) {
                ftLevel = new ExpandChampionListFragment();
                ftLevel.setGroupMode(mode);
            }
            ft.replace(R.id.group_ft_container, ftLevel, "ExpandChampionListFragment-level");
            ftCurrent = ftLevel;
        }
        else if (mode == Constants.GROUP_BY_YEAR) {
            if (ftYear == null) {
                ftYear = new ExpandChampionListFragment();
                ftYear.setGroupMode(mode);
            }
            ft.replace(R.id.group_ft_container, ftYear, "ExpandChampionListFragment-year");
            ftCurrent = ftYear;
        }
        else if (mode == Constants.GROUP_BY_COURT) {
            if (ftCourt == null) {
                ftCourt = new ExpandChampionListFragment();
                ftCourt.setGroupMode(mode);
            }
            ft.replace(R.id.group_ft_container, ftCourt, "ExpandChampionListFragment-court");
            ftCurrent = ftCourt;
        }
        else {
            if (ftSeq == null) {
                ftSeq = new SeqChampionListFragment();
                ftSeq.setGroupMode(mode);
            }
            ft.replace(R.id.group_ft_container, ftSeq, "SeqChampionListFragment");
            ftCurrent = ftSeq;
        }
        ft.commit();
    }

}
