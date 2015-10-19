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

	public View getView(int position, View convertView, ViewGroup parent) {

		// assign the view we are converting to a local variable
		View v = convertView;

		// first check to see if the view is null. if so, we have to inflate it.
		// to inflate it basically means to render, or show, the view.
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.feed_list_item, null);
		}

		/*
		 * Recall that the variable position is sent in as an argument to this
		 * method. The variable simply refers to the position of the current
		 * object in the list. (The ArrayAdapter iterates through the list we
		 * sent it)
		 * 
		 * Therefore, i refers to the current Item object.
		 */
		Feed i = objects.get(position);

		if (i != null) {

			// This is how you obtain a reference to the TextViews.
			// These TextViews are created in the XML files we defined.

			TextView feedTitle = (TextView) v.findViewById(R.id.feedTitle);
			ImageView feedImage = (ImageView) v.findViewById(R.id.feedImage);

			// check to see if each individual textview is null.
			// if not, assign some text!
			if (feedTitle != null) {
				feedTitle.setText(i.getFeedTitle());
			}

			if (feedImage != null) {

				Bitmap bitmap = i.getBitmap();

				if (bitmap == null) {

					Log.d("FeedListAdapter",
							"Trying Get from cache image " + i.getFeedId());

					bitmap = ci.getBitmapFromMemCache(i.getFeedId() + "");

					i.setBitmap(bitmap);
					notifyDataSetChanged();

				}
				if (bitmap != null) {

					feedImage.setImageBitmap(bitmap);

				} else {

					Log.d("FeedListAdapter", "Download Image " + i.getFeedId());

					imagedownloader.download( i.getFeedImage(),
							feedImage, i.getFeedId() + "");

				}
			}

		}

		// the view must be returned to our activity
		return v;

	}

}
