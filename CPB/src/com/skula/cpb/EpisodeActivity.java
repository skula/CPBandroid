package com.skula.cpb;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.skula.cpb.models.Episode;
import com.skula.cpb.services.BetaserieService;
import com.skula.cpb.services.DatabaseService;

public class EpisodeActivity extends Activity {
	private ListView itemList;
	private DatabaseService dbService;
	private BetaserieService btService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.episodeslayout);

		btService = new BetaserieService();
		dbService = new DatabaseService(this);
		itemList = (ListView) findViewById(R.id.episode_list);
		updateList();

		itemList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				Episode item = (Episode) itemList.getItemAtPosition(position);
				item.getNumber();
			}
		});
	}

	private void updateList() {
		try{
		List<Episode> list = btService.getUnseenEpisodes2();
		Episode itemArray[] = (Episode[]) list.toArray(new Episode[list.size()]);
		EpisodeAdapter adapter = new EpisodeAdapter(this, R.layout.episodeitemlayout, itemArray);
		itemList.setAdapter(adapter);
		}catch(Exception e){
			e.getMessage();
		}
	}
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_favorites:
			updateList(dbService.getFavorites());
			return true;
		default:
			return false;
		}
	}

	*/
}