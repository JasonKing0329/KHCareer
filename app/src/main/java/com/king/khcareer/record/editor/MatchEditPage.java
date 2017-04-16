package com.king.khcareer.record.editor;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.king.khcareer.common.config.Configuration;
import com.king.khcareer.common.config.Constants;
import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.model.sql.pubdata.PubDataProvider;
import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;
import com.king.khcareer.common.image.ImageUtil;
import com.king.khcareer.base.CustomDialog;
import com.king.mytennis.view.R;
import com.king.khcareer.settings.AutoFillItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/13 13:22
 */
public class MatchEditPage implements View.OnClickListener {

    private IEditorHolder editorHolder;

    private ImageView ivChangeMatch;
    private ImageView ivMatch;
    private ViewGroup groupMatch;
    private ViewGroup groupScore;
    private ViewGroup groupWinner;
    private String[] arr_round;
    private String[] arr_year;
    private TextView tvMatch , tvMatchCountry, tvMatchLevel, tvMatchCity, tvMatchCourt;
    private TextView tvWinner, tvScore;
    protected Spinner sp_year, sp_round;
    protected int cur_year = 2, cur_round = 0;// 记录当前spinner选项

    private SpinnerListener spinnerListener;

    private InputScoreDialog inputScoreDialog;
    private String mStrWinner;
    private String mStrScore;

    private MatchNameBean matchBean;

    public MatchEditPage(IEditorHolder holder) {
        editorHolder = holder;
        spinnerListener = new SpinnerListener();
        arr_round = Constants.RECORD_MATCH_ROUNDS;
        arr_year = new String[20];
        for (int n = 0; n < 20; n++) {
            arr_year[n] = "" + (n + 2010);
        }
    }

    public void initView() {

        ArrayAdapter<String> spinnerAdapter;

        groupMatch = (ViewGroup) editorHolder.getActivity().findViewById(R.id.editor_match_group);
        groupScore = (ViewGroup) editorHolder.getActivity().findViewById(R.id.editor_score_group);
        groupScore.setOnClickListener(this);
        groupWinner = (ViewGroup) editorHolder.getActivity().findViewById(R.id.editor_group_winner);
        tvMatch = (TextView) editorHolder.getActivity().findViewById(R.id.editor_match_name);
        tvMatchCountry = (TextView) editorHolder.getActivity().findViewById(R.id.editor_match_country);
        tvMatchCity = (TextView) editorHolder.getActivity().findViewById(R.id.editor_match_city);
        tvWinner = (TextView) editorHolder.getActivity().findViewById(R.id.editor_match_winner);
        tvScore = (TextView) editorHolder.getActivity().findViewById(R.id.editor_match_score);
        tvWinner.setOnClickListener(this);
        tvScore.setOnClickListener(this);
        ivChangeMatch = (ImageView) editorHolder.getActivity().findViewById(R.id.editor_match_change);
        ivChangeMatch.setOnClickListener(this);
        ivMatch = (ImageView) editorHolder.getActivity().findViewById(R.id.editor_match_image);
        tvMatchCourt = (TextView) editorHolder.getActivity().findViewById(R.id.editor_match_court);
        tvMatchLevel = (TextView) editorHolder.getActivity().findViewById(R.id.editor_match_level);
        sp_year = (Spinner) editorHolder.getActivity().findViewById(R.id.editor_match_year);
        sp_round = (Spinner) editorHolder.getActivity().findViewById(R.id.editor_match_round);

        spinnerAdapter = new ArrayAdapter<String>(editorHolder.getActivity(),
                android.R.layout.simple_spinner_item, arr_year);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_item);
        sp_year.setAdapter(spinnerAdapter);
        sp_year.setOnItemSelectedListener(spinnerListener);
        spinnerAdapter = new ArrayAdapter<String>(editorHolder.getActivity(),
                android.R.layout.simple_spinner_item, arr_round);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_round.setAdapter(spinnerAdapter);
        sp_round.setOnItemSelectedListener(spinnerListener);

        initData();
    }

    private void initData() {
        Configuration conf = Configuration.getInstance();
        conf.loadFromPreference(editorHolder.getActivity());
        String match = conf.autoFillItem.getMatch();
        if (TextUtils.isEmpty(match)) {
            return;
        }
        else {
            groupMatch.setVisibility(View.VISIBLE);
            // initialize match bean, otherwise it will warning null match when save
            matchBean = new PubDataProvider().getMatchByName(match);
        }
        cur_year = conf.index_year;
        sp_year.setSelection(cur_year);
        String court = conf.autoFillItem.getCourt();
        tvMatch.setText(match);
        tvMatchCountry.setText(conf.autoFillItem.getCountry());
        tvMatchCity.setText(conf.autoFillItem.getCity());
        tvMatchCourt.setText(court);
        tvMatchLevel.setText(conf.autoFillItem.getLevel());
        cur_round = getRoundIndex(conf.autoFillItem.getRound());
        sp_round.setSelection(cur_round);
        ImageUtil.load("file://" + ImageFactory.getMatchHeadPath(match, court), ivMatch);
    }

    /**
     * 已添加完一个记录后继续添加保留上次添加的赛事信息，清空winner和score
     */
    public void reset() {
        tvWinner.setText("");
        tvScore.setText("");
        mStrScore = "";
        mStrWinner = "";
        groupWinner.setVisibility(View.INVISIBLE);
    }

    private int getRoundIndex(String round) {
        for (int i = 0; i < arr_round.length; i ++) {
            if (arr_round[i].equals(round)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onClick(View v) {
        if (v == ivChangeMatch) {
            // 回调在onMatchSelected
            editorHolder.selectMatch();
        }
        else if (v == groupScore) {
            inputScoreDialog = new InputScoreDialog(editorHolder.getActivity(), new CustomDialog.OnCustomDialogActionListener() {
                @Override
                public boolean onSave(Object object) {
                    Map<String, String> map = (Map<String, String>) object;
                    mStrScore = map.get("score");
                    mStrWinner = map.get("winner");
                    tvScore.setText(mStrScore);
                    tvWinner.setText(mStrWinner);
                    groupWinner.setVisibility(View.VISIBLE);
                    return true;
                }

                @Override
                public boolean onCancel() {
                    return false;
                }

                @Override
                public void onLoadData(HashMap<String, Object> data) {
                    data.put("winner", mStrWinner);
                    data.put("score", mStrScore);
                    data.put("competitor", editorHolder.getCompetitor());
                }
            });
            inputScoreDialog.setTitle("Input score");
            inputScoreDialog.show();
        }
    }

    /**
     * selectMatch 回调
     * @param bean
     */
    public void onMatchSelected(MatchNameBean bean) {
        matchBean = bean;

        // 保存为默认填写
        AutoFillItem item = new AutoFillItem();
        item.setMatch(bean.getName());
        item.setCountry(bean.getMatchBean().getCountry());
        item.setCity(bean.getMatchBean().getCity());
        item.setLevel(bean.getMatchBean().getLevel());
        item.setCourt(bean.getMatchBean().getCourt());
        item.setRegion(bean.getMatchBean().getRegion());
        item.setMonth(bean.getMatchBean().getMonth());
        Configuration.getInstance().createPreference(editorHolder.getActivity(), item);

        groupMatch.setVisibility(View.VISIBLE);
        tvMatch.setText(bean.getName());
        tvMatchCountry.setText(bean.getMatchBean().getCountry());
        tvMatchLevel.setText(bean.getMatchBean().getLevel());
        tvMatchCourt.setText(bean.getMatchBean().getCourt());
        tvMatchCity.setText(bean.getMatchBean().getCity());
        ImageUtil.load("file://" + ImageFactory.getMatchHeadPath(bean.getName(), bean.getMatchBean().getCourt()), ivMatch);
    }

    public String fillRecord(Record record) {
        if (matchBean == null) {
            return editorHolder.getActivity().getString(R.string.editor_null_match);
        }
        record.setCity(matchBean.getMatchBean().getCity());
        record.setMatchCountry(matchBean.getMatchBean().getCountry());
        record.setCourt(matchBean.getMatchBean().getCourt());
        record.setLevel(matchBean.getMatchBean().getLevel());
        record.setMatch(matchBean.getName());
        record.setRegion(matchBean.getMatchBean().getRegion());
        record.setRound(arr_round[cur_round]);
        record.setScore(mStrScore);
        int cur_month = 0;
        try {
            cur_month = matchBean.getMatchBean().getMonth() - 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        String month=(1+cur_month)<10 ? ("0"+(1+cur_month)):(""+(1+cur_month));
        record.setStrDate(""+(2010+cur_year)+"-"+month);
        if (tvWinner.getText().toString().matches(MultiUserManager.getInstance().getCurrentUser().getDisplayName())) {
            record.setWinner(MultiUserManager.USER_DB_FLAG);
        }
        else record.setWinner(record.getCompetitor());
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM");
        long lDate;
        try {
            lDate=format.parse(record.getStrDate()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            lDate=System.currentTimeMillis();
        }
        record.setLongDate(lDate);

        Configuration conf = Configuration.getInstance();
        conf.index_month = cur_month;
        conf.index_year = cur_year;
        return null;
    }

    private class SpinnerListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            if (parent == sp_year) {
                cur_year = position;
            } else if (parent == sp_round) {
                cur_round = position;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }
}
