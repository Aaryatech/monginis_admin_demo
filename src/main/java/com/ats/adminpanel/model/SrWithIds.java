package com.ats.adminpanel.model;

public class SrWithIds {
	
	private int id;
	private int sr;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSr() {
		return sr;
	}
	public void setSr(int sr) {
		this.sr = sr;
	}
	@Override
	public String toString() {
		return "SrWithIds [id=" + id + ", sr=" + sr + "]";
	}
	
	

}
