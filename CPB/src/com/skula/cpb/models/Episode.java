package com.skula.cpb.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Episode {
	private String title;
	private int season;
	private int number;
	private String shortcut;

	public Episode() {
	}

	public Episode(Episode ep) {
		this.title = ep.getTitle();
		this.season = ep.getSeason();
		this.number = ep.getNumber();
	}

	public Episode(String title, String id) {
		this.title = title;
		this.season = Integer.valueOf(id.substring(id.indexOf("S")+1,id.indexOf("E")));		
		this.number = Integer.valueOf(id.substring(id.indexOf("E")+1));
	}
	
	public Episode(String title, String id, String shortcut) {
		this.title = title;
		this.season = Integer.valueOf(id.substring(id.indexOf("S")+1,id.indexOf("E")));		
		this.number = Integer.valueOf(id.substring(id.indexOf("E")+1));
		this.shortcut = shortcut;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public int getSeason() {
		return this.season;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getNumber() {
		return this.number;
	}
	
	public String getShortcut() {
		return shortcut;
	}

	public void setShortcut(String shortcut) {
		this.shortcut = shortcut;
	}

	public String toString(){
		String s = "s" + (season < 10 ? "0" : "") + season;
		String n = "e" + (number < 10 ? "0" : "") + number;
		return title + " " + s + n;
	}
}