package com.ats.adminpanel.model.salesdashboard;

public class ItemListBySubCat {

	private int itemId;
	private String itemName;
	private int catId;
	private int subCatId;
	private float sale;
	private float saleQty;
	private float crn;
	private float crnQty;
	private float net;
	private float netQty;
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public int getCatId() {
		return catId;
	}
	public void setCatId(int catId) {
		this.catId = catId;
	}
	public int getSubCatId() {
		return subCatId;
	}
	public void setSubCatId(int subCatId) {
		this.subCatId = subCatId;
	}
	public float getSale() {
		return sale;
	}
	public void setSale(float sale) {
		this.sale = sale;
	}
	public float getSaleQty() {
		return saleQty;
	}
	public void setSaleQty(float saleQty) {
		this.saleQty = saleQty;
	}
	public float getCrn() {
		return crn;
	}
	public void setCrn(float crn) {
		this.crn = crn;
	}
	public float getCrnQty() {
		return crnQty;
	}
	public void setCrnQty(float crnQty) {
		this.crnQty = crnQty;
	}
	public float getNet() {
		return net;
	}
	public void setNet(float net) {
		this.net = net;
	}
	public float getNetQty() {
		return netQty;
	}
	public void setNetQty(float netQty) {
		this.netQty = netQty;
	}
	@Override
	public String toString() {
		return "ItemListBySubCat [itemId=" + itemId + ", itemName=" + itemName + ", catId=" + catId + ", subCatId="
				+ subCatId + ", sale=" + sale + ", saleQty=" + saleQty + ", crn=" + crn + ", crnQty=" + crnQty
				+ ", net=" + net + ", netQty=" + netQty + "]";
	}
	
}
