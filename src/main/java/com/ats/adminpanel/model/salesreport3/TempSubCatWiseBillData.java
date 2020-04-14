package com.ats.adminpanel.model.salesreport3;

public class TempSubCatWiseBillData {

	private int subCatId;
	private String subCatName;
	
	private int soldQty;
	private float soldAmt;
	private float taxableAmt;
	private float taxAmt;

	private int varQty;
	private float varAmt;
	private float varTaxableAmt;
	private float varTaxAmt;

	private int retQty;
	private float retAmt;
	private float retTaxableAmt;
	private float retTaxAmt;
	
	
	
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
	public int getSoldQty() {
		return soldQty;
	}
	public void setSoldQty(int soldQty) {
		this.soldQty = soldQty;
	}
	public float getSoldAmt() {
		return soldAmt;
	}
	public void setSoldAmt(float soldAmt) {
		this.soldAmt = soldAmt;
	}
	public float getTaxableAmt() {
		return taxableAmt;
	}
	public void setTaxableAmt(float taxableAmt) {
		this.taxableAmt = taxableAmt;
	}
	public float getTaxAmt() {
		return taxAmt;
	}
	public void setTaxAmt(float taxAmt) {
		this.taxAmt = taxAmt;
	}
	public int getVarQty() {
		return varQty;
	}
	public void setVarQty(int varQty) {
		this.varQty = varQty;
	}
	public float getVarAmt() {
		return varAmt;
	}
	public void setVarAmt(float varAmt) {
		this.varAmt = varAmt;
	}
	public float getVarTaxableAmt() {
		return varTaxableAmt;
	}
	public void setVarTaxableAmt(float varTaxableAmt) {
		this.varTaxableAmt = varTaxableAmt;
	}
	public float getVarTaxAmt() {
		return varTaxAmt;
	}
	public void setVarTaxAmt(float varTaxAmt) {
		this.varTaxAmt = varTaxAmt;
	}
	public int getRetQty() {
		return retQty;
	}
	public void setRetQty(int retQty) {
		this.retQty = retQty;
	}
	public float getRetAmt() {
		return retAmt;
	}
	public void setRetAmt(float retAmt) {
		this.retAmt = retAmt;
	}
	public float getRetTaxableAmt() {
		return retTaxableAmt;
	}
	public void setRetTaxableAmt(float retTaxableAmt) {
		this.retTaxableAmt = retTaxableAmt;
	}
	public float getRetTaxAmt() {
		return retTaxAmt;
	}
	public void setRetTaxAmt(float retTaxAmt) {
		this.retTaxAmt = retTaxAmt;
	}
	@Override
	public String toString() {
		return "SubCatWiseBillData [subCatId=" + subCatId + ", subCatName=" + subCatName + ", soldQty=" + soldQty
				+ ", soldAmt=" + soldAmt + ", taxableAmt=" + taxableAmt + ", taxAmt=" + taxAmt + ", varQty=" + varQty
				+ ", varAmt=" + varAmt + ", varTaxableAmt=" + varTaxableAmt + ", varTaxAmt=" + varTaxAmt + ", retQty="
				+ retQty + ", retAmt=" + retAmt + ", retTaxableAmt=" + retTaxableAmt + ", retTaxAmt=" + retTaxAmt + "]";
	}
	
}
