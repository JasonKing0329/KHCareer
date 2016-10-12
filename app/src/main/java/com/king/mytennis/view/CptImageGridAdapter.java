package com.king.mytennis.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.king.mytennis.download.DownloadItem;
import com.king.mytennis.http.Command;
import com.king.mytennis.http.RequestCallback;
import com.king.mytennis.http.bean.ImageUrlBean;
import com.king.mytennis.model.Configuration;
import com.king.mytennis.model.ImageFactory;
import com.king.mytennis.service.ImageUtil;
import com.king.mytennis.view_v_7_0.interaction.controller.InteractionController;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CptImageGridAdapter  extends BaseAdapter implements RequestCallback {

	private String[] cptArray;
	private Context context;

	/**
	 * 下载/浏览网络图库 控制器
	 */
	private InteractionController interactionController;

	/**
	 * 保存首次从文件夹加载的图片序号
	 */
	private Map<String, Integer> imageIndexMap;

	public CptImageGridAdapter(Context context, String[] array) {
		this.context = context;
		cptArray = array;
		imageIndexMap = new HashMap<>();
		interactionController = new InteractionController(this);
	}

	@Override
	public int getCount() {

		return cptArray.length;
	}

	@Override
	public Object getItem(int position) {

		return cptArray[position];
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_cptgrid, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.cptgrid_image);
			holder.name = (TextView) convertView.findViewById(R.id.cptgrid_name);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(cptArray[position]);

		String filePath;
		if (imageIndexMap.get(cptArray[position]) == null) {
			filePath = ImageFactory.getPlayerHeadPath(cptArray[position], imageIndexMap);
		}
		else {
			filePath = ImageFactory.getPlayerHeadPath(cptArray[position], imageIndexMap.get(cptArray[position]));
		}
		ImageUtil.load("file://" + filePath, holder.image, R.drawable.icon_list);
		return convertView;
	}

	public void onItemClickDownload(int position) {
		String name = cptArray[position];
		interactionController.getImages(Command.TYPE_IMG_PLAYER_HEAD, name);
	}

	public void onItemClickRefresh(int position) {
		String name = cptArray[position];
		ImageFactory.getPlayerHeadPath(name, imageIndexMap);
		notifyDataSetChanged();
	}

	public void onItemClickManage(int position) {
		final String name = cptArray[position];
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

	private class ViewHolder {
		public ImageView image;
		public TextView name;
	}
}
