package com.ilp.ilpschedule.model;

import java.util.Locale;

import com.ilp.ilpschedule.util.Util;

public class Contact implements Comparable<Contact> {
	private long id;
	private String title;
	private String number;

	public String getTitle() {
		return title;
	}

	public Contact() {

	}

	public boolean isValid() {
		return Util.checkString(title) && Util.checkString(number);
	}

	public Contact(String title, String number) {
		super();
		this.title = title;
		this.number = number;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Contact [id=" + id + ", title=" + title + ", number=" + number
				+ "]";
	}

	@Override
	public int compareTo(Contact another) {
		return getTitle().toLowerCase(Locale.US).compareTo(
				another.getTitle().toLowerCase(Locale.US));
	}

}
