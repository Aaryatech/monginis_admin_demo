package com.ats.adminpanel.model.logistics;

import java.util.Date;

public class GetServHeader {

	private int servId;

	private Date servDate;

	private int typeId;

	private int dealerId;

	private int servType;

	private int servType2;

	private String billNo;

	private Date billDate;

	private int vehId;

	private String vehNo;

	private float taxAmt;

	private float taxableAmt;

	private float total;

	public int getServId() {
		return servId;
	}

	public void setServId(int servId) {
		this.servId = servId;
	}

	public Date getServDate() {
		return servDate;
	}

	public void setServDate(Date servDate) {
		this.servDate = servDate;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getDealerId() {
		return dealerId;
	}

	public void setDealerId(int dealerId) {
		this.dealerId = dealerId;
	}

	public int getServType() {
		return servType;
	}

	public void setServType(int servType) {
		this.servType = servType;
	}

	public int getServType2() {
		return servType2;
	}

	public void setServType2(int servType2) {
		this.servType2 = servType2;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public int getVehId() {
		return vehId;
	}

	public void setVehId(int vehId) {
		this.vehId = vehId;
	}

	public String getVehNo() {
		return vehNo;
	}

	public void setVehNo(String vehNo) {
		this.vehNo = vehNo;
	}

	public float getTaxAmt() {
		return taxAmt;
	}

	public void setTaxAmt(float taxAmt) {
		this.taxAmt = taxAmt;
	}

	public float getTaxableAmt() {
		return taxableAmt;
	}

	public void setTaxableAmt(float taxableAmt) {
		this.taxableAmt = taxableAmt;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "GetServHeader [servId=" + servId + ", servDate=" + servDate + ", typeId=" + typeId + ", dealerId="
				+ dealerId + ", servType=" + servType + ", servType2=" + servType2 + ", billNo=" + billNo
				+ ", billDate=" + billDate + ", vehId=" + vehId + ", vehNo=" + vehNo + ", taxAmt=" + taxAmt
				+ ", taxableAmt=" + taxableAmt + ", total=" + total + "]";
	}

}
