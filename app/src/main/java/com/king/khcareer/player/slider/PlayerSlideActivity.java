package com.king.khcareer.player.slider;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.king.khcareer.base.BaseActivity;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.match.page.MatchPageActivity;
import com.king.khcareer.model.sql.player.bean.H2hParentBean;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;
import com.king.khcareer.player.page.PageRecordAdapter;
import com.king.khcareer.player.page.PlayerPageActivity;
import com.king.khcareer.pubview.cardslider.CardSliderLayoutManager;
import com.king.khcareer.pubview.cardslider.CardSnapHelper;
import com.king.khcareer.utils.ConstellationUtil;
import com.king.mytennis.view.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/11/24 14:53
 */
public class PlayerSlideActivity extends BaseActivity implements ISlideView {

    @BindView(R.id.tv_name_chn1)
    TextView tvNameChn1;
    @BindView(R.id.tv_name_chn2)
    TextView tvNameChn2;
    @BindView(R.id.ts_h2h)
    TextSwitcher tsH2h;
    @BindView(R.id.rv_players)
    RecyclerView rvPlayers;
    @BindView(R.id.rv_records)
    RecyclerView rvRecords;
    @BindView(R.id.ts_name_eng)
    TextSwitcher tsNameEng;
    @BindView(R.id.ts_place)
    TextSwitcher tsPlace;
    @BindView(R.id.ts_birthday)
    TextSwitcher tsBirthday;

    private PlayerSlideAdapter playerSlideAdapter;

    private SlidePresenter presenter;

    private int currentPosition;
    private int countryOffset1;
    private int countryOffset2;
    private long countryAnimDuration;
    
    private List<PlayerBean> playerList;

    private PageRecordAdapter recordAdapter;

    @Override
    protected boolean applyCommonTheme() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_slide);
        ButterKnife.bind(this);

        initRecyclerView();
        initCountryText();
        initSwitchers();
        presenter = new SlidePresenter(this);
        presenter.loadPlayers();
    }

    private void initRecyclerView() {
        rvPlayers.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    onActiveCardChange();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRecords.setLayoutManager(manager);
    }

    @Override
    public void onPlayerLoaded(List<PlayerBean> playerList) {

        this.playerList = playerList;

        if (playerSlideAdapter == null) {
            playerSlideAdapter = new PlayerSlideAdapter();
            playerSlideAdapter.setList(playerList);
            playerSlideAdapter.setOnPlayerItemListener(new PlayerSlideAdapter.OnPlayerItemListener() {
                @Override
                public void onClickPlayer(PlayerBean bean, int position) {
                    clickPlayer(position);
                }
            });
            rvPlayers.setAdapter(playerSlideAdapter);
            new CardSnapHelper().attachToRecyclerView(rvPlayers);
            onActiveCardChange(0);
        }
        else {
            playerSlideAdapter.setList(playerList);
            playerSlideAdapter.notifyDataSetChanged();
        }
    }

    private void clickPlayer(int position) {
        CardSliderLayoutManager lm =  (CardSliderLayoutManager) rvPlayers.getLayoutManager();

        if (lm.isSmoothScrolling()) {
            return;
        }

        final int activeCardPosition = lm.getActiveCardPosition();
        if (activeCardPosition == RecyclerView.NO_POSITION) {
            return;
        }

        if (position == activeCardPosition) {
            Intent intent = new Intent(PlayerSlideActivity.this, PlayerPageActivity.class);
            intent.putExtra(PlayerPageActivity.KEY_COMPETITOR_NAME, playerList.get(position).getNameChn());
            startActivity(intent);
        } else if (position > activeCardPosition) {
            rvPlayers.smoothScrollToPosition(position);
            onActiveCardChange(position);
        }
    }

    @Override
    public void onPlayerLoadFailed(String message) {
        showConfirmMessage(message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
    }

    @Override
    public void onRecordLoaded(List<Object> list) {
        if (recordAdapter == null) {
            recordAdapter = new PageRecordAdapter(MultiUserManager.getInstance().getCurrentUser(), list);
            recordAdapter.setOnItemClickListener(new PageRecordAdapter.OnItemClickListener() {
                @Override
                public void onClickRecord(Record record) {
                    Intent intent = new Intent(PlayerSlideActivity.this, MatchPageActivity.class);
                    intent.putExtra(MatchPageActivity.KEY_MATCH_NAME, record.getMatch());
                    startActivity(intent);
                }

                @Override
                public void onLongClickRecord(View view, Record record) {

                }
            });
            rvRecords.setAdapter(recordAdapter);
        }
        else {
            recordAdapter.setList(list);
            recordAdapter.notifyDataSetChanged();
        }
        rvRecords.scrollToPosition(0);
    }

    @OnClick({R.id.iv_back})
    public void onBack() {
        onBackPressed();
    }

    private void initSwitchers() {
        tsH2h.setFactory(new TextViewFactory(R.style.TemperatureTextView, true));
        
        tsNameEng.setFactory(new TextViewFactory(R.style.PlaceTextView, false));

        tsBirthday.setFactory(new TextViewFactory(R.style.ClockTextView, false));

        tsPlace.setInAnimation(this, android.R.anim.fade_in);
        tsPlace.setOutAnimation(this, android.R.anim.fade_out);
        tsPlace.setFactory(new TextViewFactory(R.style.DescriptionTextView, false));

    }

    private void initCountryText() {
        countryAnimDuration = getResources().getInteger(R.integer.labels_animation_duration);
        countryOffset1 = getResources().getDimensionPixelSize(R.dimen.player_slide_left_offset);
        countryOffset2 = getResources().getDimensionPixelSize(R.dimen.player_slide_left_offset2);

        tvNameChn1.setX(countryOffset1);
        tvNameChn2.setX(countryOffset2);
        tvNameChn2.setAlpha(0f);

//        tvNameChn1.setTypeface(Typeface.createFromAsset(getAssets(), "open-sans-extrabold.ttf"));
//        tvNameChn2.setTypeface(Typeface.createFromAsset(getAssets(), "open-sans-extrabold.ttf"));
    }

    private void setChnName(String text, boolean left2right) {
        final TextView invisibleText;
        final TextView visibleText;
        if (tvNameChn1.getAlpha() > tvNameChn2.getAlpha()) {
            visibleText = tvNameChn1;
            invisibleText = tvNameChn2;
        } else {
            visibleText = tvNameChn2;
            invisibleText = tvNameChn1;
        }

        final int vOffset;
        if (left2right) {
            invisibleText.setX(0);
            vOffset = countryOffset2;
        } else {
            invisibleText.setX(countryOffset2);
            vOffset = 0;
        }

        invisibleText.setText(text);

        final ObjectAnimator iAlpha = ObjectAnimator.ofFloat(invisibleText, "alpha", 1f);
        final ObjectAnimator vAlpha = ObjectAnimator.ofFloat(visibleText, "alpha", 0f);
        final ObjectAnimator iX = ObjectAnimator.ofFloat(invisibleText, "x", countryOffset1);
        final ObjectAnimator vX = ObjectAnimator.ofFloat(visibleText, "x", vOffset);

        final AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(iAlpha, vAlpha, iX, vX);
        animSet.setDuration(countryAnimDuration);
        animSet.start();
    }

    private void onActiveCardChange() {
        final int pos = ((CardSliderLayoutManager) rvPlayers.getLayoutManager()).getActiveCardPosition();
        if (pos == RecyclerView.NO_POSITION || pos == currentPosition) {
            return;
        }

        onActiveCardChange(pos);
    }

    private void onActiveCardChange(int pos) {
        int animH[] = new int[] {R.anim.slide_in_right, R.anim.slide_out_left};
        int animV[] = new int[] {R.anim.slide_in_top, R.anim.slide_out_bottom};

        final boolean left2right = pos < currentPosition;
        if (left2right) {
            animH[0] = R.anim.slide_in_left;
            animH[1] = R.anim.slide_out_right;

            animV[0] = R.anim.slide_in_bottom;
            animV[1] = R.anim.slide_out_top;
        }

        PlayerBean bean = playerList.get(pos);
        setChnName(bean.getNameChn(), left2right);

        tsH2h.setInAnimation(this, animH[0]);
        tsH2h.setOutAnimation(this, animH[1]);
        H2hParentBean h2h = presenter.queryH2h(bean);
        tsH2h.setText(h2h.getWin() + " - " + h2h.getLose());

        tsNameEng.setInAnimation(this, animV[0]);
        tsNameEng.setOutAnimation(this, animV[1]);
        tsNameEng.setText(bean.getNameEng());

        tsBirthday.setInAnimation(this, animV[0]);
        tsBirthday.setOutAnimation(this, animV[1]);
        try {
            tsBirthday.setText(bean.getBirthday() + "   " + ConstellationUtil.getConstellationChn(bean.getBirthday()));
        } catch (ConstellationUtil.ConstellationParseException e) {
            e.printStackTrace();
            tsBirthday.setText(bean.getBirthday());
        }

        tsPlace.setText(bean.getCountry());

        currentPosition = pos;

        presenter.loadRecords(bean.getNameChn());
    }

    private class TextViewFactory implements  ViewSwitcher.ViewFactory {

        @StyleRes
        final int styleId;
        final boolean center;

        TextViewFactory(@StyleRes int styleId, boolean center) {
            this.styleId = styleId;
            this.center = center;
        }

        @SuppressWarnings("deprecation")
        @Override
        public View makeView() {
            final TextView textView = new TextView(PlayerSlideActivity.this);

            if (center) {
                textView.setGravity(Gravity.CENTER);
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                textView.setTextAppearance(PlayerSlideActivity.this, styleId);
            } else {
                textView.setTextAppearance(styleId);
            }

            return textView;
        }

    }

}
