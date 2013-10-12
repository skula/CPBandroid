package com.skula.cpb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.skula.cpb.services.CestPasBienService;
import com.skula.cpb.services.TransmissionService;


public class MainActivity extends Activity {
	private ListView itemList;
	private EditText request;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		setContentView(R.layout.activity_main);
	
		this.request = (EditText) findViewById(R.id.search_request);
		
		this.itemList = (ListView) findViewById(R.id.torrent_list);
		itemList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id) {
				HashMap<String, String> m = (HashMap) itemList.getAdapter().getItem(position);
				String url = m.get("url");
				
				TransmissionService ts = new TransmissionService();
				if(ts.addTorrent(url)){
					Toast.makeText(v.getContext(), "Torrent ajouté !", Toast.LENGTH_SHORT).show();
				} else{
					Toast.makeText(v.getContext(), "Erreur !", Toast.LENGTH_SHORT).show();
				}
				return true;
			}
		});
		
		Button btnSearch = (Button)findViewById(R.id.btn_search);
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String req = request.getText().toString();
				if(!req.equals("")){
					try {
						fillList(CestPasBienService.search(req));
					} catch (Exception e) {
					}
				}
			}
		});
	}

	private void fillList(List<Map<String, String>> list) {
		SimpleAdapter simpleAdpt = new SimpleAdapter(this, list, 
			R.layout.torrentlayout, new String[] {"url", "label"}, 
												 new int[] {R.id.torrent_url, R.id.torrent_label});
		itemList.setAdapter(simpleAdpt);
	}
}
