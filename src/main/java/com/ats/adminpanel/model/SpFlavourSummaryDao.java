package com.ats.adminpanel.model;


public class SpFlavourSummaryDao {
	
	private String uid;
	
	private int id;
	
	private String spName;
	
	private float spSelectedWeight;
	
	private int spFlavourId;
	
	private String spfName;
	
	private float spQty;
	
	private float spValue;
	
	private float crnQty;
	
	private float grnGvnAmt;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSpName() {
		return spName;
	}

	public void setSpName(String spName) {
		this.spName = spName;
	}

	public float getSpSelectedWeight() {
		return spSelectedWeight;
	}

	public void setSpSelectedWeight(float spSelectedWeight) {
		this.spSelectedWeight = spSelectedWeight;
	}

	public int getSpFlavourId() {
		return spFlavourId;
	}

	public void setSpFlavourId(int spFlavourId) {
		this.spFlavourId = spFlavourId;
	}

	public String getSpfName() {
		return spfName;
	}

	public void setSpfName(String spfName) {
		this.spfName = spfName;
	}

	public float getSpQty() {
		return spQty;
	}

	public void setSpQty(float spQty) {
		this.spQty = spQty;
	}

	public float getSpValue() {
		return spValue;
	}

	public void setSpValue(float spValue) {
		this.spValue = spValue;
	}

	public float getCrnQty() {
		return crnQty;
	}

	public void setCrnQty(float crnQty) {
		this.crnQty = crnQty;
	}

	public float getGrnGvnAmt() {
		return grnGvnAmt;
	}

	public void setGrnGvnAmt(float grnGvnAmt) {
		this.grnGvnAmt = grnGvnAmt;
	}

	@Override
	public String toString() {
		return "SpFlavourSummaryDao [uid=" + uid + ", id=" + id + ", spName=" + spName + ", spSelectedWeight="
				+ spSelectedWeight + ", spFlavourId=" + spFlavourId + ", spfName=" + spfName + ", spQty=" + spQty
				+ ", spValue=" + spValue + ", crnQty=" + crnQty + ", grnGvnAmt=" + grnGvnAmt + "]";
	}
	
	

}
