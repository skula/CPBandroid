package com.skula.cpb.services;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONService {

	@SuppressWarnings("unchecked")
	public static JSONObject addTorrent(String url) throws JSONException {
		JSONObject fields = new JSONObject();
		fields.put("filename", url);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("arguments", fields);
		jsonObject.put("method", "torrent-add");
		return jsonObject;
	}

	public static String getAddStatus(JSONObject jsonObject) throws JSONException {
		return jsonObject.getString("result");
	}
}