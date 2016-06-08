package com.ilp.ilpschedule;

import java.sql.Date;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class DatePickFragment extends DialogFragment {
	public static final String TAG = "com.tcs.myilp.DatePickerFragment";
	private OnDateSetListener listner;
	private Date date;

	public DatePickFragment(OnDateSetListener listner, Date date) {
		this.listner = listner;
		this.date = date;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar c = Calendar.getInstance();
		if (date != null)
			c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		return new DatePickerDialog(getActivity(), listner, year, month, day);
	}
}
