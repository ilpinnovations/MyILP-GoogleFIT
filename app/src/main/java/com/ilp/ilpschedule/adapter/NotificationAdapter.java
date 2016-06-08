package com.ilp.ilpschedule.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ilp.ilpschedule.R;
import com.ilp.ilpschedule.model.Notification;
import com.ilp.ilpschedule.model.NotificationViewHolder;

public class NotificationAdapter extends ArrayAdapter<Notification> {
	private Context context;
	private ArrayList<Notification> notifications;

	public NotificationAdapter(Context context, ArrayList<Notification> objects) {
		super(context, R.layout.notification_item, objects);
		this.context = context;
		notifications = objects;
	}

	public void setData(List<Notification> data) {
		if (notifications == null)
			notifications = new ArrayList<Notification>();
		if (data != null && data.size() > 0) {
			notifications.clear();
			notifications.addAll(data);
			Collections.sort(notifications);
			notifyDataSetChanged();
			notifyDataSetInvalidated();
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NotificationViewHolder nvh;
		if (convertView == null) {
			convertView = ((LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.notification_item, parent, false);
			nvh = new NotificationViewHolder();
			nvh.setMsg((TextView) convertView
					.findViewById(R.id.textViewNotificationContent));
			nvh.setDate((TextView) convertView
					.findViewById(R.id.textViewNotificationTimestamp));
			convertView.setTag(nvh);
		}
		nvh = (NotificationViewHolder) convertView.getTag();

		nvh.getMsg().setText(getItem(position).getMsg());
		nvh.getDate().setText(
				Notification.outputDateFormat.format(getItem(position)
						.getDate()));
		return convertView;
	}

	public void addData(List<Notification> notifications) {
		if (this.notifications == null)
			this.notifications = new ArrayList<>();
		this.notifications.clear();
		this.notifications.addAll(notifications);
		Collections.sort(this.notifications);
		notifyDataSetInvalidated();
		notifyDataSetChanged();
	}
}
