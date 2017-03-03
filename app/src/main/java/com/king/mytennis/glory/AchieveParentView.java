package com.king.mytennis.glory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.king.mytennis.glory.adapter.AchieveDateContentAdapter;
import com.king.mytennis.glory.adapter.GrandSlameAdapter;
import com.king.mytennis.model.Record;
import com.king.mytennis.view.CustomDialog;
import com.king.mytennis.view.R;

import cn.samson.timershaftdemo.view.XListView;
import cn.samson.timershaftdemo.view.XListView.IXListViewListener;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * v2.4.1 废弃rank部分，rank由score module的编辑对话框管理
 */
public class AchieveParentView implements IXListViewListener {

	private Context context;

	private XListView sListView;
	private List<HashMap<String, String>> achiveListViewData;
	private Handler sHandler;
	//private static int start = 0;
	//private static int refreshCnt = 0;
	private AchieveDateContentAdapter listAdapter;
	private GrandSlameAdapter tableListAdapter;

	private FrameLayout achiveListParentLayout, achieveRankLayout;
	private TextView achieveTitle;
	private ImageView achieveTitleIcon;

	private TextView achieveTitleLinear;
	private ImageView achieveTitleIconLinear;

	private GloryController controller;

	public AchieveParentView(Context context) {
		this.context = context;
		controller = new GloryController();

		initXListDateContentView();
		initLinearView();

		setView(GloryIndex.FAME_TITLE_INDEX);
		sHandler = new Handler();
	}

	private void initLinearView() {
		Activity view = (Activity) context;
		achieveRankLayout = (FrameLayout) view.findViewById(R.id.glory_layout_linearview);
		achieveTitleLinear = (TextView) view.findViewById(R.id.achieve_list_title_linear);
		achieveTitleIconLinear = (ImageView) view.findViewById(R.id.achieve_list_title_icon_linear);
		achieveRankLayout.setVisibility(View.GONE);
	}

	private void initXListDateContentView() {
		Activity view = (Activity) context;
		achiveListParentLayout = (FrameLayout) view.findViewById(R.id.glory_layout_xlistview);
		achieveTitle = (TextView) view.findViewById(R.id.achieve_list_title);
		achieveTitleIcon = (ImageView) view.findViewById(R.id.achieve_list_title_icon);
		sListView = (XListView) view.findViewById(R.id.xListView);
		//sListView.setPullLoadEnable(true);
		sListView.setPullLoadEnable(false);
		sListView.setPullRefreshEnable(false);
		sListView.setXListViewListener(this);
	}

	public void setView(int flag) {
		switch (flag) {
			case GloryIndex.FAME_TITLE_INDEX:
				achieveTitle.setText(GloryIndex.FAME_TITLE);
				achieveTitleIcon.setImageResource(R.drawable.glory_title);
				achiveListViewData = getGloryTitles();
				initDateContentView();
				break;
			case GloryIndex.FAME_GRANDSLAM_INDEX:
				achieveTitle.setText(GloryIndex.FAME_GRANDSLAM);
				achieveTitleIcon.setImageResource(R.drawable.glory_gs);
				controller.loadGloryGrandSlame();
				initDateTableContentView();
				break;
			case GloryIndex.FAME_PRIZE_INDEX:
				achieveTitle.setText(GloryIndex.FAME_PRIZE);
				achieveTitleIcon.setImageResource(R.drawable.glory_prize);
				break;
			case GloryIndex.FAME_RUNNERUP_INDEX:
				achieveTitle.setText(GloryIndex.FAME_RUNNERUP);
				achieveTitleIcon.setImageResource(R.drawable.glory_runnerup);
				achiveListViewData = getGloryRunnerups();
				initDateContentView();
				break;
			default:
				achiveListParentLayout.setVisibility(View.GONE);
				break;
		}
	}

	private void initDateTableContentView() {
		achieveRankLayout.setVisibility(View.GONE);
		achiveListParentLayout.setVisibility(View.VISIBLE);

		//if (tableListAdapter == null) {
		tableListAdapter = new GrandSlameAdapter(context, controller.getGSData());
		sListView.setAdapter(tableListAdapter);
		sListView.setOnItemClickListener(new GSEditListener());
//		}
//		else {
//			tableListAdapter.setAchieveList(achiveList);
//			tableListAdapter.notifyDataSetChanged();
//		}
	}

	private void initDateContentView() {
		achiveListParentLayout.setVisibility(View.VISIBLE);

		//if (listAdapter == null) {
		listAdapter = new AchieveDateContentAdapter(context, achiveListViewData);
		sListView.setAdapter(listAdapter);
		sListView.setOnItemClickListener(new MatchListener());
//		}
//		else {
//			listAdapter.setAchieveList(achiveList);
//			listAdapter.notifyDataSetChanged();
//		}
	}

	private List<HashMap<String, String>> getGloryRunnerups() {

		return controller.loadRunnerUps(context);
	}

	private List<HashMap<String, String>> getGloryTitles() {

		return controller.loadTitles(context);
	}

	private void onLoad() {
		sListView.stopRefresh();
		sListView.stopLoadMore();
		Date date = new Date();
		String time = date.getHours() + ":" + date.getMinutes() + ":"
				+ date.getSeconds();
		sListView.setRefreshTime(time);
	}

	@Override
	public void onRefresh() {
		sHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// start = ++refreshCnt;
				// start = refreshCnt;
				//items.clear();
				//getGloryTitles();
				// sAdapter.notifyDataSetChanged();
				//listAdapter = new AchieveListAdapter(context, list);
				//sListView.setAdapter(listAdapter);
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		sHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				getGloryTitles();
				listAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}

	public void filtTitle(int indexLevel, int indexCourt) {
		String level = null, court = null;
		if (indexLevel > 0) {
			String[] levels = context.getResources().getStringArray(R.array.actionbar_spinner_level);
			level = levels[indexLevel];
		}
		if (indexCourt > 0) {
			String[] courts = context.getResources().getStringArray(R.array.actionbar_spinner_court);
			court = courts[indexCourt];
		}
		List<HashMap<String, String>> list = controller.filterTitle(level, court);

		listAdapter.setAchieveList(list);
		listAdapter.notifyDataSetChanged();
	}

	public void enableEditGS() {
		tableListAdapter.setEditMode(true);
	}

	public void cancelEditGS() {
		tableListAdapter.setEditMode(false);
	}
	public void saveGS() {
		// TODO Auto-generated method stub
		controller.saveGloryGrandSlame();
		tableListAdapter.setEditMode(false);
	}

	public void addGs() {
		tableListAdapter.addGS();
	}
	private class GSEditListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
			if (tableListAdapter.isEditMode() && position > 1) {
				tableListAdapter.editGS(position - 2);
			}
		}

	}

	private class MatchListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
			//achiveListViewData是所有的，过滤的时候不是使用该list
			//final HashMap<String, String> map = achiveListViewData.get(position - 1);
			final HashMap<String, String> map = (HashMap<String, String>) listAdapter.getItem(position - 1);
			new GloryMatchDialog(context, new CustomDialog.OnCustomDialogActionListener() {

				@Override
				public boolean onSave(Object object) {
					return false;
				}

				@Override
				public void onLoadData(HashMap<String, Object> data) {
					List<Record> list = controller.loadMatchRecord(context,
							map.get("achieve_glory"), map.get("achieve_date"));
					data.put(CustomDialog.OnCustomDialogActionListener.DATA_TYPE, list);
				}

				@Override
				public boolean onCancel() {
					return false;
				}
			}).show();
		}

	}
}
