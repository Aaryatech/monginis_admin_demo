package com.ats.adminpanel.model;
 
public class GrandTotalBillWise {
	
	private int billDetailNo; 
	private String invoiceNo; 
	private String billDate; 
	private String frName; 
	private String frGstNo; 
	private int billNo;  
	private float grandTotal;
	public int getBillDetailNo() {
		return billDetailNo;
	}
	public void setBillDetailNo(int billDetailNo) {
		this.billDetailNo = billDetailNo;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getFrName() {
		return frName;
	}
	public void setFrName(String frName) {
		this.frName = frName;
	}
	public String getFrGstNo() {
		return frGstNo;
	}
	public void setFrGstNo(String frGstNo) {
		this.frGstNo = frGstNo;
	}
	public int getBillNo() {
		return billNo;
	}
	public void setBillNo(int billNo) {
		this.billNo = billNo;
	}
	public float getGrandTotal() {
		return grandTotal;
	}
	public void setGrandTotal(float grandTotal) {
		this.grandTotal = grandTotal;
	}
	@Override
	public String toString() {
		return "GrandTotalBillWise [billDetailNo=" + billDetailNo + ", invoiceNo=" + invoiceNo + ", billDate="
				+ billDate + ", frName=" + frName + ", frGstNo=" + frGstNo + ", billNo=" + billNo + ", grandTotal="
				+ grandTotal + "]";
	}
	
	
	

}
