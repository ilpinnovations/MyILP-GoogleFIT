package com.ilp.ilpschedule.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilp.ilpschedule.R;
import com.ilp.ilpschedule.model.DrawerItem;
import com.ilp.ilpschedule.model.DrawerItemViewHolder;
import com.ilp.ilpschedule.model.Employee;
import com.ilp.ilpschedule.util.Constants;
import com.ilp.ilpschedule.util.Util;

import java.util.ArrayList;

public class DrawerAdapter extends ArrayAdapter<DrawerItem> {
	private Context context;
	private ArrayList<DrawerItem> data;
	private OnClickListener callbackListner;

	public DrawerAdapter(Context context, int resource,
			ArrayList<DrawerItem> data, OnClickListener callbackListner) {
		super(context, resource, data);
		this.context = context;
		this.data = data;
		this.callbackListner = callbackListner;
	}

	@Override
	public int getCount() {
		return 8;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DrawerItemViewHolder dvh;
		DrawerItem item = data.get(position);
		if (convertView == null) {
			dvh = new DrawerItemViewHolder();
			dvh.setTag(item.getTag());
			LayoutInflater layoutInflater = ((LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE));

			if (position == 0) {
				// header
				convertView = layoutInflater.inflate(R.layout.drawer_header,
						parent, false);
				dvh.setType(Constants.DRAWER_ITEM_TYPE.HEADER);
			} else {
				convertView = layoutInflater.inflate(R.layout.drawer_item,
						parent, false);
				dvh.setIcon((ImageView) convertView
						.findViewById(R.id.imageViewDrawer));
				dvh.setOption((TextView) convertView
						.findViewById(R.id.textViewDrawer));
				dvh.setType(Constants.DRAWER_ITEM_TYPE.OPTION);

			}
			convertView.setTag(dvh);
		}
		dvh = (DrawerItemViewHolder) convertView.getTag();
		if (dvh.getType() == Constants.DRAWER_ITEM_TYPE.OPTION) {
			if (getItem(position).isActive()) {
				dvh.getIcon().setImageDrawable(
						context.getResources().getDrawable(
								item.getActiveIconId()));
				dvh.getOption().setTextColor(
						context.getResources().getColor(
								R.color.colorTextInverse));
			} else {
				dvh.getIcon().setImageDrawable(
						context.getResources().getDrawable(item.getIconId()));
				dvh.getOption().setTextColor(
						context.getResources().getColor(R.color.colorText));
			}
			dvh.getOption().setText(item.getText());
			convertView.setOnClickListener(callbackListner);
		} else {
			Employee emp = Util.getEmployee(context);
			((TextView) convertView.findViewById(R.id.textViewName))
					.setText(emp.getName());
			((TextView) convertView.findViewById(R.id.textViewEmail))
					.setText(emp.getEmail());
			((TextView) convertView.findViewById(R.id.textViewLocation))
					.setText(emp.getLocation());
			((TextView) convertView.findViewById(R.id.textViewEmpId))
					.setText(String.valueOf(emp.getEmpId()));
			((TextView) convertView.findViewById(R.id.textViewLgName))
					.setText(String.valueOf(emp.getLg()));
		}
		return convertView;
	}

	public void clearSelection() {
		for (int i = 0; i < getCount(); i++) {
			getItem(i).setActive(false);
		}
		notifyDataSetChanged();
		notifyDataSetInvalidated();
	}
}
