package com.king.khcareer.record.k4;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.king.khcareer.base.BaseActivity;
import com.king.khcareer.base.CustomDialog;
import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.common.image.glide.GlideOptions;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.common.viewsys.DefaultDialogManager;
import com.king.khcareer.match.page.MatchPageActivity;
import com.king.khcareer.model.sql.player.H2HDAOList;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.player.page.PlayerPageActivity;
import com.king.khcareer.record.DetailsDialog;
import com.king.khcareer.record.RecordFilterDialog;
import com.king.khcareer.record.RecordService;
import com.king.khcareer.record.detail.DetailGallery;
import com.king.khcareer.record.editor.RecordEditorActivity;
import com.king.mytennis.view.R;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceAlignmentEnum;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/20 16:17
 */
public class RecordActivity extends BaseActivity implements IRecordView, OnItemMenuListener, OnHeadLongClickListener
    , OnBMClickListener {

    private final int REQUEST_UPDATE = 100;

    @BindView(R.id.rv_record)
    RecyclerView rvRecord;
    @BindView(R.id.iv_record_head)
    ImageView ivRecordHead;
    @BindView(R.id.tv_career_winlose)
    TextView tvCareerWinlose;
    @BindView(R.id.tv_career_rate)
    TextView tvCareerRate;
    @BindView(R.id.tv_year_winlose)
    TextView tvYearWinlose;
    @BindView(R.id.tv_year_rate)
    TextView tvYearRate;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ctl_toolbar)
    CollapsingToolbarLayout ctlToolbar;
    @BindView(R.id.bmb_menu)
    BoomMenuButton bmbMenu;

    private RecordAdapter recordAdapter;

    private RecordPageData recordPageData;

    private RecordPresenter recordPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);

        initToolbar();
        initBoomButton();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRecord.setLayoutManager(manager);

        recordPresenter = new RecordPresenter(this);
        recordPresenter.loadRecordDatas();
    }

    private void initToolbar() {
        toolbar.setTitle(MultiUserManager.getInstance().getCurrentUser().getFullName());
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 加入了转场动画，必须用onBackPressed，finish无效果
                onBackPressed();
            }
        });
    }

    private void initBoomButton() {
        // 修改了源码，image自适应为button的一半中间，不需要再设置imagePadding了
//        int padding = bmbMenu.getContext().getResources().getDimensionPixelSize(R.dimen.boom_menu_icon_padding);
        int radius = bmbMenu.getContext().getResources().getDimensionPixelSize(R.dimen.boom_menu_btn_radius);
        bmbMenu.setButtonEnum(ButtonEnum.SimpleCircle);
        bmbMenu.setButtonRadius(radius);
        bmbMenu.setPiecePlaceEnum(PiecePlaceEnum.DOT_3_1);
        bmbMenu.setButtonPlaceEnum(ButtonPlaceEnum.Vertical);
        bmbMenu.setButtonPlaceAlignmentEnum(ButtonPlaceAlignmentEnum.BR);
        bmbMenu.setButtonRightMargin(bmbMenu.getContext().getResources().getDimensionPixelSize(R.dimen.home_pop_menu_right));
        bmbMenu.setButtonBottomMargin(bmbMenu.getContext().getResources().getDimensionPixelSize(R.dimen.home_pop_menu_bottom));
        bmbMenu.setButtonVerticalMargin(bmbMenu.getContext().getResources().getDimensionPixelSize(R.dimen.boom_menu_btn_margin_ver));
        bmbMenu.addBuilder(new SimpleCircleButton.Builder()
                .normalImageRes(R.drawable.ic_search_white_24dp)
                .buttonRadius(radius)
//                .imagePadding(new Rect(padding, padding, padding, padding))
                .listener(this));
        bmbMenu.addBuilder(new SimpleCircleButton.Builder()
                .normalImageRes(R.drawable.ic_refresh_white_24dp)
                .buttonRadius(radius)
                .listener(this));
        bmbMenu.addBuilder(new SimpleCircleButton.Builder()
                .normalImageRes(R.drawable.ic_arrow_upward_white_24dp)
                .buttonRadius(radius)
                .listener(this));
    }

    @Override
    public void onRecordDataLoaded(RecordPageData data) {

        recordPageData = data;

        tvCareerRate.setText(data.getCareerRate());
        tvCareerWinlose.setText("Win " + data.getCareerWin() + "   Lose " + data.getCareerLose());
        tvYearRate.setText(data.getYearRate());
        tvYearWinlose.setText("Win " + data.getYearWin() + "   Lose " + data.getYearLose());

        // year默认展开
        List<YearItem> yearList = data.getYearList();
        for (YearItem item:yearList) {
            item.setExpanded(true);
        }

        if (recordAdapter == null) {
            recordAdapter = new RecordAdapter(data.getYearList(), this, this);
            rvRecord.setAdapter(recordAdapter);
        } else {
            recordAdapter.updateData(data.getYearList());
        }
        Record record = data.getYearList().get(0).getChildItemList().get(0).getRecord();
        String path = ImageFactory.getMatchHeadPath(record.getMatch(), record.getCourt());

        Glide.with(this)
                .load(path)
                .apply(GlideOptions.getEditorMatchOptions())
                .into(ivRecordHead);

        // expand the first item, it's 1, not 0
        recordAdapter.expandParent(1);
    }

    @Override
    public void onUpdateRecord(final RecordItem recordItem) {
        Intent intent = new Intent().setClass(this, RecordEditorActivity.class);
        intent.putExtra(RecordEditorActivity.KEY_RECORD_ID, String.valueOf(recordItem.getRecord().getId()));
        startActivityForResult(intent, REQUEST_UPDATE);
    }

    private void showSearchDialog() {
        RecordFilterDialog dialog = new RecordFilterDialog(this, new CustomDialog.OnCustomDialogActionListener() {
            @Override
            public boolean onSave(Object object) {
                List<Record> list = (List<Record>) object;
                recordPresenter.loadRecordDatas((ArrayList<Record>) list);
                return true;
            }

            @Override
            public boolean onCancel() {
                return true;
            }

            @Override
            public void onLoadData(HashMap<String, Object> data) {
                data.put("data", recordPageData.getRecordList());
            }
        });
        dialog.setTitle(getString(R.string.filter_title));
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        if (recordPresenter != null) {
            recordPresenter.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onDeleteRecord(final RecordItem record) {
        new DefaultDialogManager().showWarningActionDialog(this, getString(R.string.delete_confirm)
                , getString(R.string.ok), null, getString(R.string.cancel)
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case DialogInterface.BUTTON_POSITIVE:
                                doDelete(record);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                });
    }

    private void doDelete(RecordItem record) {
        RecordService service = new RecordService();
        // delete from database
        service.delete(record.getRecord());
        // delete from list
        // 通过调试发现，itemPosition是在整个recycler view里的position，框架对根据position做的3级显示，整个position都是顺序排列的
        recordAdapter.removedItem(record.getItemPosition());
    }

    @Override
    public void onAllDetail(RecordItem record) {
        Intent intent = new Intent();
        intent.setClass(this, DetailGallery.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("record", record.getRecord());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onListDetail(RecordItem record) {
        DetailsDialog dlg = new DetailsDialog(this, record.getRecord(), new H2HDAOList(recordPageData.getRecordList(), record.getRecord().getCompetitor()));
        dlg.show();
    }

    @Override
    public void onItemClicked(View view, RecordItem recordItem) {
        Intent intent = new Intent();
        intent.setClass(this, PlayerPageActivity.class);
        intent.putExtra(PlayerPageActivity.KEY_COMPETITOR_NAME, recordItem.getRecord().getCompetitor());
        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this
                , Pair.create(view.findViewById(R.id.iv_player),getString(R.string.anim_player_page_head)));
        startActivity(intent, transitionActivityOptions.toBundle());
    }

    @Override
    public void onBoomButtonClick(int index) {
        switch (index) {
            case 0:
                showSearchDialog();
                break;
            case 1:
                recordPresenter.loadRecordDatas();
                break;
            case 2:
                rvRecord.smoothScrollToPosition(0);
                break;
        }
    }

    @Override
    public void onLongClickHead(View view, HeaderItem item) {
        Intent intent = new Intent();
        intent.setClass(this, MatchPageActivity.class);
        intent.putExtra(MatchPageActivity.KEY_MATCH_NAME, item.getRecord().getMatch());
        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this
                , Pair.create(view.findViewById(R.id.iv_match),getString(R.string.anim_match_page_head)));
        startActivity(intent, transitionActivityOptions.toBundle());
    }
}
