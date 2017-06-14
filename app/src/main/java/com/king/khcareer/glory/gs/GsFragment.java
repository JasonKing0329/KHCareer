package com.king.khcareer.glory.gs;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.king.khcareer.glory.BaseGloryPageFragment;
import com.king.khcareer.glory.title.OnRecordItemListener;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.mytennis.view.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/1 18:45
 */
public class GsFragment extends BaseGloryPageFragment implements OnRecordItemListener {

    @BindView(R.id.rv_gs_list)
    RecyclerView rvGsList;

    Unbinder unbinder;

    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_glory_gs;
    }

    @Override
    protected void onCreate(View view) {
        unbinder = ButterKnife.bind(this, view);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvGsList.setLayoutManager(manager);

        GsYearAdapter adapter = new GsYearAdapter(gloryHolder.getGloryTitle().getGsItemList());
        adapter.setOnRecordItemListener(this);
        rvGsList.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClickRecord(Record record) {
        showGloryMatchDialog(record);
    }
}
