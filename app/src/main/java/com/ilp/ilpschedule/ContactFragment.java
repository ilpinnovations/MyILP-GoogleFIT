package com.ilp.ilpschedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ilp.ilpschedule.adapter.ContactAdapter;
import com.ilp.ilpschedule.db.DbHelper;
import com.ilp.ilpschedule.model.Contact;
import com.ilp.ilpschedule.model.ContactViewHolder;
import com.ilp.ilpschedule.util.Constants;
import com.ilp.ilpschedule.util.Util;

public class ContactFragment extends Fragment {
	public static final String TAG = "com.tcs.myilp.ContactFragment";

	private RequestQueue reqQue;
	private ListView listViewContacts;
	private ContactAdapter contactAdapter;
	private OnClickListener contactItemClickListner = new OnClickListener() {
		@Override
		public void onClick(View view) {
			ContactViewHolder cvh = (ContactViewHolder) view.getTag();
			buildAlertToContact(cvh.getTextViewTitle().getText().toString(),
					cvh.getTextViewNumber().getText().toString());
		}
	};
	private Listener<String> fetchContactSuccessListner = new Listener<String>() {

		@Override
		public void onResponse(String result) {
			try {
				Log.d(TAG, "fetch contacts response ->" + result);
				JSONArray jarr = new JSONArray(result);
				JSONObject jobj;
				List<Contact> contacts = new ArrayList<>();
				Contact contact;
				for (int i = 0; i < jarr.length(); i++) {
					jobj = jarr.getJSONObject(i);
					Iterator<String> it = jobj.keys();
					while (it.hasNext()) {
						contact = new Contact();
						String title = it.next();
						contact.setTitle(title);
						contact.setNumber(jobj.getString(title));
						Log.d(TAG, contact.toString());
						contacts.add(contact);
					}
				}
				DbHelper dbh = new DbHelper(getActivity());
				dbh.addContacts(contacts);
				Log.d(TAG, "contacts added to db");
				contacts = dbh.getContacts();
				contactAdapter.addData(contacts);
			} catch (JSONException ex) {
				Log.d(TAG, "error in json data" + ex.getLocalizedMessage());
			} finally {
				Util.hideProgressDialog(getActivity());
			}

		}

	};
	private ErrorListener fetchContactsErrorListner = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			Log.d(TAG,
					"error occured while fetching contacts"
							+ error.getLocalizedMessage());
			Util.hideProgressDialog(getActivity());
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_contact, container,
				false);
		listViewContacts = (ListView) rootView
				.findViewById(R.id.listViewContacts);
		listViewContacts.setEmptyView((TextView) rootView
				.findViewById(R.id.textViewContactsEmptyView));
		if (contactAdapter == null)
			contactAdapter = new ContactAdapter(getActivity(),
					new ArrayList<Contact>(), contactItemClickListner);

		listViewContacts.setAdapter(contactAdapter);
		setHasOptionsMenu(true);
		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.contact_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_contact_refresh) {
			fetchContacts();
			return true;
		}
		else if(id==R.id.action_contact_help){
			Util.toast(getActivity(), getString(R.string.toast_contact_help));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private RequestQueue getReqQue() {
		if (reqQue == null)
			reqQue = Volley.newRequestQueue(getActivity());
		return reqQue;
	}

	@Override
	public void onResume() {
		getActivity().setTitle(R.string.title_contacts);
		super.onResume();
	}

	@Override
	public void onStart() {
		if (contactAdapter != null) {
			List<Contact> contacts = new DbHelper(getActivity()).getContacts();
			if (contacts != null && contacts.size() > 0)
				contactAdapter.addData(contacts);
			else
				fetchContacts();
		}
		super.onStart();
	}

	private void fetchContacts() {
		if (Util.hasInternetAccess(getActivity())) {
			Map<String, String> params = new HashMap<>();
			params.put(Constants.NETWORK_PARAMS.CONTACT.ILP,
					Util.getEmployee(getActivity()).getLocation());
			StringRequest request = new StringRequest(Request.Method.GET,
					Constants.URL_CONTACTS + Util.getUrlEncodedString(params),
					fetchContactSuccessListner, fetchContactsErrorListner);
			getReqQue().add(request);
			Util.showProgressDialog(getActivity());
		} else {
			Util.toast(getActivity(), getString(R.string.toast_no_internet));
		}
	}

	private void buildAlertToContact(String title, final String number) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				getActivity());
		builder.setMessage("Contact " + title + "\n" + number + " through")
				.setCancelable(true)
				.setPositiveButton("Call",
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int id) {
								try{
									Intent intent = new Intent(Intent.ACTION_CALL);
									intent.setData(Uri.parse("tel:" + number));
									startActivity(intent);
								}
								catch (Exception ex){
									Toast.makeText(getActivity(), "Please enable call permissions from app setings", Toast.LENGTH_LONG).show();
								}

							}
						})
				.setNegativeButton("Message",
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int id) {
								Intent intent = new Intent(Intent.ACTION_SENDTO);
								intent.setData(Uri.parse("smsto:" + number));
								startActivity(intent);
							}
						});
		final AlertDialog alert = builder.create();
		alert.show();
	}
}
