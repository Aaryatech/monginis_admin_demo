package com.ats.adminpanel.model.salesdashboard;

public class FrWiseSaleForDash {

	private int frId;
	private String frName;
	private int routeId;
	private float sale;
	private float saleQty;

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

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

	public float getSale() {
		return sale;
	}

	public void setSale(float sale) {
		this.sale = sale;
	}

	public float getSaleQty() {
		return saleQty;
	}

	public void setSaleQty(float saleQty) {
		this.saleQty = saleQty;
	}

	@Override
	public String toString() {
		return "FrWiseSaleForDash [frId=" + frId + ", frName=" + frName + ", routeId=" + routeId + ", sale=" + sale
				+ ", saleQty=" + saleQty + "]";
	}
	
}
