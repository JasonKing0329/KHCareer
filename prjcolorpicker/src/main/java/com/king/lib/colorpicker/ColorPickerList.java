package com.king.lib.colorpicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.king.lib.colorpicker.ColorPicker.OnColorPickerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JingYang
 * @version create time：2016-1-13 下午1:57:54
 *
 */
public class ColorPickerList implements OnItemClickListener {

	private Context mContext;
	private ColorPicker colorPicker;
	private ListView listView;
	private List<ColorPickerSelectionData> list;
	private View mContainer;
	private DataAdapter mAdapter;

	// ColorPicker编辑自身样式
	private List<ColorPickerSelectionData> mSelfList;
	private int mTextColor;
	private ResourceProvider mResourceProvider;

	private int mSelectPosition;

	public ColorPickerList(Context context, ColorPicker colorPicker, View container) {
		mContext = context;
		mContainer = container;
		this.colorPicker = colorPicker;
		listView = (ListView) container.findViewById(R.id.colorpicker_list);
		listView.setOnItemClickListener(this);

		onShow();
	}

	public void onShow() {
		mSelectPosition = 0;
		if (mResourceProvider != null) {
			mTextColor = ResourceController.getColor(mContext.getResources()
					, ColorPickerRes.COLORPICKER_LIST_TEXT
					, mResourceProvider.getColor(ColorPickerRes.COLORPICKER_LIST_TEXT));
		}
	}

	public void setSelectionData(List<ColorPickerSelectionData> list) {
		this.list = list;
		if (list != null && list.size() > 0) {
			mSelectPosition = 0;
			mContainer.setVisibility(View.VISIBLE);
			mAdapter = new DataAdapter();
			listView.setAdapter(mAdapter);
			colorPicker.onKeyChanged(list.get(0).getKey(), list.get(0).getColor());
		}
	}

	private class DataAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return list == null ? 0:list.size();
		}

		@Override
		public Object getItem(int position) {

			return list == null ? 0:list.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.colorpicker_selection_item, null);
				holder = new ViewHolder();
				holder.bkView = convertView.findViewById(R.id.colorpicker_selection_item_bk);
				holder.name = (TextView) convertView.findViewById(R.id.colorpicker_selection_item_name);
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.name.setText(list.get(position).getName());
			if (position == mSelectPosition) {
				holder.bkView.setBackgroundResource(R.drawable.colorpicker_frame_bk);
			}
			else {
				holder.bkView.setBackground(null);
			}
			holder.name.setTextColor(mTextColor);
			return convertView;
		}

	}

	private class ViewHolder {
		View bkView;
		TextView name;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		if (mSelectPosition != position) {
			mSelectPosition = position;
			colorPicker.onKeyChanged(list.get(position).getKey(), list.get(position).getColor());
			mAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 更改value值
	 * @param newColor
	 */
	public void updateColor(int newColor) {
		if (list != null) {
			list.get(mSelectPosition).setColor(newColor);
		}
	}

	public boolean onPickDone(OnColorPickerListener mListener) {

		if (list != null && list.size() > 0) {
			if (mListener != null) {
				mListener.onColorSelected(list);
			}
		}
		return false;
	}

	/**
	 * 编辑自身，加载相关属性
	 */
	public void loadSelfEditData() {
		if (mSelfList == null) {
			createSelfList();
		}
		setSelectionData(mSelfList);
	}

	/**
	 * 更改list文字颜色
	 * @param newColor
	 */
	public void updateListTextColor(int newColor) {
		mTextColor = newColor;
		mAdapter.notifyDataSetChanged();
	}

	public String getSelectionKey() {
		return list.get(mSelectPosition).getKey();
	}

	/**
	 * 编辑自身，保存修改
	 */
	public void saveUpdate() {
		if (mSelfList != null) {
			if (mResourceProvider != null) {
				for (int i = 0; i < mSelfList.size(); i ++) {
					mResourceProvider.updateColor(mSelfList.get(i).getKey(), mSelfList.get(i).getColor());
				}
				mResourceProvider.saveColorUpdate();
			}
		}
	}

	private void createSelfList() {
		mSelfList = new ArrayList<ColorPickerSelectionData>();
		ColorPickerSelectionData data = new ColorPickerSelectionData();
		data.setKey(ColorPickerRes.COLORPICKER_BACKGROUND);
		data.setName("背景");
		data.setColor(ResourceController.getColor(mContext.getResources()
				, ColorPickerRes.COLORPICKER_BACKGROUND
				, mResourceProvider.getColor(ColorPickerRes.COLORPICKER_BACKGROUND)));
		mSelfList.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorPickerRes.COLORPICKER_TITLE);
		data.setName("标题");
		data.setColor(ResourceController.getColor(mContext.getResources()
				, ColorPickerRes.COLORPICKER_TITLE
				, mResourceProvider.getColor(ColorPickerRes.COLORPICKER_TITLE)));
		mSelfList.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorPickerRes.COLORPICKER_DIVIDER);
		data.setName("分界线");
		data.setColor(ResourceController.getColor(mContext.getResources()
				, ColorPickerRes.COLORPICKER_DIVIDER
				, mResourceProvider.getColor(ColorPickerRes.COLORPICKER_DIVIDER)));
		mSelfList.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorPickerRes.COLORPICKER_LIST_TEXT);
		data.setName("List文字");
		data.setColor(ResourceController.getColor(mContext.getResources()
				, ColorPickerRes.COLORPICKER_LIST_TEXT
				, mResourceProvider.getColor(ColorPickerRes.COLORPICKER_LIST_TEXT)));
		mSelfList.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorPickerRes.COLORPICKER_FRAME_BORDER);
		data.setName("指示框边框");
		data.setColor(ResourceController.getColor(mContext.getResources()
				, ColorPickerRes.COLORPICKER_FRAME_BORDER
				, mResourceProvider.getColor(ColorPickerRes.COLORPICKER_FRAME_BORDER)));
		mSelfList.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorPickerRes.COLORPICKER_COLOR_TEXT);
		data.setName("普通文字");
		data.setColor(ResourceController.getColor(mContext.getResources()
				, ColorPickerRes.COLORPICKER_COLOR_TEXT
				, mResourceProvider.getColor(ColorPickerRes.COLORPICKER_COLOR_TEXT)));
		mSelfList.add(data);
	}

	public void setmResourceProvider(ResourceProvider mResourceProvider) {
		this.mResourceProvider = mResourceProvider;
	}
}
