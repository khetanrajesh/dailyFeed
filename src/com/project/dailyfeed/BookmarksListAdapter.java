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

	public View getView(final int position, View v, ViewGroup parent) {

		ViewHolder holder;

		bookCarSavedData = getContext()
				.getSharedPreferences(
						getContext().getResources().getString(
								R.string.dailyFeedSavedData),
						getContext().MODE_PRIVATE);

		gson = new Gson();

		editor = bookCarSavedData.edit();

		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.bookmarks_list_item, null);
			holder = new ViewHolder();

			holder.feedTitle = (TextView) v.findViewById(R.id.feedTitle);

			holder.cancel = (ImageView) v.findViewById(R.id.cancel);

			v.setTag(holder);

		} else {

			holder = (ViewHolder) v.getTag();
		}

		Feed i = objects.get(position);

		if (i != null) {

			holder.feedTitle.setText(i.getFeedTitle());

			holder.cancel.setOnClickListener(new OnClickListener() {

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

		return v;

	}

	static class ViewHolder {

		TextView feedTitle;

		ImageView cancel;

	}
}
