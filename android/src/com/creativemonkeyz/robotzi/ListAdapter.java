package com.creativemonkeyz.robotzi;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.HashMap;

public class ListAdapter extends BaseAdapter {
	private static LayoutInflater inflater;

	private Activity activity;
	private HashMap<Integer, Item> items;
    private int layoutId;

	public ListAdapter(Activity activity, HashMap<Integer, Item> items, int layoutId) {
        this.activity = activity;
        this.items = items;
        this.layoutId = layoutId;

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(activity).defaultDisplayImageOptions(defaultOptions).build());
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Item getItem(int position) {
        return items.get(items.size() - position);
	}

    @Override
    public long getItemId(int position) {
        return position;
    }

	@Override
	public View getView(int position, View view, ViewGroup parent) {
        Item item = getItem(position);
        ViewHolder holder = new ViewHolder();

        if (view == null) {
            view = inflater.inflate(layoutId, null);

            holder.episode = (TextView) view.findViewById(R.id.episode);
            holder.season = (TextView) view.findViewById(R.id.season);
            holder.imageView = (ImageView) view.findViewById(R.id.preview);
            view.setTag(holder);

        } else
            holder = (ViewHolder) view.getTag();

        switch (layoutId) {
            case R.layout.list_ep_pics:
                ImageLoader.getInstance().displayImage(item.previewLink, holder.imageView);

                holder.episode.setText(item.episode);
                holder.episode.setText(item.season);

                break;

            case R.layout.list_walls:


                break;
        }

		return view;
	}

    private static class ViewHolder {
        TextView episode;
        TextView season;
        ImageView imageView;
    }
}