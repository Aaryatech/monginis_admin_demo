package com.ats.adminpanel.model.salesdashboard;

public class CatWiseSaleTotal {

	private int catId;
	private String catName;
	private float total;

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

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "CatWiseSaleTotal [catId=" + catId + ", catName=" + catName + ", total=" + total + "]";
	}
	
}
