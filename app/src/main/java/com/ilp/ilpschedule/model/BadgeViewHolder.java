package com.ilp.ilpschedule.model;

import android.widget.ImageView;
import android.widget.TextView;

public class BadgeViewHolder {
	private TextView textViewName, textViewPoint;
	private ImageView imageViewBadge;

	public TextView getTextViewPoint() {
		return textViewPoint;
	}

	public void setTextViewPoint(TextView textViewPoint) {
		this.textViewPoint = textViewPoint;
	}

	public TextView getTextViewName() {
		return textViewName;
	}

	public void setTextViewName(TextView textViewName) {
		this.textViewName = textViewName;
	}

	public ImageView getImageViewBadge() {
		return imageViewBadge;
	}

	public void setImageViewBadge(ImageView imageViewBadge) {
		this.imageViewBadge = imageViewBadge;
	}
}
