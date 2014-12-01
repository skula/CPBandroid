package com.skula.cpb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.skula.cpb.models.Episode;

public class EpisodeAdapter extends ArrayAdapter<Episode> {

	Context context;
	int layoutResourceId;
	Episode data[] = null;

	public EpisodeAdapter(Context context, int layoutResourceId, Episode[] data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Episode episode = data[position];
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.episodeitemlayout, parent, false);
		
		//TextView id = (TextView) rowView.findViewById(R.id.id);
		//id.setText(torrent.getId());	
		
		return rowView;
	}
}
