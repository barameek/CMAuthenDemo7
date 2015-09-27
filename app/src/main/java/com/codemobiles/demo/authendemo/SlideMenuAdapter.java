package com.codemobiles.demo.authendemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SlideMenuAdapter extends BaseAdapter {

    private final String[] slide_menu_titles;
    private final TypedArray slide_menu_iconImageViews;
    private LayoutInflater mInflater;
    private Context mContext;

    public SlideMenuAdapter(Context context) {

        mContext = context;
        mInflater = LayoutInflater.from(context);
        slide_menu_titles = mContext.getResources().getStringArray(R.array.slide_menu_titles);
        slide_menu_iconImageViews = mContext.getResources().obtainTypedArray(R.array.slide_menu_icon);

    }

    public int getCount() {
        return slide_menu_titles.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.drawer_listview_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.drawer_icon);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.drawer_title);
            holder.profileImageView = (ImageView) convertView.findViewById(R.id.drawer_profileImage);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == 0) {
            // VISIBLE, INVISIBLE, GONE
            setHolderVisibility(View.GONE, holder);
            holder.profileImageView.setVisibility(View.VISIBLE);
        } else {
            setHolderVisibility(View.VISIBLE, holder);
            holder.profileImageView.setVisibility(View.GONE);
            holder.iconImageView.setImageResource(slide_menu_iconImageViews.getResourceId(position, -1));
            holder.titleTextView.setText(slide_menu_titles[position]);
        }

        return convertView;

    }


    public void setHolderVisibility(int code, ViewHolder viewHolder) {
        viewHolder.iconImageView.setVisibility(code);
        viewHolder.titleTextView.setVisibility(code);
    }

    public class ViewHolder {
        ImageView iconImageView;
        TextView titleTextView;
        ImageView profileImageView;
    }
}
