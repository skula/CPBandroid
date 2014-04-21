package com.skula.cpb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.skula.cpb.services.BetaserieService;
import com.skula.cpb.services.CestPasBienService;
import com.skula.cpb.services.TransmissionService;


public class MainActivity extends Activity {
	private ListView itemList;
	private EditText request;
	private Spinner episodesSpn;
	private SearchView searchView;
	private BetaserieService betaSrv;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);	
		setContentView(R.layout.activity_main);
	
		this.searchView = (SearchView) findViewById(R.id.episodes_search);
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			public boolean onQueryTextSubmit(String query) {
				
				return false;
			}
			
			public boolean onQueryTextChange(String newText) {
				
				return false;
			}
		});
		searchView.setQuery("test", true);
		
		this.betaSrv = new BetaserieService();
		this.episodesSpn = (Spinner) findViewById(R.id.episodes_spn);
		updateEpisodeList();
		
		
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
		
		Button btnClear = (Button)findViewById(R.id.btn_clear);
		btnClear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				request.setText("");
			}
		});
		
		Button btnPaste = (Button)findViewById(R.id.btn_paste);
		btnPaste.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				request.setText(String.valueOf(episodesSpn.getSelectedItem()));
			}
		});
		
		Button btnRefresh = (Button)findViewById(R.id.btn_refresh);
		btnRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateEpisodeList();
			}
		});
	}

	private void fillList(List<Map<String, String>> list) {
		SimpleAdapter simpleAdpt = new SimpleAdapter(this, list, 
			R.layout.torrentlayout, new String[] {"url", "label"}, 
												 new int[] {R.id.torrent_url, R.id.torrent_label});
		itemList.setAdapter(simpleAdpt);
	}
	
	private void updateEpisodeList(){
		ArrayAdapter<String> epAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, betaSrv.getUnseenEpisodes());
		epAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		try{
		episodesSpn.setAdapter(epAdapter);
		}catch(Exception e){
			e.getMessage();
		}
	}
}
