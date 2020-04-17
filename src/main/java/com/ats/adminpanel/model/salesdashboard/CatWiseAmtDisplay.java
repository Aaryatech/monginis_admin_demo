package com.ats.adminpanel.model.salesdashboard;

public class CatWiseAmtDisplay {

	private int catId;
	private String catName;
	private float sale;
	private float crn;
	private float net;

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public float getSale() {
		return sale;
	}

	public void setSale(float sale) {
		this.sale = sale;
	}

	public float getCrn() {
		return crn;
	}

	public void setCrn(float crn) {
		this.crn = crn;
	}

	public float getNet() {
		return net;
	}

	public void setNet(float net) {
		this.net = net;
	}

	@Override
	public String toString() {
		return "CatWiseAmtDisplay [catId=" + catId + ", catName=" + catName + ", sale=" + sale + ", crn=" + crn
				+ ", net=" + net + "]";
	}

}
