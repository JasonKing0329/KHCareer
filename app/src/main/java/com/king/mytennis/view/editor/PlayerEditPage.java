package com.king.mytennis.view.editor;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.mytennis.model.ImageFactory;
import com.king.mytennis.model.Record;
import com.king.mytennis.multiuser.MultiUserManager;
import com.king.mytennis.pubdata.bean.PlayerBean;
import com.king.mytennis.service.ImageUtil;
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
    private TextView tvNameEng, tvBirthday;

    private PlayerBean playerBean;

    public PlayerEditPage(IEditorHolder holder) {
        editorHolder = holder;
    }

    public void initView() {

        groupPlayer = (ViewGroup) editorHolder.getActivity().findViewById(R.id.editor_player_group);
        et_rankp1 = (EditText) editorHolder.getActivity().findViewById(R.id.editor_player_rank1);
        et_seedp1 = (EditText) editorHolder.getActivity().findViewById(R.id.editor_player_seed1);
        tvUser = (TextView) editorHolder.getActivity().findViewById(R.id.editor_player_user_name);
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
        groupPlayer.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v == ivChangePlayer) {
            // 回调在onPlayerSelected
            editorHolder.selectPlayer();
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
        ImageUtil.load("file://" + ImageFactory.getPlayerHeadPath(bean.getNameChn()), ivPlayer, R.drawable.view7_folder_cover_player);
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
}
