package com.skula.cpb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.skula.cpb.services.DatabaseService;

public class DownloadingActivity extends Activity {
	private ListView itemList;
	private DatabaseService dbService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.downloadingslayout);

		dbService = new DatabaseService(this);
		itemList = (ListView) findViewById(R.id.dl_list);
		updateList();

		itemList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
			
			}
		});
	}

	private void updateList() {
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.transmission, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.search:
			intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			return true;
		case R.id.betaseries:
			intent = new Intent(this, EpisodeActivity.class);
			startActivity(intent);
			return true;
		case R.id.param:
			intent = new Intent(this, ParamActivity.class);
			startActivity(intent);
			return true;
		default:
			return false;
		}
	}
}