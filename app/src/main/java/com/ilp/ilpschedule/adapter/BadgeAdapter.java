package com.ilp.ilpschedule.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilp.ilpschedule.R;
import com.ilp.ilpschedule.model.Badge;
import com.ilp.ilpschedule.model.BadgeViewHolder;
import com.ilp.ilpschedule.util.Util;

public class BadgeAdapter extends ArrayAdapter<Badge> {

	private Context context;
	private List<Badge> badges;
	private BadgeViewHolder bvh;

	public BadgeAdapter(Context context, List<Badge> badges) {
		super(context, R.layout.badge_list_item, badges);
		this.badges = badges;
		this.context = context;
		Collections.sort(this.badges);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = ((LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.badge_list_item, parent, false);
			bvh = new BadgeViewHolder();
			bvh.setTextViewName((TextView) convertView
					.findViewById(R.id.textViewBadgeName));
			bvh.setTextViewPoint((TextView) convertView
					.findViewById(R.id.textViewBadgePoint));
			bvh.setImageViewBadge((ImageView) convertView
					.findViewById(R.id.imageViewBadgeIcon));
			convertView.setTag(bvh);
		}
		Badge badge = badges.get(position);
		bvh = (BadgeViewHolder) convertView.getTag();
		bvh.getTextViewName().setText(badge.getName());
		bvh.getTextViewPoint().setText(
				context.getString(R.string.badge_item_points)
						+ badge.getPoints());
		bvh.getImageViewBadge().setImageResource(
				Util.getBadgeByPoints(badge.getPoints()));
		return convertView;
	}

	public void addData(List<Badge> badges) {
		if (this.badges == null)
			this.badges = new ArrayList<>();
		this.badges.clear();
		this.badges.addAll(badges);
		Collections.sort(this.badges);
		notifyDataSetChanged();
		notifyDataSetInvalidated();
	}
}
