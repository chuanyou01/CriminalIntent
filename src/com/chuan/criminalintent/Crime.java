package com.chuan.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {
	private UUID mId;
	private String mTitle;
	private Date mDate;
	private boolean bSolved;
	public Crime() {
		mId = UUID.randomUUID();
		mDate  = new Date();
	}
	public String getTitle() {
		return mTitle;
	}
	public void setTitle(String title) {
		mTitle = title;
	}
	public UUID getId() {
		return mId;
	}
	public Date getDate() {
		return mDate;
	}
	public void setDate(Date date) {
		mDate = date;
	}
	public boolean isbSolved() {
		return bSolved;
	}
	public void setbSolved(boolean bSolved) {
		this.bSolved = bSolved;
	}
	public String toString(){
		return mTitle;
	}
}
