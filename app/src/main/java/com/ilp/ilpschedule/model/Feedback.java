package com.ilp.ilpschedule.model;

public class Feedback {
	private long id, slot_id;
	private float rating;
	private String comment;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSlot_id() {
		return slot_id;
	}

	public void setSlot_id(long slot_id) {
		this.slot_id = slot_id;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
