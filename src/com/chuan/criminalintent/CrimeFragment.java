package com.chuan.criminalintent;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Toast;
//import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;

public class CrimeFragment extends Fragment {
	public static final String EXTRA_CRIME_ID = "com.chuan.criminalintent.crime_id";
	private static final String TAG = "CrimeFragment";
	private static final String DAILOG_DATE = "date";
	private static final String DIALOG_IAMGE = "image";
	private static final int REQUEST_DATE = 0;
	private static final int REQUEST_TIME = 1;
	private static final int REQUEST_PHOTO = 2;
	private Crime mCrime;
	private EditText mTitleFiled;
	private Button mDateButton;
	private CheckBox mSovledCheckBox;
	private ImageButton mPhotoButton;
	private ImageView mImageView;
	private OrientationEventListener mOrientationEventListener;
	private int mOrintation;
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
		mOrientationEventListener= new OrientationEventListener(getActivity().getApplicationContext()) {
			
			@Override
			public void onOrientationChanged(int orientation) {
				mOrintation = orientation;

//				Log.i(TAG, "orientation is " + orientation);
			}
		};
		mOrientationEventListener.enable();
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
		switch (requestCode) {
		case REQUEST_DATE:
			Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mCrime.setDate(date);
			updateDate();
			break;
		case REQUEST_TIME:
			date = (Date)data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
			mCrime.setDate(date);
			updateDate();
			break;
		case REQUEST_PHOTO:
			String strFileName = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
			if (strFileName!=null) {
				int nDegree = 0;
				int configorientain = getResources().getConfiguration().orientation;
				if (configorientain== Configuration.ORIENTATION_PORTRAIT) {
					nDegree = 90;
				}else if(configorientain== Configuration.ORIENTATION_LANDSCAPE){
					if (mOrintation<210 && mOrintation>30) {
						nDegree = 270;
					}else{
						nDegree = 0;
					}
				}
				DeleteOldPhoto();
				Photo p = new Photo(strFileName, nDegree);
				mCrime.setPhoto(p);	
				showPhoto();
			}
			break;
		default:
			break;
		}
	}
	
	public void DeleteOldPhoto(){
		Photo p = mCrime.getPhoto();
		if (p!=null) {
			
			boolean bDelet = getActivity().getFileStreamPath(p.getFileName()).delete();
			if (bDelet){
				Log.i(TAG, "Old Image File id delete");
			}
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
		
		mPhotoButton = (ImageButton)v.findViewById(R.id.crime_imageButton);
		mPhotoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
				startActivityForResult(i, REQUEST_PHOTO);
				
//				startActivity(i);
			}
		});
		
		mImageView = (ImageView)v.findViewById(R.id.crime_imageView);
		mImageView.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				Photo p = mCrime.getPhoto();
				if (p==null) {
					return;
				}
				FragmentManager fm = getActivity().getSupportFragmentManager();
				ImageFragment.newInstance(p).show(fm, DIALOG_IAMGE);				
			}
		});
		mImageView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Toast tost = Toast.makeText(getActivity(), "LongClick of the image", Toast.LENGTH_SHORT);
				tost.show();
				return false;
			}
		});
		
		PackageManager pm = getActivity().getPackageManager();
		boolean hasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
				pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) || 
				Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD || 
				Camera.getNumberOfCameras()>0;
		if(!hasCamera){
			mPhotoButton.setEnabled(false);
		}
		
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
	@Override
	public void onPause() {
		super.onPause();
		CrimeLab.get(getActivity()).saveCriems();
	}
	
	private void showPhoto(){
		Photo p = mCrime.getPhoto();
		BitmapDrawable b = null;
		if (p!=null) {
			String strPath = getActivity().getFileStreamPath(p.getFileName()).getAbsolutePath();
			int nDegree =  p.getnOritation();
			b = PictureUtils.getScaledDrawable(getActivity(), strPath, nDegree);
			mImageView.setImageDrawable(b);
		}
	}
	@Override
	public void onStart() {
		super.onStart();
		showPhoto();
	}
	@Override
	public void onStop() {
		super.onStop();
		PictureUtils.cleanImageView(mImageView);
	}
}
