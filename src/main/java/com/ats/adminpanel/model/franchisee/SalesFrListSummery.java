package com.ats.adminpanel.model.franchisee;

import java.util.List;

import com.ats.adminpanel.model.AllFrIdName;

public class SalesFrListSummery {
	List<SalesReportFranchisee> salesReportFranchiseeList;
	List<AllFrIdName> frIdNamesList;

	public List<SalesReportFranchisee> getSalesReportFranchiseeList() {
		return salesReportFranchiseeList;
	}

	public void setSalesReportFranchiseeList(List<SalesReportFranchisee> salesReportFranchiseeList) {
		this.salesReportFranchiseeList = salesReportFranchiseeList;
	}

	public List<AllFrIdName> getFrIdNamesList() {
		return frIdNamesList;
	}

	public void setFrIdNamesList(List<AllFrIdName> frIdNamesList) {
		this.frIdNamesList = frIdNamesList;
	}

	@Override
	public String toString() {
		return "SalesFrListSummery [salesReportFranchiseeList=" + salesReportFranchiseeList + ", frIdNamesList="
				+ frIdNamesList + "]";
	}

}
