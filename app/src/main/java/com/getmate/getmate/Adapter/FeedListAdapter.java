package com.getmate.getmate.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.getmate.getmate.Class.Event;
import com.getmate.getmate.FeedImageView;
import com.getmate.getmate.Profile;
import com.getmate.getmate.R;
import com.getmate.getmate.app.AppController;

import java.util.List;


public class FeedListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Event> feedItems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public FeedListAdapter(Activity activity, List<Event> feedItems) {
		this.activity = activity;
		this.feedItems = feedItems;
	}

	@Override
	public int getCount() {
		return feedItems.size();
	}

	@Override
	public Object getItem(int location) {
		return feedItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.feed_item, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView timestamp = (TextView) convertView
				.findViewById(R.id.timestamp);
		TextView statusMsg = (TextView) convertView
				.findViewById(R.id.txtStatusMsg);
		TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
		ImageView profilePic = (ImageView) convertView
				.findViewById(R.id.profilePic);
		TextView by_post = (TextView) convertView.findViewById(R.id.bypost);
		FeedImageView feedImageView = (FeedImageView) convertView
				.findViewById(R.id.feedImage1);

		Event item = feedItems.get(position);
		profilePic.setImageResource(item.getInterest_imageId());
		name.setText(item.getBy_post());
		by_post.setText(item.getTitle());
		url.setText(item.getInterestname());
		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
				Long.parseLong(item.getDate()+""),
				System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
		timestamp.setText(timeAgo);


		if (!TextUtils.isEmpty(item.getDescription())) {
			statusMsg.setText(item.getDescription());
			statusMsg.setVisibility(View.VISIBLE);
		} else {

			statusMsg.setVisibility(View.GONE);
		}


			url.setVisibility(View.GONE);


		if (item.getEvent_imageUrl() != null) {
			feedImageView.setImageUrl(item.getEvent_imageUrl(), imageLoader);
			feedImageView.setVisibility(View.VISIBLE);
			feedImageView
					.setResponseObserver(new FeedImageView.ResponseObserver() {
						@Override
						public void onError() {
						}

						@Override
						public void onSuccess() {
						}
					});
		} else {
			feedImageView.setVisibility(View.GONE);
		}

		return convertView;
	}

}
