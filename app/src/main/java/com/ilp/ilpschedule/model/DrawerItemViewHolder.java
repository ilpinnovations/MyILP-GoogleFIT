package com.ilp.ilpschedule.model;

import android.widget.ImageView;
import android.widget.TextView;

public class DrawerItemViewHolder {
	private ImageView icon;
	private TextView option;
	private int type;
	private boolean active;
	private String tag; 
	public DrawerItemViewHolder() {
		active = false;
	}

	public ImageView getIcon() {
		return icon;
	}

	public void setIcon(ImageView icon) {
		this.icon = icon;
	}

	public TextView getOption() {
		return option;
	}

	public void setOption(TextView option) {
		this.option = option;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}
