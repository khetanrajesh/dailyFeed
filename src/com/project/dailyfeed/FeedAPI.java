package com.project.dailyfeed;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class FeedAPI {

	private static final String LOG_TAG = "FeedAPI";
	private static final String API_BASE = "http://dailyhunt.0x10.info/api";
	private static final String Resource = "/dailyhunt";

	public JSONObject getFeeds() {

		JSONObject x = callBackend("list_news");

		return x;

	}

	public JSONObject getAPIHits() {

		JSONObject x = callBackend("api_hits");

		return x;

	}

	protected JSONObject callBackend(String query) {

		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(API_BASE + Resource);

			sb.append("?type=json");
			sb.append("&query=" + query);

			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			// Load the results into a StringBuilder
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			Log.e(LOG_TAG, "Error processing feed API URL", e);
			return null;
		} catch (IOException e) {
			Log.e(LOG_TAG, "Error connecting to feed  API", e);
			return null;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		try {
			// Create a JSON object hierarchy from the results
			JSONObject jsonObj = new JSONObject(jsonResults.toString());

			Log.d("Result", jsonObj.toString());

			return jsonObj;
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Cannot process JSON results", e);
		}

		return null;
		// TODO Auto-generated method stub

	}

}
