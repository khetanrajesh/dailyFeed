package com.project.dailyfeed;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class BookmarksActivity extends AppCompatActivity {

	Toolbar toolbar;

	ListView bookmarksList;

	ArrayList<Feed> feeds = new ArrayList<Feed>();
	ArrayList<Feed> bookmarks = new ArrayList<Feed>();
	BookmarksListAdapter adapter;

	SharedPreferences savedData;
	Editor editor;
	Gson gson;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmarks);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		initialize();

	}

	@SuppressWarnings("unchecked")
	private void initialize() {
		// TODO Auto-generated method stub

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		savedData = getSharedPreferences(
				getResources().getString(R.string.dailyFeedSavedData),
				MODE_PRIVATE);
		bookmarksList = (ListView) findViewById(R.id.bookmarksList);

		gson = new Gson();

		editor = savedData.edit();

		String bookmarksString = savedData.getString(
				getResources().getString(R.string.bookmarks), null);

		if (bookmarksString != null) {

			Type type2 = new TypeToken<ArrayList<Feed>>() {
			}.getType();

			Object obj = gson.fromJson(bookmarksString, type2);

			bookmarks = (ArrayList<Feed>) obj;

			adapter = new BookmarksListAdapter(BookmarksActivity.this,
					R.layout.bookmarks_list_item, bookmarks);

			bookmarksList.setAdapter(adapter);

			bookmarksList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapterView, View view,
						int position, long id) {
					// TODO Auto-generated method stub

					Intent intent = new Intent(BookmarksActivity.this,
							ReadModeActivity.class);

					Feed item = (Feed) bookmarks.get(position);

					Feed f = new Feed(item.getFeedImage(), item.getFeedTitle(),
							item.getFeedSource(), item.getFeedCategory(), item
									.getFeedContent(), item.getFeedURL(), item
									.getFeedId());

					if (item.getBitmap() != null) {

						ByteArrayOutputStream bs = new ByteArrayOutputStream();
						item.getBitmap().compress(Bitmap.CompressFormat.PNG,
								50, bs);
						intent.putExtra("byteArray", bs.toByteArray());

					}

					Gson gson = new Gson();

					intent.putExtra("feed", gson.toJson(f));
					startActivity(intent);

				}

			});

			if (bookmarks.size() == 0) {

				bookmarksList.setVisibility(View.GONE);

				TextView v = (TextView) findViewById(R.id.noBookmarks);
				v.setVisibility(View.VISIBLE);
			}

		} else {
			bookmarksList.setVisibility(View.GONE);

			TextView v = (TextView) findViewById(R.id.noBookmarks);
			v.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == android.R.id.home) {

			this.finish();
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

}
