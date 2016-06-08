package com.ilp.ilpschedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ilp.ilpschedule.adapter.BadgeAdapter;
import com.ilp.ilpschedule.model.Badge;
import com.ilp.ilpschedule.model.Employee;
import com.ilp.ilpschedule.util.Constants;
import com.ilp.ilpschedule.util.Util;

/**
 * {@link BadgeFragment} is a {@link Fragment} that manages the actions of
 * related to {@link Badge} model
 * 
 * @author Zeeshan Khan
 * 
 */
public class BadgeFragment extends Fragment {
	public static final String TAG = "com.tcs.myilp.BadgeFragment";
	private RequestQueue reqQue;
	private TextView textViewBadgeMyPoint, textViewBadgeDesc;
	private ImageView imageViewBadge;
	private BadgeAdapter badgeAdapter;
	private ListView listViewBadges;

	private void setMyBadges(int point) {
		if (point >= 100) {
			imageViewBadge.setImageResource(R.drawable.badge_karma_king);
			textViewBadgeMyPoint.setText(new StringBuilder(
					getString(R.string.badge_karma_king))
					.append(Constants.SPACE).append(point)
					.append(Constants.SPACE)
					.append(getString(R.string.badge_points)).toString());

		} else if (point >= 60) {
			imageViewBadge.setImageResource(R.drawable.badge_karma_leader);
			textViewBadgeMyPoint.setText(new StringBuilder(
					getString(R.string.badge_karma_leader))
					.append(Constants.SPACE).append(point)
					.append(Constants.SPACE)
					.append(getString(R.string.badge_points)).toString());

			textViewBadgeDesc
					.setText(new StringBuilder(
							getString(R.string.badge_you_need))
							.append(Constants.SPACE).append(100 - point)
							.append(Constants.SPACE)
							.append(getString(R.string.badge_more_for_king))
							.toString());
		} else if (point >= 30) {
			imageViewBadge.setImageResource(R.drawable.badge_karma_empower);
			textViewBadgeMyPoint.setText(new StringBuilder(
					getString(R.string.badge_karma_empower))
					.append(Constants.SPACE).append(point)
					.append(Constants.SPACE)
					.append(getString(R.string.badge_points)).toString());

			textViewBadgeDesc.setText(new StringBuilder(
					getString(R.string.badge_you_need)).append(Constants.SPACE)
					.append(60 - point).append(Constants.SPACE)
					.append(getString(R.string.badge_more_for_leader))
					.toString());
		} else if (point >= 15) {
			imageViewBadge.setImageResource(R.drawable.badge_karma_warrior);
			textViewBadgeMyPoint.setText(new StringBuilder(
					getString(R.string.badge_karma_warrior))
					.append(Constants.SPACE).append(point)
					.append(Constants.SPACE)
					.append(getString(R.string.badge_points)).toString());
			textViewBadgeDesc.setText(new StringBuilder(
					getString(R.string.badge_you_need)).append(Constants.SPACE)
					.append(30 - point).append(Constants.SPACE)
					.append(getString(R.string.badge_more_for_empower))
					.toString());

		} else {
			imageViewBadge.setImageResource(R.drawable.badge_nobadge);
			textViewBadgeMyPoint.setText(new StringBuilder(
					getString(R.string.badge_you_earn)).append(Constants.SPACE)
					.append(point).append(Constants.SPACE)
					.append(getString(R.string.badge_points)).toString());
			textViewBadgeDesc.setText(new StringBuilder(
					getString(R.string.badge_you_need)).append(Constants.SPACE)
					.append(15 - point).append(Constants.SPACE)
					.append(getString(R.string.badge_more_for_warrior))
					.toString());
		}
	}

	private Listener<String> fetchBadgesSuccessListner = new Listener<String>() {

		@Override
		public void onResponse(String result) {
			try {
				List<Badge> badges = new ArrayList<>();
				JSONObject jobj = new JSONObject(result);
				JSONArray jarr;
				if (jobj.has("Android")) {
					jarr = jobj.getJSONArray("Android");
					int length = jarr.length();
					if (length > 0) {
						Badge badge;
						Employee emp = Util.getEmployee(getActivity());
						for (int i = 0; i < length; i++) {
							jobj = jarr.getJSONObject(i);
							if (jobj.has("emp_id")) {
								badge = new Badge();
								badge.setName(jobj.optString("emp_name"));
								badge.setPoints(jobj.optInt("count"));
								if (jobj.optLong("emp_id") == emp.getEmpId()) {
									int points = badge.getPoints();
									Util.saveMyPoints(getActivity(), points);
									setMyBadges(points);
								}
								badges.add(badge);
							}
						}

					}
				}
				Log.d(TAG, badges.toString());
				if (badgeAdapter != null)
					badgeAdapter.addData(badges);
			} catch (Exception ex) {
				Log.d(TAG, "error parsing data" + ex.getLocalizedMessage());
			} finally {
				Util.hideProgressDialog(getActivity());
			}
			Util.hideProgressDialog(getActivity());
		}

	};
	private ErrorListener fetchBadgesErrorListner = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			Log.d(TAG, "error fetching badges->" + error.getLocalizedMessage());
			Util.toast(getActivity(),
					getString(R.string.toast_error_connecting));
			Util.hideProgressDialog(getActivity());
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_badge, container,
				false);
		textViewBadgeDesc = (TextView) rootView
				.findViewById(R.id.textViewBadgeDesc);
		textViewBadgeMyPoint = (TextView) rootView
				.findViewById(R.id.textViewBadgeMyPoint);
		imageViewBadge = (ImageView) rootView.findViewById(R.id.imageViewBadge);
		listViewBadges = (ListView) rootView.findViewById(R.id.listViewBadges);
		listViewBadges.setEmptyView((TextView) rootView
				.findViewById(R.id.textViewBadgesEmptyView));
		if (badgeAdapter == null)
			badgeAdapter = new BadgeAdapter(getActivity(),
					new ArrayList<Badge>());
		listViewBadges.setAdapter(badgeAdapter);
		setHasOptionsMenu(true);
		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.badge_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_badge_refresh) {
			fetchBadges();
			return true;
		} else if (id == R.id.action_badge_help) {
			Util.toast(getActivity(), getString(R.string.toast_badge_help));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		getActivity().setTitle(R.string.title_badges);
		fetchBadges();
		super.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();
		setMyBadges(Util.getMyPoints(getActivity()));
	}

	private RequestQueue getReqQue() {
		if (reqQue == null)
			reqQue = Volley.newRequestQueue(getActivity());
		return reqQue;
	}

	private void fetchBadges() {
		if (Util.hasInternetAccess(getActivity())) {
			Map<String, String> params = new HashMap<>();
			Employee emp = Util.getEmployee(getActivity());
			params.put(Constants.NETWORK_PARAMS.BADGE.EMPID,
					String.valueOf(emp.getEmpId()));
			params.put(Constants.NETWORK_PARAMS.BADGE.BATCH, emp.getLg());
			StringRequest request = new StringRequest(Constants.URL_BADGES
					+ Util.getUrlEncodedString(params),
					fetchBadgesSuccessListner, fetchBadgesErrorListner);
			getReqQue().add(request);
			Util.showProgressDialog(getActivity());
		} else {
			Util.toast(getActivity(), getString(R.string.toast_no_internet));
		}
	}
}
