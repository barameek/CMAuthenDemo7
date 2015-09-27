package com.codemobiles.demo.authendemo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codemobiles.util.CMXmlJsonConvertor;
import com.codemobiles.util.CircleTransform;
import com.google.android.youtube.player.YouTubeIntents;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by maboho_retina on 3/7/15 AD.
 */
public class ListViewAdapter extends BaseAdapter {


    private LayoutInflater mInflater;
    private String youtubeID;
    private String title;
    private String desc;
    private String authorImageURL;
    private String youtubeImageURL;
    private Context mContext;
    public ArrayList<Object> feedDataList;


    public ListViewAdapter(Context context, ArrayList<Object> _feedDataList) {
        mInflater = LayoutInflater.from(context);
        feedDataList = _feedDataList;
        mContext = context;
    }

    public int getCount() {
        return feedDataList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_listview, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.item_listview_title);
            holder.description = (TextView) convertView.findViewById(R.id.item_listview_desc);
            holder.youtubeThumbnail = (ImageView) convertView.findViewById(R.id.item_listview_youtube_image);
            holder.authorImage = (ImageView) convertView.findViewById(R.id.item_listview_authorIcon);

            holder.youtubeThumbnail.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final String youtubeID = v.getTag().toString();
                    Intent intent = YouTubeIntents.createPlayVideoIntentWithOptions(mContext, youtubeID, true, false);
                    mContext.startActivity(intent);
                }
            });

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        title = CMXmlJsonConvertor.getValue(feedDataList.get(position), "title");
        desc = CMXmlJsonConvertor.getValue(feedDataList.get(position), "description");
        authorImageURL = CMXmlJsonConvertor.getValue(feedDataList.get(position), "image_link");
        youtubeID = CMXmlJsonConvertor.getValue(feedDataList.get(position), "youtubeID");
        youtubeImageURL = CMXmlJsonConvertor.getValue(feedDataList.get(position), "youtube_image");

        holder.title.setText(title);
        holder.description.setText(desc);

        Picasso.with(mContext).load(authorImageURL).transform(new CircleTransform()).into(holder.authorImage);
        Picasso.with(mContext).load(youtubeImageURL).into(holder.youtubeThumbnail);
        holder.youtubeThumbnail.setTag(youtubeID);


        return convertView;

    }


    public class ViewHolder {
        TextView title;
        ImageView authorImage;
        ImageView youtubeThumbnail;
        TextView description;
    }

}

