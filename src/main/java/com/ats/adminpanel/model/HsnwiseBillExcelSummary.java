package com.ats.adminpanel.model;

public class HsnwiseBillExcelSummary{

	private String id;
	
	private int billNo;
	
	private String invoiceNo;
	
	private String partyName;
	
	private String partyGstin;
	
	private String billDate;
	
	private float invoiceTotal;
	
	private String itemHsncd;
	
	private float qty;
	
	private float taxableAmt;
	
	private float taxRate;
	
	private float sgstRs;
	
	private float cgstRs;
	
	private float igstRs;
	
	private float cessPer;
	
	private float cessRs;
	
	private float totalTax;
	
	private float grandTotal;
	
	private String state;
	
	private String country;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getBillNo() {
		return billNo;
	}

	public void setBillNo(int billNo) {
		this.billNo = billNo;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getPartyName() {
		return partyName;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	public String getPartyGstin() {
		return partyGstin;
	}

	public void setPartyGstin(String partyGstin) {
		this.partyGstin = partyGstin;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public float getInvoiceTotal() {
		return invoiceTotal;
	}

	public void setInvoiceTotal(float invoiceTotal) {
		this.invoiceTotal = invoiceTotal;
	}

	public String getItemHsncd() {
		return itemHsncd;
	}

	public void setItemHsncd(String itemHsncd) {
		this.itemHsncd = itemHsncd;
	}

	public float getQty() {
		return qty;
	}

	public void setQty(float qty) {
		this.qty = qty;
	}

	public float getTaxableAmt() {
		return taxableAmt;
	}

	public void setTaxableAmt(float taxableAmt) {
		this.taxableAmt = taxableAmt;
	}

	public float getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(float taxRate) {
		this.taxRate = taxRate;
	}

	public float getSgstRs() {
		return sgstRs;
	}

	public void setSgstRs(float sgstRs) {
		this.sgstRs = sgstRs;
	}

	public float getCgstRs() {
		return cgstRs;
	}

	public void setCgstRs(float cgstRs) {
		this.cgstRs = cgstRs;
	}

	public float getIgstRs() {
		return igstRs;
	}

	public void setIgstRs(float igstRs) {
		this.igstRs = igstRs;
	}

	public float getCessPer() {
		return cessPer;
	}

	public void setCessPer(float cessPer) {
		this.cessPer = cessPer;
	}

	public float getCessRs() {
		return cessRs;
	}

	public void setCessRs(float cessRs) {
		this.cessRs = cessRs;
	}

	public float getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(float totalTax) {
		this.totalTax = totalTax;
	}

	public float getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(float grandTotal) {
		this.grandTotal = grandTotal;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "HsnwiseBillExcelSummary [id=" + id + ", billNo=" + billNo + ", invoiceNo=" + invoiceNo + ", partyName="
				+ partyName + ", partyGstin=" + partyGstin + ", billDate=" + billDate + ", invoiceTotal=" + invoiceTotal
				+ ", itemHsncd=" + itemHsncd + ", qty=" + qty + ", taxableAmt=" + taxableAmt + ", taxRate=" + taxRate
				+ ", sgstRs=" + sgstRs + ", cgstRs=" + cgstRs + ", igstRs=" + igstRs + ", cessPer=" + cessPer
				+ ", cessRs=" + cessRs + ", totalTax=" + totalTax + ", grandTotal=" + grandTotal + ", state=" + state
				+ ", country=" + country + "]";
	}
	
	
}
