package com.king.khcareer.match.swipecard;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.king.khcareer.download.DownloadItem;
import com.king.khcareer.model.http.Command;
import com.king.khcareer.model.http.RequestCallback;
import com.king.khcareer.model.http.bean.ImageUrlBean;
import com.king.khcareer.player.swipecard.adapter.AbstractSwipeAdapter;
import com.king.khcareer.common.config.Configuration;
import com.king.khcareer.common.config.Constants;
import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.common.image.ImageUtil;
import com.king.khcareer.utils.DebugLog;
import com.king.khcareer.base.CustomDialog;
import com.king.mytennis.view.R;
import com.king.khcareer.common.helper.ObjectCache;
import com.king.khcareer.common.image.interaction.controller.InteractionController;
import com.king.khcareer.match.timeline.MatchActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author JingYang
 * @version create time：2016-3-11 上午11:32:25
 *
 */
@Deprecated
public class MatchSwipeCardAdapter extends AbstractSwipeAdapter implements RequestCallback {

	private List<MatchBean>  mList;
	private List<MatchBean> mOriginList;
	private Map<String, Integer> indexPosMap;

	private int colorHard;
	private int colorClay;
	private int colorGrass;
	private int colorInnerHard;
	private String[] courtValues;

	private ViewHolder firstItemHolder;

	/**
	 * 下载/浏览网络图库 控制器
	 */
	private InteractionController interactionController;

	/**
	 * 保存首次从文件夹加载的图片序号
	 */
	private Map<String, Integer> imageIndexMap;

	public MatchSwipeCardAdapter(Context context, List<MatchBean> list) {
		super(context);
		mContext = context;
		if (list == null) {
			mOriginList = new ArrayList<>();
			mList = new ArrayList<>();
		}
		else {
			mOriginList = list;
			mList = new ArrayList<>();
			mList.addAll(mOriginList);
		}
		indexPosMap = new HashMap<>();

		courtValues = Constants.RECORD_MATCH_COURTS;
		colorHard = mContext.getResources().getColor(R.color.swipecard_text_hard);
		colorClay = mContext.getResources().getColor(R.color.swipecard_text_clay);
		colorGrass = mContext.getResources().getColor(R.color.swipecard_text_grass);
		colorInnerHard = mContext.getResources().getColor(R.color.swipecard_text_innerhard);

		imageIndexMap = new HashMap<>();
		interactionController = new InteractionController(this);
	}
	@Override
	public int getCount() {

		return mList.size();
	}

	@Override
	/**
	 * 为了方便onItemClickListener知道position，这里返回position
	 */
	public Object getItem(int position) {

		return position;
	}

	@Override
	public Object getItemData(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Log.d("MatchSwipeCardAdapter", "getView " + position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.swipecard_item_match, parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.swipecard_match_img);
			holder.download = (ImageView) convertView.findViewById(R.id.swipecard_icon_download);
			holder.refresh = (ImageView) convertView.findViewById(R.id.swipecard_icon_refresh);
			holder.name = (TextView) convertView.findViewById(R.id.swipecard_match_name);
			holder.place = (TextView) convertView.findViewById(R.id.swipecard_match_city);
			holder.level = (TextView) convertView.findViewById(R.id.swipecard_match_level);
			holder.court = (TextView) convertView.findViewById(R.id.swipecard_match_court);
			holder.total = (TextView) convertView.findViewById(R.id.swipecard_match_total);
			holder.best = (TextView) convertView.findViewById(R.id.swipecard_match_best);
			holder.local = (ImageView) convertView.findViewById(R.id.swipecard_icon_local);
			holder.convertView = convertView;
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position == 0) {
			firstItemHolder = holder;
		}

		fillHolder(holder, position);
		return convertView;
	}

	private void fillHolder(ViewHolder holder, int position) {
		MatchBean bean = mList.get(position);

		String filePath = null;
		if (imageIndexMap.get(bean.getName()) == null) {
			filePath = ImageFactory.getMatchHeadPath(bean.getName(), bean.getCourt(), imageIndexMap);
		}
		else {
			filePath = ImageFactory.getMatchHeadPath(bean.getName(), bean.getCourt(), imageIndexMap.get(bean.getName()));
		}
		ImageUtil.load("file://" + filePath, holder.image, R.drawable.swipecard_default_img);
		holder.name.setText(bean.getName());
		holder.place.setText(bean.getCountry() + "/" + bean.getCity());
		holder.level.setText(bean.getLevel());
		holder.court.setText(bean.getCourt());
		holder.total.setText("总胜负  " + bean.getWin() + "胜" + bean.getLose() + "负");
		String best = "最佳战绩  " + bean.getBest();
		if (bean.getBestYears().length() > 0) {
			best = best + "(" + bean.getBestYears() + ")";
		}
		holder.best.setText(best);

		int color = getCardIndexColor(position);
		holder.name.setTextColor(color);
		holder.court.setTextColor(color);

		holder.download.setTag(bean);
		holder.download.setOnClickListener(downloadListener);
		holder.refresh.setTag(bean);
		holder.refresh.setOnClickListener(refreshListener);
		holder.local.setTag(bean);
		holder.local.setOnClickListener(localListener);
	}

	View.OnClickListener downloadListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			MatchBean bean = (MatchBean) v.getTag();
			interactionController.getImages(Command.TYPE_IMG_MATCH, bean.getName());
		}
	};

	View.OnClickListener refreshListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			MatchBean bean = (MatchBean) v.getTag();
			String path = ImageFactory.getMatchHeadPath(bean.getName(), bean.getCourt(), imageIndexMap);
			DebugLog.e(path);
			// 开源小bug，不能用notifyDataSetChanged通知第0个刷新
			refreshFirstItem(null);
		}
	};

	View.OnClickListener localListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			MatchBean bean = (MatchBean) v.getTag();
			final String match = bean.getName();
			interactionController.showLocalImageDialog(mContext, new CustomDialog.OnCustomDialogActionListener() {
				@Override
				public boolean onSave(Object object) {
					List<String> list = (List<String>) object;
					interactionController.deleteImages(list);
					refreshFirstItem(null);
					return false;
				}

				@Override
				public boolean onCancel() {
					return false;
				}

				@Override
				public void onLoadData(HashMap<String, Object> data) {
					ImageUrlBean bean = interactionController.getMatchImageUrlBean(match);
					data.put("data", bean);
					data.put("flag", Command.TYPE_IMG_MATCH);
				}
			});
		}
	};

	@Override
	public void onServiceDisConnected() {
		Toast.makeText(mContext, R.string.gdb_server_offline, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onRequestError() {
		Toast.makeText(mContext, R.string.gdb_request_fail, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onImagesReceived(final ImageUrlBean bean) {
		if (bean.getUrlList() == null) {
			String text = mContext.getString(R.string.image_not_found);
			text = String.format(text, bean.getKey());
			Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
		}
		else {
			// 直接下载更新
			if (bean.getUrlList().size() == 1) {
				List<DownloadItem> list = new ArrayList<>();
				DownloadItem item = new DownloadItem();
				item.setKey(bean.getUrlList().get(0));
				item.setFlag(Command.TYPE_IMG_MATCH);
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
				interactionController.showHttpImageDialog(mContext, new CustomDialog.OnCustomDialogActionListener() {
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
						data.put("flag", Command.TYPE_IMG_MATCH);
					}
				});
			}
		}
	}

	@Override
	public void onDownloadFinished() {
		refreshFirstItem(null);
	}

	private void startDownload(List<DownloadItem> list, String key) {
		File file = new File(Configuration.IMG_MATCH_BASE + key);
		if (!file.exists() || !file.isDirectory()) {
			file.mkdir();
		}
		interactionController.downloadImage(mContext, list, file.getPath(), true);
	}

	private class ViewHolder {
		View convertView;
		ImageView image;
		ImageView download, refresh, local;
		TextView name, place, level, court, total, best;
	}

	@Override
	public void remove(int index) {
		if (index > -1 && index < mList.size()) {
			mList.remove(index);
			notifyDataSetChanged();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateDatas(Object list) {

		mOriginList.clear();
		mOriginList.addAll((List<MatchBean>)list);
		mList.clear();
		mList.addAll(mOriginList);

		indexSideBar.clear();
		indexPosMap.clear();
		TreeSet<String> set = new TreeSet<String>();
		String index = null;
		String name = null;
		for (int i = 0; i < mList.size(); i ++) {
			name = mList.get(i).getNamePinYin();
			index = "" + name.charAt(0);
			if (set.add(index)) {
				indexSideBar.addIndex(index);
				indexPosMap.put(index, i);
			}
		}

		notifyDataSetChanged();
	}

	@Override
	public int getCardIndexColor(int position) {
		int color = colorHard;
		if (position < mList.size()) {
			if (mList.get(position).getCourt().equals(courtValues[1])) {
				color = colorClay;
			}
			else if (mList.get(position).getCourt().equals(courtValues[2])) {
				color = colorGrass;
			}
			else if (mList.get(position).getCourt().equals(courtValues[3])) {
				color = colorInnerHard;
			}
		}
		return color;
	}

	@Override
	public void refreshFirstItem(Animation animation) {

		if (firstItemHolder != null) {
			fillHolder(firstItemHolder, 0);
			if (animation != null) {
				firstItemHolder.convertView.startAnimation(animation);
			}
		}
	}
	@Override
	public void addItem(int i, Object bean) {
		if (i <= mList.size()) {
			mList.add(i, (MatchBean) bean);
		}
	}
	@Override
	public void onItemClicked(int itemPosition) {
		ObjectCache.putMatchBean(mList.get(itemPosition));
		Intent intent = new Intent().setClass(mContext, MatchActivity.class);
		((Activity) mContext).startActivity(intent);
	}
	@Override
	public void onIndexLetter(String index) {
		int position = indexPosMap.get(index);
		mList.clear();
		for (int i = position; i < mOriginList.size(); i ++) {
			mList.add(mOriginList.get(i));
		}
		notifyDataSetChanged();

		refreshFirstItem(null);
	}

}
