package com.chuan.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

public class Photo {
	private static final String JSON_FILENAME = "filename";
	private static final String JSON_ORITATION = "noritation";
	private String mFileName;
	private int mnOritation;
	public Photo(String fileName, int Oritation) {
		mFileName = fileName;
		mnOritation = Oritation;
	}
	public Photo(JSONObject json) throws JSONException{
		mFileName = json.getString(JSON_FILENAME);
		mnOritation = json.getInt(JSON_ORITATION);
	}
	public JSONObject toJson() throws JSONException{
		JSONObject json = new JSONObject();
		json.put(JSON_FILENAME, mFileName);
		json.put(JSON_ORITATION, mnOritation);
		return json;
	}
	public String getFileName(){
		return mFileName;
	}
	public int getnOritation() {
		return mnOritation;
	}
	public void setnOritation(int oritation) {
		mnOritation = oritation;
	}
}
