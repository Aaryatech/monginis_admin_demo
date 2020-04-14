package com.ats.adminpanel.model;

public class SpKgSummaryDao {

private String uid;
	
	private int id;
	
	private String spName;
	
	private float spSelectedWeight;
	
	private float spQty;
	
	private float spValue;
	
	private float crnQty;
	
	private float crnSelectedWeight;
	
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

	public float getCrnSelectedWeight() {
		return crnSelectedWeight;
	}

	public void setCrnSelectedWeight(float crnSelectedWeight) {
		this.crnSelectedWeight = crnSelectedWeight;
	}

	public float getGrnGvnAmt() {
		return grnGvnAmt;
	}

	public void setGrnGvnAmt(float grnGvnAmt) {
		this.grnGvnAmt = grnGvnAmt;
	}

	@Override
	public String toString() {
		return "SpKgSummaryDao [uid=" + uid + ", id=" + id + ", spName=" + spName + ", spSelectedWeight="
				+ spSelectedWeight + ", spQty=" + spQty + ", spValue=" + spValue + ", crnQty=" + crnQty
				+ ", crnSelectedWeight=" + crnSelectedWeight + ", grnGvnAmt=" + grnGvnAmt + ", ]";
	}

	
}
