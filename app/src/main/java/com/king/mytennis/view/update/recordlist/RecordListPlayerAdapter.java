package com.king.mytennis.view.update.recordlist;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.king.mytennis.download.DownloadItem;
import com.king.mytennis.http.Command;
import com.king.mytennis.http.RequestCallback;
import com.king.mytennis.http.bean.ImageUrlBean;
import com.king.mytennis.model.Configuration;
import com.king.mytennis.model.ImageFactory;
import com.king.mytennis.service.ImageUtil;
import com.king.mytennis.view.CustomDialog;
import com.king.mytennis.view.R;
import com.king.mytennis.view_v_7_0.interaction.controller.InteractionController;
import com.king.mytennis.view_v_7_0.view.CircleImageView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RecordListPlayerAdapter extends BaseExpandableListAdapter implements RequestCallback, View.OnClickListener {

	private List<HashMap<String, String>> titleList;
	private List<List<HashMap<String, String>>> recordList;
	private Context context;
	private int COLOR_COURT_HARD;
	private int COLOR_COURT_CLAY;
	private int COLOR_COURT_GRASS;
	private int PLAYER_TITLE_FLAG0;
	private int PLAYER_TITLE_FLAG1;

	/**
	 * 保存首次从文件夹加载的图片序号
	 */
	private Map<String, Integer> playerImageIndexMap;

	/**
	 * 保存首次从文件夹加载的图片序号
	 */
	private Map<String, Integer> matchImageIndexMap;

	/**
	 * Player list，下载图片/刷新头像/管理图片
	 */
	private InteractionController interactionController;

	/**
	 * 单击头像位置
	 */
	private int nGroupPosition;

	public RecordListPlayerAdapter(Context context, List<HashMap<String, String>> titleList, List<List<HashMap<String, String>>> recordList) {
		this.context = context;
		this.titleList = titleList;
		this.recordList = recordList;
		COLOR_COURT_HARD = context.getResources().getColor(R.color.court_hard);
		COLOR_COURT_CLAY = context.getResources().getColor(R.color.court_clay);
		COLOR_COURT_GRASS = context.getResources().getColor(R.color.court_grass);
		PLAYER_TITLE_FLAG0 = context.getResources().getColor(R.color.groupbyplayer_flag0);
		PLAYER_TITLE_FLAG1 = context.getResources().getColor(R.color.groupbyplayer_flag1);
		playerImageIndexMap = new HashMap<>();
		matchImageIndexMap = new HashMap<>();
		interactionController = new InteractionController(this);
	}

	@Override
	public int getGroupCount() {

		return titleList == null ? 0:titleList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (recordList != null) {
			if (recordList.get(groupPosition) != null) {
				return recordList.get(groupPosition).size();
			}
		}
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {

		return titleList == null ? null:titleList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {

		if (recordList != null) {
			if (recordList.get(groupPosition) != null) {
				return recordList.get(groupPosition).get(childPosition);
			}
		}
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {

		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {

		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
							 View convertView, ViewGroup parent) {

		TitleViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.recordlist_title_group_by_player, null);
			holder = new TitleViewHolder();
			holder.arrow = (ImageView) convertView.findViewById(R.id.recordlist_title_image);
			holder.player = (TextView) convertView.findViewById(R.id.recordlist_player_title_player);
			holder.head = (CircleImageView) convertView.findViewById(R.id.recordlist_title_image_player);
			holder.h2h = (TextView) convertView.findViewById(R.id.recordlist_player_title_h2h);
			convertView.setTag(holder);
		}
		else {
			holder = (TitleViewHolder) convertView.getTag();
		}
		if (isExpanded) {
			holder.arrow.setImageResource(R.drawable.ic_expand_less_white_36dp);
		}
		else {
			holder.arrow.setImageResource(R.drawable.ic_expand_more_white_36dp);
		}
		HashMap<String, String> map = titleList.get(groupPosition);
		holder.player.setText(map.get("player") + "(" + map.get("country") + ")");

		holder.h2h.setText(map.get("h2h"));

		String filePath;
		if (playerImageIndexMap.get(map.get("player")) == null) {
			filePath = ImageFactory.getPlayerHeadPath(map.get("player"), playerImageIndexMap);
		}
		else {
			filePath = ImageFactory.getPlayerHeadPath(map.get("player"), playerImageIndexMap.get(map.get("player")));
		}
		ImageUtil.load("file://" + filePath, holder.head, R.drawable.icon_list);
		holder.head.setOnClickListener(this);
		holder.head.setTag(R.id.tag_record_list_player_group_index, groupPosition);

		String bkFlag = map.get("bkFlag");
		if (bkFlag != null) {
			if (bkFlag.equals("0")) {
				convertView.setBackgroundColor(PLAYER_TITLE_FLAG0);
			}
			else if (bkFlag.equals("1")) {
				convertView.setBackgroundColor(PLAYER_TITLE_FLAG1);
			}
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
							 boolean isLastChild, View convertView, ViewGroup parent) {

		RecordViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.recordlist_record_group_by_player, null);
			holder = new RecordViewHolder();
			holder.matchImage = (ImageView) convertView.findViewById(R.id.recordlist_record_head);
			holder.player = (TextView) convertView.findViewById(R.id.recordlist_record_player);
			holder.match = (TextView) convertView.findViewById(R.id.recordlist_record_match);
			holder.score = (TextView) convertView.findViewById(R.id.recordlist_record_score);
			convertView.setTag(holder);
		}
		else {
			holder = (RecordViewHolder) convertView.getTag();
		}
		HashMap<String, String> map = recordList.get(groupPosition).get(childPosition);

		holder.player.setText(map.get("player"));
		holder.match.setText(map.get("match"));
		holder.score.setText(map.get("score"));

		String filePath;
		if (matchImageIndexMap.get(map.get("match_name")) == null) {
			filePath = ImageFactory.getMatchHeadPath(map.get("match_name"), map.get("court_name"), matchImageIndexMap);
		}
		else {
			filePath = ImageFactory.getMatchHeadPath(map.get("match_name"), map.get("court_name"), matchImageIndexMap.get(map.get("match_name")));
		}
		ImageUtil.load("file://" + filePath, holder.matchImage, R.drawable.icon_list);

		if (map.get("player").contains("硬地")) {
			convertView.setBackgroundColor(COLOR_COURT_HARD);
		}
		else if (map.get("player").contains("红土")) {
			convertView.setBackgroundColor(COLOR_COURT_CLAY);
		}
		else if (map.get("player").contains("草地")) {
			convertView.setBackgroundColor(COLOR_COURT_GRASS);
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {

		return true;//一定要在这里控制，否则单击、长按都没有效果
	}

	@Override
	public void onClick(View v) {
		nGroupPosition = (int) v.getTag(R.id.tag_record_list_player_group_index);

		AlertDialog.Builder dlg = new AlertDialog.Builder(context);
		dlg.setTitle(titleList.get(nGroupPosition).get("player"));
		dlg.setItems(context.getResources().getStringArray(R.array.cptdlg_item_oper)
				, itemListener);
		dlg.show();
	}

	DialogInterface.OnClickListener itemListener = new DialogInterface.OnClickListener() {

		private final int DOWNLOAD = 0;
		private final int REFRESH = 1;
		private final int MANAGE = 2;
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (which == DOWNLOAD) {
				onItemClickDownload(nGroupPosition);
			}
			else if (which == REFRESH) {
				onItemClickRefresh(nGroupPosition);
			}
			else if (which == MANAGE) {
				onItemClickManage(nGroupPosition);
			}
		}
	};

	public void onItemClickDownload(int position) {
		String name = titleList.get(position).get("player");
		interactionController.getImages(Command.TYPE_IMG_PLAYER_HEAD, name);
	}

	public void onItemClickRefresh(int position) {
		String name = titleList.get(position).get("player");
		ImageFactory.getPlayerHeadPath(name, playerImageIndexMap);
		notifyDataSetChanged();
	}

	public void onItemClickManage(int position) {
		final String name = titleList.get(position).get("player");
		interactionController.showLocalImageDialog(context, new CustomDialog.OnCustomDialogActionListener() {
			@Override
			public boolean onSave(Object object) {
				List<String> list = (List<String>) object;
				interactionController.deleteImages(list);
				notifyDataSetChanged();
				return false;
			}

			@Override
			public boolean onCancel() {
				return false;
			}

			@Override
			public void onLoadData(HashMap<String, Object> data) {
				ImageUrlBean bean = interactionController.getPlayerHeadUrlBean(name);
				data.put("data", bean);
				data.put("flag", Command.TYPE_IMG_PLAYER_HEAD);
			}
		});
	}

	@Override
	public void onServiceDisConnected() {
		Toast.makeText(context, R.string.gdb_server_offline, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onRequestError() {
		Toast.makeText(context, R.string.gdb_request_fail, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onImagesReceived(final ImageUrlBean bean) {
		if (bean.getUrlList() == null) {
			String text = context.getString(R.string.image_not_found);
			text = String.format(text, bean.getKey());
			Toast.makeText(context, text, Toast.LENGTH_LONG).show();
		}
		else {
			// 直接下载更新
			if (bean.getUrlList().size() == 1) {
				List<DownloadItem> list = new ArrayList<>();
				DownloadItem item = new DownloadItem();
				item.setKey(bean.getUrlList().get(0));
				item.setFlag(Command.TYPE_IMG_PLAYER_HEAD);
				item.setSize(bean.getSizeList().get(0));

				String url = bean.getUrlList().get(0);
				if (url.contains("/")) {
					String[] array = url.split("/");
					url = array[array.length - 1];
				}
				item.setName(url);

				list.add(item);

				startDownload(list, bean.getKey());
			}
			// 显示对话框选择下载
			else {
				interactionController.showHttpImageDialog(context, new CustomDialog.OnCustomDialogActionListener() {
					@Override
					public boolean onSave(Object object) {
						List<DownloadItem> list = (List<DownloadItem>) object;
						startDownload(list, bean.getKey());
						return false;
					}

					@Override
					public boolean onCancel() {
						return false;
					}

					@Override
					public void onLoadData(HashMap<String, Object> data) {
						data.put("data", bean);
						data.put("flag", Command.TYPE_IMG_PLAYER_HEAD);
					}
				});
			}
		}
	}

	@Override
	public void onDownloadFinished() {
		notifyDataSetChanged();
	}

	private void startDownload(List<DownloadItem> list, String key) {
		File file = new File(Configuration.IMG_PLAYER_HEAD + key);
		if (!file.exists() || !file.isDirectory()) {
			file.mkdir();
		}
		interactionController.downloadImage(context, list, file.getPath(), true);
	}

	private class TitleViewHolder {
		ImageView arrow;
		CircleImageView head;
		TextView player, h2h;
	}
	private class RecordViewHolder {
		ImageView matchImage;
		TextView player, match, score;
	}
}
