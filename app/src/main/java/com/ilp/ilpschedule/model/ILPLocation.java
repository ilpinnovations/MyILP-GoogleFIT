package com.ilp.ilpschedule.model;

import com.ilp.ilpschedule.util.Util;

public class ILPLocation {
	private long id;
	private String name;
	private double lat, lon;
	private String location,type;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * check validation before inserting into the database
	 * 
	 * @return
	 */
	public boolean isValid() {
		return Util.checkString(name) && lat > 0.0 && lon > 0.0
				&& Util.checkString(location);
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
