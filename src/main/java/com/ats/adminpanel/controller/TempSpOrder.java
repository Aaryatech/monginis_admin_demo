package com.ats.adminpanel.controller;

public class TempSpOrder {

	private int index;

	private int spId;
	private String spCode;

	private String spName;

	private String flavour;

	private float spWeight;
	
	private float spAmt;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getSpId() {
		return spId;
	}

	public void setSpId(int spId) {
		this.spId = spId;
	}

	public String getSpCode() {
		return spCode;
	}

	public void setSpCode(String spCode) {
		this.spCode = spCode;
	}

	public String getSpName() {
		return spName;
	}

	public void setSpName(String spName) {
		this.spName = spName;
	}

	public String getFlavour() {
		return flavour;
	}

	public void setFlavour(String flavour) {
		this.flavour = flavour;
	}

	public float getSpWeight() {
		return spWeight;
	}

	public void setSpWeight(float spWeight) {
		this.spWeight = spWeight;
	}

	public float getSpAmt() {
		return spAmt;
	}

	public void setSpAmt(float spAmt) {
		this.spAmt = spAmt;
	}

	@Override
	public String toString() {
		return "TempSpOrder [index=" + index + ", spId=" + spId + ", spCode=" + spCode + ", spName=" + spName
				+ ", flavour=" + flavour + ", spWeight=" + spWeight + ", spAmt=" + spAmt + "]";
	}
	

}
