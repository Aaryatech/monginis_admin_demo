package com.ats.adminpanel.model.salesreport3;

import java.util.List;


public class FrWiseItemSaleList {
	
	private int subCatId;
	private String subCatName;

	/*
	 * private float totalSoldAmt; private int totalSoldQty;
	 * 
	 * private float totalTaxableAmt; private float totalTax;
	 * 
	 * 
	 * private int totalVarQty; private float totalVarAmt;
	 * 
	 * private float totalVarTaxableAmt; private float totalVarTax;
	 * 
	 * private int totalRetQty; private float totalRetAmt;
	 * 
	 * private float totalRetTaxableAmt; private float totalRetTax;
	 */
	List<SubCatWiseItemList> itemList = null;

	public int getSubCatId() {
		return subCatId;
	}

	public void setSubCatId(int subCatId) {
		this.subCatId = subCatId;
	}

	public String getSubCatName() {
		return subCatName;
	}

	public void setSubCatName(String subCatName) {
		this.subCatName = subCatName;
	}

	public List<SubCatWiseItemList> getItemList() {
		return itemList;
	}

	public void setItemList(List<SubCatWiseItemList> itemList) {
		this.itemList = itemList;
	}

	@Override
	public String toString() {
		return "FrWiseItemSaleList [subCatId=" + subCatId + ", subCatName=" + subCatName + ", itemList=" + itemList
				+ "]";
	}
	
	

}
