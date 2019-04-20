package com.lxl.advance.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.lxl.advance.R;

import java.util.HashMap;
import java.util.List;

/**
 * author:
 * 时间:2017/10/12
 * qq:1220289215
 * 类描述：
 */

public class ExpandAdapter extends BaseExpandableListAdapter {
    private final List<String> titles;
    private final HashMap<String,List<String>> mitems;

    public ExpandAdapter(List<String> titles, HashMap<String, List<String>> mitems) {
        this.titles = titles;
        this.mitems = mitems;
    }

    @Override
    public int getGroupCount() {
        return titles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String title = titles.get(groupPosition);
        return mitems.get(title).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return titles.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String title = titles.get(groupPosition);
        return mitems.get(title).get(childPosition);
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
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        String title = (String) getGroup(groupPosition);
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(title);
        textView.setTextColor(parent.getContext().getResources().getColor(R.color.colorPrimary));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        String title = (String) getChild(groupPosition,childPosition);
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(title);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
