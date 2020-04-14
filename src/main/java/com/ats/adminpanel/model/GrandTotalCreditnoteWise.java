package com.ats.adminpanel.model;

import java.util.Date;

public class GrandTotalCreditnoteWise {
	
	private int crndId;  
	private int crnId;
	private String crnDate;
	private String invoiceNo;
	private String billDate;
	private String frName;
	private String frCode;
	private String frGstNo; 
	private float crnAmt;
	public int getCrndId() {
		return crndId;
	}
	public void setCrndId(int crndId) {
		this.crndId = crndId;
	}
	public int getCrnId() {
		return crnId;
	}
	public void setCrnId(int crnId) {
		this.crnId = crnId;
	}
	public String getCrnDate() {
		return crnDate;
	}
	public void setCrnDate(String crnDate) {
		this.crnDate = crnDate;
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
	public String getFrCode() {
		return frCode;
	}
	public void setFrCode(String frCode) {
		this.frCode = frCode;
	}
	public String getFrGstNo() {
		return frGstNo;
	}
	public void setFrGstNo(String frGstNo) {
		this.frGstNo = frGstNo;
	}
	public float getCrnAmt() {
		return crnAmt;
	}
	public void setCrnAmt(float crnAmt) {
		this.crnAmt = crnAmt;
	}
	@Override
	public String toString() {
		return "GrandTotalCreditnoteWise [crndId=" + crndId + ", crnId=" + crnId + ", crnDate=" + crnDate
				+ ", invoiceNo=" + invoiceNo + ", billDate=" + billDate + ", frName=" + frName + ", frCode=" + frCode
				+ ", frGstNo=" + frGstNo + ", crnAmt=" + crnAmt + "]";
	}
	
	
	

}
