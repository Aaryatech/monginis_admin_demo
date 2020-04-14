package com.ats.adminpanel.model.salesreport3;

public class ItemWiseFrList {

	private int itemId;
	private int frId;
	private String frName;
	
	private float frSoldAmt;
	private int frSoldQty;
	
	private float frTaxableAmt;
	private float frTotalTax;
	
	
	private int frVarQty;
	private float frVarAmt;

	private float frVarTaxableAmt;
	private float frVarTax;

	private int frRetQty;
	private float frRetAmt;
	
	private float frRetTaxableAmt;
	private float frRetTax;
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
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
	public float getFrSoldAmt() {
		return frSoldAmt;
	}
	public void setFrSoldAmt(float frSoldAmt) {
		this.frSoldAmt = frSoldAmt;
	}
	public int getFrSoldQty() {
		return frSoldQty;
	}
	public void setFrSoldQty(int frSoldQty) {
		this.frSoldQty = frSoldQty;
	}
	public float getFrTaxableAmt() {
		return frTaxableAmt;
	}
	public void setFrTaxableAmt(float frTaxableAmt) {
		this.frTaxableAmt = frTaxableAmt;
	}
	public float getFrTotalTax() {
		return frTotalTax;
	}
	public void setFrTotalTax(float frTotalTax) {
		this.frTotalTax = frTotalTax;
	}
	public int getFrVarQty() {
		return frVarQty;
	}
	public void setFrVarQty(int frVarQty) {
		this.frVarQty = frVarQty;
	}
	public float getFrVarAmt() {
		return frVarAmt;
	}
	public void setFrVarAmt(float frVarAmt) {
		this.frVarAmt = frVarAmt;
	}
	public float getFrVarTaxableAmt() {
		return frVarTaxableAmt;
	}
	public void setFrVarTaxableAmt(float frVarTaxableAmt) {
		this.frVarTaxableAmt = frVarTaxableAmt;
	}
	public float getFrVarTax() {
		return frVarTax;
	}
	public void setFrVarTax(float frVarTax) {
		this.frVarTax = frVarTax;
	}
	public int getFrRetQty() {
		return frRetQty;
	}
	public void setFrRetQty(int frRetQty) {
		this.frRetQty = frRetQty;
	}
	public float getFrRetAmt() {
		return frRetAmt;
	}
	public void setFrRetAmt(float frRetAmt) {
		this.frRetAmt = frRetAmt;
	}
	public float getFrRetTaxableAmt() {
		return frRetTaxableAmt;
	}
	public void setFrRetTaxableAmt(float frRetTaxableAmt) {
		this.frRetTaxableAmt = frRetTaxableAmt;
	}
	public float getFrRetTax() {
		return frRetTax;
	}
	public void setFrRetTax(float frRetTax) {
		this.frRetTax = frRetTax;
	}
	@Override
	public String toString() {
		return "ItemWiseFrList [itemId=" + itemId + ", frId=" + frId + ", frName=" + frName + ", frSoldAmt=" + frSoldAmt
				+ ", frSoldQty=" + frSoldQty + ", frTaxableAmt=" + frTaxableAmt + ", frTotalTax=" + frTotalTax
				+ ", frVarQty=" + frVarQty + ", frVarAmt=" + frVarAmt + ", frVarTaxableAmt=" + frVarTaxableAmt
				+ ", frVarTax=" + frVarTax + ", frRetQty=" + frRetQty + ", frRetAmt=" + frRetAmt + ", frRetTaxableAmt="
				+ frRetTaxableAmt + ", frRetTax=" + frRetTax + "]";
	}
	
	
	
}
