package com.ats.adminpanel.model.franchisee;

public class SalesReportFranchisee {

	private String id;

	private int billNo;

	private String type;

	private String invoiceNo;

	private String frName;

	private String billDate;

	private int frId;

	private float grandTotal;

	private float taxableAmt;

	private float totalTax;

	private String orderRef;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getFrName() {
		return frName;
	}

	public void setFrName(String frName) {
		this.frName = frName;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public int getFrId() {
		return frId;
	}

	public void setFrId(int frId) {
		this.frId = frId;
	}

	public float getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(float grandTotal) {
		this.grandTotal = grandTotal;
	}

	public float getTaxableAmt() {
		return taxableAmt;
	}

	public void setTaxableAmt(float taxableAmt) {
		this.taxableAmt = taxableAmt;
	}

	public float getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(float totalTax) {
		this.totalTax = totalTax;
	}

	public String getOrderRef() {
		return orderRef;
	}

	public void setOrderRef(String orderRef) {
		this.orderRef = orderRef;
	}

	@Override
	public String toString() {
		return "SalesReportFranchisee [id=" + id + ", billNo=" + billNo + ", type=" + type + ", invoiceNo=" + invoiceNo
				+ ", frName=" + frName + ", billDate=" + billDate + ", frId=" + frId + ", grandTotal=" + grandTotal
				+ ", taxableAmt=" + taxableAmt + ", totalTax=" + totalTax + ", orderRef=" + orderRef + "]";
	}

}
