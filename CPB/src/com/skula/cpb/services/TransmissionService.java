package com.skula.cpb.services;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;



public class TransmissionService {
	private static final int HTTP_CONFLICT_CODE = 409;
	private static final String SESSION_HEADER = "X-Transmission-Session-Id";

	private static final String DEFAULT_HOST = "31.34.50.65";
	private static final int DEFAULT_PORT = 9091;
	private static final String DEFAULT_USER = "slown";
	private static final String DEFAULT_PASS = "salope";
	private static final String DEFAULT_URL_SUFFIX = "/transmission/rpc";

	public  URL url;
	private String host;
	private String login;
	private String pass;
	private int port;
	private String sessionId;
	private String urlSuffix;

	public TransmissionService() {
		this.host = DEFAULT_HOST;
		this.port = DEFAULT_PORT;
		this.login = DEFAULT_USER;
		this.pass = DEFAULT_PASS;
		this.urlSuffix = DEFAULT_URL_SUFFIX;
	}
	
	public TransmissionService(String host, int port, String user, String pass) {
		this.host = host;
		this.port = port;
		this.login = user;
		this.pass = pass;
		this.urlSuffix = DEFAULT_URL_SUFFIX;
	}
	
	private String getUrl(){
		return "http://"+host+":"+port+urlSuffix;
	}
	
	public boolean addTorrent(String url){
		try{
			JSONObject request = JSONService.addTorrent(url);
			JSONObject response = postCommand(request);		
			return JSONService.getAddStatus(response).equals("success");
		}catch(Exception e){
			return false;
		}
	}
	
	private JSONObject postCommand(JSONObject command)
			throws IOException, Exception, JSONException {

		String json = command.toString(2);
		
		HttpPost request = new HttpPost(getUrl());
		StringEntity entity = new StringEntity(json);
		request.addHeader("Content-Type", "application/json-rpc");
        request.setHeader("Accept", "application/json-rpc");
        if (sessionId != null)
        	request.setHeader(SESSION_HEADER, sessionId);
        request.setEntity(entity); 

        HttpResponse response =null;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpHost targetHost = new HttpHost(this.host, this.port, "http"); 
        httpClient.getCredentialsProvider().setCredentials(
		        new AuthScope(targetHost.getHostName(), targetHost.getPort()), 
		        new UsernamePasswordCredentials(this.login, this.pass));

        HttpConnectionParams.setSoTimeout(httpClient.getParams(), 2000); 
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),2000); 


        response = httpClient.execute(request); 
        StatusLine a = response.getStatusLine();
        if (a.getStatusCode() == HTTP_CONFLICT_CODE) {
			String sessId = response.getLastHeader(SESSION_HEADER).getValue();
			if (sessId != null) {
				this.sessionId = sessId;
				return postCommand(command);
			}
		}
		
        DataInputStream is = new DataInputStream(response.getEntity().getContent());
        String line = null;
        StringBuilder sb= new StringBuilder();
        while((line = is.readLine()) != null){
            sb.append(line);
        }

		JSONTokener toker = new JSONTokener(sb.toString());
        return new JSONObject(toker);
	}
}
