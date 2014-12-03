package com.skula.cpb.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.skula.cpb.models.Downloading;

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
	
	public static JSONObject getTorrents() throws JSONException {
		JSONArray fieldsContent = new JSONArray();
		for(String s : Downloading.torrentFieldName){
			fieldsContent.put(s);
		}
		JSONObject fields = new JSONObject();
		fields.put("fields", fieldsContent);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("arguments", fields);
		jsonObject.put("method", "torrent-get");
		return jsonObject;
	}
	
	public static List<Downloading> getTorretList(JSONObject jsonObject) throws JSONException {
		String response = jsonObject.getString("result");
		if(!response.equals("success")){
			return null;
		}
		JSONObject arguments = jsonObject.getJSONObject("arguments");
		JSONArray torrents = arguments.getJSONArray("torrents");
		
		List<Downloading> res = new ArrayList<Downloading>();
		for(int i=0; i<torrents.length(); i++){
			JSONObject jsonTorrent = torrents.getJSONObject(i);
			Downloading dl = new Downloading();	
			dl.setDownloadDir(jsonTorrent.getString("downloadDir"));
			dl.setId(jsonTorrent.getString("id"));
			dl.setLeftUntilDone(jsonTorrent.getString("leftUntilDone"));
			dl.setPercentDone(jsonTorrent.getString("percentDone"));
			dl.setRateDownload(jsonTorrent.getString("rateDownload"));
			dl.setName(jsonTorrent.getString("name"));
			dl.setStatus(jsonTorrent.getString("status"));
			dl.setTotalSize(jsonTorrent.getString("totalSize"));
			res.add(dl);
		}		
		return res;
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject removeTorrent(String id) throws JSONException {
		JSONArray list = new JSONArray();
		list.put(String.valueOf(id));

		JSONObject arguments = new JSONObject();
		arguments.put("ids", list);
		arguments.put("delete-local-data", false);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("arguments", arguments);
		jsonObject.put("method", "torrent-remove");
		return jsonObject;
	}

	public static String getAddStatus(JSONObject jsonObject) throws JSONException {
		return jsonObject.getString("result");
	}
}