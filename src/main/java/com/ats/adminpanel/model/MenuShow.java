package com.ats.adminpanel.model;

public class MenuShow {

	private int menuId;

	private int catId;

	private int delStatus;

	private String menuTitle;

	private String menuDesc;

	private String menuImage;

	private String selectedMenuImage;

	private int isSameDayApplicable;

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

	public int getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(int delStatus) {
		this.delStatus = delStatus;
	}

	public String getMenuTitle() {
		return menuTitle;
	}

	public void setMenuTitle(String menuTitle) {
		this.menuTitle = menuTitle;
	}

	public String getMenuDesc() {
		return menuDesc;
	}

	public void setMenuDesc(String menuDesc) {
		this.menuDesc = menuDesc;
	}

	public String getMenuImage() {
		return menuImage;
	}

	public void setMenuImage(String menuImage) {
		this.menuImage = menuImage;
	}

	public String getSelectedMenuImage() {
		return selectedMenuImage;
	}

	public void setSelectedMenuImage(String selectedMenuImage) {
		this.selectedMenuImage = selectedMenuImage;
	}

	public int getIsSameDayApplicable() {
		return isSameDayApplicable;
	}

	public void setIsSameDayApplicable(int isSameDayApplicable) {
		this.isSameDayApplicable = isSameDayApplicable;
	}

	@Override
	public String toString() {
		return "MenuShow [menuId=" + menuId + ", catId=" + catId + ", delStatus=" + delStatus + ", menuTitle="
				+ menuTitle + ", menuDesc=" + menuDesc + ", menuImage=" + menuImage + ", selectedMenuImage="
				+ selectedMenuImage + ", isSameDayApplicable=" + isSameDayApplicable + "]";
	}

}
