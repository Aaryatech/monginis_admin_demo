package com.ats.adminpanel.model;

public class RouteMgmt {

	private int routeTrayId;

	private String routeName;

	private int routeType;

	private String frIds;

	private int srNo;

	private int isSameDay;

	private int delStatus;

	private int isActive;

	private int exInt1;

	private int exInt2;

	private String exVar1;

	private String exVar2;

	public int getRouteTrayId() {
		return routeTrayId;
	}

	public void setRouteTrayId(int routeTrayId) {
		this.routeTrayId = routeTrayId;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public int getRouteType() {
		return routeType;
	}

	public void setRouteType(int routeType) {
		this.routeType = routeType;
	}

	public String getFrIds() {
		return frIds;
	}

	public void setFrIds(String frIds) {
		this.frIds = frIds;
	}

	public int getSrNo() {
		return srNo;
	}

	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}

	public int getIsSameDay() {
		return isSameDay;
	}

	public void setIsSameDay(int isSameDay) {
		this.isSameDay = isSameDay;
	}

	public int getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(int delStatus) {
		this.delStatus = delStatus;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public int getExInt1() {
		return exInt1;
	}

	public void setExInt1(int exInt1) {
		this.exInt1 = exInt1;
	}

	public int getExInt2() {
		return exInt2;
	}

	public void setExInt2(int exInt2) {
		this.exInt2 = exInt2;
	}

	public String getExVar1() {
		return exVar1;
	}

	public void setExVar1(String exVar1) {
		this.exVar1 = exVar1;
	}

	public String getExVar2() {
		return exVar2;
	}

	public void setExVar2(String exVar2) {
		this.exVar2 = exVar2;
	}

	@Override
	public String toString() {
		return "RouteMgmt [routeTrayId=" + routeTrayId + ", routeName=" + routeName + ", routeType=" + routeType
				+ ", frIds=" + frIds + ", srNo=" + srNo + ", isSameDay=" + isSameDay + ", delStatus=" + delStatus
				+ ", isActive=" + isActive + ", exInt1=" + exInt1 + ", exInt2=" + exInt2 + ", exVar1=" + exVar1
				+ ", exVar2=" + exVar2 + "]";
	}

}
