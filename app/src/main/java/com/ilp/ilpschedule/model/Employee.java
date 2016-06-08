package com.ilp.ilpschedule.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ilp.ilpschedule.util.Constants;

public class Employee {
	private long empId;
	private String location, email, name, lg;

	public Employee() {

	}

	public long getEmpId() {
		return empId;
	}

	public void setEmpId(long empId) {
		this.empId = empId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Employee(int empId, String name, String email, String location) {
		this.empId = empId;
		this.location = location;

		this.email = email;
		this.name = name;
	}

	public int isValid() {
		Pattern hasSpace = Pattern.compile("\\s");
		Matcher matcher;
		if (getEmpId() < 1) {
			return Constants.EMP_ERRORS.EMP_ID.BLANK;
		}
		if (getName() == null || getName().length() == 0) {
			return Constants.EMP_ERRORS.NAME.BLANK;
		}
		if (getEmail() == null || getEmail().length() == 0) {
			return Constants.EMP_ERRORS.EMAIL.BLANK;
		}
		if (!getEmail().endsWith("@tcs.com")
				|| !android.util.Patterns.EMAIL_ADDRESS.matcher(getEmail())
						.matches()) {
			return Constants.EMP_ERRORS.EMAIL.INVALID;
		}

		if (getLg() == null || getLg().length() == 0) {
			return Constants.EMP_ERRORS.EMP_LG.BLANK;
		}
		matcher = hasSpace.matcher(getLg());
		if (matcher.matches()) {
			return Constants.EMP_ERRORS.EMP_LG.INVALID;
		}

		if (getLocation() == null || getLocation().length() == 0) {
			return Constants.EMP_ERRORS.LOCATION.BLANK;
		}
		return Constants.EMP_ERRORS.NO_ERROR;
	}

	public String getLg() {
		return lg;
	}

	public void setLg(String lg) {
		this.lg = lg;
	}
}
