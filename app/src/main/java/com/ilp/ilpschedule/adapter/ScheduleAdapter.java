package com.ilp.ilpschedule.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ilp.ilpschedule.R;
import com.ilp.ilpschedule.model.Slot;
import com.ilp.ilpschedule.model.SlotViewHolder;
import com.ilp.ilpschedule.util.Util;

public class ScheduleAdapter extends ArrayAdapter<Slot> {
	private Context context;
	private ArrayList<Slot> data;
	private SlotViewHolder svh;
	private OnClickListener callbackListner;

	public ArrayList<Slot> getData() {
		return data;
	}

	public ScheduleAdapter(Context context, ArrayList<Slot> objects,
			OnClickListener callbackListner) {
		super(context, R.layout.schedule_list_item, objects);
		this.context = context;
		if (objects == null)
			data = new ArrayList<Slot>();
		else
			data = objects;
		this.callbackListner = callbackListner;
	}

	public void setData(List<Slot> data) {
		if (data != null && data.size() > 0) {
			this.data.clear();
			this.data.addAll(data);
			notifyDataSetInvalidated();
			notifyDataSetChanged();
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = ((LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.schedule_list_item, parent, false);
			svh = new SlotViewHolder();

			svh.setSlotContent((TextView) convertView
					.findViewById(R.id.textViewSlotContent));
			svh.getSlotContent().setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Util.toast(context, ""
							+ ((TextView) v).getText().toString());
				}
			});

			svh.setCourseContent((TextView) convertView
					.findViewById(R.id.textViewCourseContent));

			svh.setFacultyContent((TextView) convertView
					.findViewById(R.id.textViewFacultyContent));

			svh.setRoomContent((TextView) convertView
					.findViewById(R.id.textViewRoomContent));
			svh.getRoomContent().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Util.toast(context, ((TextView) v).getText().toString());
				}
			});
			convertView.setTag(svh);
		}

		Slot s = data.get(position);
		svh = (SlotViewHolder) convertView.getTag();
		svh.getSlotContent().setText("Slot "+s.getSlot());
		svh.getRoomContent().setText("Room " + s.getRoom());
		svh.getCourseContent().setText(s.getCourse());

		svh.getFacultyContent().setText(s.getFaculty());
		svh.setId(s.getId());
		convertView.setOnClickListener(callbackListner);
		convertView.setTag(svh);
		return convertView;
	}
}
