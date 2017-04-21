package com.king.khcareer.record.k4;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.king.khcareer.base.BaseActivity;
import com.king.mytennis.view.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/20 16:17
 */
public class RecordActivity extends BaseActivity {

    @BindView(R.id.rv_record)
    RecyclerView rvRecord;

    private RecordAdapter recordAdapter;

    private List<HeaderItem> headerList;

    private RecordPresenter recordPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);

        recordPresenter = new RecordPresenter();
        recordPresenter.loadRecordDatas();
    }
}
