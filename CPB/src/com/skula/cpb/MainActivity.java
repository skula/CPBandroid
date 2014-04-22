package com.skula.cpb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
	private Spinner episodesSpn;
	private SearchView searchView;
	private BetaserieService betaSrv;
	private String[] sts;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);	
		setContentView(R.layout.activity_main);
	
		
		
		this.searchView = (SearchView) findViewById(R.id.episodes_search);
		searchView.setIconifiedByDefault(false);
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			public boolean onQueryTextSubmit(String query) {
				if(!query.equals("")){
					try {
						fillList(CestPasBienService.search(query));
					} catch (Exception e) {
					}
				}
				return false;
			}
			
			public boolean onQueryTextChange(String newText) {
				
				return false;
			}
		});		
		
		Button b  = (Button) findViewById(R.id.btn_list);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder l = new AlertDialog.Builder(v.getContext());
				l.setTitle("Liste des épisodes");
				final ListView lv = new ListView(v.getContext());
				ArrayAdapter<String> ada = new ArrayAdapter<String>(v.getContext(),android.R.layout.simple_expandable_list_item_1, sts);
				lv.setAdapter(ada);
					
				l.setView(lv);
				final Dialog dialog = l.create();
				dialog.show();
				
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						searchView.setQuery(lv.getItemAtPosition(arg2).toString(), false);
						dialog.cancel();
					}
				});
				
		
				
			}
		});
		
		this.betaSrv = new BetaserieService();
		List<String> prout = betaSrv.getUnseenEpisodes();
		sts = prout.toArray(new String[prout.size()]);
		//sts= new String[]{"coucou", "test","coucou", "test","coucou", "test","coucou", "test","coucou", "test","coucou", "test","coucou", "test","coucou", "test","coucou", "test","coucou", "test","coucou", "test","coucou", "test"};
		
		
		this.episodesSpn = (Spinner) findViewById(R.id.episodes_spn);
		updateEpisodeList();

		episodesSpn.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				searchView.setQuery(episodesSpn.getItemAtPosition(arg2).toString(), true);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.refresh:
			updateEpisodeList();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
