package com.ats.adminpanel.model;


public class GetFrMenuConfigure {

	private int settingId;
	
	private int frId;
	
	private String frName;

	private int menuId;
	
	private int catId;
	
	private int subCatId;
	
	private String fromTime;
	
	private String toTime;
	
	private int settingType;
	
	private int day;
	
	private int date;
	
	private String menuTitle;
	
	private String catName;

	private int isDel;

	public int getSettingId() {
		return settingId;
	}

	public void setSettingId(int settingId) {
		this.settingId = settingId;
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

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	public int getSubCatId() {
		return subCatId;
	}

	public void setSubCatId(int subCatId) {
		this.subCatId = subCatId;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public int getSettingType() {
		return settingType;
	}

	public void setSettingType(int settingType) {
		this.settingType = settingType;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public String getMenuTitle() {
		return menuTitle;
	}

	public void setMenuTitle(String menuTitle) {
		this.menuTitle = menuTitle;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public int getIsDel() {
		return isDel;
	}

	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}

	@Override
	public String toString() {
		return "GetFrMenuConfigure [settingId=" + settingId + ", frId=" + frId + ", frName=" + frName + ", menuId="
				+ menuId + ", catId=" + catId + ", subCatId=" + subCatId + ", fromTime=" + fromTime + ", toTime="
				+ toTime + ", settingType=" + settingType + ", day=" + day + ", date=" + date + ", menuTitle="
				+ menuTitle + ", catName=" + catName + ", isDel=" + isDel + "]";
	}
	
	
}
