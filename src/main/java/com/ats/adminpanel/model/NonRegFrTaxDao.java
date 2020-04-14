package com.ats.adminpanel.model;

public class NonRegFrTaxDao {

    private String uid;
	
	private int frId;
	
	private String frName;
	
	private float taxPer;
	
	private float billTaxableAmt;
	
	private float billSgstAmt;
	
	private float billCgstAmt;
	
	private float billIgstAmt;
	
	private float billGrandAmt;
	
    private float crnTaxableAmt;
	
	private float crnSgstAmt;
	
	private float crnCgstAmt;
	
	private float crnIgstAmt;
	
	private float crnGrandAmt;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
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

	public float getTaxPer() {
		return taxPer;
	}

	public void setTaxPer(float taxPer) {
		this.taxPer = taxPer;
	}

	public float getBillTaxableAmt() {
		return billTaxableAmt;
	}

	public void setBillTaxableAmt(float billTaxableAmt) {
		this.billTaxableAmt = billTaxableAmt;
	}

	public float getBillSgstAmt() {
		return billSgstAmt;
	}

	public void setBillSgstAmt(float billSgstAmt) {
		this.billSgstAmt = billSgstAmt;
	}

	public float getBillCgstAmt() {
		return billCgstAmt;
	}

	public void setBillCgstAmt(float billCgstAmt) {
		this.billCgstAmt = billCgstAmt;
	}

	public float getBillIgstAmt() {
		return billIgstAmt;
	}

	public void setBillIgstAmt(float billIgstAmt) {
		this.billIgstAmt = billIgstAmt;
	}

	public float getBillGrandAmt() {
		return billGrandAmt;
	}

	public void setBillGrandAmt(float billGrandAmt) {
		this.billGrandAmt = billGrandAmt;
	}

	public float getCrnTaxableAmt() {
		return crnTaxableAmt;
	}

	public void setCrnTaxableAmt(float crnTaxableAmt) {
		this.crnTaxableAmt = crnTaxableAmt;
	}

	public float getCrnSgstAmt() {
		return crnSgstAmt;
	}

	public void setCrnSgstAmt(float crnSgstAmt) {
		this.crnSgstAmt = crnSgstAmt;
	}

	public float getCrnCgstAmt() {
		return crnCgstAmt;
	}

	public void setCrnCgstAmt(float crnCgstAmt) {
		this.crnCgstAmt = crnCgstAmt;
	}

	public float getCrnIgstAmt() {
		return crnIgstAmt;
	}

	public void setCrnIgstAmt(float crnIgstAmt) {
		this.crnIgstAmt = crnIgstAmt;
	}

	public float getCrnGrandAmt() {
		return crnGrandAmt;
	}

	public void setCrnGrandAmt(float crnGrandAmt) {
		this.crnGrandAmt = crnGrandAmt;
	}

	@Override
	public String toString() {
		return "NonRegFrTaxDao [uid=" + uid + ", frId=" + frId + ", frName=" + frName + ", taxPer=" + taxPer
				+ ", billTaxableAmt=" + billTaxableAmt + ", billSgstAmt=" + billSgstAmt + ", billCgstAmt=" + billCgstAmt
				+ ", billIgstAmt=" + billIgstAmt + ", billGrandAmt=" + billGrandAmt + ", crnTaxableAmt=" + crnTaxableAmt
				+ ", crnSgstAmt=" + crnSgstAmt + ", crnCgstAmt=" + crnCgstAmt + ", crnIgstAmt=" + crnIgstAmt
				+ ", crnGrandAmt=" + crnGrandAmt + "]";
	}
	
	
}
