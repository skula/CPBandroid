package com.skula.cpb.services;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CestPasBienService {
	private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
	private static final String CONTENT_LANGUAGE = "en-US";
	private static final String USER_ARENT = "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11";
	private static final String COOKIE = ""; //__cfduid=df83fae0487b4e39f73f8837ca250e35e1369247829
	
	private static final String SEARCH_URL = "http://www.cpasbien.io/recherche/";
	private static final String DL_TORRENT_URL = "http://www.cpasbien.io/dl-torrent";
	private static final String TORRENT_URL = "http://www.cpasbien.io/telechargement/";
	private static final String PARAMS = "champ_recherche=";

	
	public static List<Map<String, String>> search(String search) throws Exception {
		List<Map<String, String>> res = new ArrayList<Map<String, String>>();
		
		URL url = null;
	    HttpURLConnection connection = null;        
	    try{
	        //Create connection
	        url = new URL(SEARCH_URL);
	        connection = (HttpURLConnection)url.openConnection();
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Content-Type", CONTENT_TYPE);
	        connection.setRequestProperty("Content-Language", CONTENT_LANGUAGE); 
	        connection.setRequestProperty("User-Agent", USER_ARENT);
	        connection.setRequestProperty("Cookie", COOKIE); 
	        connection.setUseCaches(false);
	        connection.setDoInput(true);
	        connection.setDoOutput(true);

	        //send Request 
	        DataOutputStream dataout = new DataOutputStream(connection.getOutputStream());
	        dataout.writeBytes(PARAMS+search);
	        dataout.flush();
	        dataout.close();

	        //get response
	        InputStream is = connection.getInputStream();
	        BufferedReader br = new BufferedReader(new InputStreamReader(is));
	        String line;
	        StringBuffer response = new StringBuffer();
	
			Map<String, String> map = null;
	        while((line = br.readLine()) != null){
	        	if(line.contains(DL_TORRENT_URL) && (line.contains("class=\"titre\""))){
	        		res = parseList(line);
	        	}
	        }
			
	        //System.out.println(response.toString());
	        br.close();
	        //System.out.println(response.toString());
	    }catch(Exception e){
	        System.out.println("Unable to full create connection");
	        e.printStackTrace();
	    }finally {

	          if(connection != null) {
	            connection.disconnect(); 
	          }
	    }
		return res;
	}
	
	private static Map<String, String> parseItem(String line) {
        Map<String, String> res = new HashMap<String, String>();
        String url = line.substring(8, line.indexOf(".html\" title="));
        url = url.substring((url.lastIndexOf("/") + 1));

        res.put("url", TORRENT_URL + url + ".torrent");
        String title = line.substring(line.indexOf("class=\"titre\">") + 14);
        res.put("label", title);

        return res;
  }

  public static List<Map<String, String>> parseList(String line) {
        List<Map<String, String>> res = new ArrayList<Map<String, String>>();

        String s3 = line;
        int a = s3.indexOf("a href=\"");
        try {
               while (a != -1) {
                      int b = s3.indexOf("</a><div class");
                      String s = s3.substring(a, b);
                      res.add(parseItem(s));
                      s3 = s3.substring(b + 14);
                      a = s3.indexOf("a href=\"");
               }
        } catch (Exception e) {
               e.getMessage();
               return null;
        }
       
        return res;
  }
}