package com.ats.adminpanel.model;

public class TypeWiseItemTotal {
	
	private int id; 
	private int itemId; 
	private String itemName; 
	private Double itemMrp2; 
	private float aTotal; 
	private float bTotal;
	private float cTotal;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	public Double getItemMrp2() {
		return itemMrp2;
	}
	public void setItemMrp2(Double itemMrp2) {
		this.itemMrp2 = itemMrp2;
	}
	public float getaTotal() {
		return aTotal;
	}
	public void setaTotal(float aTotal) {
		this.aTotal = aTotal;
	}
	public float getbTotal() {
		return bTotal;
	}
	public void setbTotal(float bTotal) {
		this.bTotal = bTotal;
	}
	public float getcTotal() {
		return cTotal;
	}
	public void setcTotal(float cTotal) {
		this.cTotal = cTotal;
	}
	@Override
	public String toString() {
		return "TypeWiseItemTotal [id=" + id + ", itemId=" + itemId + ", itemName=" + itemName + ", itemMrp2="
				+ itemMrp2 + ", aTotal=" + aTotal + ", bTotal=" + bTotal + ", cTotal=" + cTotal + "]";
	}
	
	

}
