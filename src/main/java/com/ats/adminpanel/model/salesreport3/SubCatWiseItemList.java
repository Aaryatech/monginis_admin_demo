package com.ats.adminpanel.model.salesreport3;

import java.util.List;


public class SubCatWiseItemList {
	
	private int subCatId;
	private int itemId;
	private String itemName;
	
	/*private float itemTotalSoldAmt;
	private int itemTotalSoldQty;
	
	private float itemTotalTaxableAmt;
	private float itemTotalTax;
	
	
	private int itemTotalVarQty;
	private float itemTotalVarAmt;

	private float itemTotalVarTaxableAmt;
	private float itemTotalVarTax;

	private int itemTotalRetQty;
	private float itemTotalRetAmt;
	
	private float itemTotalRetTaxableAmt;
	private float itemTotalRetTax;*/
	
	List<ItemWiseFrList> frList=null;

	public int getSubCatId() {
		return subCatId;
	}

	public void setSubCatId(int subCatId) {
		this.subCatId = subCatId;
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

	public List<ItemWiseFrList> getFrList() {
		return frList;
	}

	public void setFrList(List<ItemWiseFrList> frList) {
		this.frList = frList;
	}

	@Override
	public String toString() {
		return "SubCatWiseItemList [subCatId=" + subCatId + ", itemId=" + itemId + ", itemName=" + itemName
				+ ", frList=" + frList + "]";
	}
	
	

}
