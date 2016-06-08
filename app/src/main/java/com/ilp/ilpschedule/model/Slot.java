package com.ilp.ilpschedule.model;

import java.sql.Date;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.ilp.ilpschedule.util.Util;

public class Slot implements Parcelable {
	private String slot, course, faculty, room, batch;
	private Date date;
	private long id;

	public String getSlot() {
		return slot;
	}

	public void setSlot(String slot) {
		this.slot = slot;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getFaculty() {
		return faculty;
	}

	public void setFaculty(String faculty) {
		this.faculty = faculty;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	@Override
	public String toString() {
		return "Slot [slot=" + slot + ", course=" + course + ", faculty="
				+ faculty + ", room=" + room + ", batch=" + batch + ", date="
				+ date + ", id=" + id + "]";
	}

	public Slot(String slot, String course, String faculty, String room) {
		super();
		this.slot = slot;
		this.course = course;
		this.faculty = faculty;
		this.room = room;
	}

	public Slot() {
		this.slot = "Not specified";
		this.course = "Not specified";
		this.faculty = "Not specified";
		this.room = "Not specified";
	}

	private Slot(Parcel in) {
		course = in.readString();
		slot = in.readString();
		faculty = in.readString();
		room = in.readString();
	}

	public static final Parcelable.Creator<Slot> CREATOR = new Parcelable.Creator<Slot>() {
		public Slot createFromParcel(Parcel in) {
			return new Slot(in);
		}

		public Slot[] newArray(int size) {
			return new Slot[size];
		}
	};
	private static final String TAG = "Slot";

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(course);
		dest.writeString(slot);
		dest.writeString(faculty);
		dest.writeString(room);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * check for valid values to insert into database
	 * 
	 * @return
	 */
	public boolean isValid() {
		boolean valid = Util.checkString(slot) && Util.checkString(course)
				&& Util.checkString(faculty) && Util.checkString(room)
				&& Util.checkString(batch);
		if (valid)
			Log.d(TAG, "slot valid");
		else
			Log.d(TAG, "slot invalid");
		return valid;
	}
}
