package com.ilp.ilpschedule.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ilp.ilpschedule.R;
import com.ilp.ilpschedule.model.Contact;
import com.ilp.ilpschedule.model.ContactViewHolder;

public class ContactAdapter extends ArrayAdapter<Contact> {

	public static final String TAG = "com.ilp.ilpschedule.adapter.ContactAdapter";
	private List<Contact> contacts;
	private Context context;
	private ContactViewHolder cvh;
	private OnClickListener callbackListner;

	public ContactAdapter(Context context, List<Contact> contacts,
			OnClickListener callbackListner) {
		super(context, R.layout.contact_list_item, contacts);
		this.contacts = contacts;
		Collections.sort(contacts);
		this.context = context;
		this.callbackListner = callbackListner;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = ((LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.contact_list_item, parent, false);
			cvh = new ContactViewHolder();
			cvh.setTextViewTitle((TextView) convertView
					.findViewById(R.id.textViewContactTitle));
			cvh.setTextViewNumber((TextView) convertView
					.findViewById(R.id.textViewContactNumber));
			cvh.setTextViewIcon((TextView) convertView
					.findViewById(R.id.textViewContactIcon));
			convertView.setTag(cvh);
		}
		cvh = (ContactViewHolder) convertView.getTag();
		Contact contact = contacts.get(position);
		cvh.getTextViewTitle().setText(contact.getTitle());
		cvh.getTextViewNumber().setText(contact.getNumber());
		cvh.getTextViewIcon().setText(
				String.valueOf(contact.getTitle().toUpperCase(Locale.US)
						.charAt(0)));
		int color_set = R.drawable.contact_icon_set_a;
		switch (position % 6) {
		case 0:
			color_set = R.drawable.contact_icon_set_a;
			break;
		case 1:
			color_set = R.drawable.contact_icon_set_b;
			break;
		case 2:
			color_set = R.drawable.contact_icon_set_c;
			break;
		case 3:
			color_set = R.drawable.contact_icon_set_d;
			break;
		case 4:
			color_set = R.drawable.contact_icon_set_e;
			break;
		case 5:
			color_set = R.drawable.contact_icon_set_f;
			break;

		}
		cvh.getTextViewIcon().setBackgroundResource(color_set);
		convertView.setOnClickListener(callbackListner);
		return convertView;
	}

	public void addData(List<Contact> contacts) {
		if (this.contacts == null)
			this.contacts = new ArrayList<>();
		this.contacts.clear();
		this.contacts.addAll(contacts);
		Collections.sort(this.contacts);
		notifyDataSetInvalidated();
		notifyDataSetChanged();
	}
}
