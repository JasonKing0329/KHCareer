package com.king.khcareer.record.editor;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.king.khcareer.base.KApplication;
import com.king.khcareer.common.image.glide.GlideOptions;
import com.king.khcareer.model.sql.player.interfc.H2HDAO;
import com.king.khcareer.model.sql.player.H2HDAODB;
import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;
import com.king.khcareer.player.page.PlayerPageActivity;
import com.king.mytennis.view.R;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/13 13:22
 */
public class PlayerEditPage implements View.OnClickListener {

    private IEditorHolder editorHolder;
    private ImageView ivChangePlayer;
    private ImageView ivPlayer;
    private ViewGroup groupPlayer;

    protected EditText et_rankp1, et_seedp1, et_rank, et_seed;
    private TextView tvUser;
    private TextView tvCompetitor;
    private TextView tvH2h;
    private TextView tvNameEng, tvBirthday;

    private PlayerBean playerBean;

    private H2HDAO h2HDAO;

    public PlayerEditPage(IEditorHolder holder) {
        editorHolder = holder;
    }

    public void initView() {

        groupPlayer = (ViewGroup) editorHolder.getActivity().findViewById(R.id.editor_player_group);
        et_rankp1 = (EditText) editorHolder.getActivity().findViewById(R.id.editor_player_rank1);
        et_seedp1 = (EditText) editorHolder.getActivity().findViewById(R.id.editor_player_seed1);
        tvUser = (TextView) editorHolder.getActivity().findViewById(R.id.editor_player_user_name);
        tvH2h = (TextView) editorHolder.getActivity().findViewById(R.id.editor_player_h2h);
        tvH2h.setOnClickListener(this);
        tvCompetitor = (TextView) editorHolder.getActivity().findViewById(R.id.editor_player_name);
        tvNameEng = (TextView) editorHolder.getActivity().findViewById(R.id.editor_player_name_eng);
        tvBirthday = (TextView) editorHolder.getActivity().findViewById(R.id.editor_player_birthday);
        et_rank = (EditText) editorHolder.getActivity().findViewById(R.id.editor_player_rank2);
        et_seed = (EditText) editorHolder.getActivity().findViewById(R.id.editor_player_seed2);
        ivPlayer = (ImageView) editorHolder.getActivity().findViewById(R.id.editor_player_image);
        ivChangePlayer = (ImageView) editorHolder.getActivity().findViewById(R.id.edit_player_change);
        ivChangePlayer.setOnClickListener(this);

        tvUser.setText(MultiUserManager.getInstance().getCurrentUser().getDisplayName());
    }

    public void reset() {
        et_rankp1.setText("");
        et_seedp1.setText("");
        et_rank.setText("");
        et_seed.setText("");
        tvCompetitor.setText("");
        tvBirthday.setText("");
        tvNameEng.setText("");
        tvH2h.setText("H2H");
        groupPlayer.setVisibility(View.GONE);
        playerBean = null;
    }

    @Override
    public void onClick(View v) {
        if (v == ivChangePlayer) {
            // 回调在onPlayerSelected
            editorHolder.selectPlayer();
        }
        else if (v == tvH2h) {
            showH2hDetails();
        }
    }

    private void showH2hDetails() {
        if (playerBean != null) {
            Intent intent = new Intent(editorHolder.getActivity(), PlayerPageActivity.class);
            intent.putExtra(PlayerPageActivity.KEY_COMPETITOR_NAME, playerBean.getNameChn());
            editorHolder.getActivity().startActivity(intent);
        }
    }

    public String getCompetitor() {
        return tvCompetitor.getText().toString();
    }

    /**
     * selectPlayer的回调
     * @param bean
     */
    public void onPlayerSelected(PlayerBean bean) {
        playerBean = bean;
        groupPlayer.setVisibility(View.VISIBLE);
        tvCompetitor.setText(bean.getNameChn().concat("(").concat(bean.getCountry()).concat(")"));
        tvBirthday.setText(bean.getBirthday());
        tvNameEng.setText(bean.getNameEng());
        Glide.with(KApplication.getInstance())
                .load(ImageFactory.getDetailPlayerPath(bean.getNameChn()))
                .apply(GlideOptions.getEditorPlayerOptions())
                .into(ivPlayer);

        h2HDAO = new H2HDAODB(bean.getNameChn());
        tvH2h.setText("H2H  " + h2HDAO.getWin() + "-" + h2HDAO.getLose());
    }

    public String fillRecord(Record record) {
        if (playerBean == null) {
            return editorHolder.getActivity().getString(R.string.editor_null_player);
        }
        int temp;
        try {
            temp=Integer.parseInt(et_rankp1.getText().toString());
        } catch (Exception e) {
            temp=0;
        }
        record.setRank(temp);
        try {
            temp=Integer.parseInt(et_seedp1.getText().toString());
        } catch (Exception e) {
            temp=0;
        }
        record.setSeed(temp);
        try {
            temp=Integer.parseInt(et_rank.getText().toString());
        } catch (Exception e) {
            temp=0;
        }
        record.setCptRank(temp);
        try {
            temp=Integer.parseInt(et_seed.getText().toString());
        } catch (Exception e) {
            temp=0;
        }
        record.setCptSeed(temp);
        record.setCompetitor(playerBean.getNameChn());
        record.setCptCountry(playerBean.getCountry());
        return null;
    }

    public void initWithRecord(Record record, PlayerBean player) {
        et_rankp1.setText(String.valueOf(record.getRank()));
        et_seedp1.setText(String.valueOf(record.getSeed()));
        et_rank.setText(String.valueOf(record.getCptRank()));
        et_seed.setText(String.valueOf(record.getCptSeed()));

        onPlayerSelected(player);
        fillRecord(record);
    }
}
