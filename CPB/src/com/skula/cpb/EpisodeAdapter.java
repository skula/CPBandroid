package com.skula.cpb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
		
		TextView shortcut = (TextView) rowView.findViewById(R.id.episode_shortcut);
		shortcut.setText(episode.getShortcut());	
		TextView season = (TextView) rowView.findViewById(R.id.episode_season);
		season.setText(episode.getSeason()+"");
		TextView number = (TextView) rowView.findViewById(R.id.episode_number);
		number.setText(episode.getNumber()+"");
		TextView label = (TextView) rowView.findViewById(R.id.episode_label);
		label.setText(episode.toString());
		
		return rowView;
	}
}
