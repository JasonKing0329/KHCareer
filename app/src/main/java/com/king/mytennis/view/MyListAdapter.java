/**
 * list本身是按日期升序排列的，为了按日期降序显示并未对list做反向排序
 * 而是在adapter中用
 * 			MyRecord record=list.get(list.size()-position-1);
 * 滚动listview列表而已，因此在look界面下其他操作如查看详细、修改、删除操作也要相应变换position
 */
package com.king.mytennis.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.king.mytennis.model.Record;
import com.king.mytennis.multiuser.MultiUserManager;
import com.king.mytennis.view.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@Deprecated
public class MyListAdapter extends BaseAdapter{

	private ViewHolder vholder;
	private LayoutInflater inflater;
	private ArrayList<Record> list;
	private HashMap<String, Bitmap> headMap;
	private Context context;

	public MyListAdapter(Context context, ArrayList<Record> list){
		this.context = context;
		this.list=list;
		inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		headMap = new HashMap<String, Bitmap>();
//		String img_url="/sdcard/Djokovic@Toronto.jpg";
//		try {
//			InputStream stream=new FileInputStream(img_url);
//			bitmap=BitmapFactory.decodeStream(stream);
//
//		} catch (FileNotFoundException e) {
//			System.out.println("文件不存在");
//			e.printStackTrace();
//		}
/**
 * 下面的用来限制图片的显示大小，不知道为什么不行
 */
//		BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true; // 设置了此属性一定要记得将值设置为false
//		Bitmap bitmap =BitmapFactory.decodeFile(img_url, options);
//		options.inJustDecodeBounds = false;
//		//int be = options.outHeight/40;
//		options.inSampleSize = 60;
//		bitmap = BitmapFactory.decodeFile(img_url,options);
	}

	public void setHeadMap(HashMap<String, Bitmap> map) {
		headMap = map;
	}

	public void setRecordList(ArrayList<Record> list) {
		this.list = list;
	}

	public void clear() {
		if (list != null) {
			list.clear();
		}
	}

	@Override
	public int getCount() {
		return list.size();
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

		Record record=list.get(list.size()-position-1);
		if(convertView==null){//convertView就是ListView中的每一行显示，第一次需要findviewbyid，调用此方法费时
			convertView=inflater.inflate(R.layout.style_recordinlist, null);
			vholder=new ViewHolder();//每个View都需要new一个holder
			vholder.iv_icon=(ImageView)convertView.findViewById(R.id.listview_icon);
			vholder.tv_compname=(TextView)convertView.findViewById(R.id.listview_compname);
			vholder.tv_court=(TextView)convertView.findViewById(R.id.listview_court);
			vholder.tv_level=(TextView)convertView.findViewById(R.id.listview_level);
			vholder.tv_matchname=(TextView)convertView.findViewById(R.id.listview_matchname);
			vholder.tv_round=(TextView)convertView.findViewById(R.id.listview_round);
			vholder.tv_score=(TextView)convertView.findViewById(R.id.listview_score);
			vholder.tv_time=(TextView)convertView.findViewById(R.id.listview_time);
			vholder.tv_winner=(TextView)convertView.findViewById(R.id.listview_winner);
			convertView.setTag(vholder);//相当于互相等同了，这样后convertView中就有了holder信息
		}
		else{//第二次往后就不用findviewbyid，直接在holder中找就可以了，效率高了
			vholder=(ViewHolder)convertView.getTag();
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

	class ViewHolder{
		ImageView iv_icon;
		TextView tv_compname,tv_court,tv_level,tv_matchname,tv_round,tv_winner
				,tv_score,tv_time;
	}
}
