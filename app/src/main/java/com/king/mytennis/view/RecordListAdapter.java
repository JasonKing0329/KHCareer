/**
 * 整体结构设计造成的缺陷：
 * 1.list本身是按日期升序排列的，为了按日期降序显示并未对list做反向排序
 * 而是在adapter中用
 * 			MyRecord record=list.get(list.size()-position-1);
 * 滚动listview列表而已，因此在look界面下其他操作如查看详细、修改、删除操作也要相应变换position
 *
 * 2.最初在适配list的时候，是从文件中读取全部记录（文字记录相比图片占用内存小得多），
 * 这样是为了省事。后来考虑到要加载头像，如果还按照之前庞大的数据记录顺序加载头像，会造成
 * 内存严重浪费。有很多浏览不到的记录根本不需要缓存头像bitmap。因此设计加载更多显示模式，让
 * 每一次点击加载更多时才加载新内容的头像，这样大大减少了内存消耗。
 */
package com.king.mytennis.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.king.mytennis.model.Record;
import com.king.mytennis.multiuser.MultiUserManager;
import com.king.mytennis.view.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * record list的适配器
 * 解决两种显示模式——完全显示模式，部分显示模式（“加载更多”）
 * @author king
 *
 */
@Deprecated
public class RecordListAdapter extends BaseAdapter{

	public static final int EACHMAXSIZE = 20;
	private int curSize;

	private boolean isUseLoadMore;

	private ViewHolder vholder;
	private LayoutInflater inflater;
	private List<Record> list;
	private HashMap<String, Bitmap> headMap;
	private Context context;

	public RecordListAdapter(Context context, List<Record> list){
		this.context = context;
		this.list=list;
		if (list != null && list.size() < EACHMAXSIZE) {
			curSize = list.size();
			isUseLoadMore = false;
		}
		else {
			curSize = EACHMAXSIZE + 1;
			isUseLoadMore = true;
		}
		inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		headMap = new HashMap<String, Bitmap>();
	}

	public void setHeadMap(HashMap<String, Bitmap> map) {
		headMap = map;
	}

	/**
	 * 每当list整体改变时（执行了搜索或插入或删除操作）
	 * 不仅重新适配list，还要重新决定加载更多显示模式
	 * @param list
	 */
	public void setRecordList(List<Record> list) {
		this.list = list;
		if (list != null && list.size() < EACHMAXSIZE) {
			curSize = list.size();
			isUseLoadMore = false;
		}
		else {
			curSize = EACHMAXSIZE + 1;
			isUseLoadMore = true;
		}
	}

	public boolean isLoadMore(int position) {
		if (position == curSize - 1 && position != list.size()) {
			return true;
		}
		return false;
	}

	public void loadMore() {
		if (--curSize < list.size()) {//由于已有加载更多这一项，要先减一
			curSize += EACHMAXSIZE;
			if (curSize < list.size()) {//还有更多则继续显示加载更多
				curSize ++;
			}
			else {//否则一定要设为list的大小，不能超过
				curSize = list.size();
			}
		}
	}

	/**
	 * 通过这里结合loadMore控制加载更多的实现
	 */
	@Override
	public int getCount() {
		if (isUseLoadMore) {
			return curSize;
		}
		else {
			return list.size();
		}
	}

	/**
	 * 解决两种显示模式——完全显示模式，部分显示模式（“加载更多”）
	 * 这里是开关，决定是否启用“加载更多”显示模式
	 * @param is
	 */
	public void setUseLoadMore(boolean is) {
		isUseLoadMore = is;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (isUseLoadMore) {
			/**加载更多**/
			if (position == curSize - 1 && position != list.size() - 1) {
				convertView = inflater.inflate(R.layout.list_more, null);
				return convertView;
			}

		}

		/**普通record**/
		Record record=list.get(list.size()-position-1);
		if(convertView==null){//convertView就是ListView中的每一行显示，第一次需要findviewbyid，调用此方法费时
			vholder=new ViewHolder();//每个View都需要new一个holder
			convertView=inflater.inflate(R.layout.style_recordinlist, null);
			initConvertView(vholder, convertView);
			convertView.setTag(vholder);//相当于互相等同了，这样后convertView中就有了holder信息
		}
		else{//第二次往后就不用findviewbyid，直接在holder中找就可以了，效率高了

			vholder=(ViewHolder)convertView.getTag();

			if (vholder == null) {//经常会出现获取失败
				Log.i("123", "" + position);
				vholder=new ViewHolder();
				convertView=inflater.inflate(R.layout.style_recordinlist, null);
				initConvertView(vholder, convertView);
				convertView.setTag(vholder);
			}
		}
		Bitmap bitmap = null;
		try {
			bitmap = headMap.get(record.getCompetitor());
		} catch (Exception e) {
			bitmap = null;
		}
		if (bitmap != null) {
			vholder.iv_icon.setBackgroundDrawable(new BitmapDrawable(bitmap));
		}
		else {
			vholder.iv_icon.setBackgroundResource(R.drawable.icon_list);
		}
		vholder.tv_compname.setText(record.getCompetitor()+"("
				+record.getCptRank()+"/"+record.getCptSeed()+")");
		vholder.tv_court.setText(record.getCourt());
		vholder.tv_level.setText(record.getLevel());
		vholder.tv_matchname.setText(record.getMatch());
		vholder.tv_round.setText(record.getRound());
		vholder.tv_score.setText(record.getScore());
		vholder.tv_time.setText(record.getStrDate());
		//vholder.tv_winner.setText(record.isWinner() ? MultiUserManager.getInstance().getCurrentUser():record.getCompetitor());
		if (record.getCourt().contains("硬地")) {
			convertView.setBackgroundColor(context.getResources().getColor(R.color.court_hard));
		}
		else if (record.getCourt().equals("红土")) {
			convertView.setBackgroundColor(context.getResources().getColor(R.color.court_clay));
		}
		else if (record.getCourt().equals("草地")) {
			convertView.setBackgroundColor(context.getResources().getColor(R.color.court_grass));
		}
		//vholder.iv_icon.setImageBitmap(bitmap);
		return convertView;
	}

	private void initConvertView(ViewHolder holder, View view) {

		vholder.iv_icon=(ImageView)view.findViewById(R.id.listview_icon);
		vholder.tv_compname=(TextView)view.findViewById(R.id.listview_compname);
		vholder.tv_court=(TextView)view.findViewById(R.id.listview_court);
		vholder.tv_level=(TextView)view.findViewById(R.id.listview_level);
		vholder.tv_matchname=(TextView)view.findViewById(R.id.listview_matchname);
		vholder.tv_round=(TextView)view.findViewById(R.id.listview_round);
		vholder.tv_score=(TextView)view.findViewById(R.id.listview_score);
		vholder.tv_time=(TextView)view.findViewById(R.id.listview_time);
		vholder.tv_winner=(TextView)view.findViewById(R.id.listview_winner);
	}

	class ViewHolder{
		ImageView iv_icon;
		TextView tv_compname,tv_court,tv_level,tv_matchname,tv_round,tv_winner
				,tv_score,tv_time;
	}
}
