package com.ilp.ilpschedule.model;

import android.widget.TextView;

public class SlotViewHolder {
	private TextView courseContent, facultyContent, roomContent;
	private TextView slotContent;
	private long id;

	public TextView getSlotContent() {
		return slotContent;
	}

	public void setSlotContent(TextView slotContent) {
		this.slotContent = slotContent;
	}

	public TextView getCourseContent() {
		return courseContent;
	}

	public void setCourseContent(TextView courseContent) {
		this.courseContent = courseContent;
	}

	public TextView getFacultyContent() {
		return facultyContent;
	}

	public void setFacultyContent(TextView facultyContent) {
		this.facultyContent = facultyContent;
	}

	public TextView getRoomContent() {
		return roomContent;
	}

	public void setRoomContent(TextView roomContent) {
		this.roomContent = roomContent;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
