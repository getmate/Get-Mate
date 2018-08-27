package com.getmate.getmate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.getmate.getmate.Adapter.FeedListAdapter;
import com.getmate.getmate.Class.Event;
import com.getmate.getmate.app.AppController;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class EventTimeline extends Fragment {
	private static final String TAG = MainActivity.class.getSimpleName();
	private ListView listView;
	private FeedListAdapter listAdapter;
	private List<Event> feedItems;
	private String URL_FEED = Constant.URL+"event/all";

	public static EventTimeline newInstance() {
		EventTimeline fragment = new EventTimeline();
		return fragment;
	}
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.timeline, container, false);

			listView = (ListView) v.findViewById(R.id.list);

			feedItems = new ArrayList<Event>();

			listAdapter = new FeedListAdapter(getActivity(), feedItems);
			listView.setAdapter(listAdapter);
		    Cache cache = AppController.getInstance().getRequestQueue().getCache();
			Entry entry = cache.get(URL_FEED);
			if (entry != null) {

				try {
					String data = new String(entry.data, "UTF-8");
					try {
						parseJsonFeed(new JSONObject(data));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

			} else {

				JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
						URL_FEED, null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						VolleyLog.d(TAG, "Response: " + response.toString());
						if (response != null) {
							parseJsonFeed(response);
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
					}
				});

				AppController.getInstance().addToRequestQueue(jsonReq);
			}
			return v;
	}

	private void parseJsonFeed(JSONObject response) {
		try {
			JSONArray feedArray = response.getJSONArray("eventlist");

			for (int i = 0; i < feedArray.length(); i++) {
				JSONObject feedObj = (JSONObject) feedArray.get(i);
				Gson gson = new Gson();
				Event eventitem = gson.fromJson(feedObj.toString(), Event.class);
				Log.e("Event",eventitem.getTitle()+" "+eventitem.getDescription());
				feedItems.add(eventitem);
			}
			listAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


}
