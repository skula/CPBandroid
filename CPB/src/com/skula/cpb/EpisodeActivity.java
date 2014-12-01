package com.skula.cpb;

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
			}
		});
		
		
	}

	private void updateList() {
		/*Episode itemArray[] = (Episode[]) list.toArray(new ExplorerItem[list.size()]);
		ItemAdapter adapter = new ItemAdapter(this, R.layout.itemlayout, itemArray);
		itemList.setAdapter(adapter);*/
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