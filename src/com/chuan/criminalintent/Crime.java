package com.chuan.criminalintent;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Crime {
	private UUID mId;
	private String mTitle;
	private Date mDate;
	private boolean bSolved;
	private Photo  mPhoto;
	private static final String JSON_ID = "id";
	private static final String JSON_TITEL = "title";
	private static final String JSON_DATE = "date";
	private static final String JSON_SOLVED = "solved";
	private static final String JSON_PHOTO	= "photo";
	public Crime() {
		mId = UUID.randomUUID();
		mDate  = new Date();
	}
	public Crime(JSONObject json) throws JSONException{
		mId = UUID.fromString(json.getString(JSON_ID));
		if (json.has(JSON_TITEL)){
			mTitle = json.getString(JSON_TITEL);
		}
		mDate = new Date(json.getLong(JSON_DATE));
		bSolved = json.getBoolean(JSON_SOLVED);		
		if (json.has(JSON_PHOTO)) {
			mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
		}
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
	public JSONObject toJSON() throws JSONException{
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITEL, mTitle);
		json.put(JSON_SOLVED, bSolved);
		json.put(JSON_DATE, mDate.getTime());
		if (mPhoto!=null) {
			json.put(JSON_PHOTO, mPhoto.toJson());
		}
		return json;
	}
	public Photo getPhoto() {
		return mPhoto;
	}
	public void setPhoto(Photo photo) {
		mPhoto = photo;
	}
}
