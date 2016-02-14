package com.chuan.criminalintent;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.view.Display;
import android.widget.ImageView;

public class PictureUtils {
	@SuppressWarnings("deprecation")
	public static BitmapDrawable getScaledDrawable(Activity a, String strPath, int nDegree) {
		Display display = a.getWindowManager().getDefaultDisplay();
		float deswitdh = display.getWidth();
		float desheight = display.getHeight();

		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(strPath, option);
		float srcWidth = option.outWidth;
		float srcHeight = option.outHeight;

		int nSampleSize = 1;
		if (srcHeight > desheight || srcWidth > deswitdh) {
			if (srcWidth > srcHeight) {
				nSampleSize = Math.round(srcHeight / desheight);
			} else {
				nSampleSize = Math.round(srcWidth / deswitdh);
			}
		}

		option = new BitmapFactory.Options();
		option.inSampleSize = nSampleSize;
		Bitmap bitmap = BitmapFactory.decodeFile(strPath, option);
		
//		int nDegree = readPictureDegree(strPath);
		Bitmap bmp = rotateBitmap(bitmap, nDegree);
		return new BitmapDrawable(a.getResources(), bmp);
	}

	public static int readPictureDegree(String strPath) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(strPath);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			default:
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	public static Bitmap rotateBitmap(Bitmap bitmap, int nDegree){
		if (nDegree==0 || nDegree==360 || bitmap==null) {
			return bitmap;
		}
		Matrix matrix = new Matrix();
		matrix.setRotate(nDegree, bitmap.getWidth()/2, bitmap.getHeight()/2);
		
		Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		bitmap.recycle();		
		return bmp;
	}
	
	public static void cleanImageView(ImageView imageView) {
		if (!(imageView.getDrawable() instanceof BitmapDrawable)) {
			return;
		}

		BitmapDrawable b = (BitmapDrawable) imageView.getDrawable();
		b.getBitmap().recycle();
		imageView.setImageDrawable(null);
	}
}
