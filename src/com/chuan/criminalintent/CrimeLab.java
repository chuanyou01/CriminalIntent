package com.chuan.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

public class CrimeLab {
	private static CrimeLab sCrimeLab;
	private Context mAppContext;
	
	private ArrayList<Crime> mCrimes;
	private CrimeLab( Context appContext) {
		mAppContext = appContext;
		mCrimes = new ArrayList<Crime>();
//		for (int i = 0; i < 30; i++) {
//			Crime c = new Crime();
//			c.setTitle("Crime #" + i);
//			c.setbSolved(i%2==0);
//			mCrimes.add(c);
//		}
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
}
