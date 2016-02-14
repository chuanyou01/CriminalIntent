package com.chuan.criminalintent;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class CrimeCameraFragment extends Fragment {
	private static final String TAG = "CrimeCameraFragment";
	public static final String EXTRA_PHOTO_FILENAME = "com.criminalintent.photo_filename";
	private Camera mCamera;
	private SurfaceView mSurfaceView;
	private View mProgressContianer;
	private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
		@Override
		public void onShutter() {
			mProgressContianer.setVisibility(View.VISIBLE);
		}
	};
	private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			String strFileName = UUID.randomUUID().toString() + ".jpg";
			FileOutputStream os = null;
			boolean bSuc = false;
			try {
				os = getActivity().openFileOutput(strFileName, Context.MODE_PRIVATE);
				os.write(data);
				bSuc = true;
			} catch (IOException e) {
				Log.e(TAG, "Error Writting file " + e);
			}finally{
				try {
					if (os!=null) {
						os.close();
					}
				} catch (Exception e) {
					Log.e(TAG, "Close File failed " + e);
				}
			}
			if (bSuc) {
				Intent i = new Intent();
				i.putExtra(EXTRA_PHOTO_FILENAME, strFileName);
				getActivity().setResult(Activity.RESULT_OK, i);
			}else{
				getActivity().setResult(Activity.RESULT_CANCELED);
			}
			
			getActivity().finish();
		}
	};
	
	@Override
	@SuppressWarnings("deprecation")
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime_camera, container, false);
		
		Button takePicture = (Button)v.findViewById(R.id.crime_camera_takePictureButton);
		takePicture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCamera!=null) {
					mCamera.takePicture(mShutterCallback, null, mPictureCallback);
				}
				
			}
		});
		mSurfaceView = (SurfaceView)v.findViewById(R.id.crime_camera_surfaceView);
		SurfaceHolder hoder = mSurfaceView.getHolder();
		hoder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		hoder.addCallback(new Callback() {	
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				if (mCamera!=null) {
					mCamera.stopPreview();
				}
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				try {
					if (mCamera!=null) {
						mCamera.setPreviewDisplay(holder);
					}
				} catch (IOException e) {
					Log.e(TAG, "Error setting up preview display", e);
				}
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				if (mCamera==null) {
					return;
				}
				Camera.Parameters parameters = mCamera.getParameters();
				
				Size s= getBestSurpportSize(parameters.getSupportedPreviewSizes(), width, height);
				parameters.setPreviewSize(s.width, s.height);
				s = getBestSurpportSize(parameters.getSupportedPictureSizes(), width, height);
				parameters.setPictureSize(width, height);
				mCamera.setParameters(parameters);
				try {
					mCamera.startPreview();
				} catch (Exception e) {
					Log.e(TAG, "Could not start preview");
					mCamera.release();
					mCamera = null;
				}
			}
		});
		
		mProgressContianer = (View)v.findViewById(R.id.crime_camera_prgressContianer);
		mProgressContianer.setVisibility(View.INVISIBLE);
		
		return v;
	}
	@Override
	public void onResume() {
		super.onResume();
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.GINGERBREAD) {
			mCamera = Camera.open(0);
		}
		else{
			mCamera = Camera.open();
		}
	}
	@Override
	public void onPause() {
		super.onPause();
		if (mCamera!=null) {
			mCamera.release();
			mCamera = null;
		}
	}
	private Size getBestSurpportSize(List<Size> sizes , int width, int height){
		Size sBest = sizes.get(0);
		int LargestArea = sBest.width*sBest.height;
		for(Size s: sizes){
			int area = s.width* s.height;
			if (area>LargestArea) {
				LargestArea = area;
				sBest = s;
			}
		}
		return sBest;
	}
}
