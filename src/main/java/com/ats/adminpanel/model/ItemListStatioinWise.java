package com.ats.adminpanel.model;

import java.util.List;

public class ItemListStatioinWise {
	
	private int stationNo;
	List<TypeWiseItemTotal> typeWiseItemTotalList;
	public int getStationNo() {
		return stationNo;
	}
	public void setStationNo(int stationNo) {
		this.stationNo = stationNo;
	}
	public List<TypeWiseItemTotal> getTypeWiseItemTotalList() {
		return typeWiseItemTotalList;
	}
	public void setTypeWiseItemTotalList(List<TypeWiseItemTotal> typeWiseItemTotalList) {
		this.typeWiseItemTotalList = typeWiseItemTotalList;
	}
	@Override
	public String toString() {
		return "ItemListStatioinWise [stationNo=" + stationNo + ", typeWiseItemTotalList=" + typeWiseItemTotalList
				+ "]";
	}
	 

}
