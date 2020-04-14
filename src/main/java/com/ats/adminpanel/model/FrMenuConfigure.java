package com.ats.adminpanel.model;

import java.io.Serializable;


public class FrMenuConfigure implements Serializable{

	private int settingId;

	private int frId;

	private int menuId;

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

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public int getIsDel() {
		return isDel;
	}

	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}

	@Override
	public String toString() {
		return "FrMenuConfigure [settingId=" + settingId + ", frId=" + frId + ", menuId=" + menuId + ", isDel=" + isDel
				+ "]";
	}
	
	
}
