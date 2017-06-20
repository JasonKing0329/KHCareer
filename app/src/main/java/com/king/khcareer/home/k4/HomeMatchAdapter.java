package com.king.khcareer.home.k4;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.khcareer.match.gallery.UserMatchBean;
import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.common.image.ImageUtil;
import com.king.mytennis.view.R;

import java.util.List;

/**
 * Created by Administrator on 2017/4/4 0004.
 */

public class HomeMatchAdapter extends RecyclerView.Adapter<HomeMatchAdapter.ItemHolder> {

    private List<UserMatchBean> list;
    private View.OnClickListener itemOnclickListener;

    public HomeMatchAdapter(List<UserMatchBean> list) {
        this.list = list;
    }

    @Override
    public HomeMatchAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeMatchAdapter.ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_match, parent, false));
    }

    @Override
    public void onBindViewHolder(HomeMatchAdapter.ItemHolder holder, int position) {
        UserMatchBean bean = list.get(position);
        String filePath = ImageFactory.getMatchHeadPath(bean.getNameBean().getName()
                , bean.getNameBean().getMatchBean().getCourt());
        ImageUtil.load("file://" + filePath, holder.image, R.drawable.default_img);
        holder.level.setText(bean.getNameBean().getMatchBean().getLevel());
        holder.court.setText(bean.getNameBean().getMatchBean().getCourt());
        holder.name.setText(bean.getNameBean().getName());
        holder.group.setTag(position);
        holder.group.setOnClickListener(itemOnclickListener);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0:list.size();
    }

    public void setDatas(List<UserMatchBean> datas) {
        this.list = datas;
    }

    public void setItemOnclickListener(View.OnClickListener itemOnclickListener) {
        this.itemOnclickListener = itemOnclickListener;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView level, court, name;
        ViewGroup group;
        public ItemHolder(View convertView) {
            super(convertView);
            group = (ViewGroup) convertView.findViewById(R.id.match_group);
            level = (TextView) convertView.findViewById(R.id.home_match_level);
            court = (TextView) convertView.findViewById(R.id.home_match_court);
            name = (TextView) convertView.findViewById(R.id.home_match_name);
            image = (ImageView) convertView.findViewById(R.id.home_match_image);
        }
    }
}
