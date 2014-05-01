package com.skula.cpb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.skula.cpb.services.BetaserieService;
import com.skula.cpb.services.CestPasBienService;
import com.skula.cpb.services.DatabaseService;
import com.skula.cpb.services.TransmissionService;


public class MainActivity extends Activity {
	private ListView itemList;
	private SearchView searchView;
	private BetaserieService betaSrv;
	private String[] unseenEpisodes;
	private DatabaseService dbService;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);	
		setContentView(R.layout.activity_main);
		
		this.dbService = new DatabaseService(this);
		this.betaSrv = new BetaserieService();
		updateEpisodeList();
		//Toast.makeText(this, "Aucune connexion au réseau !", Toast.LENGTH_SHORT).show();
		
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
		
		Button btnList = (Button) findViewById(R.id.btn_list);
		btnList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder dialBuilder = new AlertDialog.Builder(v.getContext());
				dialBuilder.setTitle("Episodes non téléchargés");
				final ListView lv = new ListView(v.getContext());
				ArrayAdapter<String> ada = new ArrayAdapter<String>(v.getContext(),android.R.layout.simple_expandable_list_item_1, unseenEpisodes);
				lv.setAdapter(ada);		
				dialBuilder.setView(lv);
				final Dialog dialog = dialBuilder.create();
				dialog.show();
				
				lv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						searchView.setQuery(lv.getItemAtPosition(arg2).toString(), true);
						dialog.cancel();
					}
				});
			}
		});
		
		this.itemList = (ListView) findViewById(R.id.torrent_list);
		itemList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id) {
				HashMap<String, String> m = (HashMap) itemList.getAdapter().getItem(position);
				String url = m.get("url");
				
				String ip = dbService.getParam(Cnst.PARAM_IP_TRANSMISSION);
				String port = dbService.getParam(Cnst.PARAM_PORT_TRANSMISSION);
				String login = dbService.getParam(Cnst.PARAM_LOGIN_TRANSMISSION);
				String pw = dbService.getParam(Cnst.PARAM_PW_TRANSMISSION);
				
				TransmissionService ts = new TransmissionService(ip, Integer.valueOf(port), login, pw);
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
		List<String> l = betaSrv.getUnseenEpisodes();
		if(l==null){
			this.unseenEpisodes = new String[0];
		}else{
			this.unseenEpisodes = l.toArray(new String[l.size()]);
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
		case R.id.param:
			Intent intent = new Intent(this,
					ParamActivity.class);
			try{
			startActivity(intent);
			}catch(Exception e){
				e.getMessage();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
