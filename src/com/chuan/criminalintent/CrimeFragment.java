package com.chuan.criminalintent;

import java.util.Date;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
//import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;

public class CrimeFragment extends Fragment {
	public static final String EXTRA_CRIME_ID = "com.chuan.criminalintent.crime_id";
	private static final String DAILOG_DATE = "date";
	private static final int REQUEST_DATE = 0;
	private static final int REQUEST_TIME = 1;
	private Crime mCrime;
	private EditText mTitleFiled;
	private Button mDateButton;
	private CheckBox mSovledCheckBox;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
			if(NavUtils.getParentActivityName(getActivity())!=null)
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		setHasOptionsMenu(true);
//		UUID crimeId = (UUID)getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);
		UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
		mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
	}
	public static CrimeFragment newInstance(UUID crimeId){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_CRIME_ID, crimeId);
		CrimeFragment fagement = new CrimeFragment();
		fagement.setArguments(args);		
		return fagement;
	}
	public void returnResult(){
		getActivity().setResult(Activity.RESULT_OK, null);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode!=Activity.RESULT_OK) {
			return;
		}
		if(requestCode==REQUEST_DATE){
			Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mCrime.setDate(date);
			updateDate();
		}
		else if (requestCode==REQUEST_TIME) {
			Date date = (Date)data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
			mCrime.setDate(date);
			updateDate();
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime, container, false);
		mTitleFiled = (EditText)v.findViewById(R.id.crime_title);
		mTitleFiled.setText(mCrime.getTitle());
		mTitleFiled.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				mCrime.setTitle(arg0.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				
			}
		});
		
		mDateButton = (Button)v.findViewById(R.id.crime_date);
//		mDateButton.setEnabled(false);
		updateDate();
		mDateButton.setOnClickListener(new OnClickListener() {
			private int nType =  REQUEST_TIME;
			private void ShowPicker(){
				FragmentManager fm = getActivity().getSupportFragmentManager();
				DialogFragment dialog = nType==REQUEST_TIME?
						TimePickerFragment.newInstance(mCrime.getDate())
						: DatePickerFragment.newInstance(mCrime.getDate());
				dialog.setTargetFragment(CrimeFragment.this, nType);
				dialog.show(fm, DAILOG_DATE);
			}
			private void showCheckBox(){
				
				AlertDialog.Builder choose = new AlertDialog.Builder(getActivity());
				choose.setTitle("Choose a type");
				choose.setNegativeButton(R.string.timePick, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						nType = REQUEST_TIME;
						ShowPicker();
					}
				});
				choose.setPositiveButton(R.string.datePick, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						nType = REQUEST_DATE;
						ShowPicker();
					}

				});
				choose.create();
				choose.show();
			}
			@Override
			public void onClick(View arg0) {
				
//				DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
//				DialogFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
				showCheckBox();
			}
			
		});
		
		mSovledCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
		mSovledCheckBox.setChecked(mCrime.isbSolved());
		mSovledCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				mCrime.setbSolved(arg1);			
			}
		});
		
		return v;
	}
	public void updateDate(){
		CharSequence mCharSequence = "yyyy-MM-dd HH:mm:ss"; 
		CharSequence strDate = DateFormat.format(mCharSequence, mCrime.getDate());
		mDateButton.setText(strDate);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (NavUtils.getParentActivityName(getActivity())!=null) {
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
