package com.lxl.yuer.advance.photopicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lxl.yuer.advance.R;

import java.util.List;

/**
 * Created by Administrator on 2015/12/10.
 * 文件夹适配器
 */
public class PhotoDirectoryAdapter extends BaseAdapter{
    private List<String> photoDirectoryList;
    private Context mContext;
    public PhotoDirectoryAdapter(List<String> photoDirectoryList, Context context) {
        this.photoDirectoryList = photoDirectoryList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return photoDirectoryList.size();
    }

    @Override
    public String getItem(int position) {
        return photoDirectoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder=new Holder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_popup_window_list, null);
            holder.mTextView=(TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        holder.mTextView.setText(getItem(position));
        return convertView;
    }

    class Holder{
        TextView mTextView;
    }
}
