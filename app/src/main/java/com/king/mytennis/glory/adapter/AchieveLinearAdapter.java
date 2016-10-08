package com.king.mytennis.glory.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.king.mytennis.glory.GloryController;
import com.king.mytennis.view.CustomDialog;
import com.king.mytennis.view.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class AchieveLinearAdapter implements OnClickListener {

	private ViewHolder viewHolder;
	private Context context;
	private RankViewData rankData;
	private GloryController controller;
	private boolean isEditMode;
	
	public class RankViewData {
		public int rank;
		public int score;
		public int top1week;
		public int matchNumber;
	}
	
	public AchieveLinearAdapter(GloryController controller) {
		this.controller = controller;
		isEditMode = false;
	}
	
	public void registView(Context context) {
		this.context = context;
		if (viewHolder == null) {
			Activity view = (Activity) context;
			viewHolder = new ViewHolder();
			viewHolder.currentRank = (EditText) view.findViewById(R.id.glory_rank_current);
			viewHolder.ytdMatchNumber = (EditText) view.findViewById(R.id.glory_ytd_matchnumber);
			viewHolder.currentScore = (TextView) view.findViewById(R.id.glory_rank_current_score);
			viewHolder.top1Week = (EditText) view.findViewById(R.id.glory_rank_top1week);
			viewHolder.decButton = (ImageView) view.findViewById(R.id.glory_rank_top1week_decrease);
			viewHolder.incButton = (ImageView) view.findViewById(R.id.glory_rank_top1week_increase);
			viewHolder.decButton.setOnClickListener(this);
			viewHolder.incButton.setOnClickListener(this);
			viewHolder.currentScore.setOnClickListener(this);
		}
		if (rankData == null) {
			HashMap<String, Integer> data = controller.getRankData();
			if (data != null) {
				rankData = new RankViewData();
				rankData.rank = data.get("rank");
				rankData.score = data.get("score");
				rankData.top1week = data.get("top1week");
				rankData.matchNumber = data.get("matchNumber");
			}
			else {
				rankData = new RankViewData();
				rankData.rank = 1;
				rankData.score = 16888;
				rankData.top1week = 89;
				rankData.matchNumber = 19;
			}
		}
		viewHolder.currentRank.setText("" + rankData.rank);
		viewHolder.currentScore.setText("" + rankData.score);
		viewHolder.top1Week.setText("" + rankData.top1week);
		viewHolder.ytdMatchNumber.setText("" + rankData.matchNumber);
	}
	
	private class ViewHolder {
		public EditText currentRank;
		public EditText ytdMatchNumber;
		public TextView currentScore;
		public EditText top1Week;
		public ImageView decButton, incButton;
	}

	public void enableEdit() {
		isEditMode = true;
		viewHolder.currentRank.setBackgroundResource(R.drawable.selector_basic_frame);
		viewHolder.currentScore.setBackgroundResource(R.drawable.selector_basic_frame);
		viewHolder.ytdMatchNumber.setBackgroundResource(R.drawable.selector_basic_frame);
		viewHolder.currentRank.setEnabled(true);
		viewHolder.currentScore.setEnabled(true);
		viewHolder.ytdMatchNumber.setEnabled(true);
		viewHolder.decButton.setVisibility(View.VISIBLE);
		viewHolder.incButton.setVisibility(View.VISIBLE);
		viewHolder.top1Week.setEnabled(true);

		if (rankData == null) {
			HashMap<String, Integer> data = controller.getRankData();
			if (data != null) {
				rankData = new RankViewData();
				rankData.rank = data.get("rank");
				rankData.score = data.get("score");
				rankData.top1week = data.get("top1week");
				rankData.matchNumber = data.get("matchNumber");
			}
			else {
				rankData = new RankViewData();
				rankData.rank = 1;
				rankData.score = 16888;
				rankData.top1week = 89;
				rankData.matchNumber = 19;
			}
		}
		rankData.rank = Integer.parseInt(viewHolder.currentRank.getText().toString());
		rankData.score = Integer.parseInt(viewHolder.currentScore.getText().toString());
		rankData.matchNumber = Integer.parseInt(viewHolder.ytdMatchNumber.getText().toString());
		rankData.top1week = Integer.parseInt(viewHolder.top1Week.getText().toString());
	}
	
	public void disableEdit(boolean saveData) {
		isEditMode = false;
		viewHolder.currentRank.setBackgroundColor(context.getResources().getColor(R.color.transparent));
		viewHolder.currentScore.setBackgroundColor(context.getResources().getColor(R.color.transparent));
		viewHolder.ytdMatchNumber.setBackgroundColor(context.getResources().getColor(R.color.transparent));
		viewHolder.currentRank.setEnabled(false);
		viewHolder.currentScore.setEnabled(false);
		viewHolder.ytdMatchNumber.setEnabled(false);
		viewHolder.decButton.setVisibility(View.GONE);
		viewHolder.incButton.setVisibility(View.GONE);
		
		if (saveData) {
			rankData.rank = Integer.parseInt(viewHolder.currentRank.getText().toString());
			rankData.score = Integer.parseInt(viewHolder.currentScore.getText().toString());
			rankData.top1week = Integer.parseInt(viewHolder.top1Week.getText().toString());
			rankData.matchNumber = Integer.parseInt(viewHolder.ytdMatchNumber.getText().toString());
		}
		else {
			viewHolder.currentRank.setText("" + rankData.rank);
			viewHolder.currentScore.setText("" + rankData.score);
			viewHolder.ytdMatchNumber.setText("" + rankData.matchNumber);
			viewHolder.top1Week.setText("" + rankData.top1week);
		}
	}

	@Override
	public void onClick(View view) {
		if (view == viewHolder.incButton) {
			int num = Integer.parseInt(viewHolder.top1Week.getText().toString());
			num ++;
			rankData.top1week = num;
			viewHolder.top1Week.setText("" + num);
		}
		else if (view == viewHolder.decButton) {
			int num = Integer.parseInt(viewHolder.top1Week.getText().toString());
			num --;
			rankData.top1week = num;
			viewHolder.top1Week.setText("" + num);
		}
		else if (view == viewHolder.currentScore) {
			if (isEditMode) {
				ScoreEditor editor = new ScoreEditor(context, new CustomDialog.OnCustomDialogActionListener() {
					
					@Override
					public boolean onSave(Object object) {
						if (object != null) {
							List<HashMap<String, String>> list = (List<HashMap<String, String>>) object;
							controller.saveSeasonScoreList(list);
							refreshScore(list);
						}
						return true;
					}
					
					@Override
					public void onLoadData(HashMap<String, Object> data) {
						List<HashMap<String, String>> list = controller.getSeasonScoreList();
						data.put("current_score_list", list);
					}
					
					@Override
					public boolean onCancel() {

						return false;
					}
				});
			
			editor.show();
			}
		}
	}

	public void saveRank() {
		String edit = viewHolder.currentRank.getText().toString();
		int score = 0;
		if (edit != null && edit.length() != 0) {
			score = Integer.parseInt(edit);
		}
		rankData.rank = score;
		
		edit = viewHolder.ytdMatchNumber.getText().toString();
		score = 0;
		if (edit != null && edit.length() != 0) {
			score = Integer.parseInt(edit);
		}
		rankData.matchNumber = score;

		edit = viewHolder.top1Week.getText().toString();
		score = 0;
		if (edit != null && edit.length() != 0) {
			score = Integer.parseInt(edit);
		}
		rankData.top1week = score;
		
		controller.saveRankData(rankData);
	}
	protected void refreshScore(List<HashMap<String, String>> list) {
		if (list != null && list.size() > 0) {
			int score = 0;
			for (int i = 0; i < list.size(); i ++) {
				score += Integer.parseInt(list.get(i).get("season_score_score"));
			}
			rankData.score = score;
			viewHolder.currentScore.setText("" + score);
		}
	}

	private class ScoreEditorItem {
		public TextView index;
		public EditText match;
		public EditText score;
		public ImageView remove;
	}
	
	private class ScoreEditor extends CustomDialog {

		private ImageView addButton;
		private LinearLayout layout;
		private List<ScoreEditorItem> viewList;
		private List<HashMap<String, String>> scoreDataList;
		
		public ScoreEditor(Context context, OnCustomDialogActionListener actionListener) {
			super(context, actionListener);
			initDataView();//如果不在super之后给remove button注册listener，而在getCustomView中初始化的话，button将获取不到响应事件
		}

		private void initDataView() {
			HashMap<String, Object> map = new HashMap<String, Object>();
			actionListener.onLoadData(map);
			scoreDataList = (List<HashMap<String, String>>) map.get("current_score_list");
			if (scoreDataList != null && scoreDataList.size() > 0) {
				int score = 0;
				for (int i = 0; i < scoreDataList.size(); i ++) {
					addScoreItem(i);
					score += Integer.parseInt(scoreDataList.get(i).get("season_score_score"));
				}
				setTitle("" + score);
			}
		}

		@Override
		protected View getCustomView() {

			ScrollView scrollView = new ScrollView(context);
			int height = context.getResources().getDimensionPixelSize(R.dimen.dialog_score_editor_height);
			LayoutParams params = new LayoutParams(-1, height);
			scrollView.setLayoutParams(params);
			layout = new LinearLayout(context);
			layout.setOrientation(LinearLayout.VERTICAL);
			
			scrollView.addView(layout);
			return scrollView;
		}

		@Override
		protected View getCustomToolbar() {

			addButton = new ImageView(context);
			int size = context.getResources().getDimensionPixelSize(R.dimen.custom_dialog_toolbar_icon);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
			addButton.setLayoutParams(params);
			addButton.setImageResource(R.drawable.icon_increase);
			addButton.setBackgroundResource(R.drawable.custom_dialog_icon_bk);
			addButton.setOnClickListener(this);
			return addButton;
		}

		android.view.View.OnClickListener removeListener = new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int index = (Integer) v.getTag();
				viewList.remove(index);
				layout.removeViewAt(index);
				scoreDataList.remove(index);
				notifyIndexChanged();
				notifyScoreChanged();
			}

		};
		
		OnFocusChangeListener scoreListener = new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					int index = (Integer) v.getTag();
					EditText text = (EditText) v;
					String edit = text.getText().toString();
					int score = 0;
					if (edit != null && edit.length() != 0) {
						score = Integer.parseInt(edit);
					}
					scoreDataList.get(index).put("season_score_score", "" + score);
					notifyScoreChanged();
				}
			}
		};

		OnFocusChangeListener matchListener = new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					int index = (Integer) v.getTag();
					EditText text = (EditText) v;
					String edit = text.getText().toString();
					if (edit == null || edit.length() == 0) {
						viewList.get(index).score.setError(context.getResources().getString(R.string.match_name_not_null));
						return;
					}
					scoreDataList.get(index).put("season_score_match", "" + edit);
				}
			}
		};
		
		private View addScoreItem(int index) {
			View item = null;
			ScoreEditorItem itemView = null;
			item = LayoutInflater.from(context).inflate(R.layout.glory_score_item, null);
			itemView = new ScoreEditorItem();
			itemView.index = (TextView) item.findViewById(R.id.glory_score_index);
			itemView.match = (EditText) item.findViewById(R.id.glory_score_match);
			itemView.score = (EditText) item.findViewById(R.id.glory_score_score);
			itemView.remove = (ImageView) item.findViewById(R.id.glory_score_remove_item);
			itemView.score.setTag(index);
			itemView.score.setOnFocusChangeListener(scoreListener);
			itemView.match.setTag(index);
			itemView.match.setOnFocusChangeListener(matchListener);
			itemView.remove.setTag(index);
			itemView.remove.setOnClickListener(removeListener);
			itemView.index.setText("" + (index + 1));
			
			layout.addView(item);
			if (viewList == null) {
				viewList = new ArrayList<AchieveLinearAdapter.ScoreEditorItem>();
			}
			viewList.add(itemView);
			
			if (index < scoreDataList.size()) {
				itemView.match.setText(scoreDataList.get(index).get("season_score_match"));
				itemView.score.setText(scoreDataList.get(index).get("season_score_score"));
			}
			else {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("season_score_score", "0");
				scoreDataList.add(map);
			}
			return item;
		}
		
		@Override
		public void onClick(View view) {
			if (view == addButton) {
				addScoreItem(viewList.size());
			}
			else if (view == saveButton) {
				actionListener.onSave(scoreDataList);
			}
			super.onClick(view);
		}

		private void notifyIndexChanged() {
			ScoreEditorItem item = null;
			for (int i = 0; i < viewList.size(); i ++) {
				item = viewList.get(i);
				item.index.setText("" + (i + 1));
				item.remove.setTag(i);
				item.score.setTag(i);
				item.match.setTag(i);
			}
		}

		private void notifyScoreChanged() {
			int score = 0;
			for (int i = 0; i < scoreDataList.size(); i ++) {
				score += Integer.parseInt(scoreDataList.get(i).get("season_score_score"));
			}
			setTitle("" + score);
		}

	}

}
