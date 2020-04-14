package com.ats.adminpanel.model;

import java.util.List;
import java.util.TreeSet;

public class SpKgSummaryDaoResponse {

	List<SpKgSummaryDao> summaryDaoList;
	
	 TreeSet<Float> kgList;

	public List<SpKgSummaryDao> getSummaryDaoList() {
		return summaryDaoList;
	}

	public void setSummaryDaoList(List<SpKgSummaryDao> summaryDaoList) {
		this.summaryDaoList = summaryDaoList;
	}

	public TreeSet<Float> getKgList() {
		return kgList;
	}

	public void setKgList(TreeSet<Float> kgList) {
		this.kgList = kgList;
	}

	@Override
	public String toString() {
		return "SpKgSummaryDaoResponse [summaryDaoList=" + summaryDaoList + ", kgList=" + kgList + "]";
	}
	 
	 
		
}
