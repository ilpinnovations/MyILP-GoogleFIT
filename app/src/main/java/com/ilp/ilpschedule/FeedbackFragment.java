package com.ilp.ilpschedule;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ilp.ilpschedule.db.DbHelper;
import com.ilp.ilpschedule.model.Employee;
import com.ilp.ilpschedule.model.Feedback;
import com.ilp.ilpschedule.model.Slot;
import com.ilp.ilpschedule.util.Constants;
import com.ilp.ilpschedule.util.Util;

public class FeedbackFragment extends Fragment {
	public static final String TAG = "com.tcs.myilp.FeedbackFragment";

	private EditText editTextComment;
	private TextView textViewFeedbackFaculty, textViewFeedbackCourse,
			textViewFeedbackCount, textViewFeedbackAvg;
	private ImageButton imageButtonFeedbackSend;
	private RatingBar ratingBarFeedback;
	private long slot_id;
	private Bundle bundle;
	private RequestQueue reqQueue;
	private OnClickListener feedbackSubmitClickListner = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// send feedback to server
			Util.showProgressDialog(getActivity());
			if (isDataValid()) {
				Feedback feedback = new Feedback();
				feedback.setComment(editTextComment.getText().toString().trim());
				feedback.setRating(ratingBarFeedback.getRating());
				feedback.setSlot_id(slot_id);
				DbHelper dbh = new DbHelper(getActivity());
				long id = dbh.addFeedback(feedback);
				if (id != -1) {
					Log.d(TAG, "feedback saved");
					if (Util.hasInternetAccess(getActivity())) {
						final Feedback finalfeedback = dbh.getFeedback(id);
						StringRequest request = new StringRequest(
								Request.Method.POST,
								Constants.URL_FEEDBACK_SUBMIT,
								feedbackSubmitTaskSuccessListner,
								feedbackSubmitTaskErrorListner) {
							@Override
							protected java.util.Map<String, String> getParams()
									throws com.android.volley.AuthFailureError {
								Map<String, String> params = new HashMap<String, String>();
								params.put(
										Constants.NETWORK_PARAMS.FEEDBACK_SUBMIT.COMMENT,
										editTextComment.getText().toString()
												.trim());
								params.put(
										Constants.NETWORK_PARAMS.FEEDBACK_SUBMIT.RATE,
										String.valueOf(ratingBarFeedback
												.getRating()));
								DbHelper dbh = new DbHelper(getActivity());
								Slot s = dbh
										.getSlot(finalfeedback.getSlot_id());
								params.put(
										Constants.NETWORK_PARAMS.FEEDBACK_SUBMIT.FACULTY,
										s.getFaculty());
								params.put(
										Constants.NETWORK_PARAMS.FEEDBACK_SUBMIT.COURSE,
										s.getCourse());
								params.put(
										Constants.NETWORK_PARAMS.FEEDBACK_SUBMIT.SLOT,
										s.getSlot());
								params.put(
										Constants.NETWORK_PARAMS.FEEDBACK_SUBMIT.DATE,
										Constants.paramsDateFormat.format(s
												.getDate()));

								Employee employee = Util
										.getEmployee(getActivity());
								params.put(
										Constants.NETWORK_PARAMS.FEEDBACK_SUBMIT.EMP_ID,
										String.valueOf(employee.getEmpId()));
								params.put(
										Constants.NETWORK_PARAMS.FEEDBACK_SUBMIT.EMP_NAME,
										employee.getName());
								params.put(
										Constants.NETWORK_PARAMS.FEEDBACK_SUBMIT.EMP_LOC,
										employee.getLocation());
								params.put(
										Constants.NETWORK_PARAMS.FEEDBACK_SUBMIT.EMP_BATCH,
										employee.getLg());
								return params;
							};
						};
						getRequestQueue().add(request);
					} else {
						Toast.makeText(getActivity(),
								getString(R.string.toast_no_internet),
								Toast.LENGTH_SHORT).show();
						Util.hideProgressDialog(getActivity());
					}
				} else {
					Toast.makeText(getActivity(), "Invalid feedback!!",
							Toast.LENGTH_SHORT).show();
					Util.hideProgressDialog(getActivity());
				}
			} else {
				Toast.makeText(getActivity(), "Comment cannot be empty",
						Toast.LENGTH_SHORT).show();
				Util.hideProgressDialog(getActivity());
			}
		}
	};
	private Listener<String> feedbackSubmitTaskSuccessListner = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			boolean result = false;
			try {
				JSONObject jobj = new JSONObject(response);
				if (jobj.has("Android")) {
					JSONArray jarr = jobj.getJSONArray("Android");
					if (jarr.length() > 0) {
						jobj = jarr.getJSONObject(0);
						if (jobj.has("feed_result")) {
							String res = jobj.getString("feed_result");
							if (res.equalsIgnoreCase("success")) {
								res = getString(R.string.toast_feedback_success);
								result = true;
							} else if (res.equalsIgnoreCase("already")) {
								res = getString(R.string.toast_feedback_already);
								result = true;
							} else {
								res = getString(R.string.toast_feedback_fail);
							}
							Toast.makeText(getActivity(), res,
									Toast.LENGTH_SHORT).show();
						}
					}
				}
				Log.d(TAG, "got the response of submittion ->" + response);
			} catch (JSONException ex) {
				Log.d(TAG, "error parsing result" + ex.getLocalizedMessage());
			} finally {
				Util.hideProgressDialog(getActivity());
			}
			if (result)
				getFragmentManager().popBackStack();
			Log.d(TAG, response);
		}
	};
	private ErrorListener feedbackSubmitTaskErrorListner = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			Util.toast(getActivity().getApplicationContext(),
					"Error connecting server. Try again!");
			Log.d(TAG, "error" + error + error.getLocalizedMessage());
			Util.hideProgressDialog(getActivity());
		}

	};
	private Listener<String> feedbackSummaryTaskSuccessListner = new Listener<String>() {

		@Override
		public void onResponse(String result) {
			Log.d(TAG, "result=>>" + result);
			try {
				JSONObject obj = new JSONObject(result);
				JSONArray jarr = obj.optJSONArray("Android");
				obj = jarr.optJSONObject(0);
				if (obj.has("count"))
					textViewFeedbackCount.setText("Total feedbacks: "
							+ obj.optString("count"));
				else
					textViewFeedbackCount.setText("No feedback");
				if (obj.has("avg_count"))
					textViewFeedbackAvg.setText("Average rating: "
							+ obj.optString("avg_count"));
				else
					textViewFeedbackAvg.setText("-NA-");
			} catch (Exception ex) {
				Log.d(TAG,
						"error parsing summary data" + ex.getLocalizedMessage());
			} finally {
				Util.hideProgressDialog(getActivity());
			}
		}

	};
	private ErrorListener feedbackSummaryTaskErrorListner = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			Log.d(TAG, "error=>>" + error);
			Util.toast(getActivity(),
					getString(R.string.toast_error_connecting));
			Util.hideProgressDialog(getActivity());
		}
	};

	private RequestQueue getRequestQueue() {
		if (reqQueue == null)
			reqQueue = Volley.newRequestQueue(getActivity());
		return reqQueue;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_feedback, container,
				false);

		textViewFeedbackCourse = (TextView) rootView
				.findViewById(R.id.textViewFeedbackCourse);

		textViewFeedbackFaculty = (TextView) rootView
				.findViewById(R.id.textViewFeedbackFaculty);

		imageButtonFeedbackSend = (ImageButton) rootView
				.findViewById(R.id.imageButtonFeedbackSend);

		imageButtonFeedbackSend.setOnClickListener(feedbackSubmitClickListner);

		ratingBarFeedback = (RatingBar) rootView
				.findViewById(R.id.ratingBarFeedback);

		editTextComment = (EditText) rootView
				.findViewById(R.id.editTextFeedbackComment);

		textViewFeedbackCount = (TextView) rootView
				.findViewById(R.id.textViewFeedbackCount);
		textViewFeedbackAvg = (TextView) rootView
				.findViewById(R.id.textViewFeedbackAvg);
		return rootView;
	}

	private void switchView(boolean forFaculty) {
		if (forFaculty) {
			editTextComment.setVisibility(View.GONE);
			ratingBarFeedback.setVisibility(View.GONE);
			imageButtonFeedbackSend.setVisibility(View.GONE);
			textViewFeedbackCount.setVisibility(View.VISIBLE);
			textViewFeedbackAvg.setVisibility(View.VISIBLE);
		} else {
			editTextComment.setVisibility(View.VISIBLE);
			ratingBarFeedback.setVisibility(View.VISIBLE);
			imageButtonFeedbackSend.setVisibility(View.VISIBLE);
			textViewFeedbackCount.setVisibility(View.GONE);
			textViewFeedbackAvg.setVisibility(View.GONE);
		}
	}

	@Override
	public void onStart() {
		String faculty = bundle.getString(
				Constants.BUNDLE_KEYS.FEEDBACK_FRAGMENT.FACULTY, "NA");
		String course = bundle.getString(
				Constants.BUNDLE_KEYS.FEEDBACK_FRAGMENT.COURSE, "NA");
		slot_id = bundle.getLong(
				Constants.BUNDLE_KEYS.FEEDBACK_FRAGMENT.SLOT_ID, -1);

		textViewFeedbackFaculty.setText(faculty);
		textViewFeedbackCourse.setText(course);
		DbHelper dbh = new DbHelper(getActivity());
		if (bundle.getBoolean(
				Constants.BUNDLE_KEYS.FEEDBACK_FRAGMENT.IS_FACULTY, false)) {

			switchView(true);
			if (Util.hasInternetAccess(getActivity())) {
				Util.showProgressDialog(getActivity());
				Slot slot = dbh.getSlot(slot_id);
				// network request is required
				Map<String, String> params = new HashMap<>();
				params.put(Constants.NETWORK_PARAMS.FEEDBACK_SUMMARY.FACULTY,
						faculty);
				params.put(Constants.NETWORK_PARAMS.FEEDBACK_SUMMARY.SLOT,
						slot.getSlot());
				params.put(Constants.NETWORK_PARAMS.FEEDBACK_SUMMARY.DATE,
						Constants.paramsDateFormat.format(slot.getDate()));
				params.put(Constants.NETWORK_PARAMS.FEEDBACK_SUMMARY.COURSE,
						course);
				Log.d(TAG, params.toString());
				StringRequest request = new StringRequest(
						Constants.URL_FEEDBACK_SUMMARY
								+ Util.getUrlEncodedString(params),
						feedbackSummaryTaskSuccessListner,
						feedbackSummaryTaskErrorListner);
				getRequestQueue().add(request);
			} else {
				Util.toast(getActivity(), getString(R.string.toast_no_internet));
			}
		} else {
			switchView(false);
			editTextComment.setText("");
			ratingBarFeedback.setRating(1.0f);
			Feedback feedback = dbh.getFeedbackBySlotId(slot_id);
			if (feedback != null) {
				if (feedback.getComment() != null)
					editTextComment.setText(feedback.getComment());
				if (feedback.getRating() > 0) {
					ratingBarFeedback.setRating(feedback.getRating());
				}
			}
		}
		super.onStart();
	}

	@Override
	public void onResume() {
		getActivity().setTitle(R.string.title_feedback);
		super.onResume();
	}

	private boolean isDataValid() {
		return Util.checkString(editTextComment.getText().toString());
	}

	public void setData(Bundle bundle) {
		this.bundle = bundle;
	}

}
