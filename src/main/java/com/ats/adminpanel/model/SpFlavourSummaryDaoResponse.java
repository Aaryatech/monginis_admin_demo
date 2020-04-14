package com.ats.adminpanel.model;

import java.util.List;
import java.util.TreeSet;

public class SpFlavourSummaryDaoResponse {

	List<SpFlavourSummaryDao> summaryDaoList;
	
	 TreeSet<Integer> flavourIdList;

	public List<SpFlavourSummaryDao> getSummaryDaoList() {
		return summaryDaoList;
	}

	public void setSummaryDaoList(List<SpFlavourSummaryDao> summaryDaoList) {
		this.summaryDaoList = summaryDaoList;
	}

	public TreeSet<Integer> getFlavourIdList() {
		return flavourIdList;
	}

	public void setFlavourIdList(TreeSet<Integer> flavourIdList) {
		this.flavourIdList = flavourIdList;
	}

	@Override
	public String toString() {
		return "SpFlavourSummaryDaoResponse [summaryDaoList=" + summaryDaoList + ", flavourIdList=" + flavourIdList
				+ "]";
	}
	 
	 
	 
}
