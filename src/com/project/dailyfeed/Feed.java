package com.project.dailyfeed;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class Feed {

	public static ArrayList<Feed> feedArray = new ArrayList<Feed>();

	private String feedImage;

	private Bitmap bitmap;

	private String feedTitle, feedSource, feedCategory, feedContent, feedURL;

	int feedId;

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public int getFeedId() {
		return feedId;
	}

	public void setFeedId(int feedId) {
		this.feedId = feedId;
	}

	public static ArrayList<Feed> getFeedArray() {
		return feedArray;
	}

	public static void setFeedArray(ArrayList<Feed> feedArray) {
		Feed.feedArray = feedArray;
	}

	public String getFeedImage() {
		return feedImage;
	}

	public void setFeedImage(String feedImage) {
		this.feedImage = feedImage;
	}

	public String getFeedTitle() {
		return feedTitle;
	}

	public void setFeedTitle(String feedTitle) {
		this.feedTitle = feedTitle;
	}

	public String getFeedSource() {
		return feedSource;
	}

	public void setFeedSource(String feedSource) {
		this.feedSource = feedSource;
	}

	public String getFeedCategory() {
		return feedCategory;
	}

	public void setFeedCategory(String feedCategory) {
		this.feedCategory = feedCategory;
	}

	public String getFeedContent() {
		return feedContent;
	}

	public void setFeedContent(String feedContent) {
		this.feedContent = feedContent;
	}

	public String getFeedURL() {
		return feedURL;
	}

	public void setFeedURL(String feedURL) {
		this.feedURL = feedURL;
	}

	public Feed(String feedImage, String feedTitle, String feedSource,
			String feedCategory, String feedContent, String feedURL, int feedId) {
		super();
		this.feedImage = feedImage;
		this.feedTitle = feedTitle;
		this.feedSource = feedSource;
		this.feedCategory = feedCategory;
		this.feedContent = feedContent;
		this.feedURL = feedURL;
		this.feedId = feedId;
	}

	public static Feed getFeedById(int feedId) {

		for (Feed feed : feedArray) {

			if (feed.getFeedId() == feedId) {

				return feed;
			}
		}
		return null;

	}

}
