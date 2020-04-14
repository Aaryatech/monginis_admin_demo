package com.ats.adminpanel.yearlyreport;

import java.util.List;


public class TempSubCatList {
	
	private int subCatId;
	private String subCatName;
	
	private float scTotalSoldQty;
	private float scTotalSoldAmt;
	private float scTotalVarQty;
	private float scTotalVarAmt;
	private float scTotalRetQty;
	private float scTotalRetAmt;
	private float scTotalNetQty;
	private float scTotalNetAmt;
	private float scTotalRetAmtPer;
	
	private float scTotalSoldTaxableAmt;
	private float scTotalVarTaxableAmt;
	private float scTotalRetTaxableAmt;
	private float scTotalNetTaxableAmt;
	private float scTotalRetTaxableAmtPer;
	
	private List<TempItemList> tempItemList=null;

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

	public List<TempItemList> getTempItemList() {
		return tempItemList;
	}

	public void setTempItemList(List<TempItemList> tempItemList) {
		this.tempItemList = tempItemList;
	}
	
	

	public float getScTotalSoldQty() {
		return scTotalSoldQty;
	}

	public void setScTotalSoldQty(float scTotalSoldQty) {
		this.scTotalSoldQty = scTotalSoldQty;
	}

	public float getScTotalSoldAmt() {
		return scTotalSoldAmt;
	}

	public void setScTotalSoldAmt(float scTotalSoldAmt) {
		this.scTotalSoldAmt = scTotalSoldAmt;
	}

	public float getScTotalVarQty() {
		return scTotalVarQty;
	}

	public void setScTotalVarQty(float scTotalVarQty) {
		this.scTotalVarQty = scTotalVarQty;
	}

	public float getScTotalVarAmt() {
		return scTotalVarAmt;
	}

	public void setScTotalVarAmt(float scTotalVarAmt) {
		this.scTotalVarAmt = scTotalVarAmt;
	}

	public float getScTotalRetQty() {
		return scTotalRetQty;
	}

	public void setScTotalRetQty(float scTotalRetQty) {
		this.scTotalRetQty = scTotalRetQty;
	}

	public float getScTotalRetAmt() {
		return scTotalRetAmt;
	}

	public void setScTotalRetAmt(float scTotalRetAmt) {
		this.scTotalRetAmt = scTotalRetAmt;
	}

	public float getScTotalNetQty() {
		return scTotalNetQty;
	}

	public void setScTotalNetQty(float scTotalNetQty) {
		this.scTotalNetQty = scTotalNetQty;
	}

	public float getScTotalNetAmt() {
		return scTotalNetAmt;
	}

	public void setScTotalNetAmt(float scTotalNetAmt) {
		this.scTotalNetAmt = scTotalNetAmt;
	}

	public float getScTotalRetAmtPer() {
		return scTotalRetAmtPer;
	}

	public void setScTotalRetAmtPer(float scTotalRetAmtPer) {
		this.scTotalRetAmtPer = scTotalRetAmtPer;
	}

	public float getScTotalSoldTaxableAmt() {
		return scTotalSoldTaxableAmt;
	}

	public void setScTotalSoldTaxableAmt(float scTotalSoldTaxableAmt) {
		this.scTotalSoldTaxableAmt = scTotalSoldTaxableAmt;
	}

	public float getScTotalVarTaxableAmt() {
		return scTotalVarTaxableAmt;
	}

	public void setScTotalVarTaxableAmt(float scTotalVarTaxableAmt) {
		this.scTotalVarTaxableAmt = scTotalVarTaxableAmt;
	}

	public float getScTotalRetTaxableAmt() {
		return scTotalRetTaxableAmt;
	}

	public void setScTotalRetTaxableAmt(float scTotalRetTaxableAmt) {
		this.scTotalRetTaxableAmt = scTotalRetTaxableAmt;
	}

	public float getScTotalNetTaxableAmt() {
		return scTotalNetTaxableAmt;
	}

	public void setScTotalNetTaxableAmt(float scTotalNetTaxableAmt) {
		this.scTotalNetTaxableAmt = scTotalNetTaxableAmt;
	}

	public float getScTotalRetTaxableAmtPer() {
		return scTotalRetTaxableAmtPer;
	}

	public void setScTotalRetTaxableAmtPer(float scTotalRetTaxableAmtPer) {
		this.scTotalRetTaxableAmtPer = scTotalRetTaxableAmtPer;
	}

	@Override
	public String toString() {
		return "TempSubCatList [subCatId=" + subCatId + ", subCatName=" + subCatName + ", tempItemList=" + tempItemList
				+ "]";
	}

}
