package com.ats.adminpanel.model;
 

public class CreditNoteReport {
	
	private int crndId;
	private int crnId;
	private int billNo;
	private String billDate;
	private int itemId;
	private int grnGvnId;
	private String grnGvnDate;
	private float grnGvnQty;
	private float taxableAmt;
	private float totalTax;
	private float grnGvnAmt;
	private float cgstPer;
	private float sgstPer;
	private float igstPer;
	private float baseRate;
	private String refInvoiceNo;
	private String grngvnSrno;
	private String crnDate;
	private String crnNo;
	private String itemName;
	private String frName;
	private int frId;
	private String itemUom;
	private float peneltyAmt;
	
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
	public int getBillNo() {
		return billNo;
	}
	public void setBillNo(int billNo) {
		this.billNo = billNo;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public int getGrnGvnId() {
		return grnGvnId;
	}
	public void setGrnGvnId(int grnGvnId) {
		this.grnGvnId = grnGvnId;
	}
	public String getGrnGvnDate() {
		return grnGvnDate;
	}
	public void setGrnGvnDate(String grnGvnDate) {
		this.grnGvnDate = grnGvnDate;
	}
	public float getGrnGvnQty() {
		return grnGvnQty;
	}
	public void setGrnGvnQty(float grnGvnQty) {
		this.grnGvnQty = grnGvnQty;
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
	public float getGrnGvnAmt() {
		return grnGvnAmt;
	}
	public void setGrnGvnAmt(float grnGvnAmt) {
		this.grnGvnAmt = grnGvnAmt;
	}
	public float getCgstPer() {
		return cgstPer;
	}
	public void setCgstPer(float cgstPer) {
		this.cgstPer = cgstPer;
	}
	public float getSgstPer() {
		return sgstPer;
	}
	public void setSgstPer(float sgstPer) {
		this.sgstPer = sgstPer;
	}
	public float getIgstPer() {
		return igstPer;
	}
	public void setIgstPer(float igstPer) {
		this.igstPer = igstPer;
	}
	public float getBaseRate() {
		return baseRate;
	}
	public void setBaseRate(float baseRate) {
		this.baseRate = baseRate;
	}
	public String getRefInvoiceNo() {
		return refInvoiceNo;
	}
	public void setRefInvoiceNo(String refInvoiceNo) {
		this.refInvoiceNo = refInvoiceNo;
	}
	public String getGrngvnSrno() {
		return grngvnSrno;
	}
	public void setGrngvnSrno(String grngvnSrno) {
		this.grngvnSrno = grngvnSrno;
	}
	public String getCrnDate() {
		return crnDate;
	}
	public void setCrnDate(String crnDate) {
		this.crnDate = crnDate;
	}
	public String getCrnNo() {
		return crnNo;
	}
	public void setCrnNo(String crnNo) {
		this.crnNo = crnNo;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getFrName() {
		return frName;
	}
	public void setFrName(String frName) {
		this.frName = frName;
	}
	public int getFrId() {
		return frId;
	}
	public void setFrId(int frId) {
		this.frId = frId;
	}
	public float getPeneltyAmt() {
		return peneltyAmt;
	}
	public void setPeneltyAmt(float peneltyAmt) {
		this.peneltyAmt = peneltyAmt;
	}
	public String getItemUom() {
		return itemUom;
	}
	public void setItemUom(String itemUom) {
		this.itemUom = itemUom;
	}
	@Override
	public String toString() {
		return "CreditNoteReport [crndId=" + crndId + ", crnId=" + crnId + ", billNo=" + billNo + ", billDate="
				+ billDate + ", itemId=" + itemId + ", grnGvnId=" + grnGvnId + ", grnGvnDate=" + grnGvnDate
				+ ", grnGvnQty=" + grnGvnQty + ", taxableAmt=" + taxableAmt + ", totalTax=" + totalTax + ", grnGvnAmt="
				+ grnGvnAmt + ", cgstPer=" + cgstPer + ", sgstPer=" + sgstPer + ", igstPer=" + igstPer + ", baseRate="
				+ baseRate + ", refInvoiceNo=" + refInvoiceNo + ", grngvnSrno=" + grngvnSrno + ", crnDate=" + crnDate
				+ ", crnNo=" + crnNo + ", itemName=" + itemName + ", frName=" + frName + ", frId=" + frId + ", itemUom="
				+ itemUom + ", peneltyAmt=" + peneltyAmt + "]";
	}
	
	

}
