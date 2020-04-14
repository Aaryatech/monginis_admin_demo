package com.ats.adminpanel.model;
 

public class ItemListForDispatchReport {
	
	 
	private int id; 
	private int itemId; 
	private String itemName; 
	private Double itemMrp2; 
	private float orderQty; 
	private float editQty;
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
	public float getOrderQty() {
		return orderQty;
	}
	public void setOrderQty(float orderQty) {
		this.orderQty = orderQty;
	}
	public float getEditQty() {
		return editQty;
	}
	public void setEditQty(float editQty) {
		this.editQty = editQty;
	}
	@Override
	public String toString() {
		return "ItemListForDispatchReport [id=" + id + ", itemId=" + itemId + ", itemName=" + itemName + ", itemMrp2="
				+ itemMrp2 + ", orderQty=" + orderQty + ", editQty=" + editQty + "]";
	}
	
	

}
