package com.ats.adminpanel.model.dashboard;

public class AdminDashYearlyReport {
	private String id;
	private String monthStr;
	private int month;
	private int year;

	private float sale;

	private float grn1;
	private float grn2;
	private float grn3;
	private float grnTotal;

	private float gvn;
	private float net;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMonthStr() {
		return monthStr;
	}

	public void setMonthStr(String monthStr) {
		this.monthStr = monthStr;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public float getSale() {
		return sale;
	}

	public void setSale(float sale) {
		this.sale = sale;
	}

	public float getGrn1() {
		return grn1;
	}

	public void setGrn1(float grn1) {
		this.grn1 = grn1;
	}

	public float getGrn2() {
		return grn2;
	}

	public void setGrn2(float grn2) {
		this.grn2 = grn2;
	}

	public float getGrn3() {
		return grn3;
	}

	public void setGrn3(float grn3) {
		this.grn3 = grn3;
	}

	public float getGrnTotal() {
		return grnTotal;
	}

	public void setGrnTotal(float grnTotal) {
		this.grnTotal = grnTotal;
	}

	public float getGvn() {
		return gvn;
	}

	public void setGvn(float gvn) {
		this.gvn = gvn;
	}

	public float getNet() {
		return net;
	}

	public void setNet(float net) {
		this.net = net;
	}

	@Override
	public String toString() {
		return "AdminDashYearlyReport [id=" + id + ", monthStr=" + monthStr + ", month=" + month + ", year=" + year
				+ ", sale=" + sale + ", grn1=" + grn1 + ", grn2=" + grn2 + ", grn3=" + grn3 + ", grnTotal=" + grnTotal
				+ ", gvn=" + gvn + ", net=" + net + "]";
	}
}
