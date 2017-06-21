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
public class RunnerUpFragment extends BaseGloryPageFragment {

    private int groupMode;

    private Fragment ftCurrent;
    private SeqRunnerupListFragment ftSeq;
    private ExpandRunnerupListFragment ftLevel;
    private ExpandRunnerupListFragment ftYear;
    private ExpandRunnerupListFragment ftCourt;

    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_parent_container;
    }

    @Override
    protected void onCreate(View view) {
        groupMode = -1;
        groupBy(SettingProperty.getGloryRunnerupGroupMode(getContext()));
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

        SettingProperty.setGloryRunnerupGroupMode(getActivity(), mode);
        groupMode = mode;

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        if (ftCurrent != null) {
            ft.hide(ftCurrent);
        }

        if (mode == Constants.GROUP_BY_LEVEL) {
            if (ftLevel == null) {
                ftLevel = new ExpandRunnerupListFragment();
                ftLevel.setGroupMode(mode);
            }
            ft.replace(R.id.group_ft_container, ftLevel, "ExpandRunnerupListFragment-level");
            ftCurrent = ftLevel;
        }
        else if (mode == Constants.GROUP_BY_YEAR) {
            if (ftYear == null) {
                ftYear = new ExpandRunnerupListFragment();
                ftYear.setGroupMode(mode);
            }
            ft.replace(R.id.group_ft_container, ftYear, "ExpandRunnerupListFragment-year");
            ftCurrent = ftYear;
        }
        else if (mode == Constants.GROUP_BY_COURT) {
            if (ftCourt == null) {
                ftCourt = new ExpandRunnerupListFragment();
                ftCourt.setGroupMode(mode);
            }
            ft.replace(R.id.group_ft_container, ftCourt, "ExpandRunnerupListFragment-court");
            ftCurrent = ftCourt;
        }
        else {
            if (ftSeq == null) {
                ftSeq = new SeqRunnerupListFragment();
                ftSeq.setGroupMode(mode);
            }
            ft.replace(R.id.group_ft_container, ftSeq, "SeqRunnerupListFragment");
            ftCurrent = ftSeq;
        }
        ft.commit();
    }

}
