package com.ats.adminpanel.model;

import java.util.List; 

public class SectionMaster {
	 
	private int sectionId;  
	private String sectionName;  
	private int delStatus; 
	private String menuIds; 
	List<AllMenus> menuList;
	public int getSectionId() {
		return sectionId;
	}
	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public int getDelStatus() {
		return delStatus;
	}
	public void setDelStatus(int delStatus) {
		this.delStatus = delStatus;
	}
	public String getMenuIds() {
		return menuIds;
	}
	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}
	public List<AllMenus> getMenuList() {
		return menuList;
	}
	public void setMenuList(List<AllMenus> menuList) {
		this.menuList = menuList;
	}
	@Override
	public String toString() {
		return "SectionMaster [sectionId=" + sectionId + ", sectionName=" + sectionName + ", delStatus=" + delStatus
				+ ", menuIds=" + menuIds + ", menuList=" + menuList + "]";
	}
	

}
