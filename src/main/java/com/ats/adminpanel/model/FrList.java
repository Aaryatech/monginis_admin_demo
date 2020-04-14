package com.ats.adminpanel.model;

import java.util.List;
 
public class FrList {
	 
	private int id; 
	private int frId; 
	private String frName; 
	private int abcType; 
	List<ItemListForDispatchReport> itemList;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFrId() {
		return frId;
	}
	public void setFrId(int frId) {
		this.frId = frId;
	}
	public String getFrName() {
		return frName;
	}
	public void setFrName(String frName) {
		this.frName = frName;
	}
	public int getAbcType() {
		return abcType;
	}
	public void setAbcType(int abcType) {
		this.abcType = abcType;
	}
	public List<ItemListForDispatchReport> getItemList() {
		return itemList;
	}
	public void setItemList(List<ItemListForDispatchReport> itemList) {
		this.itemList = itemList;
	}
	@Override
	public String toString() {
		return "FrList [id=" + id + ", frId=" + frId + ", frName=" + frName + ", abcType=" + abcType + ", itemList="
				+ itemList + "]";
	}
	
	

}
