package com.chuan.criminalintent;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageFragment extends DialogFragment {
	private static final String EXTRA_PATH = "com.chuan.criminalintent.image_path";
	private static final String EXTRA_ORITATION = "com.chuan.criminalintnet.image_oritation";
	
	private ImageView mImageView;
	
	public static ImageFragment newInstance(Photo p){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_PATH, p.getFileName());
		args.putInt(EXTRA_ORITATION, p.getnOritation());
		
		ImageFragment fragment = new ImageFragment();
		fragment.setArguments(args);
		fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mImageView = new ImageView(getActivity());
		String strPath = (String)getArguments().getSerializable(EXTRA_PATH);
		int oritation = getArguments().getInt(EXTRA_ORITATION);
		
		String imagPath = getActivity().getFileStreamPath(strPath).getAbsolutePath();
		
		BitmapDrawable image = PictureUtils.getScaledDrawable(getActivity(), imagPath,  oritation);
		mImageView.setImageDrawable(image);
		return mImageView;
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		PictureUtils.cleanImageView(mImageView);
	}
}
