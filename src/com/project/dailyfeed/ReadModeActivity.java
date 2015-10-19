package com.project.dailyfeed;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReadModeActivity extends AppCompatActivity {

	static Toolbar toolbar;

	CardView detailsView, contentView;

	ImageView collapse, feedImage, bookmark, feedURL, share;

	TextView feedTitle, feedContent;

	LinearLayout options;

	Feed feed;

	Gson gson = new Gson();

	ImageDownloader imagedownloader = new ImageDownloader();

	CacheImage ci = CacheImage.getInstance();

	SharedPreferences savedData;
	Editor editor;

	Boolean isBookmarked = false;

	ArrayList<Feed> bookmarksArray = new ArrayList<Feed>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read_feed);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		Bundle extras = getIntent().getExtras();

		if (extras != null) {

			String s = extras.getString("feed");

			if (s != null) {

				Type type = new TypeToken<Feed>() {
				}.getType();
				feed = gson.fromJson(s, type);

			}

		}

		if (feed != null) {

			initialize();

		}

	}

	private void initialize() {

		detailsView = (CardView) findViewById(R.id.card_view1);
		contentView = (CardView) findViewById(R.id.card_view3);

		options = (LinearLayout) findViewById(R.id.options);

		collapse = (ImageView) findViewById(R.id.collapse);
		feedImage = (ImageView) findViewById(R.id.feedImage);
		feedTitle = (TextView) findViewById(R.id.feedTitle);
		bookmark = (ImageView) findViewById(R.id.bookmark);
		feedURL = (ImageView) findViewById(R.id.feedURL);
		share = (ImageView) findViewById(R.id.share);
		feedContent = (TextView) findViewById(R.id.feedContent);

		feedContent.setMovementMethod(new ScrollingMovementMethod());

		feedTitle.setText(feed.getFeedTitle());

		setUpBookmark();

		if (feedImage != null) {

			Bitmap bitmap = null;

			if (getIntent().hasExtra("byteArray")) {

				bitmap = BitmapFactory.decodeByteArray(getIntent()
						.getByteArrayExtra("byteArray"), 0, getIntent()
						.getByteArrayExtra("byteArray").length);

			}

			if (bitmap == null) {

				bitmap = ci.getBitmapFromMemCache(feed.getFeedId() + "");

			}
			if (bitmap != null) {

				feedImage.setImageBitmap(bitmap);

			} else {

				imagedownloader.download(feed.getFeedImage(), feedImage,
						feed.getFeedId() + "");

			}
		}

		feedContent.setText(feed.getFeedContent());

		collapse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (feedImage.getVisibility() == View.VISIBLE) {

					feedImage.setVisibility(View.GONE);
					options.setVisibility(View.GONE);

					collapse.setImageResource(R.drawable.ic_down);

				} else {

					feedImage.setVisibility(View.VISIBLE);
					options.setVisibility(View.VISIBLE);
					collapse.setImageResource(R.drawable.ic_up);

				}

			}
		});

		feedURL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent getURL = new Intent(Intent.ACTION_VIEW, Uri.parse(feed
						.getFeedURL()));
				startActivity(getURL);

			}
		});

		final String messageText = "Title: " + feed.getFeedTitle() + '\n'
				+ "Source: " + feed.getFeedSource() + '\n' + "Category: "
				+ feed.getFeedCategory() + '\n' + "ImageURL: "
				+ feed.getFeedImage() + '\n' + "URL: " + feed.getFeedURL();

		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, messageText);
				sendIntent.setType("text/plain");
				startActivity(sendIntent);

			}
		});

		bookmark.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (isBookmarked) {

					Utility.displayToast("Already Bookmarked");

				} else {

					Utility.displayToast("Added to Bookmarks");

					bookmark.setImageResource(R.drawable.ic_bookmark_black);

					isBookmarked = true;

					addToBookmarks();
				}

			}
		});

	}

	protected void addToBookmarks() {
		// TODO Auto-generated method stub

		bookmarksArray.add(feed);

		editor = savedData.edit();

		editor.putString(getResources().getString(R.string.bookmarks),
				gson.toJson(bookmarksArray));

		editor.commit();

	}

	@SuppressWarnings("unchecked")
	private void setUpBookmark() {
		// TODO Auto-generated method stub

		savedData = getSharedPreferences(
				getResources().getString(R.string.dailyFeedSavedData),
				MODE_PRIVATE);

		String bookmarks = savedData.getString(
				getResources().getString(R.string.bookmarks), null);

		if (bookmarks != null) {

			Type type2 = new TypeToken<ArrayList<Feed>>() {
			}.getType();

			Object obj = gson.fromJson(bookmarks, type2);

			bookmarksArray = (ArrayList<Feed>) obj;

			for (Feed f : bookmarksArray) {

				if (f.getFeedTitle().trim()
						.equalsIgnoreCase(feed.getFeedTitle().trim())) {

					isBookmarked = true;

					bookmark.setImageResource(R.drawable.ic_bookmark_black);

					break;

				}
			}

		}
	}

}
