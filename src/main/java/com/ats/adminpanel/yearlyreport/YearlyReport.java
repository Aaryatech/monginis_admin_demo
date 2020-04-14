package com.ats.adminpanel.yearlyreport;

import java.util.List;


public class YearlyReport {

	
	private int monthId;
	private String yearId;
	private String dateStr;
	
	private float monthTotalSoldQty;
	private float monthTotalSoldAmt;
	private float monthTotalVarQty;
	private float monthTotalVarAmt;
	private float monthTotalRetQty;
	private float monthTotalRetAmt;
	private float monthTotalNetQty;
	private float monthTotalNetAmt;
	private float monthTotalRetAmtPer;
	
	private float monthTotalSoldTaxableAmt;
	private float monthTotalVarTaxableAmt;
	private float monthTotalRetTaxableAmt;
	private float monthTotalNetTaxableAmt;
	private float monthTotalRetTaxableAmtPer;
	
	List<TempFrList> frList=null;

	public int getMonthId() {
		return monthId;
	}

	public void setMonthId(int monthId) {
		this.monthId = monthId;
	}

	public String getYearId() {
		return yearId;
	}

	public void setYearId(String yearId) {
		this.yearId = yearId;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public List<TempFrList> getFrList() {
		return frList;
	}

	public void setFrList(List<TempFrList> frList) {
		this.frList = frList;
	}
	
	
	
	

	public float getMonthTotalSoldQty() {
		return monthTotalSoldQty;
	}

	public void setMonthTotalSoldQty(float monthTotalSoldQty) {
		this.monthTotalSoldQty = monthTotalSoldQty;
	}

	public float getMonthTotalSoldAmt() {
		return monthTotalSoldAmt;
	}

	public void setMonthTotalSoldAmt(float monthTotalSoldAmt) {
		this.monthTotalSoldAmt = monthTotalSoldAmt;
	}

	public float getMonthTotalVarQty() {
		return monthTotalVarQty;
	}

	public void setMonthTotalVarQty(float monthTotalVarQty) {
		this.monthTotalVarQty = monthTotalVarQty;
	}

	public float getMonthTotalVarAmt() {
		return monthTotalVarAmt;
	}

	public void setMonthTotalVarAmt(float monthTotalVarAmt) {
		this.monthTotalVarAmt = monthTotalVarAmt;
	}

	public float getMonthTotalRetQty() {
		return monthTotalRetQty;
	}

	public void setMonthTotalRetQty(float monthTotalRetQty) {
		this.monthTotalRetQty = monthTotalRetQty;
	}

	public float getMonthTotalRetAmt() {
		return monthTotalRetAmt;
	}

	public void setMonthTotalRetAmt(float monthTotalRetAmt) {
		this.monthTotalRetAmt = monthTotalRetAmt;
	}

	public float getMonthTotalNetQty() {
		return monthTotalNetQty;
	}

	public void setMonthTotalNetQty(float monthTotalNetQty) {
		this.monthTotalNetQty = monthTotalNetQty;
	}

	public float getMonthTotalNetAmt() {
		return monthTotalNetAmt;
	}

	public void setMonthTotalNetAmt(float monthTotalNetAmt) {
		this.monthTotalNetAmt = monthTotalNetAmt;
	}

	public float getMonthTotalRetAmtPer() {
		return monthTotalRetAmtPer;
	}

	public void setMonthTotalRetAmtPer(float monthTotalRetAmtPer) {
		this.monthTotalRetAmtPer = monthTotalRetAmtPer;
	}

	public float getMonthTotalSoldTaxableAmt() {
		return monthTotalSoldTaxableAmt;
	}

	public void setMonthTotalSoldTaxableAmt(float monthTotalSoldTaxableAmt) {
		this.monthTotalSoldTaxableAmt = monthTotalSoldTaxableAmt;
	}

	public float getMonthTotalVarTaxableAmt() {
		return monthTotalVarTaxableAmt;
	}

	public void setMonthTotalVarTaxableAmt(float monthTotalVarTaxableAmt) {
		this.monthTotalVarTaxableAmt = monthTotalVarTaxableAmt;
	}

	public float getMonthTotalRetTaxableAmt() {
		return monthTotalRetTaxableAmt;
	}

	public void setMonthTotalRetTaxableAmt(float monthTotalRetTaxableAmt) {
		this.monthTotalRetTaxableAmt = monthTotalRetTaxableAmt;
	}

	public float getMonthTotalNetTaxableAmt() {
		return monthTotalNetTaxableAmt;
	}

	public void setMonthTotalNetTaxableAmt(float monthTotalNetTaxableAmt) {
		this.monthTotalNetTaxableAmt = monthTotalNetTaxableAmt;
	}

	public float getMonthTotalRetTaxableAmtPer() {
		return monthTotalRetTaxableAmtPer;
	}

	public void setMonthTotalRetTaxableAmtPer(float monthTotalRetTaxableAmtPer) {
		this.monthTotalRetTaxableAmtPer = monthTotalRetTaxableAmtPer;
	}

	@Override
	public String toString() {
		return "YearlyReport [monthId=" + monthId + ", yearId=" + yearId + ", dateStr=" + dateStr + ", frList=" + frList
				+ "]";
	}
	
	
	
	
}
