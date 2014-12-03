package com.skula.cpb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.skula.cpb.models.Episode;

public class DownloadingAdapter extends ArrayAdapter<Episode> {

	Context context;
	int layoutResourceId;
	Episode data[] = null;

	public DownloadingAdapter(Context context, int layoutResourceId, Episode[] data) {
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
		
		//TextView shortcut = (TextView) rowView.findViewById(R.id.episode_shortcut);
		//shortcut.setText(episode.getShortcut());	

		return rowView;
	}
}
