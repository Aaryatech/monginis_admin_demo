package com.ats.adminpanel.model.salesdashboard;

import java.util.List;


public class CatWiseAllData {

	private List<CatWiseSaleTotal> saleList;
	private List<CatWiseSaleTotal> crnList;
	private List<CatWiseSaleTotal> netList;

	public List<CatWiseSaleTotal> getSaleList() {
		return saleList;
	}

	public void setSaleList(List<CatWiseSaleTotal> saleList) {
		this.saleList = saleList;
	}

	public List<CatWiseSaleTotal> getCrnList() {
		return crnList;
	}

	public void setCrnList(List<CatWiseSaleTotal> crnList) {
		this.crnList = crnList;
	}

	public List<CatWiseSaleTotal> getNetList() {
		return netList;
	}

	public void setNetList(List<CatWiseSaleTotal> netList) {
		this.netList = netList;
	}

	@Override
	public String toString() {
		return "CatWiseAllData [saleList=" + saleList + ", crnList=" + crnList + ", netList=" + netList + "]";
	}
}
