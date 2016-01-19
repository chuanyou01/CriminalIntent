package com.chuan.criminalintent;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

public class DatePickerFragment extends DialogFragment {
	public static final String EXTRA_DATE = "com.chuan.criminalintent.date";
	private Date mDate;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mDate = (Date)getArguments().getSerializable(EXTRA_DATE);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		int Year = calendar.get(Calendar.YEAR);
		int Month = calendar.get(Calendar.MONTH);
		int Day = calendar.get(Calendar.DAY_OF_MONTH);
		
		
		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);
		
		DatePicker datePicker = (DatePicker)v.findViewById(R.id.dialog_date_datePicker);
		datePicker.init(Year, Month, Day, new OnDateChangedListener() {
			
			@Override
			public void onDateChanged(DatePicker arg0, int arg1, int arg2, int arg3) {
				mDate = new GregorianCalendar(arg1, arg2, arg3).getTime();
				getArguments().putSerializable(EXTRA_DATE, mDate);
			}
		});
		
		return new AlertDialog.Builder(getActivity())
				.setView(v)
				.setTitle(R.string.date_picker_title)
				.setPositiveButton(R.string.ok, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						sendResult(Activity.RESULT_OK);
					}
				})
				.create();
	}
	
	private void sendResult(int resultCode){
		if (getTargetFragment()==null) {
			return;
		}
		Intent i = new Intent();
		i.putExtra(EXTRA_DATE, mDate);
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
	}
	
	public static DatePickerFragment newInstance(Date date){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_DATE, date);
		
		DatePickerFragment fragment = new DatePickerFragment();
		fragment.setArguments(args);
		return fragment;
	}
}
