package com.ats.adminpanel.yearlyreport;

import java.util.List;


public class TempFrList {
	
	private int frId;
	private String frName;
	
	private float frTotalSoldQty;
	private float frTotalSoldAmt;
	private float frTotalVarQty;
	private float frTotalVarAmt;
	private float frTotalRetQty;
	private float frTotalRetAmt;
	private float frTotalNetQty;
	private float frTotalNetAmt;
	private float frTotalRetAmtPer;
	
	private float frTotalSoldTaxableAmt;
	private float frTotalVarTaxableAmt;
	private float frTotalRetTaxableAmt;
	private float frTotalNetTaxableAmt;
	private float frTotalRetTaxableAmtPer;
	

	private List<TempSubCatList> tempSubCatList=null;

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

	public List<TempSubCatList> getTempSubCatList() {
		return tempSubCatList;
	}

	public void setTempSubCatList(List<TempSubCatList> tempSubCatList) {
		this.tempSubCatList = tempSubCatList;
	}
	
	

	public float getFrTotalSoldQty() {
		return frTotalSoldQty;
	}

	public void setFrTotalSoldQty(float frTotalSoldQty) {
		this.frTotalSoldQty = frTotalSoldQty;
	}

	public float getFrTotalSoldAmt() {
		return frTotalSoldAmt;
	}

	public void setFrTotalSoldAmt(float frTotalSoldAmt) {
		this.frTotalSoldAmt = frTotalSoldAmt;
	}

	public float getFrTotalVarQty() {
		return frTotalVarQty;
	}

	public void setFrTotalVarQty(float frTotalVarQty) {
		this.frTotalVarQty = frTotalVarQty;
	}

	public float getFrTotalVarAmt() {
		return frTotalVarAmt;
	}

	public void setFrTotalVarAmt(float frTotalVarAmt) {
		this.frTotalVarAmt = frTotalVarAmt;
	}

	public float getFrTotalRetQty() {
		return frTotalRetQty;
	}

	public void setFrTotalRetQty(float frTotalRetQty) {
		this.frTotalRetQty = frTotalRetQty;
	}

	public float getFrTotalRetAmt() {
		return frTotalRetAmt;
	}

	public void setFrTotalRetAmt(float frTotalRetAmt) {
		this.frTotalRetAmt = frTotalRetAmt;
	}

	public float getFrTotalNetQty() {
		return frTotalNetQty;
	}

	public void setFrTotalNetQty(float frTotalNetQty) {
		this.frTotalNetQty = frTotalNetQty;
	}

	public float getFrTotalNetAmt() {
		return frTotalNetAmt;
	}

	public void setFrTotalNetAmt(float frTotalNetAmt) {
		this.frTotalNetAmt = frTotalNetAmt;
	}

	public float getFrTotalRetAmtPer() {
		return frTotalRetAmtPer;
	}

	public void setFrTotalRetAmtPer(float frTotalRetAmtPer) {
		this.frTotalRetAmtPer = frTotalRetAmtPer;
	}

	public float getFrTotalSoldTaxableAmt() {
		return frTotalSoldTaxableAmt;
	}

	public void setFrTotalSoldTaxableAmt(float frTotalSoldTaxableAmt) {
		this.frTotalSoldTaxableAmt = frTotalSoldTaxableAmt;
	}

	public float getFrTotalVarTaxableAmt() {
		return frTotalVarTaxableAmt;
	}

	public void setFrTotalVarTaxableAmt(float frTotalVarTaxableAmt) {
		this.frTotalVarTaxableAmt = frTotalVarTaxableAmt;
	}

	public float getFrTotalRetTaxableAmt() {
		return frTotalRetTaxableAmt;
	}

	public void setFrTotalRetTaxableAmt(float frTotalRetTaxableAmt) {
		this.frTotalRetTaxableAmt = frTotalRetTaxableAmt;
	}

	public float getFrTotalNetTaxableAmt() {
		return frTotalNetTaxableAmt;
	}

	public void setFrTotalNetTaxableAmt(float frTotalNetTaxableAmt) {
		this.frTotalNetTaxableAmt = frTotalNetTaxableAmt;
	}

	public float getFrTotalRetTaxableAmtPer() {
		return frTotalRetTaxableAmtPer;
	}

	public void setFrTotalRetTaxableAmtPer(float frTotalRetTaxableAmtPer) {
		this.frTotalRetTaxableAmtPer = frTotalRetTaxableAmtPer;
	}

	@Override
	public String toString() {
		return "TempFrList [frId=" + frId + ", frName=" + frName + ", tempSubCatList=" + tempSubCatList + "]";
	}
	

}
