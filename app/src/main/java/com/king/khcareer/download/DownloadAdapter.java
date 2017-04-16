package com.king.khcareer.download;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.king.khcareer.utils.FileSizeUtil;
import com.king.mytennis.view.R;

import java.util.List;

/**
 * Created by Administrator on 2016/9/2.
 */
public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ItemHolder> {

    private Context mContext;
    private List<DownloadItemProxy> itemList;

    public DownloadAdapter(Context context, List<DownloadItemProxy> itemList) {
        mContext = context;
        this.itemList = itemList;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_download_list, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0:itemList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder implements Handler.Callback
    {

        private TextView name;
        private TextView size;
        private NumberProgressBar progressBar;

        public ItemHolder(View view)
        {
            super(view);
            name = (TextView) view.findViewById(R.id.download_item_name);
            size = (TextView) view.findViewById(R.id.download_item_size);
            progressBar = (NumberProgressBar) view.findViewById(R.id.download_item_progressbar);
        }

        public void bind(int position) {
            name.setText(itemList.get(position).getItem().getName());
            size.setText(FileSizeUtil.convertFileSize(itemList.get(position).getItem().getSize()));
            progressBar.setProgress(itemList.get(position).getProgress());
        }

        @Override
        public boolean handleMessage(Message msg) {
            progressBar.setProgress(msg.what);
            return false;
        }
    }


}
