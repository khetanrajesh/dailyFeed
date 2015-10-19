package com.project.dailyfeed;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.project.dailyfeed.CacheImage;
import com.project.dailyfeed.ImageDownloader;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BookmarksListAdapter extends ArrayAdapter<Feed> {

	private ArrayList<Feed> objects;

	SharedPreferences bookCarSavedData;
	Editor editor;

	Gson gson;

	ImageDownloader imagedownloader = new ImageDownloader();

	CacheImage ci = CacheImage.getInstance();

	public BookmarksListAdapter(Context context, int textViewResourceId,
			ArrayList<Feed> objects) {

		super(context, textViewResourceId, objects);
		this.objects = objects;

	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		// assign the view we are converting to a local variable
		View v = convertView;

		bookCarSavedData = getContext()
				.getSharedPreferences(
						getContext().getResources().getString(
								R.string.dailyFeedSavedData),
						getContext().MODE_PRIVATE);

		gson = new Gson();

		editor = bookCarSavedData.edit();

		// first check to see if the view is null. if so, we have to inflate it.
		// to inflate it basically means to render, or show, the view.
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.bookmarks_list_item, null);
		}

		Feed i = objects.get(position);

		if (i != null) {

			TextView feedTitle = (TextView) v.findViewById(R.id.feedTitle);

			ImageView cancel = (ImageView) v.findViewById(R.id.cancel);

			feedTitle.setText(i.getFeedTitle());

			cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					objects.remove(position);

					editor.putString(
							getContext().getResources().getString(
									R.string.bookmarks), gson.toJson(objects));

					editor.commit();

					Utility.displayToast("Bookmark Removed");

					notifyDataSetChanged();

				}
			});
		}

		// the view must be returned to our activity
		return v;

	}
}
