package com.project.dailyfeed;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;


import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

	static Toolbar toolbar;

	AutoCompleteTextView searchBox;

	ListView feedList;

	CardView searchView, loadingView, listView, countView, categoryView;

	TextView processingText, apiHits, feedSource, sortByName, sortByPrice,
			sortByRating;

	// private static final String LOG_TAG = "mainActivity";

	ArrayList<Feed> feedArrayLocal = new ArrayList<Feed>();

	ImageView clearButton;

	Button category;

	JSONObject object = null;

	FeedListAdapter adapter;

	int numberOfFeeds;
	String apiHit;

	CharSequence filterCategoryOptions[] = new CharSequence[] {};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		Utility.ApplicationContext = getApplicationContext();

		initialize();

	}

	private void initialize() {
		// TODO Auto-generated method stub

		searchBox = (AutoCompleteTextView) findViewById(R.id.search);

		searchView = (CardView) findViewById(R.id.card_view1);
		loadingView = (CardView) findViewById(R.id.card_view2);
		listView = (CardView) findViewById(R.id.card_view3);
		countView = (CardView) findViewById(R.id.card_view5);
		categoryView = (CardView) findViewById(R.id.card_view4);

		searchView.setVisibility(View.GONE);
		listView.setVisibility(View.GONE);
		countView.setVisibility(View.GONE);
		categoryView.setVisibility(View.GONE);

		processingText = (TextView) findViewById(R.id.processingText);
		apiHits = (TextView) findViewById(R.id.apiHits);
		feedSource = (TextView) findViewById(R.id.feedSource);

		feedList = (ListView) findViewById(R.id.feedList);

		clearButton = (ImageView) findViewById(R.id.clear);

		category = (Button) findViewById(R.id.category);

		setListAdapter(Feed.feedArray);

		setUpSearchBar();

		if (Utility.isNetworkAvailable(getApplicationContext())) {

			AsynGetFeeds object = new AsynGetFeeds();

			object.execute();
		} else {

			processingText.setText("Connection Error");

		}

		category.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);
				builder.setTitle("Category");
				builder.setItems(filterCategoryOptions,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// the user clicked on colors[which]

								String categoryString = filterCategoryOptions[which]
										.toString();

								ArrayList<Feed> feedArrayfiltered = new ArrayList<Feed>();

								if (categoryString.equalsIgnoreCase("All")) {

									setListAdapter(Feed.feedArray);

								}

								else {

									for (Feed item : Feed.feedArray) {
										if (item.getFeedCategory() != null
												&& item.getFeedCategory()
														.trim()
														.equalsIgnoreCase(
																categoryString
																		.trim())) {

											feedArrayfiltered.add(item);

										}
										// something here
									}

									setListAdapter(feedArrayfiltered);

								}

								category.setText("Category : " + categoryString);

							}
						});
				builder.show();

			}
		});

		feedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

				Intent intent = new Intent(MainActivity.this,
						ReadModeActivity.class);

				Feed item = (Feed) adapterView.getItemAtPosition(position);

				Feed f = new Feed(item.getFeedImage(), item.getFeedTitle(),
						item.getFeedSource(), item.getFeedCategory(), item
								.getFeedContent(), item.getFeedURL(), item
								.getFeedId());

				if (item.getBitmap() != null) {

					ByteArrayOutputStream bs = new ByteArrayOutputStream();
					item.getBitmap()
							.compress(Bitmap.CompressFormat.PNG, 50, bs);
					intent.putExtra("byteArray", bs.toByteArray());

				}

				Gson gson = new Gson();

				intent.putExtra("feed", gson.toJson(f));
				startActivity(intent);

			}
		});

	}

	private void setUpSearchBar() {
		// TODO Auto-generated method stub

		searchBox.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {

					clearButton.setVisibility(View.GONE);

				}

				if (hasFocus) {

					clearButton.setVisibility(View.VISIBLE);

					clearButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							searchBox.setText("");

						}
					});

				}
			}
		});

		searchBox.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				ArrayList<Feed> feedArrayfiltered = new ArrayList<Feed>();

				for (Feed item : Feed.feedArray) {
					if (item.getFeedTitle() != null
							&& ((item.getFeedTitle().toUpperCase(Locale
									.getDefault())).contains(s.toString()
									.toUpperCase(Locale.getDefault())) || (item
									.getFeedSource().toUpperCase(Locale
									.getDefault())).contains(s.toString()
									.toUpperCase(Locale.getDefault())))) {

						feedArrayfiltered.add(item);

					}
					// something here
				}

				setListAdapter(feedArrayfiltered);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	private boolean getCars() {
		// TODO Auto-generated method stub

		FeedAPI feedAPI = new FeedAPI();
		JSONArray feeds = null;
		String feedImage;

		String feedTitle, feedSource, feedCategory, feedContent, feedURL;

		try {

			object = feedAPI.getFeeds();

			feeds = object.getJSONArray("articles");

			numberOfFeeds = feeds.length();

			Feed.feedArray.clear();

			for (int i = 0; i < feeds.length(); i++) {

				feedTitle = (String) ((JSONObject) feeds.get(i)).get("title");

				feedSource = (String) ((JSONObject) feeds.get(i)).get("source");
				feedImage = (String) ((JSONObject) feeds.get(i)).get("image");

				feedCategory = (String) ((JSONObject) feeds.get(i))
						.get("category");

				feedContent = (String) ((JSONObject) feeds.get(i))
						.get("content");

				feedURL = (String) ((JSONObject) feeds.get(i)).get("url");

				Feed.feedArray.add(new Feed(feedImage, feedTitle, feedSource,
						feedCategory, feedContent, feedURL, i));

			}

			feedArrayLocal = Feed.feedArray;

		} catch (Exception E) {

			Log.e("Stack Trace", "Error Parsing JSON", E);

			return false;

		}
		return true;

	}

	private void getApiHits() {
		// TODO Auto-generated method stub

		FeedAPI feedsAPI = new FeedAPI();

		try {

			object = feedsAPI.getAPIHits();

			apiHit = (String) (object.get("api_hits"));

		} catch (Exception E) {

			Log.e("Stack Trace", "Error Parsing JSON", E);

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == R.id.bookmarks) {

			Intent i = new Intent(MainActivity.this, BookmarksActivity.class);

			startActivity(i);
		}

		if (id == android.R.id.home) {

			this.finish();
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	private class AsynGetFeeds extends AsyncTask<String, Integer, JSONObject> {

		boolean success;

		@Override
		protected void onPreExecute() {

			listView.setVisibility(View.GONE);
			searchView.setVisibility(View.GONE);
			countView.setVisibility(View.GONE);
			categoryView.setVisibility(View.GONE);

			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(JSONObject result) {

			super.onPostExecute(result);

			if (this.success) {

				listView.setVisibility(View.VISIBLE);
				searchView.setVisibility(View.VISIBLE);
				countView.setVisibility(View.VISIBLE);
				categoryView.setVisibility(View.VISIBLE);

				loadingView.setVisibility(View.GONE);

				adapter.notifyDataSetChanged();

				HashSet<String> uniqueSource = new HashSet<>();

				HashSet<String> uniqueCategory = new HashSet<>();

				for (Feed f : feedArrayLocal) {

					uniqueSource.add(f.getFeedSource());
					uniqueCategory.add(f.getFeedCategory());

				}

				uniqueCategory.add("All");

				filterCategoryOptions = uniqueCategory
						.toArray(new CharSequence[uniqueCategory.size()]);

				category.setText("Category : All");

				feedSource.setText("Feed Source : " + uniqueSource.size());

				apiHits.setText("API Hits: " + apiHit);

			} else {

				processingText.setText("An error occured");

			}

		}

		@Override
		protected JSONObject doInBackground(String... query) {

			this.success = getCars();

			getApiHits();

			return null;

		}

	};

	private void setListAdapter(ArrayList<Feed> carArray) {
		// TODO Auto-generated method stub

		adapter = new FeedListAdapter(MainActivity.this,
				R.layout.feed_list_item, carArray);

		feedList.setAdapter(adapter);
	}

}
