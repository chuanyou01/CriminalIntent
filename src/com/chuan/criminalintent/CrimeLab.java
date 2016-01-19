package com.chuan.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONException;

import android.content.Context;
import android.util.Log;

public class CrimeLab {
	private static final String TAG="CrimeLab";
	private static final String FILENAME = "crimes.json";
	private CriminalIntentJSONSerializer mSerializer;
	
	private static CrimeLab sCrimeLab;
	private Context mAppContext;
	
	
	private ArrayList<Crime> mCrimes;
	private CrimeLab( Context appContext) {
		mAppContext = appContext;
		mSerializer = new CriminalIntentJSONSerializer(appContext, FILENAME);
		try {
			mCrimes = mSerializer.loadCrimes();
		} catch (Exception e) {
			mCrimes = new ArrayList<Crime>();
			Log.d(TAG, "error loading crims e:" + e);
		}
	}
	public static CrimeLab get(Context appContext){
		if (sCrimeLab==null) {
			sCrimeLab = new CrimeLab(appContext.getApplicationContext());
		}
		return sCrimeLab;
	}
	public ArrayList<Crime> getCrimes() {
		return mCrimes;
	}
	
	public Crime getCrime(UUID id){
		for (Crime c: mCrimes){
			if (c.getId().equals(id)) {
				return c;
			}
		}
		return null;
	}
	
	public void addCrime(Crime c){
		mCrimes.add(c);
	}
	
	public boolean saveCriems(){
		try {
			mSerializer.saveCrimes(mCrimes);
			Log.d(TAG, "crimes saved to file");
			return true;
		} catch (Exception e) {
			Log.d(TAG, "error saving crimes :" + e);
		}
		return false;
	}
}
