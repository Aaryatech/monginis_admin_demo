package com.ats.adminpanel.model;


public class CrnHsnwiseExcelReport{

	private String id;
	
	private int crnId;
	
	private String supplierInvoiceNo;
	
	private String supplierInvoiceDate;
	
	private String invoiceNo;
	
	private String invoiceDate;
	
	private int frId;
	
	private String frName;
	
	private String itemHsncd;
	
	private float qty;
	
	private float taxableAmt;
	
	private float cgstRs;
	
	private float sgstRs;
	
	private float igstRs;
	
	private float cessRs;
	
	private float taxRate;
	
	private float documentAmount;
	
	private String frGstNo;
	
	private String country;

	private String state;

	
	
	public float getCessRs() {
		return cessRs;
	}

	public void setCessRs(float cessRs) {
		this.cessRs = cessRs;
	}

	public int getCrnId() {
		return crnId;
	}

	public void setCrnId(int crnId) {
		this.crnId = crnId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSupplierInvoiceNo() {
		return supplierInvoiceNo;
	}

	public void setSupplierInvoiceNo(String supplierInvoiceNo) {
		this.supplierInvoiceNo = supplierInvoiceNo;
	}

	public String getSupplierInvoiceDate() {
		return supplierInvoiceDate;
	}

	public void setSupplierInvoiceDate(String supplierInvoiceDate) {
		this.supplierInvoiceDate = supplierInvoiceDate;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
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

	public float getCgstRs() {
		return cgstRs;
	}

	public void setCgstRs(float cgstRs) {
		this.cgstRs = cgstRs;
	}

	public float getSgstRs() {
		return sgstRs;
	}

	public void setSgstRs(float sgstRs) {
		this.sgstRs = sgstRs;
	}

	public float getIgstRs() {
		return igstRs;
	}

	public void setIgstRs(float igstRs) {
		this.igstRs = igstRs;
	}

	public float getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(float taxRate) {
		this.taxRate = taxRate;
	}

	public float getDocumentAmount() {
		return documentAmount;
	}

	public void setDocumentAmount(float documentAmount) {
		this.documentAmount = documentAmount;
	}

	public String getFrGstNo() {
		return frGstNo;
	}

	public void setFrGstNo(String frGstNo) {
		this.frGstNo = frGstNo;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "CrnHsnwiseExcelReport [id=" + id + ", crnId=" + crnId + ", supplierInvoiceNo=" + supplierInvoiceNo
				+ ", supplierInvoiceDate=" + supplierInvoiceDate + ", invoiceNo=" + invoiceNo + ", invoiceDate="
				+ invoiceDate + ", frId=" + frId + ", frName=" + frName + ", itemHsncd=" + itemHsncd + ", qty=" + qty
				+ ", taxableAmt=" + taxableAmt + ", cgstRs=" + cgstRs + ", sgstRs=" + sgstRs + ", igstRs=" + igstRs
				+ ", cessRs=" + cessRs + ", taxRate=" + taxRate + ", documentAmount=" + documentAmount + ", frGstNo="
				+ frGstNo + ", country=" + country + ", state=" + state + "]";
	}

	
	
}
