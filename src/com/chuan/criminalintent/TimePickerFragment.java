package com.chuan.criminalintent;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class TimePickerFragment extends DialogFragment {
	public static final String EXTRA_TIME = "com.chuan.criminalintent.time";
	private Date mDate;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mDate = (Date)getArguments().getSerializable(EXTRA_TIME);
		View v = getActivity().getLayoutInflater().inflate(R.layout.dailog_time, null);
		TimePicker timePicker = (TimePicker)v.findViewById(R.id.dialog_time_timePicker);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		int Hour = calendar.get(Calendar.HOUR);
		int Minute = calendar.get(Calendar.MINUTE);
		
		timePicker.setCurrentHour(Hour);
		timePicker.setCurrentMinute(Minute);
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			
			@Override
			public void onTimeChanged(TimePicker arg0, int arg1, int arg2) {
				Calendar calendar = Calendar.getInstance();
				Calendar calendarRet = Calendar.getInstance();
				calendar.setTime(TimePickerFragment.this.mDate);
				calendarRet.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH), arg1, arg2, 0);
				mDate = calendarRet.getTime();				
				getArguments().putSerializable(EXTRA_TIME, mDate);				
			}
		});
		
		return new AlertDialog.Builder(getActivity())
		.setView(v)
		.setTitle(R.string.time_picker_title)
		.setPositiveButton(R.string.ok, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {		
				sendResult(Activity.RESULT_OK);
			}
		})
		.create();
	}
	
	public void sendResult(int resultCode){
		if (getTargetFragment()==null) {
			return ;
		}
		Intent i = new Intent();
		i.putExtra(EXTRA_TIME, mDate);
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);	
	}
	
	public static TimePickerFragment newInstance(Date date){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_TIME, date);
		TimePickerFragment fragment = new TimePickerFragment();
		fragment.setArguments(args);
		return fragment;
	}
}
