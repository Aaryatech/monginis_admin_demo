package com.ats.adminpanel.model;

public class FlavourConf{
	
	private int flavId;
	
	private int spfId;
	
	private int spType;
	
	private float rate;
	
	private float mrp;
	
	private int delStatus;
	
	private String exVar1;
	
	private int exInt1;
	
	private int spId;

	
	
	public int getSpType() {
		return spType;
	}

	public void setSpType(int spType) {
		this.spType = spType;
	}

	public int getFlavId() {
		return flavId;
	}

	public void setFlavId(int flavId) {
		this.flavId = flavId;
	}

	public int getSpfId() {
		return spfId;
	}

	public void setSpfId(int spfId) {
		this.spfId = spfId;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public float getMrp() {
		return mrp;
	}

	public void setMrp(float mrp) {
		this.mrp = mrp;
	}

	public int getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(int delStatus) {
		this.delStatus = delStatus;
	}

	public String getExVar1() {
		return exVar1;
	}

	public void setExVar1(String exVar1) {
		this.exVar1 = exVar1;
	}

	public int getExInt1() {
		return exInt1;
	}

	public void setExInt1(int exInt1) {
		this.exInt1 = exInt1;
	}

	public int getSpId() {
		return spId;
	}

	public void setSpId(int spId) {
		this.spId = spId;
	}

	@Override
	public String toString() {
		return "FlavourConf [flavId=" + flavId + ", spfId=" + spfId + ", spType=" + spType + ", rate=" + rate + ", mrp="
				+ mrp + ", delStatus=" + delStatus + ", exVar1=" + exVar1 + ", exInt1=" + exInt1 + ", spId=" + spId
				+ "]";
	}

}
