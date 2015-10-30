package com.project.dailyfeed;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedListAdapter extends ArrayAdapter<Feed> {

	private ArrayList<Feed> objects;

	ImageDownloader imagedownloader = new ImageDownloader();

	CacheImage ci = CacheImage.getInstance();

	public FeedListAdapter(Context context, int textViewResourceId,
			ArrayList<Feed> objects) {

		super(context, textViewResourceId, objects);
		this.objects = objects;
	}

	public View getView(int position, View v, ViewGroup parent) {

		ViewHolder holder;

		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.feed_list_item, null);

			holder = new ViewHolder();

			holder.feedTitle = (TextView) v.findViewById(R.id.feedTitle);
			holder.feedImage = (ImageView) v.findViewById(R.id.feedImage);

			v.setTag(holder);

		} else {

			holder = (ViewHolder) v.getTag();
		}

		Feed i = objects.get(position);

		if (i != null) {

			holder.feedTitle.setText(i.getFeedTitle());

			Bitmap bitmap = i.getBitmap();

			if (bitmap == null) {

				Log.d("FeedListAdapter",
						"Trying Get from cache image " + i.getFeedId());

				bitmap = ci.getBitmapFromMemCache(i.getFeedId() + "");

				i.setBitmap(bitmap);
				notifyDataSetChanged();

			}
			if (bitmap != null) {

				holder.feedImage.setImageBitmap(bitmap);

			} else {

				Log.d("FeedListAdapter", "Download Image " + i.getFeedId());

				imagedownloader.download(i.getFeedImage(), holder.feedImage,
						i.getFeedId() + "");

			}
		}

		return v;

	}

	static class ViewHolder {

		TextView feedTitle;
		ImageView feedImage;

	}

}
