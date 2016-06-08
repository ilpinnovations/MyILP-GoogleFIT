package com.ilp.ilpschedule.model;

public class DrawerItem {
	private String text;
	private int type;
	private int iconId, activeIconId;
	private String tag;
	private boolean active;

	public DrawerItem(String text, int iconId, int activeIconId, int type,
			String tag) {
		this.text = text;
		this.type = type;
		this.activeIconId = activeIconId;
		this.iconId = iconId;
		this.setTag(tag);
	}

	public DrawerItem(int type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getIconId() {
		return iconId;
	}

	public void setIconId(int iconId) {
		this.iconId = iconId;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getActiveIconId() {
		return activeIconId;
	}

	public void setActiveIconId(int activeIconId) {
		this.activeIconId = activeIconId;
	}

}
